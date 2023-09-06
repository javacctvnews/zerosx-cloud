package com.zerosx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerosx.api.system.dto.SysDictDataRetrieveDTO;
import com.zerosx.api.system.vo.I18nSelectOptionVO;
import com.zerosx.system.entity.SysDictData;
import com.zerosx.system.vo.SysDictDataVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 字典数据表 Mapper 接口
 * </p>
 *
 * @author junmy
 * @since 2020-11-18
 */
@Mapper
public interface ISysDictDataMapper extends BaseMapper<SysDictData> {

    List<SysDictDataVO> selectDictDataList(SysDictDataRetrieveDTO dictData);

    List<I18nSelectOptionVO> getSysDictDataSelectList(String dictType);

}
