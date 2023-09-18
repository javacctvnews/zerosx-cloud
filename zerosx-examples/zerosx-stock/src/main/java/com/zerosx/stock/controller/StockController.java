package com.zerosx.stock.controller;

import com.zerosx.api.examples.IStockControllerApi;
import com.zerosx.api.examples.dto.CommodityDTO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.stock.service.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController implements IStockControllerApi {

    @Autowired
    private IStockService stockService;

    @PostMapping("/decrease_stock")
    public ResultVO<?> decreaseStock(@RequestBody CommodityDTO commodityDTO) {
        return ResultVOUtil.successBoolean(stockService.decreaseStock(commodityDTO));
    }

    @PostMapping("/decrease_stock/redis")
    public ResultVO<?> decreaseStockRedis() {
        return ResultVOUtil.success(stockService.decreaseStockRedis());
    }

}
