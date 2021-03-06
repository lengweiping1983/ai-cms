<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="table-container">
	<div class="table-actions-wrapper-condition">
		<form action="${ctx}/album/album/selectAlbum?appCode=${param.appCode}"
			id="selectAlbum">
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
								onclick="$.Page.queryByForm({containerId: 'content_list_modal_container_body', formId: 'selectAlbum'});">
								<i class="fa fa-search"></i> 查询
							</button>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
<table
	class="table dataTable table-striped table-bordered table-hover table-checkable"
	id="selectAlbum_content_list">
	<thead>
		<tr>
			<th width="3%"><div class="checker">
					<span class=""><input type="checkbox"
						class="group-checkable"
						data-set="#selectAlbum_content_list .checkboxes" /></span>
				</div></th>
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
			<th width="3%"><div class="checker">
					<span class=""><input type="checkbox"
						class="group-checkable"
						data-set="#selectAlbum_content_list .checkboxes" /></span>
				</div></th>
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
					<td><div class="checker">
						<span class=""><input type="checkbox" class="checkboxes"
							value="${t.id}" data-name="${t.name}" /></span>
					</div></td>
					<td>${status.index+1}</td>
					<td>${t.id}</td>
					<td><tags:type value='${t.type}' /></td>
					<td><a href="javascript:;"
						onclick="$.AlbumController.selectAlbum(${t.id},'${t.name}');">${t.code}</a></td>
					<td><a href="javascript:;"
						onclick="$.AlbumController.selectAlbum(${t.id},'${t.name}');">${t.name}</a></td>
					<td><a href="javascript:;"
						onclick="$.AlbumController.selectAlbum(${t.id},'${t.name}');">${t.title}</a></td>
					<td><c:forEach var="template" items="${templateList}">
							<c:if
								test="${! empty t.templateCode && template.code eq t.templateCode}"> ${template.name}</c:if>
						</c:forEach></td>
					<td><tags:status value='${t.status}' /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<tags:pageInfo containerId="content_list_modal_container_body"
		formId="selectAlbum" />

<script>
    $(".table-actions-wrapper-condition").keypress(function (e) {
        var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
        if (keyCode == 13) {
        	$.Page.queryByForm({containerId: 'content_list_modal_container_body', formId: 'selectAlbum'});
        	return false;
        }
    });
    $.AlbumController.init('selectAlbum_');
	if($("#content_list_modal_container_ok")[0]) {
		$('#content_list_modal_container_ok').attr("onclick","$.AlbumController.selectAlbums('selectAlbum_');");
		$('#content_list_modal_container_ok').show();
	}
</script>
