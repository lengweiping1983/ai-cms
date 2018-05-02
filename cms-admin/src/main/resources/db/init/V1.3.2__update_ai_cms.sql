use ai_cms;


ALTER TABLE `cms_media_series` ADD COLUMN `global_code` VARCHAR(255);
ALTER TABLE `cms_media_program` ADD COLUMN `global_code` VARCHAR(255);
ALTER TABLE `cms_media_file` ADD COLUMN `global_code` VARCHAR(255);

update cms_media_series set global_code = play_code;
update cms_media_program set global_code = play_code;
update cms_media_file set global_code = play_code;


ALTER TABLE `cms_injection_receive_task` ADD COLUMN `next_check_time` datetime;

CREATE TABLE `cms_injection_receive_object` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `item_id` bigint(20) NOT NULL,
  `item_type` int(11) NOT NULL,
  `module` int(11) NOT NULL,
  `pid` bigint(20) NOT NULL,
  `receive_code` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;