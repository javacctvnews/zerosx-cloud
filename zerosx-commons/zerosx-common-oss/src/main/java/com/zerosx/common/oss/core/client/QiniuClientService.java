package com.zerosx.common.oss.core.client;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.zerosx.common.oss.core.config.IOssConfig;
import com.zerosx.common.oss.core.config.QiniuOssConfig;
import com.zerosx.common.oss.model.OssObjectVO;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;

/**
 * QiniuClientService
 * <p>七牛云
 *
 * @author: javacctvnews
 * @create: 2023-09-08 13:14
 **/
@Slf4j
public class QiniuClientService extends AbsOssClientService {

    private final QiniuOssConfig qiniuOssConfig;
    private final Auth auth;
    private final UploadManager uploadManager;
    private final BucketManager bucketManager;

    public QiniuClientService(QiniuOssConfig qiniuOssConfig) {
        this.qiniuOssConfig = qiniuOssConfig;
        this.auth = Auth.create(qiniuOssConfig.getAccessKeyId(), qiniuOssConfig.getAccessKeySecret());
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration();
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        uploadManager = new UploadManager(cfg);
        bucketManager = new BucketManager(auth, cfg);
    }


    @Override
    public OssObjectVO upload(String objectName, InputStream is) {
        String upToken = auth.uploadToken(qiniuOssConfig.getBucketName());
        try {
            Response response = uploadManager.put(is, objectName, upToken, null, null);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            OssObjectVO viewUrl = viewUrl(objectName);
            return new OssObjectVO(objectName, putRet.key, viewUrl.getObjectViewUrl(), viewUrl.getExpiration());
        } catch (QiniuException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("文件上传异常：" + e.getMessage());
        }
    }

    @Override
    public OssObjectVO viewUrl(String objectName) {
        String encodedFileName = null;
        try {
            encodedFileName = URLEncoder.encode(objectName, "utf-8").replace("+", "%20");

            String publicUrl = String.format("%s/%s", qiniuOssConfig.getDomainAddress(), encodedFileName);
            //可以自定义链接过期时间
            long expireInSeconds = qiniuOssConfig.getExpireTime() * 1000L;
            Date expireDate = getExpireDate(qiniuOssConfig.getExpireTime());
            String downloadUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
            return new OssObjectVO(objectName, "", downloadUrl, expireDate);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("文件查询异常：" + e.getMessage());
        }
    }

    @Override
    public boolean delete(String objectName) {
        try {
            Response response = bucketManager.delete(qiniuOssConfig.getBucketName(), objectName);
            return response.isOK();
        } catch (QiniuException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("文件查询异常：" + e.getMessage());
        }
    }

    @Override
    public IOssConfig getConfig() {
        return this.qiniuOssConfig;
    }

}
