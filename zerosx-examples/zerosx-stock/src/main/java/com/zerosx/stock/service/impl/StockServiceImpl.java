package com.zerosx.stock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerosx.api.examples.dto.CommodityDTO;
import com.zerosx.stock.entity.Stock;
import com.zerosx.stock.mapper.IStockMapper;
import com.zerosx.stock.service.IStockService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StockServiceImpl extends ServiceImpl<IStockMapper, Stock> implements IStockService {

    @Override
    public boolean decreaseStock(CommodityDTO commodityDTO) {
        log.info("全局事务id:{}", RootContext.getXID());
        LambdaQueryWrapper<Stock> qw = Wrappers.lambdaQuery(Stock.class);
        qw.eq(Stock::getCommodityCode, commodityDTO.getCommodityCode());
        Stock dbStock = getOne(qw);
        Stock stock = new Stock();
        stock.setId(dbStock.getId());
        stock.setCommodityCode(commodityDTO.getCommodityCode());
        stock.setCount(dbStock.getCount() - 1);
        return updateById(stock);
    }
}
