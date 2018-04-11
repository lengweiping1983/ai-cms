<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>联赛管理
					</div>
					<div class="actions">
						<a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.LeagueController.toEdit('${ctx}/league/league/add')">
							<i class="fa fa-plus"></i>增加联赛
						</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/league/league/">
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
													type="button" onclick="$.Page.queryByForm();">
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
						<table
							class="table dataTable table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th>序号</th>
									<th class="sorting" abbr="id">ID</th>
									<th class="sorting" abbr="name">管理名称</th>
									<th class="sorting" abbr="tag">TAG</th>
									<th class="sorting" abbr="area">地区</th>
									<th class="sorting" abbr="num">总赛程数</th>
									<th class="sorting" abbr="status">状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<th>序号</th>
									<th class="sorting" abbr="id">ID</th>
									<th class="sorting" abbr="name">管理名称</th>
									<th class="sorting" abbr="tag">TAG</th>
									<th class="sorting" abbr="area">地区</th>
									<th class="sorting" abbr="num">总赛程数</th>
									<th class="sorting" abbr="status">状态</th>
									<th>操作</th>
								</tr>
							</tfoot>
							<tbody>
								<c:forEach items="${page.content}" var="t" varStatus="status">
									<tr>
										<td>${status.index+1}</td>
										<td>${t.id}</td>
										<td><a href="javascript:;"
											onclick="$.LeagueController.detail('${t.id}');">${t.name}</a></td>
										<td>${t.tag}</td>
										<td>${t.area}</td>
										<td>${t.num}</td>
										<td><tags:status value='${t.status}' /></td>
										<td><c:set var="statusMethodName"
												value="${fns:getStatusMothodName(t.status)}" />
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.LeagueController.toChangeStatus('${ctx}/league/league/${t.id}/status','${t.name}','${statusMethodName}');">
												<i class="fa fa-pencil"></i>${statusMethodName}
											</button>
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.LeagueController.toEdit('${ctx}/league/league/${t.id}/edit',${t.id});">
												<i class="fa fa-edit"></i>修改
											</button> <tags:deleteByStatus value='${t.status}'
												onclick="$.LeagueController.toDelete('${ctx}/league/league/${t.id}/delete','${t.name}');" />
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<tags:pageInfo />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END CONTENT BODY -->

<tags:contentModal />


<script>
if (jQuery().datepicker) {
    $('.date-picker').datepicker({
        rtl: App.isRTL(),
        orientation: "left",
        format: 'yyyy-mm-dd',
        language: 'zh-CN',
        autoclose: true
    });
}
    $(".table-actions-wrapper-condition").keypress(function (e) {
        var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
        if (keyCode == 13) {
        	$.Page.queryByForm();
        	return false;
        }
    });
</script>