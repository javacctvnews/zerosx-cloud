package com.zerosx.common.core.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.utils.DateUtils2;
import com.zerosx.common.utils.JacksonUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;


@Slf4j
public class EasyExcelUtil {

    private static final String DEFAULT_SHEET_NAME = "Sheet1";

    private EasyExcelUtil() {

    }

    public static <T> void writeExcel(HttpServletResponse response, List<T> list, Class<?> entityClass) throws IOException {
        writeExcel(response, list, UUID.randomUUID().toString(), entityClass, ExcelTypeEnum.XLSX);
    }

    public static <T> void writeExcel(HttpServletResponse response, List<T> list, String fileName, Class<?> entityClass) throws IOException {
        writeExcel(response, list, fileName, entityClass, ExcelTypeEnum.XLSX);
    }


    public static <T> void writeExcel(HttpServletResponse response, List<T> list, String fileName, Class<?> entityClass, ExcelTypeEnum excelTypeEnum) throws IOException {
        try {
            EasyExcel.write(getOutputStream(response, fileName), entityClass)
                    .sheet(DEFAULT_SHEET_NAME)
                    .registerWriteHandler(new AutoColumnWidthWriteHandler())
                    .registerWriteHandler(new XHorizontalCellStyleStrategy())
                    .doWrite(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            ResultVO<?> resultVO = ResultVOUtil.error(ResultEnum.FAIL.getCode(), "下载文件失败:" + e.getMessage());
            response.getWriter().println(JacksonUtil.toJSONString(resultVO));
        }
    }

    public static OutputStream getOutputStream(HttpServletResponse response, String fileName) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20") + "_" + DateUtils2.now(DateUtils2.FORMAT_2);
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ExcelTypeEnum.XLSX.getValue());
            return response.getOutputStream();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OutputStream getOutputStream(HttpServletResponse response) {
        return getOutputStream(response, DateUtils2.now(DateUtils2.FORMAT_2));
    }

}


