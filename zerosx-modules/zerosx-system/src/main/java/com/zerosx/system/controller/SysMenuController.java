package com.zerosx.system.controller;

import com.zerosx.common.base.dto.RolePermissionDTO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.vo.SysPermissionBO;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.system.dto.SysMenuDTO;
import com.zerosx.system.dto.SysMenuPageDTO;
import com.zerosx.system.dto.SysRoleMenuQueryDTO;
import com.zerosx.system.entity.SysMenu;
import com.zerosx.system.service.ISysMenuService;
import com.zerosx.system.vo.RouterVO;
import com.zerosx.system.vo.SysMenuPageVO;
import com.zerosx.system.vo.SysRoleMenuTreeVO;
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
 * 菜单权限表
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-20 14:49:30
 */
@Slf4j
@RestController
@Tag(name = "菜单权限表")
public class SysMenuController {

    @Autowired
    private ISysMenuService sysMenuService;

    @Operation(summary = "分页列表")
    @OpLog(mod = "菜单权限表", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/menu/page_list")
    public ResultVO<CustomPageVO<SysMenuPageVO>> pageList(@RequestBody RequestVO<SysMenuPageDTO> requestVO) {
        return ResultVOUtil.success(sysMenuService.pageList(requestVO, true));
    }

    @Operation(summary = "新增")
    @OpLog(mod = "菜单权限表", btn = "新增", opType = OpTypeEnum.INSERT)
    @PostMapping("/menu/save")
    public ResultVO<?> add(@Validated @RequestBody SysMenuDTO sysMenuDTO) {
        return ResultVOUtil.successBoolean(sysMenuService.add(sysMenuDTO));
    }

    @Operation(summary = "编辑")
    @OpLog(mod = "菜单权限表", btn = "编辑", opType = OpTypeEnum.UPDATE)
    @PostMapping("/menu/update")
    public ResultVO<?> update(@Validated @RequestBody SysMenuDTO sysMenuDTO) {
        return ResultVOUtil.successBoolean(sysMenuService.update(sysMenuDTO));
    }

    @Operation(summary = "删除")
    @OpLog(mod = "菜单权限表", btn = "删除", opType = OpTypeEnum.DELETE)
    @DeleteMapping("/menu/delete/{menuId}")
    public ResultVO<?> deleteRecord(@PathVariable("menuId") Long menuId) {
        return ResultVOUtil.successBoolean(sysMenuService.deleteRecord(menuId));
    }

    @Operation(summary = "导出")
    @OpLog(mod = "菜单权限表", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/ys_menu/export")
    public void operatorExport(@RequestBody RequestVO<SysMenuPageDTO> requestVO, HttpServletResponse response) throws IOException {
        long t1 = System.currentTimeMillis();
        CustomPageVO<SysMenuPageVO> pageInfo = sysMenuService.pageList(requestVO, false);
        EasyExcelUtil.writeExcel(response, pageInfo.getList(), SysMenuPageVO.class);
        log.debug("【{}】执行导出{}条 耗时:{}ms", ZerosSecurityContextHolder.getUserName(), pageInfo.getTotal(), System.currentTimeMillis() - t1);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("/getRouters")
    @OpLog(mod = "菜单权限表", btn = "登录获取路由", opType = OpTypeEnum.QUERY)
    public ResultVO<List<RouterVO>> getRouters() {
        Long userId = ZerosSecurityContextHolder.getUserId();
        log.debug("用户id是【{}】的用户获取路由信息", userId);
        List<SysMenu> menus = sysMenuService.selectMenuTreeByUserId(userId);
        return ResultVOUtil.success(sysMenuService.buildMenus(menus));
    }

    /**
     * 获取菜单列表
     */
    @Operation(summary = "获取菜单列表")
    @PostMapping("/menu/list")
    @OpLog(mod = "菜单权限表", btn = "路由列表", opType = OpTypeEnum.QUERY)
    public ResultVO<List<SysMenu>> list(@RequestBody SysMenuPageDTO sysMenuPageDTO) {
        return ResultVOUtil.success(sysMenuService.selectMenuList(sysMenuPageDTO));
    }


    /**
     * 根据菜单编号获取详细信息
     *
     * @param menuId 菜单ID
     */
    @GetMapping(value = "/getMenuById/{menuId}")
    @Operation(summary = "根据菜单编号获取详细信息")
    public ResultVO<SysMenu> getMenuById(@PathVariable Long menuId) {
        return ResultVOUtil.success(sysMenuService.getMenuById(menuId));
    }

    @PostMapping("/sys_menu/roleMenuTree")
    @Operation(summary = "角色菜单树结构")
    public ResultVO<SysRoleMenuTreeVO> roleMenuTree(@RequestBody SysRoleMenuQueryDTO sysRoleMenuQueryDTO) {
        return ResultVOUtil.success(sysMenuService.roleMenuTree(sysRoleMenuQueryDTO));
    }

    /**
     * 查询角色对应的权限
     *
     * @param rolePermissionDTO
     * @return
     */
    @Operation(summary = "查询角色的权限")
    @PostMapping("/sys_menu/query_perms")
    public ResultVO<SysPermissionBO> queryPermsByRoleIds(@RequestBody RolePermissionDTO rolePermissionDTO) {
        return ResultVOUtil.success(sysMenuService.queryPermsByRoleIds(rolePermissionDTO));
    }

}
