use ai_cms;

ALTER TABLE `cms_injection_receive_task` CHANGE COLUMN `reply_error_description` `reply_error_description` varchar(1024) DEFAULT NULL;

update cms_injection_platform set direction=2 where direction=1;
update cms_injection_platform set direction=1 where direction=0;



ALTER TABLE `cms_injection_platform` ADD COLUMN `need_image_object` int DEFAULT 1;
ALTER TABLE `cms_injection_platform` ADD COLUMN `need_packing_program` int DEFAULT 0;
ALTER TABLE `cms_injection_platform` ADD COLUMN `need_delete_media_file` int DEFAULT 1;
ALTER TABLE `cms_injection_platform` ADD COLUMN `separate_char` varchar(255) DEFAULT ',';


ALTER TABLE `cms_injection_platform` CHANGE COLUMN `direction` `direction` int DEFAULT 1;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `csp_id` `csp_id` varchar(255) DEFAULT NULL;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `lsp_id` `lsp_id` varchar(255) DEFAULT NULL;
ALTER TABLE `cms_injection_platform` CHANGE COLUMN `service_url` `service_url` varchar(255) DEFAULT NULL;


drop table cms_injection_send_event;
drop table cms_media_import;