package com.zerosx.gateway.filter;

import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.constants.HeadersConstants;
import com.zerosx.common.base.constants.SecurityConstants;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.LoginUserTenantsBO;
import com.zerosx.common.base.vo.OauthClientDetailsBO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.enums.RedisKeyNameEnum;
import com.zerosx.common.core.utils.AntPathMatcherUtils;
import com.zerosx.common.core.vo.CustomUserDetails;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.gateway.feign.AsyncSysUserService;
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
import org.springframework.web.filter.reactive.ServerWebExchangeContextFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * CustomServerWebExchangeContextFilter
 * <p> filter上下文数据
 * 1）检查token是否有效
 * 2）
 *
 * @author: javacctvnews
 * @create: 2023-09-03 13:53
 **/
@Slf4j
public class CustomServerWebExchangeContextFilter extends ServerWebExchangeContextFilter {

    private TokenStore tokenStore;
    private RedissonOpService redissonOpService;
    private AsyncSysUserService asyncLoginUserService;

    public CustomServerWebExchangeContextFilter(TokenStore tokenStore, RedissonOpService redissonOpService, AsyncSysUserService asyncLoginUserService) {
        this.tokenStore = tokenStore;
        this.redissonOpService = redissonOpService;
        this.asyncLoginUserService = asyncLoginUserService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.debug("执行CustomServerWebExchangeContextFilter");
        MultiValueMap<String, String> headerValues = new LinkedMultiValueMap<>(8);
        //添加traceId
        /*MDCTraceUtils.addTrace();
        headerValues.add(MDCTraceUtils.TRACE_ID_HEADER, MDCTraceUtils.getTraceId());
        headerValues.add(MDCTraceUtils.SPAN_ID_HEADER, MDCTraceUtils.getNextSpanId());*/
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String token = getToken(request);
        List<String> ignoreUrls = Arrays.stream(SecurityConstants.ENDPOINTS).collect(Collectors.toList());
        if (ignoreUrls.contains(path) || AntPathMatcherUtils.matchUrl(path, ignoreUrls)) {
            log.debug("非自定义接口不添加secretary上下文数据:{}", path);
            return buildExchange(exchange, chain, headerValues);
        }
        //OAuth2Authentication的缓存
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
        if (oAuth2AccessToken == null) {
            return buildExchange(exchange, chain, headerValues);
        }
        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(oAuth2AccessToken);
        if (!hasResourcesPerms(oAuth2Authentication.getOAuth2Request().getClientId(), path)) {
            return Mono.error(new BusinessException("您没有此ResourceIds资源权限！"));
        }
        CustomUserDetails userDetails = (CustomUserDetails) oAuth2Authentication.getPrincipal();
        String username = userDetails.getUsername();
        long t1 = System.currentTimeMillis();
        LoginUserTenantsBO loginUserTenantsBO = queryUserInfo(username);
        Map<String, String> map = new HashMap<>();
        map.put(SecurityConstants.SECURITY_CONTEXT, JacksonUtil.toJSONString(loginUserTenantsBO));
        ServerWebExchangeUtils.putUriTemplateVariables(exchange, map);
        headerValues.add(HeadersConstants.CLIENT_ID, oAuth2Authentication.getOAuth2Request().getClientId());
        headerValues.add(HeadersConstants.OPERATOR_ID, userDetails.getOperatorId());
        headerValues.add(HeadersConstants.USERNAME, userDetails.getUserName());
        headerValues.add(HeadersConstants.USERID, String.valueOf(userDetails.getId()));
        headerValues.add(HeadersConstants.USERTYPE, userDetails.getUserType());
        headerValues.add(HeadersConstants.TOKEN, token);
        log.debug("添加secretary上下文数据,耗时{}ms", System.currentTimeMillis() - t1);
        return buildExchange(exchange, chain, headerValues);
    }

    /**
     * 检查是否拥有resourceId资源访问权限
     *
     * @param clientId
     * @param path
     * @return
     */
    private boolean hasResourcesPerms(String clientId, String path) {
        String clientDetailsClientId = RedisKeyNameEnum.key(RedisKeyNameEnum.OAUTH_CLIENT_DETAILS, clientId);
        ClientDetails clientDetails = redissonOpService.get(clientDetailsClientId);
        Set<String> resourceIds;
        if (clientDetails == null) {
            Future<ResultVO<OauthClientDetailsBO>> client = asyncLoginUserService.getClient(clientId);
            ResultVO<OauthClientDetailsBO> oauthClientDetailsBOResultVO;
            try {
                oauthClientDetailsBOResultVO = client.get();
            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
            OauthClientDetailsBO detailsBO = oauthClientDetailsBOResultVO.getData();
            if (detailsBO == null) {
                return false;
            }
            String dbResourceIds = detailsBO.getResourceIds();
            if (StringUtils.isBlank(dbResourceIds)) {
                return false;
            }
            String[] split = dbResourceIds.split(",");
            resourceIds = Arrays.stream(split).collect(Collectors.toSet());
        } else {
            resourceIds = clientDetails.getResourceIds();
        }
        String[] split = path.split("/");
        return resourceIds.contains(split[1]);
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

    public LoginUserTenantsBO queryUserInfo(String username) {
        //根据用户名先查询Redis，Redis不存在时查询DB （DB中用户数据与Redis一致性）
        String usernameKey = RedisKeyNameEnum.key(RedisKeyNameEnum.CURRENT_USER, username);
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

}
