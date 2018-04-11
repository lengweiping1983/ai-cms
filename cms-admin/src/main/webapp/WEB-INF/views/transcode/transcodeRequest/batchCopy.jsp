<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">复制转码工单</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="itemType" value="${itemType}" /> <input
						type="hidden" name="itemIds" value="${itemIds}" />
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="control-label col-md-3">码率(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<div class="checkbox-list">
											<c:forEach var="item" items="${mediaTemplateList}">
												<c:set var="templateSelected" value="" />
												<c:forEach var="templateId"
													items="${transcodeRequest.templateId}">
													<c:if test="${item.id == templateId}">
														<c:set var="templateSelected" value="1" />
													</c:if>
												</c:forEach>
												<label><input name="templateId"
													<c:if test="${templateSelected eq 1}"> checked </c:if>
													class="validate[required]" type="checkbox"
													value="${item.id}">${item.title} <c:if
														test="${item.transcodeMode eq 0}">
														<span class="badge badge-danger">扫描目录转码</span>
													</c:if> <c:if test="${item.transcodeMode eq 2}">
													</c:if></label>
											</c:forEach>
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
					onclick="$.TranscodeRequestController.batchCopy('${ctx}/transcode/transcodeRequest/${t.id}/batchCopy');">
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