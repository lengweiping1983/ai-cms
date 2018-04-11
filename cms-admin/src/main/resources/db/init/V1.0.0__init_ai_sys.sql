/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50622
 Source Host           : 127.0.0.1
 Source Database       : ai_sys

 Target Server Type    : MySQL
 Target Server Version : 50622
 File Encoding         : utf-8

 Date: 03/28/2018 08:29:46 AM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `sys_dic`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dic`;
CREATE TABLE `sys_dic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `code` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sys_dic_item`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dic_item`;
CREATE TABLE `sys_dic_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `dic_id` bigint(20) DEFAULT NULL,
  `code` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `sort` int(11) NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_1xktvv5ahg4889wyt6yovagrk` (`dic_id`),
  CONSTRAINT `FK_1xktvv5ahg4889wyt6yovagrk` FOREIGN KEY (`dic_id`) REFERENCES `sys_dic` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `href` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `permission` varchar(255) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9ogfu2m8yudrxxnkpt17icdiv` (`parent_id`),
  CONSTRAINT `FK_9ogfu2m8yudrxxnkpt17icdiv` FOREIGN KEY (`parent_id`) REFERENCES `sys_menu` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1741 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_menu`
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES ('1', '2018-03-25 09:00:00', '2018-03-25 09:00:00', '/system/', 'icon-pin', '系统管理', null, null, '999', '1'), ('2', '2018-03-25 09:00:00', '2018-03-25 09:00:00', '/system/menu/', 'icon-book-open', '菜单管理', '1', null, '1', '1'), ('3', '2018-03-25 09:00:00', '2018-03-25 09:00:00', '/system/role/', 'icon-rocket', '角色管理', '1', null, '2', '1'), ('5', '2018-03-25 09:00:00', '2018-03-25 09:00:00', '/system/user/', 'icon-symbol-male', '用户管理', '1', null, '3', '1'), ('6', '2018-03-25 09:00:00', '2018-03-25 09:00:00', '/system/dic/', 'icon-control-end', '字典管理', '1', null, '4', '1'), ('7', '2018-03-25 09:00:00', '2018-03-25 09:00:00', null, null, '增加', '2', 'system:menu:add', '999', '2'), ('8', '2018-03-25 09:00:00', '2018-03-25 09:00:00', null, null, '删除', '2', 'system:menu:delete', '999', '2'), ('9', '2018-03-25 09:00:00', '2018-03-25 09:00:00', null, null, '修改', '2', 'system:menu:edit', '999', '2'), ('10', '2018-03-25 09:00:00', '2018-03-25 09:00:00', null, null, '查看', '2', 'system:menu:view', '999', '2'), ('11', '2018-03-25 09:00:00', '2018-03-25 09:00:00', null, null, '增加', '5', 'system:user:add', '999', '2'), ('12', '2018-03-25 09:00:00', '2018-03-25 09:00:00', null, null, '删除', '5', 'system:user:delete', '999', '2'), ('13', '2018-03-25 09:00:00', '2018-03-25 09:00:00', null, null, '修改', '5', 'system:user:edit', '999', '2'), ('14', '2018-03-25 09:00:00', '2018-03-25 09:00:00', null, null, '修改密码', '5', 'system:user:editPassword', '999', '2'), ('15', '2018-03-25 09:00:00', '2018-03-25 09:00:00', null, null, '查看', '5', 'system:user:view', '999', '2'), ('17', '2018-03-25 09:00:00', '2018-03-25 09:00:00', null, null, '增加', '3', 'system:role:add', '999', '2'), ('18', '2018-03-25 09:00:00', '2018-03-25 09:00:00', null, null, '删除', '3', 'system:role:delete', '999', '2'), ('19', '2018-03-25 09:00:00', '2018-03-25 09:00:00', null, null, '修改', '3', 'system:role:edit', '999', '2'), ('20', '2018-03-25 09:00:00', '2018-03-25 09:00:00', null, null, '查看', '3', 'system:role:view', '999', '2'), ('21', '2018-03-25 09:00:00', '2018-03-27 17:39:51', '/log', 'fa fa-commenting-o', '营运日志', null, null, '900', '1'), ('22', '2018-03-25 09:00:00', '2018-03-27 17:40:00', '/system/operationlog', 'fa fa-commenting', '操作行为日志', '21', null, '999', '1'), ('32', '2018-03-25 09:00:00', '2018-03-26 12:23:36', '/config', 'fa fa-genderless', '配置管理', null, null, '100', '1'), ('34', '2018-03-25 09:00:00', '2018-03-25 09:00:00', '/config/site', 'fa fa-gg', '渠道配置', '32', null, '20', '1'), ('35', '2018-03-25 09:00:00', '2018-03-27 17:39:34', '/config/cp', 'fa fa-gg-circle', '提供商配置', '32', null, '30', '1'), ('100', '2018-03-25 09:00:00', '2018-03-25 09:00:00', '/media', 'fa fa-btc', '媒资生产', null, null, '10', '1'), ('101', '2018-03-25 09:00:00', '2018-03-25 09:00:00', '/media/file', 'fa fa-cloud', '媒资文件浏览', '100', null, '10', '1'), ('102', '2018-03-25 09:00:00', '2018-03-25 09:00:00', '/transcode/transcodeRequest', 'fa fa-registered', '转码工单管理', '100', null, '20', '1'), ('103', '2018-03-25 09:00:00', '2018-03-25 09:00:00', '/transcode/transcodeTask', 'fa fa-chrome', '转码任务管理', '100', null, '30', '1'), ('200', '2018-03-25 09:00:00', '2018-03-25 09:00:00', '/media', 'fa fa-intersex', '媒资审核', null, null, '20', '1'), ('201', '2018-03-25 09:00:00', '2018-03-25 09:00:00', '/media/programAudit?search_auditStatus__IN_S=0,1,9', 'fa fa-mars', '节目审核', '200', null, '30', '1'), ('202', '2018-03-25 09:00:00', '2018-03-25 09:00:00', '/media/seriesAudit?search_auditStatus__IN_S=0,1,9', 'fa fa-mars-double', '剧头审核', '200', null, '40', '1'), ('300', '2018-03-25 09:00:00', '2018-03-27 17:35:39', '/media', 'fa fa-bank', '资媒管理', null, null, '30', '1'), ('301', '2018-03-25 09:00:00', '2018-03-27 17:34:38', '/media/program?search_auditStatus__IN_S=2', 'fa fa-cube', '节目管理', '300', null, '10', '1'), ('302', '2018-03-25 09:00:00', '2018-03-27 17:35:27', '/media/series?search_auditStatus__IN_S=2', 'fa fa-cubes', '剧头管理', '300', null, '30', '1'), ('400', '2018-03-25 09:00:00', '2018-03-27 17:37:11', '/media', 'fa fa-motorcycle', '媒资分发', null, null, '40', '1'), ('401', '2018-03-25 09:00:00', '2018-03-27 17:37:52', '/injection/platform', 'fa fa-plane', '分发平台配置', '400', null, '999', '1'), ('402', '2018-03-25 09:00:00', '2018-03-27 17:38:30', '/injection/sendTask', 'fa fa-cloud-upload', '分发任务查询', '400', null, '999', '1'), ('403', '2018-03-25 09:00:00', '2018-03-27 17:39:10', '/injection/receiveTask', 'fa fa-cloud-download', '接收任务查询', '400', null, '999', '1'), ('1619', '2018-03-25 09:00:00', null, null, null, '查看', '301', 'media:program:view', '999', '2'), ('1620', '2018-03-25 09:00:00', null, null, null, '增加', '301', 'media:program:add', '999', '2'), ('1621', '2018-03-25 09:00:00', null, null, null, '修改', '301', 'media:program:edit', '999', '2'), ('1622', '2018-03-25 09:00:00', null, null, null, '删除', '301', 'media:program:delete', '999', '2'), ('1623', '2018-03-25 09:00:00', null, null, null, '批量修改', '301', 'media:program:batchEdit', '999', '2'), ('1624', '2018-03-25 09:00:00', null, null, null, '批量删除', '301', 'media:program:batchDelete', '999', '2'), ('1625', '2018-03-25 09:00:00', null, null, null, '上线', '301', 'media:program:online', '999', '2'), ('1626', '2018-03-25 09:00:00', null, null, null, '下线', '301', 'media:program:offline', '999', '2'), ('1627', '2018-03-25 09:00:00', null, null, null, '批量上线', '301', 'media:program:batchOnline', '999', '2'), ('1628', '2018-03-25 09:00:00', null, null, null, '批量下线', '301', 'media:program:batchOffline', '999', '2'), ('1629', '2018-03-25 09:00:00', null, null, null, '批量导入', '301', 'media:program:batchImport', '999', '2'), ('1630', '2018-03-25 09:00:00', null, null, null, '批量导出', '301', 'media:program:batchExport', '999', '2'), ('1651', '2018-03-25 09:00:00', null, null, null, '查看', '201', 'media:programAudit:view', '999', '2'), ('1652', '2018-03-25 09:00:00', null, null, null, '增加', '201', 'media:programAudit:add', '999', '2'), ('1653', '2018-03-25 09:00:00', null, null, null, '修改', '201', 'media:programAudit:edit', '999', '2'), ('1654', '2018-03-25 09:00:00', null, null, null, '删除', '201', 'media:programAudit:delete', '999', '2'), ('1655', '2018-03-25 09:00:00', null, null, null, '批量修改', '201', 'media:programAudit:batchEdit', '999', '2'), ('1656', '2018-03-25 09:00:00', null, null, null, '批量删除', '201', 'media:programAudit:batchDelete', '999', '2'), ('1661', '2018-03-25 09:00:00', null, null, null, '批量导入', '201', 'media:programAudit:batchImport', '999', '2'), ('1662', '2018-03-25 09:00:00', null, null, null, '批量导出', '201', 'media:programAudit:batchExport', '999', '2'), ('1663', '2018-03-25 09:00:00', null, null, null, '送审', '201', 'media:programAudit:submit', '999', '2'), ('1664', '2018-03-25 09:00:00', null, null, null, '审核', '201', 'media:programAudit:audit', '999', '2'), ('1665', '2018-03-25 09:00:00', null, null, null, '批量送审', '201', 'media:programAudit:batchSubmit', '999', '2'), ('1666', '2018-03-25 09:00:00', null, null, null, '批量审核', '201', 'media:programAudit:batchAudit', '999', '2'), ('1683', '2018-03-25 09:00:00', null, null, null, '查看', '302', 'media:series:view', '999', '2'), ('1684', '2018-03-25 09:00:00', null, null, null, '增加', '302', 'media:series:add', '999', '2'), ('1685', '2018-03-25 09:00:00', null, null, null, '修改', '302', 'media:series:edit', '999', '2'), ('1686', '2018-03-25 09:00:00', null, null, null, '删除', '302', 'media:series:delete', '999', '2'), ('1687', '2018-03-25 09:00:00', null, null, null, '批量修改', '302', 'media:series:batchEdit', '999', '2'), ('1688', '2018-03-25 09:00:00', null, null, null, '批量删除', '302', 'media:series:batchDelete', '999', '2'), ('1689', '2018-03-25 09:00:00', null, null, null, '上线', '302', 'media:series:online', '999', '2'), ('1690', '2018-03-25 09:00:00', null, null, null, '下线', '302', 'media:series:offline', '999', '2'), ('1691', '2018-03-25 09:00:00', null, null, null, '批量上线', '302', 'media:series:batchOnline', '999', '2'), ('1692', '2018-03-25 09:00:00', null, null, null, '批量下线', '302', 'media:series:batchOffline', '999', '2'), ('1693', '2018-03-25 09:00:00', null, null, null, '批量导入', '302', 'media:series:batchImport', '999', '2'), ('1694', '2018-03-25 09:00:00', null, null, null, '批量导出', '302', 'media:series:batchExport', '999', '2'), ('1699', '2018-03-25 09:00:00', null, null, null, '查看', '202', 'media:seriesAudit:view', '999', '2'), ('1700', '2018-03-25 09:00:00', null, null, null, '增加', '202', 'media:seriesAudit:add', '999', '2'), ('1701', '2018-03-25 09:00:00', null, null, null, '修改', '202', 'media:seriesAudit:edit', '999', '2'), ('1702', '2018-03-25 09:00:00', null, null, null, '删除', '202', 'media:seriesAudit:delete', '999', '2'), ('1703', '2018-03-25 09:00:00', null, null, null, '批量修改', '202', 'media:seriesAudit:batchEdit', '999', '2'), ('1704', '2018-03-25 09:00:00', null, null, null, '批量删除', '202', 'media:seriesAudit:batchDelete', '999', '2'), ('1709', '2018-03-25 09:00:00', null, null, null, '批量导入', '202', 'media:seriesAudit:batchImport', '999', '2'), ('1710', '2018-03-25 09:00:00', null, null, null, '批量导出', '202', 'media:seriesAudit:batchExport', '999', '2'), ('1711', '2018-03-25 09:00:00', null, null, null, '送审', '202', 'media:seriesAudit:submit', '999', '2'), ('1712', '2018-03-25 09:00:00', null, null, null, '审核', '202', 'media:seriesAudit:audit', '999', '2'), ('1713', '2018-03-25 09:00:00', null, null, null, '批量送审', '202', 'media:seriesAudit:batchSubmit', '999', '2'), ('1714', '2018-03-25 09:00:00', null, null, null, '批量审核', '202', 'media:seriesAudit:batchAudit', '999', '2'), ('1715', '2018-03-25 09:00:00', null, null, null, '批量分发', '301', 'media:program:batchInjection', '999', '2'), ('1716', '2018-03-25 09:00:00', null, null, null, '批量回收', '301', 'media:program:batchOutInjection', '999', '2'), ('1717', '2018-03-25 09:00:00', null, null, null, '批量分发', '302', 'media:series:batchInjection', '999', '2'), ('1718', '2018-03-25 09:00:00', null, null, null, '批量回收', '302', 'media:series:batchOutInjection', '999', '2'), ('1719', '2018-03-25 09:00:00', null, null, null, '查看(媒体文件)', '201', 'media:mediaFileAudit:view', '999', '2'), ('1720', '2018-03-25 09:00:00', null, null, null, '增加(媒体文件)', '201', 'media:mediaFileAudit:add', '999', '2'), ('1721', '2018-03-25 09:00:00', null, null, null, '修改(媒体文件)', '201', 'media:mediaFileAudit:edit', '999', '2'), ('1722', '2018-03-25 09:00:00', null, null, null, '删除(媒体文件)', '201', 'media:mediaFileAudit:delete', '999', '2'), ('1724', '2018-03-25 09:00:00', null, null, null, '批量删除(媒体文件)', '201', 'media:mediaFileAudit:batchDelete', '999', '2'), ('1729', '2018-03-25 09:00:00', null, null, null, '查看(媒体文件)', '301', 'media:mediaFile:view', '999', '2'), ('1730', '2018-03-25 09:00:00', null, null, null, '增加(媒体文件)', '301', 'media:mediaFile:add', '999', '2'), ('1731', '2018-03-25 09:00:00', null, null, null, '修改(媒体文件)', '301', 'media:mediaFile:edit', '999', '2'), ('1732', '2018-03-25 09:00:00', null, null, null, '删除(媒体文件)', '301', 'media:mediaFile:delete', '999', '2'), ('1734', '2018-03-25 09:00:00', null, null, null, '批量删除(媒体文件)', '301', 'media:mediaFile:batchDelete', '999', '2'), ('1735', '2018-03-25 09:00:00', null, null, null, '上线(媒体文件)', '301', 'media:mediaFile:online', '999', '2'), ('1736', '2018-03-25 09:00:00', null, null, null, '下线(媒体文件)', '301', 'media:mediaFile:offline', '999', '2'), ('1737', '2018-03-25 09:00:00', null, null, null, '批量上线(媒体文件)', '301', 'media:mediaFile:batchOnline', '999', '2'), ('1738', '2018-03-25 09:00:00', null, null, null, '批量下线(媒体文件)', '301', 'media:mediaFile:batchOffline', '999', '2'), ('1739', '2018-03-25 09:00:00', null, null, null, '播放代码', '301', 'media:program:playCode', '999', '2'), ('1740', '2018-03-25 09:00:00', null, null, null, '播放代码(媒体文件)', '301', 'media:mediaFile:playCode', '999', '2');
COMMIT;

-- ----------------------------
--  Table structure for `sys_operation_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `action` varchar(255) DEFAULT NULL,
  `action_result` int(11) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `module` varchar(255) DEFAULT NULL,
  `object_id` varchar(1024) DEFAULT NULL,
  `object_name` longtext,
  `sub_module` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sys_role_rel_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_rel_menu`;
CREATE TABLE `sys_role_rel_menu` (
  `menu_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  KEY `FK_csf9dc6norsi61onmb2cmbf9f` (`role_id`),
  KEY `FK_idj63yq2lfpx823xlxdkeg8ki` (`menu_id`),
  CONSTRAINT `FK_csf9dc6norsi61onmb2cmbf9f` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`),
  CONSTRAINT `FK_idj63yq2lfpx823xlxdkeg8ki` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `can_be_delete` bit(1) DEFAULT NULL,
  `last_login_ip` varchar(255) DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `login_ip` varchar(255) DEFAULT NULL,
  `login_name` varchar(64) NOT NULL,
  `login_num` int(11) DEFAULT NULL,
  `login_time` datetime DEFAULT NULL,
  `mobile` varchar(64) DEFAULT NULL,
  `no` varchar(64) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `status` int(11) DEFAULT NULL,
  `cp_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_user`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES ('1', 'lengweiping1983@hotmail.com', '管理员', '13661621160', '2018-03-25 09:00:00', '2018-03-27 23:34:14', b'0', '127.0.0.1', '2018-03-27 21:40:37', '127.0.0.1', 'admin', '34', '2018-03-27 23:34:14', '', '000', '88b8cbaad8b49b1641d28b1f3d2186e42a6b7db81de3022c72d1d73b', '1', null), ('2', 'lengweiping1983@hotmail.com', '冷伟平', '13661621160', '2018-03-25 09:00:00', '2018-03-25 09:00:00', b'1', '127.0.0.1', '2017-08-03 12:42:58', '127.0.0.1', 'lengweiping', '1', '2018-03-25 09:00:00', '', '001', '88b8cbaad8b49b1641d28b1f3d2186e42a6b7db81de3022c72d1d73b', '1', null);
COMMIT;

-- ----------------------------
--  Table structure for `sys_user_rel_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_rel_role`;
CREATE TABLE `sys_user_rel_role` (
  `role_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  KEY `FK_5qrhlf79aw4igonqdw00f5g4p` (`user_id`),
  KEY `FK_k5xw2dgv0ohucaumbw4mwqipw` (`role_id`),
  CONSTRAINT `FK_5qrhlf79aw4igonqdw00f5g4p` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`),
  CONSTRAINT `FK_k5xw2dgv0ohucaumbw4mwqipw` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
