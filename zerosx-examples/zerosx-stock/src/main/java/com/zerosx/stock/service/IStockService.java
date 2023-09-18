package com.zerosx.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerosx.api.examples.dto.CommodityDTO;
import com.zerosx.stock.entity.Stock;

public interface IStockService extends IService<Stock> {

    boolean decreaseStock(CommodityDTO commodityDTO);

    String decreaseStockRedis();

}
