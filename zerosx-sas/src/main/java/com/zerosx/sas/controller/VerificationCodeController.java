package com.zerosx.sas.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.sas.service.IVerificationCodeService;
import com.zerosx.sas.vo.AuthCaptchaVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    @OpLog(mod = "验证码", btn = "图形验证码", opType = OpTypeEnum.INSERT)
    public ResultVO<AuthCaptchaVO> getImgCode() {
        return ResultVOUtil.success(verificationCodeService.createCode());
    }


}
