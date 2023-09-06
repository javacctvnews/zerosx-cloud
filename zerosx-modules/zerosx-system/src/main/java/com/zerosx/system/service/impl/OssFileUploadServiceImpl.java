package com.zerosx.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.vo.OssObjectVO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.BeanCopierUtil;
import com.zerosx.common.base.utils.JacksonUtil;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.oss.properties.FileServerProperties;
import com.zerosx.common.oss.templete.S3FileOpService;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.redis.enums.RedisKeyNameEnum;
import com.zerosx.system.dto.OssFileUploadDTO;
import com.zerosx.system.entity.OssFileUpload;
import com.zerosx.system.mapper.IOssFileUploadMapper;
import com.zerosx.system.service.IOssFileUploadService;
import com.zerosx.system.utils.FileUploadUtils;
import com.zerosx.system.vo.OssFileUploadPageVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OssFileUploadServiceImpl extends SuperServiceImpl<IOssFileUploadMapper, OssFileUpload> implements IOssFileUploadService {

    private static final Long ONE_DAY_SEC = 3600L * 1000 * 24;

    @Autowired
    private FileServerProperties fileServerProperties;
    @Autowired
    private S3FileOpService s3FileOpService;
    @Autowired
    private RedissonOpService redissonOpService;

    @Override
    public boolean saveOssFile(String ossType, String originalFilename, OssObjectVO upload) {
        OssFileUpload ossFileUpload = new OssFileUpload();
        ossFileUpload.setOperatorId(ZerosSecurityContextHolder.getOperatorIds());
        ossFileUpload.setOssType(ossType);
        ossFileUpload.setOriginalFileName(originalFilename);
        ossFileUpload.setObjectName(upload.getObjectName());
        ossFileUpload.setObjectUrl(upload.getObjectPath());
        ossFileUpload.setObjectViewUrl(upload.getObjectViewUrl());
        return save(ossFileUpload);
    }

    @Override
    public OssFileUpload getByObjectName(String objectName) {
        LambdaQueryWrapper<OssFileUpload> ofuqw = Wrappers.lambdaQuery(OssFileUpload.class);
        ofuqw.select(OssFileUpload::getId, OssFileUpload::getObjectName, OssFileUpload::getObjectViewUrl, OssFileUpload::getExpirationTime);
        ofuqw.eq(OssFileUpload::getObjectName, objectName);
        return getOne(ofuqw);
    }

    @SneakyThrows
    @Override
    public OssObjectVO upload(MultipartFile multipartFile) {
        String objectName = FileUploadUtils.extractFilename(multipartFile, fileServerProperties.getFilePrefix());
        OssObjectVO upload = s3FileOpService.upload(objectName, multipartFile.getInputStream());
        log.debug(JacksonUtil.toJSONString(upload));
        //保存文件
        if (upload != null) {
            saveOssFile(fileServerProperties.getType(), multipartFile.getOriginalFilename(), upload);
        }
        return upload;
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
        String ossFileKey = RedisKeyNameEnum.key(RedisKeyNameEnum.OSS_FILE_URL, objectName);
        //先从Redis获取
        String cacheViewUrl = null;
        try {
            cacheViewUrl = redissonOpService.get(ossFileKey);
        } catch (Exception e) {
            redissonOpService.del(ossFileKey);
        }
        if (StringUtils.isNotBlank(cacheViewUrl)) {
            return cacheViewUrl;
        }
        OssFileUpload ossFile = getByObjectName(objectName);
        if (ossFile == null) {
            return "";
        }
        Integer expireDate = fileServerProperties.getS3().getExpireDate();
        if (ossFile.getExpirationTime() == null || ossFile.getExpirationTime().getTime() - System.currentTimeMillis() < 10 * 60 * 1000) {
            //有效期
            Date expirationDate = new Date(System.currentTimeMillis() + ONE_DAY_SEC * expireDate);
            String viewUrl = s3FileOpService.viewUrl(objectName, expirationDate);
            log.debug("【{}】获取文件URL:{} URL:{}", fileServerProperties.getType(), objectName, viewUrl);
            //更新有效期
            ossFile.setExpirationTime(expirationDate);
            ossFile.setObjectViewUrl(viewUrl);
            boolean b = updateById(ossFile);
            if (b) {
                redissonOpService.setExpire(ossFileKey, viewUrl, ONE_DAY_SEC * expireDate);
            }
            return viewUrl;
        }
        redissonOpService.setExpire(ossFileKey, ossFile.getObjectViewUrl(), ONE_DAY_SEC * expireDate);
        return ossFile.getObjectViewUrl();
    }

    @Override
    public List<String> getObjectViewUrls(String objectNames) {
        return null;
    }

    @Override
    public Map<String, String> getObjectViewUrlMap(List<String> objectNames) {
        return null;
    }

    @Override
    public void downloadFile(String objectName, HttpServletResponse response) {

    }

    @Override
    public boolean deleteFile(String objectName) {
        boolean deleted = s3FileOpService.deleteObject(objectName);
        if (deleted) {
            LambdaQueryWrapper<OssFileUpload> ossfileRm = Wrappers.lambdaQuery(OssFileUpload.class);
            ossfileRm.eq(OssFileUpload::getObjectName, objectName);
            boolean remove = remove(ossfileRm);
            if (remove) {
                redissonOpService.del(RedisKeyNameEnum.key(RedisKeyNameEnum.OSS_FILE_URL, objectName));
            }
        }
        return deleted;
    }

    @Override
    public CustomPageVO<OssFileUploadPageVO> listPages(RequestVO<OssFileUploadDTO> requestVO) {
        OssFileUploadDTO t = requestVO.getT() == null ? new OssFileUploadDTO() : requestVO.getT();
        LambdaQueryWrapper<OssFileUpload> qw = Wrappers.lambdaQuery(OssFileUpload.class);
        qw.eq(StringUtils.isNotBlank(t.getOssType()), OssFileUpload::getOssType, t.getOssType());
        qw.eq(StringUtils.isNotBlank(t.getOperatorId()), OssFileUpload::getOperatorId, t.getOperatorId());
        qw.and(StringUtils.isNotBlank(t.getFileName()), wp -> wp.like(OssFileUpload::getOriginalFileName, t.getFileName()).or().like(OssFileUpload::getObjectName, t.getFileName()));
        qw.orderByDesc(OssFileUpload::getCreateTime);
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, true), qw).convert((e) -> {
            return BeanCopierUtil.copyProperties(e, OssFileUploadPageVO.class);
        }));
    }

    @Override
    public boolean fullDelete(Long[] ids) {
        for (Long id : ids) {
            OssFileUpload ossFileUpload = getById(id);
            if (ossFileUpload != null) {
                String objectName = ossFileUpload.getObjectName();
                s3FileOpService.deleteObject(objectName);
                redissonOpService.del(RedisKeyNameEnum.key(RedisKeyNameEnum.OSS_FILE_URL, objectName));
                removeById(id);
            }
        }
        return true;
    }
}
