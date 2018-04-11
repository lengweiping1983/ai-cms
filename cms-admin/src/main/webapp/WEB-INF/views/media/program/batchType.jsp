<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">批量修改节目类型</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="itemType" value="${itemType}" /> <input
						type="hidden" name="itemIds" value="${itemIds}" />
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="control-label col-md-3">节目类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-7">
										<select id="type" name="type" class="form-control"
											onchange="$.ProgramController.changeSelectType(this.value);">
											<c:forEach var="item" items="${typeEnum}">
												<option value="${item.key}">${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div id="program_div_1" style="display: none">
							<div class="row">
								<div class="col-md-12">
									<div class="form-group">
										<label class="control-label col-md-3">剧头名称(<span
											class="required">*</span>):
										</label>

										<div class="col-md-7">
											<div class="input-group">
												<input type="hidden" name="seriesId" id="seriesId" value="" />
												<input type="text" class="form-control validate[required]"
													name="seriesName" id="seriesName" placeholder="点击我选择剧头"
													readonly="readonly"
													onclick="$.SeriesController.toSelectSeries();" /> <span
													class="input-group-btn">
													<button onclick="$.SeriesController.toSelectSeries();"
														class="btn btn-success" type="button">
														<i class="fa fa-arrow-left fa-fw" /></i> 选择剧头
													</button>
												</span>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button class="btn btn-outline green"
					onclick="$.ProgramController.batchType('${ctx}/media/program/batchType');">
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