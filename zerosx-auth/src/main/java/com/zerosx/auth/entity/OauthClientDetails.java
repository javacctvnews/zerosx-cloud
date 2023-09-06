package com.zerosx.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description
 * 客户端管理表
 * @Author javacctvnews
 * @Date 2023/3/27 12:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "oauth_client_details")
public class OauthClientDetails extends Model<OauthClientDetails> implements Serializable {

    private static final long serialVersionUID = -5515916556887167589L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    private String clientId;
    /**
     * 应用名称
     */
    private String clientName;
    private String resourceIds = "";
    private String clientSecret;
    private String clientSecretStr;
    private String scope = "all";
    private String authorizedGrantTypes = "authorization_code,password,refresh_token,client_credentials";
    private String webServerRedirectUri;
    private String authorities = "";
    @TableField(value = "access_token_validity")
    private Integer accessTokenValiditySeconds = 18000;
    @TableField(value = "refresh_token_validity")
    private Integer refreshTokenValiditySeconds = 28800;
    private String additionalInformation = "{}";
    private String autoapprove = "true";

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 状态，0：正常，1：停用
     */
    private String status;

    /**
     * 逻辑删除，0：未删除，1：已删除
     */
    @TableLogic
    private Integer deleted;
}
