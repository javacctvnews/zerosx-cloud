package com.zerosx.common.oss.core.client;

import com.zerosx.common.oss.model.OssObjectVO;
import com.zerosx.common.oss.core.config.IOssConfig;

import java.io.InputStream;

/**
 * oss操作类接口
 */
public interface IOssClientService {

    /**
     * 上传单个文件
     *
     * @param objectName
     * @param is
     * @return
     */
    OssObjectVO upload(String objectName, InputStream is);

    /**
     * 查询对象外网访问URL，即下载URL
     *
     * @param objectName
     * @return
     */
    OssObjectVO viewUrl(String objectName);

    /**
     * 删除文件
     *
     * @param objectName
     * @return
     */
    boolean delete(String objectName);

    /**
     * 设置或更新文件的生存时间，比如设置10天，则10天后会自动删除
     *
     * @param objectName
     * @param days
     * @return
     */
    boolean deleteAfterDays(String objectName, int days);

    IOssConfig getConfig();

}
