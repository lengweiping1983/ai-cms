<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>外部入口管理
					</div>
					<div class="actions">
						<a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.AppConfigController.toEdit('${ctx}/config/appConfig/add')">
							<i class="fa fa-plus"></i>增加外部入口
						</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/config/appConfig/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">外部入口代码: <input type="text"
												class="form-control input-small input-inline"
												name="search_appCode__LIKE_S"
												value="${param.search_appCode__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">外部入口名称: <input type="text"
												name="search_appName__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_appName__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
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
									<th class="sorting" abbr="id">外部入口id</th>
									<th class="sorting" abbr="appCode">外部入口代码</th>
									<th class="sorting" abbr="appName">外部入口名称</th>
									<th class="sorting" abbr="appName">服务URL</th>
									<th>操作</th>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<th class="sorting" abbr="id">外部入口id</th>
									<th class="sorting" abbr="appCode">外部入口代码</th>
									<th class="sorting" abbr="appName">外部入口名称</th>
									<th class="sorting" abbr="appName">服务URL</th>
									<th>操作</th>
								</tr>
							</tfoot>
							<tbody>
								<c:forEach items="${page.content}" var="t">
									<tr>
										<td>${t.id}</td>
										<td>${t.appCode}</td>
										<td>${t.appName}</td>
										<td>${t.serviceUrl}</td>
										<td>
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.AppConfigController.toEdit('${ctx}/config/appConfig/${t.id}/edit',${t.id});">
												<i class="fa fa-edit"></i>修改
											</button>
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.AppConfigController.toDelete('${ctx}/config/appConfig/${t.id}/delete','${t.appName}');">
												<i class="fa fa-remove"></i>删除
											</button>
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
