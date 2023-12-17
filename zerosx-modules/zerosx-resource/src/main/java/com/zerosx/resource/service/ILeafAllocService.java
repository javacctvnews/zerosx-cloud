package com.zerosx.resource.service;

import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.resource.dto.LeafAllocDTO;
import com.zerosx.resource.dto.LeafAllocPageDTO;
import com.zerosx.resource.entity.LeafAlloc;
import com.zerosx.resource.vo.LeafAllocPageVO;
import com.zerosx.resource.vo.LeafAllocVO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 美团分布式ID
 * @Description
 * @author javacctvnews
 * @date 2023-12-05 14:00:46
 */
public interface ILeafAllocService extends ISuperService<LeafAlloc> {

    /**
     *
     * @param requestVO
     * @param searchCount
     * @return
     */
    CustomPageVO<LeafAllocPageVO> pageList(RequestVO<LeafAllocPageDTO> requestVO, boolean searchCount);

    /**
     * 分页查询的data
     * @param query
     * @return
     */
    List<LeafAlloc> dataList(LeafAllocPageDTO query);

    /**
     * 新增
     * @param leafAllocDTO
     * @return
     */
    boolean add(LeafAllocDTO leafAllocDTO);

    /**
     * 编辑
     * @param leafAllocDTO
     * @return
     */
    boolean update(LeafAllocDTO leafAllocDTO);

    /**
     * 按id查询
     * @param id
     * @return
     */
    LeafAllocVO queryById(String id);

    /**
     * 删除
     * @param ids
     * @return
     */
    boolean deleteRecord(String[] ids);

    /**
     * 导出Excel
     *
     * @param requestVO requestVO
     * @param response  response
     */
    void excelExport(RequestVO<LeafAllocPageDTO> requestVO, HttpServletResponse response);

    LeafAlloc updateMaxIdAndGetLeafAlloc(String key);

    LeafAlloc updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc temp);
}

