ALTER TABLE `epg_content_program` ADD COLUMN `image3` varchar(255) DEFAULT NULL;
ALTER TABLE `epg_content_program` ADD COLUMN `image4` varchar(255) DEFAULT NULL;
ALTER TABLE `epg_content_series` ADD COLUMN `image3` varchar(255) DEFAULT NULL;
ALTER TABLE `epg_content_series` ADD COLUMN `image4` varchar(255) DEFAULT NULL;

ALTER TABLE `epg_content_program` ADD COLUMN `image3_code` varchar(255) DEFAULT NULL;
ALTER TABLE `epg_content_program` ADD COLUMN `image4_code` varchar(255) DEFAULT NULL;
ALTER TABLE `epg_content_series` ADD COLUMN `image3_code` varchar(255) DEFAULT NULL;
ALTER TABLE `epg_content_series` ADD COLUMN `image4_code` varchar(255) DEFAULT NULL;

ALTER TABLE `epg_media_file` ADD COLUMN `source_file_path` varchar(255) DEFAULT NULL;
ALTER TABLE `epg_media_file` ADD COLUMN `source_file_md5` varchar(255) DEFAULT NULL;
ALTER TABLE `epg_media_file` ADD COLUMN `source_file_size` bigint DEFAULT NULL;

ALTER TABLE `com_cp` ADD COLUMN `short_name` varchar(255) DEFAULT NULL;
ALTER TABLE `com_cp` ADD COLUMN `type` int DEFAULT 1;


CREATE TABLE `cms_media_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `cloud_code` varchar(255) DEFAULT NULL,
  `cloud_id` varchar(255) DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `file_size` bigint(20) DEFAULT NULL,
  `program_id` bigint(20) DEFAULT NULL,
  `series_id` bigint(20) DEFAULT NULL,
  `sort_index` int(11) DEFAULT NULL,
  `source` int(11) NOT NULL,
  `source_file_name` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  `media_spec` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `title` varchar(255) DEFAULT NULL,
  `transcode_mode` int(11) DEFAULT '1',
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


CREATE TABLE `cms_injection_download_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `file_md5` varchar(255) DEFAULT NULL,
  `file_size` bigint(20) DEFAULT NULL,
  `first_request_time` datetime DEFAULT NULL,
  `input_file_path` varchar(255) DEFAULT NULL,
  `last_request_time` datetime DEFAULT NULL,
  `media_file_id` bigint(20) DEFAULT NULL,
  `module` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `output_file_path` varchar(255) DEFAULT NULL,
  `percent` int(11) DEFAULT NULL,
  `pid` bigint(20) NOT NULL,
  `priority` int(11) DEFAULT NULL,
  `program_id` bigint(20) DEFAULT NULL,
  `request_times` int(11) DEFAULT NULL,
  `request_total_times` int(11) DEFAULT NULL,
  `response_code` varchar(255) DEFAULT NULL,
  `response_msg` varchar(255) DEFAULT NULL,
  `response_time` datetime DEFAULT NULL,
  `source_file_md5` varchar(255) DEFAULT NULL,
  `source_file_size` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `cms_injection_object` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `category` varchar(255) NOT NULL,
  `injection_status` int(11) DEFAULT '0',
  `injection_time` datetime DEFAULT NULL,
  `item_id` bigint(20) NOT NULL,
  `item_parent_id` bigint(20) DEFAULT NULL,
  `item_type` int(11) NOT NULL,
  `partner_item_code` varchar(255) DEFAULT NULL,
  `partner_item_reserved1` varchar(255) DEFAULT NULL,
  `partner_item_reserved2` varchar(255) DEFAULT NULL,
  `platform_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cms_injection_object_index` (`platform_id`,`category`,`item_type`,`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `cms_injection_platform` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `code_prefix` varchar(255) DEFAULT '00000000',
  `correlate_prefix` varchar(255) DEFAULT NULL,
  `csp_id` varchar(255) DEFAULT NULL,
  `depend_platform_id` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `direction` int(11) DEFAULT '1',
  `indirect_platform_id` varchar(255) DEFAULT NULL,
  `injection_platform_id` varchar(255) DEFAULT NULL,
  `interface_mode` int(11) DEFAULT '1',
  `is_callback` int(11) DEFAULT '0',
  `is_wsdl` int(11) DEFAULT '1',
  `live_service_url` varchar(255) DEFAULT NULL,
  `lsp_id` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `namespace` varchar(255) DEFAULT 'iptv',
  `need_audit` int(11) DEFAULT '1',
  `need_delete_media_file` int(11) DEFAULT '1',
  `need_download_video` int(11) DEFAULT '1',
  `need_image_object` int(11) DEFAULT '1',
  `need_injection` int(11) DEFAULT '0',
  `need_packing_program` int(11) DEFAULT '0',
  `platform_code` varchar(255) DEFAULT NULL,
  `play_code_custom` int(11) DEFAULT '0',
  `provider` int(11) DEFAULT '0',
  `separate_char` varchar(255) DEFAULT ',',
  `service_url` varchar(255) DEFAULT NULL,
  `site_code` varchar(255) NOT NULL,
  `status` int(11) DEFAULT '0',
  `template_custom` int(11) DEFAULT '0',
  `template_filename` varchar(255) DEFAULT NULL,
  `template_id` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `cms_injection_receive_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `cmd_file_url` varchar(1024) DEFAULT NULL,
  `cmd_result` int(11) DEFAULT NULL,
  `correlate_id` varchar(255) DEFAULT NULL,
  `download_fail` bigint(20) NOT NULL,
  `download_success` bigint(20) NOT NULL,
  `download_times` int(11) DEFAULT '0',
  `download_total` bigint(20) NOT NULL,
  `first_response_time` datetime DEFAULT NULL,
  `last_response_time` datetime DEFAULT NULL,
  `platform_id` bigint(20) NOT NULL,
  `priority` int(11) DEFAULT '1',
  `receive_time` datetime NOT NULL,
  `reply_error_description` varchar(1024) DEFAULT NULL,
  `reply_result` int(11) DEFAULT NULL,
  `request_error_description` varchar(1024) DEFAULT NULL,
  `request_result` int(11) DEFAULT NULL,
  `request_xml_file_content` longtext,
  `request_xml_file_path` varchar(1024) DEFAULT NULL,
  `response_error_description` varchar(1024) DEFAULT NULL,
  `response_result` int(11) DEFAULT NULL,
  `response_status` int(11) DEFAULT '0',
  `response_times` int(11) DEFAULT '0',
  `response_total_times` int(11) DEFAULT '0',
  `response_xml_file_content` longtext,
  `response_xml_file_path` varchar(1024) DEFAULT NULL,
  `result_file_url` varchar(1024) DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



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
  `next_check_time` datetime DEFAULT NULL,
  `operation_object_ids` longtext,
  `platform_id` bigint(20) NOT NULL,
  `pre_task_id` bigint(20) DEFAULT NULL,
  `pre_task_status` int(11) DEFAULT NULL,
  `priority` int(11) DEFAULT '1',
  `request_error_description` varchar(1024) DEFAULT NULL,
  `request_result` int(11) DEFAULT NULL,
  `request_times` int(11) DEFAULT '0',
  `request_total_times` int(11) DEFAULT '0',
  `request_xml_file_content` longtext,
  `request_xml_file_path` varchar(1024) DEFAULT NULL,
  `response_error_description` varchar(1024) DEFAULT NULL,
  `response_result` int(11) DEFAULT NULL,
  `response_time` datetime DEFAULT NULL,
  `response_xml_file_content` longtext,
  `response_xml_file_path` varchar(1024) DEFAULT NULL,
  `result_file_url` varchar(1024) DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  `sub_item_id` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `cms_media_template` VALUES 
('-1', '2018-04-20 08:12:15', '2018-04-20 08:14:13', '128', 'AAC', 'default', 'UNKNOWN', 'HD', '', 'TS-VBR-H264-0-720P-25-AAC-128', '1', '未知', '1', '0', '0', 'VBR', 'H264', 'TS', '25', null, '0', '0', null, null, '720P'), 
('2', '2018-04-20 08:15:12', null, '128', 'AAC', 'default', '8M_VBR', 'HD', '', 'TS-VBR-H264-8000-1080P-25-AAC-128', '1', '8M_VBR', '1', '0', '8000', 'VBR', 'H264', 'TS', '25', null, '8000', '8000', null, null, '1080P')
;

INSERT INTO `cms_injection_platform` VALUES 
('1', '2018-04-20 08:24:10', null, '00000000', '', 'MGTV', '', '', '1', '', '', '1', '0', '1', '', 'MGTV', '芒果专区', 'iptv', '1', '1', '1', '1', '0', '0', null, '0', '0', ',', 'http://localhost:9020/cisreceive/services/ctms?wsdl', 'FUJIANGD', '1', '0', '', '-1,2', '2'),
('2', '2018-04-20 08:25:19', null, '00000000', null, 'MGTV', '', '', '2', '', '', '1', '0', '1', '', 'MGTV', '芒果专区接收', 'iptv', '1', '1', '0', '1', '0', '0', null, '0', '0', ',', 'http://localhost:9010/cissend/services/ctms?wsdl', 'FUJIANGD', '1', '0', null, '', '1')
;

update com_site set code='FUJIANGD',name='福建广电' where id=1;
update cms_injection_platform set csp_id='MGTV',lsp_id='MGTV',service_url='http://localhost:9010/cissend/services/ctms?wsdl' where id=2;