package com.zerosx.gateway.auth;

import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.constants.HeadersConstants;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.core.utils.AntPathMatcherUtils;
import com.zerosx.common.core.utils.MDCTraceUtils;
import com.zerosx.common.core.vo.CustomUserDetails;
import com.zerosx.common.redis.enums.RedisKeyNameEnum;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.security.properties.CustomSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * CustomWebFilter
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-02 14:45
 **/
@Slf4j
public class CustomHeadersWebFilter implements WebFilter {

    private CustomSecurityProperties customSecurityProperties;
    private TokenStore tokenStore;
    private RedissonOpService redissonOpService;

    public CustomHeadersWebFilter(CustomSecurityProperties customSecurityProperties, TokenStore tokenStore, RedissonOpService redissonOpService) {
        this.customSecurityProperties = customSecurityProperties;
        this.tokenStore = tokenStore;
        this.redissonOpService = redissonOpService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Map<String, String> map = ServerWebExchangeUtils.getUriTemplateVariables(exchange);
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> headerValues = new LinkedMultiValueMap<>(8);
        //tranceId
        //链路追踪id
        String traceId = request.getHeaders().getFirst(MDCTraceUtils.TRACE_ID_HEADER);
        if (StringUtils.isBlank(traceId)) {
            MDCTraceUtils.addTrace();
        }
        headerValues.add(MDCTraceUtils.TRACE_ID_HEADER, MDCTraceUtils.getTraceId());
        headerValues.add(MDCTraceUtils.SPAN_ID_HEADER, MDCTraceUtils.getNextSpanId());
        //其他headers

        String path = request.getURI().getPath();
        String token = getToken(request);
        log.debug("拦截到请求:{} token:{}", path, token);
        List<String> allIgnoreAuthUrls = customSecurityProperties.getAllIgnoreAuthUrls();
        //不需要认证的url
        if (allIgnoreAuthUrls.contains(path) || AntPathMatcherUtils.matchUrl(path, customSecurityProperties.getAllIgnoreAuthUrls())) {
            return buildExchange(exchange, chain, headerValues);
        }
        //OAuth2Authentication的缓存
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(oAuth2AccessToken);
        //改从缓存中获取
        String clientDetailsClientId = RedisKeyNameEnum.key(RedisKeyNameEnum.OAUTH_CLIENT_DETAILS, oAuth2Authentication.getOAuth2Request().getClientId());
        ClientDetails clientDetails = redissonOpService.get(clientDetailsClientId);
        Set<String> resourceIds = clientDetails.getResourceIds();
        String[] split = path.split("/");
        if (!resourceIds.contains(split[1])) {
            return Mono.error(new BusinessException("您没有此资源权限！"));
        }
        Object principal = oAuth2Authentication.getPrincipal();
        headerValues.add(HeadersConstants.CLIENT_ID, oAuth2Authentication.getOAuth2Request().getClientId());
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails user = (CustomUserDetails) principal;
            //租户ID
            headerValues.add(HeadersConstants.OPERATOR_ID, user.getOperatorId());
            headerValues.add(HeadersConstants.USERNAME, user.getUserName());
            String username = user.getUserName();
            headerValues.add(HeadersConstants.USERNAME, username);
            //token的客户端标识
            String clientId = oAuth2Authentication.getOAuth2Request().getClientId();
            //管理平台或运维小程序
            if ("saas".equals(clientId)) {
                headerValues.add(HeadersConstants.USERID, String.valueOf(user.getId()));
                headerValues.add(HeadersConstants.USERNAME, user.getUserName());
                headerValues.add(HeadersConstants.USERTYPE, user.getUserType());
            }
        }
        return buildExchange(exchange, chain, headerValues);
    }


    private static Mono<Void> buildExchange(ServerWebExchange exchange, WebFilterChain chain, MultiValueMap<String, String> headerValues) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate().headers(h -> h.addAll(headerValues)).build();
        ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
        return chain.filter(build);
    }


    /**
     * 获取请求token
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(CommonConstants.TOKEN_HEADER);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotBlank(token) && token.startsWith(OAuth2AccessToken.BEARER_TYPE)) {
            return token.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
        }

        return token;
    }

}
