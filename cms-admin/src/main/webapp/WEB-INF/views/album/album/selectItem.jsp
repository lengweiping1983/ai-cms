<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="table-container">
	<div class="table-actions-wrapper-condition">
		<form action="${ctx}/album/album/selectItem" id="selectItem">
			<input type="hidden" name="selectMode" value="${param.selectMode}">
			<div class="row">
				<div class="col-md-12">
					<div class="col-md-4">
						<label class="control-label">专题类型: <select
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
						<label class="control-label">专题代码: <input type="text"
							class="form-control input-small input-inline"
							name="search_code__LIKE_S" value="${param.search_code__LIKE_S}">
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">专题名称: <input type="text"
							name="search_name__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_name__LIKE_S}">
						</label>
					</div>
				</div>
				<div class="col-md-12">
					<div class="col-md-4">
						<label class="control-label">显示名称: <input type="text"
							name="search_title__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_title__LIKE_S}">
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">使用模板: <select
							name="search_templateCode__EQ_S"
							class="form-control input-small input-inline">
								<option value="">全部</option>
								<c:forEach var="template" items="${templateList}">
									<option value="${template.code}"
										<c:if test="${! empty param.search_templateCode__EQ_S && template.code eq param.search_templateCode__EQ_S}">selected="selected"</c:if>>${template.name}</option>
								</c:forEach>
						</select>
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">上线状态: <select
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
				</div>
				<div class="col-md-12">
					<div class="col-md-4">
						<label class="control-label">所属APP: <select
							class="form-control input-small input-inline"
							name="search_appCode__EQ_S">
								<option value="">全部</option>
								<c:forEach var="app" items="${appList}">
									<option value="${app.code}"
										<c:if test="${(! empty param.search_appCode__EQ_S && app.code eq param.search_appCode__EQ_S) || app.code eq search_appCode__EQ_S}">selected="selected"</c:if>>${app.name}</option>
								</c:forEach>
						</select>
						</label>
					</div>
					<div class="col-md-8">
						<div style="float: right">
							<button class="btn btn-default btn-sm btn-outline green"
								type="button"
								onclick="$.Page.queryByForm({containerId: 'content_list_modal_container_body', formId: 'selectItem'});">
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
	<table class="table dataTable table-striped table-bordered table-hover" 
	id="selectItem_content_list">
		<thead>
			<tr>
				<c:if test="${param.selectMode eq 100}">
					<th width="6%"><div class="checker">
							<span class=""><input type="checkbox"
								class="group-checkable"
								data-set="#selectItem_content_list .checkboxes" /></span>
						</div></th>
				</c:if>
				<th>序号</th>
				<th class="sorting" abbr="id">ID</th>
				<th class="sorting" abbr="type">专题类型</th>
				<th class="sorting" abbr="code">专题代码</th>
				<th class="sorting" abbr="name">专题名称</th>
				<th class="sorting" abbr="title">显示名称</th>
				<th class="sorting" abbr="templateCode">使用模板</th>
				<th class="sorting" abbr="status">上线状态</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<c:if test="${param.selectMode eq 100}">
					<th><div class="checker">
							<span class=""><input type="checkbox"
								class="group-checkable"
								data-set="#selectItem_content_list .checkboxes" /></span>
						</div></th>
				</c:if>
				<th>序号</th>
				<th class="sorting" abbr="id">ID</th>
				<th class="sorting" abbr="type">专题类型</th>
				<th class="sorting" abbr="code">专题代码</th>
				<th class="sorting" abbr="name">专题名称</th>
				<th class="sorting" abbr="title">显示名称</th>
				<th class="sorting" abbr="templateCode">使用模板</th>
				<th class="sorting" abbr="status">上线状态</th>
			</tr>
		</tfoot>
		<tbody>
			<c:forEach items="${page.content}" var="t" varStatus="status">
				<tr>
					<c:if test="${param.selectMode eq 100}">
						<td><div class="checker">
								<span class=""><input type="checkbox" class="checkboxes"
									id="{'id':'${t.id }','name':'${t.name}','title':'${t.title}','status':'${t.status}'}"
									value="${t.id}" data-name="${t.name}" /></span>
							</div></td>
					</c:if>
					<td>${status.index+1}</td>
					<td>${t.id}</td>
					<td><tags:type value='${t.type}' /></td>
					<td><c:choose>
						<c:when test="${param.selectMode eq 100}">
						${t.title}
						</c:when>
						<c:otherwise>
							<a href="javascript:;"
							onclick="$.AlbumController.selectItem('${param.selectMode}','${t.id}','${t.name}','${t.title}','${t.image1}','${t.image2}','${t.status}');">${t.code}</a>
						</c:otherwise>
					</c:choose></td>
					<td><c:choose>
						<c:when test="${param.selectMode eq 100}">
						${t.title}
						</c:when>
						<c:otherwise>
							<a href="javascript:;"
							onclick="$.AlbumController.selectItem('${param.selectMode}','${t.id}','${t.name}','${t.title}','${t.image1}','${t.image2}','${t.status}');">${t.name}</a>
						</c:otherwise>
					</c:choose></td>
					<td><c:choose>
						<c:when test="${param.selectMode eq 100}">
						${t.title}
						</c:when>
						<c:otherwise>
							<a href="javascript:;"
							onclick="$.AlbumController.selectItem('${param.selectMode}','${t.id}','${t.name}','${t.title}','${t.image1}','${t.image2}','${t.status}');">${t.title}</a>
						</c:otherwise>
					</c:choose></td>
					<td><c:forEach var="template" items="${templateList}">
							<c:if
								test="${! empty t.templateCode && template.code eq t.templateCode}"> ${template.name}</c:if>
						</c:forEach></td>
					<td><tags:status value='${t.status}' /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<tags:pageInfo containerId="content_list_modal_container_body" formId="selectItem" />
</div>


<script>
	$.AlbumController.init('selectItem_');
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
	if ('${param.selectMode}' == '100') {
		if ($("#content_list_modal_container_ok")[0]) {
			$('#content_list_modal_container_ok')
					.attr("onclick",
							"$.AlbumController.selectCheckedItem('${param.selectMode}','selectItem_');");
			$('#content_list_modal_container_ok').show();
		}
	}
</script>