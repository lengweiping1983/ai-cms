use ai_cms;

ALTER TABLE `cms_injection_object` CHANGE COLUMN `injection_status` `injection_status` int DEFAULT 0;

update cms_injection_object set injection_status=0 where injection_status=1;
update cms_media_file set injection_status='0' where injection_status='1';
update cms_media_program set injection_status='0' where injection_status='1';
update cms_media_series set injection_status='0' where injection_status='1';

ALTER TABLE `cms_injection_platform` ADD COLUMN `need_audit` int DEFAULT 1;


-- CREATE UNIQUE INDEX cms_media_file_program_type_template on cms_media_file(`program_id`,`type`,`template_id`);

DROP INDEX cms_media_file_program_type_template on cms_media_file;


INSERT INTO `cms_media_template` (`id`, `create_time`, `update_time`, `code`, `external_code`, `status`, `title`, `title_tag`, `type`, `a_bitrate`, `a_codec`, `v_2pass`, `v_bitrate`, `v_codec`, `v_format`, `v_framerate`, `v_gop`, `v_max_bitrate`, `v_min_bitrate`, `v_profile`, `v_profile_level`, `v_resolution`, `v_bitrate_mode`, `transcode_mode`, `category`, `definition`, `prefix_desc`, `suffix_desc`) 
VALUES (-1,'2018-03-10 14:28:16',NULL,'未知','未知',1,'未知','未知',0,0,'',0,0,'','TS',0,NULL,NULL,NULL,NULL,NULL,'','',1,NULL,NULL,NULL,NULL);