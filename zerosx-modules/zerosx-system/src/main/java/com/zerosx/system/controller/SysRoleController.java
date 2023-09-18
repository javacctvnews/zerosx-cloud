package com.zerosx.system.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.annotation.SystemLog;
import com.zerosx.common.log.enums.BusinessType;
import com.zerosx.system.dto.SysRoleDTO;
import com.zerosx.system.dto.SysRolePageDTO;
import com.zerosx.system.entity.SysRole;
import com.zerosx.system.service.ISysRoleService;
import com.zerosx.system.vo.SysRolePageVO;
import com.zerosx.system.vo.SysRoleVO;
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
 * 角色管理
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-27 17:53:15
 */
@Slf4j
@RestController
@Tag(name = "角色管理")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    @Operation(summary = "分页列表")
    @SystemLog(title = "角色管理", btnName = "分页查询", businessType = BusinessType.QUERY)
    @PostMapping("/sys_role/page_list")
    public ResultVO<CustomPageVO<SysRolePageVO>> pageList(@RequestBody RequestVO<SysRolePageDTO> requestVO) {
        return ResultVOUtil.success(sysRoleService.pageList(requestVO, true));
    }

    @Operation(summary = "新增")
    @SystemLog(title = "角色管理", btnName = "新增", businessType = BusinessType.INSERT)
    @PostMapping("/sys_role/save")
    public ResultVO<?> add(@Validated @RequestBody SysRoleDTO sysRoleDTO) {
        return ResultVOUtil.successBoolean(sysRoleService.add(sysRoleDTO));
    }

    @Operation(summary = "编辑")
    @SystemLog(title = "角色管理", btnName = "编辑", businessType = BusinessType.UPDATE)
    @PostMapping("/sys_role/update")
    public ResultVO<?> update(@Validated @RequestBody SysRoleDTO sysRoleDTO) {
        return ResultVOUtil.successBoolean(sysRoleService.update(sysRoleDTO));
    }

    @Operation(summary = "按id查询")
    @GetMapping("/sys_role/queryById/{id}")
    public ResultVO<SysRoleVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(sysRoleService.queryById(id));
    }

    @Operation(summary = "删除")
    @SystemLog(title = "角色管理", btnName = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/sys_role/delete/{roleIds}")
    public ResultVO<?> deleteRecord(@PathVariable("roleIds") Long[] roleIds) {
        return ResultVOUtil.successBoolean(sysRoleService.deleteRecord(roleIds));
    }

    @Operation(summary = "导出")
    @SystemLog(title = "角色管理", btnName = "导出", businessType = BusinessType.EXPORT)
    @PostMapping("/sys_role/export")
    public void operatorExport(@RequestBody RequestVO<SysRolePageDTO> requestVO, HttpServletResponse response) throws IOException {
        long t1 = System.currentTimeMillis();
        CustomPageVO<SysRolePageVO> pages = sysRoleService.pageList(requestVO, false);
        EasyExcelUtil.writeExcel(response, pages.getList(), SysRolePageVO.class);
        log.debug("【{}】执行导出{}条 耗时:{}ms", ZerosSecurityContextHolder.getUserName(), pages.getTotal(), System.currentTimeMillis() - t1);
    }

    @Operation(summary = "下拉框")
    @PostMapping("/sys_role/select_list")
    public ResultVO<List<SysRole>> selectList(@RequestBody SysRolePageDTO sysRolePageDTO) {
        return ResultVOUtil.success(sysRoleService.dataList(sysRolePageDTO));
    }

}
