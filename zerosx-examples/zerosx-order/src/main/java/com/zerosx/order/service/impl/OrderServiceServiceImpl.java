package com.zerosx.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerosx.api.examples.IAccountControllerApi;
import com.zerosx.api.examples.dto.AccountDTO;
import com.zerosx.api.examples.dto.OrderDTO;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.utils.LeafUtils;
import com.zerosx.order.entity.Order;
import com.zerosx.order.mapper.IOrderMapper;
import com.zerosx.order.service.IOrderService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
        resultVO.checkException();
        //生成订单号
        orderDTO.setOrderNo(LeafUtils.idString());
        //生成订单
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        order.setCount(orderDTO.getOrderCount());
        order.setAmount(orderDTO.getOrderAmount());
        save(order);
        //打开注释测试事务发生异常后，全局回滚功能
        if (BigDecimal.ZERO.compareTo(orderDTO.getOrderAmount()) > 0) {
            throw new BusinessException("订单金额不能小于0");
        }
        log.debug("订单创建成功:{}", orderDTO.getOrderNo());
        return resultVO.getCode() == 0;
    }

}
