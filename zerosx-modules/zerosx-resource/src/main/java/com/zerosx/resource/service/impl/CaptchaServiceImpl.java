package com.zerosx.resource.service.impl;

import cn.hutool.core.util.IdUtil;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.zerosx.common.core.enums.RedisKeyNameEnum;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.resource.service.ICaptchaService;
import com.zerosx.resource.vo.AuthCaptchaVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * CaptchaServiceImpl
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-11-14 14:33
 **/
@Service
@Slf4j
public class CaptchaServiceImpl implements ICaptchaService {

    /**
     * 图形验证码默认过期时间，单位：秒
     */
    private static final Long DEFAULT_IMAGE_EXPIRE = 300L;

    @Autowired
    private RedissonOpService redissonOpService;

    @Override
    public AuthCaptchaVO createCaptcha() {
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
        redissonOpService.set(RedisKeyNameEnum.key(RedisKeyNameEnum.IMAGE_CODE, uuid), imgCode, DEFAULT_IMAGE_EXPIRE);
        return vo;
    }

}
