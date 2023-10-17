package com.zerosx.api.resource;

import com.zerosx.api.resource.factory.OssFileUploadClientFallback;
import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "IOssFileUploadClient", name = ServiceIdConstants.RESOURCE, fallbackFactory = OssFileUploadClientFallback.class)
public interface IOssFileUploadClient {

    /**
     * 文件URL获取(单个)
     *
     * @param objectName
     * @return
     */
    @PostMapping(value = "/view_url")
    ResultVO<String> getObjectViewUrl(@RequestParam("objectName") String objectName);

}
