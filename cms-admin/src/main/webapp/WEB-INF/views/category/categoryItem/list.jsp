<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<!-- <div class="page-content"> -->
<!-- 	<div class="row"> -->
<!-- 		<div class="col-md-12"> -->
<div class="portlet box green">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-globe"></i>[${category.name}]栏目项管理
		</div>
		<div class="actions">
			<shiro:hasPermission name="category:item:add">
				<shiro:hasPermission name="category:item:batchEdit">
					<a href="javascript:;" class="btn btn-default btn-sm"
						onclick="$.CategoryItemController.toBatchAdd('${ctx}/category/${category.id}/categoryItem/batchAdd','${categoryItem.id}')">
						<i class="fa fa-plus"></i>批量增加栏目项
					</a>
				</shiro:hasPermission>
					<a href="javascript:;" class="btn btn-default btn-sm"
						onclick="$.CategoryItemController.toEdit('${ctx}/category/${category.id}/categoryItem/add','${categoryItem.id}')">
						<i class="fa fa-plus"></i>增加栏目项
					</a>

			</shiro:hasPermission>
			<a href="javascript:;" class="btn btn-default btn-sm"
				onclick="$.common.ajaxLoadLastContent({containerId: 'categoryPageInfo'})">
				<i class="fa fa-reply"></i> 返回到栏目管理
			</a>
		</div>
	</div>
	<div class="portlet-body">
		<div class="table-container">
			<div class="table-actions-wrapper-condition">
				<form action="${ctx}/category/${category.id}/categoryItem/"
					id="categoryItem">
					<input type="hidden" name="search_categoryId__EQ_L"
						value="${param.search_categoryId__EQ_L}" />
					<div class="row">
						<div class="col-md-12">
							<div class="col-md-6">
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
							<div class="col-md-6">
								<label class="control-label">元素名称: <input type="text"
									name="search_itemName__LIKE_S"
									class="form-control input-small input-inline"
									value="${param.search_itemName__LIKE_S}">
								</label>
							</div>

						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="col-md-6">
								<label class="control-label">显示名称: <input type="text"
									name="search_itemTitle__LIKE_S"
									class="form-control input-small input-inline"
									value="${param.search_itemTitle__LIKE_S}">
								</label>
							</div>
							<div class="col-md-6">
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
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="col-md-6">
								<label class="control-label">定制名称: <input type="text"
									name="search_title__LIKE_S"
									class="form-control input-small input-inline"
									value="${param.search_title__LIKE_S}">
								</label>
							</div>
							<div class="col-md-6">
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
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="col-md-6">
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
							</div>
							<div class="col-md-6">
								<div style="float: right">
									<button class="btn btn-default btn-sm btn-outline green"
										type="button"
										onclick="$.Page.queryByForm({containerId: 'categoryPageInfo', formId: 'categoryItem'});">
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
					<c:if test="${fns:isInjectionCategory()}">
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
					<th class="sorting" abbr="title">定制名称</th>
					<th class="sorting" abbr="status">上线状态</th>
					<c:if test="${fns:isInjectionCategory()}">
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
								<span class=""><input type="checkbox" class="checkboxes"
									value="${t.id}" data-name="${t.itemName}" /></span>
							</div>
						</td>
						<td>${status.index+1}</td>
						<td><tags:type value='${t.itemType}' /></td>
						<td><a href="javascript:;"
							onclick="$.CategoryItemController.detail('${t.itemType}','${t.itemId}');">${t.itemId}</a></td>
						<td><a href="javascript:;"
							onclick="$.CategoryItemController.detail('${t.itemType}','${t.itemId}');">${t.itemName}</a></td>
						<td><a href="javascript:;"
							onclick="$.CategoryItemController.detail('${t.itemType}','${t.itemId}');">${t.itemTitle}</a></td>
						<td>${t.tag}</td>
						<td><tags:status value='${t.itemStatus}' /></td>
						<td>${t.title}</td>
						<td><tags:status value='${t.status}' /></td>
						<c:if test="${fns:isInjectionCategory()}">
						<td><tags:injectionStatus value='${t.injectionStatus}' /></td>
						</c:if>
						<td>${t.sortIndex}</td>
						<td><c:set var="statusMethodName"
								value="${fns:getStatusMothodName(t.status)}" />
							<shiro:hasPermission name="category:item:status">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.CategoryItemController.toChangeStatus('${ctx}/category/${category.id}/categoryItem/${t.id}/status','${t.itemName}','${statusMethodName}');">
								<i class="fa fa-pencil"></i>${statusMethodName}
							</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="category:item:edit">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.CategoryItemController.toEdit('${ctx}/category/${category.id}/categoryItem/${t.id}/edit',${t.id});">
								<i class="fa fa-edit"></i>修改
							</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="category:item:delete">
							<tags:deleteByStatus value='${t.status}'
								onclick="$.CategoryItemController.toDelete('${ctx}/category/${category.id}/categoryItem/${t.id}/delete','${t.itemName}');" />
							</shiro:hasPermission>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<tags:pageInfo containerId="categoryPageInfo" formId="categoryItem" />
		<shiro:hasPermission name="category:item:batchEdit">
			<div class="modal-footer">
			<button class="btn btn-default btn-sm btn-outline green"
				onclick="$.BaseController.batchTo('${ctx}/category/${category.id}/categoryItem/batchChangeTitle', 1);">
				<i class="fa fa-edit"></i>批量修改
			</button>

			<button class="btn btn-default btn-sm btn-outline green"
				onclick="$.CategoryItemController.toBatchOnline('${ctx}/category/${category.id}/categoryItem/batchChangeStatus', 1);">
				<i class="fa fa-edit"></i>批量上线
			</button>

			<button class="btn btn-default btn-sm btn-outline green"
				onclick="$.CategoryItemController.toBatchOffline('${ctx}/category/${category.id}/categoryItem/batchChangeStatus', 1);">
				<i class="fa fa-edit"></i>批量下线
			</button>

			<button class="btn btn-default btn-sm btn-outline green"
				onclick="$.CategoryItemController.toBatchDelete('${ctx}/category/${category.id}/categoryItem/batchDelete', 1);">
				<i class="fa fa-remove"></i>批量删除
			</button>

			<c:if test="${fns:isInjectionCategory()}">
				<button class="btn btn-default btn-sm btn-outline green"
					onclick="$.CategoryItemController.batchTo('${ctx}/category/${category.id}/categoryItem/batchInjection','99');">
					<i class="fa fa-edit"></i>批量分发
				</button>
				<button class="btn btn-default btn-sm btn-outline green"
					onclick="$.CategoryItemController.batchTo('${ctx}/category/${category.id}/categoryItem/batchOutInjection','99');">
					<i class="fa fa-edit"></i>批量回收
				</button>
				<button class="btn btn-default btn-sm btn-outline green"
					onclick="$.CategoryItemController.toResetInjectionStatus('${ctx}/category/${category.id}/categoryItem/resetInjectionStatus','99');">
					<i class="fa fa-edit"></i>重置分发状态
				</button>
			</c:if>
		</div>
		</shiro:hasPermission>
	</div>
</div>
<!-- 		</div> -->
<!-- 	</div> -->
<!-- </div> -->
<!-- END CONTENT BODY -->

<tags:contentModal />

<div class="modal fade" id="content_list_modal_container" tabindex="-1"
	role="basic" aria-hidden="true">
	<div class="modal-dialog modal-full" id="content_list_dialog_container">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title" id="content_list_modal_container_title">选择</h4>
			</div>
			<div class="modal-body" id="content_list_container"></div>
			<div class="modal-footer">
				<button type="button" class="btn btn-outline green"
					style="display: none" id="content_list_modal_container_ok">
					<i class="fa fa-check"></i>确定
				</button>
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>

<script>
	$.CategoryItemController.init();
    $(".table-actions-wrapper-condition").keypress(function (e) {
        var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
        if (keyCode == 13) {
        	$.Page.queryByForm({containerId: 'categoryPageInfo', formId: 'categoryItem'});
        	return false;
        }
    });
</script>

