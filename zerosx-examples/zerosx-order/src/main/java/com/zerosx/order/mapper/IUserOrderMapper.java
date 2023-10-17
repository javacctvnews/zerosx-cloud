package com.zerosx.order.mapper;

import com.zerosx.order.entity.UserOrder;
import com.zerosx.common.core.service.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户订单
 * 
 * @author javacctvnews
 * @date 2023-09-22 14:09:54
 */
@Mapper
public interface IUserOrderMapper extends SuperMapper<UserOrder> {

}
