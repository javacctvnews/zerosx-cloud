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

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

import com.zerosx.order.entity.User;
import com.zerosx.order.mapper.IUserMapper;
import com.zerosx.order.service.IUserService;
import com.zerosx.order.vo.UserPageVO;
import com.zerosx.order.dto.UserPageDTO;
import com.zerosx.order.dto.UserDTO;
import com.zerosx.order.vo.UserVO;

/**
 * 加解密DEMO
 * @Description
 * @author javacctvnews
 * @date 2023-09-22 15:32:00
 */
@Slf4j
@Service
public class UserServiceImpl extends SuperServiceImpl<IUserMapper, User> implements IUserService {

    @Override
    public CustomPageVO<UserPageVO> pageList(RequestVO<UserPageDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), getWrapper(requestVO.getT())), UserPageVO.class);
    }

    @Override
    public List<User> dataList(UserPageDTO query) {
        return list(getWrapper(query));
    }

    private LambdaQueryWrapper<User> getWrapper(UserPageDTO query) {
        LambdaQueryWrapper<User> qw = Wrappers.lambdaQuery(User.class);
        if(query == null){
            return qw;
        }
        //todo
        return qw;
    }

    @Override
    public boolean add(UserDTO userDTO) {
        User addEntity = BeanCopierUtils.copyProperties(userDTO, User.class);
        return save(addEntity);
    }

    @Override
    public boolean update(UserDTO userDTO) {
        User dbUpdate = getById(userDTO.getId());
        if (dbUpdate == null) {
            throw new BusinessException("编辑记录不存在");
        }
        User updateEntity = BeanCopierUtils.copyProperties(userDTO, User.class);
        return updateById(updateEntity);
    }

   @Override
    public UserVO queryById(Long id) {
        return EasyTransUtils.copyTrans(getById(id), UserVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(Long[] ids) {
        return removeByIds(Arrays.asList(ids));
    }


    @Override
    public void excelExport(RequestVO<UserPageDTO> requestVO, HttpServletResponse response) {
        excelExport(PageUtils.of(requestVO, false), getWrapper(requestVO.getT()), UserPageVO.class, response);
    }

}
