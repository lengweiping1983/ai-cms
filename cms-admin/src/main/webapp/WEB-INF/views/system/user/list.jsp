<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<script src="${ctx}/static/scripts/system/user-controller.js"
	type="text/javascript"></script>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<!-- BEGIN EXAMPLE TABLE PORTLET-->
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>用户管理
					</div>
					<div class="actions">
						<shiro:hasPermission name="system:user:add">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.UserController.toEdit('${ctx}/system/user/add');">
								<i class="fa fa-plus"></i>增加用户
							</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/system/user/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label"> &#12288;&#12288;帐号: <input
												type="text" placeholder="帐号" name="search_loginName__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_loginName__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label"> &#12288;&#12288;姓名: <input
												type="text" placeholder="姓名" name="search_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_name__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label"> &#12288;&#12288;工号: <input
												type="text" placeholder="工号" name="search_no__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_no__LIKE_S}">
											</label>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label"> 固定电话: <input
												type="text" placeholder="固定电话" name="search_phone__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_phone__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label"> 移动电话: <input
												type="text" placeholder="移动电话" name="search_mobile__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_mobile__LIKE_S}">
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
									<th class="sorting" abbr="loginName" width="10%">帐号</th>
									<th class="sorting" abbr="name" width="10%">姓名</th>
									<th class="sorting" abbr="no" width="8%">工号</th>
									<th class="sorting" abbr="email" width="15%">邮箱</th>
									<th class="sorting" abbr="phone" width="12%">固定电话</th>
									<th class="sorting" abbr="mobile" width="12%">移动电话</th>
									<th class="sorting" abbr="status" width="8%">状态</th>
									<shiro:hasAnyPermissions
										name="system:user:edit,system:user:editPassword,system:user:delete">
										<th width="25%">操作</th>
									</shiro:hasAnyPermissions>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<th class="sorting" abbr="loginName">帐号</th>
									<th class="sorting" abbr="name">姓名</th>
									<th class="sorting" abbr="no">工号</th>
									<th class="sorting" abbr="email">邮箱</th>
									<th class="sorting" abbr="phone">固定电话</th>
									<th class="sorting" abbr="mobile">移动电话</th>
									<th class="sorting" abbr="status">状态</th>
									<shiro:hasAnyPermissions
										name="system:user:edit,system:user:editPassword,system:user:delete">
										<th>操作</th>
									</shiro:hasAnyPermissions>
								</tr>
							</tfoot>
							<tbody>
								<c:forEach items="${page.content}" var="user">
									<tr>
										<td>${user.loginName}</td>
										<td>${user.name}</td>
										<td>${user.no}</td>
										<td>${user.email}</td>
										<td>${user.phone}</td>
										<td>${user.mobile}</td>
										<td><c:choose>
												<c:when test="${user.status == 0}">
													<span class="badge badge-danger">停用</span>
												</c:when>
												<c:otherwise>
													<span class="badge badge-success">正常</span>
												</c:otherwise>
											</c:choose></td>
										<shiro:hasAnyPermissions
											name="system:user:edit,system:user:editPassword,system:user:delete">
											<td><shiro:hasPermission name="system:user:edit">
													<button class="btn btn-default btn-sm btn-outline green"
														onclick="$.UserController.toEdit('${ctx}/system/user/${user.id}/edit','${user.id}');">
														<i class="fa fa-edit"></i>修改
													</button>
												</shiro:hasPermission> <shiro:hasPermission name="system:user:editPassword">
													<button class="btn btn-default btn-sm btn-outline green"
														onclick="$.UserController.toEditPassword('${ctx}/system/user/${user.id}/editPassword');">
														<i class="fa fa-key"></i>修改密码
													</button>
												</shiro:hasPermission> <c:if test="${user.canBeDelete}">
													<shiro:hasPermission name="system:user:delete">
														<button class="btn btn-default btn-sm btn-outline green"
															onclick="$.UserController.toDelete('${ctx}/system/user/${user.id}/delete','${user.loginName}');">
															<i class="fa fa-remove"></i>删除
														</button>
													</shiro:hasPermission>
												</c:if></td>
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