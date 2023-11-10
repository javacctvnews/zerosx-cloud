package com.zerosx.sas.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.zerosx.api.resource.ISmsSupplierClient;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.core.enums.RedisKeyNameEnum;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.sas.service.IVerificationCodeService;
import com.zerosx.sas.vo.AuthCaptchaVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * @ClassName
 * @Description 验证码
 * @Author javacctvnews
 * @Date 2023/3/26 21:46
 * @Version 1.0
 */
@Service
@Slf4j
public class VerificationCodeServiceImpl implements IVerificationCodeService {

    /**
     * 图形验证码默认过期时间，单位：秒
     */
    private static final Long DEFAULT_IMAGE_EXPIRE = 300L;
    /**
     * 短信验证码300秒过期
     */
    private static final Long DEFAULT_SMS_EXPIRE = 300L;

    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private ISmsSupplierClient smsSupplierControllerApi;

    @Override
    public AuthCaptchaVO createCode() {
        AuthCaptchaVO vo = new AuthCaptchaVO();
        // 保存验证码信息的key
        String uuid = IdUtil.simpleUUID();
        // 三个参数分别为宽、高、位数
        SpecCaptcha gifCaptcha = new SpecCaptcha(110, 38, 5);
        gifCaptcha.setCharType(3);
        // 设置类型：字母数字混合TYPE_NUM_AND_UPPER
        gifCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        String imgCode = gifCaptcha.text().toLowerCase(Locale.ROOT);//图片字符
        vo.setCaptchaEnabled(true);
        vo.setUuid(uuid);
        vo.setImg(gifCaptcha.toBase64());//图片
        log.debug("UUID:{} 验证码:{}", uuid, imgCode);
        // 保存验证码
        this.saveImageCode(uuid, imgCode);
        return vo;
    }

    /**
     * 缓存图形验证码，120秒有效
     *
     * @param requestId
     * @param imgCode
     */
    @Override
    public void saveImageCode(String requestId, String imgCode) {
        redissonOpService.set(RedisKeyNameEnum.key(RedisKeyNameEnum.IMAGE_CODE, requestId), imgCode, DEFAULT_IMAGE_EXPIRE);
    }

    @Override
    public void validateImgCode(String requestId, String validCode) {
        if (StrUtil.isBlank(requestId)) {
            throw new BusinessException("请在请求参数中携带deviceId参数");
        }
        if (StrUtil.isBlank(validCode)) {
            throw new BusinessException("请填写验证码");
        }
        String code = redissonOpService.get(RedisKeyNameEnum.key(RedisKeyNameEnum.IMAGE_CODE, requestId));
        if (StringUtils.isBlank(code)) {
            throw new BusinessException("验证码不存在或已过期");
        }
        if (!StrUtil.equals(code, validCode.toLowerCase())) {
            throw new BusinessException("验证码不正确");
        }
        //删除验证码
        redissonOpService.del(RedisKeyNameEnum.key(RedisKeyNameEnum.IMAGE_CODE, requestId));
    }

    @Override
    public boolean checkSmsCode(String mobilePhone, String smsCode) {
        if (StringUtils.isBlank(mobilePhone) || StringUtils.isBlank(smsCode)) {
            return false;
        }
        String cacheCode = redissonOpService.get(RedisKeyNameEnum.key(RedisKeyNameEnum.SMS_CODE, mobilePhone));
        if (StringUtils.isBlank(cacheCode)) {
            throw new BusinessException("验证码不存在或已过期");
        }
        if (!smsCode.equals(cacheCode.toLowerCase())) {
            throw new BusinessException("验证码不正确");
        }
        return true;
    }



    /*public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        String randomStr = IdGenerator.getRandomStr(6);
        map.put("code", randomStr);
        //发送短信
        SmsSendDTO smsSendDTO = new SmsSendDTO();
        smsSendDTO.setOperatorId("000000");
        smsSendDTO.setBusinessCode(SmsBusinessCodeEnum.VERIFY_CODE.getCode());
        smsSendDTO.setPhoneNumbers("13693435776");
        smsSendDTO.setParams(map);
        String jsonString = JacksonUtil.toJSONString(smsSendDTO);
        System.out.println(jsonString);
    }*/
}
