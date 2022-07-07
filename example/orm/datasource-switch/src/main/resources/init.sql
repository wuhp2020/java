CREATE TABLE `order_info` (
  `id` int(20) NOT NULL COMMENT 'ID' ,
  `order_no` char(20) NOT NULL COMMENT '订单号' ,
  `take_goods_code` char(20) DEFAULT NULL COMMENT '取货码' ,
  `channel_source_code` char(20) DEFAULT NULL COMMENT '渠道来源编码' ,
  `channel_source_name` varchar(255) DEFAULT NULL COMMENT '渠道来源名称' ,
  `business_code` char(20) DEFAULT NULL COMMENT '商家编码' ,
  `business_name` varchar(255) DEFAULT NULL COMMENT '商家名称' ,
  `business_phone` char(11) DEFAULT NULL COMMENT '商家手机号' ,
  `money` DECIMAL(10, 2) DEFAULT 0 COMMENT '商品金额' ,
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名' ,
  `store_code` char(20) DEFAULT NULL COMMENT '配送门店编码' ,
  `store_name` varchar(255) DEFAULT NULL COMMENT '配送门店名称' ,
  `is_pay` char(1) DEFAULT NULL COMMENT '是否支付: 1-是, 0-否' ,
  `delivery_type` char(1) DEFAULT NULL COMMENT '配送方式' ,
  `delivery_company_code` char(20) DEFAULT NULL COMMENT '配送公司编码' ,
  `delivery_company_name` varchar(255) DEFAULT NULL COMMENT '配送公司名称' ,
  `status` char(1) DEFAULT NULL COMMENT '订单状态' ,
  `address` varchar(255) DEFAULT NULL COMMENT '订单收货地址' ,
  `create_time` datetime COMMENT '订单创建时间' ,
  PRIMARY KEY (`id`),
  KEY order_no_index (`order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



CREATE TABLE `waybill_info` (
  `id` int(20) NOT NULL COMMENT 'ID' ,
  `order_no` char(20) NOT NULL COMMENT '订单号' ,
  `waybill_no` char(20) DEFAULT NULL COMMENT '运单号' ,
  `delivery_fee` DECIMAL(10, 2) DEFAULT 0 COMMENT '运单费用' ,
  `rider_name` varchar(255) DEFAULT NULL COMMENT '骑手名' ,
  `delivery_type` char(1) DEFAULT NULL COMMENT '配送方式' ,
  `delivery_company_code` char(20) DEFAULT NULL COMMENT '配送公司编码' ,
  `delivery_company_name` varchar(255) DEFAULT NULL COMMENT '配送公司名称' ,
  `status` char(1) DEFAULT NULL COMMENT '订单状态' ,
  `create_time` datetime COMMENT '订单创建时间' ,
  PRIMARY KEY (`id`),
  KEY order_waybill_no_index (`order_no`, `waybill_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;