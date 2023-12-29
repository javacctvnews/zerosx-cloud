/*创建数据库*/
CREATE DATABASE `zerosx_system` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
/*切换数据库*/
USE `zerosx_system`;

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
  `business_type` int DEFAULT '0' COMMENT '按钮操作类别',
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

/* 初始租户 */
INSERT INTO zerosx_system.t_muti_tenancy_group (operator_id, tenant_group_name, tenant_short_name, social_credit_code, business_license_picture, valid_status, audit_status, province, city, area, street, contact_name, contact_mobile_phone, telephone, log_picture, remarks, create_time, update_time, create_by, update_by, deleted)
VALUES('000000', 'ZEROSX科技有限公司', 'ZEROSX科技', 'ZEROSX920202088888', 'zerosx-cloud_20230925_1705997287735398460.jpg', '0', 1, '110000', '110100', '110101', '朝阳区', 'zerosx', 'f16eb6909546f60972cbea745f79886c', '53807297bbba43625c792bd1b4947e81', 'zerosx-cloud_20230925_1705997260405313616.png', 'ZEROSX科技', '2023-09-06 00:41:24.128000', '2023-12-14 23:39:58.161000', 'admin123', 'admin123', 0);

/* 系统菜单初始数据 */
/* truncate TABLE zerosx_system.t_sys_menu; */
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1, '系统管理', 0, 30, 'system', NULL, '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2023-07-19 15:53:55', 'admin123', '2023-09-12 10:11:26', '', '', '系统管理目录', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(4, 'Zeros官网', 0, 100, 'https://www.zeroeev.com', NULL, '', 0, 0, 'M', '0', '0', '', 'bug', 'admin', '2023-07-19 15:53:55', 'admin123', '2023-10-07 22:49:41', '', '', 'RuoYi-Cloud-Plus官网地址', 1);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(102, '菜单权限', 1, 0, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2023-07-19 15:53:55', 'admin123', '2023-12-15 17:38:16', '', '', '菜单管理菜单', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2023-07-19 15:53:55', 'admin123', '2023-12-15 16:27:42', '', '', '字典管理菜单', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(106, '参数设置', 1, 7, 'sysParam', 'system/sysParam/index', '', 1, 0, 'C', '0', '0', 'system:sysParam:list', 'form', 'admin', '2023-07-19 15:53:55', 'admin123', '2023-12-15 16:27:50', '', '', '参数设置菜单', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(108, '日志管理', 1, 90, 'log', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2023-07-19 15:53:55', 'admin123', '2023-12-15 16:28:49', '', '', '日志管理菜单', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(500, '操作日志', 108, 1, 'systemOperatorLog', 'system/systemOperatorLog/index', '', 1, 0, 'C', '0', '0', 'system:systemOperatorLog:list', 'form', 'admin', '2023-07-19 15:53:55', 'admin', '2023-07-27 12:48:56', '', '', '操作日志菜单', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(501, '登录日志', 108, 2, 'oauthTokenRecord', 'system/oauthTokenRecord/index', '', 1, 0, 'C', '0', '0', 'system:oauthTokenRecord:list', 'logininfor', 'admin', '2023-07-19 15:53:55', 'admin123', '2023-07-31 12:04:46', '', '', '登录日志菜单', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1013, '菜单查询', 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2023-07-19 15:53:56', 'admin123', '2023-07-31 12:09:36', 'post', '/api-system/menu/list', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1014, '菜单新增', 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2023-07-19 15:53:56', '', NULL, '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1015, '菜单修改', 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2023-07-19 15:53:56', '', NULL, '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1016, '菜单删除', 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2023-07-19 15:53:56', '', NULL, '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1610, '租户管理', 0, 1, 'tenant', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'saas', '', '2023-07-24 13:10:54', 'admin123', '2023-12-25 18:54:49', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1611, '租户管理', 1610, 1, '/tenant/multiTenant/index', 'tenant/multiTenant/index', NULL, 1, 0, 'C', '0', '0', 'tenant:tenant:pageList', 'peoples', '', '2023-07-24 13:12:47', 'admin123', '2023-07-31 11:36:04', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1612, '行政区域', 1613, 2, 'area', 'resource/area/index', NULL, 1, 0, 'C', '0', '0', 'resource:area:list', 'region', '', '2023-07-24 22:47:10', 'admin123', '2023-09-12 10:14:44', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1613, '资源管理', 0, 5, 'resource', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'documentation', '', '2023-07-25 23:57:47', 'admin123', '2023-09-06 23:55:26', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1614, '文件管理', 1613, 1, 'ossFileUpload', 'resource/ossFileUpload/index', NULL, 1, 0, 'C', '1', '0', 'resource:ossFileUpload:list', 'list', '', '2023-07-25 23:59:07', 'admin123', '2023-12-15 16:14:09', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1616, '角色管理', 1610, 3, 'sysRole', 'perms/sysRole/index', NULL, 1, 0, 'C', '0', '0', 'perms:sysRole:list', 'lock', 'admin', '2023-07-27 17:40:18', 'admin123', '2023-12-15 17:43:19', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1617, '权限管理', 1, 0, 'perms', NULL, NULL, 1, 0, 'M', '1', '0', NULL, 'tree-table', 'admin', '2023-07-28 11:38:54', 'admin123', '2023-12-15 18:08:53', '', '', '', 1);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1619, '岗位管理', 1610, 8, 'sysPost', 'perms/sysPost/index', NULL, 1, 0, 'C', '0', '0', 'perms:sysPost:list', 'post', 'admin', '2023-07-28 13:54:18', 'admin123', '2023-12-15 17:38:06', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1620, '部门管理', 1610, 3, 'sysDept', 'perms/sysDept/index', NULL, 1, 0, 'C', '0', '0', 'perms:sysDept:list', 'tree', 'admin', '2023-07-28 16:05:22', 'admin123', '2023-12-15 17:37:55', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1621, '用户管理', 1610, 2, 'sysUser', 'perms/sysUser/index', NULL, 1, 0, 'C', '0', '0', 'perms:sysUser:list', 'peoples', 'admin', '2023-07-28 16:21:33', 'admin123', '2023-12-15 17:43:09', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1622, '列表查询', 1611, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'tenant:tenant:pageList', '#', 'admin', '2023-07-30 17:20:10', '', '2023-07-30 17:20:10', 'post', '/api-system/muti_tenancy/list_pages', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1623, '新增', 1611, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'tenant:tenant:add', '#', 'admin', '2023-07-30 17:20:55', '', '2023-07-30 17:20:55', 'post', '/api-system/muti_tenancy/save', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1624, '编辑', 1611, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'tenant:tenant:update', '#', 'admin', '2023-07-30 19:27:47', '', '2023-07-30 19:27:47', 'post', '/api-system/muti_tenancy/update', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1625, '删除', 1611, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'tenant:tenant:deleted', '#', 'admin', '2023-07-30 19:29:17', '', '2023-07-30 19:29:17', 'delete', '/api-system/muti_tenancy/deleted/{ids}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1626, '导出', 1611, 5, '', NULL, NULL, 1, 0, 'F', '0', '0', 'tenant:tenant:export', '#', 'admin', '2023-07-30 19:30:18', '', '2023-07-30 19:30:18', 'post', '/api-system/muti_tenancy/export', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1627, '列表查询', 1621, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysuser:list', '#', 'admin', '2023-07-30 19:34:01', '', '2023-07-30 19:34:01', 'post', '/api-system/sys_user/page_list', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1628, '新增', 1621, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysuser:add', '#', 'admin', '2023-07-30 19:35:49', '', '2023-07-30 19:35:49', 'post', '/api-system/sys_user/save', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1629, '编辑', 1621, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysuser:update', '#', 'admin', '2023-07-30 19:36:34', '', '2023-07-30 19:36:34', 'post', '/api-system/sys_user/update', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1630, '删除', 1621, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysuser:delete', '#', 'admin', '2023-07-30 19:37:47', '', '2023-07-30 19:37:47', 'delete', '/api-system/sys_user/delete/{userId}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1631, '导出', 1621, 5, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysuser:export', '#', 'admin', '2023-07-30 19:38:21', '', '2023-07-30 19:38:21', 'post', '/api-system/sys_user/export', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1632, '列表查询', 1619, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'pems:syspost:list', '#', 'admin', '2023-07-30 20:07:23', 'admin', '2023-07-30 21:59:13', 'post', '/api-system/sys_post/page_list', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1633, '新增', 1619, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:syspost:add', '#', 'admin', '2023-07-30 21:56:02', 'admin', '2023-07-30 21:58:59', 'post', '/api-system/sys_post/save', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1634, '编辑', 1619, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:syspost:update', '#', 'admin', '2023-07-30 21:57:17', 'admin', '2023-07-30 21:58:43', 'post', '/api-system/sys_post/update', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1635, '删除', 1619, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:syspost:deleted', '#', 'admin', '2023-07-30 21:58:24', '', '2023-07-30 21:58:24', 'delete', '/api-system/sys_post/delete/{ids}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1636, '列表查询', 1616, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysrole:list', '#', 'admin', '2023-07-30 22:00:50', '', '2023-07-30 22:00:50', 'post', '/api-system/sys_role/page_list', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1637, '新增', 1616, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysrole:add', '#', 'admin', '2023-07-30 22:01:31', 'admin123', '2023-08-19 15:31:41', 'post', '/api-system/sys_role/save', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1638, '编辑', 1616, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysrole:update', '#', 'admin', '2023-07-30 22:02:08', '', '2023-07-30 22:02:08', 'post', '/api-system/sys_role/update', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1639, '删除', 1616, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysrole:delete', '#', 'admin', '2023-07-30 22:04:10', 'admin', '2023-07-30 22:04:54', 'delete', '/api-system/sys_role/delete/{roleIds}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1640, '导出', 1616, 5, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysrole:export', '#', 'admin', '2023-07-30 22:05:34', '', '2023-07-30 22:05:34', 'post', '/api-system/sys_role/export', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1641, '导出', 1619, 5, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:syspost:export', '#', 'admin', '2023-07-30 22:39:27', '', '2023-07-30 22:39:27', 'post', '/api-system/sys_post/export', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1642, '列表查询', 1620, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysdept:list', '#', 'admin', '2023-07-30 22:40:18', 'admin123', '2023-07-31 10:32:02', 'post', '/api-system/sys_dept/table_tree', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1643, '新增', 1620, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysdept:add', '#', 'admin', '2023-07-30 22:42:26', '', '2023-07-30 22:42:26', 'post', '/api-system/sys_dept/save', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1644, '编辑', 1620, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysdept:update', '#', 'admin', '2023-07-30 22:43:37', '', '2023-07-30 22:43:37', 'post', '/api-system/sys_dept/update', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1645, '删除', 1620, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysdept:delete', '#', 'admin', '2023-07-30 22:44:25', '', '2023-07-30 22:44:25', 'delete', '/api-system/sys_dept/delete/{ids}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1646, '导出', 1620, 5, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysdept:export', '#', 'admin', '2023-07-30 22:46:08', '', '2023-07-30 22:46:08', 'post', '/api-system/sys_dept/export', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1647, '树形列表查询', 1612, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'resource:area:list', '#', 'admin', '2023-07-30 22:50:47', 'admin123', '2023-09-28 00:14:44', 'get', '/api-resource/area_city_source/lazy_tree/{parentCode}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1648, '列表查询', 1614, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'resource:ossfile:list', '#', 'admin', '2023-07-30 22:52:06', 'admin123', '2023-09-28 00:13:04', 'post', '/api-resource/oss_file/list_pages', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1649, '删除', 1614, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'resource:ossfile:delete', '#', 'admin', '2023-07-30 22:53:44', 'admin123', '2023-09-28 00:13:11', 'get', '/api-resource/oss_file/full_delete/{ids}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1650, '详情', 1614, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'resource:ossfile:detail', '#', 'admin', '2023-07-30 22:55:47', 'admin123', '2023-09-28 00:13:17', 'get', '/api-resource/oss-file/detail/{id}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1651, '列表查询', 105, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:dict:list', '#', 'admin', '2023-07-30 23:03:23', '', '2023-07-30 23:03:23', 'post', '/api-system/sysDictType_page', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1652, '新增', 105, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2023-07-30 23:06:40', 'admin', '2023-07-30 23:06:46', 'post', '/api-system/sysDictType_insert', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1653, '编辑', 105, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:dict:update', '#', 'admin', '2023-07-30 23:07:29', '', '2023-07-30 23:07:29', 'put', '/api-system/sysDictType_update', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1654, '删除', 105, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:dict:delete', '#', 'admin', '2023-07-30 23:08:16', '', '2023-07-30 23:08:16', 'delete', '/api-system/sysDictType_delete/{dictId}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1655, '导出', 105, 5, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2023-07-30 23:09:10', '', '2023-07-30 23:09:10', 'post', '/api-system/sysDictType/export', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1656, '刷新缓存', 105, 6, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:dict:initCache', '#', 'admin', '2023-07-30 23:10:52', '', '2023-07-30 23:10:52', 'get', '/api-system/sysDictData/init', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1657, '字典数据', 105, 7, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:dict:dictdata', '#', 'admin', '2023-07-30 23:20:51', '', '2023-07-30 23:20:51', 'post', '/api-system/sysDictData_page', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1658, '新增', 1657, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:dict:dictdata:add', '#', 'admin', '2023-07-30 23:23:17', '', '2023-07-30 23:23:17', 'post', '/api-system/sysDictData_insert', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1659, '编辑', 1657, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:dict:dictdata:update', '#', 'admin', '2023-07-30 23:24:09', '', '2023-07-30 23:24:09', 'put', '/api-system/sysDictData_update', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1660, '删除', 1657, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:dict:dictdata:delete', '#', 'admin', '2023-07-30 23:26:18', '', '2023-07-30 23:26:18', 'delete', '/api-system/sysDictData_delete/{dictCode}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1661, '导出', 1657, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:dict:dictdata:export', '#', 'admin', '2023-07-30 23:27:13', '', '2023-07-30 23:27:13', 'post', '/api-system/sysDictData/export', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1662, '列表查询', 500, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:operatorlog:list', '#', 'admin', '2023-07-30 23:37:57', '', '2023-07-30 23:37:57', 'post', '/api-system/system_operator_log/page_list', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1663, '删除', 500, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:operatorlog:delete', '#', 'admin', '2023-07-30 23:38:43', '', '2023-07-30 23:38:43', 'delete', '/api-system/system_operator_log/delete/{id}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1664, '清空', 500, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:operatorlog:clean', '#', 'admin', '2023-07-30 23:40:11', '', '2023-07-30 23:40:11', 'post', '/api-system/system_operator_log/clean', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1665, '导出', 500, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:operatorlog:export', '#', 'admin', '2023-07-30 23:40:54', '', '2023-07-30 23:40:54', 'post', '/api-system/system_operator_log/export', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1666, '详情', 500, 6, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:operatorlog:detail', '#', 'admin', '2023-07-30 23:42:01', '', '2023-07-30 23:42:01', 'post', '/api-system/system_operator_log/page_list', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1667, '列表查询', 501, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:loginlog:list', '#', 'admin', '2023-07-30 23:43:25', 'admin123', '2023-12-15 00:30:45', 'post', '/api-auth/oauth_token_record/page_list', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1668, '删除', 501, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:loginlog:delete', '#', 'admin', '2023-07-30 23:44:19', 'admin123', '2023-12-15 00:31:08', 'delete', '/api-auth/oauth_token_record/delete/{id}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1669, '清空', 501, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:loginlog:clean', '#', 'admin', '2023-07-30 23:45:00', 'admin123', '2023-12-15 00:31:14', 'delete', '/api-auth/oauth_token_record/delete_all', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1670, '导出', 501, 5, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:loginin:export', '#', 'admin', '2023-07-30 23:45:40', 'admin123', '2023-12-15 00:31:24', 'post', '/api-auth/oauth_token_record/export', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1671, '列表查询', 106, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:param:list', '#', 'admin', '2023-07-30 23:46:36', '', '2023-07-30 23:46:36', 'post', '/api-system/sys_param/page_list', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1672, '新增', 106, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:param:add', '#', 'admin', '2023-07-30 23:47:13', '', '2023-07-30 23:47:13', 'post', '/api-system/sys_param/save', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1673, '编辑', 106, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:param:update', '#', 'admin', '2023-07-30 23:47:48', '', '2023-07-30 23:47:48', 'post', '/api-system/sys_param/update', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1674, '删除', 106, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:param:delete', '#', 'admin', '2023-07-30 23:48:38', '', '2023-07-30 23:48:38', 'delete', '/api-system/sys_param/delete/{ids}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1675, '导出', 106, 6, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:param:export', '#', 'admin', '2023-07-30 23:49:15', '', '2023-07-30 23:49:15', 'post', '/api-system/sys_param/export', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1676, '个人中心-更新', 1621, 7, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysUser:updateProfile', '#', 'admin123', '2023-08-02 00:03:53', 'admin123', '2023-08-02 00:04:52', 'put', '/api-system/sys_user/update_profile', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1677, '个人中心-更换头像', 1621, 8, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysUser:updateavatar', '#', 'admin123', '2023-08-02 00:04:40', '', '2023-08-02 00:04:40', 'post', '/api-system/sys_user/profile/avatar', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1678, '个人中心-查看', 1621, 9, '', NULL, NULL, 1, 0, 'F', '0', '0', 'perms:sysUser:query', '#', 'admin123', '2023-08-03 00:07:32', '', '2023-08-03 00:07:32', 'get', '/api-system/sys_user/profile', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1679, '接口文档', 1686, 3, 'openapi3', 'monitor/openapi3/index', NULL, 1, 0, 'C', '0', '0', 'monitor:openapi3:index', 'swagger', '', '2023-08-14 13:45:40', 'admin123', '2023-12-15 16:29:13', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1680, '系统监控', 1686, 6, 'monitor', 'monitor/monitor/index', NULL, 1, 0, 'C', '1', '1', 'monitor:monitor:index', 'eye-open', '', '2023-08-14 13:46:50', 'admin123', '2023-12-27 23:36:05', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1681, '认证客户端', 1682, 102, 'oauthClientDetails', 'system/oauthClientDetails/index', NULL, 1, 0, 'C', '0', '0', 'system:oauthClientDetails:index', 'client', '', '2023-08-14 13:54:15', 'admin123', '2023-12-15 17:45:54', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1682, '客户端管理', 1, 10, 'oauth', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'client', '', '2023-08-18 14:19:36', 'admin123', '2023-12-15 17:46:17', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1683, '令牌管理', 1682, 2, 'oauthOnline', 'system/oauthOnline/index', NULL, 1, 0, 'C', '0', '0', 'system:oauthOnline:index', 'logininfor', '', '2023-08-18 14:22:55', 'admin123', '2023-08-19 16:55:24', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1684, '短信配置', 1613, 7, 'smsSupplier', 'resource/smsSupplier/index', NULL, 1, 0, 'C', '0', '0', 'resource:smsSupplier:index', 'sms', 'admin123', '2023-08-30 18:39:12', 'admin123', '2023-09-12 10:10:54', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1685, '任务调度中心', 1686, 4, 'job', 'monitor/job/index', NULL, 1, 0, 'C', '1', '1', 'monitor:xxljob:list', 'job', 'admin123', '2023-09-07 18:42:45', 'admin123', '2023-12-27 23:35:58', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1686, '系统工具', 0, 60, 'monitor', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'setting-tools', 'admin123', '2023-09-07 23:33:34', 'admin123', '2023-12-15 16:23:44', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1687, '对象存储', 1613, 5, 'ossSupplier', 'resource/ossSupplier/index', NULL, 1, 0, 'C', '0', '0', 'resource:ossSupplier:list', 'oss', 'admin123', '2023-09-08 18:28:03', 'admin123', '2023-09-12 10:10:45', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1688, '对象存储', 1613, 32, 'oss', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'upload', 'admin123', '2023-09-08 22:04:40', 'admin123', '2023-09-09 17:52:56', '', '', '', 1);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1689, '新增', 1687, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:ossSupplier:add', '#', 'admin123', '2023-09-11 20:39:21', 'admin123', '2023-09-28 00:35:52', 'post', '/api-resource/oss_supplier/save', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1690, '编辑', 1687, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:ossSupplier:update', '#', 'admin123', '2023-09-11 20:39:58', 'admin123', '2023-09-28 00:35:58', 'post', '/api-resource/oss_supplier/update', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1691, '删除', 1687, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:ossSupplier:delete', '#', 'admin123', '2023-09-11 20:40:40', 'admin123', '2023-09-28 00:36:03', 'delete', '/api-resource/oss_supplier/delete/{ids}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1692, '导出', 1687, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:ossSupplier:export', '#', 'admin123', '2023-09-11 20:41:16', 'admin123', '2023-09-28 00:36:08', 'post', '/api-resource/oss_supplier/export', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1693, '文件列表', 1687, 6, '', NULL, NULL, 1, 0, 'F', '0', '0', 'resource:ossFileUpload:list', '#', 'admin123', '2023-09-11 20:42:08', 'admin123', '2023-09-11 20:42:08', 'post', '/api-system/oss_file/list_pages', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1694, '删除', 1693, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'resource:ossfile:delete', '#', 'admin123', '2023-09-11 20:43:14', 'admin123', '2023-09-28 00:36:25', 'delete', '/api-resource/delete_file/{objectName}/delete', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1695, '详情', 1693, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'resource:ossfile:detail', '#', 'admin123', '2023-09-11 20:44:51', 'admin123', '2023-09-28 00:36:30', 'get', '/api-resource/oss_file/queryById/{id}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1696, '新增', 1684, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:smsSupplier:add', '#', 'admin123', '2023-09-11 20:45:51', 'admin123', '2023-09-28 00:36:47', 'post', '/api-resource/sms_supplier/save', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1697, '编辑', 1684, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:smsSupplier:update', '#', 'admin123', '2023-09-11 20:46:24', 'admin123', '2023-09-28 00:36:52', 'post', '/api-resource/sms_supplier/update', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1698, '删除', 1684, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:smsSupplier:delete', '#', 'admin123', '2023-09-11 20:46:59', 'admin123', '2023-09-28 00:36:56', 'delete', '/api-resource/sms_supplier/delete/{ids}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1699, '导出', 1684, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:smsSupplier:export', '#', 'admin123', '2023-09-11 20:47:47', 'admin123', '2023-09-28 00:37:01', 'post', '/api-resource/sms_supplier/export', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1700, '短信模板', 1684, 5, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:sms_supplier_business:page_list', '#', 'admin123', '2023-09-11 20:49:21', 'admin123', '2023-09-28 00:37:11', 'post', '/api-resource/sms_supplier_business/page_list', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1701, '新增', 1700, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:smsSupplierBusiness:add', '#', 'admin123', '2023-09-11 20:50:01', 'admin123', '2023-09-28 00:37:22', 'post', '/api-resource/sms_supplier_business/save', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1702, '编辑', 1700, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:smsSupplierBusiness:update', '#', 'admin123', '2023-09-11 20:50:36', 'admin123', '2023-09-28 00:37:26', 'post', '/api-resource/sms_supplier_business/update', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1703, '删除', 1700, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:smsSupplierBusiness:delete', '#', 'admin123', '2023-09-11 20:51:12', 'admin123', '2023-09-28 00:37:43', 'delete', '/api-resource/sms_supplier_business/delete/{ids}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1704, '导出', 1700, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:smsSupplierBusiness:export', '#', 'admin123', '2023-09-11 20:53:33', 'admin123', '2023-09-28 00:37:48', 'post', '/api-resource/sms_supplier_business/export', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1705, '分页列表', 1687, 0, '', NULL, NULL, 1, 0, 'F', '0', '0', 'resource:ossSupplier:list', '#', 'admin123', '2023-09-11 23:37:10', 'admin123', '2023-09-28 00:35:47', 'post', '/api-resource/oss_supplier/page_list', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1706, '分页列表', 1693, 0, '', NULL, NULL, 1, 0, 'F', '0', '0', 'resource:ossFileUpload:list', '#', 'admin123', '2023-09-11 23:38:10', 'admin123', '2023-09-28 00:36:18', 'post', '/api-resource/oss_file/list_pages', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1707, '分页列表', 1684, 0, '', NULL, NULL, 1, 0, 'F', '0', '0', 'resource:smsSupplier:index', '#', 'admin123', '2023-09-11 23:38:57', 'admin123', '2023-09-28 00:36:40', 'post', '/api-resource/sms_supplier/page_list', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1708, '分页列表', 1700, 0, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:sms_supplier_business:page_list', '#', 'admin123', '2023-09-11 23:39:34', 'admin123', '2023-09-28 00:37:17', 'post', '/api-resource/sms_supplier_business/page_list', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1709, '分页列表', 1683, 0, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:oauthOnline:index', '#', 'admin123', '2023-09-11 23:42:24', 'admin123', '2023-09-11 23:42:35', 'post', '/api-auth/token/page_list', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1710, '强退', 1683, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'auth:oauthClientDetails:delete', '#', 'admin123', '2023-09-11 23:43:46', 'admin123', '2023-09-11 23:43:46', 'post', '/api-auth/token/logout', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1711, '强退所有', 1683, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'auth:oauthClientDetails:deleteAll', '#', 'admin123', '2023-09-11 23:45:02', 'admin123', '2023-09-11 23:45:02', 'post', '/api-auth/token/logout', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1712, '清空过期TOKEN', 1683, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'auth:oauthClientDetails:deleteExpire', '#', 'admin123', '2023-09-11 23:46:15', 'admin123', '2023-09-11 23:46:41', 'post', '/api-auth/token/clean_token_data', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1713, '导出', 1683, 6, '', NULL, NULL, 1, 0, 'F', '0', '0', 'auth:oauthClientDetails:export', '#', 'admin123', '2023-09-11 23:47:20', 'admin123', '2023-09-11 23:47:20', 'post', '/api-auth/token/export', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1714, '分页查询', 1681, 0, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:oauthClientDetails:index', '#', 'admin123', '2023-09-11 23:48:11', 'admin123', '2023-09-11 23:48:11', 'post', '/api-auth/oauth_client_details/list_page', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1715, '新增', 1681, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'auth:oauthClientDetails:add', '#', 'admin123', '2023-09-11 23:48:51', 'admin123', '2023-09-11 23:48:51', 'post', '/api-auth/oauth_client_details/save', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1716, '编辑', 1681, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'auth:oauthClientDetails:update', '#', 'admin123', '2023-09-11 23:49:21', 'admin123', '2023-09-11 23:49:21', 'post', '/api-auth/oauth_client_details/edit', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1717, '删除', 1681, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'auth:oauthClientDetails:delete', '#', 'admin123', '2023-09-11 23:49:51', 'admin123', '2023-09-11 23:49:51', 'delete', '/api-auth/oauth_client_details/delete/{ids}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1718, '导出', 1681, 5, '', NULL, NULL, 1, 0, 'F', '0', '0', 'auth:oauthClientDetails:export', '#', 'admin123', '2023-09-11 23:50:19', 'admin123', '2023-09-11 23:50:19', 'post', '/api-auth/oauth_client_details/export', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1719, '分布式ID', 1686, 0, 'leafAlloc', 'system/leafAlloc', NULL, 1, 0, 'C', '0', '0', 'system:leafAlloc:list', 'table', 'admin123', '2023-12-05 14:09:27', 'admin123', '2023-12-15 16:29:01', '', '', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1720, '新增', 1719, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'leaf:leafAlloc:add', '#', 'admin123', '2023-12-07 18:47:35', 'admin123', '2023-12-07 18:56:48', 'post', '/api-leaf/leaf_alloc/save', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1721, '编辑', 1719, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'leaf:leafAlloc:update', '#', 'admin123', '2023-12-07 18:48:12', 'admin123', '2023-12-07 18:52:57', 'post', '/api-leaf/leaf_alloc/update', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1722, '删除', 1719, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'leaf:leafAlloc:delete', '#', 'admin123', '2023-12-07 18:53:40', 'admin123', '2023-12-07 18:53:40', 'delete', '/api-leaf/leaf_alloc/delete/{bizTags}', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1723, '导出', 1719, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'leaf:leafAlloc:export', '#', 'admin123', '2023-12-07 18:54:37', 'admin123', '2023-12-07 18:54:37', 'post', '/api-leaf/leaf_alloc/export', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1724, '分页列表', 1719, 0, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:leafAlloc:list', '#', 'admin123', '2023-12-07 18:56:42', 'admin123', '2023-12-07 18:56:42', 'post', '/api-leaf/leaf_alloc/page_list', '', 0);
INSERT INTO zerosx_system.t_sys_menu (menu_id, menu_name, parent_id, order_num, `path`, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, request_method, request_url, remark, deleted) VALUES(1725, '监控中心', 1686, 100, 'grafana', 'monitor/grafana/index', NULL, 1, 0, 'C', '0', '0', 'monitor:grafana:index', 'server', 'admin123', '2023-12-25 23:18:46', 'admin123', '2023-12-27 22:59:54', '', '', '', 0);
/*初始用户（超级管理员）用户名：admin123|zerosx 密码：Admin123 */
INSERT INTO zerosx_system.t_sys_user (id,dept_id,user_name,user_code,nick_name,user_type,email,phone_number,sex,avatar,password,status,login_ip,login_date,create_by,create_time,update_by,update_time,remark,operator_id,deleted) VALUES
(1,NULL,'admin123',NULL,'飞飞有只小毛驴','super_admin','2d96733524cd9c35800f36321717fa7d5f9d6353cd6e9ed52f1f855971d3e7f5','c98e33cf0bdc569958a08bc1042d078c','0','zerosx_cloud_20230906_1699334613312151614.png','{bcrypt}$2a$10$1WCi9CmaY//O6YFyMlWeW.BTm6sLi/oDS.G/Lr29L0/LziIai0wO2','0','127.0.0.1','2023-07-19 15:53:54','admin','2023-07-19 15:53:54','admin123','2023-08-28 16:37:35','超级管理员','',0);
INSERT INTO zerosx_system.t_sys_user (id, dept_id, user_name, user_code, nick_name, user_type, email, phone_number, sex, avatar, password, status, login_ip, login_date, create_by, create_time, update_by, update_time, remark, operator_id, deleted)
VALUES(2, NULL, 'zerosx', '1735333801401188388', 'zerosx', 'saas_operator', '4af9dd451b7f16ec3a9eb2009df28e6c', '500cc6beb9d4d073540136122f5a2850', '2', '', '{bcrypt}$2a$10$8GKP0r4.fdxyaNKCL2U7wuRBMW9rhv1mb196.YGZJ7mTiiLkmds0i', '0', '', NULL, 'admin123', '2023-12-15 00:19:54', 'admin123', '2023-12-15 00:19:54', 'Admin123', '000000', 0);
/* 初始角色 */
INSERT INTO zerosx_system.t_sys_role (id, role_name, role_key, role_sort, menu_check_strictly, dept_check_strictly, status, create_by, create_time, update_by, update_time, remark, operator_id, deleted) VALUES(3, '租户通用123123', '1699818194522329145', 0, 1, 1, '0', 'admin123', '2023-09-08 00:13:33', 'admin123', '2023-12-15 00:27:13', '123123', '000000', 0);
/* 初始角色与用户关系表 */
INSERT INTO zerosx_system.t_sys_user_role (user_id, role_id) VALUES(2, 3);
/* 初始角色与菜单按钮关系表 */
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1610);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1611);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1622);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1623);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1624);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1625);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1626);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1613);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1614);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1648);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1649);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1650);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1612);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1647);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1687);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1705);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1689);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1690);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1691);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1692);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1693);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1706);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1694);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1695);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1684);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1707);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1696);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1697);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1698);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1699);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1700);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1708);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1701);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1702);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1703);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1704);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1621);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1627);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1628);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1629);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1630);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1631);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1676);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1677);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1678);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1616);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1636);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1637);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1638);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1639);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1640);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1620);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1642);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1643);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1644);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1645);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1646);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1619);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1632);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1633);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1634);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1635);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1641);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 108);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 500);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1662);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1663);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1664);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1665);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1666);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 501);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1667);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1668);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1669);
INSERT INTO zerosx_system.t_sys_role_menu (role_id, menu_id) VALUES(3, 1670);