package com.zerosx.api.examples.factory;

import com.zerosx.api.examples.IOrderControllerApi;
import com.zerosx.api.examples.dto.OrderDTO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderControllerApiFallbackFactory implements FallbackFactory<IOrderControllerApi> {

    @Override
    public IOrderControllerApi create(Throwable cause) {
        return new IOrderControllerApi() {
            @Override
            public ResultVO<?> createOrder(OrderDTO orderDTO) {
                return ResultVOUtil.feignFail("订单创建失败");
            }
        };
    }
}
