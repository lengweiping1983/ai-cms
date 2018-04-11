<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>[${itemName}]${managementName}
					</div>
					<div class="actions">
						<a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.TrailerController.toEdit('${ctx}/media/trailer/add?itemType=${param.search_itemType__EQ_I}&itemId=${param.search_itemId__EQ_L}')">
							<i class="fa fa-plus"></i>增加关联节目
						</a> <a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.common.ajaxLoadLastContent()"> <i
							class="fa fa-reply"></i> 返回${managementName}
						</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/media/trailer/">
								<input type="hidden" name="itemName" value="${itemName}" /> <input
									type="hidden" name="managementName" value="${managementName}" />
								<input type="hidden" name="search_itemType__EQ_I"
									value="${param.search_itemType__EQ_I}" /> <input type="hidden"
									name="search_itemId__EQ_L" value="${param.search_itemId__EQ_L}" />
							</form>
						</div>
					</div>
					<div class="dataTables_wrapper no-footer">
						<table
							class="table dataTable table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th>序号</th>
									<th>类型</th>
									<th>节目id</th>
									<th>节目名称</th>
									<th>节目状态</th>
									<th class="sorting" abbr="status">状态</th>
									<th class="sorting" abbr="sortIndex">排序值</th>
									<th>操作</th>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<th>序号</th>
									<th>类型</th>
									<th>节目id</th>
									<th>节目名称</th>
									<th>节目状态</th>
									<th class="sorting" abbr="status">状态</th>
									<th class="sorting" abbr="sortIndex">排序值</th>
									<th>操作</th>
								</tr>
							</tfoot>
							<tbody>
								<c:forEach items="${page.content}" var="t" varStatus="status">
									<c:forEach items="${programList}" var="programTemp">
										<c:if test="${programTemp.id == t.programId}">
											<c:set var="program" value="${programTemp}" />
										</c:if>
									</c:forEach>
									<tr>
										<td>${status.index+1}</td>
										<td><tags:type value='${t.type}' /></td>
										<td>${program.id}</td>
										<td><a href="javascript:;"
											onclick="$.ProgramController.detail('${program.id}');">${program.title}</a></td>
										<td><tags:status value='${program.status}' /></td>
										<td><tags:status value='${t.status}' /></td>
										<td>${t.sortIndex}</td>
										<td><c:set var="statusMethodName"
												value="${fns:getStatusMothodName(t.status)}" />
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.TrailerController.toChangeStatus('${ctx}/media/trailer/${t.id}/status','${program.title}','${statusMethodName}');">
												<i class="fa fa-pencil"></i>${statusMethodName}
											</button>
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.TrailerController.toEdit('${ctx}/media/trailer/${t.id}/edit',${t.id});">
												<i class="fa fa-edit"></i>修改
											</button> <tags:deleteByStatus value='${t.status}'
												onclick="$.TrailerController.toDelete('${ctx}/media/trailer/${t.id}/delete','${program.title}');" />
										</td>
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
</div>
<!-- END CONTENT BODY -->

<tags:contentModal />

<div class="modal fade" id="content_list_modal_container" tabindex="-1"
	role="basic" aria-hidden="true">
	<div class="modal-dialog modal-full" id="content_list_dialog_container">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title" id="content_list_modal_container_title">选择</h4>
			</div>
			<div class="modal-body" id="content_list_container"></div>
			<div class="modal-footer">
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>

<script>
    $(".table-actions-wrapper-condition").keypress(function (e) {
        var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
        if (keyCode == 13) {
        	$.Page.queryByForm();
        	return false;
        }
    });
</script>

