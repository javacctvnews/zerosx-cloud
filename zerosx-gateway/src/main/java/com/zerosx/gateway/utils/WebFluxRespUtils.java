package com.zerosx.gateway.utils;

import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

@Slf4j
public class WebFluxRespUtils {

    /**
     * 获取请求token
     */
    public static String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(CommonConstants.TOKEN_HEADER);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotBlank(token) && token.startsWith(CommonConstants.BEARER_TYPE)) {
            return token.substring(CommonConstants.BEARER_TYPE.length()).trim();
        }
        return token;
    }

    public static Mono<Void> responseWrite(ServerWebExchange exchange, ResultVO<?> result) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setAccessControlAllowCredentials(true);
        response.getHeaders().setAccessControlAllowOrigin("*");
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBufferFactory dataBufferFactory = response.bufferFactory();
        DataBuffer buffer = dataBufferFactory.wrap(JacksonUtil.toJSONString(result).getBytes(Charset.defaultCharset()));
        return response.writeWith(Mono.just(buffer)).doOnError((error) -> {
            DataBufferUtils.release(buffer);
        });
    }

}
