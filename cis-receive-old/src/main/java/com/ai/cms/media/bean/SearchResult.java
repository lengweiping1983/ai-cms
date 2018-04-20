package com.ai.cms.media.bean;

import java.util.Date;

public class SearchResult {
	private Long id;// 管理名称

	private Date createTime;// 创建时间

	private String name;// 管理名称

	private String title;// 显示名称

	private String code;// 内容唯一标识，用于外部系统交互

	private Integer contentType;// 内容类型:1=电影, 2=电视剧, 3=综艺, 4=体育, 5=少儿, 6=纪实,
								// 7=生活, 8=教育, 9=音乐, 10=新闻, 11=财经, 12=法治, 13=电竞,
								// 14=原创
	private String year;// 年份

	private String area;// 地区

	private String language;// 语言

	private Integer duration;// 时长，单位分钟

	private String director;// 导演，使用','分割

	private String actor;// 演员，使用','分割

	private String genres;// 类别，使用','分割

	private String image1;// 横海报

	private String image2;// 竖海报

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getGenres() {
		return genres;
	}

	public void setGenres(String genres) {
		this.genres = genres;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}
}
