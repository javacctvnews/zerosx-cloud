package com.zerosx.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.system.dto.SysDictTypeDTO;
import com.zerosx.system.dto.SysDictTypeRetrieveDTO;
import com.zerosx.system.dto.SysDictTypeUpdateDTO;
import com.zerosx.system.entity.SysDictType;
import com.zerosx.system.vo.SysDictTypeVO;

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
}
