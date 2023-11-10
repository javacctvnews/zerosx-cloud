package com.zerosx.sas.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.Instant;

/***
 * @Description
 * 客户端管理表
 * @Author javacctvnews
 * @Date 2023/3/27 12:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "oauth2_registered_client")
public class Oauth2RegisteredClient extends Model<Oauth2RegisteredClient> implements Serializable {

    private static final long serialVersionUID = -5515916556887167589L;

    private String id;
    private String clientId;
    private Instant clientIdIssuedAt;
    private String clientSecret;
    private Instant clientSecretExpiresAt;
    private String clientName;
    private String clientAuthenticationMethods;
    private String authorizationGrantTypes;
    private String redirectUris;
    private String postLogoutRedirectUris;
    private String scopes;
    private String clientSettings;
    private String tokenSettings;

    /*@TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    *//**
     * 状态，0：正常，1：停用
     *//*
    private String status;

    *//**
     * 逻辑删除，0：未删除，1：已删除
     *//*
    @TableLogic
    private Integer deleted;*/
}
