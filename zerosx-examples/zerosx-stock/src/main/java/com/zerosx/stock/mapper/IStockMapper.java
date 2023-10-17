package com.zerosx.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerosx.stock.entity.Stock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStockMapper extends BaseMapper<Stock> {

    List<Stock> queryList(Stock stock);

}
