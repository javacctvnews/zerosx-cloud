package com.zerosx.order.controller;

import com.zerosx.common.core.vo.CustomPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;

import com.zerosx.order.vo.UserOrderPageVO;
import com.zerosx.order.dto.UserOrderPageDTO;
import com.zerosx.order.dto.UserOrderDTO;
import com.zerosx.order.vo.UserOrderVO;
import com.zerosx.order.service.IUserOrderService;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户订单
 * @Description
 * @author javacctvnews
 * @date 2023-09-22 14:09:54
 */
@Slf4j
@RestController
@Tag(name = "用户订单")
public class UserOrderController {

    @Autowired
    private IUserOrderService userOrderService;

    @Operation(summary ="分页列表")
    @OpLog(mod = "用户订单", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/user_order/page_list")
    public ResultVO<CustomPageVO<UserOrderPageVO>> pageList(@RequestBody RequestVO<UserOrderPageDTO> requestVO){
        return ResultVOUtil.success(userOrderService.pageList(requestVO, true));
    }

    @Operation(summary ="新增")
    @OpLog(mod = "用户订单", btn = "新增", opType = OpTypeEnum.INSERT)
    @PostMapping("/user_order/save")
    public ResultVO<?> add(@Validated @RequestBody UserOrderDTO userOrderDTO) {
        return ResultVOUtil.successBoolean(userOrderService.add(userOrderDTO));
    }

    @Operation(summary ="编辑")
    @OpLog(mod = "用户订单", btn = "编辑", opType = OpTypeEnum.UPDATE)
    @PostMapping("/user_order/update")
    public ResultVO<?> update(@Validated @RequestBody UserOrderDTO userOrderDTO) {
        return ResultVOUtil.successBoolean(userOrderService.update(userOrderDTO));
    }

    @Operation(summary ="按id查询")
    @GetMapping("/user_order/queryById/{id}")
    public ResultVO<UserOrderVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(userOrderService.queryById(id));
    }

    @Operation(summary ="删除")
    @OpLog(mod = "用户订单", btn = "删除", opType = OpTypeEnum.DELETE)
    @DeleteMapping("/user_order/delete/{ids}")
    public ResultVO<?> deleteRecord(@PathVariable("ids") Long[] ids){
        return ResultVOUtil.successBoolean(userOrderService.deleteRecord(ids));
    }

    @Operation(summary = "导出")
    @OpLog(mod = "用户订单", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/user_order/export")
    public void operatorExport(@RequestBody RequestVO<UserOrderPageDTO> requestVO, HttpServletResponse response) throws IOException {
        userOrderService.excelExport(requestVO, response);
    }

}
