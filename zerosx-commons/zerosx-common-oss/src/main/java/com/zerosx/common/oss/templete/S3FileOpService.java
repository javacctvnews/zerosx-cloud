package com.zerosx.common.oss.templete;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.model.VoidResult;
import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.OssObjectVO;
import com.zerosx.common.oss.properties.FileServerProperties;
import com.zerosx.common.oss.properties.OssProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Slf4j
public class S3FileOpService {

    private OSS ossClient;

    private FileServerProperties fileServerProperties;

    public S3FileOpService(OSS ossClient,FileServerProperties fileServerProperties) {
        this.ossClient = ossClient;
        this.fileServerProperties = fileServerProperties;
    }

    /**
     * 上传
     *
     * @param objectName
     * @param is
     * @return
     */
    public OssObjectVO upload(String objectName, InputStream is) {
        OssProperties aliyun = fileServerProperties.getS3();
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(aliyun.getBucketName(), objectName, is);
            // 设置该属性可以返回response。如果不设置，则返回的response为空。
            putObjectRequest.setProcess("true");
            // 上传字符串。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            // 如果上传成功，则返回200。
            //log.debug("文件上传结果:{} 文件名:{}", result.getResponse().getStatusCode(), objectName);
            String objectViewUrl = viewUrl(objectName, null);
            if (result.getResponse().isSuccessful()) {
                return new OssObjectVO(objectName, result.getResponse().getUri(), objectViewUrl);
            }
        } catch (OSSException oe) {
            log.error("文件上传出现OSSException：" + objectName, oe);
            throw new BusinessException(ResultEnum.FAIL.getCode(), "文件上传出现OSSException");
        } catch (ClientException ce) {
            log.error("OSS客户端连接异常", ce);
            throw new BusinessException(ResultEnum.FAIL.getCode(), "OSS客户端连接异常");
        }
        return null;
    }

    /**
     * 查询访问连接
     *
     * @param objectName
     * @param expirationDate
     * @return
     */
    public String viewUrl(String objectName, Date expirationDate) {
        if (expirationDate == null) {
            Integer expireDate = fileServerProperties.getS3().getExpireDate();
            expirationDate = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * expireDate);
        }
        URL url = ossClient.generatePresignedUrl(fileServerProperties.getS3().getBucketName(), objectName, expirationDate, HttpMethod.GET);
        return url == null ? "" : url.toString();
    }

    /**
     * 删除文件
     * @param objectName
     * @return
     */
    public boolean deleteObject(String objectName) {
        VoidResult voidResult = ossClient.deleteObject(fileServerProperties.getS3().getBucketName(), objectName);
        return voidResult != null && voidResult.getResponse().isSuccessful();
    }
}
