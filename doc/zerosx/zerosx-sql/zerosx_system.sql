/*创建数据库*/
CREATE DATABASE `zerosx_system` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
/*切换数据库*/
USE `zerosx_system`;

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


DROP TABLE IF EXISTS `t_sys_user_post`;
CREATE TABLE `t_sys_user_post` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户和岗位关联表';


DROP TABLE IF EXISTS `t_sys_menu`;
CREATE TABLE `t_sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query_param` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '显示状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方法',
  `request_url` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '请求url',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '备注',
  `deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除，0:未删除；1：已删除',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限表';


DROP TABLE IF EXISTS `t_sys_role_dept`;
CREATE TABLE `t_sys_role_dept` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和部门关联表';


DROP TABLE IF EXISTS `t_sys_post`;
CREATE TABLE `t_sys_post` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `operator_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户标识',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除，0：未删除，1：删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='岗位管理';


DROP TABLE IF EXISTS `t_sys_role_menu`;
CREATE TABLE `t_sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和菜单关联表';


DROP TABLE IF EXISTS `t_sys_user_role`;
CREATE TABLE `t_sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户和角色关联表';


DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户账号',
  `user_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户内部编码',
  `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'saas_operator' COMMENT '用户类型（saas_operator租户管理员）',
  `email` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '用户邮箱',
  `phone_number` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `operator_id` varchar(20) NOT NULL COMMENT '租户标识',
  `deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UN_userName` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';


DROP TABLE IF EXISTS `t_system_operator_log`;
CREATE TABLE `t_system_operator_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `btn_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '模块按钮名称',
  `business_type` int DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method_name` varchar(100) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `operator_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `operator_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '主机地址',
  `ip_location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'ip归属地',
  `operator_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `operator_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `operator_time` datetime(6) DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint DEFAULT '0' COMMENT '消耗时间',
  `create_time` datetime(6) NOT NULL COMMENT '创建时间',
  `update_time` datetime(6) NOT NULL COMMENT '最新更新时间',
  `operator_id` varchar(100) DEFAULT NULL COMMENT '运营商id',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除，0：未删除；1：已删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `idx_tenant` (`operator_id`) USING BTREE,
  KEY `idx_deleted_otime` (`deleted`,`operator_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志记录';


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


DROP TABLE IF EXISTS `t_sys_dept`;
CREATE TABLE `t_sys_dept` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '部门名称',
  `dept_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门编码',
  `parent_id` bigint DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(500) DEFAULT '' COMMENT '祖级列表',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系电话',
  `email` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `operator_id` varchar(20) NOT NULL COMMENT '租户标识',
  `deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表';


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


DROP TABLE IF EXISTS `t_sys_role`;
CREATE TABLE `t_sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `operator_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户标识',
  `deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色信息表';


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


DROP TABLE IF EXISTS `t_muti_tenancy_group`;
CREATE TABLE `t_muti_tenancy_group` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `operator_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户集团ID，系统唯一',
  `tenant_group_name` varchar(100) NOT NULL COMMENT '租户集团公司全称',
  `tenant_short_name` varchar(100) DEFAULT NULL COMMENT '租户集团公司简称',
  `social_credit_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '集团公司社会信用代码',
  `business_license_picture` varchar(200) DEFAULT NULL COMMENT '营业执照图片(存oss的key)',
  `valid_status` char(1) NOT NULL DEFAULT '0' COMMENT '使用状态，0：正常；1：停用',
  `audit_status` tinyint NOT NULL DEFAULT '0' COMMENT '审核状态，0：待审核；1：审核通过；2：审核未通过',
  `province` varchar(16) NOT NULL COMMENT '租户集团公司所在省',
  `city` varchar(16) NOT NULL COMMENT '集团公司所在市',
  `area` varchar(16) DEFAULT NULL COMMENT '集团公司所在区',
  `street` varchar(100) NOT NULL COMMENT '集团公司详细街道地址',
  `contact_name` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系人姓名',
  `contact_mobile_phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系人电话',
  `telephone` varchar(100) DEFAULT NULL COMMENT '公司400号码',
  `log_picture` varchar(100) DEFAULT NULL COMMENT '集团公司logo地址(存oss的key)',
  `remarks` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_time` datetime(6) NOT NULL COMMENT '创建时间',
  `update_time` datetime(6) NOT NULL COMMENT '更新时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除，0：未删除；1：删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UN` (`operator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='多租户集体公司';


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


/* 系统菜单初始数据 */
INSERT INTO zerosx_system.t_sys_menu (menu_id,menu_name,parent_id,order_num,`path`,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,request_method,request_url,remark,deleted) VALUES
	 (1,'系统管理',0,3,'system',NULL,'',1,0,'M','0','0','','system','admin','2023-07-19 15:53:55','admin','2023-07-28 11:39:43','','','系统管理目录',0),
	 (4,'Zeros官网',0,100,'https://www.zeroeev.com',NULL,'',0,0,'M','0','0','','bug','admin','2023-07-19 15:53:55','admin','2023-07-30 23:50:06','','','RuoYi-Cloud-Plus官网地址',0),
	 (102,'菜单管理',1,3,'menu','system/menu/index','',1,0,'C','0','0','system:menu:list','tree-table','admin','2023-07-19 15:53:55','',NULL,'','','菜单管理菜单',0),
	 (105,'字典管理',1,6,'dict','system/dict/index','',1,0,'C','0','0','system:dict:list','dict','admin','2023-07-19 15:53:55','',NULL,'','','字典管理菜单',0),
	 (106,'参数设置',1,7,'sysParam','system/sysParam/index','',1,0,'C','0','0','system:sysParam:list','edit','admin','2023-07-19 15:53:55','admin','2023-07-29 00:51:21','','','参数设置菜单',0),
	 (108,'日志管理',1,9,'log','','',1,0,'M','0','0','','log','admin','2023-07-19 15:53:55','',NULL,'','','日志管理菜单',0),
	 (500,'操作日志',108,1,'systemOperatorLog','system/systemOperatorLog/index','',1,0,'C','0','0','system:systemOperatorLog:list','form','admin','2023-07-19 15:53:55','admin','2023-07-27 12:48:56','','','操作日志菜单',0),
	 (501,'登录日志',108,2,'oauthTokenRecord','system/oauthTokenRecord/index','',1,0,'C','0','0','system:oauthTokenRecord:list','logininfor','admin','2023-07-19 15:53:55','admin123','2023-07-31 12:04:46','','','登录日志菜单',0),
	 (1013,'菜单查询',102,1,'','','',1,0,'F','0','0','system:menu:query','#','admin','2023-07-19 15:53:56','admin123','2023-07-31 12:09:36','post','/api-system/menu/list','',0),
	 (1014,'菜单新增',102,2,'','','',1,0,'F','0','0','system:menu:add','#','admin','2023-07-19 15:53:56','',NULL,'','','',0);
INSERT INTO zerosx_system.t_sys_menu (menu_id,menu_name,parent_id,order_num,`path`,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,request_method,request_url,remark,deleted) VALUES
	 (1015,'菜单修改',102,3,'','','',1,0,'F','0','0','system:menu:edit','#','admin','2023-07-19 15:53:56','',NULL,'','','',0),
	 (1016,'菜单删除',102,4,'','','',1,0,'F','0','0','system:menu:remove','#','admin','2023-07-19 15:53:56','',NULL,'','','',0),
	 (1610,'租户管理',0,0,'tenant',NULL,NULL,1,0,'M','0','0',NULL,'international','','2023-07-24 13:10:54','','2023-07-24 13:35:25','','','',0),
	 (1611,'租户管理',1610,1,'/tenant/multiTenant/index','tenant/multiTenant/index',NULL,1,0,'C','0','0','tenant:tenant:pageList','peoples','','2023-07-24 13:12:47','admin123','2023-07-31 11:36:04','','','',0),
	 (1612,'行政区域',1613,2,'area','resource/area/index',NULL,1,0,'C','0','0','resource:area:list','drag','','2023-07-24 22:47:10','admin123','2023-08-14 13:45:56','','','',0),
	 (1613,'资源管理',0,5,'resource',NULL,NULL,1,0,'M','0','0',NULL,'documentation','','2023-07-25 23:57:47','admin123','2023-09-06 23:55:26','','','',0),
	 (1614,'文件管理',1613,1,'ossFileUpload','resource/ossFileUpload/index',NULL,1,0,'C','1','0','resource:ossFileUpload:list','clipboard','','2023-07-25 23:59:07','admin123','2023-09-09 17:53:08','','','',0),
	 (1616,'角色管理',1617,2,'sysRole','perms/sysRole/index',NULL,1,0,'C','0','0','perms:sysRole:list','lock','admin','2023-07-27 17:40:18','admin123','2023-08-19 00:29:21','','','',0),
	 (1617,'权限管理',0,1,'perms',NULL,NULL,1,0,'M','0','0',NULL,'tree-table','admin','2023-07-28 11:38:54','admin','2023-07-28 11:39:54','','','',0),
	 (1619,'岗位管理',1617,8,'sysPost','perms/sysPost/index',NULL,1,0,'C','0','0','perms:sysPost:list','post','admin','2023-07-28 13:54:18','admin123','2023-07-31 14:45:20','','','',0);
INSERT INTO zerosx_system.t_sys_menu (menu_id,menu_name,parent_id,order_num,`path`,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,request_method,request_url,remark,deleted) VALUES
	 (1620,'部门管理',1617,3,'sysDept','perms/sysDept/index',NULL,1,0,'C','0','0','perms:sysDept:list','tree','admin','2023-07-28 16:05:22','','2023-07-28 16:05:22','','','',0),
	 (1621,'用户管理',1617,1,'sysUser','perms/sysUser/index',NULL,1,0,'C','0','0','perms:sysUser:list','peoples','admin','2023-07-28 16:21:33','','2023-07-28 16:21:33','','','',0),
	 (1622,'列表查询',1611,1,'',NULL,NULL,1,0,'F','0','0','tenant:tenant:pageList','#','admin','2023-07-30 17:20:10','','2023-07-30 17:20:10','post','/api-system/muti_tenancy/list_pages','',0),
	 (1623,'新增',1611,2,'',NULL,NULL,1,0,'F','0','0','tenant:tenant:add','#','admin','2023-07-30 17:20:55','','2023-07-30 17:20:55','post','/api-system/muti_tenancy/save','',0),
	 (1624,'编辑',1611,3,'',NULL,NULL,1,0,'F','0','0','tenant:tenant:update','#','admin','2023-07-30 19:27:47','','2023-07-30 19:27:47','post','/api-system/muti_tenancy/update','',0),
	 (1625,'删除',1611,4,'',NULL,NULL,1,0,'F','0','0','tenant:tenant:deleted','#','admin','2023-07-30 19:29:17','','2023-07-30 19:29:17','delete','/api-system/muti_tenancy/deleted/{ids}','',0),
	 (1626,'导出',1611,5,'',NULL,NULL,1,0,'F','0','0','tenant:tenant:export','#','admin','2023-07-30 19:30:18','','2023-07-30 19:30:18','post','/api-system/muti_tenancy/export','',0),
	 (1627,'列表查询',1621,1,'',NULL,NULL,1,0,'F','0','0','perms:sysuser:list','#','admin','2023-07-30 19:34:01','','2023-07-30 19:34:01','post','/api-system/sys_user/page_list','',0),
	 (1628,'新增',1621,2,'',NULL,NULL,1,0,'F','0','0','perms:sysuser:add','#','admin','2023-07-30 19:35:49','','2023-07-30 19:35:49','post','/api-system/sys_user/save','',0),
	 (1629,'编辑',1621,3,'',NULL,NULL,1,0,'F','0','0','perms:sysuser:update','#','admin','2023-07-30 19:36:34','','2023-07-30 19:36:34','post','/api-system/sys_user/update','',0);
INSERT INTO zerosx_system.t_sys_menu (menu_id,menu_name,parent_id,order_num,`path`,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,request_method,request_url,remark,deleted) VALUES
	 (1630,'删除',1621,4,'',NULL,NULL,1,0,'F','0','0','perms:sysuser:delete','#','admin','2023-07-30 19:37:47','','2023-07-30 19:37:47','delete','/api-system/sys_user/delete/{userId}','',0),
	 (1631,'导出',1621,5,'',NULL,NULL,1,0,'F','0','0','perms:sysuser:export','#','admin','2023-07-30 19:38:21','','2023-07-30 19:38:21','post','/api-system/sys_user/export','',0),
	 (1632,'列表查询',1619,1,'',NULL,NULL,1,0,'F','0','0','pems:syspost:list','#','admin','2023-07-30 20:07:23','admin','2023-07-30 21:59:13','post','/api-system/sys_post/page_list','',0),
	 (1633,'新增',1619,2,'',NULL,NULL,1,0,'F','0','0','perms:syspost:add','#','admin','2023-07-30 21:56:02','admin','2023-07-30 21:58:59','post','/api-system/sys_post/save','',0),
	 (1634,'编辑',1619,2,'',NULL,NULL,1,0,'F','0','0','perms:syspost:update','#','admin','2023-07-30 21:57:17','admin','2023-07-30 21:58:43','post','/api-system/sys_post/update','',0),
	 (1635,'删除',1619,4,'',NULL,NULL,1,0,'F','0','0','perms:syspost:deleted','#','admin','2023-07-30 21:58:24','','2023-07-30 21:58:24','delete','/api-system/sys_post/delete/{ids}','',0),
	 (1636,'列表查询',1616,1,'',NULL,NULL,1,0,'F','0','0','perms:sysrole:list','#','admin','2023-07-30 22:00:50','','2023-07-30 22:00:50','post','/api-system/sys_role/page_list','',0),
	 (1637,'新增',1616,2,'',NULL,NULL,1,0,'F','0','0','perms:sysrole:add','#','admin','2023-07-30 22:01:31','admin123','2023-08-19 15:31:41','post','/api-system/sys_role/save','',0),
	 (1638,'编辑',1616,3,'',NULL,NULL,1,0,'F','0','0','perms:sysrole:update','#','admin','2023-07-30 22:02:08','','2023-07-30 22:02:08','post','/api-system/sys_role/update','',0),
	 (1639,'删除',1616,4,'',NULL,NULL,1,0,'F','0','0','perms:sysrole:delete','#','admin','2023-07-30 22:04:10','admin','2023-07-30 22:04:54','delete','/api-system/sys_role/delete/{roleIds}','',0);
INSERT INTO zerosx_system.t_sys_menu (menu_id,menu_name,parent_id,order_num,`path`,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,request_method,request_url,remark,deleted) VALUES
	 (1640,'导出',1616,5,'',NULL,NULL,1,0,'F','0','0','perms:sysrole:export','#','admin','2023-07-30 22:05:34','','2023-07-30 22:05:34','post','/api-system/sys_role/export','',0),
	 (1641,'导出',1619,5,'',NULL,NULL,1,0,'F','0','0','perms:syspost:export','#','admin','2023-07-30 22:39:27','','2023-07-30 22:39:27','post','/api-system/sys_post/export','',0),
	 (1642,'列表查询',1620,1,'',NULL,NULL,1,0,'F','0','0','perms:sysdept:list','#','admin','2023-07-30 22:40:18','admin123','2023-07-31 10:32:02','post','/api-system/sys_dept/table_tree','',0),
	 (1643,'新增',1620,2,'',NULL,NULL,1,0,'F','0','0','perms:sysdept:add','#','admin','2023-07-30 22:42:26','','2023-07-30 22:42:26','post','/api-system/sys_dept/save','',0),
	 (1644,'编辑',1620,3,'',NULL,NULL,1,0,'F','0','0','perms:sysdept:update','#','admin','2023-07-30 22:43:37','','2023-07-30 22:43:37','post','/api-system/sys_dept/update','',0),
	 (1645,'删除',1620,4,'',NULL,NULL,1,0,'F','0','0','perms:sysdept:delete','#','admin','2023-07-30 22:44:25','','2023-07-30 22:44:25','delete','/api-system/sys_dept/delete/{ids}','',0),
	 (1646,'导出',1620,5,'',NULL,NULL,1,0,'F','0','0','perms:sysdept:export','#','admin','2023-07-30 22:46:08','','2023-07-30 22:46:08','post','/api-system/sys_dept/export','',0),
	 (1647,'树形列表查询',1612,1,'',NULL,NULL,1,0,'F','0','0','resource:area:list','#','admin','2023-07-30 22:50:47','admin','2023-07-30 22:52:22','get','/api-system/area_city_source/lazy_tree/{parentCode}','',0),
	 (1648,'列表查询',1614,1,'',NULL,NULL,1,0,'F','0','0','resource:ossfile:list','#','admin','2023-07-30 22:52:06','','2023-07-30 22:52:06','post','/api-system/oss_file/list_pages','',0),
	 (1649,'删除',1614,2,'',NULL,NULL,1,0,'F','0','0','resource:ossfile:delete','#','admin','2023-07-30 22:53:44','','2023-07-30 22:53:44','get','/api-system/oss_file/full_delete/{ids}','',0);
INSERT INTO zerosx_system.t_sys_menu (menu_id,menu_name,parent_id,order_num,`path`,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,request_method,request_url,remark,deleted) VALUES
	 (1650,'详情',1614,3,'',NULL,NULL,1,0,'F','0','0','resource:ossfile:detail','#','admin','2023-07-30 22:55:47','','2023-07-30 22:55:47','get','/api-system/oss-file/detail/{id}','',0),
	 (1651,'列表查询',105,1,'',NULL,NULL,1,0,'F','0','0','system:dict:list','#','admin','2023-07-30 23:03:23','','2023-07-30 23:03:23','post','/api-system/sysDictType_page','',0),
	 (1652,'新增',105,2,'',NULL,NULL,1,0,'F','0','0','system:dict:add','#','admin','2023-07-30 23:06:40','admin','2023-07-30 23:06:46','post','/api-system/sysDictType_insert','',0),
	 (1653,'编辑',105,3,'',NULL,NULL,1,0,'F','0','0','system:dict:update','#','admin','2023-07-30 23:07:29','','2023-07-30 23:07:29','put','/api-system/sysDictType_update','',0),
	 (1654,'删除',105,4,'',NULL,NULL,1,0,'F','0','0','system:dict:delete','#','admin','2023-07-30 23:08:16','','2023-07-30 23:08:16','delete','/api-system/sysDictType_delete/{dictId}','',0),
	 (1655,'导出',105,5,'',NULL,NULL,1,0,'F','0','0','system:dict:export','#','admin','2023-07-30 23:09:10','','2023-07-30 23:09:10','post','/api-system/sysDictType/export','',0),
	 (1656,'刷新缓存',105,6,'',NULL,NULL,1,0,'F','0','0','system:dict:initCache','#','admin','2023-07-30 23:10:52','','2023-07-30 23:10:52','get','/api-system/sysDictData/init','',0),
	 (1657,'字典数据',105,7,'',NULL,NULL,1,0,'F','0','0','system:dict:dictdata','#','admin','2023-07-30 23:20:51','','2023-07-30 23:20:51','post','/api-system/sysDictData_page','',0),
	 (1658,'新增',1657,1,'',NULL,NULL,1,0,'F','0','0','system:dict:dictdata:add','#','admin','2023-07-30 23:23:17','','2023-07-30 23:23:17','post','/api-system/sysDictData_insert','',0),
	 (1659,'编辑',1657,2,'',NULL,NULL,1,0,'F','0','0','system:dict:dictdata:update','#','admin','2023-07-30 23:24:09','','2023-07-30 23:24:09','put','/api-system/sysDictData_update','',0);
INSERT INTO zerosx_system.t_sys_menu (menu_id,menu_name,parent_id,order_num,`path`,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,request_method,request_url,remark,deleted) VALUES
	 (1660,'删除',1657,3,'',NULL,NULL,1,0,'F','0','0','system:dict:dictdata:delete','#','admin','2023-07-30 23:26:18','','2023-07-30 23:26:18','delete','/api-system/sysDictData_delete/{dictCode}','',0),
	 (1661,'导出',1657,4,'',NULL,NULL,1,0,'F','0','0','system:dict:dictdata:export','#','admin','2023-07-30 23:27:13','','2023-07-30 23:27:13','post','/api-system/sysDictData/export','',0),
	 (1662,'列表查询',500,1,'',NULL,NULL,1,0,'F','0','0','system:operatorlog:list','#','admin','2023-07-30 23:37:57','','2023-07-30 23:37:57','post','/api-system/system_operator_log/page_list','',0),
	 (1663,'删除',500,2,'',NULL,NULL,1,0,'F','0','0','system:operatorlog:delete','#','admin','2023-07-30 23:38:43','','2023-07-30 23:38:43','delete','/api-system/system_operator_log/delete/{id}','',0),
	 (1664,'清空',500,3,'',NULL,NULL,1,0,'F','0','0','system:operatorlog:clean','#','admin','2023-07-30 23:40:11','','2023-07-30 23:40:11','post','/api-system/system_operator_log/clean','',0),
	 (1665,'导出',500,4,'',NULL,NULL,1,0,'F','0','0','system:operatorlog:export','#','admin','2023-07-30 23:40:54','','2023-07-30 23:40:54','post','/api-system/system_operator_log/export','',0),
	 (1666,'详情',500,6,'',NULL,NULL,1,0,'F','0','0','system:operatorlog:detail','#','admin','2023-07-30 23:42:01','','2023-07-30 23:42:01','post','/api-system/system_operator_log/page_list','',0),
	 (1667,'列表查询',501,1,'',NULL,NULL,1,0,'F','0','0','system:loginlog:list','#','admin','2023-07-30 23:43:25','admin123','2023-07-31 12:07:16','post','/api-auth/oauth_token_record/page_list','',0),
	 (1668,'删除',501,2,'',NULL,NULL,1,0,'F','0','0','system:loginlog:delete','#','admin','2023-07-30 23:44:19','admin123','2023-07-31 11:53:38','delete','/api-auth/oauth_token_record/delete/{id}','',0),
	 (1669,'清空',501,3,'',NULL,NULL,1,0,'F','0','0','system:loginlog:clean','#','admin','2023-07-30 23:45:00','admin123','2023-07-31 11:53:44','delete','/api-auth/oauth_token_record/delete_all','',0);
INSERT INTO zerosx_system.t_sys_menu (menu_id,menu_name,parent_id,order_num,`path`,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,request_method,request_url,remark,deleted) VALUES
	 (1670,'导出',501,5,'',NULL,NULL,1,0,'F','0','0','system:loginin:export','#','admin','2023-07-30 23:45:40','admin123','2023-07-31 11:53:56','post','/api-auth/oauth_token_record/export','',0),
	 (1671,'列表查询',106,1,'',NULL,NULL,1,0,'F','0','0','system:param:list','#','admin','2023-07-30 23:46:36','','2023-07-30 23:46:36','post','/api-system/sys_param/page_list','',0),
	 (1672,'新增',106,2,'',NULL,NULL,1,0,'F','0','0','system:param:add','#','admin','2023-07-30 23:47:13','','2023-07-30 23:47:13','post','/api-system/sys_param/save','',0),
	 (1673,'编辑',106,3,'',NULL,NULL,1,0,'F','0','0','system:param:update','#','admin','2023-07-30 23:47:48','','2023-07-30 23:47:48','post','/api-system/sys_param/update','',0),
	 (1674,'删除',106,4,'',NULL,NULL,1,0,'F','0','0','system:param:delete','#','admin','2023-07-30 23:48:38','','2023-07-30 23:48:38','delete','/api-system/sys_param/delete/{ids}','',0),
	 (1675,'导出',106,6,'',NULL,NULL,1,0,'F','0','0','system:param:export','#','admin','2023-07-30 23:49:15','','2023-07-30 23:49:15','post','/api-system/sys_param/export','',0),
	 (1676,'个人中心-更新',1621,7,'',NULL,NULL,1,0,'F','0','0','perms:sysUser:updateProfile','#','admin123','2023-08-02 00:03:53','admin123','2023-08-02 00:04:52','put','/api-system/sys_user/update_profile','',0),
	 (1677,'个人中心-更换头像',1621,8,'',NULL,NULL,1,0,'F','0','0','perms:sysUser:updateavatar','#','admin123','2023-08-02 00:04:40','','2023-08-02 00:04:40','post','/api-system/sys_user/profile/avatar','',0),
	 (1678,'个人中心-查看',1621,9,'',NULL,NULL,1,0,'F','0','0','perms:sysUser:query','#','admin123','2023-08-03 00:07:32','','2023-08-03 00:07:32','get','/api-system/sys_user/profile','',0),
	 (1679,'接口文档',1686,4,'openapi3','monitor/openapi3/index',NULL,1,0,'C','0','0','monitor:openapi3:index','swagger','','2023-08-14 13:45:40','admin123','2023-09-07 23:37:24','','','',0);
INSERT INTO zerosx_system.t_sys_menu (menu_id,menu_name,parent_id,order_num,`path`,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,request_method,request_url,remark,deleted) VALUES
	 (1680,'系统监控',1686,3,'monitor','monitor/monitor/index',NULL,1,0,'C','0','0','monitor:monitor:index','eye-open','','2023-08-14 13:46:50','admin123','2023-09-07 23:37:16','','','',0),
	 (1681,'客户端管理',1682,102,'oauthClientDetails','system/oauthClientDetails/index',NULL,1,0,'C','0','0','system:oauthClientDetails:index','client','','2023-08-14 13:54:15','admin123','2023-08-18 20:27:28','','','',0),
	 (1682,'客户端管理',1,16,'oauth',NULL,NULL,1,0,'M','0','0',NULL,'client','','2023-08-18 14:19:36','admin123','2023-08-18 14:21:31','','','',0),
	 (1683,'令牌管理',1682,2,'oauthOnline','system/oauthOnline/index',NULL,1,0,'C','0','0','system:oauthOnline:index','logininfor','','2023-08-18 14:22:55','admin123','2023-08-19 16:55:24','','','',0),
	 (1684,'短信配置',1613,7,'smsSupplier','resource/smsSupplier/index',NULL,1,0,'C','0','0','resource:smsSupplier:index','message','admin123','2023-08-30 18:39:12','admin123','2023-08-30 18:40:44','','','',0),
	 (1685,'任务调度中心',1686,9,'job','monitor/job/index',NULL,1,0,'C','0','0','monitor:xxljob:list','job','admin123','2023-09-07 18:42:45','admin123','2023-09-07 23:37:51','','','',0),
	 (1686,'系统工具',0,6,'monitor',NULL,NULL,1,0,'M','0','0',NULL,'monitor','admin123','2023-09-07 23:33:34','admin123','2023-09-07 23:33:34','','','',0),
	 (1687,'对象存储',1613,5,'ossSupplier','resource/ossSupplier/index',NULL,1,0,'C','0','0','resource:ossSupplier:list','upload','admin123','2023-09-08 18:28:03','admin123','2023-09-08 22:09:18','','','',0),
	 (1688,'对象存储',1613,32,'oss',NULL,NULL,1,0,'M','0','0',NULL,'upload','admin123','2023-09-08 22:04:40','admin123','2023-09-09 17:52:56','','','',1);

/*初始用户（超级管理员）用户名：admin123 密码：Admin123 */
INSERT INTO zerosx_system.t_sys_user (dept_id,user_name,user_code,nick_name,user_type,email,phone_number,sex,avatar,password,status,login_ip,login_date,create_by,create_time,update_by,update_time,remark,operator_id,deleted) VALUES
(NULL,'admin123',NULL,'飞飞有只小毛驴','super_admin','2d96733524cd9c35800f36321717fa7d5f9d6353cd6e9ed52f1f855971d3e7f5','c98e33cf0bdc569958a08bc1042d078c','0','zerosx_cloud_20230906_1699334613312151614.png','{bcrypt}$2a$10$1WCi9CmaY//O6YFyMlWeW.BTm6sLi/oDS.G/Lr29L0/LziIai0wO2','0','127.0.0.1','2023-07-19 15:53:54','admin','2023-07-19 15:53:54','admin123','2023-08-28 16:37:35','超级管理员','',0);

/*系统参数*/
INSERT INTO zerosx_system.t_sys_param (param_name,param_key,param_value,param_scope,status,remark,create_time,create_by,update_time,update_by,operator_id,deleted) VALUES
('最大日期查询范围','query_date_scope','180','0','0','限制最大的日期查询范围，默认无限制','2023-08-02 12:05:53','admin123','2023-08-19 17:30:04','admin123',NULL,0),
('knif4j接口地址','openapi3-url','http://localhost:9100/doc.html','0','0','基于openapi3的knif4j内部接口地址:https://apifox.com/apidoc/shared-34924519-6dc9-4845-b60d-a12316ad6eb8','2023-08-11 11:48:16','admin123','2023-08-13 09:57:15','admin123',NULL,0),
('系统监控地址','monitor-url','http://127.0.0.1:19120/login','0','0','SpringBootAdmin系统监控地址http://localhost:19120/login','2023-08-12 10:40:31','admin123','2023-08-14 13:51:25','admin123',NULL,0);

/*数据字典-类型*/
INSERT INTO zerosx_system.t_sys_dict_type (dict_name,dict_type,dict_status,remarks,create_time,create_by,update_time,update_by,deleted) VALUES
('菜单状态','sys_show_hide','0','菜单状态','2023-07-21 14:48:00','admin','2023-07-21 14:48:00','admin',0),
('性别','sys_user_sex','0','性别','2023-07-21 16:46:38.578000','admin','2023-07-21 16:46:38.578000','admin',0),
('操作日志类型','businessType','0','操作日志的操作类型','2023-07-23 21:29:18.655000','admin','2023-07-23 21:29:18.655000','admin',0),
('操作类型','sys_oper_type','0','操作类型','2023-07-23 21:33:54.371000','admin','2023-07-23 21:33:54.371000','admin',0),
('行政区域等级','area_deep','0','行政区域等级','2023-07-25 10:50:13.179000','admin','2023-07-25 10:50:13.179000','admin',0),
('租户状态','tenant_valid_status','0','租户状态：0：待审核通过；1：正常使用；2：停用','2023-07-25 23:40:12.653000','admin','2023-07-25 23:40:12.653000','admin',0),
('请求方法','sys_request_method','0','请求方法','2023-07-26 14:24:32.773000','admin','2023-07-26 14:24:32.773000','admin',0),
('系统参数范围','sys_param_scope','0','系统参数范围','2023-07-29 01:14:36.628000','admin','2023-07-29 01:14:36.628000','admin',0),
('用户类型','sys_user_type','0','系统用户类型','2023-07-30 16:14:38.689000','admin','2023-07-30 16:14:38.689000','admin',0),
('测试','test','0','测试123','2023-08-02 20:17:36.656000','admin123','2023-08-31 18:18:52.885000','admin123',0);
INSERT INTO zerosx_system.t_sys_dict_type (dict_name,dict_type,dict_status,remarks,create_time,create_by,update_time,update_by,deleted) VALUES
('资源标识','resource_ids','0','资源标识','2023-08-12 21:20:51.574000','admin123','2023-08-31 18:15:58.543000','admin123',0);

/*数据字典-数据*/
INSERT INTO zerosx_system.t_sys_dict_data (dict_sort,dict_label,dict_value,dict_type,is_default,status,create_time,create_by,update_time,update_by,remarks,css_class,list_class,deleted) VALUES
(0,'显示','0','sys_show_hide','Y','0','2023-07-21 14:49:00','admin','2023-07-23 19:37:35.314000','admin','显示',NULL,NULL,0),
(1,'隐藏','1','sys_show_hide','N','0','2023-07-21 14:49:00','admin','2023-07-23 19:37:41.890000','admin','隐藏',NULL,NULL,0),
(1,'女','1','sys_user_sex','N','0','2023-07-22 00:55:24.401000','admin','2023-07-22 01:17:57.534000','admin','性别女',NULL,NULL,0),
(0,'男','0','sys_user_sex','N','0','2023-07-22 01:15:54.531000','admin','2023-07-22 01:25:44.123000','admin','性别男',NULL,NULL,0),
(2,'未知','2','sys_user_sex','N','0','2023-07-22 01:17:26.920000','admin','2023-08-19 17:51:31.169000','admin123','未知性别',NULL,NULL,0),
(0,'查询','1','sys_oper_type','N','0','2023-07-23 21:34:11.343000','admin','2023-07-23 21:38:45.771000','admin','新增',NULL,NULL,0),
(0,'新增','2','sys_oper_type','N','0','2023-07-23 21:34:19.299000','admin','2023-07-23 21:38:57.379000','admin',NULL,NULL,NULL,0),
(0,'更新','3','sys_oper_type','N','0','2023-07-23 21:34:27.190000','admin','2023-07-23 21:39:08.799000','admin',NULL,NULL,NULL,0),
(0,'删除','4','sys_oper_type','N','0','2023-07-23 21:34:40.730000','admin','2023-07-23 21:39:19.048000','admin',NULL,NULL,NULL,0),
(0,'授权','5','sys_oper_type','N','0','2023-07-23 21:34:48.704000','admin','2023-07-23 21:39:32.539000','admin',NULL,NULL,NULL,0);
INSERT INTO zerosx_system.t_sys_dict_data (dict_sort,dict_label,dict_value,dict_type,is_default,status,create_time,create_by,update_time,update_by,remarks,css_class,list_class,deleted) VALUES
(0,'导出','6','sys_oper_type','N','0','2023-07-23 21:34:57.099000','admin','2023-07-23 21:39:46.289000','admin',NULL,NULL,NULL,0),
(0,'导入','7','sys_oper_type','N','0','2023-07-23 21:35:08.261000','admin','2023-07-23 21:39:57.757000','admin',NULL,NULL,NULL,0),
(0,'强退','9','sys_oper_type','N','0','2023-07-23 21:35:21.187000','admin','2023-07-23 21:40:16.491000','admin',NULL,NULL,NULL,0),
(0,'其他','0','sys_oper_type','N','0','2023-07-23 21:35:28.751000','admin','2023-07-23 21:35:28.751000','admin',NULL,NULL,NULL,0),
(0,'清空数据','10','sys_oper_type','N','0','2023-07-23 21:40:32.048000','admin','2023-07-23 21:40:32.048000','admin',NULL,NULL,NULL,0),
(0,'省','0','area_deep','N','0','2023-07-25 10:50:31.504000','admin','2023-07-25 10:50:31.504000','admin',NULL,NULL,NULL,0),
(0,'市','1','area_deep','N','0','2023-07-25 10:50:43.198000','admin','2023-07-25 10:50:43.198000','admin',NULL,NULL,NULL,0),
(0,'区','2','area_deep','N','0','2023-07-25 10:50:50.018000','admin','2023-07-25 10:50:50.018000','admin',NULL,NULL,NULL,0),
(0,'镇','3','area_deep','N','0','2023-07-25 10:51:00.348000','admin','2023-07-25 10:51:00.348000','admin',NULL,NULL,NULL,0),
(0,'待审核','0','tenant_valid_status','N','0','2023-07-25 23:40:38.316000','admin','2023-07-25 23:40:38.316000','admin',NULL,NULL,NULL,0);
INSERT INTO zerosx_system.t_sys_dict_data (dict_sort,dict_label,dict_value,dict_type,is_default,status,create_time,create_by,update_time,update_by,remarks,css_class,list_class,deleted) VALUES
(0,'正常','1','tenant_valid_status','N','0','2023-07-25 23:40:46.882000','admin','2023-07-25 23:44:39.659000','admin',NULL,NULL,NULL,0),
(0,'停用','2','tenant_valid_status','N','0','2023-07-25 23:40:57.007000','admin','2023-07-25 23:40:57.007000','admin',NULL,NULL,NULL,0),
(0,'POST','post','sys_request_method','N','0','2023-07-26 14:24:59.828000','admin','2023-07-27 14:09:43.116000','admin','post请求',NULL,'primary',0),
(0,'GET','get','sys_request_method','N','0','2023-07-26 14:25:14.358000','admin','2023-07-26 14:25:14.358000','admin',NULL,NULL,NULL,0),
(0,'PUT','put','sys_request_method','N','0','2023-07-26 14:25:29.968000','admin','2023-07-26 14:26:00.645000','admin',NULL,NULL,NULL,0),
(0,'DELETE','delete','sys_request_method','N','0','2023-07-26 14:25:43.098000','admin','2023-07-26 14:25:43.098000','admin',NULL,NULL,NULL,0),
(1,'全局','0','sys_param_scope','N','0','2023-07-29 01:15:06.356000','admin','2023-08-02 17:41:15.517000','admin123',NULL,NULL,'primary',0),
(2,'运营商','1','sys_param_scope','N','0','2023-07-29 01:15:22.424000','admin','2023-08-02 17:41:07.518000','admin123',NULL,NULL,'success',0),
(0,'超级管理员','super_admin','sys_user_type','N','0','2023-07-30 16:14:58.102000','admin','2023-07-30 16:14:58.102000','admin','超级管理员',NULL,'danger',0),
(0,'SaaS管理员','saas_operator','sys_user_type','N','0','2023-07-30 16:15:22.223000','admin','2023-07-30 16:15:22.223000','admin','SaaS管理员',NULL,'primary',0);
INSERT INTO zerosx_system.t_sys_dict_data (dict_sort,dict_label,dict_value,dict_type,is_default,status,create_time,create_by,update_time,update_by,remarks,css_class,list_class,deleted) VALUES
(0,'租户操作员','tenant_operator','sys_user_type','N','0','2023-07-30 16:15:39.180000','admin','2023-07-30 16:15:39.180000','admin','租户操作员',NULL,'success',0),
(0,'测试01','test012','test','N','0','2023-08-02 20:19:24.249000','admin123','2023-08-31 18:19:00.837000','admin123',NULL,NULL,'primary',0),
(0,'测试023','test02','test','N','0','2023-08-02 20:19:40.460000','admin123','2023-08-31 18:19:08.507000','admin123',NULL,NULL,'success',0),
(0,'系统服务','api-system','resource_ids','N','0','2023-08-12 21:21:45.412000','admin123','2023-08-12 21:21:45.412000','admin123','系统服务',NULL,'primary',0),
(0,'授权服务','api-auth','resource_ids','N','0','2023-08-12 21:22:05.948000','admin123','2023-08-12 21:22:05.948000','admin123','授权服务',NULL,'primary',0);
