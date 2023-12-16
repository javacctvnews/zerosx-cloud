package com.zerosx.resource.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.resource.core.IDGen;
import com.zerosx.resource.dto.LeafAllocDTO;
import com.zerosx.resource.dto.LeafAllocPageDTO;
import com.zerosx.resource.service.ILeafAllocService;
import com.zerosx.resource.vo.LeafAllocPageVO;
import com.zerosx.resource.vo.LeafAllocVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 美团分布式ID
 *
 * @author javacctvnews
 * @Description
 * @date 2023-12-05 14:00:46
 */
@Slf4j
@RestController
@Tag(name = "美团分布式ID")
public class LeafAllocController {

    @Autowired
    private ILeafAllocService leafAllocService;
    @Autowired
    private IDGen idGen;

    @Operation(summary = "分页列表")
    @OpLog(mod = "美团分布式ID", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/leaf_alloc/page_list")
    public ResultVO<CustomPageVO<LeafAllocPageVO>> pageList(@RequestBody RequestVO<LeafAllocPageDTO> requestVO) {
        return ResultVOUtil.success(leafAllocService.pageList(requestVO, true));
    }

    @Operation(summary = "新增")
    @OpLog(mod = "美团分布式ID", btn = "新增", opType = OpTypeEnum.INSERT)
    @PostMapping("/leaf_alloc/save")
    public ResultVO<?> add(@Validated @RequestBody LeafAllocDTO leafAllocDTO) {
        return ResultVOUtil.successBoolean(leafAllocService.add(leafAllocDTO));
    }

    @Operation(summary = "编辑")
    @OpLog(mod = "美团分布式ID", btn = "编辑", opType = OpTypeEnum.UPDATE)
    @PostMapping("/leaf_alloc/update")
    public ResultVO<?> update(@Validated @RequestBody LeafAllocDTO leafAllocDTO) {
        return ResultVOUtil.successBoolean(leafAllocService.update(leafAllocDTO));
    }

    @Operation(summary = "按id查询")
    @GetMapping("/leaf_alloc/queryById/{id}")
    public ResultVO<LeafAllocVO> queryById(@PathVariable String id) {
        return ResultVOUtil.success(leafAllocService.queryById(id));
    }

    @Operation(summary = "删除")
    @OpLog(mod = "美团分布式ID", btn = "删除", opType = OpTypeEnum.DELETE)
    @DeleteMapping("/leaf_alloc/delete/{bizTags}")
    public ResultVO<?> deleteRecord(@PathVariable("bizTags") String[] bizTags) {
        return ResultVOUtil.successBoolean(leafAllocService.deleteRecord(bizTags));
    }

    @Operation(summary = "导出")
    @OpLog(mod = "美团分布式ID", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/leaf_alloc/export")
    public void operatorExport(@RequestBody RequestVO<LeafAllocPageDTO> requestVO, HttpServletResponse response) throws IOException {
        leafAllocService.excelExport(requestVO, response);
    }

    @Operation(summary = "创建分布式ID(分段式)")
    @GetMapping(value = "/segment/{bizTag}")
    public ResultVO<Long> segment(@PathVariable("bizTag") String bizTag) {
        return ResultVOUtil.success(idGen.idLong(bizTag));
    }

}
