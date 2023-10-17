package com.zerosx.stock;

import com.zerosx.stock.entity.Stock;
import com.zerosx.stock.mapper.IStockMapper;
import com.zerosx.stock.service.IStockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * EncryptDemo
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-23 13:14
 **/
@SpringBootTest(classes = StockApplication.class)
public class EncryptDemo {

    @Autowired
    private IStockService stockService;
    @Autowired
    private IStockMapper stockMapper;

    @Test
    public void test01(){
        Stock stock = new Stock();
        stock.setCount(1);
        stock.setName("123445");
        stock.setCommodityCode("123333");
        stockService.save(stock);
    }

    @Test
    public void test02(){
        Stock stock = new Stock();
        List<Stock> stocks = stockMapper.queryList(stock);

    }
}
