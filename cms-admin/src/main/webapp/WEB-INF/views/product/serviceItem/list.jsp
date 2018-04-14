<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>[${service.name}]服务项管理
					</div>
					<div class="actions">
						<a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.ServiceItemController.toEdit('${ctx}/service/${service.id}/serviceItem/add','${serviceItem.id}')">
							<i class="fa fa-plus"></i>增加服务项
						</a> <a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.common.ajaxLoadLastContent()"> <i
							class="fa fa-reply"></i> 返回到服务管理
						</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/service/${service.id}/serviceItem/">
								<input type="hidden" name="search_serviceId__EQ_L"
									value="${param.search_serviceId__EQ_L}" />
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">元素类型: <select
												name="search_itemType__EQ_I"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${typeEnum}">
														<option value="${item.key}"
															<c:if test="${! empty param.search_itemType__EQ_I && item.key eq param.search_itemType__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
											</select>
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
											<label class="control-label">显示名称: <input type="text"
												name="search_itemTitle__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_itemTitle__LIKE_S}">
											</label>
										</div>
									</div>

									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">元素状态: <select
												name="search_itemStatus__EQ_I"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${statusEnum}">
														<option value="${item.key}"
															<c:if test="${! empty param.search_itemStatus__EQ_I && item.key eq param.search_itemStatus__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
											</select>
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">上线状态: <select
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
											<label class="control-label">分发状态: <select
												name="search_injectionStatus__EQ_I"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${injectionStatusEnum}">
														<option value="${item.key}"
															<c:if test="${! empty param.search_injectionStatus__EQ_I && item.key eq param.search_injectionStatus__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
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
								<th class="sorting" abbr="itemType">元素类型</th>
								<th class="sorting" abbr="itemId">元素id</th>
								<th class="sorting" abbr="itemName">元素名称</th>
								<th class="sorting" abbr="itemTitle">显示名称</th>
								<th class="sorting" abbr="tag">TAG</th>
								<th class="sorting" abbr="itemStatus">元素状态</th>
								<th class="sorting" abbr="status">上线状态</th>
								<th class="sorting" abbr="validTime">生效时间</th>
								<th class="sorting" abbr="expiredTime">失效时间</th>
								<c:if test="${fns:isInjectionService()}">
									<th class="sorting" abbr="injectionStatus">分发状态</th>
								</c:if>
								<th class="sorting" abbr="sortIndex">排序值</th>
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
								<th class="sorting" abbr="itemType">元素类型</th>
								<th class="sorting" abbr="itemId">元素id</th>
								<th class="sorting" abbr="itemName">元素名称</th>
								<th class="sorting" abbr="itemTitle">显示名称</th>
								<th class="sorting" abbr="tag">TAG</th>
								<th class="sorting" abbr="itemStatus">元素状态</th>
								<th class="sorting" abbr="status">上线状态</th>
								<th class="sorting" abbr="validTime">生效时间</th>
								<th class="sorting" abbr="expiredTime">失效时间</th>
								<c:if test="${fns:isInjectionService()}">
									<th class="sorting" abbr="injectionStatus">分发状态</th>
								</c:if>
								<th class="sorting" abbr="sortIndex">排序值</th>
								<th>操作</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="t" varStatus="status">
								<tr>
									<td>
										<div class="checker">
											<span class=""><input type="checkbox"
												class="checkboxes" value="${t.id}" data-name="${t.itemName}" /></span>
										</div>
									</td>
									<td>${status.index+1}</td>
									<td><tags:type value='${t.itemType}' /></td>
									<td><a href="javascript:;"
										onclick="$.ServiceItemController.detail('${t.itemType}','${t.itemId}');">${t.itemId}</a></td>
									<td><a href="javascript:;"
										onclick="$.ServiceItemController.detail('${t.itemType}','${t.itemId}');">${t.itemName}</a></td>
									<td><a href="javascript:;"
										onclick="$.ServiceItemController.detail('${t.itemType}','${t.itemId}');">${t.itemTitle}</a></td>
									<td>${t.tag}</td>
									<td><tags:status value='${t.itemStatus}' /></td>
									<td><tags:status value='${t.status}' /></td>
									<td><fmt:formatDate value="${t.validTime}"
											pattern="yyyy-MM-dd HH:mm" /></td>
									<td><fmt:formatDate value="${t.expiredTime}"
											pattern="yyyy-MM-dd HH:mm" /></td>
									<c:if test="${fns:isInjectionService()}">
										<td><tags:injectionStatus value='${t.injectionStatus}' /></td>
									</c:if>
									<td>${t.sortIndex}</td>
									<td><c:set var="statusMethodName"
											value="${fns:getStatusMothodName(t.status)}" />
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.ServiceItemController.toChangeStatus('${ctx}/service/${service.id}/serviceItem/${t.id}/status','${t.itemName}','${statusMethodName}');">
											<i class="fa fa-pencil"></i>${statusMethodName}
										</button>
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.ServiceItemController.toEdit('${ctx}/service/${service.id}/serviceItem/${t.id}/edit',${t.id});">
											<i class="fa fa-edit"></i>修改
										</button> <tags:deleteByStatus value='${t.status}'
											onclick="$.ServiceItemController.toDelete('${ctx}/service/${service.id}/serviceItem/${t.id}/delete','${t.itemName}');" />
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<tags:pageInfo />
					<div class="modal-footer">
						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.BaseController.toBatchOnline('${ctx}/service/${service.id}/serviceItem/batchChangeStatus', 1);">
							<i class="fa fa-edit"></i>批量上线
						</button>

						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.BaseController.toBatchOffline('${ctx}/service/${service.id}/serviceItem/batchChangeStatus', 1);">
							<i class="fa fa-edit"></i>批量下线
						</button>

						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.BaseController.toBatchDelete('${ctx}/service/${service.id}/serviceItem/batchDelete', 1);">
							<i class="fa fa-remove"></i>批量删除
						</button>

						<c:if test="${fns:isInjectionService()}">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.ServiceItemController.batchTo('${ctx}/service/${service.id}/serviceItem/batchInjection','99');">
								<i class="fa fa-edit"></i>批量分发
							</button>
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.ServiceItemController.batchTo('${ctx}/service/${service.id}/serviceItem/batchOutInjection','99');">
								<i class="fa fa-edit"></i>批量回收
							</button>
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.ServiceItemController.toResetInjectionStatus('${ctx}/service/${service.id}/serviceItem/resetInjectionStatus','99');">
								<i class="fa fa-edit"></i>重置分发状态
							</button>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END CONTENT BODY -->

<tags:contentModal />

<script>
	$.BaseController.init();
    $(".table-actions-wrapper-condition").keypress(function (e) {
        var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
        if (keyCode == 13) {
        	$.Page.queryByForm();
        	return false;
        }
    });
</script>
