<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<c:set var="staticFileVersion" value="10" />
<c:choose>
	<c:when test="${fn:contains(profiles, 'dev') || fn:contains(profiles, 'demo')}">
		<c:set var="staticFileVersion" value="<%=System.currentTimeMillis()%>" />
	</c:when>
	<c:otherwise>
	</c:otherwise>
</c:choose>

<link
	href="${ctx}/assets/global/plugins/jstree/dist/themes/default/style.min.css"
	rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/global/css/plugins.css" rel="stylesheet"
	type="text/css" />
<script src="${ctx}/assets/global/plugins/jstree/dist/jstree.min.js"
	type="text/javascript"></script>

<link href="${ctx}/assets/global/plugins/bootstrap-daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/global/plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css" />

<script src="${ctx}/assets/global/plugins/bootstrap-daterangepicker/moment.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/bootstrap-daterangepicker/daterangepicker.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js" type="text/javascript"></script>

<link href="${ctx}/static//plugins/bootstrap-multiselect-0.9.13/dist/css/bootstrap-multiselect.css" rel="stylesheet" type="text/css" />                
<script src="${ctx}/static/plugins/bootstrap-multiselect-0.9.13/dist/js/bootstrap-multiselect.js" type="text/javascript"></script>

<!-- <link rel="stylesheet" type="text/css" -->
<%-- 	href="${ctx}/static/plugins/annotator/bootstrap-yeti.min.css"> --%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/plugins/annotator/annotator-pro-editor.css?time=${staticFileVersion}">
<!-- <link rel="stylesheet" type="text/css" -->
<%-- 	href="${ctx}/static/plugins/annotator/annotator-pro.min.css?time=${staticFileVersion}"> --%>

<script src="${ctx}/static/plugins/annotator/modernizr.js"></script>
<%-- <script src="${ctx}/static/plugins/annotator/jquery.min.js"></script> --%>
<%-- <script src="${ctx}/static/plugins/annotator/bootstrap.min.js"></script> --%>
<script src="${ctx}/static/plugins/annotator/annotator-pro-editor.js?time=${staticFileVersion}"></script>
<%-- <script src="${ctx}/static/plugins/annotator/annotator-pro.min.js?time=${staticFileVersion}"></script> --%>


<script src="${ctx}/static/plugins/dot/doT.js?time=${staticFileVersion}" type="text/javascript"></script>
<script src="${ctx}/static/scripts/common/common.js?time=${staticFileVersion}" type="text/javascript"></script>
<script src="${ctx}/static/scripts/common/page.js?time=${staticFileVersion}" type="text/javascript"></script>
<script src="${ctx}/static/scripts/common/base-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/common/pinyin.js?time=${staticFileVersion}"
	type="text/javascript"></script>
	
			

<script src="${ctx}/static/scripts/config/site-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/config/cp-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>	

<script src="${ctx}/static/scripts/config/app-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>	
<script src="${ctx}/static/scripts/config/entry-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>	
	
<script src="${ctx}/static/scripts/product/charge-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>	
<script src="${ctx}/static/scripts/product/service-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/product/serviceItem-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>


<script src="${ctx}/static/scripts/media/series-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/media/program-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>	
<script src="${ctx}/static/scripts/media/mediaFile-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/media/trailer-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/media/play-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/media/fileManage-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>	
<script src="${ctx}/static/scripts/media/mediaImport-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
	
<script src="${ctx}/static/scripts/transcode/transcodeRequest-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/transcode/transcodeTask-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>

<script src="${ctx}/static/scripts/live/channel-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/live/schedule-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/live/channelSchedule-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/live/channelConfig-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/live/scheduleImport-controller.js?time=${staticFileVersion}"
		type="text/javascript"></script>
	
<script src="${ctx}/static/scripts/star/club-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/star/star-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
		
<script src="${ctx}/static/scripts/injection/platform-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/injection/sendTask-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/injection/receiveTask-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/injection/downloadTask-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>	
	
		
<script src="${ctx}/static/scripts/league/league-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/league/leagueSeason-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/league/leagueMatch-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/league/leagueSeasonMatch-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>	
<script src="${ctx}/static/scripts/league/leagueSeasonClub-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/league/leagueSeasonStar-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>


	
<script src="${ctx}/static/scripts/album/album-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/album/albumItem-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>

<script src="${ctx}/static/scripts/widget/widget-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/widget/widgetItem-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>

<script src="${ctx}/static/scripts/category/category-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/category/categoryItem-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>

<script src="${ctx}/static/scripts/uri/uri-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
	
<script src="${ctx}/static/scripts/template/template-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/template/templateParam-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>

	
<script src="${ctx}/static/scripts/subscriber/subscriber-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/subscriber/subscriberGroup-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>	
	
<script src="${ctx}/static/scripts/subscriber/orderRecord-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
	
	
<script src="${ctx}/static/scripts/market/activity-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>
<script src="${ctx}/static/scripts/market/activityUser-controller.js?time=${staticFileVersion}"
	type="text/javascript"></script>

