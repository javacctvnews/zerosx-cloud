package com.zerosx.resource.service;

import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.oss.core.config.IOssConfig;
import com.zerosx.common.oss.model.OssObjectVO;
import com.zerosx.resource.dto.OssFileUploadDTO;
import com.zerosx.resource.entity.OssFileUpload;
import com.zerosx.resource.vo.OssFileUploadPageVO;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public interface IOssFileUploadService extends ISuperService<OssFileUpload> {

    boolean saveOssFile(IOssConfig ossConfig, Long fileSize, String originalFilename, OssObjectVO upload);

    OssFileUpload getByObjectName(String objectName);

    /**
     * 文件上传
     *
     * @param multipartFile
     * @return
     */
    OssObjectVO upload(MultipartFile multipartFile);

    List<OssObjectVO> batchUploadFile(MultipartFile[] multipartFile);

    String getObjectViewUrl(String objectName);

    void downloadFile(String objectName, HttpServletResponse response);

    boolean deleteFile(String objectName);

    CustomPageVO<OssFileUploadPageVO> listPages(RequestVO<OssFileUploadDTO> requestVO);

    boolean fullDelete(Long[] ids);

    OssFileUploadPageVO queryById(Long id);
}
