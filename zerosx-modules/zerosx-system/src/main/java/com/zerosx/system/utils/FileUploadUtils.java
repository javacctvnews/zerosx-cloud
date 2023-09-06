package com.zerosx.system.utils;

import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.common.core.utils.MimeTypeUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 文件上传工具类
 *
 */
public class FileUploadUtils {

    /**
     * 编码文件名
     */
    public static String extractFilename(MultipartFile file, String prefix) {
        String dateFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyMMdd"));
        //return MessageFormat.format("{0}_{1}_{2}.{3}", dateFormat, FilenameUtils.getBaseName(file.getOriginalFilename()), IdGenerator.getIdStr(), getExtension(file));
        if (StringUtils.isBlank(prefix)) {
            return MessageFormat.format("{0}_{1}.{2}", dateFormat, IdGenerator.getIdStr(), getExtension(file));
        }
        return MessageFormat.format("{0}_{1}_{2}.{3}", prefix, dateFormat, IdGenerator.getIdStr(), getExtension(file));
    }


    /**
     * 1、文件大小校验
     * 等等
     *
     * @param file
     * @param maxSize 0：无限制
     */
    public static void assertAllowed(MultipartFile file, long maxSize) {
        if (maxSize == 0) {
            return;
        }
        long size = file.getSize();
        if (size > maxSize) {
            throw new BusinessException(ResultEnum.FAIL.getCode(), "文件大小不可超过" + maxSize / 1024 / 1024 + "M");
        }
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension        上传文件类型
     * @param allowedExtension 允许上传文件类型
     * @return true/false
     */
    public static boolean isAllowedExtension(String extension, String[] allowedExtension) {
        return true;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isBlank(extension)) {
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }


}