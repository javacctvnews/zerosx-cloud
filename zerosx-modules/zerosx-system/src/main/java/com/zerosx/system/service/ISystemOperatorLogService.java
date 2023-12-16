package com.zerosx.system.service;

import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.vo.SystemOperatorLogBO;
import com.zerosx.system.dto.SystemOperatorLogDTO;
import com.zerosx.system.dto.SystemOperatorLogPageDTO;
import com.zerosx.system.entity.SystemOperatorLog;
import com.zerosx.system.vo.SystemOperatorLogPageVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 操作日志
 *
 * @author javacctvnews
 * @Description
 * @date 2023-04-02 15:06:36
 */
public interface ISystemOperatorLogService extends ISuperService<SystemOperatorLog> {

    /**
     * 分页查询
     *
     * @param requestVO
     * @return
     */
    CustomPageVO<SystemOperatorLogPageVO> pageList(RequestVO<SystemOperatorLogPageDTO> requestVO, boolean searchCount);

    /**
     * 新增
     *
     * @param systemOperatorLogBO
     * @return
     */
    boolean add(SystemOperatorLogBO systemOperatorLogBO);

    /**
     * 编辑
     *
     * @param systemOperatorLogDTO
     * @return
     */
    boolean update(SystemOperatorLogDTO systemOperatorLogDTO);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    boolean deleteRecord(Long[] id);

    /**
     * 删除指定天数的操作日志
     *
     * @param deleteDays
     * @return
     */
    boolean deleteSystemOperatorLog(int deleteDays);

    /**
     * 清空全部日志
     *
     * @return
     */
    boolean cleanAll();

    /**
     * 分页查询的data集合
     *
     * @param systemOperatorLogPageDTO dto
     * @return
     */
    List<SystemOperatorLog> queryPageVOList(SystemOperatorLogPageDTO systemOperatorLogPageDTO);

    /**
     * 按id查询
     *
     * @param id id
     * @return
     */
    SystemOperatorLogPageVO queryById(Long id);

    /**
     * 读取redis的操作日志保存到mysql
     *
     */
    void saveSystemOperatorLog();

    /**
     * 导出Excel
     *
     * @param requestVO requestVO
     * @param response  response
     */
    void excelExport(RequestVO<SystemOperatorLogPageDTO> requestVO, HttpServletResponse response);

}

