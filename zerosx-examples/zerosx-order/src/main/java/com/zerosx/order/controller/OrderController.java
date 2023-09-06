package com.zerosx.order.controller;

import com.zerosx.api.examples.IOrderControllerApi;
import com.zerosx.api.examples.dto.OrderDTO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.order.service.IOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "订单中心")
@RestController
public class OrderController implements IOrderControllerApi {

    @Autowired
    private IOrderService orderService;

    @PostMapping("/create_order")
    public ResultVO<?> createOrder(@RequestBody OrderDTO orderDTO){
        return ResultVOUtil.successBoolean(orderService.createOrder(orderDTO));
    }
}
