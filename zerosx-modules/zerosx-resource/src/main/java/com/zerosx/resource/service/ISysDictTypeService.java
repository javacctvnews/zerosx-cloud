package com.zerosx.resource.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.resource.dto.SysDictTypeDTO;
import com.zerosx.resource.dto.SysDictTypeRetrieveDTO;
import com.zerosx.resource.dto.SysDictTypeUpdateDTO;
import com.zerosx.resource.entity.SysDictType;
import com.zerosx.resource.vo.SysDictTypeVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 字典类型表 服务类
 */
public interface ISysDictTypeService extends IService<SysDictType> {

    /**
     * 分页查询
     *
     * @param requestVO
     * @return
     */
    CustomPageVO<SysDictTypeVO> pageList(RequestVO<SysDictTypeRetrieveDTO> requestVO, boolean searchCount);

    /**
     * 字典类型保存
     *
     * @param sysDictType
     * @return
     */
    boolean saveDictType(SysDictTypeDTO sysDictType);

    /**
     * 修改
     *
     * @param sysDictType
     * @return
     */
    boolean updateSysDictType(SysDictTypeUpdateDTO sysDictType);


    /**
     * 删除
     *
     * @param id
     * @return
     */
    boolean deleteDictType(Long[] id);

    /**
     * map查询
     *
     * @param sysDictType
     * @return
     */
    List<SysDictTypeVO> selectByMap(SysDictTypeRetrieveDTO sysDictType);

    SysDictTypeVO getDictTypeById(Long dictId);

    List<SysDictType> dataList(SysDictTypeRetrieveDTO query);

    /**
     * 导出Excel
     *
     * @param requestVO requestVO
     * @param response  response
     */
    void excelExport(RequestVO<SysDictTypeRetrieveDTO> requestVO, HttpServletResponse response);

}
