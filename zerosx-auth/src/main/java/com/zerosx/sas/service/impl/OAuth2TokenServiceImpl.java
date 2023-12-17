package com.zerosx.sas.service.impl;

import cn.hutool.core.util.PageUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.zerosx.common.base.constants.HeadersConstants;
import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.config.properties.EasyExcelProperties;
import com.zerosx.common.core.easyexcel.AutoColumnWidthWriteHandler;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.core.easyexcel.XHorizontalCellStyleStrategy;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.utils.EasyTransUtils;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.core.vo.CustomUserDetails;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.sas.auth.grant.CustomOAuth2ParameterNames;
import com.zerosx.sas.service.IOAuth2TokenService;
import com.zerosx.sas.vo.TokenQueryVO;
import com.zerosx.sas.vo.TokenVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.codec.SerializationCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * OAuth2TokenServiceImpl
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-30 09:55
 **/
@Service
@Slf4j
public class OAuth2TokenServiceImpl implements IOAuth2TokenService {

    @Autowired
    private OAuth2AuthorizationService oAuth2AuthorizationService;
    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private SerializationCodec serializationCodec;
    @Autowired
    protected EasyExcelProperties easyExcelProperties;

    @Override
    public CustomPageVO<TokenVO> pageList(RequestVO<TokenQueryVO> requestVO, boolean searchCount) {
        long t1 = System.currentTimeMillis();
        TokenQueryVO tokenQueryVO = requestVO.getT();
        String key = rmExpireTokens(tokenQueryVO.getClientId());
        Integer page = requestVO.getPageNum();
        Integer limit = requestVO.getPageSize();
        //根据请求参数生成redis的key
        //int expireCount = redissonOpService.zLen(key, serializationCodec);
        int total = redissonOpService.zLen(key, serializationCodec);
        log.debug("Token总数:{}", total);
        if (!searchCount) {
            limit = Math.toIntExact(total);
        }
        int[] startEnds = PageUtil.transToStartEnd(page - 1, limit);
        List<TokenVO> res = getTokenVOS(key, startEnds);
        log.debug("令牌分页查询:{} 耗时{}ms", res.size(), System.currentTimeMillis() - t1);
        return PageUtils.of(total, res);
    }

    private List<TokenVO> getTokenVOS(String key, int[] startEnds) {
        List<TokenVO> res = new ArrayList<>();
        Collection<String> objects = redissonOpService.zRange(key, startEnds[0], startEnds[1] - 1, serializationCodec);
        for (String tokenValue : objects) {
            OAuth2Authorization oAuth2Authorization = redissonOpService.hGet(ZCache.SAS_ACCESS_TOKEN.key(), tokenValue, serializationCodec);
            OAuth2AccessToken accessToken = oAuth2Authorization.getAccessToken().getToken();
            TokenVO tokenVO = new TokenVO();
            tokenVO.setTokenValue(accessToken.getTokenValue());
            tokenVO.setClientId(oAuth2Authorization.getRegisteredClientId());
            tokenVO.setGrantType(oAuth2Authorization.getAuthorizationGrantType().getValue());
            tokenVO.setExpiration(Date.from(accessToken.getExpiresAt()));
            tokenVO.setLoginExpiration(Date.from(accessToken.getIssuedAt()));
            long accessTokenDiffTime = ChronoUnit.SECONDS.between(Instant.now(), accessToken.getExpiresAt());
            tokenVO.setExpirationLength(BigDecimal.valueOf(accessTokenDiffTime));
            if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(oAuth2Authorization.getAuthorizationGrantType())) {
                tokenVO.setUsername(oAuth2Authorization.getRegisteredClientId());
            }
            Authentication authentication = oAuth2Authorization.getAttribute(Principal.class.getName());
            if (authentication != null) {
                CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
                tokenVO.setUsername(principal.getUsername());
                tokenVO.setOperatorId(principal.getOperatorId());
                Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
                tokenVO.setAuthUserType((String) details.get(CustomOAuth2ParameterNames.USER_AUTH_TYPE));
            }
            EasyTransUtils.easyTrans(tokenVO);
            res.add(tokenVO);
        }
        return res;
    }

    private String rmExpireTokens(String clientId) {
        String key = ZCache.SAS_TOKEN_PAGE.key(clientId);
        int remCount = redissonOpService.zRemByScore(key, (double) new Date().getTime() / 1000, serializationCodec);
        log.debug("删除的过期token数量:{}", remCount);
        return key;
    }

    @Override
    public boolean logout(TokenQueryVO tokenQueryVO) {
        List<String> tokens = tokenQueryVO.getTokens();
        if (tokens == null || tokens.isEmpty()) {
            String token = ZerosSecurityContextHolder.get(HeadersConstants.TOKEN);
            removeToken(token);
        } else {
            for (String token : tokens) {
                removeToken(token);
            }
        }
        return true;
    }

    @Override
    public boolean cleanTokenData(TokenQueryVO tokenQueryVO) {
        String opType = tokenQueryVO.getOpType();
        if ("0".equals(opType)) {
            redissonOpService.hDel(ZCache.SAS_ACCESS_TOKEN.key(), serializationCodec);
            redissonOpService.hDel(ZCache.SAS_REFRESH_TOKEN.key(), serializationCodec);
            redissonOpService.delByPattern(ZCache.SAS_TOKEN_PAGE.key("*"));
        } else {
            Collection<String> resColl = redissonOpService.zGetByScore(ZCache.SAS_TOKEN_PAGE.key(tokenQueryVO.getClientId()), (double) new Date().getTime() / 1000, serializationCodec);
            for (String token : resColl) {
                OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
                if (oAuth2Authorization != null) {
                    oAuth2AuthorizationService.remove(oAuth2Authorization);
                }
            }
        }
        return true;
    }

    @Override
    public void excelExport(RequestVO<TokenQueryVO> requestVO, HttpServletResponse response) {
        long t1 = System.currentTimeMillis();
        TokenQueryVO vo = requestVO.getT();
        if (StringUtils.isBlank(vo.getClientId())) {
            throw new BusinessException("至少选择一个客户端进行导出");
        }
        String key = ZCache.SAS_TOKEN_PAGE.key(vo.getClientId());
        //总数量
        int totalCount = redissonOpService.zLen(ZCache.SAS_TOKEN_PAGE.key(vo.getClientId()), serializationCodec);
        if (totalCount <= 0) {
            return;
        }
        Integer sheetNum = easyExcelProperties.getSheetNum();
        Integer querySize = easyExcelProperties.getQuerySize();
        //每个sheet最大查询次数
        int count = (int) Math.ceil((double) sheetNum / querySize);
        //int count = sheetNum % querySize == 0 ? sheetNum / querySize : (sheetNum / querySize) + 1;
        //多少个sheet页
        int sheetLoop = (int) Math.ceil((double) totalCount / sheetNum);
        //已查询条数
        int queryCurrentCount = 0;
        try (ExcelWriter excelWriter = EasyExcel.write(EasyExcelUtil.getOutputStream(response), TokenVO.class).build()) {
            //循环写入sheet页数据
            for (int i = 0; i < sheetLoop; i++) {
                if (queryCurrentCount >= totalCount) {
                    break;
                }
                //创建WriteSheet
                WriteSheet writeSheet = EasyExcel.writerSheet("Sheet" + (i + 1))
                        .registerWriteHandler(new AutoColumnWidthWriteHandler())
                        .registerWriteHandler(new XHorizontalCellStyleStrategy()).build();
                List<TokenVO> records;
                for (int j1 = 0; j1 < count; j1++) {
                    if (queryCurrentCount >= totalCount) {
                        break;
                    }
                    int pageNum = (i * count) + (j1 + 1);
                    long t11 = System.currentTimeMillis();
                    int[] startEnds = PageUtil.transToStartEnd(pageNum - 1, querySize);
                    records = getTokenVOS(key, startEnds);
                    int size = records.size();
                    queryCurrentCount += size;
                    log.debug("第{}页查询，每页{}条 实际{}条，耗时{}ms", pageNum, querySize, size, System.currentTimeMillis() - t11);
                    if (size > 0) {
                        excelWriter.write(EasyTransUtils.copyTrans(records, TokenVO.class), writeSheet);
                    }
                }
            }
        }
        log.debug("【{}】执行导出{}条，总耗时:{}ms", ZerosSecurityContextHolder.getUserName(), totalCount, System.currentTimeMillis() - t1);
    }

    /**
     * 删除token
     *
     * @param token token值
     */
    private void removeToken(String token) {
        OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        if (oAuth2Authorization != null) {
            oAuth2AuthorizationService.remove(oAuth2Authorization);
        }
    }

}
