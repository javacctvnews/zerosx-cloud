package com.zerosx.order.service;

import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.order.entity.User;
import com.zerosx.order.vo.UserPageVO;
import com.zerosx.order.dto.UserPageDTO;
import com.zerosx.order.dto.UserDTO;
import com.zerosx.order.vo.UserVO;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 加解密DEMO
 * @Description
 * @author javacctvnews
 * @date 2023-09-22 15:32:00
 */
public interface IUserService extends ISuperService<User> {

    /**
     *
     * @param requestVO
     * @param searchCount
     * @return
     */
    CustomPageVO<UserPageVO> pageList(RequestVO<UserPageDTO> requestVO, boolean searchCount);

    /**
     * 分页查询的data
     * @param query
     * @return
     */
    List<User> dataList(UserPageDTO query);

    /**
     * 新增
     * @param userDTO
     * @return
     */
    boolean add(UserDTO userDTO);

    /**
     * 编辑
     * @param userDTO
     * @return
     */
    boolean update(UserDTO userDTO);

    /**
     * 按id查询
     * @param id
     * @return
     */
    UserVO queryById(Long id);

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
    void excelExport(RequestVO<UserPageDTO> requestVO, HttpServletResponse response);

}

