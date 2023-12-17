package com.zerosx.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.core.utils.EasyTransUtils;
import org.springframework.stereotype.Service;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.utils.BeanCopierUtils;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.base.exception.BusinessException;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

import com.zerosx.order.entity.UserOrder;
import com.zerosx.order.mapper.IUserOrderMapper;
import com.zerosx.order.service.IUserOrderService;
import com.zerosx.order.vo.UserOrderPageVO;
import com.zerosx.order.dto.UserOrderPageDTO;
import com.zerosx.order.dto.UserOrderDTO;
import com.zerosx.order.vo.UserOrderVO;

/**
 * 用户订单
 * @Description
 * @author javacctvnews
 * @date 2023-09-22 14:09:54
 */
@Slf4j
@Service
public class UserOrderServiceImpl extends SuperServiceImpl<IUserOrderMapper, UserOrder> implements IUserOrderService {

    @Override
    public CustomPageVO<UserOrderPageVO> pageList(RequestVO<UserOrderPageDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), getWrapper(requestVO.getT())), UserOrderPageVO.class);
    }

    @Override
    public List<UserOrder> dataList(UserOrderPageDTO query) {
        return list(getWrapper(query));
    }

    private LambdaQueryWrapper<UserOrder> getWrapper(UserOrderPageDTO query) {
        LambdaQueryWrapper<UserOrder> qw = Wrappers.lambdaQuery(UserOrder.class);
        if(query == null){
            return qw;
        }
        //todo
        return qw;
    }

    @Override
    public boolean add(UserOrderDTO userOrderDTO) {
        UserOrder addEntity = BeanCopierUtils.copyProperties(userOrderDTO, UserOrder.class);
        return save(addEntity);
    }

    @Override
    public boolean update(UserOrderDTO userOrderDTO) {
        UserOrder dbUpdate = getById(userOrderDTO.getId());
        if (dbUpdate == null) {
            throw new BusinessException("编辑记录不存在");
        }
        UserOrder updateEntity = BeanCopierUtils.copyProperties(userOrderDTO, UserOrder.class);
        return updateById(updateEntity);
    }

   @Override
    public UserOrderVO queryById(Long id) {
        return EasyTransUtils.copyTrans(getById(id), UserOrderVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(Long[] ids) {
        return removeByIds(Arrays.asList(ids));
    }


    @Override
    public void excelExport(RequestVO<UserOrderPageDTO> requestVO, HttpServletResponse response) {
        excelExport(PageUtils.of(requestVO, false), getWrapper(requestVO.getT()), UserOrderPageVO.class, response);
    }

}
