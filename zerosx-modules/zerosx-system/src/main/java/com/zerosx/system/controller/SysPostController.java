package com.zerosx.system.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.annotation.SystemLog;
import com.zerosx.common.log.enums.BusinessType;
import com.zerosx.system.dto.SysPostDTO;
import com.zerosx.system.dto.SysPostPageDTO;
import com.zerosx.system.entity.SysPost;
import com.zerosx.system.service.ISysPostService;
import com.zerosx.system.vo.SysPostPageVO;
import com.zerosx.system.vo.SysPostVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 岗位管理
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-28 15:40:01
 */
@Slf4j
@RestController
@Tag(name = "岗位管理")
public class SysPostController {

    @Autowired
    private ISysPostService sysPostService;

    @Operation(summary = "分页列表")
    @SystemLog(title = "岗位管理", btnName = "分页查询", businessType = BusinessType.QUERY)
    @PostMapping("/sys_post/page_list")
    public ResultVO<CustomPageVO<SysPostPageVO>> pageList(@RequestBody RequestVO<SysPostPageDTO> requestVO) {
        return ResultVOUtil.success(sysPostService.pageList(requestVO, true));
    }

    @Operation(summary = "新增")
    @SystemLog(title = "岗位管理", btnName = "新增", businessType = BusinessType.INSERT)
    @PostMapping("/sys_post/save")
    public ResultVO<?> add(@Validated @RequestBody SysPostDTO sysPostDTO) {
        return ResultVOUtil.successBoolean(sysPostService.add(sysPostDTO));
    }

    @Operation(summary = "编辑")
    @SystemLog(title = "岗位管理", btnName = "编辑", businessType = BusinessType.UPDATE)
    @PostMapping("/sys_post/update")
    public ResultVO<?> update(@Validated @RequestBody SysPostDTO sysPostDTO) {
        return ResultVOUtil.successBoolean(sysPostService.update(sysPostDTO));
    }

    @Operation(summary = "按id查询")
    @GetMapping("/sys_post/queryById/{id}")
    public ResultVO<SysPostVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(sysPostService.queryById(id));
    }

    @Operation(summary = "删除")
    @SystemLog(title = "岗位管理", btnName = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/sys_post/delete/{ids}")
    public ResultVO<?> deleteRecord(@PathVariable("ids") Long[] ids) {
        return ResultVOUtil.successBoolean(sysPostService.deleteRecord(ids));
    }

    @Operation(summary = "导出")
    @SystemLog(title = "岗位管理", btnName = "导出", businessType = BusinessType.EXPORT)
    @PostMapping("/sys_post/export")
    public void operatorExport(@RequestBody RequestVO<SysPostPageDTO> requestVO, HttpServletResponse response) throws IOException {
        long t1 = System.currentTimeMillis();
        CustomPageVO<SysPostPageVO> pages = sysPostService.pageList(requestVO, false);
        EasyExcelUtil.writeExcel(response, pages.getList(), SysPostPageVO.class);
        log.debug("【{}】执行导出{}条 耗时:{}ms", ZerosSecurityContextHolder.getUserName(), pages.getTotal(), System.currentTimeMillis() - t1);
    }

    @Operation(summary = "下拉框")
    @PostMapping("/sys_post/select_list")
    public ResultVO<List<SysPost>> selectList(@RequestBody SysPostPageDTO sysPostPageDTO) {
        return ResultVOUtil.success(sysPostService.dataList(sysPostPageDTO));
    }
}
