<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>推荐位管理
					</div>
					<div class="actions">
						<shiro:hasPermission name="widget:widget:add">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.WidgetController.toEdit('${ctx}/widget/widget/add')">
								<i class="fa fa-plus"></i>增加推荐位
							</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/widget/widget/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">推荐位代码: <input
												type="text" class="form-control input-small input-inline"
												name="search_code__LIKE_S"
												value="${param.search_code__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">推荐位名称: <input
												type="text" name="search_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_name__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">显示名称: <input type="text"
												name="search_title__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_title__LIKE_S}">
											</label>
										</div>
									</div>
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">推荐位类型:
												<select name="search_type__EQ_I"
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
											<label class="control-label">&#12288;上线状态:
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
									<th class="sorting" abbr="code">推荐位代码</th>
									<th class="sorting" abbr="name">推荐位名称</th>
									<th class="sorting" abbr="title">显示名称</th>
									<th class="sorting" abbr="type">推荐位类型</th>
									<th class="sorting" abbr="itemNum">显示元素个数</th>
									<th class="sorting" abbr="status">上线状态</th>
									<th class="sorting" abbr="sortIndex">排序值</th>
									<th>操作</th>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<th>序号</th>
									<th class="sorting" abbr="id">ID</th>
									<th class="sorting" abbr="code">推荐位代码</th>
									<th class="sorting" abbr="name">推荐位名称</th>
									<th class="sorting" abbr="title">显示名称</th>
									<th class="sorting" abbr="type">推荐位类型</th>
									<th class="sorting" abbr="itemNum">显示元素个数</th>
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
										<td>${t.code}</td>
										<td>${t.name}</td>
										<td>${t.title}</td>
										<td><tags:type value='${t.type}' /></td>
										<td>${t.itemNum}</td>
										<td><tags:status value='${t.status}' /></td>
										<td>${t.sortIndex}</td>
										<td><c:set var="statusMethodName"
												value="${fns:getStatusMothodName(t.status)}" />
											<shiro:hasPermission name="widget:widget:status">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.WidgetController.toChangeStatus('${ctx}/widget/widget/${t.id}/status','${t.name}','${statusMethodName}');">
													<i class="fa fa-pencil"></i>${statusMethodName}
												</button>
											</shiro:hasPermission>
											<shiro:hasPermission name="widget:widget:edit">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.WidgetController.toEdit('${ctx}/widget/widget/${t.id}/edit',${t.id});">
													<i class="fa fa-edit"></i>修改
												</button>
											</shiro:hasPermission>
											<shiro:hasPermission name="widget:widget:delete">
												<tags:deleteByStatus value='${t.status}'
													onclick="$.WidgetController.toDelete('${ctx}/widget/widget/${t.id}/delete','${t.name}');" />
											</shiro:hasPermission>
											<c:if
												test='${!empty currentSiteCode or currentAppCode eq "BALL" or currentAppCode eq "UFC"}'>
												<shiro:hasPermission name="widget:widget:showDetail">
													<button class="btn btn-default btn-sm btn-outline green"
														onclick="$.WidgetController.showDetail(${t.id});">
														<i class="fa fa-gear"></i>管理推荐位项
													</button>
												</shiro:hasPermission>
											</c:if></td>
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
    $(".table-actions-wrapper-condition").keypress(function (e) {
        var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
        if (keyCode == 13) {
        	$.Page.queryByForm();
        	return false;
        }
    });
</script>

