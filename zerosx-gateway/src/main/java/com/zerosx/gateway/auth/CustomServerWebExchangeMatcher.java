package com.zerosx.gateway.auth;

import com.zerosx.common.core.utils.AntPathMatcherUtils;
import com.zerosx.common.security.properties.CustomSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 无需认证的URL
 */
@Slf4j
public class CustomServerWebExchangeMatcher implements ServerWebExchangeMatcher {

    private final CustomSecurityProperties customSecurityProperties;

    public CustomServerWebExchangeMatcher(CustomSecurityProperties customSecurityProperties) {
        this.customSecurityProperties = customSecurityProperties;
    }

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        List<String> ignoreAuthUrls = customSecurityProperties.getAllIgnoreAuthUrls();
        String path = exchange.getRequest().getURI().getPath();
        boolean matched = AntPathMatcherUtils.matchUrl(path, ignoreAuthUrls);
        if (matched) {
            log.debug("忽略认证:{}", path);
            return MatchResult.notMatch();
        }
        return MatchResult.match();
    }
}
