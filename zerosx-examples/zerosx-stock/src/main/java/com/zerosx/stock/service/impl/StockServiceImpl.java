package com.zerosx.stock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerosx.api.examples.dto.CommodityDTO;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.stock.entity.Stock;
import com.zerosx.stock.mapper.IStockMapper;
import com.zerosx.stock.service.IStockService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StockServiceImpl extends ServiceImpl<IStockMapper, Stock> implements IStockService {

    @Autowired
    private RedissonOpService redissonOpService;

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

    private static final String LOCK_STOCK = "stock_lock";
    private static final String STOCK = "stock";

    @Override
    public String decreaseStockRedis() {
        //获取锁对象
        RLock lock = redissonOpService.getRedissonClient().getLock(LOCK_STOCK);
        try {
            //boolean lock = redissonOpService.setIfAbsent(LOCK_STOCK, requestId, 10);
            if (lock == null) {
                System.out.println("系统繁忙，请稍后再试");
                return "系统繁忙，请稍后再试";
            }
            lock.lock();
            Integer stockNum = redissonOpService.get(STOCK);
            if (stockNum == null || stockNum <= 0) {
                System.out.println("已经抢购完了啦，谢谢参与");
                return "已经抢购完了啦，谢谢参与";
            }
            //减少库存
            Integer decStockNum = stockNum - 1;
            redissonOpService.set(STOCK, decStockNum);
            System.out.println("库存扣减成功，剩余：" + decStockNum);
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
        return "抢购成功！！！";
    }
}
