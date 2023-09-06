package com.zerosx.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.BeanCopierUtil;
import com.zerosx.common.core.utils.EasyTransUtils;
import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.system.dto.SysDeptDTO;
import com.zerosx.system.dto.SysDeptPageDTO;
import com.zerosx.system.entity.SysDept;
import com.zerosx.system.entity.SysRoleDept;
import com.zerosx.system.mapper.ISysDeptMapper;
import com.zerosx.system.service.ISysDeptService;
import com.zerosx.system.service.ISysRoleDeptService;
import com.zerosx.system.vo.SysDeptPageVO;
import com.zerosx.system.vo.SysDeptVO;
import com.zerosx.system.vo.SysTreeSelectVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 部门表
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-29 17:42:27
 */
@Slf4j
@Service
public class SysDeptServiceImpl extends SuperServiceImpl<ISysDeptMapper, SysDept> implements ISysDeptService {

    @Autowired
    private ISysRoleDeptService sysRoleDeptService;

    @Override
    public CustomPageVO<SysDeptPageVO> pageList(RequestVO<SysDeptPageDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), lambdaQW(requestVO.getT())), SysDeptPageVO.class);
    }

    @Override
    public List<SysDept> dataList(SysDeptPageDTO query) {
        LambdaQueryWrapper<SysDept> listqw = lambdaQW(query);
        return list(listqw);
    }

    private static LambdaQueryWrapper<SysDept> lambdaQW(SysDeptPageDTO query) {
        LambdaQueryWrapper<SysDept> listqw = Wrappers.lambdaQuery(SysDept.class);
        if (query == null) {
            return listqw;
        }
        listqw.like(StringUtils.isNotBlank(query.getDeptName()), SysDept::getDeptName, query.getDeptName());
        listqw.eq(StringUtils.isNotBlank(query.getStatus()), SysDept::getStatus, query.getStatus());
        listqw.eq(StringUtils.isNotBlank(query.getOperatorId()), SysDept::getOperatorId, query.getOperatorId());
        //listqw.orderByAsc(SysDept::getOrderNum);
        //listqw.orderByDesc(SysDept::getCreateTime);
        return listqw;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(SysDeptDTO sysDeptDTO) {
        SysDept addEntity = BeanCopierUtil.copyProperties(sysDeptDTO, SysDept.class);
        addEntity.setDeptCode(IdGenerator.getIdStr());
        boolean save = save(addEntity);
        sysRoleDeptService.updateSysDeptRoles(addEntity.getId(), sysDeptDTO.getRoleIds(), false);
        return save;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(SysDeptDTO sysDeptDTO) {
        SysDept dbUpdate = getById(sysDeptDTO.getId());
        if (dbUpdate == null) {
            throw new BusinessException("编辑记录不存在");
        }
        SysDept updateEntity = BeanCopierUtil.copyProperties(sysDeptDTO, SysDept.class);
        sysRoleDeptService.updateSysDeptRoles(dbUpdate.getId(), sysDeptDTO.getRoleIds(), true);
        return updateById(updateEntity);
    }

    @Override
    public SysDeptVO queryById(Long id) {
        SysDeptVO sysDeptVO = BeanCopierUtil.copyProperties(getById(id), SysDeptVO.class);
        LambdaQueryWrapper<SysRoleDept> rm = Wrappers.lambdaQuery(SysRoleDept.class);
        rm.eq(SysRoleDept::getDeptId, id);
        List<SysRoleDept> sysRoleDepts = sysRoleDeptService.list(rm);
        if (CollectionUtils.isNotEmpty(sysRoleDepts)) {
            sysDeptVO.setRoleIds(sysRoleDepts.stream().map(SysRoleDept::getRoleId).collect(Collectors.toList()));
        }
        return sysDeptVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(Long[] ids) {
        sysRoleDeptService.removeByDeptIds(ids);
        return removeByIds(Arrays.asList(ids));
    }

    @Override
    public List<SysDept> tableTree(SysDeptPageDTO sysDeptPageDTO) {
        List<SysDept> sysDepts = dataList(sysDeptPageDTO);
        EasyTransUtils.easyTrans(sysDepts);
        return buildMenuTree(sysDepts);
    }

    /**
     * 构建前端所需要的树结构
     *
     * @param sysDepts
     * @return
     */
    public List<SysDept> buildMenuTree(List<SysDept> sysDepts) {
        List<SysDept> returnList = new ArrayList<>();
        List<Long> tempList = new ArrayList<>();
        sysDepts.forEach(menu -> {
            tempList.add(menu.getId());
        });
        for (SysDept sysDept : sysDepts) {
            if (!tempList.contains(sysDept.getParentId())) {
                recursionFn(sysDepts, sysDept);
                returnList.add(sysDept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = sysDepts;
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        return filter(list, n -> n.getParentId().equals(t.getId()));
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return getChildList(list, t).size() > 0;
    }

    public static <E> List<E> filter(Collection<E> collection, Predicate<E> function) {
        if (CollUtil.isEmpty(collection)) {
            return CollUtil.newArrayList();
        }
        // 注意此处不要使用 .toList() 新语法 因为返回的是不可变List 会导致序列化问题
        return collection.stream().filter(function).collect(Collectors.toList());
    }

    @Override
    public List<SysTreeSelectVO> treeSelect() {
        List<SysDept> sysDepts = buildMenuTree(dataList(new SysDeptPageDTO()));
        return sysDepts.stream().map(SysTreeSelectVO::new).collect(Collectors.toList());
    }

    @Override
    public Set<Long> getDeptRoleIds(Long deptId) {
        LambdaQueryWrapper<SysRoleDept> srdqw = Wrappers.lambdaQuery(SysRoleDept.class);
        srdqw.eq(SysRoleDept::getDeptId, deptId);
        List<SysRoleDept> roleDepts = sysRoleDeptService.list(srdqw);
        if (CollectionUtils.isEmpty(roleDepts)) {
            return new HashSet<>();
        }
        return roleDepts.stream().map(SysRoleDept::getRoleId).collect(Collectors.toSet());
    }
}
