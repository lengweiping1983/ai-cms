use ai_sys;
ALTER TABLE `sys_operation_log` CHANGE COLUMN `object_id` `object_id` varchar(1024) DEFAULT NULL;
ALTER TABLE `sys_operation_log` CHANGE COLUMN `object_name` `object_name` longtext DEFAULT NULL;
ALTER TABLE `sys_user` CHANGE COLUMN `type` `type` int DEFAULT 1;

use ai_cms;

ALTER TABLE `cms_cp` CHANGE COLUMN `type` `type` int DEFAULT 1;
ALTER TABLE `cms_cp` CHANGE COLUMN `status` `status` int DEFAULT 0;
CREATE UNIQUE INDEX cms_cp_code on cms_cp(code);


ALTER TABLE `cms_site` CHANGE COLUMN `status` `status` int DEFAULT 0;
CREATE UNIQUE INDEX cms_site_code on cms_site(code);


ALTER TABLE `cms_media_template` CHANGE COLUMN `transcode_mode` `transcode_mode` int DEFAULT 1;
ALTER TABLE `cms_media_template` CHANGE COLUMN `status` `status` int DEFAULT 0;
ALTER TABLE `cms_media_template` CHANGE COLUMN `category` `category` varchar(255) DEFAULT 'default';
CREATE UNIQUE INDEX cms_media_template_code on cms_media_template(code);


ALTER TABLE `cms_transcode_task` CHANGE COLUMN `status` `status` int DEFAULT 1;
ALTER TABLE `cms_transcode_task` CHANGE COLUMN `priority` `priority` int DEFAULT 1;
ALTER TABLE `cms_transcode_task` CHANGE COLUMN `is_callback` `is_callback` int DEFAULT 0;


ALTER TABLE `cms_injection_object` CHANGE COLUMN `injection_status` `injection_status` int DEFAULT 0;
CREATE UNIQUE INDEX cms_injection_object_index on cms_injection_object(platform_id,category,item_type,item_id);

ALTER TABLE `cms_injection_platform` CHANGE COLUMN `type` `type` int DEFAULT 1;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `provider` `provider` int DEFAULT 0;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `interface_mode` `interface_mode` int DEFAULT 1;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `need_download_video` `need_download_video` int DEFAULT 1;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `need_audit` `need_audit` int DEFAULT 1;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `need_injection` `need_injection` int DEFAULT 0;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `is_wsdl` `is_wsdl` int DEFAULT 1;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `namespace` `namespace` varchar(255) DEFAULT 'iptv';
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `direction` `direction` int DEFAULT 1;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `is_callback` `is_callback` int DEFAULT 0;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `status` `status` int DEFAULT 0;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `template_custom` `template_custom` int DEFAULT 0;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `play_code_custom` `play_code_custom` int DEFAULT 0;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `code_prefix` `code_prefix` varchar(255) DEFAULT '00000000';
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `need_image_object` `need_image_object` int DEFAULT 1;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `need_packing_program` `need_packing_program` int DEFAULT 0;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `need_delete_media_file` `need_delete_media_file` int DEFAULT 1;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `separate_char` `separate_char` varchar(255) DEFAULT ',';


ALTER TABLE `cms_injection_send_task` CHANGE COLUMN `cmd_file_url` `cmd_file_url` varchar(1024) DEFAULT NULL;
ALTER TABLE `cms_injection_send_task` CHANGE COLUMN `result_file_url` `result_file_url` varchar(1024) DEFAULT NULL;
ALTER TABLE `cms_injection_send_task` CHANGE COLUMN `request_error_description` `request_error_description` varchar(1024) DEFAULT NULL;
ALTER TABLE `cms_injection_send_task` CHANGE COLUMN `request_xml_file_path` `request_xml_file_path` varchar(1024) DEFAULT NULL;
ALTER TABLE `cms_injection_send_task` CHANGE COLUMN `response_error_description` `response_error_description` varchar(1024) DEFAULT NULL;
ALTER TABLE `cms_injection_send_task` CHANGE COLUMN `response_xml_file_path` `response_xml_file_path` varchar(1024) DEFAULT NULL;

ALTER TABLE `cms_injection_send_task` CHANGE COLUMN `response_xml_file_content` `response_xml_file_content` longtext DEFAULT NULL;
ALTER TABLE `cms_injection_send_task` CHANGE COLUMN `request_xml_file_content` `request_xml_file_content` longtext DEFAULT NULL;
ALTER TABLE `cms_injection_send_task` CHANGE COLUMN `operation_object_ids` `operation_object_ids` longtext DEFAULT NULL;

ALTER TABLE `cms_injection_receive_task` CHANGE COLUMN `cmd_file_url` `cmd_file_url` varchar(1024) DEFAULT NULL;
ALTER TABLE `cms_injection_receive_task` CHANGE COLUMN `result_file_url` `result_file_url` varchar(1024) DEFAULT NULL;
ALTER TABLE `cms_injection_receive_task` CHANGE COLUMN `request_error_description` `request_error_description` varchar(1024) DEFAULT NULL;
ALTER TABLE `cms_injection_receive_task` CHANGE COLUMN `request_xml_file_path` `request_xml_file_path` varchar(1024) DEFAULT NULL;
ALTER TABLE `cms_injection_receive_task` CHANGE COLUMN `response_error_description` `response_error_description` varchar(1024) DEFAULT NULL;
ALTER TABLE `cms_injection_receive_task` CHANGE COLUMN `response_xml_file_path` `response_xml_file_path` varchar(1024) DEFAULT NULL;
ALTER TABLE `cms_injection_receive_task` CHANGE COLUMN `response_xml_file_content` `response_xml_file_content` longtext DEFAULT NULL;
ALTER TABLE `cms_injection_receive_task` CHANGE COLUMN `request_xml_file_content` `request_xml_file_content` longtext DEFAULT NULL;
ALTER TABLE `cms_injection_receive_task` CHANGE COLUMN `reply_error_description` `reply_error_description` varchar(1024) DEFAULT NULL;

ALTER TABLE `cms_injection_send_task` CHANGE COLUMN `status` `status` int DEFAULT 1;
ALTER TABLE `cms_injection_send_task` CHANGE COLUMN `priority` `priority` int DEFAULT 1;
ALTER TABLE `cms_injection_send_task` CHANGE COLUMN `request_times` `request_times` int DEFAULT 0;
ALTER TABLE `cms_injection_send_task` CHANGE COLUMN `request_total_times` `request_total_times` int DEFAULT 0;

ALTER TABLE `cms_injection_receive_task` CHANGE COLUMN `status` `status` int DEFAULT 1;
ALTER TABLE `cms_injection_receive_task` CHANGE COLUMN `priority` `priority` int DEFAULT 1;
ALTER TABLE `cms_injection_receive_task` CHANGE COLUMN `response_status` `response_status` int DEFAULT 0;
ALTER TABLE `cms_injection_receive_task` CHANGE COLUMN `download_times` `download_times` int DEFAULT 0;
ALTER TABLE `cms_injection_receive_task` CHANGE COLUMN `response_times` `response_times` int DEFAULT 0;
ALTER TABLE `cms_injection_receive_task` CHANGE COLUMN `response_total_times` `response_total_times` int DEFAULT 0;


ALTER TABLE `cms_media_series` CHANGE COLUMN `source` `source` int DEFAULT 0;
ALTER TABLE `cms_media_series` CHANGE COLUMN `love_num` `love_num` int DEFAULT 0;
ALTER TABLE `cms_media_series` CHANGE COLUMN `hate_num` `hate_num` int DEFAULT 0;
ALTER TABLE `cms_media_series` CHANGE COLUMN `status` `status` int DEFAULT 0;
ALTER TABLE `cms_media_series` CHANGE COLUMN `media_status` `media_status` int DEFAULT 110;
ALTER TABLE `cms_media_series` CHANGE COLUMN `audit_status` `audit_status` int DEFAULT 0;
ALTER TABLE `cms_media_series` CHANGE COLUMN `play_code_status` `play_code_status` int DEFAULT 0;
ALTER TABLE `cms_media_series` CHANGE COLUMN `injection_status` `injection_status` varchar(255) DEFAULT '0';


ALTER TABLE `cms_media_program` CHANGE COLUMN `type` `type` int DEFAULT 1;
ALTER TABLE `cms_media_program` CHANGE COLUMN `episode_index` `episode_index` int DEFAULT 1;

ALTER TABLE `cms_media_program` CHANGE COLUMN `source` `source` int DEFAULT 0;
ALTER TABLE `cms_media_program` CHANGE COLUMN `love_num` `love_num` int DEFAULT 0;
ALTER TABLE `cms_media_program` CHANGE COLUMN `hate_num` `hate_num` int DEFAULT 0;
ALTER TABLE `cms_media_program` CHANGE COLUMN `status` `status` int DEFAULT 0;
ALTER TABLE `cms_media_program` CHANGE COLUMN `media_status` `media_status` int DEFAULT 110;
ALTER TABLE `cms_media_program` CHANGE COLUMN `audit_status` `audit_status` int DEFAULT 0;
ALTER TABLE `cms_media_program` CHANGE COLUMN `play_code_status` `play_code_status` int DEFAULT 0;
ALTER TABLE `cms_media_program` CHANGE COLUMN `injection_status` `injection_status` varchar(255) DEFAULT '0';



ALTER TABLE `cms_media_file` CHANGE COLUMN `type` `type` int DEFAULT 1;
ALTER TABLE `cms_media_file` CHANGE COLUMN `episode_index` `episode_index` int DEFAULT 1;

ALTER TABLE `cms_media_file` CHANGE COLUMN `source` `source` int DEFAULT 0;
ALTER TABLE `cms_media_file` CHANGE COLUMN `status` `status` int DEFAULT 0;
ALTER TABLE `cms_media_file` CHANGE COLUMN `media_status` `media_status` int DEFAULT 110;
ALTER TABLE `cms_media_file` CHANGE COLUMN `play_code_status` `play_code_status` int DEFAULT 0;
ALTER TABLE `cms_media_file` CHANGE COLUMN `injection_status` `injection_status` varchar(255) DEFAULT '0';
ALTER TABLE `cms_media_file` CHANGE COLUMN `template_id` `template_id` bigint DEFAULT -1;