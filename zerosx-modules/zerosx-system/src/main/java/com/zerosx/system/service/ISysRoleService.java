package com.zerosx.system.service;

import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.system.dto.SysRoleDTO;
import com.zerosx.system.dto.SysRolePageDTO;
import com.zerosx.system.entity.SysRole;
import com.zerosx.system.vo.SysRolePageVO;
import com.zerosx.system.vo.SysRoleVO;

import java.util.List;
import java.util.Set;

/**
 * 角色信息表
 * @Description
 * @author javacctvnews
 * @date 2023-07-27 17:53:15
 */
public interface ISysRoleService extends ISuperService<SysRole> {

    /**
     * 分页查询
     * @param requestVO
     * @return
     */
    CustomPageVO<SysRolePageVO> pageList(RequestVO<SysRolePageDTO> requestVO, boolean searchCount);

    /**
     * 分页查询的data
     * @param query
     * @return
     */
    List<SysRole> dataList(SysRolePageDTO query);

    /**
     * 新增
     * @param sysRoleDTO
     * @return
     */
    boolean add(SysRoleDTO sysRoleDTO);

    /**
     * 编辑
     * @param sysRoleDTO
     * @return
     */
    boolean update(SysRoleDTO sysRoleDTO);

    /**
     * 按id查询
     * @param id
     * @return
     */
    SysRoleVO queryById(Long id);

    /**
     * 删除
     * @param ids
     * @return
     */
    boolean deleteRecord(Long[] ids);

    /**
     * 查询用户所有的角色（用户与角色、用户与部门-部门与角色）
     * @param userId
     * @return
     */
    Set<Long> selectUserRoleIds(Long userId, Long deptId);

    /**
     * 查询用户所有的角色（用户与角色、用户与部门-部门与角色）
     * @param userId
     * @return
     */
    List<SysRoleVO> selectUserRoles(Long userId, Long deptId);
}

