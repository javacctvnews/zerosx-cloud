package com.zerosx.system.service;

import com.zerosx.common.base.vo.OssObjectVO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.system.dto.OssFileUploadDTO;
import com.zerosx.system.entity.OssFileUpload;
import com.zerosx.system.vo.OssFileUploadPageVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface IOssFileUploadService extends ISuperService<OssFileUpload> {

    boolean saveOssFile(String type, String originalFilename, OssObjectVO upload);

    OssFileUpload getByObjectName(String objectName);

    /**
     * 文件上传
     * @param multipartFile
     * @return
     */
    OssObjectVO upload(MultipartFile multipartFile);

    List<OssObjectVO> batchUploadFile(MultipartFile[] multipartFile);

    String getObjectViewUrl(String objectName);

    List<String> getObjectViewUrls(String objectNames);

    Map<String, String> getObjectViewUrlMap(List<String> objectNames);

    void downloadFile(String objectName, HttpServletResponse response);

    boolean deleteFile(String objectName);

    CustomPageVO<OssFileUploadPageVO> listPages(RequestVO<OssFileUploadDTO> requestVO);

    boolean fullDelete(Long[] ids);
}
