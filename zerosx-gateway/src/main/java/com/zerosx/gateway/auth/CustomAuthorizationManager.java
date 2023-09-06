package com.zerosx.gateway.auth;

import com.zerosx.common.base.constants.SecurityConstants;
import com.zerosx.common.base.utils.JacksonUtil;
import com.zerosx.common.base.vo.LoginUserTenantsBO;
import com.zerosx.common.base.vo.SysMenuBO;
import com.zerosx.common.core.enums.UserTypeEnum;
import com.zerosx.common.core.utils.AntPathMatcherUtils;
import com.zerosx.common.core.vo.CustomUserDetails;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.security.properties.CustomSecurityProperties;
import com.zerosx.common.security.properties.PermissionProperties;
import com.zerosx.gateway.feign.AsyncSysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * url级权限认证
 * 用户权限查询：
 * 先从redis缓存中查询用户角色的权限，如果没有则从MySQL中查询（feign接口查询，查询时将角色对应的权限放入缓存中）
 */
@Slf4j
@Component
public class CustomAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Autowired
    protected CustomSecurityProperties customSecurityProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        return authentication.map(auth -> {
            long t1 = System.currentTimeMillis();
            boolean isPermission = hasPermission(auth, authorizationContext.getExchange());
            log.debug("【权限校验】耗时:{}ms", System.currentTimeMillis() - t1);
            return new AuthorizationDecision(isPermission);
        }).defaultIfEmpty(new AuthorizationDecision(false));
    }

    /**
     * url级权限认证
     *
     * @param authentication
     * @param serverWebExchange
     * @return
     */
    public boolean hasPermission(Authentication authentication, ServerWebExchange serverWebExchange) {
        Map<String, String> map = ServerWebExchangeUtils.getUriTemplateVariables(serverWebExchange);
        if (map == null || map.isEmpty()) {
            return false;
        }
        ServerHttpRequest serverHttpRequest = serverWebExchange.getRequest();
        PermissionProperties permissionProperties = customSecurityProperties.getPerms();
        if (permissionProperties == null) {
            return false;
        }
        //关闭校验
        if (!permissionProperties.getUrlPermissionEnable()) {
            return true;
        }
        //客户端校验
        String path = serverHttpRequest.getURI().getPath();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        log.debug("用户【{}】请求授权资源【{}】", username, path);
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        String clientId = oAuth2Authentication.getOAuth2Request().getClientId();
        List<String> passClientIds = permissionProperties.getPassClientIds();
        if (passClientIds.contains(clientId)) {
            return true;
        }
        //放行url
        List<String> passPermsUrls = permissionProperties.getPassPermsUrls();
        if (passPermsUrls.contains(path) || AntPathMatcherUtils.matchUrl(path, passPermsUrls)) {
            return true;
        }
        //用户的权限
        LoginUserTenantsBO sysPermissionBO = JacksonUtil.toObject(map.get(SecurityConstants.SECURITY_CONTEXT), LoginUserTenantsBO.class);
        if (sysPermissionBO == null) {
            return false;
        }
        //超级管理员
        if (UserTypeEnum.SUPER_ADMIN.getCode().equals(sysPermissionBO.getUserType())) {
            return true;
        }
        Set<Long> roleIds = sysPermissionBO.getRoleIds();
        if (roleIds == null || roleIds.isEmpty()) {
            log.debug("用户:{} 角色集合为空，禁止访问", username);
            return false;
        }
        List<SysMenuBO> permissionUrls = sysPermissionBO.getPerms();
        String requestMethod = serverHttpRequest.getMethodValue();
        //log.debug("用户:{} 最终校验是否有权限:{} ", username, path);
        for (SysMenuBO sysMenuBO : permissionUrls) {
            boolean match = antPathMatcher.match(sysMenuBO.getRequestUrl(), path);
            log.debug("请求:{} 匹配菜单集合:{} 【{}】", path, sysMenuBO.getRequestUrl(), match);
            if (StringUtils.isNotBlank(sysMenuBO.getRequestUrl()) && match) {
                if (StringUtils.isNotBlank(sysMenuBO.getRequestMethod())) {
                    return requestMethod.equalsIgnoreCase(sysMenuBO.getRequestMethod());
                } else {
                    return true;
                }
            }
        }
        return true;
    }

}
