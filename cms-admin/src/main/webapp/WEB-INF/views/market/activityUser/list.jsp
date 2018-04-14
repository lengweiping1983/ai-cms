<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>[${activity.name}]参与用户查询
					</div>
					<div class="actions">
						<c:if test="${param.fromActivity == 'true'}">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.common.ajaxLoadLastContent()"> <i
								class="fa fa-reply"></i> 返回
							</a>
						</c:if>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/activity/activityUser/">
								<input type="hidden" name="fromActivity"
									value="${param.fromActivity}"> <input type="hidden"
									name="search_activityId__EQ_I"
									value="${param.search_activityId__EQ_I}">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">参与时间: <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_createTime__GE_D" name="search_createTime__GE_D"
												value="${param.search_createTime__GE_D}"> <span
												class="">到</span> <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_createTime__LE_D" name="search_createTime__LE_D"
												value="${param.search_createTime__LE_D}">
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
											<label class="control-label">手机号: <input type="text"
												name="search_phone__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_phone__LIKE_S}">
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
					<table
						class="table dataTable table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th>序号</th>
								<th class="sorting" abbr="createTime">参与时间</th>
								<th class="sorting" abbr="partnerUserId">业务帐号</th>
								<th class="sorting" abbr="phone">手机号</th>
								<th class="sorting" abbr="clientIp">客户端IP</th>
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th>序号</th>
								<th class="sorting" abbr="createTime">参与时间</th>
								<th class="sorting" abbr="partnerUserId">业务帐号</th>
								<th class="sorting" abbr="phone">手机号</th>
								<th class="sorting" abbr="clientIp">客户端IP</th>
								<th>操作</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="t" varStatus="status">
								<tr>
									<td>${status.index+1}</td>
									<td><fmt:formatDate value="${t.createTime}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td>${t.partnerUserId}</td>
									<td>${t.phone}</td>
									<td>${t.clientIp}</td>
									<td></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<tags:pageInfo />
					
					<div class="modal-footer">
						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.ActivityUserController.exportAll('${ctx}/activity/activityUser/export','1');">
							<i class="fa fa-cloud-download"></i>导出参与用户(符合条件)
						</button>
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
			rtl : App.isRTL(),
			orientation : "left",
			format : 'yyyy-mm-dd',
			language : 'zh-CN',
			autoclose : true
		});
	}
	$(".table-actions-wrapper-condition").keypress(function(e) {
		var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
		if (keyCode == 13) {
			$.Page.queryByForm();
			return false;
		}
	});
</script>