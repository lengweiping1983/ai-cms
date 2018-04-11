<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<c:set var="containerId" value="" />
<c:set var="formId" value="" />
<c:set var="contextPathPrefix" value="${ctx}/media/mediaImport/" />
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>媒资批量导入
					</div>
					<div class="actions">
						<shiro:hasAnyPermissions name="media:media:batchImport">
							<c:forEach var="item" items="${typeEnum}">
								<a href="javascript:;" class="btn btn-default btn-sm"
									onclick="$.MediaImportController.toEdit('${contextPathPrefix}import/${item.key}')">
									<i class="fa fa-plus"></i>批量导入[${item.value}]
								</a>
							</c:forEach>
						</shiro:hasAnyPermissions>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${contextPathPrefix}"></form>
						</div>
					</div>
					<tags:pageInfo containerId="${containerId}" formId="${formId}"
						levelId="up" />
					<table
						class="table dataTable table-striped table-bordered table-hover table-checkable"
						id="content_list">
						<thead>
							<tr>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="type">导入类型</th>
								<th class="sorting" abbr="fileName">文件名称</th>
								<th class="sorting" abbr="importTime">导入时间</th>
								<!-- 								<th class="sorting" abbr="success">成功数</th> -->
								<!-- 								<th class="sorting" abbr="failure">失败数</th> -->
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="type">导入类型</th>
								<th class="sorting" abbr="fileName">文件名称</th>
								<th class="sorting" abbr="importTime">导入时间</th>
								<!-- 								<th class="sorting" abbr="success">成功数</th> -->
								<!-- 								<th class="sorting" abbr="failure">失败数</th> -->
								<th>操作</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="t" varStatus="status">
								<tr>
									<td>${status.index+1}</td>
									<td>${t.id}</td>
									<td><tags:type value='${t.type}' /></td>
									<td>${t.fileName}</td>
									<td><fmt:formatDate value="${t.importTime}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<%-- 									<td>${t.success}</td> --%>
									<%-- 									<td>${t.failure}</td> --%>
									<td></td>
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
	$.MediaImportController.init('${formId}');
	$.MediaImportController.keypress({
		containerId : '${containerId}',
		formId : '${formId}'
	});
</script>
