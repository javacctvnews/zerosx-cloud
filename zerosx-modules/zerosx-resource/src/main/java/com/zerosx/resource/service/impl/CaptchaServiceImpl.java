package com.zerosx.resource.service.impl;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.core.utils.IdGenerator;
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

    @Autowired
    private RedissonOpService redissonOpService;

    @Override
    public AuthCaptchaVO createCaptcha() {
        AuthCaptchaVO vo = new AuthCaptchaVO();
        // 保存验证码信息的key
        String uuid = IdGenerator.getIdStr();
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
        redissonOpService.set(ZCache.CAPTCHA.key(uuid), imgCode, ZCache.CAPTCHA_EXPIRE);
        return vo;
    }

}
