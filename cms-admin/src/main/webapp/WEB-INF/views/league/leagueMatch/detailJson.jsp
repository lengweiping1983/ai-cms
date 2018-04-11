<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">查看赛事Json字符串</h4>
			</div>
			<div class="modal-body">
				<input type="hidden" id="image1Data" name="image1Data"> <input
					type="hidden" id="image2Data" name="image2Data">
				<form id="editForm">
					<input type="hidden" name="id" value="${leagueMatch.id}" />
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="control-label col-md-1">Json:</label>

									<div class="col-md-10">
										<textarea name="info" rows="8" class="form-control">${jsonString}</textarea>
										<p class="help-block">第三方编排系统使用该Json字符串来扩展元数据.</p>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
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