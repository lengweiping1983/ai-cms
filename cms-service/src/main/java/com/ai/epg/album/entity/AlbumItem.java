package com.ai.epg.album.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.TemplateParamCategoryEnum;
import com.ai.common.utils.DateUtils;

/**
 * 专题项实体
 */
@Entity
@Table(name = "epg_album_item")
public class AlbumItem extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "album_id")
	private Long albumId;// 所属专题Id

	@Column(name = "item_type")
	private Integer itemType;// 元素类型

	@Column(name = "item_id")
	private Long itemId;// 元素标识

	private String title;// 定制名称

	private String image1;// 定制横海报

	private String image2;// 定制竖海报

	@Column(name = "sort_index")
	private Integer sortIndex = 1; // 排序值

	@Column(name = "status")
	private Integer status = OnlineStatusEnum.ONLINE.getKey(); // 状态:0=未上线、1=已上线、2=已下线
	
	@Column(name = "item_status")
	private Integer itemStatus = OnlineStatusEnum.DEFAULT.getKey(); // 元素状态:0=未上线、1=已上线、2=已下线

	@Column(name = "jump_item_type")
	private Integer jumpItemType;// 跳转元素类型

	@Column(name = "jump_item_id")
	private Long jumpItemId;// 跳转元素标识

	@Column(name = "jump_type")
	private Integer jumpType;// 跳转方式
	
	private String url;// 链接地址

	@Column(name = "jump_param1")
	private String jumpParam1;// 跳转参数1，在api中整合成具体调整的url

	@Column(name = "jump_param2")
	private String jumpParam2;// 跳转参数2，在api中整合成具体调整的url
	
	@Column(name = "internal_param")
	private String internalParam;// 内部参数
	
	@Column(name = "internal_param_category")
	private Integer internalParamCategory = TemplateParamCategoryEnum.MAIN
			.getKey();// 参数分类
	
	@Column(name = "background_image")
	private String backgroundImage;// 背景图片

	@Column(name = "site_code")
	private String siteCode;// 渠道代码

	@Column(name = "group_codes")
	private String groupCodes;// 用户分组列表，使用','分割

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "valid_time")
	private Date validTime = DateUtils.parseDate("2017-01-01 00:00:00");// 生效时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expired_time")
	private Date expiredTime = DateUtils.parseDate("2099-12-30 23:59:59");// 失效时间

	@Column(name = "position_id")
	private String positionId;// 位置Id

	@Column(name = "position")
	private String position;// 位置信息
	
	public Long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Long albumId) {
		this.albumId = albumId;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(Integer itemStatus) {
		this.itemStatus = itemStatus;
	}

	public Integer getJumpItemType() {
		return jumpItemType;
	}

	public void setJumpItemType(Integer jumpItemType) {
		this.jumpItemType = jumpItemType;
	}

	public Long getJumpItemId() {
		return jumpItemId;
	}

	public void setJumpItemId(Long jumpItemId) {
		this.jumpItemId = jumpItemId;
	}

	public Integer getJumpType() {
		return jumpType;
	}

	public void setJumpType(Integer jumpType) {
		this.jumpType = jumpType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getJumpParam1() {
		return jumpParam1;
	}

	public void setJumpParam1(String jumpParam1) {
		this.jumpParam1 = jumpParam1;
	}

	public String getJumpParam2() {
		return jumpParam2;
	}

	public void setJumpParam2(String jumpParam2) {
		this.jumpParam2 = jumpParam2;
	}
	
	public String getInternalParam() {
		return internalParam;
	}

	public void setInternalParam(String internalParam) {
		this.internalParam = internalParam;
	}
	
	public Integer getInternalParamCategory() {
		return internalParamCategory;
	}

	public void setInternalParamCategory(Integer internalParamCategory) {
		this.internalParamCategory = internalParamCategory;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getGroupCodes() {
		return groupCodes;
	}

	public void setGroupCodes(String groupCodes) {
		this.groupCodes = groupCodes;
	}

	public Date getValidTime() {
		return validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}

	public Date getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
