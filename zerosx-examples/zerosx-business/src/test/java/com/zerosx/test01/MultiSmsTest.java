package com.zerosx.test01;

import com.zerosx.common.oss.model.OssObjectVO;
import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.common.oss.core.OSSFactory;
import com.zerosx.common.oss.core.client.IOssClientService;
import com.zerosx.common.oss.enums.OssTypeEnum;
import com.zerosx.common.oss.core.config.AliyunOssConfig;
import com.zerosx.common.oss.core.config.QiniuOssConfig;
import com.zerosx.common.oss.core.config.TencentOssConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;

/**
 * MultiSmsTest
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-08-30 15:10
 **/
@Slf4j
public class MultiSmsTest {


    @Test
    public void testAliyun() throws Exception {
        AliyunOssConfig config = new AliyunOssConfig();
        config.setAccessKeyId("***************************");
        config.setAccessKeySecret("***************************");
        config.setBucketName("zeros-cloud-oss");
        config.setRegionId("oss-cn-shenzhen");
        config.setEndpoint("oss-cn-shenzhen.aliyuncs.com");
        IOssClientService ossClientService = OSSFactory.createClient(OssTypeEnum.ALIBABA, config);

        String fileName = "C:\\Users\\cyh\\Pictures\\desktopImg\\微信图片_20230802205217.png";
        String objectName = IdGenerator.nextSid() + ".png";
        FileInputStream fileInputStream = new FileInputStream(fileName);
        OssObjectVO objectVO = ossClientService.upload(objectName, fileInputStream);
        if (objectVO != null) {
            log.debug("访问:{}", objectVO.getObjectViewUrl());
        }
    }

    @Test
    public void testTent() throws Exception {
        TencentOssConfig config = new TencentOssConfig();
        config.setAccessKeyId("***************************");
        config.setAccessKeySecret("***************************");
        config.setBucketName("zerosx-cloud-1317712784");
        config.setRegionId("ap-guangzhou");
        IOssClientService ossClientService = OSSFactory.createClient(OssTypeEnum.TENCENT, config);

        String fileName = "C:\\Users\\cyh\\Pictures\\desktopImg\\微信图片_20230802205217.png";
        String objectName = IdGenerator.nextSid() + ".png";
        FileInputStream fileInputStream = new FileInputStream(fileName);
        OssObjectVO objectVO = ossClientService.upload(objectName, fileInputStream);
        if (objectVO != null) {
            log.debug("访问:{}", objectVO.getObjectViewUrl());
        }
    }


    @Test
    public void testQiniu() throws Exception {
        QiniuOssConfig config = new QiniuOssConfig();
        config.setAccessKeyId("***************************");
        config.setAccessKeySecret("***************************-Xq780tc47");
        config.setBucketName("zerosx-cloud");
        config.setRegionId("");
        config.setDomainAddress("http://s0nc9ljee.hn-bkt.clouddn.com");
        IOssClientService ossClientService = OSSFactory.createClient(OssTypeEnum.QINIU, config);

        String fileName = "C:\\Users\\cyh\\Pictures\\desktopImg\\微信图片_20230802205217.png";
        String objectName = IdGenerator.nextSid() + ".png";
        FileInputStream fileInputStream = new FileInputStream(fileName);
        OssObjectVO objectVO = ossClientService.upload(objectName, fileInputStream);
        if (objectVO != null) {
            log.debug("访问:{}", objectVO.getObjectViewUrl());
        }
    }
}
