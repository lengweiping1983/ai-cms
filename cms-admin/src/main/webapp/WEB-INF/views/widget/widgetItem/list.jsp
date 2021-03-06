<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<c:set var="containerId" value="" />
<c:set var="formId" value="" />
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>[${widget.name}]推荐位项管理
					</div>
					<div class="actions">
						<shiro:hasPermission name="widget:item:add">
							<shiro:hasPermission name="widget:item:batchEdit">
								<a href="javascript:;" class="btn btn-default btn-sm"
									onclick="$.WidgetItemController.toBatchAdd('${ctx}/widget/${widget.id}/widgetItem/batchAdd','${widgetItem.id}');">
									<i class="fa fa-plus"></i>批量增加推荐位项
								</a>
							</shiro:hasPermission>
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.WidgetItemController.toEdit('${ctx}/widget/${widget.id}/widgetItem/add','${widgetItem.id}')">
								<i class="fa fa-plus"></i>增加推荐位项
							</a>
						</shiro:hasPermission>
						<c:if test="${param.from == 'uri'}">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.UriController.ajaxLoadLastContent()"> <i
								class="fa fa-reply"></i> 返回
							</a>
						</c:if>
						<c:if test="${empty param.from}">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.common.ajaxLoadLastContent()"> <i
								class="fa fa-reply"></i> 返回到推荐位管理
							</a>
						</c:if>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/widget/${widget.id}/widgetItem/">
								<input type="hidden" name="from" value="${param.from}">
								<input type="hidden" name="search_widgetId__EQ_L"
									value="${param.search_widgetId__EQ_L}" />
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
											<label class="control-label">定制名称: <input type="text"
												name="search_title__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_title__LIKE_S}">
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
								<th class="sorting" abbr="title">定制名称</th>
								<th class="sorting" abbr="status">上线状态</th>
								<th class="sorting" abbr="validTime">生效时间</th>
								<th class="sorting" abbr="expiredTime">失效时间</th>
								<th class="sorting" abbr="groupCodes">用户分组</th>
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
								<th class="sorting" abbr="title">定制名称</th>
								<th class="sorting" abbr="status">上线状态</th>
								<th class="sorting" abbr="validTime">生效时间</th>
								<th class="sorting" abbr="expiredTime">失效时间</th>
								<th class="sorting" abbr="groupCodes">用户分组</th>
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
										onclick="$.WidgetItemController.detail('${t.itemType}','${t.itemId}');">${t.itemId}</a></td>
									<td><a href="javascript:;"
										onclick="$.WidgetItemController.detail('${t.itemType}','${t.itemId}');">${t.itemName}</a></td>
									<td><a href="javascript:;"
										onclick="$.WidgetItemController.detail('${t.itemType}','${t.itemId}');">${t.itemTitle}</a></td>
									<td>${t.tag}</td>
									<td><tags:status value='${t.itemStatus}' /></td>
									<td>${t.title}</td>
									<td><tags:status value='${t.status}' /></td>
									<td><fmt:formatDate value="${t.validTime}"
											pattern="yyyy-MM-dd HH:mm" /></td>
									<td><fmt:formatDate value="${t.expiredTime}"
											pattern="yyyy-MM-dd HH:mm" /></td>		
									<td>${t.groupCodes}</td>
									<td>${t.sortIndex}</td>
									<td><c:set var="statusMethodName"
											value="${fns:getStatusMothodName(t.status)}" />
										<shiro:hasPermission name="widget:item:status">
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.WidgetItemController.toChangeStatus('${ctx}/widget/${widget.id}/widgetItem/${t.id}/status','${t.itemName}','${statusMethodName}');">
												<i class="fa fa-pencil"></i>${statusMethodName}
											</button>
										</shiro:hasPermission>
										<shiro:hasPermission name="widget:item:edit">
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.WidgetItemController.toEdit('${ctx}/widget/${widget.id}/widgetItem/${t.id}/edit',${t.id});">
												<i class="fa fa-edit"></i>修改
											</button>
										</shiro:hasPermission>
										<shiro:hasPermission name="widget:item:delete">
										<tags:deleteByStatus value='${t.status}'
											onclick="$.WidgetItemController.toDelete('${ctx}/widget/${widget.id}/widgetItem/${t.id}/delete','${t.itemName}');" />
										</shiro:hasPermission>
										<shiro:hasPermission name="widget:item:layout">
										<c:choose>
											<c:when test="${t.jumpItemType eq 6 && !empty t.jumpItemId}">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.UriController.listByPosition('${ctx}/uri/uri/${t.jumpItemId}/listByPosition?internalParam=${t.internalParam}');">
													<i class="fa fa-gear"></i>页面数据编排
												</button>
											</c:when>
											<c:otherwise>
												<c:if test="${t.itemType eq 6 && !empty t.itemId}">
													<button class="btn btn-default btn-sm btn-outline green"
														onclick="$.UriController.listByPosition('${ctx}/uri/uri/${t.itemId}/listByPosition?internalParam=${t.internalParam}');">
														<i class="fa fa-gear"></i>页面数据编排
													</button>
												</c:if>
											</c:otherwise>
										</c:choose>
										</shiro:hasPermission>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<tags:pageInfo />

					<shiro:hasPermission name="widget:item:batchEdit">
						<div class="modal-footer">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.BaseController.batchTo('${ctx}/widget/${widget.id}/widgetItem/batchChangeTitle', 1);">
								<i class="fa fa-edit"></i>批量修改
							</button>

							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.BaseController.toBatchOnline('${ctx}/widget/${widget.id}/widgetItem/batchChangeStatus', 1);">
								<i class="fa fa-edit"></i>批量上线
							</button>

							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.BaseController.toBatchOffline('${ctx}/widget/${widget.id}/widgetItem/batchChangeStatus', 1);">
								<i class="fa fa-edit"></i>批量下线
							</button>

							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.BaseController.toBatchDelete('${ctx}/widget/${widget.id}/widgetItem/batchDelete', 1);">
								<i class="fa fa-remove"></i>批量删除
							</button>
						</div>
					</shiro:hasPermission>
				</div>
			</div>
		</div>
	</div>
</div>

<tags:contentModal />

<script>
	$.BaseController.initTableCheckBox();
	$.ProgramController.keypress({containerId: '${containerId}', formId: '${formId}'});
</script>

