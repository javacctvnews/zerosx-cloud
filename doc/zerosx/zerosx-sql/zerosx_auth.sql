/*创建数据库*/
CREATE DATABASE `zerosx_auth` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
/*切换数据库*/
USE `zerosx_auth`;

DROP TABLE IF EXISTS `t_oauth_token_record`;
CREATE TABLE `t_oauth_token_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `token_value` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'token值',
  `apply_oauth_time` datetime(6) NOT NULL COMMENT '申请授权时间',
  `source_ip` varchar(50) NOT NULL COMMENT '登录IP',
  `source_location` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '登录地点',
  `browser_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器',
  `os_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作系统',
  `oauth_result` tinyint NOT NULL DEFAULT '1' COMMENT '授权结果，0：成功，1：失败',
  `oauth_msg` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '授权结果描述',
  `client_id` varchar(10) NOT NULL COMMENT '客户端类型',
  `grant_type` varchar(20) NOT NULL COMMENT '授权模式',
  `request_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请ID',
  `create_time` datetime(6) NOT NULL COMMENT '创建时间',
  `update_time` datetime(6) DEFAULT NULL COMMENT '更新时间',
  `operator_id` varchar(100) DEFAULT NULL COMMENT '运营商id',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除，0：未删除；1：已删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `idx_tenant` (`operator_id`) USING BTREE,
  KEY `idx_deleted` (`deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='授权记录';


DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `branch_id` bigint NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb3;


DROP TABLE IF EXISTS `oauth2_authorization`;
CREATE TABLE `oauth2_authorization` (
    `id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `registered_client_id` varchar(100) NOT NULL,
    `principal_name` varchar(200) NOT NULL,
    `authorization_grant_type` varchar(100) NOT NULL,
    `authorized_scopes` varchar(1000) DEFAULT NULL,
    `attributes` blob,
    `state` varchar(500) DEFAULT NULL,
    `authorization_code_value` blob,
    `authorization_code_issued_at` timestamp NULL DEFAULT NULL,
    `authorization_code_expires_at` timestamp NULL DEFAULT NULL,
    `authorization_code_metadata` blob,
    `access_token_value` blob,
    `access_token_issued_at` timestamp NULL DEFAULT NULL,
    `access_token_expires_at` timestamp NULL DEFAULT NULL,
    `access_token_metadata` blob,
    `access_token_type` varchar(100) DEFAULT NULL,
    `access_token_scopes` varchar(1000) DEFAULT NULL,
    `oidc_id_token_value` blob,
    `oidc_id_token_issued_at` timestamp NULL DEFAULT NULL,
    `oidc_id_token_expires_at` timestamp NULL DEFAULT NULL,
    `oidc_id_token_metadata` blob,
    `refresh_token_value` blob,
    `refresh_token_issued_at` timestamp NULL DEFAULT NULL,
    `refresh_token_expires_at` timestamp NULL DEFAULT NULL,
    `refresh_token_metadata` blob,
    `user_code_value` blob,
    `user_code_issued_at` timestamp NULL DEFAULT NULL,
    `user_code_expires_at` timestamp NULL DEFAULT NULL,
    `user_code_metadata` blob,
    `device_code_value` blob,
    `device_code_issued_at` timestamp NULL DEFAULT NULL,
    `device_code_expires_at` timestamp NULL DEFAULT NULL,
    `device_code_metadata` blob,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `oauth2_authorization_consent`;
CREATE TABLE `oauth2_authorization_consent` (
    `registered_client_id` varchar(100) NOT NULL,
    `principal_name` varchar(200) NOT NULL,
    `authorities` varchar(1000) NOT NULL,
    PRIMARY KEY (`registered_client_id`,`principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `oauth2_registered_client`;
CREATE TABLE `oauth2_registered_client` (
    `id` varchar(100) NOT NULL,
    `client_id` varchar(100) NOT NULL,
    `client_id_issued_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `client_secret` varchar(200) DEFAULT NULL,
    `client_secret_expires_at` timestamp NULL DEFAULT NULL,
    `client_name` varchar(200) NOT NULL,
    `client_authentication_methods` varchar(1000) NOT NULL,
    `authorization_grant_types` varchar(1000) NOT NULL,
    `redirect_uris` varchar(1000) DEFAULT NULL,
    `post_logout_redirect_uris` varchar(1000) DEFAULT NULL,
    `scopes` varchar(1000) NOT NULL,
    `client_settings` varchar(2000) NOT NULL,
    `token_settings` varchar(2000) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO zerosx_sas.oauth2_registered_client (id,client_id,client_id_issued_at,client_secret,client_secret_expires_at,client_name,client_authentication_methods,authorization_grant_types,redirect_uris,post_logout_redirect_uris,scopes,client_settings,token_settings) VALUES
('saas','saas','2023-11-08 19:06:26','{bcrypt}$2a$10$4LCzbpUMFJT.gaCyEEy9quvkLUPKsXoMsADlzN8QUaTXNSkjRKxei','2030-11-30 00:00:00','saas','client_secret_post,client_secret_jwt,client_secret_basic','refresh_token,client_credentials,password,urn:ietf:params:oauth:grant-type:device_code,authorization_code,sms,captcha_pwd,urn:ietf:params:oauth:grant-type:jwt-bearer','http://127.0.0.1:80','','phone,openid,profile,email','{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":true,"settings.client.require-authorization-consent":false}','{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",7200.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"reference"},"settings.token.refresh-token-time-to-live":["java.time.Duration",14400.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",7200.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",7200.000000000]}'),
('zerosx','zerosx','2023-11-08 15:46:43','{bcrypt}$2a$10$9X2XNoUgbp6gX2iMzC.Nzu7rPqpuULSll4VveajejCSW871sPeuxy','2023-11-30 00:00:00','zerosx','client_secret_post,client_secret_jwt,client_secret_basic','refresh_token,password,client_credentials,urn:ietf:params:oauth:grant-type:device_code,authorization_code,sms,captcha_pwd,urn:ietf:params:oauth:grant-type:jwt-bearer','http://127.0.0.1:8080','','phone,openid,profile,email','{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}','{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",7200.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"reference"},"settings.token.refresh-token-time-to-live":["java.time.Duration",14400.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",7200.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",7200.000000000]}');
