<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<c:set var="staticFileVersion" value="4" />
<c:choose>
	<c:when
		test="${fn:contains(profiles, 'dev') || fn:contains(profiles, 'demo')}">
		<c:set var="staticFileVersion" value="<%=System.currentTimeMillis()%>" />
	</c:when>
	<c:otherwise>
	</c:otherwise>
</c:choose>

<link
	href="${ctx}/static/plugins/video-js-6.8.0/video-js.css?time=${staticFileVersion}"
	rel="stylesheet">
<link
	href="${ctx}/static/plugins/videojs-playlist-ui-3.4.0/videojs-playlist-ui.css?time=${staticFileVersion}"
	rel="stylesheet">

<script
	src="${ctx}/static/plugins/video-js-6.8.0/ie8/videojs-ie8.min.js?time=${staticFileVersion}"></script>
<script
	src="${ctx}/static/plugins/video-js-6.8.0/video.min.js?time=${staticFileVersion}"></script>
<script
	src="${ctx}/static/plugins/video-js-6.8.0/videojs-contrib-hls.min.js?time=${staticFileVersion}"></script>
<script
	src="${ctx}/static/plugins/videojs-playlist-4.2.0/videojs-playlist.min.js?time=${staticFileVersion}"></script>
<script
	src="${ctx}/static/plugins/videojs-playlist-ui-3.4.0/videojs-playlist-ui.js?time=${staticFileVersion}"></script>
<style>
.player-container {
	background: #1a1a1a;
	overflow: auto;
	width: 868px;
	margin: 0 0 20px;
}

.video-js {
	float: left;
}

.vjs-playlist, .my-custom-class, #my-custom-element {
	float: left;
	width: 268px;
}

.vjs-playlist.vjs-playlist-vertical {
	float: none;
	height: 480px;
	width: 268px;
}

.vjs-playlist.vjs-playlist-horizontal {
	float: none;
	height: 120px;
	width: 600px;
}

.video-dimensions {
	height: 480px;
	width: 600px;
}
</style>

<div class="player-container">
	<video id="video" class="video-js vjs-big-play-centered" controls
		autoplay height="480" width="600">
	</video>
	<div class="vjs-playlist"></div>
</div>

<script>
	var playData = [];
</script>
<c:forEach items="${playBeanList}" var="t" varStatus="status">
	<script>
		playData
				.push({
					name : "${t.name}",
					description : "${t.description}",
					duration : "${t.duration}",
					bitrate : "${t.bitrate}",
					sources : [ {
						src : '${t.playUrl}',
						type : '${t.type}'
					} ],
					thumbnail : [
							{
								srcset : '${ctx}/static/plugins/videojs-playlist-ui-3.4.0/oceans.jpg',
								type : 'image/jpeg',
								media : '(min-width: 400px;)'
							},
							{
								src : '${ctx}/static/plugins/videojs-playlist-ui-3.4.0/oceans-low.jpg'
							} ]
				});
	</script>
</c:forEach>
<script>
	var player = videojs('video');
	player.playlist(playData);
	player.playlistUi();
	player.playlist.repeat(true);
	player.on('ended', function(e) {
		if (player.playlist.lastIndex() == 0) {
			player.currentTime(0);
			player.play();
		} else {
			player.playlist.next();
		}
	});
</script>