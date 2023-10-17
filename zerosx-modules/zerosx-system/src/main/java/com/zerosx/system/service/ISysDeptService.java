package com.zerosx.system.service;

import com.zerosx.common.base.vo.BaseTenantDTO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.system.dto.SysDeptDTO;
import com.zerosx.system.dto.SysDeptPageDTO;
import com.zerosx.system.entity.SysDept;
import com.zerosx.system.vo.SysDeptPageVO;
import com.zerosx.system.vo.SysDeptVO;
import com.zerosx.system.vo.SysTreeSelectVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * 部门表
 * @Description
 * @author javacctvnews
 * @date 2023-07-29 17:42:27
 */
public interface ISysDeptService extends ISuperService<SysDept> {

    /**
     * 分页查询
     * @param requestVO
     * @return
     */
    CustomPageVO<SysDeptPageVO> pageList(RequestVO<SysDeptPageDTO> requestVO, boolean searchCount);

    /**
     * 分页查询的data
     * @param query
     * @return
     */
    List<SysDept> dataList(SysDeptPageDTO query);

    /**
     * 新增
     * @param sysDeptDTO
     * @return
     */
    boolean add(SysDeptDTO sysDeptDTO);

    /**
     * 编辑
     * @param sysDeptDTO
     * @return
     */
    boolean update(SysDeptDTO sysDeptDTO);

    /**
     * 按id查询
     * @param id
     * @return
     */
    SysDeptVO queryById(Long id);

    /**
     * 删除
     * @param ids
     * @return
     */
    boolean deleteRecord(Long[] ids);

    List<SysDept> tableTree(SysDeptPageDTO sysDeptPageDTO);

    List<SysTreeSelectVO> treeSelect(BaseTenantDTO baseTenantDTO);

    Set<Long> getDeptRoleIds(Long deptId);

    /**
     * 导出Excel
     *
     * @param requestVO requestVO
     * @param response  response
     */
    void excelExport(RequestVO<SysDeptPageDTO> requestVO, HttpServletResponse response);
}

