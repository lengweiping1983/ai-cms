package com.ai.cms.config.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.ValidStatusEnum;

@Entity
@Table(name = "cms_media_template")
public class MediaTemplate extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	private Integer type; // 模板类型:0=视频转码、1=音频转码

	private String code; // 对外唯一的模板Code

	@Column(name = "external_code")
	private String externalCode; // 和外部系统交互时，定义的码率模板Code。和本系统中Code可以不同

	private String title;

	@Column(name = "title_tag")
	private String titleTag;

	private Integer status = ValidStatusEnum.VALID.getKey(); // 状态:0=失效,1=生效

	@Column(name = "v_codec")
	private String vCodec; // 编码格式:H264、H265

	@Column(name = "v_resolution")
	private String vResolution; // 分辨率

	@Column(name = "v_format")
	private String vFormat; // 格式

	@Column(name = "v_bitrate_mode")
	private String vBitrateMode; // 码率模式

	@Column(name = "v_bitrate")
	private Integer vBitrate; // 码率，单位为K

	@Column(name = "v_max_bitrate")
	private Integer vMaxBitrate; // 最高码率，单位为K

	@Column(name = "v_min_bitrate")
	private Integer vMinBitrate; // 最小码率，单位为K

	@Column(name = "v_framerate")
	private Integer vFramerate; // 帧率

	@Column(name = "v_2pass")
	private Integer v2pass; // 编码是否做2pass:0=否、1=是

	@Column(name = "v_gop")
	private Integer vGop; // GOP

	@Column(name = "v_profile")
	private String vProfile;

	@Column(name = "v_profile_level")
	private String vProfileLevel;

	@Column(name = "a_codec")
	private String aCodec; // 音频编码格式

	@Column(name = "a_bitrate")
	private Integer aBitrate; // 音频码率

	@Column(name = "transcode_mode")
	private Integer transcodeMode = 1; // 转码方式:0=扫描目录转码，1=接口转码

	@Column(name = "definition")
	private String definition;// 清晰度

	@Column(name = "prefix_desc")
	private String prefixDesc;

	@Column(name = "suffix_desc")
	private String suffixDesc;

	@Column(name = "category")
	private String category = "default";// 分组

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getExternalCode() {
		return externalCode;
	}

	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleTag() {
		return titleTag;
	}

	public void setTitleTag(String titleTag) {
		this.titleTag = titleTag;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getvCodec() {
		return vCodec;
	}

	public void setvCodec(String vCodec) {
		this.vCodec = vCodec;
	}

	public String getvResolution() {
		return vResolution;
	}

	public void setvResolution(String vResolution) {
		this.vResolution = vResolution;
	}

	public String getvFormat() {
		return vFormat;
	}

	public void setvFormat(String vFormat) {
		this.vFormat = vFormat;
	}

	public String getvBitrateMode() {
		return vBitrateMode;
	}

	public void setvBitrateMode(String vBitrateMode) {
		this.vBitrateMode = vBitrateMode;
	}

	public Integer getvBitrate() {
		return vBitrate;
	}

	public void setvBitrate(Integer vBitrate) {
		this.vBitrate = vBitrate;
	}

	public Integer getvMaxBitrate() {
		return vMaxBitrate;
	}

	public void setvMaxBitrate(Integer vMaxBitrate) {
		this.vMaxBitrate = vMaxBitrate;
	}

	public Integer getvMinBitrate() {
		return vMinBitrate;
	}

	public void setvMinBitrate(Integer vMinBitrate) {
		this.vMinBitrate = vMinBitrate;
	}

	public Integer getvFramerate() {
		return vFramerate;
	}

	public void setvFramerate(Integer vFramerate) {
		this.vFramerate = vFramerate;
	}

	public Integer getV2pass() {
		return v2pass;
	}

	public void setV2pass(Integer v2pass) {
		this.v2pass = v2pass;
	}

	public Integer getvGop() {
		return vGop;
	}

	public void setvGop(Integer vGop) {
		this.vGop = vGop;
	}

	public String getvProfile() {
		return vProfile;
	}

	public void setvProfile(String vProfile) {
		this.vProfile = vProfile;
	}

	public String getvProfileLevel() {
		return vProfileLevel;
	}

	public void setvProfileLevel(String vProfileLevel) {
		this.vProfileLevel = vProfileLevel;
	}

	public String getaCodec() {
		return aCodec;
	}

	public void setaCodec(String aCodec) {
		this.aCodec = aCodec;
	}

	public Integer getaBitrate() {
		return aBitrate;
	}

	public void setaBitrate(Integer aBitrate) {
		this.aBitrate = aBitrate;
	}

	public Integer getTranscodeMode() {
		return transcodeMode;
	}

	public void setTranscodeMode(Integer transcodeMode) {
		this.transcodeMode = transcodeMode;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getPrefixDesc() {
		return prefixDesc;
	}

	public void setPrefixDesc(String prefixDesc) {
		this.prefixDesc = prefixDesc;
	}

	public String getSuffixDesc() {
		return suffixDesc;
	}

	public void setSuffixDesc(String suffixDesc) {
		this.suffixDesc = suffixDesc;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
