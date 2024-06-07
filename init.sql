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
    UNIQUE KEY `id` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='项目表';

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
    `core_size`       int(11)      DEFAULT NULL COMMENT '核心线程数',
    `max_size`        int(11)      DEFAULT NULL COMMENT '最大线程数',
    `queue_type`      int(11)      DEFAULT NULL COMMENT '队列类型...',
    `capacity`        int(11)      DEFAULT NULL COMMENT '队列大小',
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
    UNIQUE KEY `id` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='线程池配置表';

INSERT IGNORE INTO `config_info` (`id`, `tenant_id`, `item_id`, `tp_id`, `core_size`, `max_size`,
                                  `queue_type`, `capacity`, `keep_alive_time`,
                                  `content`, `md5`, `is_alarm`, `capacity_alarm`,
                                  `liveness_alarm`, `gmt_create`, `gmt_modified`, `del_flag`)
VALUES ('1', 'prescription', 'message-center', 'message-consume', '5', '10', '9',
        '1024', '9999',
        '{\"tenantId\":\"prescription\",\"itemId\":\"dynamic-threadpool-example\",\"tpId\":\"message-consume\",\"coreSize\":5,\"maxSize\":10,\"queueType\":9,\"capacity\":1024,\"keepAliveTime\":9999,\"rejectedType\":2,\"isAlarm\":0,\"capacityAlarm\":80,\"livenessAlarm\":80,\"allowCoreThreadTimeOut\":0}',
        'f80ea89044889fb6cec20e1a517f2ec3', '0', '80', '80', '2024-1-24 10:00:00', '2024-1-24 10:00:00', '0'),
       ('2', 'prescription', 'message-center', 'message-produce', '5', '15', '9',
        '1024', '9999',
        '{\"tenantId\":\"prescription\",\"itemId\":\"dynamic-threadpool-example\",\"tpId\":\"message-produce\",\"coreSize\":5,\"maxSize\":15,\"queueType\":9,\"capacity\":1024,\"keepAliveTime\":9999,\"rejectedType\":1,\"isAlarm\":0,\"capacityAlarm\":30,\"livenessAlarm\":30,\"allowCoreThreadTimeOut\":0}',
        '525e1429468bcfe98df7e70a75710051', '0', '30', '30', '2024-1-24 10:00:00', '2024-1-24 10:00:00', '0');



