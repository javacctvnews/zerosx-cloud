package com.zerosx.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.constants.SysMenuConstants;
import com.zerosx.common.base.dto.RolePermissionDTO;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.SysMenuBO;
import com.zerosx.common.base.vo.SysPermissionBO;
import com.zerosx.common.core.enums.RedisKeyNameEnum;
import com.zerosx.common.core.enums.system.UserTypeEnum;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.EasyTransUtils;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.utils.BeanCopierUtils;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.ds.constant.DSType;
import com.zerosx.system.dto.SysMenuDTO;
import com.zerosx.system.dto.SysMenuPageDTO;
import com.zerosx.system.dto.SysRoleMenuQueryDTO;
import com.zerosx.system.entity.SysMenu;
import com.zerosx.system.entity.SysRoleMenu;
import com.zerosx.system.entity.SysUser;
import com.zerosx.system.mapper.ISysMenuMapper;
import com.zerosx.system.mapper.ISysRoleMenuMapper;
import com.zerosx.system.service.ISysMenuService;
import com.zerosx.system.service.ISysRoleMenuService;
import com.zerosx.system.service.ISysRoleService;
import com.zerosx.system.service.ISysUserService;
import com.zerosx.system.task.SystemAsyncTask;
import com.zerosx.system.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 菜单权限表
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-20 14:49:30
 */
@Slf4j
@Service
@DS(DSType.MASTER)
public class SysMenuServiceImpl extends SuperServiceImpl<ISysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private ISysRoleMenuService sysRoleMenuService;
    @Autowired
    private SystemAsyncTask systemAsyncTask;
    @Autowired
    private ISysRoleService sysRoleService;


    @Override
    @DS(DSType.SLAVE)
    public CustomPageVO<SysMenuPageVO> pageList(RequestVO<SysMenuPageDTO> requestVO, boolean searchCount) {
        LambdaQueryWrapper<SysMenu> listqw = Wrappers.lambdaQuery(SysMenu.class);
        listqw.orderByDesc(SysMenu::getCreateTime);
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, true), listqw).convert((e) -> {
            SysMenuPageVO sysMenuPageVO = EasyTransUtils.copyTrans(e, SysMenuPageVO.class);
            //todo
            return sysMenuPageVO;
        }));
    }

    @Override
    public boolean add(SysMenuDTO sysMenuDTO) {
        SysMenu addEntity = BeanCopierUtils.copyProperties(sysMenuDTO, SysMenu.class);
        return save(addEntity);
    }

    @Override
    public boolean update(SysMenuDTO sysMenuDTO) {
        SysMenu dbUpdate = getById(sysMenuDTO.getMenuId());
        if (dbUpdate == null) {
            throw new BusinessException("编辑记录不存在");
        }
        SysMenu updateEntity = BeanCopierUtils.copyProperties(sysMenuDTO, SysMenu.class);

        Long menuId = sysMenuDTO.getMenuId();
        //删除所有关联次菜单的角色权限缓存
        LambdaQueryWrapper<SysRoleMenu> srmqw = Wrappers.lambdaQuery(SysRoleMenu.class);
        srmqw.eq(SysRoleMenu::getMenuId, menuId);
        List<SysRoleMenu> roleMenuList = sysRoleMenuService.list(srmqw);
        if (!CollectionUtils.isEmpty(roleMenuList)) {
            for (SysRoleMenu sysRoleMenu : roleMenuList) {
                redissonOpService.del(RedisKeyNameEnum.key(RedisKeyNameEnum.ROLE_PERMISSIONS, sysRoleMenu.getRoleId()));
            }
        }
        boolean updateById = updateById(updateEntity);
        //延时3秒再次删除
        if (!CollectionUtils.isEmpty(roleMenuList)) {
            String[] keyArray = roleMenuList.stream().map(role -> RedisKeyNameEnum.key(RedisKeyNameEnum.ROLE_PERMISSIONS, role.getRoleId())).distinct().toArray(String[]::new);
            systemAsyncTask.asyncRedisDelOptions(keyArray);
        }
        return updateById;
    }

    @Override
    public boolean deleteRecord(Long menuId) {
        boolean exists = baseMapper.exists(Wrappers.lambdaQuery(SysMenu.class).eq(SysMenu::getParentId, menuId));
        if (exists) {
            throw new BusinessException("存在子菜单,不允许删除");
        }
        boolean menuRoleExists = sysRoleMenuMapper.exists(Wrappers.lambdaQuery(SysRoleMenu.class).eq(SysRoleMenu::getMenuId, menuId));
        if (menuRoleExists) {
            throw new BusinessException("菜单已分配,不允许删除");
        }
        return removeById(menuId);
    }

    @Override
    @DS(DSType.SLAVE)
    public List<SysMenu> selectMenuTreeByUserId(Long userId) {
        SysUser sysUser = sysUserService.getById(userId);
        List<SysMenu> menus;
        if (UserTypeEnum.SUPER_ADMIN.getCode().equals(sysUser.getUserType())) {
            LambdaQueryWrapper<SysMenu> lqw = Wrappers.lambdaQuery(SysMenu.class)
                    .in(SysMenu::getMenuType, SysMenuConstants.TYPE_DIR, SysMenuConstants.TYPE_MENU)
                    .eq(SysMenu::getStatus, SysMenuConstants.MENU_NORMAL)
                    .orderByAsc(SysMenu::getParentId)
                    .orderByAsc(SysMenu::getOrderNum);
            menus = list(lqw);
        } else {
            //menus = baseMapper.selectMenuTreeByUserId(userId);
            Set<Long> roleIds = sysRoleService.selectUserRoleIds(userId, sysUser.getDeptId());
            if (roleIds.isEmpty()) {
                return new ArrayList<>();
            }
            menus = baseMapper.selectMenuTreeByRoleIds(roleIds);
        }
        return getChildPerms(menus, 0L);
    }

    @Override
    @DS(DSType.SLAVE)
    public List<RouterVO> buildMenus(List<SysMenu> menus) {
        List<RouterVO> routers = new LinkedList<RouterVO>();
        for (SysMenu menu : menus) {
            RouterVO router = new RouterVO();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQueryParam());
            router.setMeta(new MetaVO(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && SysMenuConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVO> childrenList = new ArrayList<RouterVO>();
                RouterVO children = new RouterVO();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVO(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
                children.setQuery(menu.getQueryParam());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVO(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVO> childrenList = new ArrayList<RouterVO>();
                RouterVO children = new RouterVO();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(SysMenuConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVO(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, Long parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (SysMenu t : list) {
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId().equals(parentId)) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        return filter(list, n -> n.getParentId().equals(t.getMenuId()));
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 将collection过滤
     *
     * @param collection 需要转化的集合
     * @param function   过滤方法
     * @return 过滤后的list
     */
    public static <E> List<E> filter(Collection<E> collection, Predicate<E> function) {
        if (CollUtil.isEmpty(collection)) {
            return CollUtil.newArrayList();
        }
        // 注意此处不要使用 .toList() 新语法 因为返回的是不可变List 会导致序列化问题
        return collection.stream().filter(function).collect(Collectors.toList());
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId().intValue() == 0 && SysMenuConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(SysMenuConstants.NO_FRAME);
    }


    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu) {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && SysMenuConstants.TYPE_DIR.equals(menu.getMenuType())
                && SysMenuConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu) {
        String component = SysMenuConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = SysMenuConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = SysMenuConstants.PARENT_VIEW;
        }
        return component;
    }


    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenu menu) {
        return menu.getIsFrame().equals(SysMenuConstants.NO_FRAME) && Validator.isUrl(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu) {
        return menu.getParentId().intValue() != 0 && SysMenuConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 内链域名特殊字符替换
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{CommonConstants.HTTP, CommonConstants.HTTPS, CommonConstants.WWW, "."},
                new String[]{"", "", "", "/"});
    }


    @Override
    @DS(DSType.SLAVE)
    public List<SysMenu> selectMenuList(SysMenuPageDTO menu) {
        List<SysMenu> menuList;
        String userType = ZerosSecurityContextHolder.getUserType();
        // 管理员显示所有菜单信息
        if (UserTypeEnum.SUPER_ADMIN.getCode().equals(userType)) {
            menuList = baseMapper.selectList(Wrappers.lambdaQuery(SysMenu.class)
                    .like(StringUtils.isNotBlank(menu.getMenuName()), SysMenu::getMenuName, menu.getMenuName())
                    .eq(StringUtils.isNotBlank(menu.getVisible()), SysMenu::getVisible, menu.getVisible())
                    .eq(StringUtils.isNotBlank(menu.getStatus()), SysMenu::getStatus, menu.getStatus())
                    .orderByAsc(SysMenu::getParentId)
                    .orderByAsc(SysMenu::getOrderNum));
        } else {
            Long userId = ZerosSecurityContextHolder.getUserId();
            /*QueryWrapper<SysMenu> wrapper = Wrappers.query();
            wrapper.eq("sur.user_id", userId)
                    .like(StringUtils.isNotBlank(menu.getMenuName()), "m.menu_name", menu.getMenuName())
                    .eq(StringUtils.isNotBlank(menu.getVisible()), "m.visible", menu.getVisible())
                    .eq(StringUtils.isNotBlank(menu.getStatus()), "m.status", menu.getStatus())
                    .orderByAsc("m.parent_id")
                    .orderByAsc("m.order_num");
            menuList = baseMapper.selectMenuListByUserId(wrapper);*/
            SysUserVO sysUserVO = sysUserService.queryById(userId);
            Set<Long> sysRoleVOS = sysRoleService.selectUserRoleIds(userId, sysUserVO.getDeptId());
            menuList = baseMapper.selectMenuListByUserId(sysRoleVOS);
        }
        return menuList;
    }

    @Override
    @DS(DSType.SLAVE)
    public SysMenu getMenuById(Long menuId) {
        return getById(menuId);
    }

    @Override
    @DS(DSType.SLAVE)
    public SysRoleMenuTreeVO roleMenuTree(SysRoleMenuQueryDTO sysRoleMenuQueryDTO) {
        SysMenuPageDTO sysMenuPageDTO = new SysMenuPageDTO();
        sysMenuPageDTO.setStatus("0");//不展示停用的菜单
        sysMenuPageDTO.setVisible("0");
        List<SysMenu> menuList = selectMenuList(sysMenuPageDTO);
        List<SysMenu> menuTree = buildMenuTree(menuList);
        List<SysTreeSelectVO> treeSelects = menuTree.stream().map(SysTreeSelectVO::new).collect(Collectors.toList());
        SysRoleMenuTreeVO sysRoleMenuTreeVO = new SysRoleMenuTreeVO();
        if (sysRoleMenuQueryDTO.getRoleId() != null) {
            List<Long> menuIds = baseMapper.queryMenuListByRoleId(sysRoleMenuQueryDTO.getRoleId());
            sysRoleMenuTreeVO.setCheckedKeys(menuIds);
        }
        sysRoleMenuTreeVO.setMenus(treeSelects);
        return sysRoleMenuTreeVO;
    }

    /**
     * 构建前端所需要的树结构
     *
     * @param menus
     * @return
     */
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<>();
        List<Long> tempList = new ArrayList<>();
        menus.forEach(menu -> {
            tempList.add(menu.getMenuId());
        });
        for (SysMenu sysMenu : menus) {
            if (!tempList.contains(sysMenu.getParentId())) {
                recursionFn(menus, sysMenu);
                returnList.add(sysMenu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    @Override
    @DS(DSType.SLAVE)
    public SysPermissionBO queryPermsByRoleIds(RolePermissionDTO rolePermissionDTO) {
        List<Long> roles = rolePermissionDTO.getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            return null;
        }
        List<SysMenuBO> endAll = new ArrayList<>();
        for (Long roleId : roles) {
            List<SysMenuBO> sysMenuBOS = baseMapper.findByRoleCodes(Collections.singletonList(roleId));
            if (!CollectionUtils.isEmpty(roles)) {
                endAll.addAll(sysMenuBOS);
                redissonOpService.set(RedisKeyNameEnum.key(RedisKeyNameEnum.ROLE_PERMISSIONS, roleId), JacksonUtil.toJSONString(sysMenuBOS));
            }
        }
        SysPermissionBO bo = new SysPermissionBO();
        bo.setPermissionUrls(endAll);
        return bo;
    }

    @Override
    @DS(DSType.SLAVE)
    public List<SysMenuBO> findByRoleCodes(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        return baseMapper.findByRoleCodes(roleIds);
    }

    @Override
    @DS(DSType.SLAVE)
    public Set<String> queryPermList(Set<Long> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return null;
        }
        List<String> permList = baseMapper.queryPermList(roles);
        if (CollectionUtils.isEmpty(permList)) {
            return null;
        }
        return permList.stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet());
    }


}
