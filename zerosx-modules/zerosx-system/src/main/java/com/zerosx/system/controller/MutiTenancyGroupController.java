package com.zerosx.system.controller;

import com.zerosx.api.system.vo.MutiTenancyGroupBO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.vo.SelectOptionVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.idempotent.anno.Idempotent;
import com.zerosx.idempotent.enums.IdempotentTypeEnum;
import com.zerosx.system.dto.MutiTenancyGroupEditDTO;
import com.zerosx.system.dto.MutiTenancyGroupQueryDTO;
import com.zerosx.system.dto.MutiTenancyGroupSaveDTO;
import com.zerosx.system.service.IMutiTenancyGroupService;
import com.zerosx.system.vo.MutiTenancyGroupPageVO;
import com.zerosx.system.vo.MutiTenancyGroupVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @OpLog(mod = "多租户集团", btn = "分页查询", opType = OpTypeEnum.QUERY)
    public ResultVO<CustomPageVO<MutiTenancyGroupPageVO>> listPages(@RequestBody RequestVO<MutiTenancyGroupQueryDTO> requestVO) {
        return ResultVOUtil.success(mutiTenancyGroupService.listPages(requestVO, true));
    }

    @Operation(summary = "保存")
    @PostMapping("/muti_tenancy/save")
    @Idempotent(type = IdempotentTypeEnum.SPEL, spEL = "#mutiTenancyGroupSaveDTO.socialCreditCode")
    @OpLog(mod = "多租户集团", btn = "新增", opType = OpTypeEnum.INSERT)
    public ResultVO saveMutiTenancyGroup(@Validated @RequestBody MutiTenancyGroupSaveDTO mutiTenancyGroupSaveDTO) {
        return ResultVOUtil.success(mutiTenancyGroupService.saveMutiTenancyGroup(mutiTenancyGroupSaveDTO));
    }

    @Operation(summary = "更新")
    @PostMapping("/muti_tenancy/update")
    @Idempotent(type = IdempotentTypeEnum.SPEL, spEL = "#mutiTenancyGroupEditDTO.socialCreditCode")
    @OpLog(mod = "多租户集团", btn = "编辑", opType = OpTypeEnum.UPDATE)
    public ResultVO updateGroupCompany(@Validated @RequestBody MutiTenancyGroupEditDTO mutiTenancyGroupEditDTO) {
        return ResultVOUtil.success(mutiTenancyGroupService.editMutiTenancyGroup(mutiTenancyGroupEditDTO));
    }

    @Operation(summary = "删除")
    @OpLog(mod = "多租户集团", btn = "删除", opType = OpTypeEnum.DELETE)
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
    @OpLog(mod = "多租户集团", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/muti_tenancy/export")
    public void operatorExport(@RequestBody RequestVO<MutiTenancyGroupQueryDTO> requestVO, HttpServletResponse response) throws IOException {
        mutiTenancyGroupService.excelExport(requestVO, response);
    }

    @Operation(summary = "按id查询")
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

    @Operation(summary = "按租户ID查询租户公司信息")
    @PostMapping("/muti_tenancy/queryOperator")
    public ResultVO<MutiTenancyGroupBO> queryOperator(@RequestParam(value = "operatorId", required = false) String operatorId) {
        return ResultVOUtil.success(mutiTenancyGroupService.queryOperator(operatorId));
    }

}

