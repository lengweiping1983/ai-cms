package com.ai.cms.injection.bean;

import com.ai.cms.injection.enums.InjectionItemTypeEnum;

public class MovieBean extends ObjectBean {

	private String Type;
	private String FileURL;
	private String PlayURL;
	private String SourceDRMType;
	private String DestDRMType;
	private String AudioType;
	private String ScreenFormat;
	private String ClosedCaptioning;
	private String MediaSpec;
	private String Result;
	private String ErrorDescription;

	private String OCSURL;
	private String Duration;
	private String FileSize;
	private String BitRateType;
	private String VideoType;
	private String AudioFormat;
	private String Resolution;
	private String VideoProfile;
	private String SystemLayer;
	private String ServiceType;

	private String Name;

	private String Definition;
	private String Bitrate;
	private String FileMd5;
	private String CdnPlatform;

	public MovieBean() {
		ElementType = InjectionItemTypeEnum.MOVIE.getValue();
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getFileURL() {
		return FileURL;
	}

	public void setFileURL(String fileURL) {
		FileURL = fileURL;
	}

	public String getPlayURL() {
		return PlayURL;
	}

	public void setPlayURL(String playURL) {
		PlayURL = playURL;
	}

	public String getSourceDRMType() {
		return SourceDRMType;
	}

	public void setSourceDRMType(String sourceDRMType) {
		SourceDRMType = sourceDRMType;
	}

	public String getDestDRMType() {
		return DestDRMType;
	}

	public void setDestDRMType(String destDRMType) {
		DestDRMType = destDRMType;
	}

	public String getAudioType() {
		return AudioType;
	}

	public void setAudioType(String audioType) {
		AudioType = audioType;
	}

	public String getScreenFormat() {
		return ScreenFormat;
	}

	public void setScreenFormat(String screenFormat) {
		ScreenFormat = screenFormat;
	}

	public String getClosedCaptioning() {
		return ClosedCaptioning;
	}

	public void setClosedCaptioning(String closedCaptioning) {
		ClosedCaptioning = closedCaptioning;
	}

	public String getOCSURL() {
		return OCSURL;
	}

	public void setOCSURL(String oCSURL) {
		OCSURL = oCSURL;
	}

	public String getDuration() {
		return Duration;
	}

	public void setDuration(String duration) {
		Duration = duration;
	}

	public String getFileSize() {
		return FileSize;
	}

	public void setFileSize(String fileSize) {
		FileSize = fileSize;
	}

	public String getBitRateType() {
		return BitRateType;
	}

	public void setBitRateType(String bitRateType) {
		BitRateType = bitRateType;
	}

	public String getVideoType() {
		return VideoType;
	}

	public void setVideoType(String videoType) {
		VideoType = videoType;
	}

	public String getAudioFormat() {
		return AudioFormat;
	}

	public void setAudioFormat(String audioFormat) {
		AudioFormat = audioFormat;
	}

	public String getResolution() {
		return Resolution;
	}

	public void setResolution(String resolution) {
		Resolution = resolution;
	}

	public String getVideoProfile() {
		return VideoProfile;
	}

	public void setVideoProfile(String videoProfile) {
		VideoProfile = videoProfile;
	}

	public String getSystemLayer() {
		return SystemLayer;
	}

	public void setSystemLayer(String systemLayer) {
		SystemLayer = systemLayer;
	}

	public String getServiceType() {
		return ServiceType;
	}

	public void setServiceType(String serviceType) {
		ServiceType = serviceType;
	}

	public String getResult() {
		return Result;
	}

	public void setResult(String result) {
		Result = result;
	}

	public String getErrorDescription() {
		return ErrorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		ErrorDescription = errorDescription;
	}

	public String getMediaSpec() {
		return MediaSpec;
	}

	public void setMediaSpec(String mediaSpec) {
		MediaSpec = mediaSpec;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDefinition() {
		return Definition;
	}

	public void setDefinition(String definitionFlag) {
		Definition = definitionFlag;
	}

	public String getBitrate() {
		return Bitrate;
	}

	public void setBitrate(String bitrate) {
		Bitrate = bitrate;
	}

	public String getFileMd5() {
		return FileMd5;
	}

	public void setFileMd5(String fileMd5) {
		FileMd5 = fileMd5;
	}

	public String getCdnPlatform() {
		return CdnPlatform;
	}

	public void setCdnPlatform(String cdnPlatform) {
		CdnPlatform = cdnPlatform;
	}

}
