use ai_cms;


ALTER TABLE `cms_media_series` ADD COLUMN `global_code` VARCHAR(255);
ALTER TABLE `cms_media_program` ADD COLUMN `global_code` VARCHAR(255);
ALTER TABLE `cms_media_file` ADD COLUMN `global_code` VARCHAR(255);

update cms_media_series set global_code = play_code;
update cms_media_program set global_code = play_code;
update cms_media_file set global_code = play_code;