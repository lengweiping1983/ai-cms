<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="table-container">
	<div class="table-actions-wrapper-condition">
		<form action="${ctx}/star/star/selectStar?type=${type}"
			id="selectStar">
			<div class="row">
				<div class="col-md-12">
					<div class="col-md-4">
						<label class="control-label">&#12288;&#12288;类型: <select
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
						<label class="control-label">所属俱乐部: <select
							name="search_clubId__EQ_L"
							class="form-control input-small input-inline">
								<option value="">全部</option>
								<c:forEach var="item" items="${clubList}">
									<option value="${item.id}"
										<c:if test="${! empty param.search_clubId__EQ_L && app.code eq param.search_clubId__EQ_L}">selected="selected"</c:if>>${item.name}</option>
								</c:forEach>
						</select>
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">TAG: <input type="text"
							name="search_tag__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_tag__LIKE_S}">
						</label>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="col-md-4">
						<label class="control-label">明星姓名: <input type="text"
							name="search_name__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_name__LIKE_S}">
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">&#12288;&#12288;&#12288;状态: <select
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
					<div class="col-md-4">
						<div style="float: right">
							<button class="btn btn-default btn-sm btn-outline green"
								type="button"
								onclick="$.Page.queryByForm({containerId: 'content_list_modal_container_body', formId: 'selectStar'});">
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
	<table class="table dataTable table-striped table-bordered table-hover">
		<thead>
			<tr>
				<th class="sorting" abbr="type">类型</th>
				<th>所属俱乐部</th>
				<th class="sorting" abbr="name">明星姓名</th>
				<th class="sorting" abbr="tag">TAG</th>
				<th class="sorting" abbr="status">状态</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<th class="sorting" abbr="type">类型</th>
				<th>所属俱乐部</th>
				<th class="sorting" abbr="name">明星姓名</th>
				<th class="sorting" abbr="tag">TAG</th>
				<th class="sorting" abbr="status">状态</th>
			</tr>
		</tfoot>
		<tbody>
			<c:forEach items="${page.content}" var="t">
				<tr>
					<td><tags:type value='${t.type}' /></td>
					<td><c:forEach var="item" items="${clubList}">
							<c:if test="${! empty t.clubId && item.id eq t.clubId}"> ${item.name}</c:if>
						</c:forEach></td>
					<td><a href="javascript:;"
						onclick="$.StarController.selectStar('${t.id}','${t.name}','${type}');">${t.name}</a></td>
					<td>${t.tag}</td>
					<td><tags:status value='${t.status}' /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<tags:pageInfo containerId="content_list_modal_container_body" formId="selectStar" />
</div>


<script>
	$(".table-actions-wrapper-condition").keypress(function(e) {
		var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
		if (keyCode == 13) {
			$.Page.queryByForm({
				containerId : 'content_list_modal_container_body',
				formId : 'selectStar'
			});
			return false;
		}
	});
</script>