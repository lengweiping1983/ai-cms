<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>用户管理
					</div>
					<div class="actions">
						<c:if test="${param.from eq 'subscriberGroup'}">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.common.ajaxLoadLastContent()"> <i
								class="fa fa-reply"></i> 返回${param.managementName}
							</a>
						</c:if>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/subscriber/subscriber/">
								<input type="hidden" name="from" value="${param.from}">
								<input type="hidden" name="managementName" value="${param.managementName}">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">所属应用: <select
												name="search_appCode__EQ_S"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="app" items="${appList}">
														<option value="${app.code}"
															<c:if test="${! empty param.search_appCode__EQ_S && app.code eq param.search_appCode__EQ_S}">selected="selected"</c:if>>${app.name}</option>
													</c:forEach>
											</select>
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">业务帐号: <input type="text"
												name="search_partnerUserId__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_partnerUserId__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">创建时间: <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_createTime__GE_D" name="search_createTime__GE_D"
												value="${param.search_createTime__GE_D}"> <span
												class="">到</span> <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_createTime__LE_D" name="search_createTime__LE_D"
												value="${param.search_createTime__LE_D}">
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
									<th class="sorting" abbr="appCode">所属应用</th>
									<th class="sorting" abbr="groupCode">用户分组代码</th>
									<th class="sorting" abbr="partnerUserId">业务帐号</th>
									<th class="sorting" abbr="userName">用户名</th>
									<th class="sorting" abbr="createTime">创建时间</th>
									<th class="sorting" abbr="status">状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<th class="sorting" abbr="appCode">所属应用</th>
									<th class="sorting" abbr="groupCode">用户分组代码</th>
									<th class="sorting" abbr="partnerUserId">业务帐号</th>
									<th class="sorting" abbr="userName">用户名</th>
									<th class="sorting" abbr="createTime">创建时间</th>
									<th class="sorting" abbr="status">状态</th>
									<th>操作</th>
								</tr>
							</tfoot>
							<tbody>
								<c:forEach items="${page.content}" var="t">
									<tr>
										<td>${t.appCode}</td>
										<td>${t.groupCode}</td>
										<td>${t.partnerUserId}</td>
										<td>${t.userName}</td>
										<td><fmt:formatDate value="${t.createTime}"
												pattern="yyyy-MM-dd HH:mm:ss" /></td>
										<td><c:if test="${t.status eq 0 }">
												<span class="badge badge-info">已开户</span>
											</c:if> <c:if test="${t.status eq 1 }">
												<span class="badge badge-success">已激活</span>
											</c:if> <c:if test="${t.status eq 2 }">
												<span class="badge badge-danger">已停机</span>
											</c:if> <c:if test="${t.status eq 3 }">
												<span class="badge badge-danger">已销户</span>
											</c:if> <c:if test="${t.status eq 4 }">
												<span class="badge badge-danger">已暂停</span>
											</c:if></td>
										<td>
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.SubscriberController.toEdit('${ctx}/subscriber/subscriber/${t.id}/edit',${t.id});">
												<i class="fa fa-edit"></i>修改用户所属分组
											</button>
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.SubscriberController.toMyOrder('${ctx}/orderRecord/orderRecord','${t.partnerUserId}','用户列表');">
												<i class="fa fa-eye"></i>查看用户订单
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