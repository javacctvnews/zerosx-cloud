/*创建数据库*/
CREATE DATABASE `zerosx_resource` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
/*切换数据库*/
USE `zerosx_resource`;

DROP TABLE IF EXISTS `t_area_city_source`;
CREATE TABLE `t_area_city_source` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '城市编号',
  `area_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '城市编号',
  `deep` tinyint NOT NULL COMMENT '层级深度；0：省，1：市，2：区，3：镇',
  `parent_area_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '上级编号',
  `ext_id` varchar(20) NOT NULL COMMENT '数据源原始的编号',
  `area_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '城市完整名称',
  `create_time` datetime(6) NOT NULL COMMENT '创建时间',
  `update_time` datetime(6) NOT NULL COMMENT '最新更新时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='行政区域数据源';


DROP TABLE IF EXISTS `t_sys_dict_type`;
CREATE TABLE `t_sys_dict_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典类型名称',
  `dict_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典类型编码',
  `dict_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `remarks` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime(6) NOT NULL COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(6) NOT NULL COMMENT '修改时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除，0：未删除；1：删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=COMPACT COMMENT='字典类型表';


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


DROP TABLE IF EXISTS `t_sys_dict_data`;
CREATE TABLE `t_sys_dict_data` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `dict_sort` int DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典标签',
  `dict_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典键值',
  `dict_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典类型:关联sys_dict_type',
  `is_default` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_time` datetime(6) NOT NULL COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(6) NOT NULL COMMENT '修改时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
  `remarks` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `css_class` varchar(20) DEFAULT NULL COMMENT '样式属性',
  `list_class` varchar(20) DEFAULT NULL COMMENT '回显样式',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除，0：未删除，1：删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `dict_type` (`dict_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=COMPACT COMMENT='字典数据表';


DROP TABLE IF EXISTS `t_sys_param`;
CREATE TABLE `t_sys_param` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `param_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '参数名',
  `param_key` varchar(50) NOT NULL COMMENT '参数编码',
  `param_value` varchar(100) NOT NULL COMMENT '参数值',
  `param_scope` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '参数范围，0：全局，1：运营商',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态，0：正常，1：停用',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注说明',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(20) DEFAULT NULL COMMENT '更新人',
  `operator_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '运营商id',
  `deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除，0：未删除，1：删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统参数';


DROP TABLE IF EXISTS `t_sms_supplier_business`;
CREATE TABLE `t_sms_supplier_business` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `sms_supplier_id` bigint NOT NULL COMMENT '服务商ID',
  `business_code` varchar(30) NOT NULL COMMENT '短信业务编码',
  `template_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '短信模板编号',
  `template_content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '模板内容',
  `signature` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '模板签名',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态，0：正常；1：停用',
  `create_time` datetime(6) NOT NULL COMMENT '创建时间',
  `create_by` varchar(30) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(6) DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  `remarks` varchar(100) DEFAULT NULL COMMENT '备注',
  `operator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户标识',
  `deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除，0：未删除；1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='短信业务模板';


DROP TABLE IF EXISTS `t_sms_supplier`;
CREATE TABLE `t_sms_supplier` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `supplier_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '服务商编码',
  `supplier_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '服务商名称',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态，0：正常；1：停用',
  `access_key_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Access Key',
  `access_key_secret` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'accessKeySecret',
  `signature` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '短信签名',
  `region_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'regionId',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(6) NOT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(6) NOT NULL COMMENT '更新时间',
  `operator_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户标识',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  `domain_address` varchar(100) DEFAULT NULL COMMENT 'SMS地址',
  `key_value` varchar(100) DEFAULT NULL COMMENT 'key值',
  `deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除，0：未删除；1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='SMS配置';


DROP TABLE IF EXISTS `t_oss_supplier`;
CREATE TABLE `t_oss_supplier` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `supplier_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '服务商编码',
  `supplier_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '服务商名称',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态，0：正常；1：停用',
  `access_key_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'AccessKey',
  `access_key_secret` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'AccessSecret',
  `bucket_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '存储桶名称',
  `region_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属地域',
  `endpoint` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'endpoint',
  `domain_address` varchar(100) DEFAULT NULL COMMENT '域名',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(6) NOT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(6) NOT NULL COMMENT '更新时间',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  `operator_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户标识',
  `deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除，0：未删除；1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='OSS配置';

DROP TABLE IF EXISTS `t_oss_file_upload`;
CREATE TABLE `t_oss_file_upload` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `oss_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'oss厂商编码',
  `original_file_name` varchar(1000) NOT NULL COMMENT '原文件名称',
  `object_name` varchar(200) NOT NULL COMMENT '文件名的key',
  `object_url` varchar(1000) DEFAULT NULL COMMENT '文件查看路径',
  `object_view_url` varchar(1000) DEFAULT NULL COMMENT '文件访问URL',
  `expiration_time` datetime(6) DEFAULT NULL COMMENT '失效时刻',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `oss_supplier_id` bigint DEFAULT NULL COMMENT 'oss服务商ID',
  `bucket_name` varchar(100) NOT NULL COMMENT '存储桶名称',
  `access_key_id` varchar(100) NOT NULL COMMENT 'accessKeyId',
  `object_size` bigint DEFAULT NULL COMMENT '文件大小',
  `operator_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '运营商id',
  `create_time` datetime(6) NOT NULL COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(6) NOT NULL COMMENT '更新时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_UN` (`object_name`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件上传记录';

/*系统参数*/
INSERT INTO zerosx_resource.t_sys_param (param_name,param_key,param_value,param_scope,status,remark,create_time,create_by,update_time,update_by,operator_id,deleted) VALUES
('最大日期查询范围','query_date_scope','180','0','0','限制最大的日期查询范围，默认无限制','2023-08-02 12:05:53','admin123','2023-08-19 17:30:04','admin123',NULL,0),
('knif4j接口地址','openapi3-url','http://localhost:9100/doc.html','0','0','基于openapi3的knif4j内部接口地址:https://apifox.com/apidoc/shared-34924519-6dc9-4845-b60d-a12316ad6eb8','2023-08-11 11:48:16','admin123','2023-08-13 09:57:15','admin123',NULL,0),
('系统监控地址','monitor-url','http://127.0.0.1:19120/login','0','0','SpringBootAdmin系统监控地址http://localhost:19120/login','2023-08-12 10:40:31','admin123','2023-08-14 13:51:25','admin123',NULL,0);

INSERT INTO zerosx_resource.t_sys_dict_type (dict_name,dict_type,dict_status,remarks,create_time,create_by,update_time,update_by,deleted) VALUES
('资源标识','resource_ids','0','资源标识','2023-08-12 21:20:51.574000','admin123','2023-08-31 18:15:58.543000','admin123',0);
INSERT INTO zerosx_resource.t_sys_dict_data (dict_sort,dict_label,dict_value,dict_type,is_default,status,create_time,create_by,update_time,update_by,remarks,css_class,list_class,deleted) VALUES
(0,'系统服务','api-system','resource_ids','N','0','2023-08-12 21:21:45.412000','admin123','2023-08-12 21:21:45.412000','admin123','系统服务',NULL,'primary',0),
(0,'资源服务','api-resource','resource_ids','N','0','2023-08-12 21:21:45.412000','admin123','2023-08-12 21:21:45.412000','admin123','资源服务',NULL,'primary',0),
(0,'授权服务','api-auth','resource_ids','N','0','2023-08-12 21:22:05.948000','admin123','2023-08-12 21:22:05.948000','admin123','授权服务',NULL,'primary',0);
