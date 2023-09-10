package com.zerosx.common.oss.core.client;

import com.aliyun.oss.*;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.model.VoidResult;
import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.OssObjectVO;
import com.zerosx.common.oss.core.config.AliyunOssConfig;
import com.zerosx.common.oss.core.config.IOssConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * AliyunClientService
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-08 13:02
 **/
@Slf4j
public class AliyunClientService extends AbsOssClientService {

    private final AliyunOssConfig aliyunOssConfig;

    private final OSS ossClient;

    public AliyunClientService(AliyunOssConfig aliyunOssConfig) {
        this.aliyunOssConfig = aliyunOssConfig;
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        this.ossClient = new OSSClientBuilder().build(
                aliyunOssConfig.getEndpoint(),
                aliyunOssConfig.getAccessKeyId(),
                aliyunOssConfig.getAccessKeySecret(),
                conf);
    }

    @Override
    public OssObjectVO upload(String objectName, InputStream is) {
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(aliyunOssConfig.getBucketName(), objectName, is);
            // 设置该属性可以返回response。如果不设置，则返回的response为空。
            putObjectRequest.setProcess("true");
            // 上传字符串。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            // 如果上传成功，则返回200。
            //log.debug("文件上传结果:{} 文件名:{}", result.getResponse().getStatusCode(), objectName);
            if (result.getResponse().isSuccessful()) {
                OssObjectVO vo = viewUrl(objectName);
                return new OssObjectVO(objectName, result.getETag(), vo.getObjectViewUrl(), vo.getExpiration());
            }
        } catch (OSSException oe) {
            log.error("文件上传出现OSSException：" + objectName, oe);
            throw new BusinessException(ResultEnum.FAIL.getCode(), "文件上传异常：" + oe.getErrorMessage());
        } catch (ClientException ce) {
            log.error("OSS客户端连接异常", ce);
            throw new BusinessException(ResultEnum.FAIL.getCode(), "OSS客户端连接异常：" + ce.getErrorMessage());
        }
        return null;
    }

    @Override
    public OssObjectVO viewUrl(String objectName) {
        // 设置请求头。
        Map<String, String> headers = new HashMap<String, String>();
        /*// 指定Object的存储类型。
        headers.put(OSSHeaders.STORAGE_CLASS, StorageClass.Standard.toString());
        // 指定ContentType。
        headers.put(OSSHeaders.CONTENT_TYPE, "text/txt");*/

        // 设置用户自定义元信息。
        Map<String, String> userMetadata = new HashMap<String, String>();
        /*userMetadata.put("key1","value1");
        userMetadata.put("key2","value2");*/
        Date expireDate = getExpireDate(aliyunOssConfig.getExpireTime());
        URL url = null;
        try {
            // 生成签名URL。
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(aliyunOssConfig.getBucketName(), objectName, HttpMethod.GET);
            // 设置过期时间。
            request.setExpiration(expireDate);
            // 将请求头加入到request中。
            request.setHeaders(headers);
            // 添加用户自定义元信息。
            request.setUserMetadata(userMetadata);
            // 设置查询参数。
            // Map<String, String> queryParam = new HashMap<String, String>();
            // 指定IP地址或者IP地址段。
            // queryParam.put("x-oss-ac-source-ip","192.0.2.0");
            // 指定子网掩码中1的个数。
            // queryParam.put("x-oss-ac-subnet-mask","32");
            // 指定VPC ID。
            // queryParam.put("x-oss-ac-vpc-id","vpc-12345678");
            // 指定是否允许转发请求。
            // queryParam.put("x-oss-ac-forward-allow","true");
            // request.setQueryParameter(queryParam);

            // 设置单链接限速，单位为bit，例如限速100 KB/s。
            // request.setTrafficLimit(100 * 1024 * 8);
            // 通过HTTP GET请求生成签名URL。
            url = ossClient.generatePresignedUrl(request);
        } catch (OSSException oe) {
            log.error(oe.getMessage(), oe);
            throw new BusinessException(ResultEnum.FAIL.getCode(), "文件查询异常：" + oe.getErrorMessage());
        } catch (ClientException ce) {
            log.error(ce.getMessage(), ce);
            throw new BusinessException(ResultEnum.FAIL.getCode(), "OSS客户端连接异常：" + ce.getErrorMessage());
        }
        OssObjectVO vo = new OssObjectVO();
        vo.setObjectName(objectName);
        vo.setObjectViewUrl(url == null ? "" : url.toString());
        vo.setExpiration(expireDate);
        return vo;
    }

    @Override
    public boolean delete(String objectName) {
        VoidResult voidResult;
        try {
            voidResult = ossClient.deleteObject(aliyunOssConfig.getBucketName(), objectName);
        } catch (OSSException oe) {
            log.error(oe.getMessage(), oe);
            throw new BusinessException(ResultEnum.FAIL.getCode(), "文件删除异常：" + oe.getErrorMessage());
        } catch (ClientException ce) {
            log.error(ce.getMessage(), ce);
            throw new BusinessException(ResultEnum.FAIL.getCode(), "OSS客户端连接异常：" + ce.getErrorMessage());
        }
        return voidResult != null && voidResult.getResponse().isSuccessful();
    }

    @Override
    public IOssConfig getConfig() {
        return this.aliyunOssConfig;
    }
}
