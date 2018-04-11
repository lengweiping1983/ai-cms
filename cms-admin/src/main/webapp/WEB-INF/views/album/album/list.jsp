<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>专题管理
					</div>
					<div class="actions">
						<shiro:hasPermission name="album:album:add">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.AlbumController.toEdit('${ctx}/album/album/add')">
								<i class="fa fa-plus"></i>增加专题
							</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/album/album/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">专题类型: <select
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
											<label class="control-label">专题代码: <input type="text"
												class="form-control input-small input-inline"
												name="search_code__LIKE_S"
												value="${param.search_code__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">专题名称: <input type="text"
												name="search_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_name__LIKE_S}">
											</label>
										</div>
									</div>
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">显示名称: <input type="text"
												name="search_title__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_title__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">使用模板: <select
												name="search_templateCode__EQ_S"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="template" items="${templateList}">
														<option value="${template.code}"
															<c:if test="${! empty param.search_templateCode__EQ_S && template.code eq param.search_templateCode__EQ_S}">selected="selected"</c:if>>${template.name}</option>
													</c:forEach>
											</select>
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">上线状态: <select
												name="search_status__EQ_I"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${statusEnum}">
														<option value="${item.key}"
															<c:if test="${! empty param.search_status__EQ_I && item.key eq param.search_status__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
											</select>
											</label>
										</div>
									</div>
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">所属APP: <select
												class="form-control input-small input-inline"
												name="search_appCode__EQ_S">
													<option value="">全部</option>
													<c:forEach var="app" items="${appList}">
														<option value="${app.code}"
															<c:if test="${(! empty param.search_appCode__EQ_S && app.code eq param.search_appCode__EQ_S) || app.code eq search_appCode__EQ_S}">selected="selected"</c:if>>${app.name}</option>
													</c:forEach>
											</select>
											</label>
										</div>
										<div class="col-md-8">
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
									<th class="sorting" abbr="type">专题类型</th>
									<th class="sorting" abbr="code">专题代码</th>
									<th class="sorting" abbr="name">专题名称</th>
									<th class="sorting" abbr="title">显示名称</th>
									<th class="sorting" abbr="templateCode">使用模板</th>
									<th class="sorting" abbr="status">上线状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<th>序号</th>
									<th class="sorting" abbr="id">ID</th>
									<th class="sorting" abbr="type">专题类型</th>
									<th class="sorting" abbr="code">专题代码</th>
									<th class="sorting" abbr="name">专题名称</th>
									<th class="sorting" abbr="title">显示名称</th>
									<th class="sorting" abbr="templateCode">使用模板</th>
									<th class="sorting" abbr="status">上线状态</th>
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
											onclick="
												<shiro:hasPermission name="album:album:view">
													$.AlbumController.detail('${t.id}');
												</shiro:hasPermission>
													">${t.code}</a></td>
										<td><a href="javascript:;"
											onclick="
												<shiro:hasPermission name="album:album:view">
													$.AlbumController.detail('${t.id}');
												</shiro:hasPermission>
													">${t.name}</a></td>
										<td><a href="javascript:;"
											onclick="
												<shiro:hasPermission name="album:album:view">
													$.AlbumController.detail('${t.id}');
												</shiro:hasPermission>
													">${t.title}</a></td>
										<td><c:forEach var="template" items="${templateList}">
												<c:if
													test="${! empty t.templateCode && template.code eq t.templateCode}"> ${template.name}</c:if>
											</c:forEach></td>
										<td><tags:status value='${t.status}' /></td>
										<td><c:set var="statusMethodName"
												value="${fns:getStatusMothodName(t.status)}" />
											<shiro:hasPermission name="album:album:status">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.AlbumController.toChangeStatus('${ctx}/album/album/${t.id}/status','${t.name}','${statusMethodName}');">
													<i class="fa fa-pencil"></i>${statusMethodName}
												</button>
											</shiro:hasPermission>
											<shiro:hasPermission name="album:album:edit">
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.AlbumController.toEdit('${ctx}/album/album/${t.id}/edit',${t.id});">
												<i class="fa fa-edit"></i>修改
											</button>
											</shiro:hasPermission>
											<shiro:hasPermission name="album:album:delete">
											<tags:deleteByStatus value='${t.status}'
												onclick="$.AlbumController.toDelete('${ctx}/album/album/${t.id}/delete','${t.name}');" />
											</shiro:hasPermission>
											<shiro:hasPermission name="album:album:showDetail">
											<c:if test="${t.type eq 1}">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.AlbumController.showDetail(${t.id});">
													<i class="fa fa-gear"></i>管理专题项
												</button>
											</c:if> <c:if test="${t.type ne 1}">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.AlbumController.listByPosition('${ctx}/album/${t.id}/albumItem/listByPosition');">
													<i class="fa fa-gear"></i>管理专题项
												</button>
											</c:if></td>
										</shiro:hasPermission>
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