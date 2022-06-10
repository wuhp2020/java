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

CREATE TABLE `employee_work` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `intotime` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `test`.`department`(`id`, `name`) VALUES (1, '发展部');

INSERT INTO `test`.`employee`(`id`, `name`, `age`, `departmentid`, `intotime`, `salary`) VALUES (1, '吴和平', 28, '1', '2022-06-07', 1);
INSERT INTO `test`.`employee`(`id`, `name`, `age`, `departmentid`, `intotime`, `salary`) VALUES (2, '张三', 24, '1', '2022-06-09', 2);

INSERT INTO `test`.`employee_work`(`id`, `name`, `intotime`) VALUES (1, '吴和平', '2022-06-07');
INSERT INTO `test`.`employee_work`(`id`, `name`, `intotime`) VALUES (2, '吴和平', '2022-06-30');
INSERT INTO `test`.`employee_work`(`id`, `name`, `intotime`) VALUES (3, '张三', '2022-06-19');
