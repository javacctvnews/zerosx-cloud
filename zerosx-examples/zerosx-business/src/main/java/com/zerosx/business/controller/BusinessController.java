package com.zerosx.business.controller;

import com.zerosx.api.examples.dto.BusinessDTO;
import com.zerosx.business.service.IBusinessService;
import com.zerosx.common.base.utils.JacksonUtil;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.sms.model.SmsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class BusinessController {

    @Autowired
    private IBusinessService businessService;

    @PostMapping("/send1")
    public ResultVO<?> send1(@RequestBody BusinessDTO businessDTO) throws Exception {

        return ResultVOUtil.success();
    }


    @PostMapping("/send")
    public ResultVO<?> send(@RequestBody BusinessDTO businessDTO) throws Exception {

        return ResultVOUtil.success();
    }


    /**
     * 模拟用户购买商品下单业务逻辑流程
     *
     * @Param:
     * @Return:
     */
    @PostMapping("/buy")
    public ResultVO<?> handleBusiness(@RequestBody BusinessDTO businessDTO) {
        log.info("请求参数：{}", businessDTO.toString());
        return ResultVOUtil.success(businessService.handleBusiness(businessDTO));
    }

    /*public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", IdGenerator.getRandomStr(6));
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setOperatorId("000000");
        smsRequest.setPhoneNumbers("13693435776");
        smsRequest.setSignature("zerosx");
        smsRequest.setTemplateCode("SMS_462805087");
        smsRequest.setTemplateParam(JacksonUtil.toJSONString(map));
        String jsonString = JacksonUtil.toJSONString(smsRequest);
        System.out.println(jsonString);
    }*/

}
