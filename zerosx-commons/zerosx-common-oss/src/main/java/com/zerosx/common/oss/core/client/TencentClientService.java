package com.zerosx.common.oss.core.client;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.zerosx.common.oss.core.config.IOssConfig;
import com.zerosx.common.oss.core.config.TencentOssConfig;
import com.zerosx.common.oss.model.OssObjectVO;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TencentClientService
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-08 14:25
 **/
@Slf4j
public class TencentClientService extends AbsOssClientService {

    private final TencentOssConfig tencentOssConfig;

    private final COSClient cosClient;

    public TencentClientService(TencentOssConfig tencentOssConfig) {
        this.tencentOssConfig = tencentOssConfig;
        this.cosClient = createCOSClient();
    }

    private COSClient createCOSClient() {
        COSCredentials cred = new BasicCOSCredentials(tencentOssConfig.getAccessKeyId(), tencentOssConfig.getAccessKeySecret());
        ClientConfig clientConfig = new ClientConfig(new Region(tencentOssConfig.getRegionId()));
        clientConfig.setHttpProtocol(HttpProtocol.https);
        return new COSClient(cred, clientConfig);
    }

    @Override
    public OssObjectVO upload(String objectName, InputStream is) {
        try {
            PutObjectResult putObjectResult = cosClient.putObject(tencentOssConfig.getBucketName(), objectName, is, null);
            OssObjectVO vo = viewUrl(objectName);
            return new OssObjectVO(objectName, putObjectResult.getETag(), vo.getObjectViewUrl(), vo.getExpiration());
        } catch (CosClientException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("文件查上传异常：" + e.getMessage());
        }
    }

    @Override
    public OssObjectVO viewUrl(String objectName) {
        // 设置签名过期时间(可选), 若未进行设置则默认使用 ClientConfig 中的签名过期时间(1小时)
        Date expirationDate = getExpireDate(tencentOssConfig.getExpireTime());
        // 请求的 HTTP 方法，上传请求用 PUT，下载请求用 GET，删除请求用 DELETE
        HttpMethodName method = HttpMethodName.GET;
        //URL url = cosClient.getObjectUrl(bucketName, objectName);
        // 填写本次请求的参数，需与实际请求相同，能够防止用户篡改此签名的 HTTP 请求的参数
        Map<String, String> params = new HashMap<String, String>();
        //params.put("param1", objectName);
        // 填写本次请求的头部，需与实际请求相同，能够防止用户篡改此签名的 HTTP 请求的头部
        Map<String, String> headers = new HashMap<String, String>();
        //headers.put("header1", objectName);
        URL url = null;
        try {
            url = cosClient.generatePresignedUrl(tencentOssConfig.getBucketName(), objectName, expirationDate, method, headers, params);
        } catch (CosClientException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("文件查查询异常：" + e.getMessage());
        }
        OssObjectVO vo = new OssObjectVO();
        vo.setObjectName(objectName);
        vo.setObjectViewUrl(url == null ? "" : url.toString());
        vo.setExpiration(expirationDate);
        return vo;
    }

    @Override
    public boolean delete(String objectName) {
        try {
            cosClient.deleteObject(tencentOssConfig.getBucketName(), objectName);
            return true;
        } catch (CosClientException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("文件查查询异常：" + e.getMessage());
        }
    }

    @Override
    public IOssConfig getConfig() {
        return this.tencentOssConfig;
    }

}
