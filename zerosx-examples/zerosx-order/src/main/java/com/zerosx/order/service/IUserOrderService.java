package com.zerosx.order.service;

import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.order.entity.UserOrder;
import com.zerosx.order.vo.UserOrderPageVO;
import com.zerosx.order.dto.UserOrderPageDTO;
import com.zerosx.order.dto.UserOrderDTO;
import com.zerosx.order.vo.UserOrderVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户订单
 * @Description
 * @author javacctvnews
 * @date 2023-09-22 14:09:54
 */
public interface IUserOrderService extends ISuperService<UserOrder> {

    /**
     *
     * @param requestVO
     * @param searchCount
     * @return
     */
    CustomPageVO<UserOrderPageVO> pageList(RequestVO<UserOrderPageDTO> requestVO, boolean searchCount);

    /**
     * 分页查询的data
     * @param query
     * @return
     */
    List<UserOrder> dataList(UserOrderPageDTO query);

    /**
     * 新增
     * @param userOrderDTO
     * @return
     */
    boolean add(UserOrderDTO userOrderDTO);

    /**
     * 编辑
     * @param userOrderDTO
     * @return
     */
    boolean update(UserOrderDTO userOrderDTO);

    /**
     * 按id查询
     * @param id
     * @return
     */
    UserOrderVO queryById(Long id);

    /**
     * 删除
     * @param ids
     * @return
     */
    boolean deleteRecord(Long[] ids);

    /**
     * 导出Excel
     *
     * @param requestVO requestVO
     * @param response  response
     */
    void excelExport(RequestVO<UserOrderPageDTO> requestVO, HttpServletResponse response);

}

