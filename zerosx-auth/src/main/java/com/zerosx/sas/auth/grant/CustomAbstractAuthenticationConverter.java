package com.zerosx.sas.auth.grant;

import com.zerosx.common.sas.token.CustomAbstractAuthenticationToken;
import com.zerosx.sas.utils.SasAuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * SasAbsBaseAuthenticationConverter
 * <p>
 * 自定义模式认证转换器
 *
 * @author javacctvnews
 * @create 2023-10-23 23:39
 **/
public abstract class CustomAbstractAuthenticationConverter<T extends CustomAbstractAuthenticationToken> implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        //从HttpServletRequest获取grant_type的值，看是否存在对应的Authentication --DelegatingAuthenticationConverter
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!checkGrantType(grantType)) {
            return null;
        }
        //请求携带的参数
        MultiValueMap<String, String> parameters = SasAuthUtils.getParameters(request);
        //检查请求参数
        checkRequestParams(parameters);
        // scope (OPTIONAL)
        Set<String> scopes = checkAndReturnScopes(parameters);
        // 这里目前是客户端认证信息
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        if (clientPrincipal == null) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ErrorCodes.INVALID_CLIENT);
        }
        // 提取附加参数
        Map<String, Object> additionalParameters = additionalParameters(parameters);
        return buildAuthenticationToken(clientPrincipal, scopes, additionalParameters);
    }

    /**
     * 提取附加参数
     *
     * @param parameters request参数map
     * @return request参数
     */
    protected Map<String, Object> additionalParameters(MultiValueMap<String, String> parameters) {
        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            /*if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.SCOPE)) {
                additionalParameters.put(key, value.get(0));
            }*/
            additionalParameters.put(key, value.get(0));
        });
        return additionalParameters;
    }

    /**
     * 检查并返回scopes
     *
     * @param parameters request参数map
     * @return scopes
     */
    protected Set<String> checkAndReturnScopes(MultiValueMap<String, String> parameters) {
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) &&
                parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.SCOPE);
        }
        Set<String> scopes = null;
        if (StringUtils.hasText(scope)) {
            scopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }
        return scopes;
    }


    /**
     * 检查grantType是否支持这个AuthenticationConverter
     *
     * @param grantType
     * @return
     */
    protected abstract boolean checkGrantType(String grantType);

    /**
     * 检查一些必要的请求参数
     *
     * @param parameters
     */
    protected abstract void checkRequestParams(MultiValueMap<String, String> parameters);

    /**
     * 构建具体的AuthenticationToken
     *
     * @param clientPrincipal
     * @param requestedScopes
     * @param additionalParameters
     * @return
     */
    protected abstract T buildAuthenticationToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters);

}
