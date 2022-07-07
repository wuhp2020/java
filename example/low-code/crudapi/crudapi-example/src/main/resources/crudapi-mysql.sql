/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.221.129
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : 192.168.221.129:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 17/06/2022 14:59:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for SPRING_SESSION
-- ----------------------------
DROP TABLE IF EXISTS `SPRING_SESSION`;
CREATE TABLE `SPRING_SESSION` (
  `PRIMARY_ID` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `SESSION_ID` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `CREATION_TIME` bigint(20) NOT NULL,
  `LAST_ACCESS_TIME` bigint(20) NOT NULL,
  `MAX_INACTIVE_INTERVAL` int(11) NOT NULL,
  `EXPIRY_TIME` bigint(20) NOT NULL,
  `PRINCIPAL_NAME` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`),
  UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of SPRING_SESSION
-- ----------------------------
BEGIN;
INSERT INTO `SPRING_SESSION` VALUES ('8ba9b786-3d5b-429d-9da0-865c0ed06d0a', '309443bd-40fd-4c25-b606-9cb26f2af982', 1655449146592, 1655449150501, 3600, 1655452750501, 'superadmin');
COMMIT;

-- ----------------------------
-- Table structure for SPRING_SESSION_ATTRIBUTES
-- ----------------------------
DROP TABLE IF EXISTS `SPRING_SESSION_ATTRIBUTES`;
CREATE TABLE `SPRING_SESSION_ATTRIBUTES` (
  `SESSION_PRIMARY_ID` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ATTRIBUTE_NAME` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`),
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `SPRING_SESSION` (`PRIMARY_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of SPRING_SESSION_ATTRIBUTES
-- ----------------------------
BEGIN;
INSERT INTO `SPRING_SESSION_ATTRIBUTES` VALUES ('8ba9b786-3d5b-429d-9da0-865c0ed06d0a', 'SPRING_SECURITY_CONTEXT', 0xACED00057372003D6F72672E737072696E676672616D65776F726B2E73656375726974792E636F72652E636F6E746578742E5365637572697479436F6E74657874496D706C00000000000002080200014C000E61757468656E7469636174696F6E7400324C6F72672F737072696E676672616D65776F726B2F73656375726974792F636F72652F41757468656E7469636174696F6E3B78707372004F6F72672E737072696E676672616D65776F726B2E73656375726974792E61757468656E7469636174696F6E2E557365726E616D6550617373776F726441757468656E7469636174696F6E546F6B656E00000000000002080200024C000B63726564656E7469616C737400124C6A6176612F6C616E672F4F626A6563743B4C00097072696E636970616C71007E0004787200476F72672E737072696E676672616D65776F726B2E73656375726974792E61757468656E7469636174696F6E2E416273747261637441757468656E7469636174696F6E546F6B656ED3AA287E6E47640E0200035A000D61757468656E746963617465644C000B617574686F7269746965737400164C6A6176612F7574696C2F436F6C6C656374696F6E3B4C000764657461696C7371007E0004787001737200266A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654C697374FC0F2531B5EC8E100200014C00046C6973747400104C6A6176612F7574696C2F4C6973743B7872002C6A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C65436F6C6C656374696F6E19420080CB5EF71E0200014C00016371007E00067870737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A657870000000017704000000017372002B636E2E637275646170692E73656375726974792E64746F2E4772616E746564417574686F7269747944544F00000000000000010200014C0009617574686F726974797400124C6A6176612F6C616E672F537472696E673B7870740010524F4C455F53555045525F41444D494E7871007E000D73720038636E2E637275646170692E73656375726974792E636F6D706F6E656E742E436157656241757468656E7469636174696F6E44657461696C7300000000000000010200015A0010696D616765436F646549735269676874787200486F72672E737072696E676672616D65776F726B2E73656375726974792E7765622E61757468656E7469636174696F6E2E57656241757468656E7469636174696F6E44657461696C7300000000000002080200024C000D72656D6F74654164647265737371007E000F4C000973657373696F6E496471007E000F78707400093132372E302E302E317001707372001F636E2E637275646170692E73656375726974792E64746F2E5573657244544F00000000000000010200115A00116163636F756E744E6F6E457870697265645A00106163636F756E744E6F6E4C6F636B65645A001563726564656E7469616C734E6F6E457870697265645A0007656E61626C65644C000B617574686F72697469657371007E00094C000C636C6561727465787450776471007E000F4C0005656D61696C71007E000F4C000269647400104C6A6176612F6C616E672F4C6F6E673B4C00066D6F62696C6571007E000F4C00046E616D6571007E000F4C00066F70656E496471007E000F4C000870617373776F726471007E000F4C00087265616C6E616D6571007E000F4C00097265736F757263657371007E00094C0005726F6C657371007E00094C0005746F6B656E71007E000F4C0008757365726E616D6571007E000F7870010101017371007E000C0000000177040000000171007E001078707400046E756C6C7372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B0200007870000000000000000174000B313131313131313131313174000FE8B685E7BAA7E7AEA1E79086E5919871007E001A74003C24326124313024523948576F747170587A6D4C4F553061596C466B7175786373306A7137663172614B48486E736266543531674D3954462E3271377174000FE8B685E7BAA7E7AEA1E79086E591987371007E000C00000000770400000000787371007E000C000000017704000000017372001F636E2E637275646170692E73656375726974792E64746F2E526F6C6544544F00000000000000010200034C0004636F646571007E000F4C0002696471007E00174C00046E616D6571007E000F787071007E00117371007E001B000000000000000B74000FE8B685E7BAA7E7AEA1E79086E5919878740020313039346132633565353763346337666133393232643639623031326366613774000A737570657261646D696E);
COMMIT;

-- ----------------------------
-- Table structure for ca_city
-- ----------------------------
DROP TABLE IF EXISTS `ca_city`;
CREATE TABLE `ca_city` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fullTextBody` text COLLATE utf8mb4_unicode_ci,
  `createdDate` datetime NOT NULL,
  `lastModifiedDate` datetime DEFAULT NULL,
  `code` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `provinceId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FULLTEXT KEY `ft_fulltext_body` (`fullTextBody`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for ca_district
-- ----------------------------
DROP TABLE IF EXISTS `ca_district`;
CREATE TABLE `ca_district` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fullTextBody` text COLLATE utf8mb4_unicode_ci,
  `createdDate` datetime NOT NULL,
  `lastModifiedDate` datetime DEFAULT NULL,
  `code` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cityId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FULLTEXT KEY `ft_fulltext_body` (`fullTextBody`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for ca_meta_column
-- ----------------------------
DROP TABLE IF EXISTS `ca_meta_column`;
CREATE TABLE `ca_meta_column` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `autoIncrement` bit(1) DEFAULT NULL,
  `caption` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdDate` datetime(6) DEFAULT NULL,
  `dataType` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `defaultValue` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `displayOrder` int(11) DEFAULT NULL,
  `indexName` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `indexStorage` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `indexType` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `insertable` bit(1) DEFAULT NULL,
  `lastModifiedDate` datetime(6) DEFAULT NULL,
  `length` int(11) DEFAULT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nullable` bit(1) DEFAULT NULL,
  `precision` int(11) DEFAULT NULL,
  `queryable` bit(1) DEFAULT NULL,
  `scale` int(11) DEFAULT NULL,
  `seqId` bigint(20) DEFAULT NULL,
  `unsigned` bit(1) DEFAULT NULL,
  `updatable` bit(1) DEFAULT NULL,
  `displayable` bit(1) DEFAULT NULL,
  `systemable` bit(1) DEFAULT NULL,
  `multipleValue` bit(1) DEFAULT b'0',
  `tableId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_bsm_column_name` (`tableId`,`name`),
  UNIQUE KEY `uq_bsm_column_index_name` (`tableId`,`indexName`),
  KEY `fk_bsm_column_seq_id` (`seqId`),
  CONSTRAINT `fk_bsm_column_seq_id` FOREIGN KEY (`seqId`) REFERENCES `ca_meta_sequence` (`id`),
  CONSTRAINT `fk_bsm_column_table_id` FOREIGN KEY (`tableId`) REFERENCES `ca_meta_table` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of ca_meta_column
-- ----------------------------
BEGIN;
INSERT INTO `ca_meta_column` VALUES (1, b'1', '编号', '2020-12-23 17:06:29.211000', 'BIGINT', NULL, '编号', 0, NULL, NULL, 'PRIMARY', b'0', '2021-08-05 10:52:53.967000', 20, 'id', b'0', NULL, b'0', NULL, NULL, b'1', b'0', b'0', b'1', b'0', 1);
INSERT INTO `ca_meta_column` VALUES (2, b'0', '名称', '2020-12-23 17:06:29.211000', 'VARCHAR', NULL, '名称', 1, NULL, NULL, NULL, b'1', '2021-08-05 10:52:53.967000', 200, 'name', b'0', NULL, b'1', NULL, NULL, b'0', b'1', b'1', b'1', b'0', 1);
INSERT INTO `ca_meta_column` VALUES (3, b'0', '全文索引', '2020-12-23 17:06:29.211000', 'TEXT', NULL, '全文索引', 2, 'ft_fulltext_body', NULL, 'FULLTEXT', b'0', '2021-08-05 10:52:53.967000', NULL, 'fullTextBody', b'1', NULL, b'0', NULL, NULL, b'0', b'0', b'0', b'1', b'0', 1);
INSERT INTO `ca_meta_column` VALUES (4, b'0', '创建时间', '2020-12-23 17:06:29.211000', 'DATETIME', NULL, '创建时间', 3, NULL, NULL, NULL, b'0', '2021-08-05 10:52:53.967000', NULL, 'createdDate', b'0', NULL, b'0', NULL, NULL, b'0', b'0', b'0', b'1', b'0', 1);
INSERT INTO `ca_meta_column` VALUES (5, b'0', '修改时间', '2020-12-23 17:06:29.211000', 'DATETIME', NULL, '修改时间', 4, NULL, NULL, NULL, b'0', '2021-08-05 10:52:53.967000', NULL, 'lastModifiedDate', b'1', NULL, b'0', NULL, NULL, b'0', b'0', b'0', b'1', b'0', 1);
INSERT INTO `ca_meta_column` VALUES (6, b'0', 'OPENID', '2020-12-23 17:06:29.211000', 'VARCHAR', NULL, 'OPENID', 5, 'uq_spring_user_openid', NULL, 'UNIQUE', b'1', '2021-08-05 10:52:53.967000', 50, 'openId', b'1', NULL, b'0', NULL, NULL, b'0', b'1', b'0', b'0', b'0', 1);
INSERT INTO `ca_meta_column` VALUES (7, b'0', '用户名', '2020-12-23 17:06:29.211000', 'VARCHAR', NULL, '用户名', 6, 'uq_spring_user_username', NULL, 'UNIQUE', b'1', '2021-08-05 10:52:53.967000', 50, 'username', b'0', NULL, b'1', NULL, NULL, b'0', b'1', b'0', b'0', b'0', 1);
INSERT INTO `ca_meta_column` VALUES (8, b'0', '真实姓名', '2020-12-23 17:06:29.211000', 'VARCHAR', NULL, '真实姓名', 7, NULL, NULL, NULL, b'1', '2021-08-05 10:52:53.967000', 200, 'realname', b'1', NULL, b'1', NULL, NULL, b'0', b'1', b'0', b'0', b'0', 1);
INSERT INTO `ca_meta_column` VALUES (9, b'0', '手机', '2020-12-23 17:06:29.211000', 'VARCHAR', NULL, '手机', 8, 'uq_spring_user_mobile', NULL, 'UNIQUE', b'1', '2021-08-05 10:52:53.967000', 20, 'mobile', b'1', NULL, b'1', NULL, NULL, b'0', b'1', b'0', b'0', b'0', 1);
INSERT INTO `ca_meta_column` VALUES (10, b'0', '邮箱', '2020-12-23 17:06:29.211000', 'VARCHAR', NULL, '邮箱', 9, 'uq_spring_user_email', NULL, 'UNIQUE', b'1', '2021-08-05 10:52:53.967000', 191, 'email', b'1', NULL, b'1', NULL, NULL, b'0', b'1', b'0', b'0', b'0', 1);
INSERT INTO `ca_meta_column` VALUES (11, b'0', '密码', '2020-12-23 17:06:29.211000', 'PASSWORD', NULL, '密码', 10, NULL, NULL, NULL, b'1', '2021-08-05 10:52:53.967000', 500, 'password', b'0', NULL, b'0', NULL, NULL, b'0', b'1', b'0', b'0', b'0', 1);
INSERT INTO `ca_meta_column` VALUES (12, b'0', '明文密码', '2020-12-23 17:06:29.211000', 'VARCHAR', NULL, '明文密码', 11, NULL, NULL, NULL, b'1', '2021-08-05 10:52:53.967000', 200, 'cleartextPwd', b'1', NULL, b'0', NULL, NULL, b'0', b'1', b'0', b'0', b'0', 1);
INSERT INTO `ca_meta_column` VALUES (13, b'0', 'TOKEN', '2020-12-23 17:06:29.211000', 'VARCHAR', NULL, 'TOKEN', 12, NULL, NULL, NULL, b'1', '2021-08-05 10:52:53.967000', 200, 'token', b'1', NULL, b'0', NULL, NULL, b'0', b'1', b'0', b'0', b'0', 1);
INSERT INTO `ca_meta_column` VALUES (14, b'0', '启用', '2020-12-23 17:06:29.211000', 'BOOL', 'true', '启用', 13, NULL, NULL, NULL, b'1', '2021-08-05 10:52:53.967000', 10, 'enabled', b'0', NULL, b'1', NULL, NULL, b'0', b'1', b'0', b'0', b'0', 1);
INSERT INTO `ca_meta_column` VALUES (15, b'0', '账号没有过期', '2020-12-23 17:06:29.211000', 'BOOL', 'true', '账号没有过期', 14, NULL, NULL, NULL, b'1', '2021-08-05 10:52:53.967000', 10, 'accountNonExpired', b'0', NULL, b'1', NULL, NULL, b'0', b'1', b'0', b'0', b'0', 1);
INSERT INTO `ca_meta_column` VALUES (16, b'0', '账号没有锁定', '2020-12-23 17:06:29.211000', 'BOOL', 'true', '账号没有锁定', 15, NULL, NULL, NULL, b'1', '2021-08-05 10:52:53.967000', 10, 'accountNonLocked', b'0', NULL, b'1', NULL, NULL, b'0', b'1', b'0', b'0', b'0', 1);
INSERT INTO `ca_meta_column` VALUES (17, b'0', '凭证没有过期', '2020-12-23 17:06:29.211000', 'BOOL', 'true', '凭证没有过期', 16, NULL, NULL, NULL, b'1', '2021-08-05 10:52:53.967000', 10, 'credentialsNonExpired', b'0', NULL, b'1', NULL, NULL, b'0', b'1', b'0', b'0', b'0', 1);
INSERT INTO `ca_meta_column` VALUES (20, b'1', '编号', '2021-02-01 10:51:21.334000', 'BIGINT', NULL, '主键', 0, NULL, NULL, 'PRIMARY', b'0', '2021-08-05 10:53:53.431000', 20, 'id', b'0', NULL, b'0', NULL, NULL, b'1', b'0', b'0', b'1', b'0', 2);
INSERT INTO `ca_meta_column` VALUES (21, b'0', '名称', '2021-02-01 10:51:21.334000', 'VARCHAR', NULL, '名称', 1, NULL, NULL, NULL, b'1', '2021-08-05 10:53:53.431000', 200, 'name', b'0', NULL, b'1', NULL, NULL, b'0', b'1', b'1', b'1', b'0', 2);
INSERT INTO `ca_meta_column` VALUES (22, b'0', '全文索引', '2021-02-01 10:51:21.334000', 'TEXT', NULL, '全文索引', 2, 'ft_fulltext_body', NULL, 'FULLTEXT', b'0', '2021-08-05 10:53:53.431000', NULL, 'fullTextBody', b'1', NULL, b'0', NULL, NULL, b'0', b'0', b'0', b'1', b'0', 2);
INSERT INTO `ca_meta_column` VALUES (23, b'0', '创建时间', '2021-02-01 10:51:21.334000', 'DATETIME', NULL, '创建时间', 3, NULL, NULL, NULL, b'0', '2021-08-05 10:53:53.431000', NULL, 'createdDate', b'0', NULL, b'0', NULL, NULL, b'0', b'0', b'0', b'1', b'0', 2);
INSERT INTO `ca_meta_column` VALUES (24, b'0', '修改时间', '2021-02-01 10:51:21.334000', 'DATETIME', NULL, '修改时间', 4, NULL, NULL, NULL, b'0', '2021-08-05 10:53:53.431000', NULL, 'lastModifiedDate', b'1', NULL, b'0', NULL, NULL, b'0', b'0', b'0', b'1', b'0', 2);
INSERT INTO `ca_meta_column` VALUES (25, b'0', '编码', '2021-02-01 10:51:21.334000', 'VARCHAR', NULL, '编码', 5, 'uq_spring_role_code', NULL, 'UNIQUE', b'1', '2021-08-05 10:53:53.431000', 191, 'code', b'0', NULL, b'1', NULL, 1, b'0', b'1', b'0', b'0', b'0', 2);
INSERT INTO `ca_meta_column` VALUES (26, b'0', '备注', '2021-02-01 10:51:21.334000', 'TEXT', NULL, '备注', 6, NULL, NULL, NULL, b'1', '2021-08-05 10:53:53.431000', NULL, 'remark', b'1', NULL, b'1', NULL, NULL, b'0', b'1', b'0', b'0', b'0', 2);
INSERT INTO `ca_meta_column` VALUES (27, b'1', '编号', '2021-02-01 10:54:05.671000', 'BIGINT', NULL, '主键', 0, NULL, NULL, 'PRIMARY', b'0', '2021-02-05 17:52:19.036000', 20, 'id', b'0', NULL, b'0', NULL, NULL, b'1', b'0', NULL, b'1', b'0', 3);
INSERT INTO `ca_meta_column` VALUES (28, b'0', '名称', '2021-02-01 10:54:05.671000', 'VARCHAR', NULL, '名称', 1, NULL, NULL, NULL, b'1', '2021-02-05 17:52:19.036000', 200, 'name', b'0', NULL, b'1', NULL, NULL, b'0', b'1', b'1', b'1', b'0', 3);
INSERT INTO `ca_meta_column` VALUES (29, b'0', '全文索引', '2021-02-01 10:54:05.671000', 'TEXT', NULL, '全文索引', 2, 'ft_fulltext_body', NULL, 'FULLTEXT', b'0', '2021-02-05 17:52:19.036000', NULL, 'fullTextBody', b'1', NULL, b'0', NULL, NULL, b'0', b'0', NULL, b'1', b'0', 3);
INSERT INTO `ca_meta_column` VALUES (30, b'0', '创建时间', '2021-02-01 10:54:05.671000', 'DATETIME', NULL, '创建时间', 3, NULL, NULL, NULL, b'0', '2021-02-05 17:52:19.036000', NULL, 'createdDate', b'0', NULL, b'0', NULL, NULL, b'0', b'0', NULL, b'1', b'0', 3);
INSERT INTO `ca_meta_column` VALUES (31, b'0', '修改时间', '2021-02-01 10:54:05.671000', 'DATETIME', NULL, '修改时间', 4, NULL, NULL, NULL, b'0', '2021-02-05 17:52:19.036000', NULL, 'lastModifiedDate', b'1', NULL, b'0', NULL, NULL, b'0', b'0', NULL, b'1', b'0', 3);
INSERT INTO `ca_meta_column` VALUES (33, b'0', 'URL表达式', '2021-02-01 10:54:05.671000', 'VARCHAR', NULL, 'URL表达式', 6, 'uq_spring_resource_url', NULL, 'UNIQUE', b'1', '2021-02-05 17:52:19.036000', 191, 'url', b'1', NULL, b'1', NULL, NULL, b'0', b'1', NULL, b'0', b'0', 3);
INSERT INTO `ca_meta_column` VALUES (34, b'0', '备注', '2021-02-01 10:54:05.671000', 'TEXT', NULL, '备注', 8, NULL, NULL, NULL, b'1', '2021-02-05 17:52:19.036000', NULL, 'remark', b'1', NULL, b'1', NULL, NULL, b'0', b'1', NULL, b'0', b'0', 3);
INSERT INTO `ca_meta_column` VALUES (35, b'1', '编号', '2021-02-01 11:11:15.313000', 'BIGINT', NULL, '主键', 0, NULL, NULL, 'PRIMARY', b'0', '2021-02-01 11:11:15.313000', 20, 'id', b'0', NULL, b'0', NULL, NULL, b'1', b'0', NULL, b'1', b'0', 4);
INSERT INTO `ca_meta_column` VALUES (36, b'0', '名称', '2021-02-01 11:11:15.313000', 'VARCHAR', NULL, '名称', 1, NULL, NULL, NULL, b'1', '2021-02-01 11:11:15.313000', 200, 'name', b'0', NULL, b'1', NULL, NULL, b'0', b'1', b'1', b'1', b'0', 4);
INSERT INTO `ca_meta_column` VALUES (37, b'0', '全文索引', '2021-02-01 11:11:15.313000', 'TEXT', NULL, '全文索引', 2, 'ft_fulltext_body', NULL, 'FULLTEXT', b'0', '2021-02-01 11:11:15.313000', NULL, 'fullTextBody', b'1', NULL, b'0', NULL, NULL, b'0', b'0', NULL, b'1', b'0', 4);
INSERT INTO `ca_meta_column` VALUES (38, b'0', '创建时间', '2021-02-01 11:11:15.313000', 'DATETIME', NULL, '创建时间', 3, NULL, NULL, NULL, b'0', '2021-02-01 11:11:15.313000', NULL, 'createdDate', b'0', NULL, b'0', NULL, NULL, b'0', b'0', NULL, b'1', b'0', 4);
INSERT INTO `ca_meta_column` VALUES (39, b'0', '修改时间', '2021-02-01 11:11:15.313000', 'DATETIME', NULL, '修改时间', 4, NULL, NULL, NULL, b'0', '2021-02-01 11:11:15.313000', NULL, 'lastModifiedDate', b'1', NULL, b'0', NULL, NULL, b'0', b'0', NULL, b'1', b'0', 4);
INSERT INTO `ca_meta_column` VALUES (40, b'0', '用户编号', '2021-02-01 11:11:15.313000', 'BIGINT', NULL, '用户编号', 5, NULL, NULL, NULL, b'1', '2021-02-01 11:11:15.313000', 20, 'userId', b'0', NULL, b'1', NULL, NULL, b'0', b'1', NULL, b'0', b'0', 4);
INSERT INTO `ca_meta_column` VALUES (41, b'0', '角色编号', '2021-02-01 11:11:15.313000', 'BIGINT', NULL, '角色编号', 6, NULL, NULL, NULL, b'1', '2021-02-01 11:11:15.313000', 20, 'roleId', b'0', NULL, b'1', NULL, NULL, b'0', b'1', NULL, b'0', b'0', 4);
INSERT INTO `ca_meta_column` VALUES (42, b'1', '编号', '2021-02-01 11:27:24.851000', 'BIGINT', NULL, '主键', 0, NULL, NULL, 'PRIMARY', b'0', '2021-02-03 16:43:36.326000', 20, 'id', b'0', NULL, b'0', NULL, NULL, b'1', b'0', NULL, b'1', b'0', 5);
INSERT INTO `ca_meta_column` VALUES (43, b'0', '名称', '2021-02-01 11:27:24.851000', 'VARCHAR', NULL, '名称', 1, NULL, NULL, NULL, b'1', '2021-02-03 16:43:36.326000', 200, 'name', b'0', NULL, b'1', NULL, NULL, b'0', b'1', b'1', b'1', b'0', 5);
INSERT INTO `ca_meta_column` VALUES (44, b'0', '全文索引', '2021-02-01 11:27:24.851000', 'TEXT', NULL, '全文索引', 2, 'ft_fulltext_body', NULL, 'FULLTEXT', b'0', '2021-02-03 16:43:36.326000', NULL, 'fullTextBody', b'1', NULL, b'0', NULL, NULL, b'0', b'0', NULL, b'1', b'0', 5);
INSERT INTO `ca_meta_column` VALUES (45, b'0', '创建时间', '2021-02-01 11:27:24.851000', 'DATETIME', NULL, '创建时间', 3, NULL, NULL, NULL, b'0', '2021-02-03 16:43:36.326000', NULL, 'createdDate', b'0', NULL, b'0', NULL, NULL, b'0', b'0', NULL, b'1', b'0', 5);
INSERT INTO `ca_meta_column` VALUES (46, b'0', '修改时间', '2021-02-01 11:27:24.851000', 'DATETIME', NULL, '修改时间', 4, NULL, NULL, NULL, b'0', '2021-02-03 16:43:36.326000', NULL, 'lastModifiedDate', b'1', NULL, b'0', NULL, NULL, b'0', b'0', NULL, b'1', b'0', 5);
INSERT INTO `ca_meta_column` VALUES (47, b'0', '角色编号', '2021-02-01 11:27:24.851000', 'BIGINT', NULL, '角色编号', 5, NULL, NULL, NULL, b'1', '2021-02-03 16:43:36.326000', 20, 'roleId', b'1', NULL, b'1', NULL, NULL, b'0', b'1', NULL, b'0', b'0', 5);
INSERT INTO `ca_meta_column` VALUES (48, b'0', '资源编号', '2021-02-01 11:27:24.851000', 'BIGINT', NULL, '资源编号', 6, NULL, NULL, NULL, b'1', '2021-02-03 16:43:36.326000', 20, 'resourceId', b'1', NULL, b'1', NULL, NULL, b'0', b'1', NULL, b'0', b'0', 5);
INSERT INTO `ca_meta_column` VALUES (56, b'0', '编码', '2021-02-02 10:06:53.176000', 'VARCHAR', NULL, '编码', 5, 'uq_spring_resource_code', NULL, 'UNIQUE', b'1', '2021-02-05 17:52:19.036000', 191, 'code', b'1', NULL, b'1', NULL, 2, b'0', b'1', NULL, b'0', b'0', 3);
INSERT INTO `ca_meta_column` VALUES (58, b'0', '操作', '2021-02-03 16:44:10.071000', 'VARCHAR', NULL, '操作', 7, NULL, NULL, NULL, b'1', '2021-02-05 17:52:19.036000', 200, 'action', b'1', NULL, b'1', NULL, NULL, b'0', b'1', NULL, b'0', b'0', 3);
INSERT INTO `ca_meta_column` VALUES (155, b'1', '编号', '2021-02-07 11:57:10.131000', 'BIGINT', NULL, '主键', 0, NULL, NULL, 'PRIMARY', b'0', '2021-02-07 15:25:57.167000', 20, 'id', b'0', NULL, b'0', NULL, NULL, b'1', b'0', NULL, b'1', b'0', 19);
INSERT INTO `ca_meta_column` VALUES (156, b'0', '名称', '2021-02-07 11:57:10.131000', 'VARCHAR', NULL, '名称', 1, NULL, NULL, NULL, b'1', '2021-02-07 15:25:57.167000', 200, 'name', b'0', NULL, b'1', NULL, NULL, b'0', b'1', b'1', b'1', b'0', 19);
INSERT INTO `ca_meta_column` VALUES (157, b'0', '全文索引', '2021-02-07 11:57:10.131000', 'TEXT', NULL, '全文索引', 2, 'ft_fulltext_body', NULL, 'FULLTEXT', b'0', '2021-02-07 15:25:57.167000', NULL, 'fullTextBody', b'1', NULL, b'0', NULL, NULL, b'0', b'0', NULL, b'1', b'0', 19);
INSERT INTO `ca_meta_column` VALUES (158, b'0', '创建时间', '2021-02-07 11:57:10.131000', 'DATETIME', NULL, '创建时间', 3, NULL, NULL, NULL, b'0', '2021-02-07 15:25:57.167000', NULL, 'createdDate', b'0', NULL, b'0', NULL, NULL, b'0', b'0', NULL, b'1', b'0', 19);
INSERT INTO `ca_meta_column` VALUES (159, b'0', '修改时间', '2021-02-07 11:57:10.131000', 'DATETIME', NULL, '修改时间', 4, NULL, NULL, NULL, b'0', '2021-02-07 15:25:57.167000', NULL, 'lastModifiedDate', b'1', NULL, b'0', NULL, NULL, b'0', b'0', NULL, b'1', b'0', 19);
INSERT INTO `ca_meta_column` VALUES (160, b'0', '编码', '2021-02-07 11:57:10.131000', 'VARCHAR', NULL, '编码', 5, NULL, NULL, NULL, b'1', '2021-02-07 15:25:57.167000', 200, 'code', b'1', NULL, b'1', NULL, NULL, b'0', b'1', NULL, b'0', b'0', 19);
INSERT INTO `ca_meta_column` VALUES (161, b'1', '编号', '2021-02-07 11:59:06.948000', 'BIGINT', NULL, '主键', 0, NULL, NULL, 'PRIMARY', b'0', '2021-02-08 16:21:31.011000', 20, 'id', b'0', NULL, b'0', NULL, NULL, b'1', b'0', NULL, b'1', b'0', 20);
INSERT INTO `ca_meta_column` VALUES (162, b'0', '名称', '2021-02-07 11:59:06.948000', 'VARCHAR', NULL, '名称', 1, NULL, NULL, NULL, b'1', '2021-02-08 16:21:31.011000', 200, 'name', b'0', NULL, b'1', NULL, NULL, b'0', b'1', b'1', b'1', b'0', 20);
INSERT INTO `ca_meta_column` VALUES (163, b'0', '全文索引', '2021-02-07 11:59:06.948000', 'TEXT', NULL, '全文索引', 2, 'ft_fulltext_body', NULL, 'FULLTEXT', b'0', '2021-02-08 16:21:31.011000', NULL, 'fullTextBody', b'1', NULL, b'0', NULL, NULL, b'0', b'0', NULL, b'1', b'0', 20);
INSERT INTO `ca_meta_column` VALUES (164, b'0', '创建时间', '2021-02-07 11:59:06.948000', 'DATETIME', NULL, '创建时间', 3, NULL, NULL, NULL, b'0', '2021-02-08 16:21:31.011000', NULL, 'createdDate', b'0', NULL, b'0', NULL, NULL, b'0', b'0', NULL, b'1', b'0', 20);
INSERT INTO `ca_meta_column` VALUES (165, b'0', '修改时间', '2021-02-07 11:59:06.948000', 'DATETIME', NULL, '修改时间', 4, NULL, NULL, NULL, b'0', '2021-02-08 16:21:31.011000', NULL, 'lastModifiedDate', b'1', NULL, b'0', NULL, NULL, b'0', b'0', NULL, b'1', b'0', 20);
INSERT INTO `ca_meta_column` VALUES (166, b'0', '编码', '2021-02-07 11:59:06.948000', 'VARCHAR', NULL, '编码', 5, NULL, NULL, NULL, b'1', '2021-02-08 16:21:31.011000', 200, 'code', b'1', NULL, b'1', NULL, NULL, b'0', b'1', NULL, b'0', b'0', 20);
INSERT INTO `ca_meta_column` VALUES (167, b'0', '省编号', '2021-02-07 11:59:06.948000', 'BIGINT', NULL, '省编号', 6, NULL, NULL, NULL, b'1', '2021-02-08 16:21:31.011000', 20, 'provinceId', b'1', NULL, b'1', NULL, NULL, b'0', b'1', NULL, b'0', b'0', 20);
INSERT INTO `ca_meta_column` VALUES (176, b'1', '编号', '2021-02-07 15:25:23.718000', 'BIGINT', NULL, '主键', 0, NULL, NULL, 'PRIMARY', b'0', '2021-08-05 10:38:57.087000', 20, 'id', b'0', NULL, b'0', NULL, NULL, b'1', b'0', b'0', b'1', b'0', 22);
INSERT INTO `ca_meta_column` VALUES (177, b'0', '名称', '2021-02-07 15:25:23.718000', 'VARCHAR', NULL, '名称', 1, NULL, NULL, NULL, b'1', '2021-08-05 10:38:57.087000', 200, 'name', b'0', NULL, b'1', NULL, NULL, b'0', b'1', b'1', b'1', b'0', 22);
INSERT INTO `ca_meta_column` VALUES (178, b'0', '全文索引', '2021-02-07 15:25:23.718000', 'TEXT', NULL, '全文索引', 2, 'ft_fulltext_body', NULL, 'FULLTEXT', b'0', '2021-08-05 10:38:57.087000', NULL, 'fullTextBody', b'1', NULL, b'0', NULL, NULL, b'0', b'0', b'0', b'1', b'0', 22);
INSERT INTO `ca_meta_column` VALUES (179, b'0', '创建时间', '2021-02-07 15:25:23.718000', 'DATETIME', NULL, '创建时间', 3, NULL, NULL, NULL, b'0', '2021-08-05 10:38:57.087000', NULL, 'createdDate', b'0', NULL, b'0', NULL, NULL, b'0', b'0', b'0', b'1', b'0', 22);
INSERT INTO `ca_meta_column` VALUES (180, b'0', '修改时间', '2021-02-07 15:25:23.718000', 'DATETIME', NULL, '修改时间', 4, NULL, NULL, NULL, b'0', '2021-08-05 10:38:57.087000', NULL, 'lastModifiedDate', b'1', NULL, b'0', NULL, NULL, b'0', b'0', b'0', b'1', b'0', 22);
INSERT INTO `ca_meta_column` VALUES (181, b'0', '编码', '2021-02-07 15:25:23.718000', 'VARCHAR', NULL, '编码', 5, NULL, NULL, NULL, b'1', '2021-08-05 10:38:57.087000', 200, 'code', b'1', NULL, b'1', NULL, NULL, b'0', b'1', b'0', b'0', b'0', 22);
INSERT INTO `ca_meta_column` VALUES (182, b'0', '市编号', '2021-02-07 15:25:23.718000', 'BIGINT', NULL, '市编号', 6, NULL, NULL, NULL, b'1', '2021-08-05 10:38:57.087000', 20, 'cityId', b'1', NULL, b'1', NULL, NULL, b'0', b'1', b'0', b'0', b'0', 22);
INSERT INTO `ca_meta_column` VALUES (219, b'0', '头像文件编号', '2021-02-08 16:27:37.689000', 'BIGINT', NULL, '头像文件编号', 17, NULL, NULL, NULL, b'1', '2021-08-05 10:52:53.967000', 20, 'fileId', b'1', NULL, b'1', NULL, NULL, b'0', b'1', b'0', b'0', b'0', 1);
COMMIT;

-- ----------------------------
-- Table structure for ca_meta_index
-- ----------------------------
DROP TABLE IF EXISTS `ca_meta_index`;
CREATE TABLE `ca_meta_index` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdDate` datetime(6) DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `indexStorage` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `indexType` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastModifiedDate` datetime(6) DEFAULT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tableId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_bsm_index_name` (`tableId`,`name`),
  CONSTRAINT `fk_bsm_index_table_id` FOREIGN KEY (`tableId`) REFERENCES `ca_meta_table` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for ca_meta_index_line
-- ----------------------------
DROP TABLE IF EXISTS `ca_meta_index_line`;
CREATE TABLE `ca_meta_index_line` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `columnId` bigint(20) DEFAULT NULL,
  `indexId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_bsm_index_line_column_id` (`columnId`),
  KEY `fk_bsm_index_line_index_id` (`indexId`),
  CONSTRAINT `fk_bsm_index_line_column_id` FOREIGN KEY (`columnId`) REFERENCES `ca_meta_column` (`id`),
  CONSTRAINT `fk_bsm_index_line_index_id` FOREIGN KEY (`indexId`) REFERENCES `ca_meta_index` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for ca_meta_sequence
-- ----------------------------
DROP TABLE IF EXISTS `ca_meta_sequence`;
CREATE TABLE `ca_meta_sequence` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdDate` datetime(6) DEFAULT NULL,
  `currentTime` bit(1) DEFAULT NULL,
  `cycle` bit(1) DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `format` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `incrementBy` bigint(20) DEFAULT NULL,
  `lastModifiedDate` datetime(6) DEFAULT NULL,
  `maxValue` bigint(20) DEFAULT NULL,
  `minValue` bigint(20) DEFAULT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nextValue` bigint(20) DEFAULT NULL,
  `sequenceType` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_bsm_sequence_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of ca_meta_sequence
-- ----------------------------
BEGIN;
INSERT INTO `ca_meta_sequence` VALUES (1, '角色编码', '2021-02-01 11:17:34.807000', b'0', NULL, NULL, 'ROLE_%09d', 1, '2021-02-05 17:53:05.432000', 999999999, 1, 'roleCode', 8, 'STRING');
INSERT INTO `ca_meta_sequence` VALUES (2, '资源编码', '2021-02-02 10:06:15.140000', b'0', NULL, NULL, 'RESOURCE_%09d', 1, '2021-02-02 10:06:15.140000', 999999999, 1, 'resourceCode', 8, 'STRING');
INSERT INTO `ca_meta_sequence` VALUES (3, '会员流水号', '2021-07-25 21:24:48.973000', b'0', NULL, NULL, '%018d', 1, '2021-07-25 21:28:07.858000', 9999999999, 1, 'membershipCode', 17, 'STRING');
COMMIT;

-- ----------------------------
-- Table structure for ca_meta_table
-- ----------------------------
DROP TABLE IF EXISTS `ca_meta_table`;
CREATE TABLE `ca_meta_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createPhysicalTable` bit(1) DEFAULT NULL,
  `createdDate` datetime(6) DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `engine` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastModifiedDate` datetime(6) DEFAULT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pluralName` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tableName` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `systemable` bit(1) DEFAULT NULL,
  `readOnly` bit(1) DEFAULT NULL COMMENT '是否只读',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_bsm_table_name` (`name`),
  UNIQUE KEY `uq_bsm_table_plural_name` (`pluralName`),
  UNIQUE KEY `uq_bsm_table_table_name` (`tableName`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of ca_meta_table
-- ----------------------------
BEGIN;
INSERT INTO `ca_meta_table` VALUES (1, '用户', b'1', '2020-12-23 17:06:29.161000', '用户', 'INNODB', '2021-08-05 10:52:53.955000', 'user', 'users', 'spring_user', b'1', b'0');
INSERT INTO `ca_meta_table` VALUES (2, '角色', b'1', '2021-02-01 10:51:21.324000', '角色', 'INNODB', '2021-08-05 10:53:53.426000', 'role', 'roles', 'spring_role', b'1', b'0');
INSERT INTO `ca_meta_table` VALUES (3, '资源', b'1', '2021-02-01 10:54:05.668000', '资源', 'INNODB', '2021-02-05 17:52:19.025000', 'resource', 'resources', 'spring_resource', b'1', b'0');
INSERT INTO `ca_meta_table` VALUES (4, '用户角色行', b'1', '2021-02-01 11:11:15.312000', '用户角色行', 'INNODB', '2021-02-01 11:11:15.312000', 'userRoleLine', 'userRoleLines', 'ca_userRoleLine', b'1', b'0');
INSERT INTO `ca_meta_table` VALUES (5, '角色资源行', b'1', '2021-02-01 11:27:24.848000', '角色资源行', 'INNODB', '2021-02-03 16:43:36.315000', 'roleResourceLine', 'roleResourceLines', 'ca_roleResourceLine', b'1', b'0');
INSERT INTO `ca_meta_table` VALUES (19, '省', b'1', '2021-02-07 11:57:10.121000', '', 'INNODB', '2021-02-07 15:25:57.159000', 'province', 'provinces', 'ca_province', b'1', b'0');
INSERT INTO `ca_meta_table` VALUES (20, '市', b'1', '2021-02-07 11:59:06.946000', '', 'INNODB', '2021-02-08 16:21:31.003000', 'city', 'cities', 'ca_city', b'1', b'0');
INSERT INTO `ca_meta_table` VALUES (22, '区', b'1', '2021-02-07 15:25:23.709000', '', 'INNODB', '2021-08-05 10:38:57.082000', 'district', 'districts', 'ca_district', b'1', b'0');
COMMIT;

-- ----------------------------
-- Table structure for ca_meta_table_relation
-- ----------------------------
DROP TABLE IF EXISTS `ca_meta_table_relation`;
CREATE TABLE `ca_meta_table_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdDate` datetime(6) DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastModifiedDate` datetime(6) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `relationType` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fromColumnId` bigint(20) DEFAULT NULL,
  `fromTableId` bigint(20) DEFAULT NULL,
  `toColumnId` bigint(20) DEFAULT NULL,
  `toTableId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_ca_table_relation` (`fromTableId`,`toTableId`,`relationType`,`fromColumnId`,`toColumnId`) USING BTREE,
  KEY `fk_bsm_table_relation_from_table_id` (`fromTableId`),
  KEY `fk_bsm_table_relation_to_table_id` (`toTableId`),
  KEY `fk_bsm_table_relation_from_column_id` (`fromColumnId`),
  KEY `fk_bsm_table_relation_to_column_id` (`toColumnId`),
  CONSTRAINT `fk_bsm_table_relation_from_column_id` FOREIGN KEY (`fromColumnId`) REFERENCES `ca_meta_column` (`id`),
  CONSTRAINT `fk_bsm_table_relation_from_table_id` FOREIGN KEY (`fromTableId`) REFERENCES `ca_meta_table` (`id`),
  CONSTRAINT `fk_bsm_table_relation_to_column_id` FOREIGN KEY (`toColumnId`) REFERENCES `ca_meta_column` (`id`),
  CONSTRAINT `fk_bsm_table_relation_to_table_id` FOREIGN KEY (`toTableId`) REFERENCES `ca_meta_table` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of ca_meta_table_relation
-- ----------------------------
BEGIN;
INSERT INTO `ca_meta_table_relation` VALUES (1, '角色行', '2021-02-01 11:11:44.048000', '角色行', '2021-02-01 11:13:05.487000', 'roleLines', 'OneToMany', 1, 1, 40, 4);
INSERT INTO `ca_meta_table_relation` VALUES (2, '角色', '2021-02-01 11:12:22.421000', '角色', '2021-02-01 11:12:22.421000', 'role', 'ManyToOne', 41, 4, 20, 2);
INSERT INTO `ca_meta_table_relation` VALUES (3, '资源行', '2021-02-01 11:29:19.889000', '资源行', '2021-02-01 11:30:11.780000', 'resourceLines', 'OneToMany', 20, 2, 47, 5);
INSERT INTO `ca_meta_table_relation` VALUES (4, '资源', '2021-02-01 11:30:41.473000', '资源', '2021-02-01 11:30:41.473000', 'resource', 'ManyToOne', 48, 5, 27, 3);
INSERT INTO `ca_meta_table_relation` VALUES (5, '城市', '2021-02-07 11:59:30.554000', NULL, '2021-02-07 11:59:30.554000', 'cities', 'OneToMany', 155, 19, 167, 20);
INSERT INTO `ca_meta_table_relation` VALUES (6, '区', '2021-02-07 15:26:36.647000', NULL, '2021-02-07 15:26:36.647000', 'districts', 'OneToMany', 161, 20, 182, 22);
COMMIT;

-- ----------------------------
-- Table structure for ca_province
-- ----------------------------
DROP TABLE IF EXISTS `ca_province`;
CREATE TABLE `ca_province` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fullTextBody` text COLLATE utf8mb4_unicode_ci,
  `createdDate` datetime NOT NULL,
  `lastModifiedDate` datetime DEFAULT NULL,
  `code` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  FULLTEXT KEY `ft_fulltext_body` (`fullTextBody`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for ca_roleResourceLine
-- ----------------------------
DROP TABLE IF EXISTS `ca_roleResourceLine`;
CREATE TABLE `ca_roleResourceLine` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fullTextBody` text COLLATE utf8mb4_unicode_ci,
  `createdDate` datetime NOT NULL,
  `lastModifiedDate` datetime DEFAULT NULL,
  `roleId` bigint(20) DEFAULT NULL,
  `resourceId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FULLTEXT KEY `ft_fulltext_body` (`fullTextBody`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of ca_roleResourceLine
-- ----------------------------
BEGIN;
INSERT INTO `ca_roleResourceLine` VALUES (11, '业务数据资源', NULL, '2021-07-27 16:57:16', NULL, 20, 12);
INSERT INTO `ca_roleResourceLine` VALUES (12, '业务数据数字资源', NULL, '2021-07-27 16:57:16', NULL, 20, 13);
COMMIT;

-- ----------------------------
-- Table structure for ca_userRoleLine
-- ----------------------------
DROP TABLE IF EXISTS `ca_userRoleLine`;
CREATE TABLE `ca_userRoleLine` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fullTextBody` text COLLATE utf8mb4_unicode_ci,
  `createdDate` datetime NOT NULL,
  `lastModifiedDate` datetime DEFAULT NULL,
  `userId` bigint(20) NOT NULL,
  `roleId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  FULLTEXT KEY `ft_fulltext_body` (`fullTextBody`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of ca_userRoleLine
-- ----------------------------
BEGIN;
INSERT INTO `ca_userRoleLine` VALUES (15, '超级管理员', NULL, '2021-07-27 15:53:21', NULL, 1, 11);
COMMIT;

-- ----------------------------
-- Table structure for persistent_logins
-- ----------------------------
DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins` (
  `username` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `series` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_used` timestamp NOT NULL,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for spring_resource
-- ----------------------------
DROP TABLE IF EXISTS `spring_resource`;
CREATE TABLE `spring_resource` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fullTextBody` text COLLATE utf8mb4_unicode_ci,
  `createdDate` datetime NOT NULL,
  `lastModifiedDate` datetime DEFAULT NULL,
  `url` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `remark` text COLLATE utf8mb4_unicode_ci,
  `code` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `action` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_spring_resource_code` (`code`),
  UNIQUE KEY `uq_spring_resource_url` (`url`),
  FULLTEXT KEY `ft_fulltext_body` (`fullTextBody`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of spring_resource
-- ----------------------------
BEGIN;
INSERT INTO `spring_resource` VALUES (12, '业务数据', '业务数据 RESOURCE_BUSINESS /api/business/** *', '2021-07-27 16:02:17', '2021-07-27 16:51:43', '/api/business/**', NULL, 'RESOURCE_BUSINESS', '*');
INSERT INTO `spring_resource` VALUES (13, '业务数据数字', '业务数据数字 RESOURCE_BUSINESS_COUNT /api/business/**/count *', '2021-07-27 16:52:29', '2021-07-27 16:52:41', '/api/business/**/count', NULL, 'RESOURCE_BUSINESS_COUNT', '*');
INSERT INTO `spring_resource` VALUES (14, '用户', '用户 RESOURCE_USER /api/business/user** *', '2021-07-27 16:58:59', '2021-07-27 17:02:03', '/api/business/user**', NULL, 'RESOURCE_USER', '*');
INSERT INTO `spring_resource` VALUES (15, '角色资源', '角色资源 RESOURCE_ROLE /api/business/role** *', '2021-07-27 17:26:46', '2021-07-27 17:27:24', '/api/business/role**', NULL, 'RESOURCE_ROLE', '*');
INSERT INTO `spring_resource` VALUES (16, '资源资源', '资源资源 RESOURCE_RESOURCE /api/business/resource** *', '2021-07-27 17:28:06', '2021-07-27 17:29:03', '/api/business/resource**', NULL, 'RESOURCE_RESOURCE', '*');
COMMIT;

-- ----------------------------
-- Table structure for spring_role
-- ----------------------------
DROP TABLE IF EXISTS `spring_role`;
CREATE TABLE `spring_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fullTextBody` text COLLATE utf8mb4_unicode_ci,
  `createdDate` datetime NOT NULL,
  `lastModifiedDate` datetime DEFAULT NULL,
  `code` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `remark` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_spring_role_code` (`code`),
  FULLTEXT KEY `ft_fulltext_body` (`fullTextBody`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of spring_role
-- ----------------------------
BEGIN;
INSERT INTO `spring_role` VALUES (11, '超级管理员', '超级管理员 ROLE_SUPER_ADMIN 超级管理员', '2021-02-01 13:28:37', '2021-06-07 16:50:45', 'ROLE_SUPER_ADMIN', '超级管理员');
INSERT INTO `spring_role` VALUES (20, '业务数据角色', '业务数据角色 ROLE_BUSINESS 业务数据用户', '2021-07-27 16:48:56', '2021-07-27 16:57:15', 'ROLE_BUSINESS', '业务数据用户');
COMMIT;

-- ----------------------------
-- Table structure for spring_user
-- ----------------------------
DROP TABLE IF EXISTS `spring_user`;
CREATE TABLE `spring_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `openId` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `realname` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mobile` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cleartextPwd` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `token` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `enabled` bit(1) NOT NULL DEFAULT b'1',
  `accountNonExpired` bit(1) NOT NULL DEFAULT b'1',
  `accountNonLocked` bit(1) NOT NULL DEFAULT b'1',
  `credentialsNonExpired` bit(1) NOT NULL DEFAULT b'1',
  `createdDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastModifiedDate` datetime DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fullTextBody` text COLLATE utf8mb4_unicode_ci,
  `fileId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_spring_user_username` (`username`),
  UNIQUE KEY `uq_spring_user_openid` (`openId`),
  UNIQUE KEY `uq_spring_user_mobile` (`mobile`),
  UNIQUE KEY `uq_spring_user_email` (`email`),
  FULLTEXT KEY `ft_fulltext_body` (`fullTextBody`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of spring_user
-- ----------------------------
BEGIN;
INSERT INTO `spring_user` VALUES (1, NULL, 'superadmin', '超级管理员', '11111111111', NULL, '$2a$10$R9HWotqpXzmLOU0aYlFkquxcs0jq7f1raKHHnsbfT51gM9TF.2q7q', NULL, '1094a2c5e57c4c7fa3922d69b012cfa7', b'1', b'1', b'1', b'1', '2020-07-21 15:00:24', '2021-07-27 15:53:21', '超级管理员', '超级管理员 superadmin 超级管理员 11111111111 true true true true', NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
