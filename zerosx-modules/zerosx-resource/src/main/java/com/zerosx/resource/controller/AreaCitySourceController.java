package com.zerosx.resource.controller;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RegionSelectVO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.resource.dto.AreaCitySourceDTO;
import com.zerosx.resource.dto.AreaCitySourcePageDTO;
import com.zerosx.resource.entity.AreaCitySource;
import com.zerosx.resource.service.IAreaCitySourceService;
import com.zerosx.resource.vo.AreaCitySourcePageVO;
import com.zerosx.resource.vo.AreaCitySourceTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 行政区域数据源
 *
 * @author javacctvnews
 * @Description
 * @date 2023-04-12 13:48:43
 */
@Slf4j
@RestController
@Tag(name = "行政区域数据源")
public class AreaCitySourceController {

    @Autowired
    private IAreaCitySourceService areaCitySourceService;

    @Operation(summary = "分页列表")
    @OpLog(mod = "行政区域数据源", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/area_city_source/page_list")
    public ResultVO<CustomPageVO<AreaCitySourcePageVO>> pageList(@RequestBody RequestVO<AreaCitySourcePageDTO> requestVO) {
        return ResultVOUtil.success(areaCitySourceService.pageList(requestVO, true));
    }

    @Operation(summary = "新增")
    @OpLog(mod = "行政区域数据源", btn = "新增", opType = OpTypeEnum.INSERT)
    @PostMapping("/area_city_source/save")
    public ResultVO<?> add(@Validated @RequestBody AreaCitySourceDTO areaCitySourceDTO) {
        return ResultVOUtil.successBoolean(areaCitySourceService.add(areaCitySourceDTO));
    }

    @Operation(summary = "编辑")
    @OpLog(mod = "行政区域数据源", btn = "编辑", opType = OpTypeEnum.UPDATE)
    @PostMapping("/area_city_source/update")
    public ResultVO<?> update(@Validated @RequestBody AreaCitySourceDTO areaCitySourceDTO) {
        return ResultVOUtil.successBoolean(areaCitySourceService.update(areaCitySourceDTO));
    }

    @Operation(summary = "删除")
    @OpLog(mod = "行政区域数据源", btn = "删除", opType = OpTypeEnum.DELETE)
    @DeleteMapping("/area_city_source/delete/{id}")
    public ResultVO<?> deleteRecord(@PathVariable("id") Long id) {
        return ResultVOUtil.successBoolean(areaCitySourceService.deleteRecord(id));
    }

    @Operation(summary = "导出")
    @OpLog(mod = "行政区域数据源", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/area_city_source/export")
    public void operatorExport(@RequestBody RequestVO<AreaCitySourcePageDTO> requestVO, HttpServletResponse response) throws IOException {
        areaCitySourceService.excelExport(requestVO, response);
    }

    /**
     * 数据来源：https://gitee.com/xiangyuecn/AreaCity-JsSpider-StatsGov
     * 导入文件：ok_data_level4.csv
     * 方便后续的更新
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @Operation(summary = "导入")
    @OpLog(mod = "行政区域源数据", btn = "导入", opType = OpTypeEnum.IMPORT)
    @PostMapping("/area_city_source/import_csv")
    public ResultVO operatorImport(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || StringUtils.isBlank(multipartFile.getOriginalFilename())) {
            return ResultVOUtil.error("导入文件为空");
        }
        Reader reader = new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8);
        CsvReader csvReader = CsvUtil.getReader(reader);
        CsvData read = csvReader.read();
        List<CsvRow> rows = read.getRows();
        AreaCitySource areaCitySource;
        List<AreaCitySource> alis = new ArrayList<>();
        CsvRow row;
        for (int i = 1; i < rows.size(); i++) {
            row = rows.get(i);
            int deep = Integer.parseInt(row.get(2));
            if (deep != 3) {//只入库省市区数据，镇数据的不入库
                areaCitySource = new AreaCitySource();
                //补齐6位
                areaCitySource.setAreaCode(String.format("%-6s", row.get(0)).replace(' ', '0'));
                //补齐6位
                areaCitySource.setParentAreaCode(String.format("%-6s", row.get(1)).replace(' ', '0'));
                areaCitySource.setDeep(deep);
                areaCitySource.setExtId(row.get(6));
                areaCitySource.setAreaName(row.get(7));
                alis.add(areaCitySource);
            }
        }
        areaCitySourceService.saveBatch(alis);
        return ResultVOUtil.success();
    }

    @Operation(summary = "树形结构")
    @OpLog(mod = "行政区域数据源", btn = "树形结构", opType = OpTypeEnum.QUERY)
    @GetMapping("/area_city_source/lazy_tree/{parentCode}")
    public ResultVO<List<AreaCitySourceTreeVO>> lazyTreeData(@PathVariable(value = "parentCode") String parentCode) {
        return ResultVOUtil.success(areaCitySourceService.lazyTreeData(parentCode));
    }

    @GetMapping("/areas")
    @Operation(summary = "全部行政区域")
    public ResultVO<List<RegionSelectVO>> getAllAreas() {
        return ResultVOUtil.success(areaCitySourceService.getAllArea());
    }

    @PostMapping("/area_name")
    @Operation(summary = "按行政区域码获取名称")
    public ResultVO<String> getAreaName(@RequestParam("areaCode") String areaCode) {
        return ResultVOUtil.success(areaCitySourceService.getAreaName(areaCode));
    }

}
