package com.zerosx.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.BeanCopierUtil;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.order.dto.UserOrderDTO;
import com.zerosx.order.dto.UserOrderPageDTO;
import com.zerosx.order.entity.UserOrder;
import com.zerosx.order.mapper.IUserOrderMapper;
import com.zerosx.order.service.IUserOrderService;
import com.zerosx.order.vo.UserOrderPageVO;
import com.zerosx.order.vo.UserOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 用户订单
 *
 * @author javacctvnews
 * @Description
 * @date 2023-09-12 16:21:49
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
        if (query == null) {
            return qw;
        }
        qw.eq(StringUtils.isNotBlank(query.getOrderNo()), UserOrder::getOrderNo, query.getOrderNo());
        qw.eq(StringUtils.isNotBlank(query.getUserId()), UserOrder::getUserId, query.getUserId());
        qw.eq(StringUtils.isNotBlank(query.getEmail()), UserOrder::getEmail, query.getEmail());
        qw.eq(StringUtils.isNotBlank(query.getPhone()), UserOrder::getPhone, query.getPhone());
        qw.eq(StringUtils.isNotBlank(query.getIdCard()), UserOrder::getIdCard, query.getIdCard());
        return qw;
    }

    @Override
    public boolean add(UserOrderDTO userOrderDTO) {
        UserOrder addEntity = BeanCopierUtil.copyProperties(userOrderDTO, UserOrder.class);
        return save(addEntity);
    }

    @Override
    public boolean update(UserOrderDTO userOrderDTO) {
        UserOrder dbUpdate = getById(userOrderDTO.getId());
        if (dbUpdate == null) {
            throw new BusinessException("编辑记录不存在");
        }
        UserOrder updateEntity = BeanCopierUtil.copyProperties(userOrderDTO, UserOrder.class);
        return updateById(updateEntity);
    }

    @Override
    public UserOrderVO queryById(Long id) {
        return BeanCopierUtil.copyProperties(getById(id), UserOrderVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(Long[] ids) {
        return removeByIds(Arrays.asList(ids));
    }

}
