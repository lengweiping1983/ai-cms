use ai_cms;

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


ALTER TABLE `cms_media_program` ADD COLUMN `original_name` varchar(255) DEFAULT NULL;
ALTER TABLE `cms_media_series` ADD COLUMN `original_name` varchar(255) DEFAULT NULL;

ALTER TABLE `cms_media_program` ADD COLUMN `image3_code` varchar(255) DEFAULT NULL;
ALTER TABLE `cms_media_program` ADD COLUMN `image4_code` varchar(255) DEFAULT NULL;
ALTER TABLE `cms_media_series` ADD COLUMN `image3_code` varchar(255) DEFAULT NULL;
ALTER TABLE `cms_media_series` ADD COLUMN `image4_code` varchar(255) DEFAULT NULL;


ALTER TABLE `cms_media_file` ADD COLUMN `subtitle` int DEFAULT NULL;
ALTER TABLE `cms_media_file` CHANGE COLUMN `template_id` `template_id` int NOT NULL DEFAULT -1;

ALTER TABLE `cms_injection_platform` ADD COLUMN `need_injection` int DEFAULT 0;