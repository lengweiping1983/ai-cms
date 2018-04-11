<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">文件重命名</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<div class="content form-horizontal">
						<div class="form-group">
							<label class="control-label col-md-2">原路径: </label>

							<div class="col-md-9">
								<input type="text" id="from" name="from" value="${from}"
									class="form-control" readonly>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-2">目标路径(<span
								class="required">*</span>):
							</label>

							<div class="col-md-9">
								<input type="text" id="to" name="to" value="${from}"
									onclick="$.FileManageController.toPathChange();"
									class="form-control validate[required]" placeholder="请输入目标路径">
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button class="btn btn-outline green" disabled id="renameButton"
					onclick="$.FileManageController.rename('${ctx}/media/file/rename');">
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