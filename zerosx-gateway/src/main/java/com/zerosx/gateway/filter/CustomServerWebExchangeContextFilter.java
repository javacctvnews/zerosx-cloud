package com.zerosx.gateway.filter;

import com.zerosx.common.base.constants.HeadersConstants;
import com.zerosx.common.base.constants.SecurityConstants;
import com.zerosx.common.base.vo.LoginUserTenantsBO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.core.utils.AntPathMatcherUtils;
import com.zerosx.common.core.vo.CustomUserDetails;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.sas.auth.CustomOAuth2AuthorizationService;
import com.zerosx.common.sas.properties.CustomSecurityProperties;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.gateway.feign.AsyncSysUserService;
import com.zerosx.gateway.utils.WebFluxRespUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.reactive.ServerWebExchangeContextFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * CustomServerWebExchangeContextFilter
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-27 16:49
 **/
@Slf4j
public class CustomServerWebExchangeContextFilter extends ServerWebExchangeContextFilter {

    private final CustomOAuth2AuthorizationService customOAuth2AuthorizationService;
    private final CustomSecurityProperties customSecurityProperties;
    private final RedissonOpService redissonOpService;
    private final AsyncSysUserService asyncLoginUserService;

    public CustomServerWebExchangeContextFilter(CustomOAuth2AuthorizationService customOAuth2AuthorizationService, CustomSecurityProperties customSecurityProperties, RedissonOpService redissonOpService, AsyncSysUserService asyncLoginUserService) {
        this.customOAuth2AuthorizationService = customOAuth2AuthorizationService;
        this.customSecurityProperties = customSecurityProperties;
        this.redissonOpService = redissonOpService;
        this.asyncLoginUserService = asyncLoginUserService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = WebFluxRespUtils.getToken(request);
        if (StringUtils.isBlank(token)) {
            String path = request.getURI().getPath();
            List<String> allIgnoreAuthUrls = customSecurityProperties.getAllIgnoreAuthUrls();

            if (allIgnoreAuthUrls.contains(path) || AntPathMatcherUtils.matchUrl(path, allIgnoreAuthUrls)) {
                log.debug("不校验Token的URL不添加上下文数据:{}", path);
                return chain.filter(exchange);
            }
        }
        //OAuth2Authorization oAuth2Authorization = customOAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        OAuth2Authorization oAuth2Authorization = customOAuth2AuthorizationService.findById(token);
        if (oAuth2Authorization == null) {
            return chain.filter(exchange);
        }
        Authentication authentication = oAuth2Authorization.getAttribute(Principal.class.getName());
        if (authentication == null) {
            return chain.filter(exchange);
        }
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        if (customUserDetails == null) {
            return chain.filter(exchange);
        }
        // 上下文数据
        LoginUserTenantsBO loginUserTenantsBO = queryUserInfo(customUserDetails.getUsername());
        if (loginUserTenantsBO == null) {
            return chain.filter(exchange);
        }
        exchange.getAttributes().put(SecurityConstants.SECURITY_CONTEXT, loginUserTenantsBO);
        exchange.getAttributes().put(SecurityConstants.OAuth2Authorization, oAuth2Authorization);
        // 请求头
        MultiValueMap<String, String> headerValues = new LinkedMultiValueMap<>(8);
        headerValues.add(HeadersConstants.CLIENT_ID, oAuth2Authorization.getRegisteredClientId());
        if (StringUtils.isNotBlank(loginUserTenantsBO.getOperatorId())) {
            headerValues.add(HeadersConstants.OPERATOR_ID, loginUserTenantsBO.getOperatorId());
        }
        headerValues.add(HeadersConstants.USERNAME, loginUserTenantsBO.getUsername());
        headerValues.add(HeadersConstants.USERID, String.valueOf(loginUserTenantsBO.getUserId()));
        headerValues.add(HeadersConstants.USERTYPE, loginUserTenantsBO.getUserType());
        headerValues.add(HeadersConstants.TOKEN, token);
        return buildExchange(exchange, chain, headerValues);
    }


    public LoginUserTenantsBO queryUserInfo(String username) {
        //根据用户名先查询Redis，Redis不存在时查询DB （DB中用户数据与Redis一致性）
        String usernameKey = ZCache.CURRENT_USER.key(username);
        String cacheUser = redissonOpService.get(usernameKey);
        if (StringUtils.isNotBlank(cacheUser)) {
            try {
                log.debug("【权限控制】从【Redis】中获取用户【{}】用户信息", username);
                return JacksonUtil.toObject(cacheUser, LoginUserTenantsBO.class);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                redissonOpService.del(usernameKey);
            }
        }
        //缓存不存在时调用接口查询数据库
        ResultVO<LoginUserTenantsBO> loginAppUserResultVO;
        try {
            loginAppUserResultVO = asyncLoginUserService.currentLoginUser(username).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        log.debug("【权限控制】从【MySQL】中获取用户【{}】用户信息", username);
        return loginAppUserResultVO.getData();
    }

    private static Mono<Void> buildExchange(ServerWebExchange exchange, WebFilterChain chain, MultiValueMap<String, String> headerValues) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                .headers(h -> h.addAll(headerValues)).build();
        return chain.filter(exchange.mutate().request(serverHttpRequest).build());
    }
}
