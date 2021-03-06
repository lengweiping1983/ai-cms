<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<c:set var="containerId" value="" />
<c:set var="formId" value="" />
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>码率模板管理
					</div>
					<div class="actions">
						<shiro:hasPermission name="config:mediaTemplate:add">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.MediaTemplateController.toEdit('${ctx}/config/mediaTemplate/add')">
								<i class="fa fa-plus"></i>增加码率模板
							</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/config/mediaTemplate/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">码率模板代码: <input
												type="text" class="form-control input-small input-inline"
												name="search_code__LIKE_S"
												value="${param.search_code__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">码率模板名称: <input
												type="text" name="search_title__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_title__LIKE_S}">
											</label>
										</div>
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
											<tags:searchButton containerId="${containerId}" />
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
					<tags:pageInfo containerId="${containerId}" formId="${formId}"
						levelId="up" />
					<table
						class="table dataTable table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th class="sorting" abbr="id">码率模板ID</th>
								<th class="sorting" abbr="code">码率模板代码</th>
								<th class="sorting" abbr="title">码率模板名称</th>
								<th class="sorting" abbr="status">状态</th>
								<shiro:hasAnyPermissions
									name="config:mediaTemplate:edit,config:mediaTemplate:delete">
									<th>操作</th>
								</shiro:hasAnyPermissions>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th class="sorting" abbr="id">码率模板ID</th>
								<th class="sorting" abbr="code">码率模板代码</th>
								<th class="sorting" abbr="title">码率模板名称</th>
								<th class="sorting" abbr="status">状态</th>
								<shiro:hasAnyPermissions
									name="config:mediaTemplate:edit,config:mediaTemplate:delete">
									<th>操作</th>
								</shiro:hasAnyPermissions>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="t">
								<tr>
									<td>${t.id}</td>
									<td>${t.code}</td>
									<td>${t.title}</td>
									<td><c:forEach var="item" items="${statusEnum}">
											<c:if test="${item.key eq t.status && item.key eq 0}">
												<span class="badge badge-danger">${item.value}</span>
											</c:if>
											<c:if test="${item.key eq t.status && item.key eq 1}">
												<span class="badge badge-success">${item.value}</span>
											</c:if>
										</c:forEach></td>
									<shiro:hasAnyPermissions
										name="config:mediaTemplate:edit,config:mediaTemplate:delete">
										<td><shiro:hasPermission name="config:mediaTemplate:edit">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.MediaTemplateController.toEdit('${ctx}/config/mediaTemplate/${t.id}/edit',${t.id});">
													<i class="fa fa-edit"></i>修改
												</button>
											</shiro:hasPermission> <c:forEach var="item" items="${statusEnum}">
												<c:if test="${item.key eq t.status && item.key eq 0}">
													<shiro:hasPermission name="config:mediaTemplate:delete">
														<button class="btn btn-default btn-sm btn-outline green"
															onclick="$.MediaTemplateController.toDelete('${ctx}/config/mediaTemplate/${t.id}/delete','${t.title}');">
															<i class="fa fa-remove"></i>删除
														</button>
													</shiro:hasPermission>
												</c:if>
												<c:if test="${item.key eq t.status && item.key eq 1}">
												</c:if>
											</c:forEach></td>
									</shiro:hasAnyPermissions>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<tags:pageInfo containerId="${containerId}" formId="${formId}" />
				</div>
			</div>
		</div>
	</div>
</div>

<tags:contentModal />

<script>
	$.MediaTemplateController.keypress({containerId: '${containerId}', formId: '${formId}'});
</script>
