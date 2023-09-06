package com.zerosx.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerosx.generator.mapper.SysGeneratorMapper;
import com.zerosx.generator.service.SysGeneratorService;
import com.zerosx.generator.utils.GenUtils;
import com.zerosx.common.base.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;


@Slf4j
@Service
public class SysGeneratorServiceImpl extends ServiceImpl implements SysGeneratorService {

    @Autowired
    private SysGeneratorMapper sysGeneratorMapper;

    @Override
    public Map<String, String> queryTable(String tableName) {
        return sysGeneratorMapper.queryTable(tableName);
    }

    @Override
    public List<Map<String, String>> queryColumns(String tableName) {
        return sysGeneratorMapper.queryColumns(tableName);
    }

    @Override
    public byte[] generatorCode(String[] tableNames, boolean outZip, String zipPath) {
        if (outZip) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (
                ZipOutputStream zip = new ZipOutputStream(outputStream);
            ) {
                createCode(tableNames, true, zip, zipPath);
            } catch (IOException e) {
                log.error("generatorCode-error: ", e);
            }
            return outputStream.toByteArray();
        } else {
            createCode(tableNames, false, null, null);
            return null;
        }
    }

    private void createCode(String[] tableNames, boolean outZip, ZipOutputStream zip, String zipPath) {
        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> table = queryTable(tableName);
            if (table == null || table.isEmpty()) {
                throw new BusinessException("表不存在:" + tableName);
            }
            //查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            //生成代码
            if (outZip) {
                GenUtils.generatorCode(table, columns, zip, zipPath);
            } else {
                GenUtils.generatorCode(table, columns);
            }
        }
    }
}
