package com.zerosx.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerosx.api.system.dto.SysDictDataRetrieveDTO;
import com.zerosx.api.system.vo.I18nSelectOptionVO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.system.dto.SysDictDataDTO;
import com.zerosx.system.dto.SysDictDataUpdateDTO;
import com.zerosx.system.entity.SysDictData;
import com.zerosx.system.vo.SysDictDataVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author junmy
 * @since 2020-11-18
 */
public interface ISysDictDataService extends IService<SysDictData> {

    /**
     * 新增
     *
     * @param sysDictData
     * @return
     */
    boolean insert(SysDictDataDTO sysDictData);

    /**
     * 下拉列表查询
     *
     * @param dictType
     * @return
     */
    List<I18nSelectOptionVO> getSysDictDataSelectList(String dictType);

    /**
     * 下拉列表查询
     *
     * @param dictType
     * @return
     */
    Map<String, Map<Object, Object>> getSysDictDataGetMap(List<String> dictType);

    /**
     * 分页查询
     *
     * @param requestVO
     * @return
     */
    CustomPageVO<SysDictDataVO> pageList(RequestVO<SysDictDataRetrieveDTO> requestVO, boolean searchCount);

    /**
     * map查询
     *
     * @param sysDictData
     * @return
     */
    List<SysDictDataVO> selectDictDataList(SysDictDataRetrieveDTO sysDictData);

    /**
     * 修改
     *
     * @param sysDictData
     * @return
     */
    boolean updateSysDictData(SysDictDataUpdateDTO sysDictData);

    /**
     * 删除
     *
     * @param code
     * @return
     */
    boolean deleteSysDictData(Long[] code);

    /**
     * 初始化数据字典缓存
     *
     * @param dictType
     */
    void initCacheDictData(String dictType);

    /**
     * 查询指定字典类型的数据集
     *
     * @param dictType
     * @return
     */
    Map<String, String> getDictDataByDictType(String dictType);

    SysDictDataVO getDictById(Long id);

    List<SysDictData> dataList(SysDictDataRetrieveDTO query);

    /**
     * 保存带有注解@AutoDictData的枚举到数据库
     *
     * @return
     */
    void autoSaveDictData();

}
