<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<script src="${ctx}/static/scripts/system/dic-controller.js"
	type="text/javascript"></script>

<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<!-- BEGIN EXAMPLE TABLE PORTLET-->
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>字典管理
					</div>
					<div class="actions">
						<shiro:hasPermission name="system:dic:add">
							<a href="javascript:;" class="btn btn-default"
								onclick="$.DicController.toAdd()"> <i class="fa fa-plus"></i>
								增加字典
							</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/system/dic/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label"> 字典名称: <input
												type="text" name="search_name__LIKE_S"
												value="${param.search_name__LIKE_S}" placeholder="字典名称"
												class="form-control input-small input-inline">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label"> 字典代码: <input
												type="text" name="search_code__LIKE_S"
												value="${param.search_code__LIKE_S}" placeholder="字典代码"
												class="form-control input-small input-inline">
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
					<table
						class="table dataTable table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th class="sorting" abbr="name">字典名称</th>
								<th class="sorting" abbr="code">字典代码</th>
								<th>描述</th>
								<shiro:hasAnyPermissions
									name="system:dic:edit,system:dic:delete">
									<th>操作</th>
								</shiro:hasAnyPermissions>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th class="sorting" abbr="name">字典名称</th>
								<th class="sorting" abbr="code">字典代码</th>
								<th>描述</th>
								<shiro:hasAnyPermissions
									name="system:dic:edit,system:dic:delete">
									<th>操作</th>
								</shiro:hasAnyPermissions>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="dic">
								<tr>
									<td>${dic.name}</td>
									<td>${dic.code}</td>
									<td>${dic.description}</td>
									<shiro:hasAnyPermissions
										name="system:dic:edit,system:dic:delete">
										<td><shiro:hasPermission name="system:dic:edit">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.DicController.toEdit('${dic.id}');">
													<i class="fa fa-edit"></i>修改
												</button>
											</shiro:hasPermission> <shiro:hasPermission name="system:dic:delete">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.DicController.toDelete(${dic.id},'${dic.name}');">
													<i class="fa fa-remove"></i>删除
												</button>
											</shiro:hasPermission> <shiro:hasPermission name="system:dic:edit">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.DicController.toAddDicItem(${dic.id});">
													<i class="fa fa-gear"></i>字典项管理
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