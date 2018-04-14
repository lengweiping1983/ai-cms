<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="table-container">
	<div class="table-actions-wrapper-condition">
		<form action="${ctx}/league/league/selectItem" id="selectItem">
			<div class="row">
				<div class="col-md-12">
					<div class="col-md-4">
						<label class="control-label">管理名称: <input type="text"
							name="search_name__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_name__LIKE_S}">
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">TAG: <input type="text"
							name="search_tag__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_tag__LIKE_S}">
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">状态: <select
							name="search_status__EQ_I"
							class="form-control input-small input-inline">
								<option value="">全部</option>
								<c:forEach var="item" items="${statusEnum}">
									<option value="${item.key}"
										<c:if test="${! empty param.search_status__EQ_I && item.key eq param.search_status__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
								</c:forEach>
						</select>
						</label>
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
	<table class="table dataTable table-striped table-bordered table-hover">
		<thead>
			<tr>
				<th class="sorting" abbr="name">管理名称</th>
				<th class="sorting" abbr="tag">TAG</th>
				<th class="sorting" abbr="area">地区</th>
				<th class="sorting" abbr="num">总赛程数</th>
				<th class="sorting" abbr="status">状态</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<th class="sorting" abbr="name">管理名称</th>
				<th class="sorting" abbr="tag">TAG</th>
				<th class="sorting" abbr="area">地区</th>
				<th class="sorting" abbr="num">总赛程数</th>
				<th class="sorting" abbr="status">状态</th>
			</tr>
		</tfoot>
		<tbody>
			<c:forEach items="${page.content}" var="t">
				<tr>
					<td><a href="javascript:;"
						onclick="$.LeagueController.selectItem('${t.id}','${t.name}','${t.name}','${t.image1}','${t.image2}','${t.status}');">${t.name}</a></td>
					<td>${t.tag}</td>
					<td>${t.area}</td>
					<td>${t.num}</td>
					<td><tags:status value='${t.status}' /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<tags:pageInfo containerId="content_list_modal_container_body" formId="selectItem" />
</div>


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