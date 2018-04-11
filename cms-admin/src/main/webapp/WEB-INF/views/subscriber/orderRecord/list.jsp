<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>订单管理
					</div>
					<div class="actions">
						<c:if test="${param.from eq 'subscriber'}">
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
							<form action="${ctx}/orderRecord/orderRecord/">
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
											<label class="control-label">&#12288;订单号: <input
												type="text" name="search_orderCode__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_orderCode__LIKE_S}">
											</label>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">产品代码: <input type="text"
												name="search_productCode__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_productCode__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">内容代码: <input type="text"
												name="search_contentCode__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_contentCode__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">元素类型: <select
												name="search_itemType__EQ_I"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${itemTypeEnum}">
														<option value="${item.key}"
															<c:if test="${! empty param.search_itemType__EQ_I && item.key eq param.search_itemType__EQ_I}">selected="selected"</c:if>>${item.value}</option>
													</c:forEach>
											</select>
											</label>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">&#12288;元素ID: <input
												type="text" name="search_itemId__EQ_I"
												class="form-control input-small input-inline"
												value="${param.search_itemId__EQ_I}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">元素名称: <input type="text"
												name="search_itemName__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_itemName__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">播放方式: <select
												name="search_playMode__EQ_S"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<option value="live"
														<c:if test="${param.search_playMode__EQ_S eq 'live'}">selected="selected"</c:if>>直播</option>
													<option value="vod"
														<c:if test="${param.search_playMode__EQ_S eq 'vod'}">selected="selected"</c:if>>点播</option>
											</select>
											</label>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">订购时间: <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_subscriptionTime__GE_D"
												name="search_subscriptionTime__GE_D"
												value="${param.search_subscriptionTime__GE_D}"> <span
												class="">到</span> <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_subscriptionTime__LE_D"
												name="search_subscriptionTime__LE_D"
												value="${param.search_subscriptionTime__LE_D}">
											</label>
										</div>

										<div class="col-md-4">
											<label class="control-label">退订时间: <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_unsubscribeTime__GE_D"
												name="search_unsubscribeTime__GE_D"
												value="${param.search_unsubscribeTime__GE_D}"> <span
												class="">到</span> <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_unsubscribeTime__LE_D"
												name="search_unsubscribeTime__LE_D"
												value="${param.search_unsubscribeTime__LE_D}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">支付状态: <select
												name="search_paymentStatus__EQ_I"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<option value="0"
														<c:if test="${param.search_paymentStatus__EQ_I eq '0'}">selected="selected"</c:if>>待支付</option>
													<option value="1"
														<c:if test="${param.search_paymentStatus__EQ_I eq '1'}">selected="selected"</c:if>>支付成功</option>
													<option value="2"
														<c:if test="${param.search_paymentStatus__EQ_I eq '2'}">selected="selected"</c:if>>支付失败</option>
													<option value="3"
														<c:if test="${param.search_paymentStatus__EQ_I eq '3'}">selected="selected"</c:if>>支付超时</option>
													<option value="4"
														<c:if test="${param.search_paymentStatus__EQ_I eq '4'}">selected="selected"</c:if>>取消支付</option>
											</select>
											</label>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">生效时间: <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_validTime__GE_D" name="search_validTime__GE_D"
												value="${param.search_validTime__GE_D}"> <span
												class="">到</span> <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_validTime__LE_D" name="search_validTime__LE_D"
												value="${param.search_validTime__LE_D}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">过期时间: <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_expiredTime__GE_D"
												name="search_expiredTime__GE_D"
												value="${param.search_expiredTime__GE_D}"> <span
												class="">到</span> <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_expiredTime__LE_D"
												name="search_expiredTime__LE_D"
												value="${param.search_expiredTime__LE_D}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">外部入口: <input type="text"
												name="search_fromEntrance__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_fromEntrance__LIKE_S}">
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
						class="table dataTable table-striped table-bordered table-hover table-checkable"
						id="content_list">
						<thead>
							<tr>
								<th>
									<div class="checker">
										<span class=""><input type="checkbox"
											class="group-checkable" data-set="#content_list .checkboxes" /></span>
									</div>
								</th>
								<th>序号</th>
								<th class="sorting" abbr="appCode">所属应用</th>
								<th class="sorting" abbr="partnerUserId">业务帐号</th>
								<th class="sorting" abbr="orderCode">订单号</th>
								<th class="sorting" abbr="productCode">产品代码</th>
								<th class="sorting" abbr="contentCode">内容代码</th>
								<th class="sorting" abbr="itemType">元素类型</th>
								<th class="sorting" abbr="itemId">元素id</th>
								<th class="sorting" abbr="itemName">元素名称</th>
								<th class="sorting" abbr="playMode">播放方式</th>
								<th class="sorting" abbr="fromEntrance">外部入口</th>
								<th class="sorting" abbr="type">类型</th>
								<th class="sorting" abbr="subscriptionTime">订购时间</th>
								<th class="sorting" abbr="unsubscribeTime">退订时间</th>
								<th class="sorting" abbr="paymentStatus">支付状态</th>
								<th class="sorting" abbr="validTime">生效时间</th>
								<th class="sorting" abbr="expiredTime">过期时间</th>
								<th class="sorting" abbr="clientIp">客户端IP</th>
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th>
									<div class="checker">
										<span class=""><input type="checkbox"
											class="group-checkable" data-set="#content_list .checkboxes" /></span>
									</div>
								</th>
								<th>序号</th>
								<th class="sorting" abbr="appCode">所属应用</th>
								<th class="sorting" abbr="partnerUserId">业务帐号</th>
								<th class="sorting" abbr="orderCode">订单号</th>
								<th class="sorting" abbr="productCode">产品代码</th>
								<th class="sorting" abbr="contentCode">内容代码</th>
								<th class="sorting" abbr="itemType">元素类型</th>
								<th class="sorting" abbr="itemId">元素id</th>
								<th class="sorting" abbr="itemName">元素名称</th>
								<th class="sorting" abbr="playMode">播放方式</th>
								<th class="sorting" abbr="fromEntrance">外部入口</th>
								<th class="sorting" abbr="type">类型</th>
								<th class="sorting" abbr="subscriptionTime">订购时间</th>
								<th class="sorting" abbr="unsubscribeTime">退订时间</th>
								<th class="sorting" abbr="paymentStatus">支付状态</th>
								<th class="sorting" abbr="validTime">生效时间</th>
								<th class="sorting" abbr="expiredTime">过期时间</th>
								<th class="sorting" abbr="clientIp">客户端IP</th>
								<th>操作</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="t" varStatus="status">
								<tr>
									<td><div class="checker">
											<span class=""><input type="checkbox"
												class="checkboxes" value="${t.id}" data-name="${t.appCode}" /></span>
										</div></td>
									<td>${status.index+1}</td>
									<td>${t.appCode}</td>
									<td>${t.partnerUserId}</td>
									<td><a href="javascript:;"
										onclick="$.OrderRecordController.detail('${t.id}');">${t.orderCode}</a></td>
									<td>${t.productCode}</td>
									<td>${t.contentCode}</td>
									<td><tags:enum value='${t.itemType}'
											enumList='${itemTypeEnum}' /></td>
									<td>${t.itemId}</td>
									<td>${t.itemName}</td>
									<td>${t.playMode}</td>
									<td>${t.fromEntrance}</td>
									<td><c:forEach var="item" items="${typeEnum}">
												<c:if test="${item.key eq t.type}">
													${item.value}
													</c:if>
											</c:forEach></td>
									<td><fmt:formatDate value="${t.subscriptionTime}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td><fmt:formatDate value="${t.unsubscribeTime}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td><c:if test="${t.paymentStatus eq 0 }">
											<span class="badge badge-info">待支付</span>
										</c:if> <c:if test="${t.paymentStatus eq 1 }">
											<span class="badge badge-success">支付成功</span>
										</c:if> <c:if test="${t.paymentStatus eq 2 }">
											<span class="badge badge-danger">支付失败</span>
										</c:if> <c:if test="${t.paymentStatus eq 3 }">
											<span class="badge badge-danger">支付超时</span>
										</c:if> <c:if test="${t.paymentStatus eq 4 }">
											<span class="badge badge-info">取消支付</span>
										</c:if></td>
									<td><fmt:formatDate value="${t.validTime}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td><fmt:formatDate value="${t.expiredTime}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>

									<td>${t.clientIp}</td>
									<td><c:if
											test="${t.paymentStatus eq 1 and empty t.unsubscribeTime }">
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.OrderRecordController.toUnsubscribe('${ctx}/orderRecord/orderRecord/${t.id}/unsubscribe','用户[${t.partnerUserId}]购买的产品[${t.productCode}]确定要退订吗？');">
												<i class="fa fa-pencil"></i>退订
											</button>
										</c:if></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<tags:pageInfo />
					<div class="modal-footer">
						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.OrderRecordController.batchExport('${ctx}/orderRecord/orderRecord/batchExport','1');">
							<i class="fa fa-cloud-download"></i>导出普通订购记录(已选择)[不包含测试，体验用户]
						</button>
						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.OrderRecordController.exportAll('${ctx}/orderRecord/orderRecord/exportAll','1');">
							<i class="fa fa-cloud-download"></i>导出普通订购记录(符合条件)[不包含测试，体验用户]
						</button>
						<button class="btn btn-default btn-sm btn-outline green"
							   onclick="$.OrderRecordController.toAdd('${ctx}/orderRecord/orderRecord/experienceAccount')">
								<i class="fa fa-cloud-download"></i>导入体验订单
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END CONTENT BODY -->

<tags:contentModal />

<div class="modal fade" id="content_list_modal_container" tabindex="-1"
	role="basic" aria-hidden="true">
	<div class="modal-dialog modal-lg" id="content_list_dialog_container">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title" id="content_list_modal_container_title">选择</h4>
			</div>
			<div class="modal-body" id="content_list_container"></div>
			<div class="modal-footer">
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>

<script>
$.OrderRecordController.init('');
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