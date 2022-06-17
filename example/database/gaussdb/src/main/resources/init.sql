CREATE TABLE `work_order` (
  `id` int(11) NOT NULL,
  `work_order_id` varchar(255) DEFAULT NULL,
  `related_size` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_work_order_id_related_size` (`work_order_id`,`related_size`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;