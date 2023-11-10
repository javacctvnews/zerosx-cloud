package com.zerosx.sas.auth.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerosx.sas.entity.Oauth2RegisteredClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * RegisteredClientParametersMapper
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-11-08 09:06
 **/
@Service
@Getter
@Setter
public class Oauth2RegisteredClientParametersMapper implements Function<Oauth2RegisteredClient, List<SqlParameterValue>> {

    private ObjectMapper objectMapper = new ObjectMapper();

    public Oauth2RegisteredClientParametersMapper() {
        ClassLoader classLoader = JdbcRegisteredClientRepository.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        this.objectMapper.registerModules(securityModules);
        this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
    }

    @Override
    public List<SqlParameterValue> apply(Oauth2RegisteredClient oauth2RegisteredClient) {
        return null;
    }

    public String writeMap(Map<String, Object> data) {
        try {
            return this.objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    public Map<String, Object> parseMap(String data) {
        try {
            return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }
}
