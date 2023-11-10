package com.zerosx.sas.auth.converter;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.utils.JacksonUtil;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.core.endpoint.DefaultOAuth2AccessTokenResponseMapConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;

import java.util.Map;

/**
 * 把OAuth2AccessTokenResponse封装成ResultVO格式
 */
public class CustomOAuth2AccessTokenResponseHttpMessageConverter extends OAuth2AccessTokenResponseHttpMessageConverter {

    private static final ParameterizedTypeReference<Map<String, Object>> STRING_OBJECT_MAP = new ParameterizedTypeReference<>() {
    };

    GenericHttpMessageConverter<Object> jsonMessageConverter = new MappingJackson2HttpMessageConverter(JacksonUtil.getInstance());

    private Converter<OAuth2AccessTokenResponse, Map<String, Object>> accessTokenResponseParametersConverter = new DefaultOAuth2AccessTokenResponseMapConverter();

    @Override
    protected void writeInternal(OAuth2AccessTokenResponse tokenResponse, HttpOutputMessage outputMessage) throws HttpMessageNotWritableException {
        try {
            Map<String, Object> tokenResponseParameters = (Map) this.accessTokenResponseParametersConverter.convert(tokenResponse);
            jsonMessageConverter.write(ResultVOUtil.success(tokenResponseParameters), STRING_OBJECT_MAP.getType(), MediaType.APPLICATION_JSON, outputMessage);
        } catch (Exception var4) {
            throw new HttpMessageNotWritableException("An error occurred writing the OAuth 2.0 Access Token Response: " + var4.getMessage(), var4);
        }
    }

}
