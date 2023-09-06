package com.zerosx.system.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.vo.SelectOptionVO;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.annotation.SystemLog;
import com.zerosx.common.log.enums.BusinessType;
import com.zerosx.system.dto.MutiTenancyGroupEditDTO;
import com.zerosx.system.dto.MutiTenancyGroupQueryDTO;
import com.zerosx.system.dto.MutiTenancyGroupSaveDTO;
import com.zerosx.system.service.IMutiTenancyGroupService;
import com.zerosx.system.vo.MutiTenancyGroupPageVO;
import com.zerosx.system.vo.MutiTenancyGroupVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@Tag(name = "多租户集团")
@Slf4j
public class MutiTenancyGroupController {

    @Autowired
    private IMutiTenancyGroupService mutiTenancyGroupService;

    @Operation(summary = "分页列表")
    @PostMapping("/muti_tenancy/list_pages")
    @SystemLog(title = "多租户集团", btnName = "分页查询", businessType = BusinessType.QUERY)
    public ResultVO<CustomPageVO<MutiTenancyGroupPageVO>> listPages(@RequestBody RequestVO<MutiTenancyGroupQueryDTO> requestVO) {
        return ResultVOUtil.success(mutiTenancyGroupService.listPages(requestVO, true));
    }

    @Operation(summary = "保存")
    @PostMapping("/muti_tenancy/save")
    @SystemLog(title = "多租户集团", btnName = "新增", businessType = BusinessType.INSERT)
    public ResultVO saveMutiTenancyGroup(@Validated @RequestBody MutiTenancyGroupSaveDTO mutiTenancyGroupSaveDTO) {
        return ResultVOUtil.success(mutiTenancyGroupService.saveMutiTenancyGroup(mutiTenancyGroupSaveDTO));
    }

    @Operation(summary = "更新")
    @PostMapping("/muti_tenancy/update")
    @SystemLog(title = "多租户集团", btnName = "编辑", businessType = BusinessType.UPDATE)
    public ResultVO updateGroupCompany(@Validated @RequestBody MutiTenancyGroupEditDTO mutiTenancyGroupEditDTO) throws Exception {
        return ResultVOUtil.success(mutiTenancyGroupService.editMutiTenancyGroup(mutiTenancyGroupEditDTO));
    }

    @Operation(summary = "删除")
    @SystemLog(title = "多租户集团", btnName = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/muti_tenancy/deleted/{ids}")
    public ResultVO deleteGroupCompany(@PathVariable("ids") Long[] ids) {
        return ResultVOUtil.success(mutiTenancyGroupService.deleteGroupCompany(ids));
    }

    @Operation(summary = "下拉框数据")
    @PostMapping("/muti_tenancy/select_options")
    public ResultVO<List<SelectOptionVO>> selectOptions() {
        return ResultVOUtil.success(mutiTenancyGroupService.selectOptions());
    }

    @Operation(summary = "导出")
    @SystemLog(title = "多租户集团", btnName = "导出", businessType = BusinessType.EXPORT)
    @PostMapping("/muti_tenancy/export")
    public void operatorExport(@RequestBody RequestVO<MutiTenancyGroupQueryDTO> requestVO, HttpServletResponse response) throws IOException {
        long t1 = System.currentTimeMillis();
        CustomPageVO<MutiTenancyGroupPageVO> pages = mutiTenancyGroupService.listPages(requestVO, false);
        EasyExcelUtil.writeExcel(response, pages.getList(), MutiTenancyGroupPageVO.class);
        log.debug("【{}】执行导出{}条 耗时:{}ms", ZerosSecurityContextHolder.getUserName(), pages.getTotal(), System.currentTimeMillis() - t1);
    }

    @Operation(summary = "按id查询")
    @SystemLog(title = "多租户集团", btnName = "按id查询", businessType = BusinessType.QUERY)
    @GetMapping("/muti_tenancy/getById/{id}")
    public ResultVO<MutiTenancyGroupVO> getById(@PathVariable("id") Long id) {
        return ResultVOUtil.success(mutiTenancyGroupService.getTenantById(id));
    }

    /**
     * 按租户ID查询全称
     *
     * @param operatorId
     * @return
     */
    @Operation(summary = "按租户ID查询全称")
    @PostMapping("/muti_tenancy/tenantName")
    public ResultVO<String> transIdName(@RequestParam(value = "operatorId", required = false) String operatorId) {
        return ResultVOUtil.success(mutiTenancyGroupService.transIdName(operatorId));
    }

}

