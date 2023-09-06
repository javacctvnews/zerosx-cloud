package com.zerosx.generator.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.utils.DateTimeUtil;
import com.zerosx.generator.service.SysGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 代码生成器
 */
@RestController
@Tag(name = "代码生成器")
@Slf4j
public class SysGeneratorController {

    @Autowired
    private SysGeneratorService sysGeneratorService;

    @PostMapping("/generator/local_code")
    @Operation(summary = "代码生成器(存本地)")
    public ResultVO<?> makeCode(String tables) {
        sysGeneratorService.generatorCode(tables.split(","), false, null);
        return ResultVOUtil.success("代码生成成功");
    }

    @GetMapping("/generator/code_zip")
    @Operation(summary = "代码生成器(压缩下载)")
    public void makeCode(String tables, HttpServletResponse response) throws IOException {
        String zipPath = "CodeGenerator_" + DateTimeUtil.now(DateTimeUtil.FORMAT_2);
        byte[] data = sysGeneratorService.generatorCode(tables.split(","), true, zipPath);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + zipPath + ".zip");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

}
