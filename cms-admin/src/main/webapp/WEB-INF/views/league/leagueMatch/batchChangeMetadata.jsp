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
										<label class="control-label">TAG: </label>
									</div>
									<div class="col-md-2">
										<input type="hidden" value="0" id="tagsFlag" name="tagsFlag">
										<input type="checkbox" class="make-switch" name="tagsSwitch"
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
										<input type="hidden" value="0" id="keywordFlag" name="keywordFlag">
										<input type="checkbox" class="make-switch" name="keywordSwitch"
											data-on="success" data-on-color="success"
											data-off-color="warning" data-size="small">
									</div>
									<div class="col-md-8">
										<input type="text" id="keyword" name="keyword" class="form-control"
											disabled>
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
										<input type="hidden" value="0" id="cpIdFlag" name="cpIdFlag">
										<input type="checkbox" class="make-switch" name="cpIdSwitch"
											data-on="success" data-on-color="success"
											data-off-color="warning" data-size="small">
									</div>
									<div class="col-md-8">
										<select class="form-control select2" id="cpId" disabled
											name="cpId">
											<option value="">请选择</option>
											<c:forEach var="item" items="${cpList}">
												<option value="${item.id}">${item.name}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>

					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button class="btn btn-outline green"
					onclick="$.LeagueMatchController.batchChangeMetadata('${ctx}/league/leagueMatch/batchChangeMetadata');">
					<i class="fa fa-save"></i>保存
				</button>
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>