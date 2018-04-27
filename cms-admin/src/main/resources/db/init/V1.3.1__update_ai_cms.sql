use ai_cms;

ALTER TABLE `cms_media_import` ADD COLUMN `audit_status` VARCHAR(255);
ALTER TABLE `cms_media_import` ADD COLUMN `cp_code` VARCHAR(255);

ALTER TABLE `cms_media_series` ADD COLUMN `receive_code` VARCHAR(255);
ALTER TABLE `cms_media_program` ADD COLUMN `receive_code` VARCHAR(255);
ALTER TABLE `cms_media_file` ADD COLUMN `receive_code` VARCHAR(255);

ALTER TABLE `cms_cp_ftp` DROP COLUMN `ip`;
ALTER TABLE `cms_cp_ftp` DROP COLUMN `port`;
ALTER TABLE `cms_cp_ftp` DROP COLUMN `username`;
ALTER TABLE `cms_cp_ftp` DROP COLUMN `password`;

ALTER TABLE `cms_injection_platform` ADD COLUMN `use_global_code` int DEFAULT 1;