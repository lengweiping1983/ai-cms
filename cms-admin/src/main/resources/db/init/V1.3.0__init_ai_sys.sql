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

 Date: 04/26/2018 15:34:41 PM
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
) ENGINE=InnoDB AUTO_INCREMENT=2000 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_menu`
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES ('1', '2018-04-25 09:00:00', null, '/system/', 'icon-pin', '系统管理', null, null, '999', '1'), ('2', '2018-04-25 09:00:00', null, '/system/menu/', 'icon-book-open', '菜单管理', '1', null, '1', '1'), ('3', '2018-04-25 09:00:00', null, '/system/role/', 'icon-rocket', '角色管理', '1', null, '2', '1'), ('5', '2018-04-25 09:00:00', null, '/system/user?search_type__EQ_I=1', 'icon-symbol-male', '用户管理', '1', null, '3', '1'), ('6', '2018-04-25 09:00:00', null, '/system/dic/', 'icon-control-end', '字典管理', '1', null, '4', '1'), ('7', '2018-04-25 09:00:00', null, null, null, '增加', '2', 'system:menu:add', '999', '2'), ('8', '2018-04-25 09:00:00', null, null, null, '修改', '2', 'system:menu:edit', '999', '2'), ('9', '2018-04-25 09:00:00', null, null, null, '删除', '2', 'system:menu:delete', '999', '2'), ('10', '2018-04-25 09:00:00', null, null, null, '查看', '2', 'system:menu:view', '999', '2'), ('11', '2018-04-25 09:00:00', null, null, null, '增加', '5', 'system:user:add', '999', '2'), ('12', '2018-04-25 09:00:00', null, null, null, '修改', '5', 'system:user:edit', '999', '2'), ('13', '2018-04-25 09:00:00', null, null, null, '修改密码', '5', 'system:user:editPassword', '999', '2'), ('14', '2018-04-25 09:00:00', null, null, null, '删除', '5', 'system:user:delete', '999', '2'), ('15', '2018-04-25 09:00:00', null, null, null, '查看', '5', 'system:user:view', '999', '2'), ('17', '2018-04-25 09:00:00', null, null, null, '增加', '3', 'system:role:add', '999', '2'), ('18', '2018-04-25 09:00:00', null, null, null, '修改', '3', 'system:role:edit', '999', '2'), ('19', '2018-04-25 09:00:00', null, null, null, '删除', '3', 'system:role:delete', '999', '2'), ('20', '2018-04-25 09:00:00', null, null, null, '查看', '3', 'system:role:view', '999', '2'), ('21', '2018-04-25 09:00:00', null, null, null, '增加', '6', 'system:dic:add', '999', '2'), ('22', '2018-04-25 09:00:00', null, null, null, '修改', '6', 'system:dic:edit', '999', '2'), ('23', '2018-04-25 09:00:00', null, null, null, '删除', '6', 'system:dic:delete', '999', '2'), ('24', '2018-04-25 09:00:00', null, null, null, '查看', '6', 'system:dic:view', '999', '2'), ('25', '2018-04-25 09:00:00', null, '/log', 'fa fa-commenting-o', '营运日志', null, null, '900', '1'), ('26', '2018-04-25 09:00:00', null, '/system/operationlog', 'fa fa-commenting', '操作行为日志', '25', '', '999', '1'), ('27', '2018-04-25 09:00:00', null, null, null, '清空历史日志', '26', 'system:log:clear', '999', '2'), ('28', '2018-04-25 09:00:00', null, null, null, '查看', '26', 'system:log:view', '999', '2'), ('32', '2018-04-25 09:00:00', null, '/config', 'fa fa-genderless', '配置管理', null, null, '100', '1'), ('33', '2018-04-25 09:00:00', null, '/config/site', 'fa fa-gg', '渠道配置', '32', null, '20', '1'), ('34', '2018-04-25 09:00:00', null, null, null, '增加', '33', 'config:site:add', '999', '2'), ('35', '2018-04-25 09:00:00', null, null, null, '修改', '33', 'config:site:edit', '999', '2'), ('36', '2018-04-25 09:00:00', null, null, null, '删除', '33', 'config:site:delete', '999', '2'), ('37', '2018-04-25 09:00:00', null, null, null, '查看', '33', 'config:site:view', '999', '2'), ('40', '2018-04-25 09:00:00', null, '/config/cp', 'fa fa-gg-circle', '提供商配置', '32', null, '30', '1'), ('41', '2018-04-25 09:00:00', null, null, null, '增加', '40', 'config:cp:add', '999', '2'), ('42', '2018-04-25 09:00:00', null, null, null, '修改', '40', 'config:cp:edit', '999', '2'), ('43', '2018-04-25 09:00:00', null, null, null, '删除', '40', 'config:cp:delete', '999', '2'), ('44', '2018-04-25 09:00:00', null, null, null, '查看', '40', 'config:cp:view', '999', '2'), ('45', '2018-04-25 09:00:00', null, null, null, '设置FTP地址', '40', 'config:cp:editCpFtp', '999', '2'), ('46', '2018-04-25 09:00:00', null, null, null, '设置登录帐号', '40', 'config:cp:user', '999', '2'), ('50', '2018-04-25 09:00:00', null, '/config/mediaTemplate', 'fa fa-contao', '码率模板配置', '32', null, '90', '1'), ('51', '2018-04-25 09:00:00', null, null, null, '增加', '50', 'config:mediaTemplate:add', '999', '2'), ('52', '2018-04-25 09:00:00', null, null, null, '修改', '50', 'config:mediaTemplate:edit', '999', '2'), ('53', '2018-04-25 09:00:00', null, null, null, '删除', '50', 'config:mediaTemplate:delete', '999', '2'), ('54', '2018-04-25 09:00:00', null, null, null, '查看', '50', 'config:mediaTemplate:view', '999', '2'), ('100', '2018-04-25 09:00:00', null, '/media', 'fa fa-btc', '媒资生产', null, null, '10', '1'), ('101', '2018-04-25 09:00:00', null, '/media/file', 'fa fa-cloud', '媒资文件浏览', '100', null, '10', '1'), ('102', '2018-04-25 09:00:00', null, '/transcode/transcodeRequest', 'fa fa-registered', '转码工单管理', '100', null, '20', '1'), ('103', '2018-04-25 09:00:00', null, '/transcode/transcodeTask', 'fa fa-chrome', '转码任务管理', '100', null, '30', '1'), ('111', '2018-04-25 09:00:00', null, null, null, '增加工单', '102', 'transcode:transcodeRequest:add', '999', '2'), ('112', '2018-04-25 09:00:00', null, null, null, '修改工单', '102', 'transcode:transcodeRequest:edit', '999', '2'), ('113', '2018-04-25 09:00:00', null, null, null, '删除工单', '102', 'transcode:transcodeRequest:delete', '999', '2'), ('114', '2018-04-25 09:00:00', null, null, null, '查看工单', '102', 'transcode:transcodeRequest:view', '999', '2'), ('115', '2018-04-25 09:00:00', null, null, null, '执行工单', '102', 'transcode:transcodeRequest:produce', '999', '2'), ('116', '2018-04-25 09:00:00', null, null, null, '批量执行工单', '102', 'transcode:transcodeRequest:batchProduce', '999', '2'), ('117', '2018-04-25 09:00:00', null, null, null, '批量复制工单', '102', 'transcode:transcodeRequest:batchCopy', '999', '2'), ('118', '2018-04-25 09:00:00', null, null, null, '批量导出工单', '102', 'transcode:transcodeRequest:batchExport', '999', '2'), ('200', '2018-04-25 09:00:00', null, '/media', 'fa fa-intersex', '媒资审核', null, null, '20', '1'), ('201', '2018-04-25 09:00:00', null, '/media/programAudit?search_auditStatus__IN_S=0,1,9', 'fa fa-mars', '节目审核', '200', null, '30', '1'), ('202', '2018-04-25 09:00:00', null, '/media/seriesAudit?search_auditStatus__IN_S=0,1,9', 'fa fa-mars-double', '剧头审核', '200', null, '40', '1'), ('203', '2018-04-25 09:00:00', null, '/media/mediaImport', 'fa fa-cart-arrow-down', '媒资导入', '200', null, '999', '1'), ('204', '2018-04-25 09:00:00', null, null, null, '批量导入', '203', 'media:mediaAudit:batchImport', '999', '2'), ('300', '2018-04-25 09:00:00', null, '/media', 'fa fa-bank', '媒资管理', null, null, '30', '1'), ('301', '2018-04-25 09:00:00', null, '/media/program?search_auditStatus__IN_S=2', 'fa fa-cube', '节目管理', '300', null, '10', '1'), ('302', '2018-04-25 09:00:00', null, '/media/series?search_auditStatus__IN_S=2', 'fa fa-cubes', '剧头管理', '300', null, '30', '1'), ('303', '2018-04-26 11:45:44', null, '/media/mediaImport', 'fa fa-cart-arrow-down', '媒资导入', '300', null, '999', '1'), ('304', '2018-04-26 11:47:46', null, null, null, '批量导入', '303', 'media:media:batchImport', '999', '2'), ('400', '2018-04-25 09:00:00', null, '/media', 'fa fa-motorcycle', '媒资分发', null, null, '40', '1'), ('401', '2018-04-25 09:00:00', null, '/injection/platform', 'fa fa-plane', '分发平台配置', '400', null, '10', '1'), ('402', '2018-04-25 09:00:00', null, null, null, '增加', '401', 'injection:platform:add', '999', '2'), ('403', '2018-04-25 09:00:00', null, null, null, '修改', '401', 'injection:platform:edit', '999', '2'), ('404', '2018-04-25 09:00:00', null, null, null, '删除', '401', 'injection:platform:delete', '999', '2'), ('405', '2018-04-25 09:00:00', null, null, null, '查看', '401', 'injection:platform:view', '999', '2'), ('421', '2018-04-25 09:00:00', null, '/injection/sendTask', 'fa fa-cloud-upload', '分发工单查询', '400', null, '20', '1'), ('422', '2018-04-25 09:00:00', null, null, null, '暂停工单', '421', 'injection:sendTask:pause', '999', '2'), ('423', '2018-04-25 09:00:00', null, null, null, '停止工单', '421', 'injection:sendTask:stop', '999', '2'), ('424', '2018-04-25 09:00:00', null, null, null, '重发工单', '421', 'injection:sendTask:reset', '999', '2'), ('425', '2018-04-25 09:00:00', null, null, null, '批量暂停工单', '421', 'injection:sendTask:batchPause', '999', '2'), ('426', '2018-04-25 09:00:00', null, null, null, '批量停止工单', '421', 'injection:sendTask:batchStop', '999', '2'), ('427', '2018-04-25 09:00:00', null, null, null, '批量重发工单', '421', 'injection:sendTask:batchReset', '999', '2'), ('428', '2018-04-25 09:00:00', null, null, null, '批量调整优先级', '421', 'injection:sendTask:batchChangePriority', '999', '2'), ('441', '2018-04-25 09:00:00', null, '/injection/receiveTask', 'fa fa-cloud-download', '接收工单查询', '400', null, '30', '1'), ('461', '2018-04-25 09:00:00', null, '/injection/downloadTask', 'fa fa-cloud-download', '下载任务管理', '400', null, '40', '1'), ('462', '2018-04-25 09:00:00', null, null, null, '暂停下载', '461', 'injection:downloadTask:pause', '999', '2'), ('463', '2018-04-25 09:00:00', null, null, null, '停止下载', '461', 'injection:downloadTask:stop', '999', '2'), ('464', '2018-04-25 09:00:00', null, null, null, '继续下载', '461', 'injection:downloadTask:reset', '999', '2'), ('465', '2018-04-25 09:00:00', null, null, null, '重新下载', '461', 'injection:downloadTask:renew', '999', '2'), ('466', '2018-04-25 09:00:00', null, null, null, '批量暂停下载', '461', 'injection:downloadTask:batchPause', '999', '2'), ('467', '2018-04-25 09:00:00', null, null, null, '批量停止下载', '461', 'injection:downloadTask:batchStop', '999', '2'), ('468', '2018-04-25 09:00:00', null, null, null, '批量继续下载', '461', 'injection:downloadTask:batchReset', '999', '2'), ('469', '2018-04-25 09:00:00', null, null, null, '批量重新下载', '461', 'injection:downloadTask:batchRenew', '999', '2'), ('470', '2018-04-25 09:00:00', null, null, null, '批量调整优先级', '461', 'injection:downloadTask:batchChangePriority', '999', '2'), ('1619', '2018-04-25 09:00:00', null, null, null, '查看', '301', 'media:program:view', '999', '2'), ('1620', '2018-04-25 09:00:00', null, null, null, '增加', '301', 'media:program:add', '999', '2'), ('1621', '2018-04-25 09:00:00', null, null, null, '修改', '301', 'media:program:edit', '999', '2'), ('1622', '2018-04-25 09:00:00', null, null, null, '删除', '301', 'media:program:delete', '999', '2'), ('1623', '2018-04-25 09:00:00', null, null, null, '批量修改', '301', 'media:program:batchEdit', '999', '2'), ('1624', '2018-04-25 09:00:00', null, null, null, '批量删除', '301', 'media:program:batchDelete', '999', '2'), ('1625', '2018-04-25 09:00:00', null, null, null, '上线', '301', 'media:program:online', '999', '2'), ('1626', '2018-04-25 09:00:00', null, null, null, '下线', '301', 'media:program:offline', '999', '2'), ('1627', '2018-04-25 09:00:00', null, null, null, '批量上线', '301', 'media:program:batchOnline', '999', '2'), ('1628', '2018-04-25 09:00:00', null, null, null, '批量下线', '301', 'media:program:batchOffline', '999', '2'), ('1630', '2018-04-25 09:00:00', null, null, null, '批量导出', '301', 'media:program:batchExport', '999', '2'), ('1651', '2018-04-25 09:00:00', null, null, null, '查看', '201', 'media:programAudit:view', '999', '2'), ('1652', '2018-04-25 09:00:00', null, null, null, '增加', '201', 'media:programAudit:add', '999', '2'), ('1653', '2018-04-25 09:00:00', null, null, null, '修改', '201', 'media:programAudit:edit', '999', '2'), ('1654', '2018-04-25 09:00:00', null, null, null, '删除', '201', 'media:programAudit:delete', '999', '2'), ('1655', '2018-04-25 09:00:00', null, null, null, '批量修改', '201', 'media:programAudit:batchEdit', '999', '2'), ('1656', '2018-04-25 09:00:00', null, null, null, '批量删除', '201', 'media:programAudit:batchDelete', '999', '2'), ('1662', '2018-04-25 09:00:00', null, null, null, '批量导出', '201', 'media:programAudit:batchExport', '999', '2'), ('1663', '2018-04-25 09:00:00', null, null, null, '送审', '201', 'media:programAudit:submit', '999', '2'), ('1664', '2018-04-25 09:00:00', null, null, null, '审核', '201', 'media:programAudit:audit', '999', '2'), ('1665', '2018-04-25 09:00:00', null, null, null, '批量送审', '201', 'media:programAudit:batchSubmit', '999', '2'), ('1666', '2018-04-25 09:00:00', null, null, null, '批量审核', '201', 'media:programAudit:batchAudit', '999', '2'), ('1683', '2018-04-25 09:00:00', null, null, null, '查看', '302', 'media:series:view', '999', '2'), ('1684', '2018-04-25 09:00:00', null, null, null, '增加', '302', 'media:series:add', '999', '2'), ('1685', '2018-04-25 09:00:00', null, null, null, '修改', '302', 'media:series:edit', '999', '2'), ('1686', '2018-04-25 09:00:00', null, null, null, '删除', '302', 'media:series:delete', '999', '2'), ('1687', '2018-04-25 09:00:00', null, null, null, '批量修改', '302', 'media:series:batchEdit', '999', '2'), ('1688', '2018-04-25 09:00:00', null, null, null, '批量删除', '302', 'media:series:batchDelete', '999', '2'), ('1689', '2018-04-25 09:00:00', null, null, null, '上线', '302', 'media:series:online', '999', '2'), ('1690', '2018-04-25 09:00:00', null, null, null, '下线', '302', 'media:series:offline', '999', '2'), ('1691', '2018-04-25 09:00:00', null, null, null, '批量上线', '302', 'media:series:batchOnline', '999', '2'), ('1692', '2018-04-25 09:00:00', null, null, null, '批量下线', '302', 'media:series:batchOffline', '999', '2'), ('1694', '2018-04-25 09:00:00', null, null, null, '批量导出', '302', 'media:series:batchExport', '999', '2'), ('1699', '2018-04-25 09:00:00', null, null, null, '查看', '202', 'media:seriesAudit:view', '999', '2'), ('1700', '2018-04-25 09:00:00', null, null, null, '增加', '202', 'media:seriesAudit:add', '999', '2'), ('1701', '2018-04-25 09:00:00', null, null, null, '修改', '202', 'media:seriesAudit:edit', '999', '2'), ('1702', '2018-04-25 09:00:00', null, null, null, '删除', '202', 'media:seriesAudit:delete', '999', '2'), ('1703', '2018-04-25 09:00:00', null, null, null, '批量修改', '202', 'media:seriesAudit:batchEdit', '999', '2'), ('1704', '2018-04-25 09:00:00', null, null, null, '批量删除', '202', 'media:seriesAudit:batchDelete', '999', '2'), ('1710', '2018-04-25 09:00:00', null, null, null, '批量导出', '202', 'media:seriesAudit:batchExport', '999', '2'), ('1711', '2018-04-25 09:00:00', null, null, null, '送审', '202', 'media:seriesAudit:submit', '999', '2'), ('1712', '2018-04-25 09:00:00', null, null, null, '审核', '202', 'media:seriesAudit:audit', '999', '2'), ('1713', '2018-04-25 09:00:00', null, null, null, '批量送审', '202', 'media:seriesAudit:batchSubmit', '999', '2'), ('1714', '2018-04-25 09:00:00', null, null, null, '批量审核', '202', 'media:seriesAudit:batchAudit', '999', '2'), ('1715', '2018-04-25 09:00:00', null, null, null, '批量分发', '301', 'media:program:batchInjection', '999', '2'), ('1716', '2018-04-25 09:00:00', null, null, null, '批量回收', '301', 'media:program:batchOutInjection', '999', '2'), ('1717', '2018-04-25 09:00:00', null, null, null, '批量分发', '302', 'media:series:batchInjection', '999', '2'), ('1718', '2018-04-25 09:00:00', null, null, null, '批量回收', '302', 'media:series:batchOutInjection', '999', '2'), ('1719', '2018-04-25 09:00:00', null, null, null, '查看(媒体内容)', '201', 'media:mediaFileAudit:view', '999', '2'), ('1720', '2018-04-25 09:00:00', null, null, null, '增加(媒体内容)', '201', 'media:mediaFileAudit:add', '999', '2'), ('1721', '2018-04-25 09:00:00', null, null, null, '修改(媒体内容)', '201', 'media:mediaFileAudit:edit', '999', '2'), ('1722', '2018-04-25 09:00:00', null, null, null, '删除(媒体内容)', '201', 'media:mediaFileAudit:delete', '999', '2'), ('1724', '2018-04-25 09:00:00', null, null, null, '批量删除(媒体内容)', '201', 'media:mediaFileAudit:batchDelete', '999', '2'), ('1729', '2018-04-25 09:00:00', null, null, null, '查看(媒体内容)', '301', 'media:mediaFile:view', '999', '2'), ('1730', '2018-04-25 09:00:00', null, null, null, '增加(媒体内容)', '301', 'media:mediaFile:add', '999', '2'), ('1731', '2018-04-25 09:00:00', null, null, null, '修改(媒体内容)', '301', 'media:mediaFile:edit', '999', '2'), ('1732', '2018-04-25 09:00:00', null, null, null, '删除(媒体内容)', '301', 'media:mediaFile:delete', '999', '2'), ('1734', '2018-04-25 09:00:00', null, null, null, '批量删除(媒体内容)', '301', 'media:mediaFile:batchDelete', '999', '2'), ('1735', '2018-04-25 09:00:00', null, null, null, '上线(媒体内容)', '301', 'media:mediaFile:online', '999', '2'), ('1736', '2018-04-25 09:00:00', null, null, null, '下线(媒体内容)', '301', 'media:mediaFile:offline', '999', '2'), ('1737', '2018-04-25 09:00:00', null, null, null, '批量上线(媒体内容)', '301', 'media:mediaFile:batchOnline', '999', '2'), ('1738', '2018-04-25 09:00:00', null, null, null, '批量下线(媒体内容)', '301', 'media:mediaFile:batchOffline', '999', '2'), ('1739', '2018-04-25 09:00:00', null, null, null, '播放代码', '301', 'media:program:playCode', '999', '2'), ('1740', '2018-04-25 09:00:00', null, null, null, '播放代码(媒体内容)', '301', 'media:mediaFile:playCode', '999', '2');
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
  `cp_code` varchar(255) DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES ('1', '2018-04-26 15:33:59', null, 'CP管理员');
COMMIT;

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
--  Records of `sys_role_rel_menu`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_rel_menu` VALUES ('100', '1'), ('111', '1'), ('112', '1'), ('113', '1'), ('114', '1'), ('115', '1'), ('116', '1'), ('117', '1'), ('118', '1'), ('101', '1'), ('102', '1'), ('103', '1'), ('201', '1'), ('1651', '1'), ('1652', '1'), ('1653', '1'), ('1654', '1'), ('1655', '1'), ('1656', '1'), ('1662', '1'), ('1663', '1'), ('1664', '1'), ('1665', '1'), ('1666', '1'), ('1719', '1'), ('1720', '1'), ('1721', '1'), ('1722', '1'), ('1724', '1'), ('202', '1'), ('1699', '1'), ('1700', '1'), ('1701', '1'), ('1702', '1'), ('1703', '1'), ('1704', '1'), ('1710', '1'), ('1711', '1'), ('1712', '1'), ('1713', '1'), ('1714', '1'), ('301', '1'), ('1619', '1'), ('1620', '1'), ('1621', '1'), ('1622', '1'), ('1623', '1'), ('1624', '1'), ('1625', '1'), ('1626', '1'), ('1627', '1'), ('1628', '1'), ('1630', '1'), ('1715', '1'), ('1716', '1'), ('1729', '1'), ('1730', '1'), ('1731', '1'), ('1732', '1'), ('1734', '1'), ('1735', '1'), ('1736', '1'), ('1737', '1'), ('1738', '1'), ('1739', '1'), ('1740', '1'), ('302', '1'), ('1683', '1'), ('1684', '1'), ('1685', '1'), ('1686', '1'), ('1687', '1'), ('1688', '1'), ('1689', '1'), ('1690', '1'), ('1691', '1'), ('1692', '1'), ('1694', '1'), ('1717', '1'), ('1718', '1'), ('421', '1'), ('422', '1'), ('423', '1'), ('424', '1'), ('425', '1'), ('426', '1'), ('427', '1'), ('428', '1'), ('441', '1'), ('461', '1'), ('462', '1'), ('463', '1'), ('464', '1'), ('465', '1'), ('466', '1'), ('467', '1'), ('468', '1'), ('469', '1'), ('470', '1'), ('26', '1'), ('27', '1'), ('28', '1'), ('25', '1');
COMMIT;

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
  `type` int(11) NOT NULL DEFAULT '1',
  `cp_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_user`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES ('1', 'lengweiping1983@hotmail.com', '管理员', '13661621160', '2018-04-25 09:00:00', '2018-04-26 15:19:28', b'0', '127.0.0.1', '2018-04-26 14:51:51', '127.0.0.1', 'admin', '189', '2018-04-26 15:19:28', '', '000', '88b8cbaad8b49b1641d28b1f3d2186e42a6b7db81de3022c72d1d73b', '1', '1', null);
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
