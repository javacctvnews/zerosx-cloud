package com.zerosx.resource.service;

import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.resource.dto.SmsSupplierBusinessDTO;
import com.zerosx.resource.dto.SmsSupplierBusinessPageDTO;
import com.zerosx.resource.entity.SmsSupplierBusiness;
import com.zerosx.resource.vo.SmsSupplierBusinessPageVO;
import com.zerosx.resource.vo.SmsSupplierBusinessVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 短信业务模板
 * @Description
 * @author javacctvnews
 * @date 2023-08-31 10:39:49
 */
public interface ISmsSupplierBusinessService extends ISuperService<SmsSupplierBusiness> {

    /**
     *
     * @param requestVO
     * @param searchCount
     * @return
     */
    CustomPageVO<SmsSupplierBusinessPageVO> pageList(RequestVO<SmsSupplierBusinessPageDTO> requestVO, boolean searchCount);

    /**
     * 分页查询的data
     * @param query
     * @return
     */
    List<SmsSupplierBusiness> dataList(SmsSupplierBusinessPageDTO query);

    /**
     * 新增
     * @param smsSupplierBusinessDTO
     * @return
     */
    boolean add(SmsSupplierBusinessDTO smsSupplierBusinessDTO);

    /**
     * 编辑
     * @param smsSupplierBusinessDTO
     * @return
     */
    boolean update(SmsSupplierBusinessDTO smsSupplierBusinessDTO);

    /**
     * 按id查询
     * @param id
     * @return
     */
    SmsSupplierBusinessVO queryById(Long id);

    /**
     * 删除
     * @param ids
     * @return
     */
    boolean deleteRecord(Long[] ids);

    /**
     * 导出Excel
     *
     * @param requestVO requestVO
     * @param response  response
     */
    void excelExport(RequestVO<SmsSupplierBusinessPageDTO> requestVO, HttpServletResponse response);
}

