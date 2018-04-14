<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>


<div class="table-container">
	<div class="table-actions-wrapper-condition">
		<form action="${ctx}/template/template/selectItem" id="selectItem">
			<input type="hidden" name="selectMode" value="${param.selectMode}">
			<input type="hidden" name="type" value="${param.type}">
			<div class="row">
				<div class="col-md-12">
					<div class="col-md-4">
						<label class="control-label">所属APP: <select
							name="search_appCode__EQ_S"
							class="form-control input-small input-inline">
								<option value="">全部</option>
								<option value="SYSTEM"
									<c:if test="${! empty param.search_appCode__EQ_S && 'SYSTEM' eq param.search_appCode__EQ_S}">selected="selected"</c:if>>系统</option>
								<c:forEach var="app" items="${appList}">
									<option value="${app.code}"
										<c:if test="${! empty param.search_appCode__EQ_S && app.code eq param.search_appCode__EQ_S}">selected="selected"</c:if>>${app.name}</option>
								</c:forEach>
						</select>
						</label>
					</div>
					<c:choose>
						<c:when test="${empty param.type}">
							<div class="col-md-4">
								<label class="control-label">模板类型: <select
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
						</c:when>
						<c:otherwise>
							<input type="hidden" name="search_type__EQ_I"
								value="${param.type}">
							<div class="col-md-4">
								<label class="control-label">模板类型: <select
									name="search_type__EQ_I" disabled="disabled"
									class="form-control input-small input-inline">
										<option value="">全部</option>
										<c:forEach var="item" items="${typeEnum}">
											<option value="${item.key}"
												<c:if test="${! empty param.search_type__EQ_I && item.key eq param.search_type__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
										</c:forEach>
								</select>
								</label>
							</div>
						</c:otherwise>
					</c:choose>

					<div class="col-md-4">
						<label class="control-label">模板代码: <input type="text"
							class="form-control input-small input-inline"
							name="search_code__LIKE_S" value="${param.search_code__LIKE_S}">
						</label>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="col-md-4">
						<label class="control-label">模板名称: <input type="text"
							name="search_name__LIKE_S"
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
					</div>
					<div style="float: right">
						<button class="btn btn-default btn-sm btn-outline green"
							type="button"
							onclick="$.Page.queryByForm({containerId: 'content_list_modal_container_body', formId: 'selectItem'});">
							<i class="fa fa-search"></i> 查询
						</button>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
<table class="table dataTable table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th>序号</th>
			<th class="sorting" abbr="id">ID</th>
			<th class="sorting" abbr="appCode">所属APP</th>
			<th class="sorting" abbr="type">模板类型</th>
			<th class="sorting" abbr="code">模板代码</th>
			<th class="sorting" abbr="name">模板名称</th>
			<th class="sorting" abbr="status">状态</th>
			<th>效果图</th>
		</tr>
	</thead>
	<tfoot>
		<tr>
			<th>序号</th>
			<th class="sorting" abbr="id">ID</th>
			<th class="sorting" abbr="appCode">所属APP</th>
			<th class="sorting" abbr="type">模板类型</th>
			<th class="sorting" abbr="code">模板代码</th>
			<th class="sorting" abbr="name">模板名称</th>
			<th class="sorting" abbr="status">状态</th>
			<th>效果图</th>
		</tr>
	</tfoot>
	<tbody>
		<c:forEach items="${page.content}" var="t" varStatus="status">
			<tr>
				<td>${status.index+1}</td>
				<td>${t.id}</td>
				<td><c:if test="${! empty t.appCode && 'SYSTEM' eq t.appCode}"> 系统</c:if>
					<c:forEach var="app" items="${appList}">
						<c:if test="${! empty t.appCode && app.code eq t.appCode}"> ${app.name}</c:if>
					</c:forEach></td>
				<td><c:forEach var="item" items="${typeEnum}">
						<c:if test="${item.key eq t.type }"> ${item.value}</c:if>
					</c:forEach></td>
				<td>${t.code}</td>
				<td><a href="javascript:;"
					onclick="$.TemplateController.selectItem('${param.selectMode}','${t.id}','${t.name}','${t.code}','${t.appCode}');">${t.name}</a></td>
				<td><c:forEach var="item" items="${statusEnum}">
						<c:if test="${item.key eq t.status && item.key eq 0}">
							<span class="badge badge-danger">${item.value}</span>
						</c:if>
						<c:if test="${item.key eq t.status && item.key eq 1}">
							<span class="badge badge-success">${item.value}</span>
						</c:if>
					</c:forEach></td>
				<td><c:set var="imageUrl"
						value="${ctx}/static/img/no-image.png" /> <c:if
						test="${! empty t.backgroundImage}">
						<c:set var="imageUrl"
							value="${fns:getImagePath(t.backgroundImage)}" />
					</c:if>
					<div class="fileinput-preview thumbnail" data-trigger="fileinput"
						style="width: 320px; height: 190px;">
						<a href="javascript:;"
							onclick="$.TemplateController.selectItem('${param.selectMode}','${t.id}','${t.name}','${t.code}','${t.appCode}');"><img
							src="${imageUrl}" alt="" style="width: 320px; height: 180px;" /></a>
					</div></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<tags:pageInfo containerId="content_list_modal_container_body" formId="selectItem" />

<script>
	$(".table-actions-wrapper-condition").keypress(function(e) {
		var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
		if (keyCode == 13) {
			$.Page.queryByForm({
				containerId : 'content_list_modal_container_body',
				formId : 'selectItem'
			});
			return false;
		}
	});
</script>

