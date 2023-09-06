package com.zerosx.api.examples;

import com.zerosx.api.examples.dto.OrderDTO;
import com.zerosx.api.examples.factory.OrderControllerApiFallbackFactory;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "zerosx-order", contextId = "zerosx-order", fallbackFactory = OrderControllerApiFallbackFactory.class)
public interface IOrderControllerApi {

    @PostMapping("/create_order")
    ResultVO<?> createOrder(@RequestBody OrderDTO orderDTO);

}
