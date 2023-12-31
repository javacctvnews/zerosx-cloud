package com.zerosx.common.core.utils;

import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.base.exception.BusinessException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 文件上传工具类
 */
public class FileUploadUtils {

    /**
     * 编码文件名
     */
    public static String extractFilename(MultipartFile file, String prefix) {
        String dateFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyMMdd"));
        if (StringUtils.isBlank(prefix)) {
            return MessageFormat.format("{0}_{1}.{2}", dateFormat, IdGenerator.nextSid(), getExtension(file));
        }
        return MessageFormat.format("{0}_{1}_{2}.{3}", prefix, dateFormat, IdGenerator.nextSid(), getExtension(file));
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

    /**
     * 文件大小
     *
     * @param fileS
     * @return
     */
    public static String formatSize(Long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        String wrongSize = "0B";
        if (fileS == null || fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}