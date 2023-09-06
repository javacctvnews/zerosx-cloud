package com.zerosx.auth.controller;

import com.zerosx.auth.dto.SmsCodeDTO;
import com.zerosx.auth.service.IVerificationCodeService;
import com.zerosx.auth.vo.AuthCaptchaVO;
import com.zerosx.auth.vo.SmsCodeVO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.log.annotation.SystemLog;
import com.zerosx.common.log.enums.BusinessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ImgCodeController
 * @Description 图形验证码
 * @Author javacctvnews
 * @Date 2023/3/26 17:48
 * @Version 1.0
 */
@RestController
@Tag(name = "验证码")
@Slf4j
public class VerificationCodeController {

    @Autowired
    private IVerificationCodeService verificationCodeService;

    /**
     * 创建验证码
     *
     * @throws Exception
     */
    @Operation(summary = "图形验证码")
    @GetMapping("/auth/getImgCode")
    @SystemLog(title = "验证码", btnName = "图形验证码", businessType = BusinessType.INSERT)
    public ResultVO<AuthCaptchaVO> getImgCode() {
        return ResultVOUtil.success(verificationCodeService.createCode());
    }

    /**
     * 创建验证码
     *
     * @throws Exception
     */
    @Operation(summary = "短信验证码")
    @PostMapping("/auth/getSmsCode")
    @SystemLog(title = "验证码", btnName = "短信验证码", businessType = BusinessType.INSERT)
    public ResultVO<SmsCodeVO> getSmsCode(@RequestBody SmsCodeDTO smsCodeDTO) {
        return ResultVOUtil.success(verificationCodeService.getSmsCode(smsCodeDTO));
    }

}
