package com.zerosx.common.core.utils;

import com.zerosx.api.resource.IDGenClient;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.enums.BizTagEnum;
import com.zerosx.common.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * LeafUtils
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-12-05 17:18
 **/
@Slf4j
public class LeafUtils {

    private static final IDGenClient idGenClient;

    static {
        idGenClient = SpringUtils.getBean(IDGenClient.class);
    }

    /**
     * 分布式ID-String 通用(默认标识)
     *
     * @return 分布式ID-String
     */
    public static String idString() {
        return String.valueOf(uid(BizTagEnum.DEFAULT_BIZ.getCode()));
    }

    /**
     * 分布式ID-String
     *
     * @param bizTagEnum 业务标识
     * @return 分布式ID-String
     */
    public static String uidStr(BizTagEnum bizTagEnum) {
        return String.valueOf(uid(bizTagEnum.getCode()));
    }

    public static Long uid(BizTagEnum bizTagEnum) {
        return uid(bizTagEnum.getCode());
    }

    /**
     * 分布式ID-Long
     *
     * @param bizTag 业务标识
     * @return 分布式ID-Long
     */
    public static Long uid(String bizTag) {
        if (StringUtils.isBlank(bizTag)) {
            throw new BusinessException("分布式ID的业务标识不能为空");
        }
        //long t1 = System.currentTimeMillis();
        ResultVO<Long> segmentRes = idGenClient.segment(bizTag);
        //log.debug("分布式ID:{} 耗时{}ms", segmentRes.getData(), System.currentTimeMillis() - t1);
        segmentRes.checkException();
        //log.debug("业务标识:【{}】:{}", bizTag, segmentRes.getData());
        return segmentRes.getData();
    }

}
