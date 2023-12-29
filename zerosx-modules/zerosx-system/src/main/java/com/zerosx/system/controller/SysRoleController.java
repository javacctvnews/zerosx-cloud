package com.zerosx.system.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.BaseTenantDTO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.vo.SelectOptionVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.idempotent.anno.Idempotent;
import com.zerosx.idempotent.enums.IdempotentTypeEnum;
import com.zerosx.system.dto.SysRoleDTO;
import com.zerosx.system.dto.SysRolePageDTO;
import com.zerosx.system.service.ISysRoleService;
import com.zerosx.system.vo.SysRolePageVO;
import com.zerosx.system.vo.SysRoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
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
    @OpLog(mod = "角色管理", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/sys_role/page_list")
    public ResultVO<CustomPageVO<SysRolePageVO>> pageList(@RequestBody RequestVO<SysRolePageDTO> requestVO) {
        return ResultVOUtil.success(sysRoleService.pageList(requestVO, true));
    }

    @Operation(summary = "新增")
    @OpLog(mod = "角色管理", btn = "新增", opType = OpTypeEnum.INSERT)
    @PostMapping("/sys_role/save")
    @Idempotent(type = IdempotentTypeEnum.SPEL, spEL = "'sysRole_'+#sysRoleDTO.roleName")
    public ResultVO<?> add(@Validated @RequestBody SysRoleDTO sysRoleDTO) {
        return ResultVOUtil.successBoolean(sysRoleService.add(sysRoleDTO));
    }

    @Operation(summary = "编辑")
    @OpLog(mod = "角色管理", btn = "编辑", opType = OpTypeEnum.UPDATE)
    @PostMapping("/sys_role/update")
    @Idempotent(type = IdempotentTypeEnum.SPEL, spEL = "'sysRole_'+#sysRoleDTO.roleName")
    public ResultVO<?> update(@Validated @RequestBody SysRoleDTO sysRoleDTO) {
        return ResultVOUtil.successBoolean(sysRoleService.update(sysRoleDTO));
    }

    @Operation(summary = "按id查询")
    @GetMapping("/sys_role/queryById/{id}")
    public ResultVO<SysRoleVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(sysRoleService.queryById(id));
    }

    @Operation(summary = "删除")
    @OpLog(mod = "角色管理", btn = "删除", opType = OpTypeEnum.DELETE)
    @DeleteMapping("/sys_role/delete/{roleIds}")
    public ResultVO<?> deleteRecord(@PathVariable("roleIds") Long[] roleIds) {
        return ResultVOUtil.successBoolean(sysRoleService.deleteRecord(roleIds));
    }

    @Operation(summary = "导出")
    @OpLog(mod = "角色管理", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/sys_role/export")
    public void operatorExport(@RequestBody RequestVO<SysRolePageDTO> requestVO, HttpServletResponse response) throws IOException {
        sysRoleService.excelExport(requestVO, response);
    }

    @Operation(summary = "下拉框")
    @PostMapping("/sys_role/select_list")
    public ResultVO<List<SelectOptionVO>> selectList(@RequestBody BaseTenantDTO baseTenantDTO) {
        return ResultVOUtil.success(sysRoleService.selectOptions(baseTenantDTO));
    }

}
