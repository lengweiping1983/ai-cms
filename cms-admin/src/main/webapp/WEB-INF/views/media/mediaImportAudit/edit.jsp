<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">
					批量导入
					<c:forEach var="item" items="${typeEnum}">
						<c:if
							test="${! empty mediaImport.type && item.key eq mediaImport.type}">[${item.value}]</c:if>
					</c:forEach>
				</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="id" value="${mediaImport.id}" /> <input
						type="hidden" id="type" name="type" value="${mediaImport.type}">
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="control-label col-md-3">文件(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<div class="fileinput fileinput-new" data-provides="fileinput">
											<div class="input-group input-large">
												<div class="form-control uneditable-input span3"
													data-trigger="fileinput">
													<i class="fa fa-file fileinput-exists"></i>&nbsp; <span
														class="fileinput-filename"> </span>
												</div>
												<span class="input-group-addon btn default btn-file">
													<span class="fileinput-new"> 选择文件</span> <span
													class="fileinput-exists"> 更换文件</span> <input type="file"
													name="file" class="validate[required]"
													onchange="$.MediaImportController.setUploadFileName(this);"
													accept=".xls,.xlsx">
												</span> <a href="#"
													class="input-group-addon btn red fileinput-exists"
													data-dismiss="fileinput"> 移除文件</a>
											</div>
											<span class="help-block">只支持“xls”或“xlsx”格式文件.</span>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="control-label col-md-3">是否创建元数据(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="createMetadata" class="form-control">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty mediaImport.createMetadata && item.key eq mediaImport.createMetadata}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select> <span class="help-block">不存在时，是否创建.</span>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="control-label col-md-3">描述:</label>
									<div class="col-md-9">
										<textarea name="description" class="form-control">${mediaImport.description}</textarea>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="control-label col-md-3">模板:</label>
									<div class="col-md-9">
										<c:forEach var="item" items="${typeEnum}">
											<c:if
												test="${mediaImport.type eq 1 && item.key eq mediaImport.type}">
												<a target="_blank"
													href="${ctx}/download/media/series_import_template.xlsx">下载[${item.value}]模板</a>
											</c:if>
											<c:if
												test="${mediaImport.type eq 2 && item.key eq mediaImport.type}">
												<a target="_blank"
													href="${ctx}/download/media/program_import_template.xlsx">下载[${item.value}]模板</a>
											</c:if>
											<c:if
												test="${mediaImport.type eq 3 && item.key eq mediaImport.type}">
												<a target="_blank"
													href="${ctx}/download/media/media_file_import_template.xlsx">下载[${item.value}]模板</a>
											</c:if>
										</c:forEach>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button id="MediaImportSave" class="btn btn-outline green"
					onclick="$.MediaImportController.edit('${ctx}/media/mediaImportAudit/import');">
					<i class="fa fa-save"></i>确定导入
				</button>
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>