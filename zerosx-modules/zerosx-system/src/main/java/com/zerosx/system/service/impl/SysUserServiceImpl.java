package com.zerosx.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.api.system.dto.UserLoginDTO;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.LoginUserTenantsBO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.SysMenuBO;
import com.zerosx.common.base.vo.SysRoleBO;
import com.zerosx.common.core.enums.UserTypeEnum;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.BeanCopierUtil;
import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.common.base.utils.JacksonUtil;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.core.vo.CustomUserDetails;
import com.zerosx.common.encrypt.core.ICustomEncryptor;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.redis.enums.RedisKeyNameEnum;
import com.zerosx.system.dto.SysUserDTO;
import com.zerosx.system.dto.SysUserPageDTO;
import com.zerosx.system.entity.*;
import com.zerosx.system.mapper.ISysUserMapper;
import com.zerosx.system.service.*;
import com.zerosx.system.task.SystemAsyncTask;
import com.zerosx.system.vo.SysRoleVO;
import com.zerosx.system.vo.SysUserPageVO;
import com.zerosx.system.vo.SysUserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统用户
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-20 13:48:04
 */
@Slf4j
@Service
public class SysUserServiceImpl extends SuperServiceImpl<ISysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private ISysUserMapper sysUserMapper;
    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private ISysUserPostService sysUserPostService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private SystemAsyncTask systemAsyncTask;
    @Autowired
    private ISysPostService sysPostService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ICustomEncryptor customEncryptor;

    @Override
    public CustomPageVO<SysUserPageVO> pageList(RequestVO<SysUserPageDTO> requestVO, boolean searchCount) {
        Map<Long, String> deptMap = new HashMap<>();
        return PageUtils.of(sysUserMapper.selectPage(PageUtils.of(requestVO, searchCount), getWrapper(requestVO.getT())).convert((e) -> {
            SysUserPageVO tarVO = BeanCopierUtil.copyProperties(e, SysUserPageVO.class);
            String deptName = deptMap.get(e.getDeptId());
            if (StringUtils.isBlank(deptName)) {
                SysDept sysDept = sysDeptService.getById(e.getDeptId());
                if (sysDept != null) {
                    deptMap.put(e.getDeptId(), sysDept.getDeptName());
                    deptName = sysDept.getDeptName();
                }
            }
            tarVO.setDeptName(deptName);
            return tarVO;
        }));
    }

    @Override
    public List<SysUser> dataList(SysUserPageDTO query) {
        return list(getWrapper(query));
    }

    private LambdaQueryWrapper<SysUser> getWrapper(SysUserPageDTO query) {
        LambdaQueryWrapper<SysUser> qw = Wrappers.lambdaQuery(SysUser.class);
        qw.eq(StringUtils.isNotBlank(query.getOperatorId()), SysUser::getOperatorId, query.getOperatorId());
        qw.eq(StringUtils.isNotBlank(query.getStatus()), SysUser::getStatus, query.getStatus());
        qw.like(StringUtils.isNotBlank(query.getPhoneNumber()), SysUser::getPhoneNumber, customEncryptor.encrypt(query.getPhoneNumber()));
        qw.and(StringUtils.isNotBlank(query.getUserKeyword()), wq -> wq.like(SysUser::getUserName, query.getUserKeyword()).or().like(SysUser::getNickName, query.getUserKeyword()));
        qw.orderByDesc(SysUser::getCreateTime);
        //SysUser sysUser = BeanCopierUtil.copyProperties(query, SysUser.class);
        //qw.setEntity(sysUser);
        return qw;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean add(SysUserDTO sysUserDTO) {
        SysUser addEntity = BeanCopierUtil.copyProperties(sysUserDTO, SysUser.class);
        addEntity.setUserCode(IdGenerator.getIdStr());
        addEntity.setPassword(passwordEncoder.encode(sysUserDTO.getPassword()));
        boolean save = save(addEntity);
        //保存用户与角色的关系
        sysUserRoleService.saveUserRoleIds(addEntity.getId(), sysUserDTO.getRoleIds(), false);
        //保存用户与岗位的关系
        sysUserPostService.saveUserPostIds(addEntity.getId(), sysUserDTO.getPostIds(), false);
        return save;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean update(SysUserDTO sysUserDTO) {
        SysUser dbUpdate = getById(sysUserDTO.getId());
        if (dbUpdate == null) {
            throw new BusinessException("编辑记录不存在");
        }
        //先删除缓存在更新 删除后会查询数据库
        redissonOpService.del(RedisKeyNameEnum.key(RedisKeyNameEnum.CURRENT_USER, sysUserDTO.getUserName()));

        SysUser updateEntity = BeanCopierUtil.copyProperties(sysUserDTO, SysUser.class);
        //重新保存关系表
        sysUserRoleService.saveUserRoleIds(dbUpdate.getId(), sysUserDTO.getRoleIds(), true);
        //保存用户与岗位的关系
        sysUserPostService.saveUserPostIds(dbUpdate.getId(), sysUserDTO.getPostIds(), true);
        boolean updateById = updateById(updateEntity);
        //等数据更新完成后删除(延时删除)
        systemAsyncTask.asyncRedisDelOptions(RedisKeyNameEnum.key(RedisKeyNameEnum.CURRENT_USER, sysUserDTO.getUserName()));
        return updateById;
    }

    @Override
    public SysUserVO queryById(Long id) {
        SysUserVO sysUserVO = BeanCopierUtil.copyProperties(getById(id), SysUserVO.class);
        LambdaQueryWrapper<SysUserRole> surqw = Wrappers.lambdaQuery(SysUserRole.class);
        surqw.eq(SysUserRole::getUserId, id);
        List<SysUserRole> roles = sysUserRoleService.list(surqw);
        if (CollectionUtils.isNotEmpty(roles)) {
            sysUserVO.setRoleIds(roles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList()));
        }
        LambdaQueryWrapper<SysUserPost> supqw = Wrappers.lambdaQuery(SysUserPost.class);
        supqw.eq(SysUserPost::getUserId, id);
        List<SysUserPost> posts = sysUserPostService.list(supqw);
        if (CollectionUtils.isNotEmpty(posts)) {
            sysUserVO.setPostIds(posts.stream().map(SysUserPost::getPostId).collect(Collectors.toList()));
        }
        return sysUserVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(Long[] ids) {
        for (Long userId : ids) {
            //删除用户与角色的关系
            boolean rmr = sysUserRoleService.removeUserRoles(userId);
            //删除用户与岗位的关系
            boolean rmp = sysUserPostService.removeUserPosts(userId);
            //删除用户
            removeById(userId);
        }
        return true;
    }

    @Override
    public CustomUserDetails queryLoginUser(UserLoginDTO userLoginDTO) {
        //String mobilePhone = customEncryptor.encrypt(userLoginDTO.getMobilePhone());
        SysUser sysUser = sysUserMapper.selectLoginSysUser(userLoginDTO.getUsername(), userLoginDTO.getMobilePhone());
        if (sysUser == null) {
            return null;
        }
        CustomUserDetails customUserDetails = BeanCopierUtil.copyProperties(sysUser, CustomUserDetails.class);
        customUserDetails.setId(sysUser.getId());
        customUserDetails.setUserType(sysUser.getUserType());
        customUserDetails.setOperatorId(sysUser.getOperatorId());
        //角色
        LambdaQueryWrapper<SysUserRole> surqw = Wrappers.lambdaQuery(SysUserRole.class);
        surqw.eq(SysUserRole::getUserId, sysUser.getId());
        List<SysUserRole> roles = sysUserRoleService.list(surqw);
        if (CollectionUtils.isNotEmpty(roles)) {
            List<SysRoleBO> sysRoleBOS = BeanCopierUtil.copyPropertiesOfList(roles, SysRoleBO.class);
            customUserDetails.setRoles(sysRoleBOS);
        }
        log.debug("用户【{}】登录 查询用户信息", userLoginDTO.getUsername() + " " + userLoginDTO.getMobilePhone());
        return customUserDetails;
    }

    @Override
    public LoginUserTenantsBO currentLoginUser(String userName) {
        if (StringUtils.isBlank(userName)) {
            return null;
        }
        //用户
        SysUser sysUser = sysUserMapper.selectLoginSysUser(userName, null);
        if (sysUser == null) {
            return null;
        }
        LoginUserTenantsBO tenantsBO = new LoginUserTenantsBO();
        tenantsBO.setUserId(sysUser.getId());
        tenantsBO.setUserType(sysUser.getUserType());
        tenantsBO.setUsername(sysUser.getUserName());
        String operatorId = sysUser.getOperatorId();
        if (StringUtils.isNotBlank(operatorId)) {
            tenantsBO.setOperatorId(operatorId);
            Set<String> operatorIds = new HashSet<>();
            operatorIds.add(operatorId);
            tenantsBO.setOperatorIds(operatorIds);
        }
        //用户所有的角色
        tenantsBO.setRoleIds(sysRoleService.selectUserRoleIds(sysUser.getId(), sysUser.getDeptId()));
        //权限
        List<SysMenuBO> sysMenuBOList = sysMenuService.findByRoleCodes(new ArrayList<>(tenantsBO.getRoleIds()));
        tenantsBO.setPerms(sysMenuBOList);
        redissonOpService.set(RedisKeyNameEnum.key(RedisKeyNameEnum.CURRENT_USER, userName), JacksonUtil.toJSONString(tenantsBO));
        return tenantsBO;
    }

    @Override
    public Map<String, Object> getCurrentUserInfo() {
        String userName = ZerosSecurityContextHolder.getUserName();
        SysUserVO sysUserVO = getSysUserVO(userName);
        Map<String, Object> resMap = new HashMap<>();
        //用户信息
        resMap.put("user", sysUserVO);
        //角色信息
        List<SysRoleVO> sysRoleVOS = sysRoleService.selectUserRoles(sysUserVO.getId(), sysUserVO.getDeptId());
        Set<Long> roles = sysRoleVOS.stream().map(SysRoleVO::getId).collect(Collectors.toSet());
        //权限字符串
        Set<String> permsSet = new HashSet<>();
        if (UserTypeEnum.SUPER_ADMIN.getCode().equals(sysUserVO.getUserType())) {
            permsSet.add("*:*:*");
            resMap.put("permissions", permsSet);
            resMap.put("roles", Collections.singletonList("super_admin"));
        } else {
            resMap.put("permissions", sysMenuService.queryPermList(roles));
            resMap.put("roles", Collections.singletonList("common"));
        }
        return resMap;
    }

    @Override
    public Map<String, Object> getUserProfile() {
        String userName = ZerosSecurityContextHolder.getUserName();
        SysUserVO sysUserVO = getSysUserVO(userName);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("user", sysUserVO);
        List<SysRoleVO> sysRoleVOS = sysRoleService.selectUserRoles(sysUserVO.getId(), sysUserVO.getDeptId());
        if (CollectionUtils.isNotEmpty(sysRoleVOS)) {
            resMap.put("roleGroup", sysRoleVOS.stream().map(SysRoleVO::getRoleName).collect(Collectors.toSet()));
        }
        List<SysPost> userPostList = sysPostService.queryUserPosts(sysUserVO.getId());
        if (CollectionUtils.isNotEmpty(userPostList)) {
            resMap.put("postGroup", userPostList.stream().map(SysPost::getPostName).collect(Collectors.toSet()));
        }
        return resMap;
    }

    @Override
    public boolean updateProfile(SysUserDTO sysUserDTO) {
        if (StringUtils.isNotBlank(sysUserDTO.getNewPassword())) {
            SysUserVO sysUserVO = getSysUserVO(ZerosSecurityContextHolder.getUserName());
            if (!passwordEncoder.matches(sysUserDTO.getOldPassword(), sysUserVO.getPassword())) {
                throw new BusinessException("原密码输入错误");
            }
            LambdaUpdateWrapper<SysUser> suuw = new LambdaUpdateWrapper<>();
            suuw.set(SysUser::getPassword, passwordEncoder.encode(sysUserDTO.getNewPassword()));
            suuw.eq(SysUser::getUserName, ZerosSecurityContextHolder.getUserName());
            return update(suuw);
        }
        LambdaUpdateWrapper<SysUser> suuw = new LambdaUpdateWrapper<>();
        suuw.set(SysUser::getNickName, sysUserDTO.getNickName());
        suuw.set(SysUser::getEmail, sysUserDTO.getEmail());
        suuw.set(SysUser::getPhoneNumber, sysUserDTO.getPhoneNumber());
        suuw.set(SysUser::getSex, sysUserDTO.getSex());
        if (StringUtils.isBlank(sysUserDTO.getUserName()) || !ZerosSecurityContextHolder.getUserName().equals(sysUserDTO.getUserName())) {
            throw new BusinessException("非法操作或用户名参数为空");
        }
        suuw.eq(SysUser::getUserName, sysUserDTO.getUserName());
        return update(suuw);
    }

    public SysUserVO getSysUserVO(String userName) {
        LambdaQueryWrapper<SysUser> suqw = Wrappers.lambdaQuery(SysUser.class);
        suqw.eq(SysUser::getUserName, userName);
        SysUser sysUser = getOne(suqw);
        if (sysUser == null) {
            throw new BusinessException("用户不存在");
        }
        return BeanCopierUtil.copyProperties(sysUser, SysUserVO.class);
    }
}
