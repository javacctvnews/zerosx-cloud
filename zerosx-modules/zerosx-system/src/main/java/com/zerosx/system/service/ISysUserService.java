package com.zerosx.system.service;

import com.zerosx.api.system.dto.UserLoginDTO;
import com.zerosx.api.system.vo.LoginUserVO;
import com.zerosx.common.base.vo.LoginUserTenantsBO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.system.dto.SysUserDTO;
import com.zerosx.system.dto.SysUserPageDTO;
import com.zerosx.system.entity.SysUser;
import com.zerosx.system.vo.SysUserPageVO;
import com.zerosx.system.vo.SysUserVO;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-20 13:48:04
 */
public interface ISysUserService extends ISuperService<SysUser> {

    /**
     * 分页查询
     *
     * @param requestVO
     * @return
     */
    CustomPageVO<SysUserPageVO> pageList(RequestVO<SysUserPageDTO> requestVO, boolean searchCount);

    CustomPageVO<SysUser> pageList2(RequestVO<SysUserPageDTO> requestVO, boolean searchCount);

    /**
     * 分页查询的data
     *
     * @param query
     * @return
     */
    List<SysUser> dataList(SysUserPageDTO query);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    SysUserVO queryById(Long id);

    /**
     * 新增
     *
     * @param sysUserDTO
     * @return
     */
    boolean add(SysUserDTO sysUserDTO);

    /**
     * 编辑
     *
     * @param sysUserDTO
     * @return
     */
    boolean update(SysUserDTO sysUserDTO);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    boolean deleteRecord(Long[] id);

    LoginUserVO queryLoginUser(UserLoginDTO userLoginDTO);

    LoginUserTenantsBO currentLoginUser(String userName);

    Map<String, Object> getCurrentUserInfo();

    Map<String, Object> getUserProfile();

    boolean updateProfile(SysUserDTO sysUserDTO);

    /**
     * 导出Excel
     *
     * @param requestVO requestVO
     * @param response  response
     */
    void excelExport(RequestVO<SysUserPageDTO> requestVO, HttpServletResponse response);
}

