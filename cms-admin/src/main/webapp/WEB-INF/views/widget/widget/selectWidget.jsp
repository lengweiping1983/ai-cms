<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="table-container">
	<div class="table-actions-wrapper-condition">
		<form
			action="${ctx}/widget/widget/selectWidget?appCode=${param.appCode}"
			id="selectWidget">
			<div class="row">
				<div class="col-md-12">
					<div class="col-md-4">
						<label class="control-label">推荐位代码: <input type="text"
							class="form-control input-small input-inline"
							name="search_code__LIKE_S" value="${param.search_code__LIKE_S}">
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">推荐位名称: <input type="text"
							name="search_name__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_name__LIKE_S}">
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">显示名称: <input type="text"
							name="search_title__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_title__LIKE_S}">
						</label>
					</div>
				</div>
				<div class="col-md-12">
					<div class="col-md-4">
						<label class="control-label">&#12288;&#12288;&#12288;类型: <select
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
						<label class="control-label">&#12288;上线状态: <select
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
							onclick="$.Page.queryByForm({containerId: 'content_list_container', formId: 'selectWidget'});">
							<i class="fa fa-search"></i> 查询
						</button>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
<table
	class="table dataTable table-striped table-bordered table-hover table-checkable"
	id="selectWidget_content_list">
	<thead>
		<tr>
			<th width="3%"><div class="checker">
					<span class=""><input type="checkbox"
						class="group-checkable"
						data-set="#selectWidget_content_list .checkboxes" /></span>
				</div></th>
			<th>序号</th>
			<th class="sorting" abbr="id">ID</th>
			<th class="sorting" abbr="code">推荐位代码</th>
			<th class="sorting" abbr="name">推荐位名称</th>
			<th class="sorting" abbr="title">显示名称</th>
			<th class="sorting" abbr="type">类型</th>
			<th class="sorting" abbr="status">上线状态</th>
		</tr>
	</thead>
	<tfoot>
		<tr>
			<th width="3%"><div class="checker">
					<span class=""><input type="checkbox"
						class="group-checkable"
						data-set="#selectWidget_content_list .checkboxes" /></span>
				</div></th>
			<th>序号</th>
			<th class="sorting" abbr="id">ID</th>
			<th class="sorting" abbr="code">推荐位代码</th>
			<th class="sorting" abbr="name">推荐位名称</th>
			<th class="sorting" abbr="title">显示名称</th>
			<th class="sorting" abbr="type">类型</th>
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
				<td><a href="javascript:;"
					onclick="$.WidgetController.selectWidget(${t.id},'${t.name}');">${t.code}</a></td>
				<td><a href="javascript:;"
					onclick="$.WidgetController.selectWidget(${t.id},'${t.name}');">${t.name}</a></td>
				<td>${t.title}</td>
				<td><c:forEach var="item" items="${typeEnum}">
						<c:if test="${item.key eq t.type}">
                                                ${item.value}
                                            </c:if>
					</c:forEach></td>
				<td><c:forEach var="item" items="${statusEnum}">
						<c:if test="${item.key eq t.status && item.key eq 0}">
							<span class="badge badge-danger">${item.value}</span>
						</c:if>
						<c:if test="${item.key eq t.status && item.key eq 1}">
							<span class="badge badge-success">${item.value}</span>
						</c:if>
					</c:forEach></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<tags:pageInfo containerId="content_list_container"
	formId="selectWidget" />


<script>
    $(".table-actions-wrapper-condition").keypress(function (e) {
        var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
        if (keyCode == 13) {
        	$.Page.queryByForm({containerId: 'content_list_container', formId: 'selectWidget'});
        	return false;
        }
    });
    $.WidgetController.init('selectWidget_');
    if($("#content_list_modal_container_ok")[0]) {
		$('#content_list_modal_container_ok').attr("onclick","$.WidgetController.selectWidgets('selectWidget_');");
		$('#content_list_modal_container_ok').show();
	}
</script>
