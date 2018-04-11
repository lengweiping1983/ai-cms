<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">批量修改元数据</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="itemType" value="${itemType}" /> <input
						type="hidden" name="itemIds" value="${itemIds}" />
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<div class="col-md-2">
										<label class="control-label">内容类型: </label>
									</div>
									<div class="col-md-2">
										<input type="checkbox" class="make-switch"
											name="contentTypeSwitch" data-on="success"
											data-on-color="success" data-off-color="warning"
											data-size="small">
									</div>
									<div class="col-md-8">
										<select id="contentType" name="contentType" disabled
											class="form-control">
											<c:forEach var="item" items="${contentTypeEnum}">
												<option value="${item.key}">${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<div class="col-md-2">
										<label class="control-label">TAG: </label>
									</div>
									<div class="col-md-2">
										<input type="checkbox" class="make-switch" name="tagSwitch"
											data-on="success" data-on-color="success"
											data-off-color="warning" data-size="small">
									</div>
									<div class="col-md-8">
										<input type="text" id="tag" name="tag" class="form-control"
											disabled>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<div class="col-md-2">
										<label class="control-label">关键字: </label>
									</div>
									<div class="col-md-2">
										<input type="checkbox" class="make-switch"
											name="keywordSwitch" data-on="success"
											data-on-color="success" data-off-color="warning"
											data-size="small">
									</div>
									<div class="col-md-8">
										<input type="text" id="keyword" name="keyword"
											class="form-control" disabled>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<div class="col-md-2">
										<label class="control-label">导演: </label>
									</div>
									<div class="col-md-2">
										<input type="checkbox" class="make-switch"
											name="directorSwitch" data-on="success"
											data-on-color="success" data-off-color="warning"
											data-size="small">
									</div>
									<div class="col-md-8">
										<input type="text" id="director" name="director"
											class="form-control" disabled>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<div class="col-md-2">
										<label class="control-label">演员: </label>
									</div>
									<div class="col-md-2">
										<input type="checkbox" class="make-switch" name="actorSwitch"
											data-on="success" data-on-color="success"
											data-off-color="warning" data-size="small">
									</div>
									<div class="col-md-8">
										<input type="text" id="actor" name="actor"
											class="form-control" disabled>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<div class="col-md-2">
										<label class="control-label">主持人 </label>
									</div>
									<div class="col-md-2">
										<input type="checkbox" class="make-switch"
											name="compereSwitch" data-on="success"
											data-on-color="success" data-off-color="warning"
											data-size="small">
									</div>
									<div class="col-md-8">
										<input type="text" id="compere" name="compere"
											class="form-control" disabled>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<div class="col-md-2">
										<label class="control-label">嘉宾: </label>
									</div>
									<div class="col-md-2">
										<input type="checkbox" class="make-switch" name="guestSwitch"
											data-on="success" data-on-color="success"
											data-off-color="warning" data-size="small">
									</div>
									<div class="col-md-8">
										<input type="text" id="guest" name="guest"
											class="form-control" disabled>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<div class="col-md-2">
										<label class="control-label">年份: </label>
									</div>
									<div class="col-md-2">
										<input type="checkbox" class="make-switch" name="yearSwitch"
											data-on="success" data-on-color="success"
											data-off-color="warning" data-size="small">
									</div>
									<div class="col-md-8">
										<input type="text" id="year" name="year" class="form-control"
											disabled>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<div class="col-md-2">
										<label class="control-label">地区: </label>
									</div>
									<div class="col-md-2">
										<input type="checkbox" class="make-switch" name="areaSwitch"
											data-on="success" data-on-color="success"
											data-off-color="warning" data-size="small">
									</div>
									<div class="col-md-8">
										<input type="text" id="area" name="area" class="form-control"
											disabled>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<div class="col-md-2">
										<label class="control-label">语言: </label>
									</div>
									<div class="col-md-2">
										<input type="checkbox" class="make-switch"
											name="languageSwitch" data-on="success"
											data-on-color="success" data-off-color="warning"
											data-size="small">
									</div>
									<div class="col-md-8">
										<input type="text" id="language" name="language"
											class="form-control" disabled>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<div class="col-md-2">
										<label class="control-label">提供商: </label>
									</div>
									<div class="col-md-2">
										<input type="checkbox" class="make-switch" name="cpIdSwitch"
											data-on="success" data-on-color="success"
											data-off-color="warning" data-size="small">
									</div>
									<div class="col-md-8">
										<input type="hidden" id="cpId" name="cpId" value="" /> <select
											class="form-control select2" id="select_cpId"
											name="select_cpId" multiple="multiple">
											<c:forEach var="item" items="${cpList}">
												<c:set var="cpIdSelected" value="" />
												<c:forEach var="cpId" items="">
													<c:if test="${item.id eq cpId}">
														<c:set var="cpIdSelected" value="1" />
													</c:if>
												</c:forEach>
												<option value="${item.id}"
													<c:if test="${cpIdSelected eq 1}">selected="selected"</c:if>>${item.name}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<div class="col-md-2">
										<label class="control-label">扩展字段1: </label>
									</div>
									<div class="col-md-2">
										<input type="checkbox" class="make-switch"
											name="kuoZhanOneSwitch" data-on="success"
											data-on-color="success" data-off-color="warning"
											data-size="small">
									</div>
									<div class="col-md-8">
										<input type="text" id="kuoZhanOne" name="kuoZhanOne"
											class="form-control" disabled>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<div class="col-md-2">
										<label class="control-label">扩展字段2: </label>
									</div>
									<div class="col-md-2">
										<input type="checkbox" class="make-switch"
											name="kuoZhanTwoSwitch" data-on="success"
											data-on-color="success" data-off-color="warning"
											data-size="small">
									</div>
									<div class="col-md-8">
										<input type="text" id="kuoZhanTwo" name="kuoZhanTwo"
											class="form-control" disabled>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button class="btn btn-outline green"
					onclick="$.SeriesController.batchChangeMetadata('${ctx}/media/series/batchChangeMetadata');">
					<i class="fa fa-save"></i>确定修改
				</button>
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>