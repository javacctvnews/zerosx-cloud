package com.zerosx.common.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.core.config.properties.EasyExcelProperties;
import com.zerosx.common.core.easyexcel.AutoColumnWidthWriteHandler;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.core.easyexcel.XHorizontalCellStyleStrategy;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.core.utils.EasyTransUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * service实现父类
 *
 * @param <M>
 * @param <T>
 */
@Slf4j
public class SuperServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements ISuperService<T> {

    @Autowired
    protected EasyExcelProperties easyExcelProperties;
    //每次查询的条数 5w
    //protected static final int querySize = 50000;
    //一个sheet最大的条数 100w
    //protected static final int sheetNum = 1000000;

    protected void checkEasyExcelProperties() {
        if (!easyExcelProperties.getEnabled()) {
            throw new BusinessException("导出功能已关闭");
        }
        if (easyExcelProperties.getQuerySize() > easyExcelProperties.getSheetNum()) {
            throw new BusinessException("querySize值不能大于sheetNum的值");
        }
    }

    /**
     * 导出Excel，资源充足情况下支持大数据量导出，测试过100w数据（jvm配置：-Xms2g -Xmx2g）
     * 说明：
     * 1）每次查询最大数据量为5w
     * 2）超出100w数据时将写入下一个sheet
     *
     * @param page         分页对象，带前端选择的列的排序
     * @param exportClz    表头的类型
     * @param queryWrapper 分页Wrapper
     * @param response     response
     */
    public void excelExport(Page<T> page, Wrapper<T> queryWrapper, Class<?> exportClz, HttpServletResponse response) {
        checkEasyExcelProperties();
        long t1 = System.currentTimeMillis();
        long totalCount = baseMapper.selectCount(queryWrapper);
        log.debug("查询总数{}条，耗时{}ms", totalCount, System.currentTimeMillis() - t1);
        if (totalCount <= 0) {
            return;
        }
        Integer sheetNum = easyExcelProperties.getSheetNum();
        Integer querySize = easyExcelProperties.getQuerySize();
        //每个sheet最大查询次数
        int count = sheetNum / querySize;
        //多少个sheet页
        int sheetLoop = (int) Math.ceil((double) totalCount / sheetNum);
        //已查询条数
        int queryCurrentCount = 0;
        try (ExcelWriter excelWriter = EasyExcel.write(EasyExcelUtil.getOutputStream(response), exportClz).build()) {
            //循环写入sheet页数据
            for (int i = 0; i < sheetLoop; i++) {
                if (queryCurrentCount >= totalCount) {
                    break;
                }
                //创建WriteSheet
                WriteSheet writeSheet = EasyExcel.writerSheet("Sheet" + (i + 1))
                        .registerWriteHandler(new AutoColumnWidthWriteHandler())
                        .registerWriteHandler(new XHorizontalCellStyleStrategy()).build();
                Page<T> pageResult;
                List<T> records;
                for (int j1 = 0; j1 < count; j1++) {
                    if (queryCurrentCount >= totalCount) {
                        break;
                    }
                    int pageNum = (i * count) + (j1 + 1);
                    long t11 = System.currentTimeMillis();
                    page.setCurrent(pageNum);
                    page.setSize(querySize);
                    pageResult = baseMapper.selectPage(page, queryWrapper);
                    records = pageResult.getRecords();
                    int size = records.size();
                    queryCurrentCount += size;
                    log.debug("第{}页查询，每页{}条 实际{}条，耗时{}ms", pageNum, querySize, size, System.currentTimeMillis() - t11);
                    if (size > 0) {
                        excelWriter.write(EasyTransUtils.copyTrans(records, exportClz), writeSheet);
                    }
                }
            }
        }
        log.debug("【{}】执行导出{}条，总耗时:{}ms", ZerosSecurityContextHolder.getUserName(), totalCount, System.currentTimeMillis() - t1);
    }

}
