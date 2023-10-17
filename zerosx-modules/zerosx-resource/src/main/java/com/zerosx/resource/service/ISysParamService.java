package com.zerosx.resource.service;

import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.resource.dto.SysParamDTO;
import com.zerosx.resource.dto.SysParamPageDTO;
import com.zerosx.resource.entity.SysParam;
import com.zerosx.resource.vo.SysParamPageVO;
import com.zerosx.resource.vo.SysParamVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 系统参数
 * @Description
 * @author javacctvnews
 * @date 2023-07-29 01:02:29
 */
public interface ISysParamService extends ISuperService<SysParam> {

    /**
     * 分页查询
     * @param requestVO
     * @return
     */
    CustomPageVO<SysParamPageVO> pageList(RequestVO<SysParamPageDTO> requestVO, boolean searchCount);

    /**
     * 分页查询的data
     * @param query
     * @return
     */
    List<SysParam> dataList(SysParamPageDTO query);

    /**
     * 新增
     * @param sysParamDTO
     * @return
     */
    boolean add(SysParamDTO sysParamDTO);

    /**
     * 编辑
     * @param sysParamDTO
     * @return
     */
    boolean update(SysParamDTO sysParamDTO);

    /**
     * 按id查询
     * @param id
     * @return
     */
    SysParamVO queryById(Long id);

    /**
     * 删除
     * @param ids
     * @return
     */
    boolean deleteRecord(Long[] ids);

    SysParamVO queryByKey(SysParamPageDTO sysParamPageDTO);

    /**
     * 导出Excel
     *
     * @param requestVO requestVO
     * @param response  response
     */
    void excelExport(RequestVO<SysParamPageDTO> requestVO, HttpServletResponse response);
}

