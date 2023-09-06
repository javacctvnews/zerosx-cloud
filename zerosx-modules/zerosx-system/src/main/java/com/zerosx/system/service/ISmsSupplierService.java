package com.zerosx.system.service;

import com.zerosx.api.system.dto.SmsSendDTO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.system.dto.SmsSupplierDTO;
import com.zerosx.system.dto.SmsSupplierPageDTO;
import com.zerosx.system.entity.SmsSupplier;
import com.zerosx.system.vo.SmsSupplierPageVO;
import com.zerosx.system.vo.SmsSupplierVO;

import java.util.List;

/**
 * 短信配置
 *
 * @author javacctvnews
 * @Description
 * @date 2023-08-30 18:28:13
 */
public interface ISmsSupplierService extends ISuperService<SmsSupplier> {

    /**
     * @param requestVO
     * @param searchCount
     * @return
     */
    CustomPageVO<SmsSupplierPageVO> pageList(RequestVO<SmsSupplierPageDTO> requestVO, boolean searchCount);

    /**
     * 分页查询的data
     *
     * @param query
     * @return
     */
    List<SmsSupplier> dataList(SmsSupplierPageDTO query);

    /**
     * 新增
     *
     * @param smsSupplierDTO
     * @return
     */
    boolean add(SmsSupplierDTO smsSupplierDTO);

    /**
     * 编辑
     *
     * @param smsSupplierDTO
     * @return
     */
    boolean update(SmsSupplierDTO smsSupplierDTO);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    SmsSupplierVO queryById(Long id);

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    boolean deleteRecord(Long[] ids);

    /**
     * 发送短信
     *
     * @param smsSendDTO
     * @return
     */
    ResultVO<?> sendSms(SmsSendDTO smsSendDTO);
}

