<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="table-container">
	<div class="table-actions-wrapper-condition">
		<form action="${ctx}/live/channel/selectChannel" id="selectChannel">
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
						<label class="control-label">中央频道代码: <input type="text"
							class="form-control input-small input-inline"
							name="search_code__LIKE_S" value="${param.search_code__LIKE_S}">
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">中央频道名称: <input type="text"
							name="search_name__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_name__LIKE_S}">
						</label>
					</div>

				</div>
				<div class="col-md-12">
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
					<div class="col-md-8">
						<div style="float: right">
							<button class="btn btn-default btn-sm btn-outline green"
								type="button"
								onclick="$.Page.queryByForm({containerId: 'content_list_container', formId: 'selectChannel'});">
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
				<th class="sorting" abbr="code">中央频道代码</th>
				<th class="sorting" abbr="name">中央频道名称</th>
				<th class="sorting" abbr="status">状态</th>
				<th class="sorting" abbr="sortIndex">排序值</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<th class="sorting" abbr="type">类型</th>
				<th class="sorting" abbr="code">中央频道代码</th>
				<th class="sorting" abbr="name">中央频道名称</th>
				<th class="sorting" abbr="status">状态</th>
				<th class="sorting" abbr="sortIndex">排序值</th>
			</tr>
		</tfoot>
		<tbody>
			<c:forEach items="${page.content}" var="t">
				<tr>
					<td><tags:type value='${t.type}' /></td>
					<td><a href="javascript:;"
						onclick="$.ChannelController.selectChannel(${t.id},'${t.name}','${t.code}','${t.type}');">${t.code}</a></td>
					<td><a href="javascript:;"
						onclick="$.ChannelController.selectChannel(${t.id},'${t.name}','${t.code}','${t.type}');">${t.name}</a></td>
					<td><tags:status value='${t.status}' /></td>
					<td>${t.sortIndex}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<tags:pageInfo containerId="content_list_container"
		formId="selectChannel" />
</div>

<script>
    $(".table-actions-wrapper-condition").keypress(function (e) {
        var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
        if (keyCode == 13) {
        	$.Page.queryByForm({containerId: 'content_list_container', formId: 'selectChannel'});
        	return false;
        }
    });
</script>
