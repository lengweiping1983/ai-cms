<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<script src="${ctx}/static/scripts/app/app-controller.js"
	type="text/javascript"></script>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>APP管理
					</div>
					<div class="actions">
						<a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.AppController.toEdit('${ctx}/app/app/add')">
							<i class="fa fa-plus"></i>增加APP
						</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/app/app/">
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
											<label class="control-label">APP代码: <input type="text"
												class="form-control input-small input-inline"
												name="search_code__LIKE_S"
												value="${param.search_code__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">APP名称: <input type="text"
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
											<label class="control-label">&#12288;&#12288;状态:
												<select name="search_status__EQ_I"
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
									<th class="sorting" abbr="type">类型</th>
									<th class="sorting" abbr="code">APP代码</th>
									<th class="sorting" abbr="name">APP名称</th>
									<th class="sorting" abbr="status">状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<th>序号</th>
									<th class="sorting" abbr="id">ID</th>
									<th class="sorting" abbr="type">类型</th>
									<th class="sorting" abbr="code">APP代码</th>
									<th class="sorting" abbr="name">APP名称</th>
									<th class="sorting" abbr="status">状态</th>
									<th>操作</th>
								</tr>
							</tfoot>
							<tbody>
								<c:forEach items="${page.content}" var="t" varStatus="status">
									<tr>
										<td>${status.index+1}</td>
										<td>${t.id}</td>
										<td><c:forEach var="item" items="${typeEnum}">
												<c:if test="${item.key eq t.type }"> ${item.value}</c:if>
											</c:forEach></td>
										<td>${t.code}</td>
										<td>${t.name}</td>
										<td><c:forEach var="item" items="${statusEnum}">
												<c:if test="${item.key eq t.status && item.key eq 0}">
													<span class="badge badge-danger">${item.value}</span>
												</c:if>
												<c:if test="${item.key eq t.status && item.key eq 1}">
													<span class="badge badge-success">${item.value}</span>
												</c:if>
											</c:forEach></td>
										<td>
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.AppController.toEdit('${ctx}/app/app/${t.id}/edit',${t.id});">
												<i class="fa fa-edit"></i>修改
											</button>
											<c:forEach var="item" items="${statusEnum}">
												<c:if test="${item.key eq t.status && item.key eq 0}">
<!-- 													<button class="btn btn-default btn-sm btn-outline green" -->
<%-- 														onclick="$.AppController.toDelete('${ctx}/app/app/${t.id}/delete','${t.name}');"> --%>
<!-- 														<i class="fa fa-remove"></i>删除 -->
<!-- 													</button> -->
												</c:if>
												<c:if test="${item.key eq t.status && item.key eq 1}">
												</c:if>
											</c:forEach>
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
    $(".table-actions-wrapper-condition").keypress(function (e) {
        var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
        if (keyCode == 13) {
        	$.Page.queryByForm();
        	return false;
        }
    });
</script>
