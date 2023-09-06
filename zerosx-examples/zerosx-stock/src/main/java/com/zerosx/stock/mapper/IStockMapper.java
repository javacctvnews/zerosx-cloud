package com.zerosx.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerosx.stock.entity.Stock;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IStockMapper extends BaseMapper<Stock> {

}
