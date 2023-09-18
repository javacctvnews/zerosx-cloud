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
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.utils.BeanCopierUtil;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.log.annotation.SystemLog;
import com.zerosx.common.log.enums.BusinessType;

import com.zerosx.order.vo.UserOrderPageVO;
import com.zerosx.order.dto.UserOrderPageDTO;
import com.zerosx.order.dto.UserOrderDTO;
import com.zerosx.order.vo.UserOrderVO;
import com.zerosx.order.entity.UserOrder;
import com.zerosx.order.service.IUserOrderService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 用户订单
 * @Description
 * @author javacctvnews
 * @date 2023-09-12 16:21:49
 */
@Slf4j
@RestController
@Tag(name = "用户订单")
public class UserOrderController {

    @Autowired
    private IUserOrderService userOrderService;

    @Operation(summary ="分页列表")
    @SystemLog(title = "用户订单", btnName = "分页查询", businessType= BusinessType.QUERY)
    @PostMapping("/user_order/page_list")
    public ResultVO<CustomPageVO<UserOrderPageVO>> pageList(@RequestBody RequestVO<UserOrderPageDTO> requestVO){
        return ResultVOUtil.success(userOrderService.pageList(requestVO, true));
    }

    @Operation(summary ="新增")
    @SystemLog(title = "用户订单", btnName = "新增", businessType= BusinessType.INSERT)
    @PostMapping("/user_order/save")
    public ResultVO<?> add(@Validated @RequestBody UserOrderDTO userOrderDTO) {
        return ResultVOUtil.successBoolean(userOrderService.add(userOrderDTO));
    }

    @Operation(summary ="编辑")
    @SystemLog(title = "用户订单", btnName = "编辑", businessType= BusinessType.UPDATE)
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
    @SystemLog(title = "用户订单", btnName = "删除", businessType= BusinessType.DELETE)
    @DeleteMapping("/user_order/delete/{ids}")
    public ResultVO<?> deleteRecord(@PathVariable("ids") Long[] ids){
        return ResultVOUtil.successBoolean(userOrderService.deleteRecord(ids));
    }

    @Operation(summary = "导出")
    @SystemLog(title = "用户订单", btnName = "导出", businessType= BusinessType.EXPORT)
    @PostMapping("/user_order/export")
    public void operatorExport(@RequestBody RequestVO<UserOrderPageDTO> requestVO, HttpServletResponse response) throws IOException {
        long t1 = System.currentTimeMillis();
        CustomPageVO<UserOrderPageVO> pages = userOrderService.pageList(requestVO, false);
        EasyExcelUtil.writeExcel(response, pages.getList(), UserOrderPageVO.class);
        log.debug("【{}】执行导出{}条 耗时:{}ms", ZerosSecurityContextHolder.getUserName(), pages.getTotal(), System.currentTimeMillis() - t1);
    }

}
