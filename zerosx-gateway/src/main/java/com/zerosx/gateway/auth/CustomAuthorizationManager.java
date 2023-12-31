package com.zerosx.gateway.auth;

import com.zerosx.api.system.ISysMenuClient;
import com.zerosx.common.base.constants.SecurityConstants;
import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.base.dto.RolePermissionDTO;
import com.zerosx.common.base.vo.LoginUserTenantsBO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.vo.SysMenuBO;
import com.zerosx.common.base.vo.SysPermissionBO;
import com.zerosx.common.core.enums.system.UserTypeEnum;
import com.zerosx.common.core.utils.AntPathMatcherUtils;
import com.zerosx.common.core.utils.MDCTraceUtils;
import com.zerosx.common.core.vo.CustomUserDetails;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.sas.properties.CustomSecurityProperties;
import com.zerosx.common.sas.properties.PermissionProperties;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.dynamictp.ZExecutor;
import com.zerosx.dynamictp.constant.DtpConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

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
    @Autowired
    private RedissonOpService redissonOpService;
    @Lazy
    @Autowired
    private ISysMenuClient sysMenuClient;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        return authentication.map(auth -> {
            long t1 = System.currentTimeMillis();
            boolean isPermission = hasPermission(auth, authorizationContext.getExchange());
            log.debug("【权限校验】[{}] 耗时:{}ms", isPermission, System.currentTimeMillis() - t1);
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
        /*Map<String, String> map = ServerWebExchangeUtils.getUriTemplateVariables(serverWebExchange);
        if (map == null || map.isEmpty()) {
            return false;
        }*/
        ServerHttpRequest serverHttpRequest = serverWebExchange.getRequest();
        handleLogId(serverHttpRequest);
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
        /*OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        String clientId = oAuth2Authentication.getOAuth2Request().getClientId();
        List<String> passClientIds = permissionProperties.getPassClientIds();
        if (passClientIds.contains(clientId)) {
            return true;
        }*/
        //放行url
        List<String> passPermsUrls = permissionProperties.getPassPermsUrls();
        if (passPermsUrls.contains(path) || AntPathMatcherUtils.matchUrl(path, passPermsUrls)) {
            return true;
        }
        //用户的权限
        LoginUserTenantsBO sysPermissionBO = (LoginUserTenantsBO) serverWebExchange.getAttributes().get(SecurityConstants.SECURITY_CONTEXT);

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
        List<SysMenuBO> permissionUrls = new ArrayList<>();
        List<Long> dbRoles = new ArrayList<>();
        for (Long roleId : roleIds) {
            //从缓存中查询角色对应的权限
            String permissionStr = redissonOpService.get(ZCache.ROLE_PERMISSIONS.key(roleId));
            List<SysMenuBO> sysMenuBOS = JacksonUtil.toList(permissionStr, SysMenuBO.class);
            if (CollectionUtils.isEmpty(sysMenuBOS)) {
                dbRoles.add(roleId);
            } else {
                permissionUrls.addAll(sysMenuBOS);
            }
        }
        if (!CollectionUtils.isEmpty(dbRoles)) {//从数据库查询
            try {
                RolePermissionDTO rolePermissionDTO = new RolePermissionDTO();
                rolePermissionDTO.setRoles(dbRoles);
                ThreadPoolExecutor executor = ZExecutor.getExecutor(DtpConstants.DYNAMIC_TP);
                ResultVO<SysPermissionBO> permissionBOResultVO = executor.submit(() -> sysMenuClient.queryPermsByRoleIds(rolePermissionDTO)).get();
                if (permissionBOResultVO.getData() != null && !CollectionUtils.isEmpty(permissionBOResultVO.getData().getPermissionUrls())) {
                    permissionUrls.addAll(permissionBOResultVO.getData().getPermissionUrls());
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        String requestMethod = serverHttpRequest.getMethod().name();
        //log.debug("用户:{} 最终校验是否有权限:{} ", username, path);
        String[] split = path.split("/");
        String urlPrefix = "/" + split[1];
        SysMenuBO sysMenuBO = permissionUrls.stream().parallel().filter(el -> {
            if (el.getRequestUrl().startsWith(urlPrefix)) {
                boolean match = antPathMatcher.match(el.getRequestUrl(), path);
                //log.debug("【{}】请求:{} 权限:{}", match ? "match" : "unMatch", path, el.getRequestUrl());
                if (StringUtils.isNotBlank(el.getRequestUrl()) && match) {
                    if (StringUtils.isNotBlank(el.getRequestMethod())) {
                        return requestMethod.equalsIgnoreCase(el.getRequestMethod());
                    } else {
                        return true;
                    }
                }
            }
            return false;
        }).findAny().orElse(null);
        return sysMenuBO != null;
    }

    private static void handleLogId(ServerHttpRequest serverHttpRequest) {
        String logId = serverHttpRequest.getHeaders().getFirst(MDCTraceUtils.TRACE_ID_HEADER);
        String spanId = serverHttpRequest.getHeaders().getFirst(MDCTraceUtils.SPAN_ID_HEADER);
        if (StringUtils.isNotBlank(logId)) {
            MDCTraceUtils.putTrace(logId, spanId);
        }
    }

}
