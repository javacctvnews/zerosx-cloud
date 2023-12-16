package com.zerosx.resource.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerosx.api.system.dto.SysDictDataPageDTO;
import com.zerosx.common.base.vo.I18nSelectOptionVO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.resource.dto.SysDictDataDTO;
import com.zerosx.resource.dto.SysDictDataUpdateDTO;
import com.zerosx.resource.entity.SysDictData;
import com.zerosx.resource.vo.SysDictDataVO;

import javax.servlet.http.HttpServletResponse;
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
    List<I18nSelectOptionVO> getDictList(String dictType);

    /**
     * 分页查询
     *
     * @param requestVO
     * @return
     */
    CustomPageVO<SysDictDataVO> pageList(RequestVO<SysDictDataPageDTO> requestVO, boolean searchCount);

    /**
     * 修改
     *
     * @param sysDictData
     * @return
     */
    boolean update(SysDictDataUpdateDTO sysDictData);

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
    Map<Object, String> getDictDataMap(String dictType);

    SysDictDataVO getDictById(Long id);

    List<SysDictData> dataList(SysDictDataPageDTO query);

    /**
     * 扫描加载贴有@AutoDictData的枚举数据字典
     *
     * @param queryDictType
     * @return
     */
    List<SysDictData> loadSysDictData(String queryDictType);

    /**
     * 导出Excel
     *
     * @param requestVO requestVO
     * @param response  response
     */
    void excelExport(RequestVO<SysDictDataPageDTO> requestVO, HttpServletResponse response);
}
