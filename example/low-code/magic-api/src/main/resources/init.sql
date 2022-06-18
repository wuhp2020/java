CREATE TABLE `magic_api`  (
  `id` bigint(255) NOT NULL AUTO_INCREMENT,
  `file_path` varchar(255) NULL DEFAULT NULL,
  `file_content` text NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
);

CREATE TABLE `department` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `employee` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `age` bigint(20) NOT NULL,
  `departmentid` varchar(255) DEFAULT NULL,
  `intotime` date DEFAULT NULL,
  `salary` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `employee_age_name` (`age`,`name`),
  KEY `employee_salary_name` (`salary`,`name`),
  KEY `employee_departid` (`departmentid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;