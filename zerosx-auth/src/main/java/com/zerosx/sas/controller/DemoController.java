package com.zerosx.sas.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DemoController
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-20 23:36
 **/
@RestController
public class DemoController {

    @PostMapping("/oauth2/token/sas")
    public ResultVO<?> test() {
        return ResultVOUtil.success("success");
    }

    @GetMapping("/oauth2/token_valid")
    public ResultVO<?> testToken() {
        return ResultVOUtil.success("成功了");
    }

}
