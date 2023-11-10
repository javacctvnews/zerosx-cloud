package com.zerosx.sas.service;


import com.zerosx.sas.vo.AuthCaptchaVO;

/**
 * @ClassName IVerificationCodeService
 * @Description 验证码（图形验证码、短信验证码
 * @Author javacctvnews
 * @Date 2023/3/26 21:41
 * @Version 1.0
 */
public interface IVerificationCodeService {

    /**
     * 使用Redis缓存图形验证码
     *
     * @param requestId
     * @param imgCode
     */
    void saveImageCode(String requestId, String imgCode);

    /**
     * 校验图形验证码是否有效
     *
     * @param requestId
     * @param validCode
     */
    void validateImgCode(String requestId, String validCode);

    AuthCaptchaVO createCode();

    boolean checkSmsCode(String mobilePhone, String smsCode);

}
