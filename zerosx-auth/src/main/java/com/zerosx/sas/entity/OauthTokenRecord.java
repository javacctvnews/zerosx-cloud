package com.zerosx.sas.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zerosx.common.core.model.SuperEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 登录日志
 *
 * @author javacctvnews
 * @Description
 * @date 2023-04-09 15:33:32
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("t_oauth_token_record")
public class OauthTokenRecord extends SuperEntity<OauthTokenRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;

    /**
     * token值
     */
    private String tokenValue;

    /**
     * 申请授权时间
     */
    private Date applyOauthTime;

    /**
     * 登录IP
     */
    private String sourceIp;

    /**
     * 登录地点
     */
    private String sourceLocation;

    /**
     * 浏览器
     */
    private String browserType;

    /**
     * 操作系统
     */
    private String osType;

    /**
     * 授权结果，0：成功，1：失败
     */
    private Integer oauthResult;

    /**
     * 授权结果描述
     */
    private String oauthMsg;

    /**
     * 客户端类型
     */
    private String clientId;

    /**
     * 授权模式
     */
    private String grantType;

    /**
     * 申请ID
     */
    private String requestId;

    //租户ID
    private String operatorId;

    /**
     * 逻辑删除，0：未删除；1：已删除
     */
    @TableLogic
    private Integer deleted;

}
