CREATE TABLE `freeman_0` (
  `id` bigint(20) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `identity` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sex` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_freeman_identity` (`identity`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `freeman_1` (
  `id` bigint(20) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `identity` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sex` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_freeman_identity` (`identity`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
