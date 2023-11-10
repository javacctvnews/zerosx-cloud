package com.zerosx.sas.service.impl;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.enums.GranterTypeEnum;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.EasyTransUtils;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.utils.IpUtils;
import com.zerosx.sas.dto.OauthTokenRecordPageDTO;
import com.zerosx.sas.entity.OauthTokenRecord;
import com.zerosx.sas.mapper.IOauthTokenRecordMapper;
import com.zerosx.sas.service.IOauthTokenRecordService;
import com.zerosx.sas.vo.OauthTokenRecordPageVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 登录日志
 *
 * @author javacctvnews
 * @Description
 * @date 2023-04-09 15:33:32
 */
@Slf4j
@Service
public class OauthTokenRecordServiceImpl extends SuperServiceImpl<IOauthTokenRecordMapper, OauthTokenRecord> implements IOauthTokenRecordService {

    @Override
    public CustomPageVO<OauthTokenRecordPageVO> pageList(RequestVO<OauthTokenRecordPageDTO> requestVO, boolean searchCount) {
        IPage<OauthTokenRecordPageVO> page = baseMapper.selectPage(PageUtils.of(requestVO, searchCount), lambdaQW(requestVO.getT()))
                .convert((e) -> {
                    OauthTokenRecordPageVO pageVO = EasyTransUtils.copyTrans(e, OauthTokenRecordPageVO.class);
                    if (StringUtils.isBlank(pageVO.getSourceLocation())) {
                        pageVO.setSourceLocation(IpUtils.getIpLocation(pageVO.getSourceIp()));
                        LambdaUpdateWrapper<OauthTokenRecord> qw = Wrappers.lambdaUpdate(OauthTokenRecord.class);
                        qw.set(OauthTokenRecord::getSourceLocation, pageVO.getSourceLocation());
                        qw.eq(OauthTokenRecord::getSourceIp, e.getSourceIp());
                        update(qw);
                    }
                    return pageVO;
                });
        return PageUtils.of(page);
    }

    private static LambdaQueryWrapper<OauthTokenRecord> lambdaQW(OauthTokenRecordPageDTO query) {
        LambdaQueryWrapper<OauthTokenRecord> qw = Wrappers.lambdaQuery(OauthTokenRecord.class);
        if (query == null) {
            return qw;
        }
        qw.and(StringUtils.isNotBlank(query.getSourceLocation()), wq -> wq
                .like(OauthTokenRecord::getSourceLocation, query.getSourceLocation())
                .or()
                .like(OauthTokenRecord::getSourceIp, query.getSourceLocation()));
        qw.like(StringUtils.isNotBlank(query.getUsername()), OauthTokenRecord::getUsername, query.getUsername());
        qw.eq(query.getStatus() != null, OauthTokenRecord::getOauthResult, query.getStatus());
        qw.ge(StringUtils.isNotBlank(query.getBeginOauthApplyTime()), OauthTokenRecord::getApplyOauthTime, query.getBeginOauthApplyTime());
        qw.le(StringUtils.isNotBlank(query.getEndOauthApplyTime()), OauthTokenRecord::getApplyOauthTime, query.getEndOauthApplyTime());
        return qw;
    }

    @Override
    public List<OauthTokenRecord> dataList(OauthTokenRecordPageDTO query) {
        LambdaQueryWrapper<OauthTokenRecord> listqw = lambdaQW(query);
        return list(listqw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(Long[] ids) {
        return removeByIds(Arrays.asList(ids));
    }

    @Override
    public boolean saveBeforeOauthRecord(String clientId, Map<String, String> parameters) {
        String grantType = parameters.get("grant_type");
        String username = parameters.get("username");
        if (GranterTypeEnum.MOBILE_SMS.getCode().equals(parameters.get("grant_type"))) {
            username = parameters.get("mobilePhone");
        }
        if (StringUtils.isBlank(username)) {
            //不同授权模式的用户名字段可能不一样 todo
            username = CommonConstants.UNKNOWN_STR;
        }
        String oauthRequestId = ZerosSecurityContextHolder.get(CommonConstants.OAUTH_REQUEST_ID);
        OauthTokenRecord otr = new OauthTokenRecord();
        otr.setClientId(clientId);
        otr.setApplyOauthTime(new Date());
        otr.setUsername(username);
        otr.setRequestId(oauthRequestId);
        otr.setBrowserType(CommonConstants.UNKNOWN_STR);
        otr.setOsType(CommonConstants.UNKNOWN_STR);
        otr.setSourceIp(CommonConstants.UNKNOWN_STR);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            UserAgent userAgent = UserAgentUtil.parse(request.getHeader(CommonConstants.USER_AGENT));
            otr.setBrowserType(userAgent.getBrowser().getName() + "/" + userAgent.getVersion());
            otr.setOsType(userAgent.getPlatform() + "/" + userAgent.getOsVersion());
            otr.setSourceIp(IpUtils.getRemoteAddr(request));
            otr.setSourceLocation(IpUtils.getIpLocation(otr.getSourceIp()));
        }
        otr.setGrantType(grantType);
        otr.setOauthResult(1);
        otr.setTokenValue("");
        otr.setOperatorId(ZerosSecurityContextHolder.getOperatorIds());
        return save(otr);
    }

    @Override
    public boolean updateOauthResult(String tokenValue, String resultMsg) {
        String oauthRequestId = ZerosSecurityContextHolder.get(CommonConstants.OAUTH_REQUEST_ID);
        LambdaUpdateWrapper<OauthTokenRecord> uw = Wrappers.lambdaUpdate(OauthTokenRecord.class);
        uw.eq(OauthTokenRecord::getRequestId, oauthRequestId);
        uw.set(OauthTokenRecord::getTokenValue, tokenValue);
        String operatorIds = ZerosSecurityContextHolder.getOperatorIds();
        uw.set(StringUtils.isNotBlank(operatorIds), OauthTokenRecord::getOperatorId, operatorIds);
        uw.set(OauthTokenRecord::getOauthMsg, StringUtils.substring(resultMsg, 0, 1000));
        if (StringUtils.isNotBlank(tokenValue)) {
            uw.set(OauthTokenRecord::getOauthResult, 0);
        }
        return update(null, uw);
    }

    @Override
    public boolean deleteAll() {
        LambdaQueryWrapper<OauthTokenRecord> deleteqw = Wrappers.lambdaQuery(OauthTokenRecord.class);
        deleteqw.le(OauthTokenRecord::getCreateTime, LocalDateTime.now());
        return remove(deleteqw);
    }

    @Override
    public void excelExport(RequestVO<OauthTokenRecordPageDTO> requestVO, HttpServletResponse response) {
        excelExport(PageUtils.of(requestVO, false), lambdaQW(requestVO.getT()), OauthTokenRecordPageVO.class, response);
    }
}
