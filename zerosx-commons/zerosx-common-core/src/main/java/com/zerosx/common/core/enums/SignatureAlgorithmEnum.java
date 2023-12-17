package com.zerosx.common.core.enums;

import com.zerosx.common.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import lombok.Getter;

/**
 * SignatureAlgorithmEnum
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-01 13:11
 **/
@Getter
@AutoDictData(code = "SignatureAlgorithmEnum", name = "签名算法(OIDC)",desc = "签名算法(OIDC)")
public enum SignatureAlgorithmEnum implements BaseEnum<String> {

    RS256("RS256", "RS256"),
    RS384("RS384", "RS384"),
    RS512("RS512", "RS512"),
    ES256("ES256", "ES256"),
    ES384("ES384", "ES384"),
    ES512("ES512", "ES512"),
    PS256("PS256", "PS256"),
    PS384("PS384", "PS384"),
    PS512("PS512", "PS512");

    private final String code;

    private final String message;

    SignatureAlgorithmEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCss() {
        return CssTypeEnum.DEFAULT.getCss();
    }

}
