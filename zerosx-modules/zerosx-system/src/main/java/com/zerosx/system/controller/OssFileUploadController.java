package com.zerosx.system.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.OssObjectVO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.annotation.SystemLog;
import com.zerosx.common.log.enums.BusinessType;
import com.zerosx.system.dto.OssFileUploadDTO;
import com.zerosx.system.service.IOssFileUploadService;
import com.zerosx.system.vo.OssFileUploadPageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Tag(name = "OSS文件")
@RestController
public class OssFileUploadController {

    @Autowired
    private IOssFileUploadService ossFileUploadService;

    @Operation(summary = "分页列表")
    @PostMapping("/oss_file/list_pages")
    @SystemLog(title = "文件管理", btnName = "分页查询", businessType = BusinessType.QUERY)
    public ResultVO<CustomPageVO<OssFileUploadPageVO>> listPages(@RequestBody RequestVO<OssFileUploadDTO> requestVO) {
        return ResultVOUtil.success(ossFileUploadService.listPages(requestVO));
    }

    /**
     * 单个文件上传
     *
     * @param multipartFile
     * @return
     */
    @Operation(summary = "单个文件上传")
    @PostMapping(value = "/upload_file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResultVO<OssObjectVO> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return ResultVOUtil.success(ossFileUploadService.upload(multipartFile));
    }

    @Operation(summary = "批量文件上传")
    @PostMapping(value = "/batch_upload_file")
    public ResultVO<List<OssObjectVO>> batchUploadFile(@RequestParam("file") MultipartFile[] multipartFile) {
        return ResultVOUtil.success(ossFileUploadService.batchUploadFile(multipartFile));
    }

    /**
     * 文件URL获取(单个)
     *
     * @param objectName
     * @return
     */
    @Operation(summary = "获取单个文件URL")
    @GetMapping(value = "/view_url/{objectName}")
    public ResultVO<String> getObjectViewUrl(@PathVariable("objectName") String objectName) {
        return ResultVOUtil.success(ossFileUploadService.getObjectViewUrl(objectName));
    }

    @Operation(summary = "按id查询")
    @SystemLog(title = "OSS文件", btnName = "按id查询", businessType = BusinessType.QUERY)
    @GetMapping("/oss_file/queryById/{id}")
    public ResultVO<OssFileUploadPageVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(ossFileUploadService.queryById(id));
    }

    /**
     * 文件下载
     *
     * @param objectName
     * @return
     */
    @Operation(summary = "文件下载")
    @GetMapping(value = "/get_download_file/{objectName}")
    public void downloadFile(@PathVariable("objectName") String objectName, HttpServletResponse response) throws IOException {
        ossFileUploadService.downloadFile(objectName, response);
    }

    /**
     * 文件删除
     *
     * @param objectName
     * @return
     */
    @Operation(summary = "文件删除")
    @DeleteMapping(value = "/delete_file/{objectName}/delete")
    public ResultVO deleteFile(@PathVariable("objectName") String objectName) {
        return ResultVOUtil.success(ossFileUploadService.deleteFile(objectName));
    }

    @Operation(summary = "批量删除(物理删除)")
    @GetMapping("/oss_file/full_delete/{ids}")
    @SystemLog(title = "文件管理", btnName = "批量删除(物理删除)", businessType = BusinessType.DELETE)
    public ResultVO<?> fullDelete(@PathVariable Long[] ids) {
        return ResultVOUtil.success(ossFileUploadService.fullDelete(ids));
    }

}
