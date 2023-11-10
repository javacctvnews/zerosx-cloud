package com.zerosx.resource.service;

import com.zerosx.common.base.vo.RegionSelectVO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.resource.dto.AreaCitySourceDTO;
import com.zerosx.resource.dto.AreaCitySourcePageDTO;
import com.zerosx.resource.entity.AreaCitySource;
import com.zerosx.resource.vo.AreaCitySourcePageVO;
import com.zerosx.resource.vo.AreaCitySourceTreeVO;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 行政区域数据源
 *
 * @author javacctvnews
 * @Description
 * @date 2023-04-12 13:48:43
 */
public interface IAreaCitySourceService extends ISuperService<AreaCitySource> {

    /**
     * 分页查询
     *
     * @param requestVO
     * @return
     */
    CustomPageVO<AreaCitySourcePageVO> pageList(RequestVO<AreaCitySourcePageDTO> requestVO, boolean searchCount);

    /**
     * 新增
     *
     * @param areaCitySourceDTO
     * @return
     */
    boolean add(AreaCitySourceDTO areaCitySourceDTO);

    /**
     * 编辑
     *
     * @param areaCitySourceDTO
     * @return
     */
    boolean update(AreaCitySourceDTO areaCitySourceDTO);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    boolean deleteRecord(Long id);

    /**
     * 树形结构
     *
     * @param parentCode
     * @return
     */
    List<AreaCitySourceTreeVO> lazyTreeData(String parentCode);

    List<RegionSelectVO> getAllArea();

    String getAreaName(String province);

    /**
     * 导出Excel
     *
     * @param requestVO requestVO
     * @param response  response
     */
    void excelExport(RequestVO<AreaCitySourcePageDTO> requestVO, HttpServletResponse response);

}

