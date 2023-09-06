package com.zerosx.auth.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "测试API")
public class DemoController {

    @PostMapping("/test/token_valid")
    public ResultVO<?> testToken(){
        return ResultVOUtil.success("成功了");
    }

}
