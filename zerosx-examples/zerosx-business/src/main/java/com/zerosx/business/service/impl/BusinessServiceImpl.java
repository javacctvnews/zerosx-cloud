package com.zerosx.business.service.impl;

import com.zerosx.api.examples.IOrderControllerApi;
import com.zerosx.api.examples.IStockControllerApi;
import com.zerosx.api.examples.dto.BusinessDTO;
import com.zerosx.api.examples.dto.CommodityDTO;
import com.zerosx.api.examples.dto.OrderDTO;
import com.zerosx.business.service.IBusinessService;
import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.ResultVO;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BusinessServiceImpl implements IBusinessService {

    @Autowired
    private IStockControllerApi stockControllerApi;
    @Autowired
    private IOrderControllerApi orderControllerApi;

    @Override
    @GlobalTransactional(rollbackFor = Exception.class, timeoutMills = 300000)
    public boolean handleBusiness(BusinessDTO businessDTO) {
        log.info("开始全局事务，XID = {}", RootContext.getXID());
        //1、扣减库存
        CommodityDTO commodityDTO = new CommodityDTO();
        commodityDTO.setCommodityCode(businessDTO.getCommodityCode());
        commodityDTO.setCount(businessDTO.getCount());
        ResultVO<?> stockResult = stockControllerApi.decreaseStock(commodityDTO);
        //2、创建订单
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(businessDTO.getUserId());
        orderDTO.setCommodityCode(businessDTO.getCommodityCode());
        orderDTO.setOrderCount(businessDTO.getCount());
        orderDTO.setOrderAmount(businessDTO.getAmount());
        ResultVO<?> orderResult = orderControllerApi.createOrder(orderDTO);

        //打开注释测试事务发生异常后，全局回滚功能
        if (StringUtils.isNotBlank(businessDTO.getName())) {
            throw new BusinessException("测试抛异常后，分布式事务回滚！");
        }

        if (stockResult.getCode() != 0 || orderResult.getCode() != 0) {
            throw new BusinessException(ResultEnum.FAIL);
        }
        return false;
    }
}
