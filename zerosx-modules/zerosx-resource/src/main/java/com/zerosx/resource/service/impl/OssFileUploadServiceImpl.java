package com.zerosx.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.EasyTransUtils;
import com.zerosx.common.core.utils.FileUploadUtils;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.oss.core.client.IOssClientService;
import com.zerosx.common.oss.core.config.IOssConfig;
import com.zerosx.common.oss.model.OssObjectVO;
import com.zerosx.common.oss.properties.DefaultOssProperties;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.resource.dto.OssFileUploadDTO;
import com.zerosx.resource.entity.OssFileUpload;
import com.zerosx.resource.mapper.IOssFileUploadMapper;
import com.zerosx.resource.service.IOssFileUploadService;
import com.zerosx.resource.service.IOssSupplierService;
import com.zerosx.resource.vo.OssFileUploadPageVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OssFileUploadServiceImpl extends SuperServiceImpl<IOssFileUploadMapper, OssFileUpload> implements IOssFileUploadService {

    private static final Long ONE_DAY_SEC = 3600L * 1000 * 24;

    @Autowired
    private DefaultOssProperties defaultOssProperties;
    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private IOssSupplierService ossSupplierService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOssFile(IOssConfig ossConfig, Long fileSize, String originalFilename, OssObjectVO upload) {
        OssFileUpload ossFileUpload = new OssFileUpload();
        ossFileUpload.setOperatorId(ZerosSecurityContextHolder.getOperatorIds());
        ossFileUpload.setOssSupplierId(ossConfig.getOssSupplierId());
        ossFileUpload.setOssType(ossConfig.getSupplierType());
        ossFileUpload.setAccessKeyId(ossConfig.getAccessKeyId());
        ossFileUpload.setBucketName(ossConfig.getBucketName());
        ossFileUpload.setOriginalFileName(originalFilename);
        ossFileUpload.setObjectName(upload.getObjectName());
        ossFileUpload.setObjectSize(fileSize);
        ossFileUpload.setObjectUrl(upload.getObjectPath());
        ossFileUpload.setObjectViewUrl(upload.getObjectViewUrl());
        ossFileUpload.setExpirationTime(upload.getExpiration());
        return save(ossFileUpload);
    }

    @Override
    public OssFileUpload getByObjectName(String objectName) {
        return baseMapper.selectByObjectName(objectName);
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OssObjectVO upload(MultipartFile multipartFile) {
        String objectName = FileUploadUtils.extractFilename(multipartFile, defaultOssProperties.getFilePrefix());
        //获取IOssClientService实例
        IOssClientService ossClientService = getOssClientService(null);
        //上传
        OssObjectVO ossObjectVO = ossClientService.upload(objectName, multipartFile.getInputStream());
        //保存文件
        saveOssFile(ossClientService.getConfig(), multipartFile.getSize(), multipartFile.getOriginalFilename(), ossObjectVO);
        if (StringUtils.isBlank(ossObjectVO.getObjectViewUrl())) {
            throw new BusinessException("上传失败");
        }
        //放入缓存
        long expireTime = (ossObjectVO.getExpiration().getTime() - new Date().getTime()) / 1000;
        redissonOpService.set(ZCache.OSS_FILE_URL.key(objectName), JacksonUtil.toJSONString(ossObjectVO), expireTime);
        return ossObjectVO;
    }

    private IOssClientService getOssClientService(Long ossSupplierId) {
        IOssClientService ossClientService = ossSupplierService.getClient(ossSupplierId);
        if (ossClientService == null) {
            throw new BusinessException("没有找到IOssClientService实例，上传失败");
        }
        return ossClientService;
    }

    @Override
    public List<OssObjectVO> batchUploadFile(MultipartFile[] multipartFile) {
        List<OssObjectVO> objectVOS = new ArrayList<>();
        for (MultipartFile file : multipartFile) {
            try {
                objectVOS.add(upload(file));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return objectVOS;
    }

    @Override
    public String getObjectViewUrl(String objectName) {
        if (StringUtils.isBlank(objectName)) {
            return StringUtils.EMPTY;
        }
        OssFileUpload ossFile = getByObjectName(objectName);
        if (ossFile == null) {
            return StringUtils.EMPTY;
        }
        handleOssFileUpload(ossFile);
        return ossFile.getObjectViewUrl();
    }

    @Override
    public void downloadFile(String objectName, HttpServletResponse response) {

    }

    @Override
    public boolean deleteFile(String objectName) {
        OssFileUpload ossFileUpload = getByObjectName(objectName);
        if (ossFileUpload == null) {
            return true;
        }
        IOssClientService ossClientService = getOssClientService(ossFileUpload.getOssSupplierId());
        boolean deleted = ossClientService.delete(objectName);
        if (deleted) {
            LambdaQueryWrapper<OssFileUpload> ossfileRm = Wrappers.lambdaQuery(OssFileUpload.class);
            ossfileRm.eq(OssFileUpload::getObjectName, objectName);
            boolean remove = remove(ossfileRm);
            if (remove) {
                redissonOpService.del(ZCache.OSS_FILE_URL.key(objectName));
            }
        }
        return deleted;
    }

    @Override
    public CustomPageVO<OssFileUploadPageVO> listPages(RequestVO<OssFileUploadDTO> requestVO) {
        OssFileUploadDTO t = requestVO.getT() == null ? new OssFileUploadDTO() : requestVO.getT();
        LambdaQueryWrapper<OssFileUpload> qw = Wrappers.lambdaQuery(OssFileUpload.class);
        qw.eq(StringUtils.isNotBlank(t.getOssType()), OssFileUpload::getOssType, t.getOssType());
        qw.eq(t.getOssSupplierId() != null, OssFileUpload::getOssSupplierId, t.getOssSupplierId());
        qw.eq(StringUtils.isNotBlank(t.getOperatorId()), OssFileUpload::getOperatorId, t.getOperatorId());
        qw.eq(StringUtils.isNotBlank(t.getBucketName()), OssFileUpload::getBucketName, t.getBucketName());
        qw.and(StringUtils.isNotBlank(t.getFileName()), wp -> wp.like(OssFileUpload::getOriginalFileName, t.getFileName()).or().like(OssFileUpload::getObjectName, t.getFileName()));
        qw.orderByDesc(OssFileUpload::getCreateTime);
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, true), qw).convert((e) -> {
            OssFileUploadPageVO vo = EasyTransUtils.copyTrans(e, OssFileUploadPageVO.class);
            vo.setObjectSizeStr(FileUploadUtils.formatSize(e.getObjectSize()));
            return vo;
        }));
    }

    @Override
    public boolean fullDelete(Long[] ids) {
        for (Long id : ids) {
            OssFileUpload ossFileUpload = getById(id);
            if (ossFileUpload != null) {
                IOssClientService ossClientService = getOssClientService(ossFileUpload.getOssSupplierId());
                String objectName = ossFileUpload.getObjectName();
                ossClientService.delete(objectName);
                redissonOpService.del(ZCache.OSS_FILE_URL.key(objectName));
                removeById(id);
            }
        }
        return true;
    }

    @Override
    public OssFileUploadPageVO queryById(Long id) {
        OssFileUpload fileUpload = getById(id);
        handleOssFileUpload(fileUpload);
        return EasyTransUtils.copyTrans(fileUpload, OssFileUploadPageVO.class);
    }

    private void handleOssFileUpload(OssFileUpload fileUpload) {
        if (fileUpload == null) {
            return;
        }
        String objectName = fileUpload.getObjectName();
        String ossFileKey = ZCache.OSS_FILE_URL.key(objectName);
        //先从Redis获取
        String cacheViewUrl = null;
        try {
            String ossObjectStr = redissonOpService.get(ossFileKey);
            OssObjectVO ossObjectVO = JacksonUtil.toObject(ossObjectStr, OssObjectVO.class);
            if (ossObjectVO != null && !ossObjectVO.expired()) {
                cacheViewUrl = ossObjectVO.getObjectViewUrl();
            }
        } catch (Exception e) {
            redissonOpService.del(ossFileKey);
        }
        if (StringUtils.isBlank(cacheViewUrl)) {
            //查询访问url
            IOssClientService ossClientService = ossSupplierService.getClient(fileUpload.getOssSupplierId());
            OssObjectVO vo = ossClientService.viewUrl(objectName);
            //更新db的过期时间
            if (StringUtils.isNotBlank(vo.getObjectViewUrl())) {
                cacheViewUrl = vo.getObjectViewUrl();
                LambdaUpdateWrapper<OssFileUpload> uw = Wrappers.lambdaUpdate(OssFileUpload.class);
                uw.set(OssFileUpload::getObjectViewUrl, vo.getObjectViewUrl());
                uw.set(OssFileUpload::getExpirationTime, vo.getExpiration());
                uw.eq(OssFileUpload::getObjectName, objectName);
                update(null, uw);
                //放入缓存
                long expireTime = (vo.getExpiration().getTime() - new Date().getTime()) / 1000;
                redissonOpService.set(ossFileKey, JacksonUtil.toJSONString(vo), expireTime);
            }
        }
        fileUpload.setObjectViewUrl(cacheViewUrl);
    }

}
