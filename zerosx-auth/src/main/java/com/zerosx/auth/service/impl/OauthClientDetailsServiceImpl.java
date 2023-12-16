package com.zerosx.auth.service.impl;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.auth.dto.OauthClientDetailsDTO;
import com.zerosx.auth.dto.OauthClientDetailsPageDTO;
import com.zerosx.auth.entity.OauthClientDetails;
import com.zerosx.auth.mapper.IOauthClientDetailsMapper;
import com.zerosx.auth.service.IOauthClientDetailsService;
import com.zerosx.auth.service.auth.CustomJdbcClientDetailsService;
import com.zerosx.auth.vo.OauthClientDetailsPageVO;
import com.zerosx.auth.vo.OauthClientDetailsVO;
import com.zerosx.auth.vo.TokenQueryVO;
import com.zerosx.auth.vo.TokenVO;
import com.zerosx.common.base.constants.HeadersConstants;
import com.zerosx.common.base.constants.SecurityConstants;
import com.zerosx.common.base.constants.TokenStoreConstants;
import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.OauthClientDetailsBO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.SelectOptionVO;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.EasyTransUtils;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.redis.properties.CustomRedissonProperties;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.utils.BeanCopierUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.redisson.codec.SerializationCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OauthClientDetailsServiceImpl extends SuperServiceImpl<IOauthClientDetailsMapper, OauthClientDetails> implements IOauthClientDetailsService {

    /**
     * 清理token时每次取多少条数据
     */
    private static final int INTERVAL = 100;

    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IOauthClientDetailsMapper clientMapper;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private CustomRedissonProperties customRedissonProperties;
    @Autowired
    private CustomJdbcClientDetailsService customJdbcClientDetailsService;
    @Autowired
    private SerializationCodec serializationCodec;

    @Override
    public CustomPageVO<OauthClientDetailsPageVO> listPage(RequestVO<OauthClientDetailsPageDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), getWrapper(requestVO.getT())).convert((e) -> {
            OauthClientDetailsPageVO pageVO = EasyTransUtils.copyTrans(e, OauthClientDetailsPageVO.class);
            pageVO.setClientSecret(e.getClientSecretStr());
            pageVO.setAccessTokenValidity(e.getAccessTokenValiditySeconds());
            pageVO.setRefreshTokenValidity(e.getRefreshTokenValiditySeconds());
            return pageVO;
        }));
    }

    @Override
    public List<OauthClientDetails> dataList(OauthClientDetailsPageDTO t) {
        return list(getWrapper(t));
    }

    @NotNull
    private static LambdaQueryWrapper<OauthClientDetails> getWrapper(OauthClientDetailsPageDTO t) {
        LambdaQueryWrapper<OauthClientDetails> pageqw = Wrappers.lambdaQuery(OauthClientDetails.class);
        pageqw.like(StringUtils.isNotBlank(t.getClientName()), OauthClientDetails::getClientName, t.getClientName());
        pageqw.like(StringUtils.isNotBlank(t.getClientId()), OauthClientDetails::getClientId, t.getClientId());
        pageqw.like(StringUtils.isNotBlank(t.getAuthorizedGrantTypes()), OauthClientDetails::getAuthorizedGrantTypes, t.getAuthorizedGrantTypes());
        pageqw.eq(StringUtils.isNotBlank(t.getStatus()), OauthClientDetails::getStatus, t.getStatus());
        return pageqw;
    }

    public OauthClientDetails getByClientId(String clientId) {
        LambdaQueryWrapper<OauthClientDetails> queryWrapper = Wrappers.lambdaQuery(OauthClientDetails.class);
        queryWrapper.eq(OauthClientDetails::getClientId, clientId);
        return clientMapper.selectOne(queryWrapper);
    }

    @Override
    public OauthClientDetailsBO getClient(String clientId) {
        OauthClientDetailsBO oauthClientDetailsBO = BeanCopierUtils.copyProperties(getByClientId(clientId), OauthClientDetailsBO.class);
        customJdbcClientDetailsService.loadClientByClientId(clientId);
        return oauthClientDetailsBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOauthClient(OauthClientDetailsDTO oauthClientDetailsDTO) {
        OauthClientDetails clientDetails = getByClientId(oauthClientDetailsDTO.getClientId());
        if (clientDetails != null) {
            throw new BusinessException("已存在的客户端:" + oauthClientDetailsDTO.getClientId());
        }
        OauthClientDetails oauthClientDetails = BeanCopierUtils.copyProperties(oauthClientDetailsDTO, OauthClientDetails.class);
        Date nowDate = new Date();
        oauthClientDetails.setCreateTime(nowDate);
        oauthClientDetails.setUpdateTime(nowDate);
        oauthClientDetails.setCreateBy(ZerosSecurityContextHolder.getUserName());
        oauthClientDetails.setScope("all");
        oauthClientDetails.setAuthorizedGrantTypes(StringUtils.join(oauthClientDetailsDTO.getAuthorizedGrantTypes(), ","));
        oauthClientDetails.setResourceIds(StringUtils.join(oauthClientDetailsDTO.getResourceIds(), ","));
        oauthClientDetails.setClientSecret(passwordEncoder.encode(oauthClientDetailsDTO.getClientSecret()));
        oauthClientDetails.setClientSecretStr(oauthClientDetailsDTO.getClientSecret());
        oauthClientDetails.setAccessTokenValiditySeconds(oauthClientDetailsDTO.getAccessTokenValidity());
        oauthClientDetails.setRefreshTokenValiditySeconds(oauthClientDetailsDTO.getRefreshTokenValidity());
        return save(oauthClientDetails);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editOauthClient(OauthClientDetailsDTO editDTO) {
        OauthClientDetails clientDetails = getById(editDTO.getId());
        if (clientDetails == null) {
            throw new BusinessException("记录不存在");
        }
        //更新了clientId
        if (!clientDetails.getClientId().equals(editDTO.getClientId())) {
            LambdaQueryWrapper<OauthClientDetails> countQw = Wrappers.lambdaQuery(OauthClientDetails.class);
            countQw.eq(OauthClientDetails::getClientId, editDTO.getClientId());
            countQw.ne(OauthClientDetails::getId, editDTO.getId());
            long count = count(countQw);
            if (count > 0) {
                throw new BusinessException("更新失败，已存在的客户端:" + editDTO.getClientId());
            }
        }
        redissonOpService.del(clientRedisKey(clientDetails.getClientId()));
        OauthClientDetails oauthClientDetails = BeanCopierUtils.copyProperties(editDTO, OauthClientDetails.class);
        oauthClientDetails.setUpdateTime(new Date());
        oauthClientDetails.setAuthorizedGrantTypes(StringUtils.join(editDTO.getAuthorizedGrantTypes(), ","));
        oauthClientDetails.setResourceIds(StringUtils.join(editDTO.getResourceIds(), ","));
        oauthClientDetails.setClientSecret(passwordEncoder.encode(editDTO.getClientSecret()));
        oauthClientDetails.setClientSecretStr(editDTO.getClientSecret());
        oauthClientDetails.setUpdateBy(ZerosSecurityContextHolder.getUserName());
        oauthClientDetails.setAccessTokenValiditySeconds(editDTO.getAccessTokenValidity());
        oauthClientDetails.setRefreshTokenValiditySeconds(editDTO.getRefreshTokenValidity());
        boolean b = updateById(oauthClientDetails);
        if (b) {
            redissonOpService.del(clientRedisKey(clientDetails.getClientId()));
        }
        return b;
    }

    @Override
    public OauthClientDetailsVO queryById(Long id) {
        OauthClientDetails clientDetails = getById(id);
        OauthClientDetailsVO oauthClientDetailsVO = EasyTransUtils.copyTrans(clientDetails, OauthClientDetailsVO.class);
        oauthClientDetailsVO.setAccessTokenValidity(clientDetails.getAccessTokenValiditySeconds());
        oauthClientDetailsVO.setRefreshTokenValidity(clientDetails.getRefreshTokenValiditySeconds());
        oauthClientDetailsVO.setClientSecret(clientDetails.getClientSecretStr());
        if (StringUtils.isNotBlank(clientDetails.getResourceIds())) {
            oauthClientDetailsVO.setResourceIds(Arrays.stream(clientDetails.getResourceIds().split(",")).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(clientDetails.getAuthorizedGrantTypes())) {
            oauthClientDetailsVO.setAuthorizedGrantTypes(Arrays.stream(clientDetails.getAuthorizedGrantTypes().split(",")).collect(Collectors.toList()));
        }
        return oauthClientDetailsVO;
    }

    @Override
    public boolean deleteRecord(Long[] ids) {
        for (Long id : ids) {
            OauthClientDetails clientDetails = getById(id);
            redissonOpService.del(clientRedisKey(clientDetails.getClientId()));
            removeById(id);
        }
        return true;
    }

    @Override
    public List<SelectOptionVO> selectList() {
        List<OauthClientDetails> list = list();
        List<SelectOptionVO> listOV = new ArrayList<>();
        list.forEach(e -> {
            SelectOptionVO ov = new SelectOptionVO();
            ov.setValue(e.getClientId());
            ov.setLabel(e.getClientName());
            listOV.add(ov);
        });
        return listOV;
    }

    private String clientRedisKey(String clientId) {
        return ZCache.OAUTH_CLIENT_DETAILS.key(clientId);
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

    private void removeToken(String token) {
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
        if (oAuth2AccessToken != null) {
            OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
            //从tokenStore中移除token
            tokenStore.removeAccessToken(oAuth2AccessToken);
            tokenStore.removeRefreshToken(oAuth2RefreshToken);
            tokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);
        }
    }

    @Override
    public CustomPageVO<TokenVO> pageList(RequestVO<TokenQueryVO> requestVO, boolean searchCount) {
        Integer page = requestVO.getPageNum();
        Integer limit = requestVO.getPageSize();
        //根据请求参数生成redis的key
        TokenQueryVO tokenQueryVO = requestVO.getT();
        String clientIdKeyStr = getRedisKey(tokenQueryVO.getUsername(), tokenQueryVO.getClientId());
        int expireCount = redissonOpService.zRemByScore(clientIdKeyStr, (double) new Date().getTime(), serializationCodec);
        log.debug("已过期个数:{}", expireCount);
        int total = redissonOpService.zLen(clientIdKeyStr, serializationCodec);
        if (!searchCount) {
            limit = Math.toIntExact(total);
        }
        int[] startEnds = PageUtil.transToStartEnd(page - 1, limit);
        List<TokenVO> res = new ArrayList<>();
        Collection<String> objects = redissonOpService.zRange(clientIdKeyStr, startEnds[0], startEnds[1] - 1, serializationCodec);
        for (String tokenValue : objects) {
            TokenVO tokenVo = new TokenVO();
            tokenVo.setTokenValue(tokenValue);
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            if (accessToken != null) {
                tokenVo.setOperatorId((String) accessToken.getAdditionalInformation().get("operatorId"));
                tokenVo.setUsername((String) accessToken.getAdditionalInformation().get("userName"));
                tokenVo.setAuthUserType((String) accessToken.getAdditionalInformation().get(SecurityConstants.USER_AUTH_TYPE));
                tokenVo.setTokenValue(accessToken.getValue());
                tokenVo.setExpiration(accessToken.getExpiration());
                tokenVo.setExpirationLength(new BigDecimal(accessToken.getExpiresIn()));
                OAuth2Authentication authentication = tokenStore.readAuthentication(tokenValue);
                if (authentication != null) {
                    OAuth2Request request = authentication.getOAuth2Request();
                    tokenVo.setClientId(request.getClientId());
                    tokenVo.setGrantType(request.getGrantType());
                }
                EasyTransUtils.easyTrans(tokenVo);
            }
            res.add(tokenVo);
        }
        return PageUtils.of(total, res);
    }

    @Override
    public boolean cleanTokenData(TokenQueryVO tokenQueryVO) {
        if (StringUtils.isBlank(tokenQueryVO.getClientId())) {
            List<ClientDetails> clientDetails = customJdbcClientDetailsService.listClientDetails();
            for (ClientDetails clientDetail : clientDetails) {
                cleanDataByClient(clientDetail.getClientId(), tokenQueryVO.getOpType());
            }
            return true;
        }
        cleanDataByClient(tokenQueryVO.getClientId(), tokenQueryVO.getOpType());
        return true;
    }

    private void cleanDataByClient(String clientId, String opType) {
        String redisKey = TokenStoreConstants.CLIENT_ID_TO_ACCESS + clientId;
        if ("1".equals(opType)) {
            redissonOpService.zRemByScore(redisKey, (double) new Date().getTime(), serializationCodec);
            return;
        }

        int total = redissonOpService.zLen(redisKey, serializationCodec);
        int loopNum = (int) Math.ceil((double) total / INTERVAL);
        int startRow = 0;
        List<Map<String, OAuth2AccessToken>> cleanClientList = new ArrayList<>();
        for (int i = 0; i < loopNum; i++) {
            if (startRow > total) {
                break;
            }
            int endRow = startRow + INTERVAL;
            Collection<String> tokenObjs = redissonOpService.zRange(redisKey, startRow, endRow - 1, serializationCodec);
            log.debug("{} {} {}", startRow, endRow - 1, tokenObjs.size());
            for (String tokenValue : tokenObjs) {
                OAuth2AccessToken obj = tokenStore.readAccessToken(tokenValue);
                if (obj == null) {
                    continue;
                }
                Map<String, OAuth2AccessToken> umap = new HashMap<>();
                String userName = (String) obj.getAdditionalInformation().get("userName");
                umap.put(userName, obj);
                cleanClientList.add(umap);
            }
            startRow += INTERVAL;
        }
        //删除
        for (Map<String, OAuth2AccessToken> record : cleanClientList) {
            for (Map.Entry<String, OAuth2AccessToken> entry : record.entrySet()) {
                String unameKey = TokenStoreConstants.UNAME_TO_ACCESS + clientId + ":" + entry.getKey();
                boolean rmClientId = redissonOpService.zRem(redisKey, entry.getValue().getValue(), serializationCodec);
                boolean rmUnameKey = redissonOpService.zRem(unameKey, entry.getValue().getValue(), serializationCodec);
                log.debug("Token已失效，【{} {}】", rmClientId, rmUnameKey);
                if ("0".equals(opType)) {
                    OAuth2RefreshToken oAuth2RefreshToken = entry.getValue().getRefreshToken();
                    tokenStore.removeAccessToken(entry.getValue());
                    tokenStore.removeRefreshToken(oAuth2RefreshToken);
                    tokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);
                }
            }
        }
    }

    /**
     * 根据请求参数生成redis的key
     */
    private String getRedisKey(String username, String clientId) {
        String result;
        if (StringUtils.isNotBlank(username)) {
            result = TokenStoreConstants.UNAME_TO_ACCESS + clientId + ":" + username;
        } else {
            result = TokenStoreConstants.CLIENT_ID_TO_ACCESS + clientId;
        }
        return result;
    }

    @Override
    public void excelExport(RequestVO<OauthClientDetailsPageDTO> requestVO, HttpServletResponse response) {
        excelExport(PageUtils.of(requestVO, false), getWrapper(requestVO.getT()), OauthClientDetailsPageVO.class, response);
    }

}
