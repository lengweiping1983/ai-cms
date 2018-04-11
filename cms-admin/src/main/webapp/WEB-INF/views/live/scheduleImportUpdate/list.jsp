<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>节目单导入
					</div>
					<div class="actions">
						<a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.ScheduleImportUpdateController.toEdit('${ctx}/scheduleimport/scheduleImportUpdate/import/1')">
							<i class="fa fa-plus"></i>批量导入节目单
						</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/scheduleimport/scheduleImportUpdate"></form>
						</div>
					</div>
					<table
						class="table dataTable table-striped table-bordered table-hover table-checkable"
						id="content_list">
						<thead>
							<tr>
<!-- 								<th> -->
<!-- 									<div class="checker"> -->
<!-- 										<span class=""><input type="checkbox" -->
<!-- 											class="group-checkable" data-set="#content_list .checkboxes" /></span> -->
<!-- 									</div> -->
<!-- 								</th> -->
								<th>序号</th>
								<th class="sorting" abbr="fileName">文件名称</th>
								<th class="sorting" abbr="importTime">导入时间</th>
								<th class="sorting" abbr="success">成功数</th>
								<th class="sorting" abbr="failure">失败数</th>
								<th class="sorting" abbr="errorMessage">错误描述</th>
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
<!-- 								<th> -->
<!-- 									<div class="checker"> -->
<!-- 										<span class=""><input type="checkbox" -->
<!-- 											class="group-checkable" data-set="#content_list .checkboxes" /></span> -->
<!-- 									</div> -->
<!-- 								</th> -->
								<th>序号</th>
								<th class="sorting" abbr="fileName">文件名称</th>
								<th class="sorting" abbr="importTime">导入时间</th>
								<th class="sorting" abbr="success">成功数</th>
								<th class="sorting" abbr="failure">失败数</th>
								<th class="sorting" abbr="errorMessage">错误描述</th>
								<th>操作</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="t" varStatus="status">
								<tr>
<!-- 									<td> -->
<!-- 										<div class="checker"> -->
<!-- 											<span class=""><input type="checkbox" -->
<%-- 												class="checkboxes" value="${t.id}" data-name="${t.fileName}" /></span> --%>
<!-- 										</div> -->
<!-- 									</td> -->
									<td>${status.index+1}</td>
									<td>${t.fileName}</td>
									<td><fmt:formatDate value="${t.importTime}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td>${t.success}</td>
									<td>${t.failure}</td>
									<td>${t.errorMessage}</td>
									<td></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<tags:pageInfo />
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END CONTENT BODY -->

<tags:contentModal />

<div class="modal fade" id="content_list_modal_container" tabindex="-1"
	role="basic" aria-hidden="true">
	<div class="modal-dialog modal-lg" id="content_list_dialog_container">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title" id="content_list_modal_container_title">选择</h4>
			</div>
			<div class="modal-body" id="content_list_container"></div>
			<div class="modal-footer">
				<button class="btn btn-outline green"
					onclick="$.FileManageController.toBatch('selectFile');">
					<i class="fa fa-save"></i>确定
				</button>
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>

<script>
	$.ProgramImportUpdateController.init();
	$(".table-actions-wrapper-condition").keypress(function(e) {
		var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
		if (keyCode == 13) {
			$.Page.queryByForm();
			return false;
		}
	});
</script>
