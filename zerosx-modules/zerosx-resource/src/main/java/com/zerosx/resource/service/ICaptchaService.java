package com.zerosx.resource.service;

import com.zerosx.resource.vo.AuthCaptchaVO;

public interface ICaptchaService {

    /**
     * 创建验证码
     *
     * @return AuthCaptchaVO
     */
    AuthCaptchaVO createCaptcha();

    /**
     * 获取防重令牌
     *
     */
    String idempotentToken();

}
