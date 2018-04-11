<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="table-container">
	<div class="table-actions-wrapper-condition">
		<form action="${ctx}/uri/uri/selectItem" id="selectItem">
			<input type="hidden" name="selectMode" value="${param.selectMode}">
			<div class="row">
				<div class="col-md-12">
					<div class="col-md-4">
						<label class="control-label">&#12288;页面类型:<select
							name="search_type__EQ_I"
							class="form-control input-small input-inline">
								<option value="">全部</option>
								<c:forEach var="item" items="${uriTypeEnum}">
									<option value="${item.key}"
										<c:if test="${! empty param.search_type__EQ_I && item.key eq param.search_type__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
								</c:forEach>
						</select>
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">页面代码:<input type="text"
							name="search_code__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_code__LIKE_S}">
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">管理名称:<input type="text"
							name="search_name__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_name__LIKE_S}">
						</label>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="col-md-4">
						<label class="control-label">&#12288;显示名称:<input
							type="text" name="search_title__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_title__LIKE_S}">
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">&#12288;&#12288;TAG:<input
							type="text" name="search_tags__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_tags__LIKE_S}">
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">&#12288;关键字:<input
							type="text" name="search_keyword__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_keyword__LIKE_S}">
						</label>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="col-md-4">
						<label class="control-label">本系统页面:<select
							name="search_internal__EQ_I"
							class="form-control input-small input-inline">
								<option value="">全部</option>
								<c:forEach var="item" items="${yesNoEnum}">
									<option value="${item.key}"
										<c:if test="${! empty param.search_internal__EQ_I && item.key eq param.search_internal__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
								</c:forEach>
						</select>
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">上线状态:<select
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
								onclick="$.Page.queryByForm({containerId: 'content_list_container', formId: 'selectItem'});">
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
				<th class="sorting" abbr="type">页面类型</th>
				<th class="sorting" abbr="code">页面代码</th>
				<th class="sorting" abbr="name">管理名称</th>
				<th class="sorting" abbr="title">显示名称</th>
				<th class="sorting" abbr="internal">本系统页面</th>
				<th class="sorting" abbr="status">上线状态</th>
				<th class="sorting" abbr="sortIndex">排序值</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<th class="sorting" abbr="type">页面类型</th>
				<th class="sorting" abbr="code">页面代码</th>
				<th class="sorting" abbr="name">管理名称</th>
				<th class="sorting" abbr="title">显示名称</th>
				<th class="sorting" abbr="internal">本系统页面</th>
				<th class="sorting" abbr="status">上线状态</th>
				<th class="sorting" abbr="sortIndex">排序值</th>
			</tr>
		</tfoot>
		<tbody>
			<c:forEach items="${page.content}" var="t">
				<tr>
					<td><c:forEach var="item" items="${uriTypeEnum}">
							<c:if test="${item.key eq t.type}">
													${item.value}
													</c:if>
						</c:forEach></td>
					<td>${t.code}</td>
					<td><a href="javascript:;"
						onclick="$.UriController.selectItem('${param.selectMode}','${t.id}','${t.name}','${t.title}','${t.icon}','${t.iconFocus}','${t.status}');">${t.name}</a></td>
					<td><a href="javascript:;"
						onclick="$.UriController.selectItem('${param.selectMode}','${t.id}','${t.name}','${t.title}','${t.icon}','${t.iconFocus}','${t.status}');">${t.title}</a></td>
					<td><c:forEach var="item" items="${yesNoEnum}">
							<c:if test="${item.key eq t.internal}">
													${item.value}
													</c:if>
						</c:forEach></td>
					<td><tags:status value='${t.status}' /></td>
					<td>${t.sortIndex}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<tags:pageInfo containerId="content_list_container" formId="selectItem" />
</div>


<script>
	$(".table-actions-wrapper-condition").keypress(function(e) {
		var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
		if (keyCode == 13) {
			$.Page.queryByForm({
				containerId : 'content_list_container',
				formId : 'selectItem'
			});
			return false;
		}
	});
</script>