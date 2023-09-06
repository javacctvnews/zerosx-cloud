package com.zerosx.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerosx.api.examples.dto.OrderDTO;
import com.zerosx.order.entity.Order;

public interface IOrderService extends IService<Order> {

    boolean createOrder(OrderDTO orderDTO);

}
