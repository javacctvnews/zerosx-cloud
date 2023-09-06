package com.zerosx.auth.service.impl;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.SelectOptionVO;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.utils.BeanCopierUtil;
import com.zerosx.common.core.utils.EasyTransUtils;
import com.zerosx.common.base.utils.JacksonUtil;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.redis.templete.RedisOpService;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.redis.enums.RedisKeyNameEnum;
import com.zerosx.common.redis.properties.CustomRedissonProperties;
import com.zerosx.common.security.utils.JdkSerializationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class OauthClientDetailsServiceImpl extends ServiceImpl<IOauthClientDetailsMapper, OauthClientDetails> implements IOauthClientDetailsService {

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
    private RedisOpService redisOpService;
    @Autowired
    private CustomRedissonProperties customRedissonProperties;
    @Autowired
    private CustomJdbcClientDetailsService customJdbcClientDetailsService;

    @Override
    public CustomPageVO<OauthClientDetailsPageVO> listPage(RequestVO<OauthClientDetailsPageDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), getWrapper(requestVO.getT())).convert((e) -> {
            OauthClientDetailsPageVO pageVO = BeanCopierUtil.copyProperties(e, OauthClientDetailsPageVO.class);
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
    public boolean saveOauthClient(OauthClientDetailsDTO oauthClientDetailsDTO) {
        OauthClientDetails clientDetails = getByClientId(oauthClientDetailsDTO.getClientId());
        if (clientDetails != null) {
            throw new BusinessException("已存在的客户端:" + oauthClientDetailsDTO.getClientId());
        }
        OauthClientDetails oauthClientDetails = BeanCopierUtil.copyProperties(oauthClientDetailsDTO, OauthClientDetails.class);
        Date nowDate = new Date();
        oauthClientDetails.setCreateTime(nowDate);
        oauthClientDetails.setUpdateTime(nowDate);
        oauthClientDetails.setCreateBy(ZerosSecurityContextHolder.getUserName());
        oauthClientDetails.setScope("all");
        oauthClientDetails.setAuthorizedGrantTypes(String.join(",", oauthClientDetailsDTO.getAuthorizedGrantTypes()));
        oauthClientDetails.setClientSecret(passwordEncoder.encode(oauthClientDetailsDTO.getClientSecret()));
        oauthClientDetails.setClientSecretStr(oauthClientDetailsDTO.getClientSecret());
        oauthClientDetails.setAccessTokenValiditySeconds(oauthClientDetailsDTO.getAccessTokenValidity());
        oauthClientDetails.setRefreshTokenValiditySeconds(oauthClientDetailsDTO.getRefreshTokenValidity());
        return save(oauthClientDetails);
    }

    @Override
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
        OauthClientDetails oauthClientDetails = BeanCopierUtil.copyProperties(editDTO, OauthClientDetails.class);
        oauthClientDetails.setUpdateTime(new Date());
        oauthClientDetails.setAuthorizedGrantTypes(String.join(",", editDTO.getAuthorizedGrantTypes()));
        oauthClientDetails.setClientSecret(passwordEncoder.encode(editDTO.getClientSecret()));
        oauthClientDetails.setClientSecretStr(editDTO.getClientSecret());
        oauthClientDetails.setUpdateBy(ZerosSecurityContextHolder.getUserName());
        oauthClientDetails.setAccessTokenValiditySeconds(editDTO.getAccessTokenValidity());
        oauthClientDetails.setRefreshTokenValiditySeconds(editDTO.getRefreshTokenValidity());
        return updateById(oauthClientDetails);
    }

    @Override
    public OauthClientDetailsVO queryById(Long id) {
        OauthClientDetails clientDetails = getById(id);
        OauthClientDetailsVO oauthClientDetailsVO = BeanCopierUtil.copyProperties(clientDetails, OauthClientDetailsVO.class);
        oauthClientDetailsVO.setAccessTokenValidity(clientDetails.getAccessTokenValiditySeconds());
        oauthClientDetailsVO.setRefreshTokenValidity(clientDetails.getRefreshTokenValiditySeconds());
        oauthClientDetailsVO.setClientSecret(clientDetails.getClientSecretStr());
        return oauthClientDetailsVO;
    }

    @Override
    public boolean deleteRecord(Long[] ids) {
        for (Long id : ids) {
            OauthClientDetails clientDetails = getById(id);
            log.debug("删除客户端:{}", JacksonUtil.toJSONString(clientDetails));
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
        return RedisKeyNameEnum.key(RedisKeyNameEnum.OAUTH_CLIENT_DETAILS, clientId);
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
        byte[] clientIdKey = JdkSerializationUtils.serialize(getRedisKey(tokenQueryVO.getUsername(), tokenQueryVO.getClientId()));
        Long total = redisOpService.length(clientIdKey);
        if (!searchCount) {
            limit = Math.toIntExact(total);
        }
        int[] startEnds = PageUtil.transToStartEnd(page - 1, limit);
        //查询token集合
        List<byte[]> tokenObjs = redisOpService.getByteList(clientIdKey, startEnds[0], startEnds[1] - 1);
        List<TokenVO> res = new ArrayList<>();
        for (byte[] obj : tokenObjs) {
            TokenVO tokenVo = new TokenVO();
            OAuth2AccessToken accessToken = JdkSerializationUtils.deserialize((byte[]) obj, OAuth2AccessToken.class);
            tokenVo.setOperatorId((String) accessToken.getAdditionalInformation().get("operatorId"));
            tokenVo.setUsername((String) accessToken.getAdditionalInformation().get("userName"));
            tokenVo.setAuthUserType((String) accessToken.getAdditionalInformation().get(SecurityConstants.USER_AUTH_TYPE));
            tokenVo.setTokenValue(accessToken.getValue());
            tokenVo.setExpiration(accessToken.getExpiration());
            tokenVo.setExpirationLength(new BigDecimal(accessToken.getExpiresIn()));
            OAuth2Authentication authentication = tokenStore.readAuthentication(accessToken);
            if (authentication != null) {
                OAuth2Request request = authentication.getOAuth2Request();
                tokenVo.setClientId(request.getClientId());
                tokenVo.setGrantType(request.getGrantType());
            }
            EasyTransUtils.easyTrans(tokenVo);
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
        String redisKey = customRedissonProperties.getKeyPrefix() + ":" + TokenStoreConstants.CLIENT_ID_TO_ACCESS + clientId;
        byte[] clientIdKey = JdkSerializationUtils.serialize(redisKey);
        Long total = redisOpService.length(clientIdKey);
        int loopNum = (int) Math.ceil((double) total / INTERVAL);
        int startRow = 0;
        List<Map<String, byte[]>> cleanClientList = new ArrayList<>();
        for (int i = 0; i < loopNum; i++) {
            if (startRow > total) {
                break;
            }
            int endRow = startRow + INTERVAL;
            List<byte[]> tokenObjs = redisOpService.getByteList(clientIdKey, startRow, endRow - 1);
            log.debug("{} {} {}", startRow, endRow - 1, tokenObjs.size());
            for (byte[] tokenObj : tokenObjs) {
                OAuth2AccessToken obj = JdkSerializationUtils.deserialize(tokenObj, OAuth2AccessToken.class);
                if ("1".equals(opType)) {
                    if (obj.getExpiresIn() < 0) {
                        Map<String, byte[]> umap = new HashMap<>();
                        String userName = (String) obj.getAdditionalInformation().get("userName");
                        umap.put(userName, tokenObj);
                        cleanClientList.add(umap);
                    }
                } else {
                    Map<String, byte[]> umap = new HashMap<>();
                    String userName = (String) obj.getAdditionalInformation().get("userName");
                    umap.put(userName, tokenObj);
                    cleanClientList.add(umap);
                }
            }
            startRow += INTERVAL;
        }
        //删除
        for (Map<String, byte[]> record : cleanClientList) {
            for (Map.Entry<String, byte[]> entry : record.entrySet()) {
                byte[] unameKey = JdkSerializationUtils.serialize(TokenStoreConstants.UNAME_TO_ACCESS + clientId + ":" + entry.getKey());
                Long rmClientId = redisOpService.lRem(clientIdKey, 1, entry.getValue());
                Long rmUnameKey = redisOpService.lRem(unameKey, 1, entry.getValue());
                log.debug("Token已失效，【{} {}】", rmClientId, rmUnameKey);
                if ("0".equals(opType)) {
                    tokenStore.removeAccessToken(JdkSerializationUtils.deserialize(entry.getValue(), OAuth2AccessToken.class));
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
            result = customRedissonProperties.getKeyPrefix() + ":" + TokenStoreConstants.UNAME_TO_ACCESS + clientId + ":" + username;
        } else {
            result = customRedissonProperties.getKeyPrefix() + ":" + TokenStoreConstants.CLIENT_ID_TO_ACCESS + clientId;
        }
        return result;
    }
}
