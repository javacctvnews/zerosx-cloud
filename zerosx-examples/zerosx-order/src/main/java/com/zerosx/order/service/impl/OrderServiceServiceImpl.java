package com.zerosx.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerosx.api.examples.IAccountControllerApi;
import com.zerosx.api.examples.dto.AccountDTO;
import com.zerosx.api.examples.dto.OrderDTO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.order.entity.Order;
import com.zerosx.order.mapper.IOrderMapper;
import com.zerosx.order.service.IOrderService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class OrderServiceServiceImpl extends ServiceImpl<IOrderMapper, Order> implements IOrderService {

    @Autowired
    private IAccountControllerApi accountControllerApi;

    @Override
    public boolean createOrder(OrderDTO orderDTO) {
        log.info("全局事务id:{}", RootContext.getXID());
        //扣减用户账户
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserId(orderDTO.getUserId());
        accountDTO.setAmount(orderDTO.getOrderAmount());
        ResultVO<?> resultVO = accountControllerApi.decreaseAccount(accountDTO);
        //生成订单号
        orderDTO.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        //生成订单
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        order.setCount(orderDTO.getOrderCount());
        order.setAmount(orderDTO.getOrderAmount().doubleValue());
        try {
            save(order);
        } catch (Exception e) {
            return false;
        }
        return resultVO.getCode() == 0;
    }

}
