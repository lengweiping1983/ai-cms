<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>俱乐部管理
					</div>
					<div class="actions">
						<a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.ClubController.toEdit('${ctx}/star/club/add')"> <i
							class="fa fa-plus"></i>增加俱乐部
						</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/star/club/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">类型: <select
												name="search_type__EQ_I"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${typeEnum}">
														<option value="${item.key}"
															<c:if test="${! empty param.search_type__EQ_I && item.key eq param.search_type__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
											</select>
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">名称: <input type="text"
												name="search_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_name__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">状态: <select
												name="search_status__EQ_I"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${statusEnum}">
														<option value="${item.key}"
															<c:if test="${! empty param.search_status__EQ_I && item.key eq param.search_status__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
											</select>
											</label>
											<div style="float: right">
												<button class="btn btn-default btn-sm btn-outline green"
													type="button" onclick="$.Page.queryByForm();">
													<i class="fa fa-search"></i> 查询
												</button>
											</div>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
					<div class="dataTables_wrapper no-footer">
						<table
							class="table dataTable table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th>序号</th>
									<th class="sorting" abbr="id">ID</th>
									<th class="sorting" abbr="type">类型</th>
									<th class="sorting" abbr="name">名称</th>
									<th class="sorting" abbr="area">地区</th>
									<th class="sorting" abbr="status">状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<th>序号</th>
									<th class="sorting" abbr="id">ID</th>
									<th class="sorting" abbr="type">类型</th>
									<th class="sorting" abbr="name">名称</th>
									<th class="sorting" abbr="area">地区</th>
									<th class="sorting" abbr="status">状态</th>
									<th>操作</th>
								</tr>
							</tfoot>
							<tbody>
								<c:forEach items="${page.content}" var="t" varStatus="status">
									<tr>
										<td>${status.index+1}</td>
										<td>${t.id}</td>
										<td><tags:type value='${t.type}' /></td>
										<td><a href="javascript:;"
											onclick="$.ClubController.detail('${t.id}');">${t.name}</a></td>
										<td>${t.area}</td>
										<td><tags:status value='${t.status}' /></td>
										<td><c:set var="statusMethodName"
												value="${fns:getStatusMothodName(t.status)}" />
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.ClubController.toChangeStatus('${ctx}/star/club/${t.id}/status','${t.name}','${statusMethodName}');">
												<i class="fa fa-pencil"></i>${statusMethodName}
											</button>
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.ClubController.toEdit('${ctx}/star/club/${t.id}/edit',${t.id});">
												<i class="fa fa-edit"></i>修改
											</button> <tags:deleteByStatus value='${t.status}'
												onclick="$.ClubController.toDelete('${ctx}/star/club/${t.id}/delete','${t.name}');" />
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.TrailerController.showProgramList(${t.id}, 10, '${t.name}', '俱乐部管理');">
												<i class="fa fa-gear"></i>相关节目
											</button></td>
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


<script>
if (jQuery().datepicker) {
    $('.date-picker').datepicker({
        rtl: App.isRTL(),
        orientation: "left",
        format: 'yyyy-mm-dd',
        language: 'zh-CN',
        autoclose: true
    });
}
    $(".table-actions-wrapper-condition").keypress(function (e) {
        var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
        if (keyCode == 13) {
        	$.Page.queryByForm();
        	return false;
        }
    });
</script>