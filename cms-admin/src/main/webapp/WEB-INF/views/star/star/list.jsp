<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>明星管理
					</div>
					<div class="actions">
						<a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.StarController.toEdit('${ctx}/star/star/add')"> <i
							class="fa fa-plus"></i>增加明星
						</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/star/star/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">&#12288;&#12288;类型: <select
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
											<label class="control-label">所属俱乐部: <select
												name="search_clubId__EQ_L"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${clubList}">
														<option value="${item.id}"
															<c:if test="${! empty param.search_clubId__EQ_L && app.code eq param.search_clubId__EQ_L}">selected="selected"</c:if>>${item.name}</option>
													</c:forEach>
											</select>
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">TAG: <input type="text"
												name="search_tag__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_tag__LIKE_S}">
											</label>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">明星姓名: <input type="text"
												name="search_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_name__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">&#12288;&#12288;&#12288;状态:
												<select name="search_status__EQ_I"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${statusEnum}">
														<option value="${item.key}"
															<c:if test="${! empty param.search_status__EQ_I && item.key eq param.search_status__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
											</select>
											</label>
										</div>
										<div class="col-md-4">
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
									<th>所属俱乐部</th>
									<th class="sorting" abbr="name">明星姓名</th>
									<th class="sorting" abbr="tag">TAG</th>
									<th class="sorting" abbr="status">状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<th>序号</th>
									<th class="sorting" abbr="id">ID</th>
									<th class="sorting" abbr="type">类型</th>
									<th>所属俱乐部</th>
									<th class="sorting" abbr="name">明星姓名</th>
									<th class="sorting" abbr="tag">TAG</th>
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
										<td><c:forEach var="item" items="${clubList}">
												<c:if test="${! empty t.clubId && item.id eq t.clubId}"> ${item.name}</c:if>
											</c:forEach></td>
										<td><a href="javascript:;"
											onclick="$.StarController.detail('${t.id}');">${t.name}</a></td>
										<td>${t.tag}</td>
										<td><tags:status value='${t.status}' /></td>
										<td><c:set var="statusMethodName"
												value="${fns:getStatusMothodName(t.status)}" />
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.StarController.toChangeStatus('${ctx}/star/star/${t.id}/status','${t.name}','${statusMethodName}');">
												<i class="fa fa-pencil"></i>${statusMethodName}
											</button>
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.StarController.toEdit('${ctx}/star/star/${t.id}/edit',${t.id});">
												<i class="fa fa-edit"></i>修改
											</button> <tags:deleteByStatus value='${t.status}'
												onclick="$.StarController.toDelete('${ctx}/star/star/${t.id}/delete','${t.name}');" />
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.TrailerController.showProgramList(${t.id}, 11, '${t.name}', '明星管理');">
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
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>

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