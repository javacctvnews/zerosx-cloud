package com.zerosx.auth.service;

import com.zerosx.auth.dto.OauthTokenRecordPageDTO;
import com.zerosx.auth.entity.OauthTokenRecord;
import com.zerosx.auth.vo.OauthTokenRecordPageVO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.core.vo.CustomPageVO;

import java.util.List;
import java.util.Map;

/**
 * 登录日志
 * @Description
 * @author javacctvnews
 * @date 2023-04-09 15:33:32
 */
public interface IOauthTokenRecordService extends ISuperService<OauthTokenRecord> {

    /**
     * 分页查询
     * @param requestVO
     * @return
     */
    CustomPageVO<OauthTokenRecordPageVO> pageList(RequestVO<OauthTokenRecordPageDTO> requestVO, boolean searchCount);

    /**
     * 删除
     * @param id
     * @return
     */
    boolean deleteRecord(Long[] id);

    /**
     * 新增 授权登录记录
     * @return
     */
    boolean saveBeforeOauthRecord(String clientId, Map<String, String> parameters);

    /**
     * 更新授权结果
     * @param tokenValue
     * @param resultMsg
     * @return
     */
    boolean updateOauthResult(String tokenValue, String resultMsg);

    /**
     * 清空所有登录日志
     * @return
     */
    boolean deleteAll();

    /**
     * 分页的data
     * @param oauthTokenRecordPageDTO
     * @return
     */
    List<OauthTokenRecord> dataList(OauthTokenRecordPageDTO oauthTokenRecordPageDTO);
}

