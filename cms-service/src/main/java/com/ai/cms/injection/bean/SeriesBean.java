package com.ai.cms.injection.bean;

import com.ai.cms.injection.enums.InjectionItemTypeEnum;

public class SeriesBean extends ObjectBean {
	private String Name;
	private String OrderNumber;
	private String OriginalName;
	private String SortName;
	private String SearchName;
	private String Genre;
	private String ActorDisplay;
	private String WriterDisplay;
	private String OriginalCountry;
	private String Language;
	private String ReleaseYear;
	private String OrgAirDate;
	private String LicensingWindowStart;
	private String LicensingWindowEnd;
	private String DisplayAsNew;
	private String DisplayAsLastChance;
	private String Macrovision;
	private String Description;
	private String Status;
	private String Price;
	private String VolumnCount;
	private String SeriesType;

	private String Type;
	private String Keywords;
	private String Tags;
	private String Reserve1;
	private String Reserve2;
	private String Reserve3;
	private String Reserve4;
	private String Reserve5;
	private String RMediaCode;
	

	private String Kpeople;
	private String Director;
	private String ScriptWriter;
	private String Compere;
	private String Guest;
	private String Reporter;
	private String OPIncharge;
	private String VSPCode;
	private String CopyRight;
	private String ContentProvider;
	private String SPCode;
	
	private String Result;
	private String ErrorDescription;

	private String Duration;
	private String viewpoint;// 看点
	private String rating;// 评分，10分制，有一个小数
	private String image1;// 横海报
	private String image2;// 竖海报
	private String DefinitionFlag;
	private String OnlineTime;

	public SeriesBean() {
		ElementType = InjectionItemTypeEnum.SERIES.getValue();
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getOrderNumber() {
		return OrderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		OrderNumber = orderNumber;
	}

	public String getOriginalName() {
		return OriginalName;
	}

	public void setOriginalName(String originalName) {
		OriginalName = originalName;
	}

	public String getSortName() {
		return SortName;
	}

	public void setSortName(String sortName) {
		SortName = sortName;
	}

	public String getSearchName() {
		return SearchName;
	}

	public void setSearchName(String searchName) {
		SearchName = searchName;
	}

	public String getGenre() {
		return Genre;
	}

	public void setGenre(String genre) {
		Genre = genre;
	}

	public String getActorDisplay() {
		return ActorDisplay;
	}

	public void setActorDisplay(String actorDisplay) {
		ActorDisplay = actorDisplay;
	}

	public String getWriterDisplay() {
		return WriterDisplay;
	}

	public void setWriterDisplay(String writerDisplay) {
		WriterDisplay = writerDisplay;
	}

	public String getOriginalCountry() {
		return OriginalCountry;
	}

	public void setOriginalCountry(String originalCountry) {
		OriginalCountry = originalCountry;
	}

	public String getLanguage() {
		return Language;
	}

	public void setLanguage(String language) {
		Language = language;
	}

	public String getReleaseYear() {
		return ReleaseYear;
	}

	public void setReleaseYear(String releaseYear) {
		ReleaseYear = releaseYear;
	}

	public String getOrgAirDate() {
		return OrgAirDate;
	}

	public void setOrgAirDate(String orgAirDate) {
		OrgAirDate = orgAirDate;
	}

	public String getLicensingWindowStart() {
		return LicensingWindowStart;
	}

	public void setLicensingWindowStart(String licensingWindowStart) {
		LicensingWindowStart = licensingWindowStart;
	}

	public String getLicensingWindowEnd() {
		return LicensingWindowEnd;
	}

	public void setLicensingWindowEnd(String licensingWindowEnd) {
		LicensingWindowEnd = licensingWindowEnd;
	}

	public String getDisplayAsNew() {
		return DisplayAsNew;
	}

	public void setDisplayAsNew(String displayAsNew) {
		DisplayAsNew = displayAsNew;
	}

	public String getDisplayAsLastChance() {
		return DisplayAsLastChance;
	}

	public void setDisplayAsLastChance(String displayAsLastChance) {
		DisplayAsLastChance = displayAsLastChance;
	}

	public String getMacrovision() {
		return Macrovision;
	}

	public void setMacrovision(String macrovision) {
		Macrovision = macrovision;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public String getVolumnCount() {
		return VolumnCount;
	}

	public void setVolumnCount(String volumnCount) {
		VolumnCount = volumnCount;
	}

	public String getSeriesType() {
		return SeriesType;
	}

	public void setSeriesType(String seriesType) {
		SeriesType = seriesType;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getKeywords() {
		return Keywords;
	}

	public void setKeywords(String keywords) {
		Keywords = keywords;
	}

	public String getTags() {
		return Tags;
	}

	public void setTags(String tags) {
		Tags = tags;
	}

	public String getReserve1() {
		return Reserve1;
	}

	public void setReserve1(String reserve1) {
		Reserve1 = reserve1;
	}

	public String getReserve2() {
		return Reserve2;
	}

	public void setReserve2(String reserve2) {
		Reserve2 = reserve2;
	}

	public String getReserve3() {
		return Reserve3;
	}

	public void setReserve3(String reserve3) {
		Reserve3 = reserve3;
	}

	public String getReserve4() {
		return Reserve4;
	}

	public void setReserve4(String reserve4) {
		Reserve4 = reserve4;
	}

	public String getReserve5() {
		return Reserve5;
	}

	public void setReserve5(String reserve5) {
		Reserve5 = reserve5;
	}

	public String getRMediaCode() {
		return RMediaCode;
	}

	public void setRMediaCode(String rMediaCode) {
		RMediaCode = rMediaCode;
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

	public String getKpeople() {
		return Kpeople;
	}

	public void setKpeople(String kpeople) {
		Kpeople = kpeople;
	}

	public String getDirector() {
		return Director;
	}

	public void setDirector(String director) {
		Director = director;
	}

	public String getScriptWriter() {
		return ScriptWriter;
	}

	public void setScriptWriter(String scriptWriter) {
		ScriptWriter = scriptWriter;
	}

	public String getCompere() {
		return Compere;
	}

	public void setCompere(String compere) {
		Compere = compere;
	}

	public String getGuest() {
		return Guest;
	}

	public void setGuest(String guest) {
		Guest = guest;
	}

	public String getReporter() {
		return Reporter;
	}

	public void setReporter(String reporter) {
		Reporter = reporter;
	}

	public String getOPIncharge() {
		return OPIncharge;
	}

	public void setOPIncharge(String oPIncharge) {
		OPIncharge = oPIncharge;
	}

	public String getVSPCode() {
		return VSPCode;
	}

	public void setVSPCode(String vSPCode) {
		VSPCode = vSPCode;
	}

	public String getSPCode() {
		return SPCode;
	}

	public void setSPCode(String sPCode) {
		SPCode = sPCode;
	}

	public String getContentProvider() {
		return ContentProvider;
	}

	public void setContentProvider(String contentProvider) {
		ContentProvider = contentProvider;
	}

	public String getCopyRight() {
		return CopyRight;
	}

	public void setCopyRight(String copyRight) {
		CopyRight = copyRight;
	}

	public String getDuration() {
		return Duration;
	}

	public void setDuration(String duration) {
		Duration = duration;
	}

	public String getViewpoint() {
		return viewpoint;
	}

	public void setViewpoint(String viewpoint) {
		this.viewpoint = viewpoint;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
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

	public String getDefinitionFlag() {
		return DefinitionFlag;
	}

	public void setDefinitionFlag(String definitionFlag) {
		DefinitionFlag = definitionFlag;
	}

	public String getOnlineTime() {
		return OnlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		OnlineTime = onlineTime;
	}
}
