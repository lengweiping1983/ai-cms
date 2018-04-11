<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<script src="${ctx}/static/scripts/system/role-controller.js"
	type="text/javascript"></script>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<!-- BEGIN EXAMPLE TABLE PORTLET-->
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>角色管理
					</div>
					<div class="actions">
						<shiro:hasPermission name="system:role:add">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.RoleController.toEdit('${ctx}/system/role/add');">
								<i class="fa fa-plus"></i>增加角色
							</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/system/role/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label"> 角色名称: <input
												type="text" placeholder="角色名称" name="search_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_name__LIKE_S}">
											</label>
										</div>
										<div style="float: right">
											<button class="btn btn-default btn-sm btn-outline green"
												type="button" onclick="$.Page.queryByForm();">
												<i class="fa fa-search"></i> 查询
											</button>
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
									<th class="sorting" abbr="name">角色名称</th>
									<th width="60%">菜单</th>
									<shiro:hasAnyPermissions
										name="system:role:edit,system:role:delete">
										<th>操作</th>
									</shiro:hasAnyPermissions>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<th class="sorting" abbr="name">角色名称</th>
									<th width="65%">菜单</th>
									<shiro:hasAnyPermissions
										name="system:role:edit,system:role:delete">
										<th>操作</th>
									</shiro:hasAnyPermissions>
								</tr>
							</tfoot>
							<tbody>
								<c:forEach items="${page.content}" var="role">
									<tr>
										<td>${role.name}</td>
										<td><c:set var="menuArray" value=","></c:set> <c:if
												test="${!empty role.menuList}">
												<c:forEach var="menu" items="${role.menuList}">
													<c:if test="${menu.type == 1}">
														<c:set var="tempMenuId" value=",${menu.id}," />
														<c:if
															test="${empty menuArray || menuArray.indexOf(tempMenuId) < 0}">
															<c:set var="menuArray" value="${menuArray}${menu.id}," />
															<div style='float: left; height: 25px'>
																<a href="javascript:;" data-menuId='${menu.id}'
																	data-roleId='${role.id}' class='atip'
																	data-toggle='tooltip' data-placement='bottom'> <span
																	class='tag label label-success' style='margin: 2px'>${menu.name}</span></a>
															</div>
														</c:if>
													</c:if>
													<c:if test="${menu.type == 2}">
														<c:set var="tempMenuId" value=",${menu.parent.id}," />
														<c:if
															test="${empty menuArray || menuArray.indexOf(tempMenuId) < 0}">
															<c:set var="menuArray"
																value="${menuArray}${menu.parent.id}," />
															<div style='float: left; height: 25px'>
																<a href="javascript:;" data-menuId='${menu.parent.id}'
																	data-roleId='${role.id}' class='atip'
																	data-toggle='tooltip' data-placement='bottom'> <span
																	class='tag label label-success' style='margin: 2px'>${menu.parent.name}</span></a>
															</div>
														</c:if>
													</c:if>
												</c:forEach>
											</c:if></td>
										<shiro:hasAnyPermissions
											name="system:role:edit,system:role:delete">
											<td><shiro:hasPermission name="system:role:edit">
													<button class="btn btn-default btn-sm btn-outline green"
														onclick="$.RoleController.toEdit('${ctx}/system/role/${role.id}/edit','${role.id}');">
														<i class="fa fa-edit"></i>修改
													</button>
												</shiro:hasPermission> <shiro:hasPermission name="system:role:delete">
													<button class="btn btn-default btn-sm btn-outline green"
														onclick="$.RoleController.toDelete('${ctx}/system/role/${role.id}/delete','${role.name}');">
														<i class="fa fa-remove"></i>删除
													</button>
												</shiro:hasPermission></td>
										</shiro:hasAnyPermissions>
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