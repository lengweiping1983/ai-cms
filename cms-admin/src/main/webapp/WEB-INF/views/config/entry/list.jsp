<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<c:set var="containerId" value="" />
<c:set var="formId" value="" />
<c:set var="contextPathPrefix" value="${ctx}/config/entry/" />
<c:set var="permissionPrefix" value="config:entry:" />
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>外部入口管理
					</div>
					<div class="actions">
						<a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.EntryController.toEdit('${contextPathPrefix}add')">
							<i class="fa fa-plus"></i>增加外部入口
						</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${contextPathPrefix}">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">代码: <input type="text"
												class="form-control input-small input-inline"
												name="search_code__LIKE_S"
												value="${param.search_code__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">名称: <input type="text"
												name="search_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_name__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<tags:searchButton containerId="${containerId}"
												formId="${formId}" />
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
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="code">代码</th>
								<th class="sorting" abbr="name">名称</th>
								<th class="sorting" abbr="serviceUrl">服务URL</th>
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="code">代码</th>
								<th class="sorting" abbr="name">名称</th>
								<th class="sorting" abbr="serviceUrl">服务URL</th>
								<th>操作</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="t">
								<tr>
									<td>${t.id}</td>
									<td>${t.code}</td>
									<td>${t.name}</td>
									<td>${t.serviceUrl}</td>
									<td>
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.EntryController.toEdit('${contextPathPrefix}${t.id}/edit',${t.id});">
											<i class="fa fa-edit"></i>修改
										</button>
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.EntryController.toDelete('${contextPathPrefix}${t.id}/delete','${t.name}');">
											<i class="fa fa-remove"></i>删除
										</button>
									</td>
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
$.EntryController.keypress({containerId: '${containerId}', formId: '${formId}'});
</script>
