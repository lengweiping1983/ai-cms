use ai_cms;

ALTER TABLE `cms_injection_platform` ADD COLUMN `type` int DEFAULT 1;
ALTER TABLE `cms_injection_platform` ADD COLUMN `depend_platform_id` varchar(255) DEFAULT NULL;

ALTER TABLE `cms_injection_platform` ADD COLUMN `need_download_image` int DEFAULT 0;
ALTER TABLE `cms_injection_platform` ADD COLUMN `need_download_video` int DEFAULT 0;