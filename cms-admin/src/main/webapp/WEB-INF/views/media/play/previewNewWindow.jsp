<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>
<!DOCTYPE html>
<html>
<body>
	<c:if test="${!empty status}">
	状态:${status}<br>
	</c:if>
	播放地址:${playUrl}
	<br>
	<object classid="clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921"
		codebase="http://download.videolan.org/pub/videolan/vlc/last/win32/axvlc.cab"
		id="vlc" name="vlc" width="868" height="604" events="True">
		<param name="Src" value="${playUrl}" />
		<param name="ShowDisplay" value="True" />
		<param name="AutoLoop" value="False" />
		<param name="AutoPlay" value="True" />
		<embed id="vlcEmb" name="vlcEmb" type="application/x-vlc-plugin"
			version="VideoLAN.VLCPlugin.2" autoplay="yes" loop="no" width="868"
			height="604" target="${playUrl}">
		</embed>
	</object>
</body>
</html>