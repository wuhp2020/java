CREATE TABLE `magic_api`  (
  `id` bigint(255) NOT NULL AUTO_INCREMENT,
  `file_path` varchar(255) NULL DEFAULT NULL,
  `file_content` text NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
);

CREATE TABLE `employee` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `name` varchar(255) DEFAULT NULL COMMENT '姓名',
    `age` int NOT NULL COMMENT '年龄',
    `departmentid` varchar(255) DEFAULT NULL COMMENT '部门id',
    `intotime` date DEFAULT NULL COMMENT '日期',
    `create_date` datetime DEFAULT NULL COMMENT '创建日期',
    `salary` double DEFAULT NULL COMMENT '薪资',
    PRIMARY KEY (`id`),
    KEY `employee_age_name` (`age`,`name`),
    KEY `employee_salary_name` (`salary`,`name`),
    KEY `employee_departid` (`departmentid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;