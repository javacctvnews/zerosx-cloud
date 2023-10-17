package com.zerosx.api.resource.factory;

import com.zerosx.api.resource.IOssFileUploadClient;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * OssFileUploadFallback
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-24 17:33
 **/
@Slf4j
@Component
public class OssFileUploadClientFallback implements FallbackFactory<IOssFileUploadClient> {

    @Override
    public IOssFileUploadClient create(Throwable cause) {
        return new IOssFileUploadClient() {
            @Override
            public ResultVO<String> getObjectViewUrl(String objectName) {
                log.error(cause.getMessage(), cause);
                return ResultVOUtil.feignFail(StringUtils.EMPTY);
            }
        };
    }
}
