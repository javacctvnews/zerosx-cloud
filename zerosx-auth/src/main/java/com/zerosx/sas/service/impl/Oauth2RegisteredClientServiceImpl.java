package com.zerosx.sas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.OauthClientDetailsBO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.SelectOptionVO;
import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.EasyTransUtils;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.redis.properties.CustomRedissonProperties;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.utils.BeanCopierUtils;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.sas.auth.client.Oauth2RegisteredClientParametersMapper;
import com.zerosx.sas.auth.grant.CustomAuthorizationGrantType;
import com.zerosx.sas.dto.Oauth2RegisteredClientDTO;
import com.zerosx.sas.dto.Oauth2RegisteredClientPageDTO;
import com.zerosx.sas.entity.Oauth2RegisteredClient;
import com.zerosx.sas.mapper.IOauth2RegisteredClientMapper;
import com.zerosx.sas.service.IOauth2RegisteredClientService;
import com.zerosx.sas.vo.Oauth2RegisteredClientPageVO;
import com.zerosx.sas.vo.Oauth2RegisteredClientVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.codec.SerializationCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Slf4j
@Service
public class Oauth2RegisteredClientServiceImpl extends SuperServiceImpl<IOauth2RegisteredClientMapper, Oauth2RegisteredClient> implements IOauth2RegisteredClientService {

    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IOauth2RegisteredClientMapper oauth2RegisteredClientMapper;
    @Autowired
    private CustomRedissonProperties customRedissonProperties;
    @Autowired
    private SerializationCodec serializationCodec;
    @Autowired
    private RegisteredClientRepository registeredClientRepository;
    @Autowired
    private Oauth2RegisteredClientParametersMapper oauth2RegisteredClientParametersMapper;

    @Override
    public CustomPageVO<Oauth2RegisteredClientPageVO> listPage(RequestVO<Oauth2RegisteredClientPageDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(
                        PageUtils.of(requestVO, searchCount),
                        getWrapper(requestVO.getT()))
                .convert(this::getOauthClientDetailsPageVO));
    }

    private Oauth2RegisteredClientPageVO getOauthClientDetailsPageVO(Oauth2RegisteredClient e) {
        Oauth2RegisteredClientPageVO pageVO = EasyTransUtils.copyTrans(e, Oauth2RegisteredClientPageVO.class);
        pageVO.setClientIdIssuedAt(Date.from(e.getClientIdIssuedAt()));
        pageVO.setClientSecretExpiresAt(Date.from(e.getClientSecretExpiresAt()));
        pageVO.setRedirectUris(e.getRedirectUris());
        pageVO.setAuthorizedGrantTypes(e.getAuthorizationGrantTypes());
        Map<String, Object> tokenSettingsMap = oauth2RegisteredClientParametersMapper.parseMap(e.getTokenSettings());
        TokenSettings tokenSettings = TokenSettings.withSettings(tokenSettingsMap).build();
        pageVO.setAccessTokenTimeToLive(tokenSettings.getAccessTokenTimeToLive().getSeconds());
        pageVO.setRefreshTokenTimeToLive(tokenSettings.getRefreshTokenTimeToLive().getSeconds());
        pageVO.setAuthorizationCodeTimeToLive(tokenSettings.getAuthorizationCodeTimeToLive().getSeconds());
        pageVO.setDeviceCodeTimeToLive(tokenSettings.getDeviceCodeTimeToLive().getSeconds());
        pageVO.setReuseRefreshTokens(tokenSettings.isReuseRefreshTokens());
        pageVO.setIdTokenSignatureAlgorithm(tokenSettings.getIdTokenSignatureAlgorithm().getName());
        return pageVO;
    }

    @Override
    public List<Oauth2RegisteredClient> dataList(Oauth2RegisteredClientPageDTO t) {
        return list(getWrapper(t));
    }

    private static LambdaQueryWrapper<Oauth2RegisteredClient> getWrapper(Oauth2RegisteredClientPageDTO t) {
        LambdaQueryWrapper<Oauth2RegisteredClient> pageqw = Wrappers.lambdaQuery(Oauth2RegisteredClient.class);
        pageqw.like(StringUtils.isNotBlank(t.getClientName()), Oauth2RegisteredClient::getClientName, t.getClientName());
        pageqw.like(StringUtils.isNotBlank(t.getClientId()), Oauth2RegisteredClient::getClientId, t.getClientId());
        pageqw.like(StringUtils.isNotBlank(t.getAuthorizedGrantTypes()), Oauth2RegisteredClient::getAuthorizationGrantTypes, t.getAuthorizedGrantTypes());
        //pageqw.eq(StringUtils.isNotBlank(t.getStatus()), Oauth2RegisteredClient::getStatus, t.getStatus());
        return pageqw;
    }

    public Oauth2RegisteredClient getByClientId(String clientId) {
        LambdaQueryWrapper<Oauth2RegisteredClient> queryWrapper = Wrappers.lambdaQuery(Oauth2RegisteredClient.class);
        queryWrapper.eq(Oauth2RegisteredClient::getClientId, clientId);
        return oauth2RegisteredClientMapper.selectOne(queryWrapper);
    }

    @Override
    public OauthClientDetailsBO getClient(String clientId) {
        OauthClientDetailsBO oauthClientDetailsBO = BeanCopierUtils.copyProperties(getByClientId(clientId), OauthClientDetailsBO.class);
        //customJdbcClientDetailsService.loadClientByClientId(clientId);
        return oauthClientDetailsBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOauthClient(Oauth2RegisteredClientDTO clientDTO) {
        log.debug("{}", JacksonUtil.toJSONString(clientDTO));
        RegisteredClient.Builder builder = RegisteredClient.withId(clientDTO.getClientId()).clientId(clientDTO.getClientId()).clientIdIssuedAt(Instant.now()).clientSecretExpiresAt(clientDTO.getClientSecretExpiresAt().toInstant()).clientName(clientDTO.getClientName())
                //.clientSecret(passwordEncoder.encode(clientDTO.getClientSecret()))
                .clientAuthenticationMethods(c -> {
                    c.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                    c.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                    c.add(ClientAuthenticationMethod.CLIENT_SECRET_JWT);
                }).authorizationGrantTypes(g -> {
                    Set<String> authorizationGrantTypes = clientDTO.getAuthorizationGrantTypes();
                    for (String grantTypeCode : authorizationGrantTypes) {
                        AuthorizationGrantType grantType = CustomAuthorizationGrantType.getGrantType(grantTypeCode);
                        if (grantType != null) {
                            g.add(grantType);
                        }
                    }
                }).redirectUri(clientDTO.getRedirectUris())
                //.postLogoutRedirectUri("http://127.0.0.1:80/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.PHONE)
                .scope(OidcScopes.EMAIL)
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                        .authorizationCodeTimeToLive(Duration.ofSeconds(clientDTO.getAuthorizationCodeTimeToLive()))
                        .accessTokenTimeToLive(Duration.ofSeconds(clientDTO.getAccessTokenTimeToLive()))
                        .deviceCodeTimeToLive(Duration.ofSeconds(clientDTO.getDeviceCodeTimeToLive()))
                        .reuseRefreshTokens(Boolean.parseBoolean(clientDTO.getReuseRefreshTokens()))
                        .refreshTokenTimeToLive(Duration.ofSeconds(clientDTO.getRefreshTokenTimeToLive()))
                        .idTokenSignatureAlgorithm(SignatureAlgorithm.from(clientDTO.getIdTokenSignatureAlgorithm()) == null ? SignatureAlgorithm.RS256 : SignatureAlgorithm.from(clientDTO.getIdTokenSignatureAlgorithm()))
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireProofKey(Boolean.parseBoolean(clientDTO.getRequireProofKey()))
                        .build());
        //新增
        RegisteredClient rc = registeredClientRepository.findByClientId(clientDTO.getClientId());
        if (rc == null) {
            builder.clientSecret(passwordEncoder.encode(clientDTO.getClientSecret()));
        } else {
            builder.clientIdIssuedAt(rc.getClientIdIssuedAt());
            builder.clientSecret(rc.getClientSecret());
        }
        registeredClientRepository.save(builder.build());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editOauthClientPwd(Oauth2RegisteredClientDTO oauth2RegisteredClientDTO) {
        RegisteredClient registeredClient = registeredClientRepository.findById(oauth2RegisteredClientDTO.getClientId());
        if (registeredClient == null) {
            throw new BusinessException("客户端应用不存在");
        }
        LambdaUpdateWrapper<Oauth2RegisteredClient> uw = Wrappers.lambdaUpdate(Oauth2RegisteredClient.class);
        uw.eq(Oauth2RegisteredClient::getClientId, oauth2RegisteredClientDTO.getClientId());
        uw.set(Oauth2RegisteredClient::getClientSecret, passwordEncoder.encode(oauth2RegisteredClientDTO.getClientSecret()));
        return update(null, uw);
    }

    @Override
    public Oauth2RegisteredClientVO queryById(String id) {
        RegisteredClient registeredClient = registeredClientRepository.findById(id);
        if (registeredClient == null) {
            return null;
        }
        Oauth2RegisteredClientVO oauth2RegisteredClientVO = EasyTransUtils.copyTrans(registeredClient, Oauth2RegisteredClientVO.class);

        oauth2RegisteredClientVO.setClientSecretExpiresAt(Date.from(registeredClient.getClientSecretExpiresAt()));

        Set<AuthorizationGrantType> authorizationGrantTypes = registeredClient.getAuthorizationGrantTypes();
        Set<String> types = new HashSet<>();
        for (AuthorizationGrantType grantType : authorizationGrantTypes) {
            types.add(grantType.getValue());
        }
        oauth2RegisteredClientVO.setAuthorizationGrantTypes(types);

        Set<String> redirectUris = registeredClient.getRedirectUris();
        oauth2RegisteredClientVO.setRedirectUris((String) redirectUris.toArray()[0]);

        ClientSettings clientSettings = registeredClient.getClientSettings();
        oauth2RegisteredClientVO.setRequireProofKey(Boolean.toString(clientSettings.isRequireProofKey()));

        TokenSettings tokenSettings = registeredClient.getTokenSettings();
        oauth2RegisteredClientVO.setAccessTokenTimeToLive(tokenSettings.getAccessTokenTimeToLive().getSeconds());
        oauth2RegisteredClientVO.setRefreshTokenTimeToLive(tokenSettings.getRefreshTokenTimeToLive().getSeconds());
        oauth2RegisteredClientVO.setAuthorizationCodeTimeToLive(tokenSettings.getAuthorizationCodeTimeToLive().getSeconds());
        oauth2RegisteredClientVO.setDeviceCodeTimeToLive(tokenSettings.getDeviceCodeTimeToLive().getSeconds());
        oauth2RegisteredClientVO.setReuseRefreshTokens(Boolean.toString(tokenSettings.isReuseRefreshTokens()));
        oauth2RegisteredClientVO.setIdTokenSignatureAlgorithm(tokenSettings.getIdTokenSignatureAlgorithm().getName());
        return oauth2RegisteredClientVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(String[] ids) {
        for (String id : ids) {
            Oauth2RegisteredClient clientDetails = getById(id);
            redissonOpService.del(clientRedisKey(clientDetails.getClientId()));
            removeById(id);
        }
        return true;
    }

    @Override
    public List<SelectOptionVO> selectList() {
        List<Oauth2RegisteredClient> list = list();
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
    public void excelExport(RequestVO<Oauth2RegisteredClientPageDTO> requestVO, HttpServletResponse response) {
        excelExport(PageUtils.of(requestVO, false), getWrapper(requestVO.getT()), Oauth2RegisteredClientPageVO.class, response);
    }

    @Override
    protected List<?> handleData(List<Oauth2RegisteredClient> list, Class<?> exportClz) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<Oauth2RegisteredClientPageVO> res = new ArrayList<>();
        for (Oauth2RegisteredClient registeredClient : list) {
            Oauth2RegisteredClientPageVO oauth2RegisteredClientPageVO = getOauthClientDetailsPageVO(registeredClient);
            res.add(oauth2RegisteredClientPageVO);
        }
        return res;
    }
}
