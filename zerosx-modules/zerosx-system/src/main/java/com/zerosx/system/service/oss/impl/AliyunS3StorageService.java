//package com.zeros.system.service.oss.impl;
//
//import com.zerosx.common.base.vo.OssObjectVO;
//import com.zeros.oss.properties.FileServerProperties;
//import com.zeros.oss.templete.S3FileRepository;
//import com.zeros.system.service.oss.IObjectStorageService;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.InputStream;
//import java.util.Date;
//
//@Service
//@Slf4j
//public class AliyunS3StorageService implements IObjectStorageService {
//
//    @Autowired
//    private S3FileRepository s3FileRepository;
//
//    @Override
//    public String name() {
//        return FileServerProperties.S3;
//    }
//
//    /**
//     * @param objectName 对象名
//     * @param is         对象流
//     * @return
//     */
//    @Override
//    public OssObjectVO upload(String objectName, InputStream is) {
//        return s3FileRepository.upload(objectName, is);
//    }
//
//    @SneakyThrows
//    @Override
//    public OssObjectVO upload(String objectName, MultipartFile multipartFile) {
//        return s3FileRepository.upload(objectName, multipartFile.getInputStream());
//    }
//
//    @Override
//    public boolean delete(String objectName) {
//        return false;
//    }
//
//    @Override
//    public void download(String objectPath, HttpServletResponse response) {
//
//    }
//
//    @Override
//    public String viewUrl(String objectName, Date expirationDate) {
//        return null;
//    }
//}
