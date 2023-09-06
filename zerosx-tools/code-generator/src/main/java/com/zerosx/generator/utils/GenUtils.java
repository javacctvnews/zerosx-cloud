package com.zerosx.generator.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileWriter;
import com.zerosx.generator.model.ColumnEntity;
import com.zerosx.generator.model.TableEntity;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.core.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器工具类
 */
@Slf4j
public class GenUtils {
    private GenUtils() {
        throw new IllegalStateException("Utility class");
    }

    private final static String FILE_NAME_MODEL = "Model.java.vm";
    private final static String FILE_NAME_MODEL_DTO = "ModelDTO.java.vm";
    private final static String FILE_NAME_MODEL_PAGE_DTO = "ModelPageDTO.java.vm";
    private final static String FILE_NAME_MODEL_VO = "ModelVO.java.vm";
    private final static String FILE_NAME_MODEL_Page_VO = "ModelPageVO.java.vm";
    private final static String FILE_NAME_MAPPER = "Mapper.java.vm";
    private final static String FILE_NAME_MAPPERXML = "Mapper.xml.vm";
    private final static String FILE_NAME_SERVICE = "Service.java.vm";
    private final static String FILE_NAME_SERVICEIMPL = "ServiceImpl.java.vm";
    private final static String FILE_NAME_CONTROLLER = "Controller.java.vm";
    private final static String FILE_NAME_VUE_API = "vue/api.js.vm";
    private final static String FILE_NAME_VUE_INDEX = "vue/index.vue.vm";
    private final static String TEMPLATE_PATH = "template/";
    private final static String PACKAGE = "package";
    private final static String MODULE_NAME = "moduleName";

    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<>();
        templates.add(TEMPLATE_PATH + FILE_NAME_MODEL);
        templates.add(TEMPLATE_PATH + FILE_NAME_MODEL_DTO);
        templates.add(TEMPLATE_PATH + FILE_NAME_MODEL_PAGE_DTO);
        templates.add(TEMPLATE_PATH + FILE_NAME_MODEL_VO);
        templates.add(TEMPLATE_PATH + FILE_NAME_MODEL_Page_VO);
        templates.add(TEMPLATE_PATH + FILE_NAME_MAPPER);
        templates.add(TEMPLATE_PATH + FILE_NAME_MAPPERXML);
        templates.add(TEMPLATE_PATH + FILE_NAME_SERVICE);
        templates.add(TEMPLATE_PATH + FILE_NAME_SERVICEIMPL);
        templates.add(TEMPLATE_PATH + FILE_NAME_CONTROLLER);
        templates.add(TEMPLATE_PATH + FILE_NAME_VUE_API);
        templates.add(TEMPLATE_PATH + FILE_NAME_VUE_INDEX);
        return templates;
    }

    public static void generatorCode(Map<String, String> table, List<Map<String, String>> columns) {
        generatorCode(table, columns, null, null);
    }

    /**
     * 生成代码
     */
    public static void generatorCode(Map<String, String> table, List<Map<String, String>> columns, ZipOutputStream zip, String zipPath) {
        //配置信息
        Configuration config = getConfig();
        boolean hasBigDecimal = false;
        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.get("tableName"));
        tableEntity.setComments(table.get("tableComment"));
        if (StringUtils.isBlank(tableEntity.getComments())) {
            throw new BusinessException(String.format("数据库表【%s】的注释不能为空", tableEntity.getTableName()));
        }
        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), config.getString("tablePrefix"));
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        map.put("operatorId", false);//默认没有operatorId
        for (Map<String, String> column : columns) {
            if("operator_id".equals(column.get("columnName"))){
                map.put("operatorId", true);//有operatorId
            }
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("columnName"));
            columnEntity.setDataType(column.get("dataType"));
            columnEntity.setComments(column.get("columnComment"));
            columnEntity.setIsNull(column.get("isNullable"));
            columnEntity.setColumnLength(String.valueOf(column.get("columnLength")));
            //备注不能为空
            if (StringUtils.isBlank(columnEntity.getComments())) {
                throw new BusinessException(String.format("字段【%s】的注释不能为空", column.get("columnName")));
            }
            columnEntity.setExtra(column.get("extra"));

            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), "unknowType");
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && "BigDecimal".equals(attrType)) {
                hasBigDecimal = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        String mainPath = config.getString("mainPath");
        mainPath = StringUtils.isBlank(mainPath) ? "io.renren" : mainPath;

        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        //map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("pathName", getPathName(tableEntity.getTableName(), config.getString("tablePrefix")));
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("mainPath", mainPath);
        map.put(PACKAGE, config.getString(PACKAGE));
        map.put(MODULE_NAME, config.getString(MODULE_NAME));
        String author = config.getString("author");
        if (StringUtils.isBlank(author)) {
            throw new BusinessException("author不能为空");
        }
        map.put("author", author);
        map.put("email", config.getString("email"));
        map.put("datetime", DateUtil.format(new Date(), DateTimeUtil.FORMAT_1));
        VelocityContext context = new VelocityContext(map);

        //代码存放的绝对路径
        String absolutePath = config.getString("absolutePath");
        if (zip != null) {
            absolutePath = zipPath;
        } else {
            if (StringUtils.isBlank(absolutePath)) {
                throw new BusinessException("absolutePath不能为空");
            }
        }
        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            try (
                    StringWriter sw = new StringWriter()
            ) {
                Template tpl = Velocity.getTemplate(template, "UTF-8");
                tpl.merge(context, sw);
                String totalFileName = getFileName(absolutePath, template, tableEntity.getClassName(), tableEntity.getClassname(), config.getString(PACKAGE), config.getString(MODULE_NAME));
                if (zip == null) {
                    if (StringUtils.isNotBlank(totalFileName)) {
                        FileWriter.create(new File(totalFileName)).write(sw.toString(), false);
                    }
                } else {
                    //添加到zip
                    if (StringUtils.isNotBlank(totalFileName)) {
                        zip.putNextEntry(new ZipEntry(totalFileName));
                        IOUtils.write(sw.toString(), zip, StandardCharsets.UTF_8);
                    }
                    zip.closeEntry();
                }
            } catch (IOException e) {
                log.error("generatorCode-error", e);
            }
        }
    }

    public static String getPathName(String tableName, String tablePrefix) {
        return tableName.substring(tablePrefix.length());
    }

    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix) && tableName.startsWith(tablePrefix)) {
            tableName = tableName.substring(tablePrefix.length());
        }
        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new RuntimeException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String absolutePath, String template, String className, String classname, String packageName, String moduleName) {
        String packagePath = absolutePath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
        }

        if (template.contains(FILE_NAME_MODEL)) {
            return packagePath + "entity" + File.separator + className + ".java";
        }

        if (template.contains(FILE_NAME_MODEL_DTO)) {
            return packagePath + "dto" + File.separator + className + "DTO.java";
        }

        if (template.contains(FILE_NAME_MODEL_PAGE_DTO)) {
            return packagePath + "dto" + File.separator + className + "PageDTO.java";
        }

        if (template.contains(FILE_NAME_MODEL_VO)) {
            return packagePath + "vo" + File.separator + className + "VO.java";
        }

        if (template.contains(FILE_NAME_MODEL_Page_VO)) {
            return packagePath + "vo" + File.separator + className + "PageVO.java";
        }

        if (template.contains(FILE_NAME_MAPPER)) {
            return packagePath + "mapper" + File.separator + "I" + className + "Mapper.java";
        }

        if (template.contains(FILE_NAME_SERVICE)) {
            return packagePath + "service" + File.separator + "I" + className + "Service.java";
        }

        if (template.contains(FILE_NAME_SERVICEIMPL)) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains(FILE_NAME_CONTROLLER)) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if (template.contains(FILE_NAME_MAPPERXML)) {
            return absolutePath + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + className + "Mapper.xml";
        }
        if (template.contains(FILE_NAME_VUE_API)) {
            return absolutePath + File.separator + "views" + File.separator + "api" + File.separator + classname + ".js";
        }
        if (template.contains(FILE_NAME_VUE_INDEX)) {
            return absolutePath + File.separator + "views" + File.separator + "pages" + File.separator + classname + File.separator + "index.vue";
        }
        return null;
    }
}
