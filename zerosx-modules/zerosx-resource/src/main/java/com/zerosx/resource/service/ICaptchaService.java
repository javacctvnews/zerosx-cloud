package com.zerosx.resource.service;

import com.zerosx.resource.vo.AuthCaptchaVO;

public interface ICaptchaService {

    /**
     * 创建验证码
     *
     * @return AuthCaptchaVO
     */
    AuthCaptchaVO createCaptcha();


}
