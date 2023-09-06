package com.zerosx.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerosx.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IOrderMapper extends BaseMapper<Order> {


}
