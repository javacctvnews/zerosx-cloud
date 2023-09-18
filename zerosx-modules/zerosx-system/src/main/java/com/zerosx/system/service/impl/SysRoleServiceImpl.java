package com.zerosx.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.BeanCopierUtil;
import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.core.enums.RedisKeyNameEnum;
import com.zerosx.system.dto.SysRoleDTO;
import com.zerosx.system.dto.SysRoleMenuQueryDTO;
import com.zerosx.system.dto.SysRolePageDTO;
import com.zerosx.system.entity.SysRole;
import com.zerosx.system.entity.SysRoleMenu;
import com.zerosx.system.mapper.ISysRoleMapper;
import com.zerosx.system.service.ISysDeptService;
import com.zerosx.system.service.ISysMenuService;
import com.zerosx.system.service.ISysRoleMenuService;
import com.zerosx.system.service.ISysRoleService;
import com.zerosx.system.task.SystemAsyncTask;
import com.zerosx.system.vo.SysRoleMenuTreeVO;
import com.zerosx.system.vo.SysRolePageVO;
import com.zerosx.system.vo.SysRoleVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色信息表
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-27 17:53:15
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends SuperServiceImpl<ISysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private ISysRoleMenuService sysRoleMenuService;
    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private SystemAsyncTask systemAsyncTask;
    @Autowired
    private ISysDeptService sysDeptService;

    @Override
    public CustomPageVO<SysRolePageVO> pageList(RequestVO<SysRolePageDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), getWrapper(requestVO.getT())), SysRolePageVO.class);
    }

    @Override
    public List<SysRole> dataList(SysRolePageDTO query) {
        return list(getWrapper(query));
    }

    private static LambdaQueryWrapper<SysRole> getWrapper(SysRolePageDTO query) {
        LambdaQueryWrapper<SysRole> listqw = Wrappers.lambdaQuery(SysRole.class);
        listqw.eq(StringUtils.isNotBlank(query.getOperatorId()), SysRole::getOperatorId, query.getOperatorId());
        listqw.and(StringUtils.isNotBlank(query.getRoleKeyword()), wp -> wp.like(SysRole::getRoleName, query.getRoleKeyword()).or().like(SysRole::getRoleKey, query.getRoleKeyword()));
        listqw.eq(StringUtils.isNotBlank(query.getStatus()), SysRole::getStatus, query.getStatus());
        listqw.orderByAsc(SysRole::getRoleSort);
        return listqw;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean add(SysRoleDTO sysRoleDTO) {
        SysRole addEntity = BeanCopierUtil.copyProperties(sysRoleDTO, SysRole.class);
        if (StringUtils.isBlank(sysRoleDTO.getRoleKey())) {
            addEntity.setRoleKey(IdGenerator.getIdStr());
        }
        checkExistName(sysRoleDTO);
        boolean save = save(addEntity);
        if (save) {
            //保持角色与menu的关系表
            List<Long> menuIds = sysRoleDTO.getMenuIds();
            List<SysRoleMenu> as = new ArrayList<>();
            SysRoleMenu srm;
            for (Long menuId : menuIds) {
                srm = new SysRoleMenu();
                srm.setRoleId(addEntity.getId());
                srm.setMenuId(menuId);
                as.add(srm);
            }
            sysRoleMenuService.saveBatch(as);
        }
        return save;
    }

    private void checkExistName(SysRoleDTO sysRoleDTO) {
        LambdaQueryWrapper<SysRole> countqw = Wrappers.lambdaQuery(SysRole.class);
        countqw.eq(SysRole::getRoleName, sysRoleDTO.getRoleName());
        countqw.eq(SysRole::getOperatorId, sysRoleDTO.getOperatorId());
        countqw.ne(sysRoleDTO.getId() !=null, SysRole::getId, sysRoleDTO.getId());
        long count = count(countqw);
        if (count > 0) {
            throw new BusinessException("已存在【" + sysRoleDTO.getRoleName() + "】，不能重复添加");
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean update(SysRoleDTO sysRoleDTO) {
        SysRole dbUpdate = getById(sysRoleDTO.getId());
        if (dbUpdate == null) {
            throw new BusinessException("编辑记录不存在");
        }
        checkExistName(sysRoleDTO);
        //更新前删除改角色关联的权限缓存
        redissonOpService.del(RedisKeyNameEnum.key(RedisKeyNameEnum.ROLE_PERMISSIONS, sysRoleDTO.getId()));
        SysRole updateEntity = BeanCopierUtil.copyProperties(sysRoleDTO, SysRole.class);
        LambdaQueryWrapper<SysRoleMenu> srmqw = Wrappers.lambdaQuery(SysRoleMenu.class);
        srmqw.eq(SysRoleMenu::getRoleId, sysRoleDTO.getId());
        sysRoleMenuService.remove(srmqw);
        List<Long> menuIds = sysRoleDTO.getMenuIds();
        List<SysRoleMenu> as = new ArrayList<>();
        SysRoleMenu srm;
        for (Long menuId : menuIds) {
            srm = new SysRoleMenu();
            srm.setRoleId(updateEntity.getId());
            srm.setMenuId(menuId);
            as.add(srm);
        }
        sysRoleMenuService.saveBatch(as);
        boolean updateById = updateById(updateEntity);
        //延时删除
        systemAsyncTask.asyncRedisDelOptions(RedisKeyNameEnum.key(RedisKeyNameEnum.ROLE_PERMISSIONS, sysRoleDTO.getId()));
        return updateById;
    }

    @Override
    public SysRoleVO queryById(Long id) {
        SysRoleVO sysRoleVO = BeanCopierUtil.copyProperties(getById(id), SysRoleVO.class);
        SysRoleMenuQueryDTO dto = new SysRoleMenuQueryDTO();
        dto.setRoleId(id);
        SysRoleMenuTreeVO sysRoleMenuTreeVO = sysMenuService.roleMenuTree(dto);
        sysRoleVO.setSysRoleMenuTreeVO(sysRoleMenuTreeVO);
        return sysRoleVO;
    }

    @Override
    public boolean deleteRecord(Long[] ids) {
        return removeByIds(Arrays.asList(ids));
    }

    @Override
    public Set<Long> selectUserRoleIds(Long userId, Long deptId) {
        List<SysRoleVO> sysRoleVOS = selectUserRoles(userId, deptId);
        return sysRoleVOS.stream().map(SysRoleVO::getId).collect(Collectors.toSet());
    }

    @Override
    public List<SysRoleVO> selectUserRoles(Long userId, Long deptId) {
        List<SysRoleVO> resList = new ArrayList<>();
        //用户角色
        List<SysRoleVO> sysRoleVOS = baseMapper.selectRoleByUserId(userId);
        if (CollectionUtils.isNotEmpty(sysRoleVOS)) {
            resList.addAll(sysRoleVOS);
        }
        //用户所在部门的角色
        if (deptId == null) {
            return resList;
        }
        Set<Long> deptRoleIds = sysDeptService.getDeptRoleIds(deptId);
        if (deptRoleIds.isEmpty()) {
            return resList;
        }
        LambdaQueryWrapper<SysRole> srqw = Wrappers.lambdaQuery(SysRole.class);
        srqw.in(SysRole::getId, deptRoleIds);
        List<SysRole> sysRoles = baseMapper.selectList(srqw);
        if (CollectionUtils.isNotEmpty(sysRoles)) {
            List<SysRoleVO> deptRoles = BeanCopierUtil.copyPropertiesOfList(sysRoles, SysRoleVO.class);
            resList.addAll(deptRoles);
        }
        return resList;
    }
}
