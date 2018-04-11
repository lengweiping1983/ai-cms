package com.ai.epg.widget.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.YesNoEnum;

/**
 * 推荐位实体
 */
@Entity
@Table(name = "epg_widget")
public class Widget extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    private String code;// 推荐位代码

    private String name;// 推荐位名称

    private String title;// 显示名称

    private String description;// 描述

    @Column(name = "type")
    private Integer type; // 类型:1=导航条、2=图片推荐位、3=视频推荐位trailer

    @Column(name = "status")
    private Integer status = OnlineStatusEnum.DEFAULT.getKey(); // 状态:0=未上线、1=已上线、2=已下线
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "online_time")
    private Date onlineTime;// 上线时间
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "offline_time")
    private Date offlineTime;// 下线时间

    @Column(name = "background_image")
    private String backgroundImage;// 背景图片

    @Column(name = "app_code")
    private String appCode;// APP代码

    @Column(name = "sort_index")
    private Integer sortIndex = 999; // 排序值

    @Column(name = "item_num")
    private Integer itemNum = 1; // 显示元素个数
    
    @Column(name = "config_item_types")
	private String configItemTypes;// 可配置的元素类型

	@Column(name = "config_image1")
	private Integer configImage1 = YesNoEnum.YES.getKey(); // 横海报可配置

	@Column(name = "config_image1_width")
	private Integer configImage1Width;

	@Column(name = "config_image1_height")
	private Integer configImage1Height;

	@Column(name = "config_image2")
	private Integer configImage2 = YesNoEnum.NO.getKey(); // 竖海报可配置

	@Column(name = "config_image2_width")
	private Integer configImage2Width;

	@Column(name = "config_image2_height")
	private Integer configImage2Height;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

	public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
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

    public Integer getItemNum() {
        return itemNum;
    }

    public void setItemNum(Integer itemNum) {
        this.itemNum = itemNum;
    }

	public String getConfigItemTypes() {
		return configItemTypes;
	}

	public void setConfigItemTypes(String configItemTypes) {
		this.configItemTypes = configItemTypes;
	}

	public Integer getConfigImage1() {
		return configImage1;
	}

	public void setConfigImage1(Integer configImage1) {
		this.configImage1 = configImage1;
	}

	public Integer getConfigImage1Width() {
		return configImage1Width;
	}

	public void setConfigImage1Width(Integer configImage1Width) {
		this.configImage1Width = configImage1Width;
	}

	public Integer getConfigImage1Height() {
		return configImage1Height;
	}

	public void setConfigImage1Height(Integer configImage1Height) {
		this.configImage1Height = configImage1Height;
	}

	public Integer getConfigImage2() {
		return configImage2;
	}

	public void setConfigImage2(Integer configImage2) {
		this.configImage2 = configImage2;
	}

	public Integer getConfigImage2Width() {
		return configImage2Width;
	}

	public void setConfigImage2Width(Integer configImage2Width) {
		this.configImage2Width = configImage2Width;
	}

	public Integer getConfigImage2Height() {
		return configImage2Height;
	}

	public void setConfigImage2Height(Integer configImage2Height) {
		this.configImage2Height = configImage2Height;
	}

}
