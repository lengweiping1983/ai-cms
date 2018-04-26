<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">设置提供商[${cp.name}]FTP地址</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="id" value="${cpFtp.id}" /> <input
						type="hidden" name="cpCode" value="${cp.code}" />
					<div class="content form-horizontal">
						<div class="form-group">
							<label class="control-label col-md-4">FTP地址(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<input type="text" name="ip" value="${cpFtp.ip}"
									class="form-control validate[required]" placeholder="请输入FTP地址">
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-4">FTP端号(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<input type="text" name="port" value="${cpFtp.port}"
									class="form-control validate[required,custom[integer]]"
									placeholder="请输入FTP端号">
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-4">用户名(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<input type="text" name="username" value="${cpFtp.username}"
									class="form-control validate[required]" placeholder="请输入用户名">
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-4">密码(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<input type="text" name="password" value="${cpFtp.password}"
									class="form-control validate[required]" placeholder="请输入密码">
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-4">FTP目录(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<input type="text" name="dirPath" value="${cpFtp.dirPath}"
									class="form-control validate[required]" placeholder="请输入FTP目录">
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button class="btn btn-outline green"
					onclick="$.CpController.edit('${ctx}/config/cp/${cp.id}/editCpFtp');">
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