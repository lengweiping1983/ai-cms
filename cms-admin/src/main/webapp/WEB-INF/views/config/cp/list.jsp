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
						<i class="fa fa-globe"></i>提供商管理
					</div>
					<div class="actions">
						<shiro:hasPermission name="config:cp:add">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.CpController.toEdit('${ctx}/config/cp/add')"> <i
								class="fa fa-plus"></i>增加提供商
							</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/config/cp/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">提供商代码: <input
												type="text" class="form-control input-small input-inline"
												name="search_code__LIKE_S"
												value="${param.search_code__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">提供商名称: <input
												type="text" name="search_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_name__LIKE_S}">
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
								<th>序号</th>
								<th class="sorting" abbr="id">提供商ID</th>
								<th class="sorting" abbr="code">提供商代码</th>
								<th class="sorting" abbr="name">提供商名称</th>
								<th class="sorting" abbr="shortName">提供商简称</th>
								<th class="sorting" abbr="type">类型</th>
								<th class="sorting" abbr="status">状态</th>
								<shiro:hasAnyPermissions name="config:cp:edit,config:cp:delete">
									<th>操作</th>
								</shiro:hasAnyPermissions>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th>序号</th>
								<th class="sorting" abbr="id">提供商ID</th>
								<th class="sorting" abbr="code">提供商代码</th>
								<th class="sorting" abbr="name">提供商名称</th>
								<th class="sorting" abbr="shortName">提供商简称</th>
								<th class="sorting" abbr="type">类型</th>
								<th class="sorting" abbr="status">状态</th>
								<shiro:hasAnyPermissions name="config:cp:edit,config:cp:delete">
									<th>操作</th>
								</shiro:hasAnyPermissions>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="t" varStatus="status">
								<tr>
									<td>${status.index+1}</td>
									<td>${t.id}</td>
									<td>${t.code}</td>
									<td>${t.name}</td>
									<td>${t.shortName}</td>
									<td><tags:enum enumList="${typeEnum}" value="${t.type}" /></td>
									<td><c:forEach var="item" items="${statusEnum}">
											<c:if test="${item.key eq t.status && item.key eq 0}">
												<span class="badge badge-danger">${item.value}</span>
											</c:if>
											<c:if test="${item.key eq t.status && item.key eq 1}">
												<span class="badge badge-success">${item.value}</span>
											</c:if>
										</c:forEach></td>
									<shiro:hasAnyPermissions name="config:cp:edit,config:cp:delete">
										<td><shiro:hasPermission name="config:cp:edit">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.CpController.toEdit('${ctx}/config/cp/${t.id}/edit',${t.id});">
													<i class="fa fa-edit"></i>修改
												</button>
											</shiro:hasPermission> <c:forEach var="item" items="${statusEnum}">
												<c:if test="${item.key eq t.status && item.key eq 0}">
													<shiro:hasPermission name="config:cp:delete">
														<button class="btn btn-default btn-sm btn-outline green"
															onclick="$.CpController.toDelete('${ctx}/config/cp/${t.id}/delete','${t.name}');">
															<i class="fa fa-remove"></i>删除
														</button>
													</shiro:hasPermission>
												</c:if>
												<c:if test="${item.key eq t.status && item.key eq 1}">
												</c:if>
											</c:forEach> <shiro:hasPermission name="config:cp:editCpFtp">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.CpController.toEdit('${ctx}/config/cp/${t.code}/editCpFtp','${t.id}');">
													<i class="fa fa-gear"></i>设置FTP地址
												</button>
											</shiro:hasPermission> <shiro:hasPermission name="config:cp:user">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.CpController.userList('${ctx}/system/user/','${t.code}');">
													<i class="fa fa-gear"></i>设置登录帐号
												</button>
											</shiro:hasPermission></td>
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
	$.CpController.keypress({containerId: '${containerId}', formId: '${formId}'});
</script>
