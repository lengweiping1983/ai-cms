package com.ai.epg.uri.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.YesNoEnum;

/**
 * 页面实体
 *
 */
@Entity
@Table(name = "epg_uri")
public class Uri extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    @NotNull
    private Integer type;

	@NotNull
	private Integer internal = YesNoEnum.YES.getKey();

    private String code;// 页面代码

    private String name;// 管理名称

    private String title;// 显示名称
    
	@Column(name = "template_id")
	private Long templateId;// 模板Id

	@Column(name = "template_code")
	private String templateCode;// 模板代码

	@Column(name = "background_image")
	private String backgroundImage;// 背景图片
	
	@Column(name = "logo_image")
	private String logoImage;// LOGO图片
    
    private String url;// 链接地址

    @Column(name = "image1")
    private String image1;// 默认图标

    @Column(name = "image2")
    private String image2;// 图标激活

    @Column(name = "app_code")
    private String appCode;// APP代码

    @Column(name = "sort_index")
    private Integer sortIndex = 999; // 排序值

    @Column(name = "status")
    private Integer status = OnlineStatusEnum.DEFAULT.getKey(); // 状态:0=未上线、1=已上线、2=已下线
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "online_time")
    private Date onlineTime;// 上线时间
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "offline_time")
    private Date offlineTime;// 下线时间

    private String description;// 描述

    @Column(name = "site_code")
    private String siteCode;// 渠道代码
    
	private String tag;// tag，之间使用','分割
	private String keyword;// 关键字，内部使用
		
    public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getInternal() {
        return internal;
    }

    public void setInternal(Integer internal) {
        this.internal = internal;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
	
	public String getLogoImage() {
		return logoImage;
	}

	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}

	public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

	public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
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
    
    public Date getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(Date onlineTime) {
		this.onlineTime = onlineTime;
	}

	public Date getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(Date offlineTime) {
		this.offlineTime = offlineTime;
	}

	public String getDescription() {
		return description;
	}
	 
	public void setDescription(String description) {
        this.description = description;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }
}
