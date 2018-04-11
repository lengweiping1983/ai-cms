
CREATE or REPLACE VIEW `cms_league_season_star_view`
AS
SELECT `item`.`id` AS `id`, 
       `item`.`league_season_id` AS `league_season_id`,
       `item`.`item_type` AS `item_type`,
       `item`.`item_id` AS `item_id`,
       `item`.`title` AS `title`, 
       `item`.`image1` AS `image1`, 
       `item`.`image2` AS `image2`,
       `item`.`sort_index` AS `sort_index`,
       `item`.`status` AS `status`,
       `item`.`create_time` AS `create_time`, 
       `item`.`site_num` AS `site_num`,
       `item`.`enter_num` AS `enter_num`,
       `item`.`point_num` AS `point_num`,
       `item`.`assist_num` AS `assist_num`,
       `club`.`name` AS `club_name`,
       `star`.`status` AS `item_status`,
       `star`.`name` AS `item_name`,
       `star`.`name` AS `item_title`,
       `star`.`image1` AS `item_image1`,
       `star`.`image2` AS `item_image2`
FROM `cms_league_season_star` `item`, `cms_star` `star`, `cms_club` `club`
WHERE `item`.`item_id` = `star`.`id`
	AND `item`.`item_type` = 9
	AND `star`.`club_id` = `club`.`id`;


CREATE or REPLACE VIEW `cms_league_season_club_view` AS SELECT
	`item`.`id` AS `id`,
	`item`.`league_season_id` AS `league_season_id`,
	`item`.`item_type` AS `item_type`,
	`item`.`item_id` AS `item_id`,
	`item`.`title` AS `title`,
	`item`.`image1` AS `image1`,
	`item`.`image2` AS `image2`,
	`item`.`sort_index` AS `sort_index`,
	`item`.`status` AS `status`,
	`item`.`create_time` AS `create_time`,
	`item`.`win_num` AS `win_num`,
	`item`.`flat_num` AS `flat_num`,
	`item`.`lose_num` AS `lose_num`,
	`item`.`enter_num` AS `enter_num`,
	`item`.`point_num` AS `point_num`,
	`item`.`fumble_num` AS `fumble_num`,
	`item`.`credit_num` AS `credit_num`,
	`club`.`status` AS `item_status`,
	`club`.`name` AS `item_name`,
	`club`.`name` AS `item_title`,
	`club`.`image1` AS `item_image1`,
	`club`.`image2` AS `item_image2`,
	`club`.`short_name` AS `short_name`
FROM
	(
		`cms_league_season_club` `item`
		JOIN `cms_club` `club`
	)
WHERE
	(
		(
			`item`.`item_id` = `club`.`id`
		)
		AND (`item`.`item_type` = 10)
	);