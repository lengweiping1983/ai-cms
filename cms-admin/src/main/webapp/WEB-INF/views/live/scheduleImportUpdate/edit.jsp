<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">
					导入节目单文件
				</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="id" value="${scheduleImportUpdate.id}" />

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
													onchange="$.ScheduleImportUpdateController.setUploadFileName(this);"
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
									<label class="control-label col-md-3">描述:</label>
									<div class="col-md-9">
										<textarea name="description" class="form-control">${scheduleImportUpdate.description}</textarea>
									</div>
								</div>
							</div>
						</div>
						<!--  
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="control-label col-md-3">模板文件: </label>
									<div class="col-md-9">
										<p class="form-control-static">
											<a href="${ctx}/template"> 下载导入模板 </a>
										</p>
									</div>
								</div>
							</div>
						</div>
						-->
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button id="ProgramImportUpdateSave" class="btn btn-outline green"
					onclick="$.ScheduleImportUpdateController.edit('${ctx}/scheduleimport/scheduleImportUpdate/import');">
					<i class="fa fa-save"></i>执行
				</button>
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>