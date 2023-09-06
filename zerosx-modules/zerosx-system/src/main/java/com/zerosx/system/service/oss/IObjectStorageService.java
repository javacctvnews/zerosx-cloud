//package com.zeros.system.service.oss;
//
//import com.zerosx.common.base.vo.OssObjectVO;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.InputStream;
//import java.util.Date;
//
///**
// * oss顶级接口
// * fastfds、minio等等
// *
// * 本系统实现阿里云oss、ftp
// */
//public interface IObjectStorageService {
//
//    String name();
//
//    /**
//     * 上传对象
//     *
//     * @param objectName 对象名
//     * @param is         对象流
//     */
//    OssObjectVO upload(String objectName, InputStream is);
//
//    /**
//     * 上传对象
//     *
//     * @param file 对象
//     */
//    OssObjectVO upload(String objectName, MultipartFile file);
//
//    /**
//     * 删除对象
//     *
//     * @param objectKey 对象标识
//     */
//    boolean delete(String objectKey);
//
//    /**
//     * 文件下载
//     * @param objectPath
//     * @param response
//     */
//    void download(String objectPath, HttpServletResponse response);
//
//    /**
//     * 查看文件
//     * @param objectName 对象名称
//     * @return URL
//     */
//    String viewUrl(String objectName, Date expirationDate);
//
//}
