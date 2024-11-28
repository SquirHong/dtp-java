CREATE DATABASE /*!32312 IF NOT EXISTS */ `dynamic_threadPool` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `dynamic_threadPool`;

/******************************************/
/*   表名称 = tenant_info   */
/******************************************/
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`    varchar(128) DEFAULT NULL COMMENT '租户ID',
    `tenant_name`  varchar(128) DEFAULT NULL COMMENT '租户名称',
    `tenant_desc`  varchar(256) DEFAULT NULL COMMENT '租户介绍',
    `owner`        varchar(32)  DEFAULT '-' COMMENT '负责人',
    `gmt_create`   datetime     DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` datetime     DEFAULT NULL COMMENT '修改时间',
    `del_flag`     tinyint(1)   DEFAULT NULL COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `id` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='租户表';

/******************************************/
/*   表名称 = item_info   */
/******************************************/
DROP TABLE IF EXISTS `item_info`;
CREATE TABLE `item_info`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`    varchar(128) DEFAULT NULL COMMENT '租户ID',
    `item_id`      varchar(128) DEFAULT NULL COMMENT '项目ID',
    `item_name`    varchar(128) DEFAULT NULL COMMENT '项目名称',
    `item_desc`    varchar(256) DEFAULT NULL COMMENT '项目介绍',
    `owner`        varchar(32)  DEFAULT NULL COMMENT '负责人',
    `gmt_create`   datetime     DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` datetime     DEFAULT NULL COMMENT '修改时间',
    `del_flag`     tinyint(1)   DEFAULT NULL COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `id` (`id`),
    UNIQUE KEY `uk_iteminfo_tenantitem` (`tenant_id`, `item_id`, `del_flag`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='项目表';

/******************************************/
/*   表名称 = log_record_info   */
/******************************************/
DROP TABLE IF EXISTS `log_record_info`;
CREATE TABLE `log_record_info`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tenant`      varchar(128)        NOT NULL DEFAULT '' COMMENT '租户标识',
    `biz_key`     varchar(128)        NOT NULL DEFAULT '' COMMENT '日志业务标识',
    `biz_no`      varchar(128)        NOT NULL DEFAULT '' COMMENT '业务码标识',
    `operator`    varchar(64)         NOT NULL DEFAULT '' COMMENT '操作人',
    `action`      varchar(128)        NOT NULL DEFAULT '' COMMENT '动作',
    `category`    varchar(128)        NOT NULL DEFAULT '' COMMENT '种类',
    `detail`      varchar(2048)       NOT NULL DEFAULT '' COMMENT '修改的详细信息，可以为json',
    `create_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_biz_key` (`biz_key`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='操作日志表';

/******************************************/
/*   表名称 = config_info   */
/******************************************/
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`
(
    `id`              bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`       varchar(128) DEFAULT NULL COMMENT '租户ID',
    `item_id`         varchar(128) DEFAULT NULL COMMENT '项目ID',
    `tp_id`           varchar(256) DEFAULT NULL COMMENT '线程池ID',
    `tp_name`         varchar(56)  DEFAULT NULL COMMENT '线程池名称',
    `core_size`       int(11)      DEFAULT NULL COMMENT '核心线程数',
    `max_size`        int(11)      DEFAULT NULL COMMENT '最大线程数',
    `queue_type`      int(11)      DEFAULT NULL COMMENT '队列类型...',
    `capacity`        int(11)      DEFAULT NULL COMMENT '队列大小',
    `rejected_type`   int(11)      DEFAULT NULL COMMENT '拒绝策略',
    `keep_alive_time` int(11)      DEFAULT NULL COMMENT '线程存活时间（秒）',
    `content`         longtext COMMENT '线程池内容',
    `md5`             varchar(32)         NOT NULL COMMENT 'MD5',
    `is_alarm`        tinyint(1)   DEFAULT NULL COMMENT '是否报警',
    `capacity_alarm`  int(11)      DEFAULT NULL COMMENT '容量报警',
    `liveness_alarm`  int(11)      DEFAULT NULL COMMENT '活跃度报警',
    `gmt_create`      datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`    datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `del_flag`        tinyint(1)   DEFAULT NULL COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY uk_tenant_item_tp (tenant_id, item_id, tp_id) -- 添加复合唯一键约束
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='线程池配置表';


/******************************************/
/*   表名称 = user   */
/******************************************/
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`           bigint(20)   NOT NULL COMMENT 'ID',
    `user_name`    varchar(64)  NOT NULL COMMENT '用户名',
    `password`     varchar(512) NOT NULL COMMENT '用户密码',
    `role`         varchar(50)  NOT NULL COMMENT '角色',
    `gmt_create`   datetime     NOT NULL COMMENT '创建时间',
    `gmt_modified` datetime     NOT NULL COMMENT '修改时间',
    `del_flag`     tinyint(1)   NOT NULL COMMENT '是否删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';

/******************************************/
/*   表名称 = role   */
/******************************************/
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`           bigint(20)  NOT NULL COMMENT 'ID',
    `role`         varchar(64) NOT NULL COMMENT '角色',
    `user_name`    varchar(64) NOT NULL COMMENT '用户名',
    `gmt_create`   datetime    NOT NULL COMMENT '创建时间',
    `gmt_modified` datetime    NOT NULL COMMENT '修改时间',
    `del_flag`     tinyint(1)  NOT NULL COMMENT '是否删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色表';

/******************************************/
/*   表名称 = permission   */
/******************************************/
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`
(
    `id`           bigint(20)   NOT NULL COMMENT 'ID',
    `role`         varchar(512) NOT NULL COMMENT '角色',
    `resource`     varchar(512) NOT NULL COMMENT '资源',
    `action`       varchar(8)   NOT NULL COMMENT '读写权限',
    `gmt_create`   datetime     NOT NULL COMMENT '创建时间',
    `gmt_modified` datetime     NOT NULL COMMENT '修改时间',
    `del_flag`     tinyint(1)   NOT NULL COMMENT '是否删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='权限表';


/******************************************/
/*   表名称 = notify   */
/******************************************/
DROP TABLE IF EXISTS `notify`;
CREATE TABLE `notify`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `tenant_id`    varchar(128) NOT NULL DEFAULT '' COMMENT '租户ID',
    `item_id`      varchar(128) NOT NULL COMMENT '项目ID',
    `tp_id`        varchar(128) NOT NULL COMMENT '线程池ID',
    `platform`     varchar(32)  NOT NULL COMMENT '通知平台',
    `type`         varchar(16)  NOT NULL COMMENT '通知类型',
    `secret_key`   varchar(256) NOT NULL COMMENT '密钥',
    `interval`     int(11)      DEFAULT NULL COMMENT '报警间隔',
    `receives`     varchar(512) NOT NULL COMMENT '接收者',
    `enable` tinyint(1) DEFAULT NULL COMMENT '是否启用',
    `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `del_flag`     tinyint(1)   NOT NULL COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_notify_biz_key` (`tenant_id`,`item_id`,`tp_id`,`platform`,`type`,`del_flag`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='报警表';


/******************************************/
/*   表名称 = inst_config   */
/******************************************/
DROP TABLE IF EXISTS `inst_config`;
CREATE TABLE `inst_config` (
                                   `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                   `tenant_id` varchar(128) DEFAULT NULL COMMENT '租户ID',
                                   `item_id` varchar(256) DEFAULT NULL COMMENT '项目ID',
                                   `tp_id` varchar(56) DEFAULT NULL COMMENT '线程池ID',
                                   `instance_id` varchar(256) DEFAULT NULL COMMENT '实例ID',
                                   `content` longtext COMMENT '线程池内容',
                                   `md5` varchar(32) NOT NULL COMMENT 'MD5',
                                   `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `id` (`id`),
                                   KEY `idx_config_instance` (`tenant_id`,`item_id`,`tp_id`,`instance_id`) USING BTREE,
                                   KEY `idx_instance` (`instance_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='线程池配置实例表';


/* 租户 */
INSERT INTO `tenant_info` (`id`, `tenant_id`, `tenant_name`, `tenant_desc`, `owner`, `gmt_create`, `gmt_modified`,
                           `del_flag`)
VALUES ('1', 'framework', '架构组', '架构组：负责集团项目的规范标准定义以及执行', 'john', '2024-6-24 11:11:11',
        '2024-6-24 11:11:11', '0');

/* 项目 */
INSERT INTO `item_info` (`id`, `tenant_id`, `item_id`, `item_name`, `item_desc`, `owner`, `gmt_create`, `gmt_modified`,
                         `del_flag`)
VALUES ('1', 'common', 'dtp', '动态线程池示例项目',
        '动态线程池示例项目，项目的 example 模块', 'john', '2024-6-24 11:11:11', '2024-6-24 11:11:11', '0');

/* 线程池 */

INSERT INTO `config_info` (`id`, `tenant_id`, `item_id`, `tp_id`, `tp_name`, `core_size`, `max_size`, `queue_type`,
                           `capacity`, `rejected_type`, `keep_alive_time`, `content`, `md5`, `is_alarm`,
                           `capacity_alarm`, `liveness_alarm`, `gmt_create`, `gmt_modified`, `del_flag`)
VALUES (1, 'common', 'dtp', 'message-consume', NULL, 5, 10, 9, 1000, 2, 100,
        '{\"tenantId\":\"common\",\"itemId\":\"dtp\",\"tpId\":\"message-consume\",\"coreSize\":5,\"maxSize\":10,\"queueType\":9,\"capacity\":1000,\"keepAliveTime\":100,\"rejectedType\":2,\"isAlarm\":true,\"capacityAlarm\":80,\"livenessAlarm\":50}',
        'a754962bceff9591ba940894a6a436ef', 1, 80, 50, '2024-07-14 22:28:08', '2024-07-14 22:28:08', 0),
       (2, 'common', 'dtp', 'message-produce', NULL, 10, 20, 9, 500, 1, 100,
        '{\"tenantId\":\"common\",\"itemId\":\"dtp\",\"tpId\":\"message-produce\",\"coreSize\":10,\"maxSize\":20,\"queueType\":9,\"capacity\":500,\"keepAliveTime\":100,\"rejectedType\":1,\"isAlarm\":true,\"capacityAlarm\":70,\"livenessAlarm\":70}',
        '5fc10a841821c26da09372af8756ff3e', 1, 70, 70, '2024-07-14 22:28:11', '2024-07-14 22:28:11', 0),
       (3, 'common', 'dtp', 'hjs-pool', NULL, 20, 30, 9, 100, 3, 30,
        '{\"tenantId\":\"common\",\"itemId\":\"dtp\",\"tpId\":\"hjs-pool\",\"coreSize\":20,\"maxSize\":30,\"queueType\":9,\"capacity\":100,\"keepAliveTime\":30,\"rejectedType\":3,\"isAlarm\":true,\"capacityAlarm\":80,\"livenessAlarm\":80}',
        '6ab773ef7b26eb80a5cfba94c35596b6', 1, 80, 80, '2024-07-14 22:28:14', '2024-07-14 22:28:14', 0),
       (4, 'common', 'dtp', 'custom-pool', NULL, 11, 22, 9, 50, 2, 20,
        '{\"tenantId\":\"common\",\"itemId\":\"dtp\",\"tpId\":\"custom-pool\",\"coreSize\":11,\"maxSize\":22,\"queueType\":9,\"capacity\":50,\"keepAliveTime\":20,\"rejectedType\":2,\"isAlarm\":true,\"capacityAlarm\":60,\"livenessAlarm\":60}',
        '305d072e795ca3bd64bd9644c7148b3a', 1, 60, 60, '2024-07-14 22:28:18', '2024-07-14 22:28:18', 0);

/* 用户 */
INSERT INTO `user`
VALUES (1832992147790843905, 'admin', '$2a$10$mHCMewlb3q8JetwerpL9Hu0QgdkKOVWXmj.PE06bU5LCzwqXG.qVO', 'ROLE_ADMIN',
        '2024-09-09 11:59:18', '2024-09-09 11:59:18', 0);


/* 通知表 */
INSERT INTO `notify` (`id`, `tenant_id`, `item_id`, `tp_id`, `platform`, `type`, `secret_key`, `interval`, `receives`,
                      `enable`, `gmt_create`, `gmt_modified`, `del_flag`)
VALUES ('1461345908531671042', 'common', 'dtp', 'message-produce', 'DING', 'CONFIG',
        '77fcf4d71ca59c7ccd6b66f09ddee44847ab276ebcf0d9e6b5ab56bae69f8468', NULL, '15926772290', '0',
        '2024-09-09 11:59:18', '2024-09-09 11:59:18', 0),
       ('1461345976047382530', 'common', 'dtp', 'message-produce', 'DING', 'ALARM',
        '77fcf4d71ca59c7ccd6b66f09ddee44847ab276ebcf0d9e6b5ab56bae69f8468', '30', '15926772290', '0',
        '2024-09-09 11:59:18', '2024-09-09 11:59:18', 0);
