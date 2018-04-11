<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade bs-modal-lg" tabindex="-1" role="basic"
	aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">预览</h4>
			</div>
			<div class="modal-body" id="modal-body">
				<c:if test="${!empty status}">
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label col-md-2">状态:</label>

								<div class="col-md-10">
									<p class="form-control-static">${status}
								</div>
							</div>
						</div>
					</div>
				</c:if>
				<div class="row">
					<div class="col-md-12">
						<div class="form-group">
							<label class="control-label col-md-2">播放地址:</label>

							<div class="col-md-10">
								<p style="word-break: break-all;" class="form-control-static">${playUrl}</p>
							</div>
						</div>
					</div>
				</div>

				<object classid="clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921"
					codebase="http://download.videolan.org/pub/videolan/vlc/last/win32/axvlc.cab"
					id="vlc" name="vlc" width="868" height="604" events="True">
					<param name="Src" value="${playUrl}" />
					<param name="ShowDisplay" value="True" />
					<param name="AutoLoop" value="False" />
					<param name="AutoPlay" value="True" />
					<embed id="vlcEmb" name="vlcEmb" type="application/x-vlc-plugin"
						version="VideoLAN.VLCPlugin.2" autoplay="yes" loop="no"
						width="868" height="604" target="${playUrl}">
					</embed>
				</object>
			</div>

			<div class="modal-footer">
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>