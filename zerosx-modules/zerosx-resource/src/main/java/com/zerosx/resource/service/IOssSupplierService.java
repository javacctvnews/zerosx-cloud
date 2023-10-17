package com.zerosx.resource.service;

import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.oss.core.client.IOssClientService;
import com.zerosx.resource.dto.OssSupplierDTO;
import com.zerosx.resource.dto.OssSupplierPageDTO;
import com.zerosx.resource.entity.OssSupplier;
import com.zerosx.resource.vo.OssSupplierPageVO;
import com.zerosx.resource.vo.OssSupplierVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * OSS配置
 * @Description
 * @author javacctvnews
 * @date 2023-09-08 18:23:18
 */
public interface IOssSupplierService extends ISuperService<OssSupplier> {

    /**
     *
     * @param requestVO
     * @param searchCount
     * @return
     */
    CustomPageVO<OssSupplierPageVO> pageList(RequestVO<OssSupplierPageDTO> requestVO, boolean searchCount);

    /**
     * 分页查询的data
     * @param query
     * @return
     */
    List<OssSupplier> dataList(OssSupplierPageDTO query);

    /**
     * 新增
     * @param ossSupplierDTO
     * @return
     */
    boolean add(OssSupplierDTO ossSupplierDTO);

    /**
     * 编辑
     * @param ossSupplierDTO
     * @return
     */
    boolean update(OssSupplierDTO ossSupplierDTO);

    /**
     * 按id查询
     * @param id
     * @return
     */
    OssSupplierVO queryById(Long id);

    /**
     * 删除
     * @param ids
     * @return
     */
    boolean deleteRecord(Long[] ids);

    /**
     * 获取IOssClientService实例
     * @param ossSupplierId
     * @return
     */
    IOssClientService getClient(Long ossSupplierId);

    /**
     * 导出Excel
     *
     * @param requestVO requestVO
     * @param response  response
     */
    void excelExport(RequestVO<OssSupplierPageDTO> requestVO, HttpServletResponse response);
}

