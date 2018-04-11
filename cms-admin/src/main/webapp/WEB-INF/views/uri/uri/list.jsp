<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>页面管理
					</div>
					<div class="actions">
						<shiro:hasPermission name="uri:uri:add">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.UriController.toEdit('${ctx}/uri/uri/add')"> <i
								class="fa fa-plus"></i>增加页面
							</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/uri/uri/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">&#12288;页面类型:<select
												name="search_type__EQ_I"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${uriTypeEnum}">
														<option value="${item.key}"
															<c:if test="${! empty param.search_type__EQ_I && item.key eq param.search_type__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
											</select>
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">页面代码:<input type="text"
												name="search_code__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_code__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">管理名称:<input type="text"
												name="search_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_name__LIKE_S}">
											</label>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">&#12288;显示名称:<input type="text"
												name="search_title__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_title__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">&#12288;&#12288;TAG:<input
												type="text" name="search_tags__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_tags__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">&#12288;关键字:<input
												type="text" name="search_keyword__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_keyword__LIKE_S}">
											</label>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">本系统页面:<select
												name="search_internal__EQ_I"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${yesNoEnum}">
														<option value="${item.key}"
															<c:if test="${! empty param.search_internal__EQ_I && item.key eq param.search_internal__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
											</select>
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">上线状态:<select
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
									<th class="sorting" abbr="type">页面类型</th>
									<th class="sorting" abbr="code">页面代码</th>
									<th class="sorting" abbr="name">管理名称</th>
									<th class="sorting" abbr="title">显示名称</th>
									<th class="sorting" abbr="tag">TAG</th>
									<th class="sorting" abbr="keyword">关键字</th>
									<th class="sorting" abbr="internal">本系统页面</th>
									<th class="sorting" abbr="status">上线状态</th>
									<th class="sorting" abbr="sortIndex">排序值</th>
									<th>操作</th>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<th>序号</th>
									<th class="sorting" abbr="id">ID</th>
									<th class="sorting" abbr="type">页面类型</th>
									<th class="sorting" abbr="code">页面代码</th>
									<th class="sorting" abbr="name">管理名称</th>
									<th class="sorting" abbr="title">显示名称</th>
									<th class="sorting" abbr="tag">TAG</th>
									<th class="sorting" abbr="keyword">关键字</th>
									<th class="sorting" abbr="internal">本系统页面</th>
									<th class="sorting" abbr="status">上线状态</th>
									<th class="sorting" abbr="sortIndex">排序值</th>
									<th>操作</th>
								</tr>
							</tfoot>
							<tbody>
								<c:forEach items="${page.content}" var="t" varStatus="status">
									<tr>
										<td>${status.index+1}</td>
										<td>${t.id}</td>
										<td><c:forEach var="item" items="${uriTypeEnum}">
												<c:if test="${item.key eq t.type}">
													${item.value}
													</c:if>
											</c:forEach></td>
										<td>${t.code}</td>
										<td><a href="javascript:;"
											onclick="
												<shiro:hasPermission name="uri:uri:view">
													$.UriController.detail('${t.id}');
												</shiro:hasPermission>
													">${t.name}</a></td>
										<td><a href="javascript:;"
											onclick="
												<shiro:hasPermission name="uri:uri:view">
													$.UriController.detail('${t.id}');
												</shiro:hasPermission>
													">${t.title}</a></td>
										<td>${t.tag}</td>
										<td>${t.keyword}</td>
										<td><c:forEach var="item" items="${yesNoEnum}">
												<c:if test="${item.key eq t.internal}">
													${item.value}
													</c:if>
											</c:forEach></td>
										<td><tags:status value='${t.status}' /></td>
										<td>${t.sortIndex}</td>
										<td><c:set var="statusMethodName"
												value="${fns:getStatusMothodName(t.status)}" />
											<shiro:hasPermission name="uri:uri:status">
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.UriController.toChangeStatus('${ctx}/uri/uri/${t.id}/status','${t.name}','${statusMethodName}');">
												<i class="fa fa-pencil"></i>${statusMethodName}
											</button>
											</shiro:hasPermission>
											<shiro:hasPermission name="uri:uri:edit">
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.UriController.toEdit('${ctx}/uri/uri/${t.id}/edit',${t.id});">
												<i class="fa fa-edit"></i>修改
											</button>
											</shiro:hasPermission>
											<shiro:hasPermission name="uri:uri:delete">
											<tags:deleteByStatus value='${t.status}'
												onclick="$.UriController.toDelete('${ctx}/uri/uri/${t.id}/delete','${t.name}');" />
											</shiro:hasPermission>
											<c:if test="${t.internal eq 1}">
												<shiro:hasPermission name="uri:uri:layout">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.UriController.listByPosition('${ctx}/uri/uri/${t.id}/listByPosition');">
													<i class="fa fa-gear"></i>页面数据编排
												</button>
												</shiro:hasPermission>
											</c:if>
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