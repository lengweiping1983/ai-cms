/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50622
 Source Host           : 127.0.0.1
 Source Database       : ai_cms

 Target Server Type    : MySQL
 Target Server Version : 50622
 File Encoding         : utf-8

 Date: 03/28/2018 08:30:03 AM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `cms_club`
-- ----------------------------
DROP TABLE IF EXISTS `cms_club`;
CREATE TABLE `cms_club` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `cloud_code` varchar(255) DEFAULT NULL,
  `cloud_id` varchar(255) DEFAULT NULL,
  `coach` varchar(255) DEFAULT NULL,
  `founding_time` datetime DEFAULT NULL,
  `greet_rate` float DEFAULT NULL,
  `hate_num` int(11) DEFAULT NULL,
  `home_court` varchar(255) DEFAULT NULL,
  `honor` varchar(255) DEFAULT NULL,
  `image1` varchar(255) DEFAULT NULL,
  `image2` varchar(255) DEFAULT NULL,
  `info` varchar(255) DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `love_num` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `offline_time` datetime DEFAULT NULL,
  `offline_user` varchar(255) DEFAULT NULL,
  `online_time` datetime DEFAULT NULL,
  `online_user` varchar(255) DEFAULT NULL,
  `short_name` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_cp`
-- ----------------------------
DROP TABLE IF EXISTS `cms_cp`;
CREATE TABLE `cms_cp` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `code` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `short_name` varchar(255) NOT NULL,
  `status` int(11) DEFAULT '0',
  `type` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cms_cp_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_injection_object`
-- ----------------------------
DROP TABLE IF EXISTS `cms_injection_object`;
CREATE TABLE `cms_injection_object` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `category` varchar(255) NOT NULL,
  `injection_status` int(11) DEFAULT '1',
  `injection_time` datetime DEFAULT NULL,
  `item_id` bigint(20) NOT NULL,
  `item_type` int(11) NOT NULL,
  `partner_item_code` varchar(255) DEFAULT NULL,
  `partner_item_reserved1` varchar(255) DEFAULT NULL,
  `partner_item_reserved2` varchar(255) DEFAULT NULL,
  `platform_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cms_injection_object_index` (`platform_id`,`category`,`item_type`,`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_injection_platform`
-- ----------------------------
DROP TABLE IF EXISTS `cms_injection_platform`;
CREATE TABLE `cms_injection_platform` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `code_prefix` varchar(255) DEFAULT '00000000',
  `correlate_prefix` varchar(255) DEFAULT NULL,
  `csp_id` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `direction` int(11) DEFAULT '0',
  `interface_mode` int(11) DEFAULT '1',
  `is_callback` int(11) DEFAULT '0',
  `is_wsdl` int(11) DEFAULT '1',
  `live_service_url` varchar(255) DEFAULT NULL,
  `lsp_id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `namespace` varchar(255) DEFAULT NULL,
  `play_code_custom` int(11) DEFAULT '0',
  `provider` int(11) DEFAULT '0',
  `service_url` varchar(255) NOT NULL,
  `site_code` varchar(255) NOT NULL,
  `status` int(11) DEFAULT '0',
  `template_custom` int(11) DEFAULT '0',
  `template_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_injection_receive_task`
-- ----------------------------
DROP TABLE IF EXISTS `cms_injection_receive_task`;
CREATE TABLE `cms_injection_receive_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `cmd_file_url` varchar(1024) DEFAULT NULL,
  `cmd_result` int(11) DEFAULT NULL,
  `correlate_id` varchar(255) DEFAULT NULL,
  `download_times` int(11) DEFAULT NULL,
  `first_response_time` datetime DEFAULT NULL,
  `last_response_time` datetime DEFAULT NULL,
  `platform_id` bigint(20) NOT NULL,
  `priority` int(11) NOT NULL,
  `receive_time` datetime NOT NULL,
  `reply_error_description` varchar(255) DEFAULT NULL,
  `reply_result` int(11) DEFAULT NULL,
  `request_error_description` varchar(1024) DEFAULT NULL,
  `request_result` int(11) DEFAULT NULL,
  `request_xml_file_content` longtext,
  `request_xml_file_path` varchar(1024) DEFAULT NULL,
  `response_error_description` varchar(1024) DEFAULT NULL,
  `response_result` int(11) DEFAULT NULL,
  `response_status` int(11) DEFAULT NULL,
  `response_time` datetime DEFAULT NULL,
  `response_times` int(11) DEFAULT NULL,
  `response_total_times` int(11) DEFAULT NULL,
  `response_xml_file_content` longtext,
  `response_xml_file_path` varchar(1024) DEFAULT NULL,
  `result_file_url` varchar(1024) DEFAULT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ns3stci98wygl0bgoj4bsxvy6` (`platform_id`),
  CONSTRAINT `FK_ns3stci98wygl0bgoj4bsxvy6` FOREIGN KEY (`platform_id`) REFERENCES `cms_injection_platform` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_injection_send_event`
-- ----------------------------
DROP TABLE IF EXISTS `cms_injection_send_event`;
CREATE TABLE `cms_injection_send_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `action` int(11) NOT NULL,
  `category` varchar(255) NOT NULL,
  `correlate_id` varchar(255) DEFAULT NULL,
  `fire_time` datetime NOT NULL,
  `item_code` varchar(255) DEFAULT NULL,
  `item_id` bigint(20) NOT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `item_type` int(11) NOT NULL,
  `next_check_time` datetime DEFAULT NULL,
  `parent_item_code` varchar(255) DEFAULT NULL,
  `parent_item_id` bigint(20) DEFAULT NULL,
  `parent_item_name` varchar(255) DEFAULT NULL,
  `parent_item_type` int(11) DEFAULT NULL,
  `parent_partner_item_code` varchar(255) DEFAULT NULL,
  `partner_item_code` varchar(255) DEFAULT NULL,
  `platform_id` bigint(20) NOT NULL,
  `priority` int(11) NOT NULL,
  `rel_id` bigint(20) DEFAULT NULL,
  `sort_index` int(11) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_injection_send_task`
-- ----------------------------
DROP TABLE IF EXISTS `cms_injection_send_task`;
CREATE TABLE `cms_injection_send_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `action` int(11) DEFAULT NULL,
  `category` varchar(255) NOT NULL,
  `cmd_file_url` varchar(1024) DEFAULT NULL,
  `cmd_result` int(11) DEFAULT NULL,
  `correlate_id` varchar(255) DEFAULT NULL,
  `first_request_time` datetime DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `item_type` int(11) DEFAULT NULL,
  `last_request_time` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `operation_object_ids` longtext,
  `platform_id` bigint(20) NOT NULL,
  `pre_task_id` bigint(20) DEFAULT NULL,
  `pre_task_status` int(11) DEFAULT NULL,
  `priority` int(11) NOT NULL,
  `request_error_description` varchar(1024) DEFAULT NULL,
  `request_result` int(11) DEFAULT NULL,
  `request_times` int(11) DEFAULT NULL,
  `request_total_times` int(11) DEFAULT NULL,
  `request_xml_file_content` longtext,
  `request_xml_file_path` varchar(1024) DEFAULT NULL,
  `response_error_description` varchar(1024) DEFAULT NULL,
  `response_result` int(11) DEFAULT NULL,
  `response_time` datetime DEFAULT NULL,
  `response_xml_file_content` longtext,
  `response_xml_file_path` varchar(1024) DEFAULT NULL,
  `result_file_url` varchar(1024) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_lxyx7afa300wl3ay5ps346xwg` (`platform_id`),
  CONSTRAINT `FK_lxyx7afa300wl3ay5ps346xwg` FOREIGN KEY (`platform_id`) REFERENCES `cms_injection_platform` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_league`
-- ----------------------------
DROP TABLE IF EXISTS `cms_league`;
CREATE TABLE `cms_league` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `caption` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `greet_rate` float DEFAULT NULL,
  `image1` varchar(255) DEFAULT NULL,
  `image2` varchar(255) DEFAULT NULL,
  `info` varchar(255) DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `num` int(11) DEFAULT NULL,
  `offline_time` datetime DEFAULT NULL,
  `offline_user` varchar(255) DEFAULT NULL,
  `online_time` datetime DEFAULT NULL,
  `online_user` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `template_code` varchar(255) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_league_match`
-- ----------------------------
DROP TABLE IF EXISTS `cms_league_match`;
CREATE TABLE `cms_league_match` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `begin_time` datetime DEFAULT NULL,
  `caption` varchar(255) DEFAULT NULL,
  `channel_id` bigint(20) DEFAULT NULL,
  `cloud_code` varchar(255) DEFAULT NULL,
  `cloud_id` varchar(255) DEFAULT NULL,
  `cp_id` varchar(255) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `episode_index` int(11) DEFAULT NULL,
  `guest_id` bigint(20) DEFAULT NULL,
  `guest_name` varchar(255) DEFAULT NULL,
  `guest_point_num` int(11) DEFAULT NULL,
  `guest_score` int(11) DEFAULT NULL,
  `guest_type` int(11) DEFAULT NULL,
  `home_id` bigint(20) DEFAULT NULL,
  `home_name` varchar(255) DEFAULT NULL,
  `home_point_num` int(11) DEFAULT NULL,
  `home_score` int(11) DEFAULT NULL,
  `home_type` int(11) DEFAULT NULL,
  `image1` varchar(255) DEFAULT NULL,
  `image2` varchar(255) DEFAULT NULL,
  `info` varchar(255) DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `league_index` int(11) DEFAULT NULL,
  `league_season_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `offline_time` datetime DEFAULT NULL,
  `offline_user` varchar(255) DEFAULT NULL,
  `online_time` datetime DEFAULT NULL,
  `online_user` varchar(255) DEFAULT NULL,
  `point_status` int(11) DEFAULT NULL,
  `program_id` bigint(20) DEFAULT NULL,
  `schedule_id` bigint(20) DEFAULT NULL,
  `search_name` varchar(255) DEFAULT NULL,
  `split_program` int(11) DEFAULT NULL,
  `sport_content_type` int(11) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `viewpoint` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_league_match_code`
-- ----------------------------
DROP TABLE IF EXISTS `cms_league_match_code`;
CREATE TABLE `cms_league_match_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `charge_code` varchar(255) DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `item_type` int(11) DEFAULT NULL,
  `play_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_league_season`
-- ----------------------------
DROP TABLE IF EXISTS `cms_league_season`;
CREATE TABLE `cms_league_season` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `begin_time` datetime DEFAULT NULL,
  `caption` varchar(255) DEFAULT NULL,
  `channel_id` bigint(20) DEFAULT NULL,
  `cloud_code` varchar(255) DEFAULT NULL,
  `cloud_id` varchar(255) DEFAULT NULL,
  `cp_id` varchar(255) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `image1` varchar(255) DEFAULT NULL,
  `image2` varchar(255) DEFAULT NULL,
  `info` varchar(255) DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `league_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `offline_time` datetime DEFAULT NULL,
  `offline_user` varchar(255) DEFAULT NULL,
  `online_time` datetime DEFAULT NULL,
  `online_user` varchar(255) DEFAULT NULL,
  `program_id` bigint(20) DEFAULT NULL,
  `schedule_id` bigint(20) DEFAULT NULL,
  `search_name` varchar(255) DEFAULT NULL,
  `split_program` int(11) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `viewpoint` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_league_season_club`
-- ----------------------------
DROP TABLE IF EXISTS `cms_league_season_club`;
CREATE TABLE `cms_league_season_club` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `credit_num` int(11) DEFAULT NULL,
  `enter_num` int(11) DEFAULT NULL,
  `flat_num` int(11) DEFAULT NULL,
  `fumble_num` int(11) DEFAULT NULL,
  `image1` varchar(255) DEFAULT NULL,
  `image2` varchar(255) DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `item_type` int(11) DEFAULT NULL,
  `league_season_id` bigint(20) DEFAULT NULL,
  `lose_num` int(11) DEFAULT NULL,
  `point_num` int(11) DEFAULT NULL,
  `sort_index` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `win_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_league_season_star`
-- ----------------------------
DROP TABLE IF EXISTS `cms_league_season_star`;
CREATE TABLE `cms_league_season_star` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `assist_num` int(11) DEFAULT NULL,
  `enter_num` int(11) DEFAULT NULL,
  `image1` varchar(255) DEFAULT NULL,
  `image2` varchar(255) DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `item_type` int(11) DEFAULT NULL,
  `league_season_id` bigint(20) DEFAULT NULL,
  `point_num` int(11) DEFAULT NULL,
  `site_num` int(11) DEFAULT NULL,
  `sort_index` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_live_channel`
-- ----------------------------
DROP TABLE IF EXISTS `cms_live_channel`;
CREATE TABLE `cms_live_channel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `bilingual` int(11) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `cp_id` varchar(255) DEFAULT NULL,
  `image1` varchar(255) DEFAULT NULL,
  `image2` varchar(255) DEFAULT NULL,
  `info` varchar(255) DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `offline_time` datetime DEFAULT NULL,
  `offline_user` varchar(255) DEFAULT NULL,
  `online_time` datetime DEFAULT NULL,
  `online_user` varchar(255) DEFAULT NULL,
  `site_code` varchar(255) DEFAULT NULL,
  `sort_index` int(11) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `zipcode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_live_channel_config`
-- ----------------------------
DROP TABLE IF EXISTS `cms_live_channel_config`;
CREATE TABLE `cms_live_channel_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `channel_id` bigint(20) DEFAULT NULL,
  `enbody_duration` int(11) DEFAULT NULL,
  `image1` varchar(255) DEFAULT NULL,
  `image2` varchar(255) DEFAULT NULL,
  `info` varchar(255) DEFAULT NULL,
  `live_encrypt` int(11) DEFAULT NULL,
  `look_back` int(11) DEFAULT NULL,
  `look_back_duration` int(11) DEFAULT NULL,
  `look_back_encrypt` int(11) DEFAULT NULL,
  `look_back_url` varchar(255) DEFAULT NULL,
  `offline_time` datetime DEFAULT NULL,
  `offline_user` varchar(255) DEFAULT NULL,
  `online_time` datetime DEFAULT NULL,
  `online_user` varchar(255) DEFAULT NULL,
  `partner_channel_code` varchar(255) DEFAULT NULL,
  `partner_channel_name` varchar(255) DEFAULT NULL,
  `partner_channel_number` varchar(255) DEFAULT NULL,
  `play_by_channel_number` int(11) DEFAULT NULL,
  `pre_time` int(11) DEFAULT NULL,
  `provider_type` int(11) DEFAULT NULL,
  `sort_index` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `suf_time` int(11) DEFAULT NULL,
  `time_shift` int(11) DEFAULT NULL,
  `time_shift_duration` int(11) DEFAULT NULL,
  `time_shift_encrypt` int(11) DEFAULT NULL,
  `time_shift_url` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_live_import`
-- ----------------------------
DROP TABLE IF EXISTS `cms_live_import`;
CREATE TABLE `cms_live_import` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `error_message` varchar(255) DEFAULT NULL,
  `failure` int(11) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `import_time` datetime DEFAULT NULL,
  `site_code` varchar(255) DEFAULT NULL,
  `success` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_live_schedule`
-- ----------------------------
DROP TABLE IF EXISTS `cms_live_schedule`;
CREATE TABLE `cms_live_schedule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `begin_time` datetime DEFAULT NULL,
  `channel_id` bigint(20) DEFAULT NULL,
  `cloud_code` varchar(255) DEFAULT NULL,
  `cloud_id` varchar(255) DEFAULT NULL,
  `cp_id` varchar(255) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `episode_index` int(11) DEFAULT NULL,
  `info` varchar(255) DEFAULT NULL,
  `injection_status` varchar(255) DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `offline_time` datetime DEFAULT NULL,
  `offline_user` varchar(255) DEFAULT NULL,
  `online_time` datetime DEFAULT NULL,
  `online_user` varchar(255) DEFAULT NULL,
  `play_code` varchar(255) DEFAULT NULL,
  `play_code_status` int(11) DEFAULT NULL,
  `program_id` bigint(20) DEFAULT NULL,
  `program_name` varchar(255) DEFAULT NULL,
  `search_name` varchar(255) DEFAULT NULL,
  `split_program` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `update_info` varchar(255) DEFAULT NULL,
  `viewpoint` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_media_file`
-- ----------------------------
DROP TABLE IF EXISTS `cms_media_file`;
CREATE TABLE `cms_media_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `bitrate` int(11) DEFAULT NULL,
  `cloud_code` varchar(255) DEFAULT NULL,
  `cloud_id` varchar(255) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `episode_index` int(11) DEFAULT '1',
  `file_md5` varchar(255) DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `file_size` bigint(20) DEFAULT NULL,
  `format` varchar(255) DEFAULT NULL,
  `injection_status` varchar(255) DEFAULT '0',
  `media_spec` varchar(255) DEFAULT NULL,
  `media_status` int(11) DEFAULT '110',
  `offline_time` datetime DEFAULT NULL,
  `offline_user` varchar(255) DEFAULT NULL,
  `online_time` datetime DEFAULT NULL,
  `online_user` varchar(255) DEFAULT NULL,
  `play_code` varchar(255) DEFAULT NULL,
  `play_code_status` int(11) DEFAULT '0',
  `program_id` bigint(20) DEFAULT NULL,
  `reserved1` varchar(255) DEFAULT NULL,
  `reserved2` varchar(255) DEFAULT NULL,
  `reserved3` varchar(255) DEFAULT NULL,
  `reserved4` varchar(255) DEFAULT NULL,
  `reserved5` varchar(255) DEFAULT NULL,
  `resolution` varchar(255) DEFAULT NULL,
  `series_id` bigint(20) DEFAULT NULL,
  `source` int(11) DEFAULT '0',
  `status` int(11) DEFAULT '0',
  `storage_no` varchar(255) DEFAULT NULL,
  `template_id` bigint(20) DEFAULT NULL,
  `type` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cms_media_file_program_type_template` (`program_id`,`type`,`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_media_import`
-- ----------------------------
DROP TABLE IF EXISTS `cms_media_import`;
CREATE TABLE `cms_media_import` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_metadata` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `error_message` varchar(255) DEFAULT NULL,
  `failure` int(11) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `import_time` datetime DEFAULT NULL,
  `mode` int(11) DEFAULT NULL,
  `site_code` varchar(255) DEFAULT NULL,
  `success` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_media_program`
-- ----------------------------
DROP TABLE IF EXISTS `cms_media_program`;
CREATE TABLE `cms_media_program` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `actor` varchar(255) DEFAULT NULL,
  `actor_pinyin` varchar(255) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `audit_comment` varchar(255) DEFAULT NULL,
  `audit_status` int(11) DEFAULT '0',
  `audit_time` datetime DEFAULT NULL,
  `audit_user` varchar(255) DEFAULT NULL,
  `authorize_address` varchar(255) DEFAULT NULL,
  `authorize_info` varchar(255) DEFAULT NULL,
  `broadcast_license` varchar(255) DEFAULT NULL,
  `caption` varchar(255) DEFAULT NULL,
  `cloud_code` varchar(255) DEFAULT NULL,
  `cloud_id` varchar(255) DEFAULT NULL,
  `compere` varchar(255) DEFAULT NULL,
  `content_type` int(11) NOT NULL,
  `copy_right` varchar(255) DEFAULT NULL,
  `cp_code` varchar(255) DEFAULT NULL,
  `cp_id` varchar(255) DEFAULT NULL,
  `director` varchar(255) DEFAULT NULL,
  `director_pinyin` varchar(255) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `en_name` varchar(255) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `guest` varchar(255) DEFAULT NULL,
  `hate_num` int(11) DEFAULT '0',
  `image1` varchar(255) DEFAULT NULL,
  `image1_code` varchar(255) DEFAULT NULL,
  `image2` varchar(255) DEFAULT NULL,
  `image2_code` varchar(255) DEFAULT NULL,
  `image3` varchar(255) DEFAULT NULL,
  `image4` varchar(255) DEFAULT NULL,
  `incharge` varchar(255) DEFAULT NULL,
  `info` varchar(255) DEFAULT NULL,
  `injection_status` varchar(255) DEFAULT '0',
  `internal_tag` varchar(255) DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `kpeople` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `licensing_window_end` datetime DEFAULT NULL,
  `licensing_window_start` datetime DEFAULT NULL,
  `love_num` int(11) DEFAULT '0',
  `media_status` int(11) DEFAULT '110',
  `name` varchar(255) NOT NULL,
  `offline_time` datetime DEFAULT NULL,
  `offline_user` varchar(255) DEFAULT NULL,
  `online_time` datetime DEFAULT NULL,
  `online_user` varchar(255) DEFAULT NULL,
  `order_number` varchar(255) DEFAULT NULL,
  `org_air_date` datetime DEFAULT NULL,
  `play_code` varchar(255) DEFAULT NULL,
  `play_code_status` int(11) DEFAULT '0',
  `rating` float DEFAULT NULL,
  `reporter` varchar(255) DEFAULT NULL,
  `reserved1` varchar(255) DEFAULT NULL,
  `reserved2` varchar(255) DEFAULT NULL,
  `reserved3` varchar(255) DEFAULT NULL,
  `reserved4` varchar(255) DEFAULT NULL,
  `reserved5` varchar(255) DEFAULT NULL,
  `script_writer` varchar(255) DEFAULT NULL,
  `search_name` varchar(255) DEFAULT NULL,
  `site_code` varchar(255) DEFAULT NULL,
  `sort_name` varchar(255) DEFAULT NULL,
  `source` int(11) DEFAULT '0',
  `sp_code` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `storage_path` varchar(255) DEFAULT NULL,
  `storage_time` datetime DEFAULT NULL,
  `submit_time` datetime DEFAULT NULL,
  `submit_user` varchar(255) DEFAULT NULL,
  `subtitle` int(11) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `template_id` varchar(255) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `viewpoint` varchar(255) DEFAULT NULL,
  `vsp_code` varchar(255) DEFAULT NULL,
  `year` varchar(255) DEFAULT NULL,
  `episode_index` int(11) DEFAULT '1',
  `series_id` bigint(20) DEFAULT NULL,
  `source_type` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cms_media_program_series_episode` (`series_id`,`episode_index`),
  CONSTRAINT `FK_m3chxntci4jk95pmejnh49owj` FOREIGN KEY (`series_id`) REFERENCES `cms_media_series` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_media_series`
-- ----------------------------
DROP TABLE IF EXISTS `cms_media_series`;
CREATE TABLE `cms_media_series` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `actor` varchar(255) DEFAULT NULL,
  `actor_pinyin` varchar(255) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `audit_comment` varchar(255) DEFAULT NULL,
  `audit_status` int(11) DEFAULT '0',
  `audit_time` datetime DEFAULT NULL,
  `audit_user` varchar(255) DEFAULT NULL,
  `authorize_address` varchar(255) DEFAULT NULL,
  `authorize_info` varchar(255) DEFAULT NULL,
  `broadcast_license` varchar(255) DEFAULT NULL,
  `caption` varchar(255) DEFAULT NULL,
  `cloud_code` varchar(255) DEFAULT NULL,
  `cloud_id` varchar(255) DEFAULT NULL,
  `compere` varchar(255) DEFAULT NULL,
  `content_type` int(11) NOT NULL,
  `copy_right` varchar(255) DEFAULT NULL,
  `cp_code` varchar(255) DEFAULT NULL,
  `cp_id` varchar(255) DEFAULT NULL,
  `director` varchar(255) DEFAULT NULL,
  `director_pinyin` varchar(255) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `en_name` varchar(255) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `guest` varchar(255) DEFAULT NULL,
  `hate_num` int(11) DEFAULT '0',
  `image1` varchar(255) DEFAULT NULL,
  `image1_code` varchar(255) DEFAULT NULL,
  `image2` varchar(255) DEFAULT NULL,
  `image2_code` varchar(255) DEFAULT NULL,
  `image3` varchar(255) DEFAULT NULL,
  `image4` varchar(255) DEFAULT NULL,
  `incharge` varchar(255) DEFAULT NULL,
  `info` varchar(255) DEFAULT NULL,
  `injection_status` varchar(255) DEFAULT '0',
  `internal_tag` varchar(255) DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `kpeople` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `licensing_window_end` datetime DEFAULT NULL,
  `licensing_window_start` datetime DEFAULT NULL,
  `love_num` int(11) DEFAULT '0',
  `media_status` int(11) DEFAULT '110',
  `name` varchar(255) NOT NULL,
  `offline_time` datetime DEFAULT NULL,
  `offline_user` varchar(255) DEFAULT NULL,
  `online_time` datetime DEFAULT NULL,
  `online_user` varchar(255) DEFAULT NULL,
  `order_number` varchar(255) DEFAULT NULL,
  `org_air_date` datetime DEFAULT NULL,
  `play_code` varchar(255) DEFAULT NULL,
  `play_code_status` int(11) DEFAULT '0',
  `rating` float DEFAULT NULL,
  `reporter` varchar(255) DEFAULT NULL,
  `reserved1` varchar(255) DEFAULT NULL,
  `reserved2` varchar(255) DEFAULT NULL,
  `reserved3` varchar(255) DEFAULT NULL,
  `reserved4` varchar(255) DEFAULT NULL,
  `reserved5` varchar(255) DEFAULT NULL,
  `script_writer` varchar(255) DEFAULT NULL,
  `search_name` varchar(255) DEFAULT NULL,
  `site_code` varchar(255) DEFAULT NULL,
  `sort_name` varchar(255) DEFAULT NULL,
  `source` int(11) DEFAULT '0',
  `sp_code` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `storage_path` varchar(255) DEFAULT NULL,
  `storage_time` datetime DEFAULT NULL,
  `submit_time` datetime DEFAULT NULL,
  `submit_user` varchar(255) DEFAULT NULL,
  `subtitle` int(11) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `template_id` varchar(255) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `viewpoint` varchar(255) DEFAULT NULL,
  `vsp_code` varchar(255) DEFAULT NULL,
  `year` varchar(255) DEFAULT NULL,
  `episode_total` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_media_split`
-- ----------------------------
DROP TABLE IF EXISTS `cms_media_split`;
CREATE TABLE `cms_media_split` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `item_type` int(11) DEFAULT NULL,
  `program_id` bigint(20) DEFAULT NULL,
  `site_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_media_template`
-- ----------------------------
DROP TABLE IF EXISTS `cms_media_template`;
CREATE TABLE `cms_media_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `a_bitrate` int(11) DEFAULT NULL,
  `a_codec` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT 'default',
  `code` varchar(255) DEFAULT NULL,
  `definition` varchar(255) DEFAULT NULL,
  `external_code` varchar(255) DEFAULT NULL,
  `prefix_desc` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `suffix_desc` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `title_tag` varchar(255) DEFAULT NULL,
  `transcode_mode` int(11) DEFAULT '1',
  `type` int(11) DEFAULT NULL,
  `v_2pass` int(11) DEFAULT NULL,
  `v_bitrate` int(11) DEFAULT NULL,
  `v_bitrate_mode` varchar(255) DEFAULT NULL,
  `v_codec` varchar(255) DEFAULT NULL,
  `v_format` varchar(255) DEFAULT NULL,
  `v_framerate` int(11) DEFAULT NULL,
  `v_gop` int(11) DEFAULT NULL,
  `v_max_bitrate` int(11) DEFAULT NULL,
  `v_min_bitrate` int(11) DEFAULT NULL,
  `v_profile` varchar(255) DEFAULT NULL,
  `v_profile_level` varchar(255) DEFAULT NULL,
  `v_resolution` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cms_media_template_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_media_trailer`
-- ----------------------------
DROP TABLE IF EXISTS `cms_media_trailer`;
CREATE TABLE `cms_media_trailer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `item_type` int(11) DEFAULT NULL,
  `program_id` bigint(20) DEFAULT NULL,
  `site_code` varchar(255) DEFAULT NULL,
  `sort_index` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_site`
-- ----------------------------
DROP TABLE IF EXISTS `cms_site`;
CREATE TABLE `cms_site` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `code` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cms_site_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_star`
-- ----------------------------
DROP TABLE IF EXISTS `cms_star`;
CREATE TABLE `cms_star` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `arm` int(11) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `cloud_code` varchar(255) DEFAULT NULL,
  `cloud_id` varchar(255) DEFAULT NULL,
  `club_id` bigint(20) DEFAULT NULL,
  `constellation` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `determine` varchar(255) DEFAULT NULL,
  `ename` varchar(255) DEFAULT NULL,
  `evaluate` varchar(255) DEFAULT NULL,
  `greet_rate` float DEFAULT NULL,
  `hate_num` int(11) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `honor` varchar(255) DEFAULT NULL,
  `image1` varchar(255) DEFAULT NULL,
  `image2` varchar(255) DEFAULT NULL,
  `info` varchar(255) DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `ko` varchar(255) DEFAULT NULL,
  `leg` int(11) DEFAULT NULL,
  `love_num` int(11) DEFAULT NULL,
  `military_exploits` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `nation` varchar(255) DEFAULT NULL,
  `no` varchar(255) DEFAULT NULL,
  `offline_time` datetime DEFAULT NULL,
  `offline_user` varchar(255) DEFAULT NULL,
  `online_time` datetime DEFAULT NULL,
  `online_user` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `surrender` varchar(255) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL,
  `weight` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_transcode_request`
-- ----------------------------
DROP TABLE IF EXISTS `cms_transcode_request`;
CREATE TABLE `cms_transcode_request` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `content_type` int(11) DEFAULT NULL,
  `cover_file` int(11) NOT NULL,
  `cp_id` varchar(255) DEFAULT NULL,
  `episode_total` int(11) DEFAULT NULL,
  `gen_program_name_rule` int(11) NOT NULL,
  `gen_task` int(11) NOT NULL,
  `internal_tag` varchar(255) DEFAULT NULL,
  `media_filename` varchar(255) DEFAULT NULL,
  `media_id` bigint(20) DEFAULT NULL,
  `media_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `need_snapshot` int(11) NOT NULL,
  `new_media` int(11) NOT NULL,
  `origin_file_deal` int(11) NOT NULL,
  `priority` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `task_fail` bigint(20) NOT NULL,
  `task_success` bigint(20) NOT NULL,
  `task_total` bigint(20) NOT NULL,
  `template_id` varchar(255) DEFAULT NULL,
  `time_points` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_transcode_request_file`
-- ----------------------------
DROP TABLE IF EXISTS `cms_transcode_request_file`;
CREATE TABLE `cms_transcode_request_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `episode_index` int(11) DEFAULT NULL,
  `file_path` varchar(255) NOT NULL,
  `media_file_type` int(11) NOT NULL,
  `media_filename` varchar(255) DEFAULT NULL,
  `media_id` bigint(20) DEFAULT NULL,
  `media_name` varchar(255) DEFAULT NULL,
  `new_media` int(11) NOT NULL,
  `request_id` bigint(20) DEFAULT NULL,
  `subtitle_file_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_afcqkiecgw41o9ic1dyvhnau8` (`request_id`),
  CONSTRAINT `FK_afcqkiecgw41o9ic1dyvhnau8` FOREIGN KEY (`request_id`) REFERENCES `cms_transcode_request` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_transcode_task`
-- ----------------------------
DROP TABLE IF EXISTS `cms_transcode_task`;
CREATE TABLE `cms_transcode_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `cloud_task_id` varchar(255) DEFAULT NULL,
  `first_request_time` datetime DEFAULT NULL,
  `input_file_path` varchar(255) NOT NULL,
  `input_subtitle` varchar(255) DEFAULT NULL,
  `is_callback` int(11) DEFAULT '0',
  `last_request_time` datetime DEFAULT NULL,
  `media_file_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `output_file_path` varchar(255) NOT NULL,
  `pre_task_id` bigint(20) DEFAULT NULL,
  `pre_task_status` int(11) DEFAULT NULL,
  `priority` int(11) DEFAULT '1',
  `profile` varchar(255) DEFAULT NULL,
  `program_id` bigint(20) NOT NULL,
  `request_file_id` bigint(20) NOT NULL,
  `request_id` bigint(20) NOT NULL,
  `request_times` int(11) DEFAULT NULL,
  `request_total_times` int(11) DEFAULT NULL,
  `response_code` varchar(255) DEFAULT NULL,
  `response_msg` varchar(255) DEFAULT NULL,
  `response_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  `template_id` bigint(20) NOT NULL,
  `time_points` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  View structure for `cms_league_season_club_view`
-- ----------------------------
DROP VIEW IF EXISTS `cms_league_season_club_view`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `cms_league_season_club_view` AS select `item`.`id` AS `id`,`item`.`league_season_id` AS `league_season_id`,`item`.`item_type` AS `item_type`,`item`.`item_id` AS `item_id`,`item`.`title` AS `title`,`item`.`image1` AS `image1`,`item`.`image2` AS `image2`,`item`.`sort_index` AS `sort_index`,`item`.`status` AS `status`,`item`.`create_time` AS `create_time`,`item`.`win_num` AS `win_num`,`item`.`flat_num` AS `flat_num`,`item`.`lose_num` AS `lose_num`,`item`.`enter_num` AS `enter_num`,`item`.`point_num` AS `point_num`,`item`.`fumble_num` AS `fumble_num`,`item`.`credit_num` AS `credit_num`,`club`.`status` AS `item_status`,`club`.`name` AS `item_name`,`club`.`name` AS `item_title`,`club`.`image1` AS `item_image1`,`club`.`image2` AS `item_image2`,`club`.`short_name` AS `short_name` from (`cms_league_season_club` `item` join `cms_club` `club`) where ((`item`.`item_id` = `club`.`id`) and (`item`.`item_type` = 10));

-- ----------------------------
--  View structure for `cms_league_season_star_view`
-- ----------------------------
DROP VIEW IF EXISTS `cms_league_season_star_view`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `cms_league_season_star_view` AS select `item`.`id` AS `id`,`item`.`league_season_id` AS `league_season_id`,`item`.`item_type` AS `item_type`,`item`.`item_id` AS `item_id`,`item`.`title` AS `title`,`item`.`image1` AS `image1`,`item`.`image2` AS `image2`,`item`.`sort_index` AS `sort_index`,`item`.`status` AS `status`,`item`.`create_time` AS `create_time`,`item`.`site_num` AS `site_num`,`item`.`enter_num` AS `enter_num`,`item`.`point_num` AS `point_num`,`item`.`assist_num` AS `assist_num`,`club`.`name` AS `club_name`,`star`.`status` AS `item_status`,`star`.`name` AS `item_name`,`star`.`name` AS `item_title`,`star`.`image1` AS `item_image1`,`star`.`image2` AS `item_image2` from ((`cms_league_season_star` `item` join `cms_star` `star`) join `cms_club` `club`) where ((`item`.`item_id` = `star`.`id`) and (`item`.`item_type` = 9) and (`star`.`club_id` = `club`.`id`));

SET FOREIGN_KEY_CHECKS = 1;
