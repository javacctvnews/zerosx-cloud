//package com.zerosx.sas.auth.grant;
//
//import com.zerosx.sas.utils.SasAuthUtils;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
//import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
//import org.springframework.security.web.authentication.AuthenticationConverter;
//import org.springframework.util.MultiValueMap;
//import org.springframework.util.StringUtils;
//
//import java.util.*;
//
///**
// * SasAbsBaseAuthenticationConverter
// * <p>
// * 自定义模式认证转换器
// *
// * @author javacctvnews
// * @create 2023-10-23 23:39
// **/
//public abstract class CustomAbstractAuthenticationConverter<T extends CustomAbstractAuthenticationToken> implements AuthenticationConverter {
//
//    /**
//     * 检查grantType是否支持这个AuthenticationConverter
//     *
//     * @param request
//     * @return
//     */
//    protected abstract boolean checkGrantType(HttpServletRequest request);
//
//    /**
//     * 检查一些必要的请求参数
//     *
//     * @param request
//     */
//    protected abstract void checkRequestParam(HttpServletRequest request);
//
//    /**
//     * 构建具体的AuthenticationToken
//     *
//     * @param clientPrincipal
//     * @param requestedScopes
//     * @param additionalParameters
//     * @return
//     */
//    protected abstract T buildAuthenticationToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters);
//
//    @Override
//    public Authentication convert(HttpServletRequest request) {
//        // grant_type (REQUIRED)
//        if (!checkGrantType(request)) {
//            SasAuthUtils.throwError(OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE, OAuth2ParameterNames.GRANT_TYPE);
//        }
//
//        MultiValueMap<String, String> parameters = SasAuthUtils.getParameters(request);
//
//        // scope (OPTIONAL)
//        Set<String> scopes = null;
//        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
//        if (StringUtils.hasText(scope) &&
//                parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
//            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.SCOPE);
//        }
//        if (StringUtils.hasText(scope)) {
//            scopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
//        }
//
//        checkRequestParam(request);
//
//        // 这里目前是客户端认证信息
//        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
//        if (clientPrincipal == null) {
//            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ErrorCodes.INVALID_CLIENT);
//        }
//
//        // 提取附加参数
//        Map<String, Object> additionalParameters = new HashMap<>();
//        parameters.forEach((key, value) -> {
//            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
//                    !key.equals(OAuth2ParameterNames.SCOPE)) {
//                additionalParameters.put(key, value.get(0));
//            }
//        });
//        return buildAuthenticationToken(clientPrincipal, scopes, additionalParameters);
//    }
//
//}
