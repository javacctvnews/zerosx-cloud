package com.zerosx.api.examples;

import com.zerosx.api.examples.dto.CommodityDTO;
import com.zerosx.api.examples.factory.StockControllerApiFallbackFactory;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "zerosx-stock", contextId = "zerosx-stock", fallbackFactory = StockControllerApiFallbackFactory.class)
public interface IStockControllerApi {

    @PostMapping("/decrease_stock")
    ResultVO<?> decreaseStock(@RequestBody CommodityDTO commodityDTO);

}
