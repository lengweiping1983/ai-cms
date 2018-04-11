<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>中央频道管理
					</div>
					<div class="actions">
						<shiro:hasPermission name="live:channel:add">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.ChannelController.toEdit('${ctx}/live/channel/add')">
								<i class="fa fa-plus"></i>增加中央频道
							</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/live/channel/">
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
											<label class="control-label">中央频道代码: <input type="text"
												class="form-control input-small input-inline"
												name="search_code__LIKE_S"
												value="${param.search_code__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">中央频道名称: <input type="text"
												name="search_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_name__LIKE_S}">
											</label>
										</div>
									</div>
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">&#12288;&#12288;状态: <select
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
									<th class="sorting" abbr="type">类型</th>
									<th class="sorting" abbr="code">中央频道代码</th>
									<th class="sorting" abbr="name">中央频道名称</th>
									<th class="sorting" abbr="status">状态</th>
									<th class="sorting" abbr="sortIndex">排序值</th>
									<th>操作</th>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<th>序号</th>
									<th class="sorting" abbr="id">ID</th>
									<th class="sorting" abbr="type">类型</th>
									<th class="sorting" abbr="code">中央频道代码</th>
									<th class="sorting" abbr="name">中央频道名称</th>
									<th class="sorting" abbr="status">状态</th>
									<th class="sorting" abbr="sortIndex">排序值</th>
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
												<shiro:hasPermission name="live:channel:detail">
													$.ChannelController.detail('${t.id}');
												</shiro:hasPermission>
													">${t.code}</a></td>
										<td><a href="javascript:;"
											onclick="
												<shiro:hasPermission name="live:channel:detail">
													$.ChannelController.detail('${t.id}');
												</shiro:hasPermission>
													">${t.name}</a></td>
										<td><tags:status value='${t.status}' /></td>
										<td>${t.sortIndex}</td>
										<td><c:set var="statusMethodName"
												value="${fns:getStatusMothodName(t.status)}" />
											<shiro:hasPermission name="live:channel:status">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.ChannelController.toChangeStatus('${ctx}/live/channel/${t.id}/status','${t.name}','${statusMethodName}');">
													<i class="fa fa-pencil"></i>${statusMethodName}
												</button>
											</shiro:hasPermission>
											<shiro:hasPermission name="live:channel:edit">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.ChannelController.toEdit('${ctx}/live/channel/${t.id}/edit',${t.id});">
													<i class="fa fa-edit"></i>修改
												</button>
											</shiro:hasPermission>
											<shiro:hasPermission name="live:channel:delete">
											<tags:deleteByStatus value='${t.status}'
												onclick="$.ChannelController.toDelete('${ctx}/live/channel/${t.id}/delete','${t.name}');" />
											</shiro:hasPermission>
											<shiro:hasPermission name="live:channel:showDetail">
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.ChannelController.showDetail(${t.id});">
												<i class="fa fa-gear"></i>管理节目
											</button>
											</shiro:hasPermission>
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


<script>
    $(".table-actions-wrapper-condition").keypress(function (e) {
        var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
        if (keyCode == 13) {
        	$.Page.queryByForm();
        	return false;
        }
    });
</script>