package com.zerosx.resource.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.resource.service.ICaptchaService;
import com.zerosx.resource.vo.AuthCaptchaVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "验证码")
@Slf4j
public class CaptchaController {

    @Autowired
    private ICaptchaService captchaService;

    @Operation(summary = "图形验证码")
    @GetMapping("/captcha")
    @OpLog(mod = "验证码", btn = "图形验证码", opType = OpTypeEnum.INSERT)
    public ResultVO<AuthCaptchaVO> getImgCode() {
        return ResultVOUtil.success(captchaService.createCaptcha());
    }


}
