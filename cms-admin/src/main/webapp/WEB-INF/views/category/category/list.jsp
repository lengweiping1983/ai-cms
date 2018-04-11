<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="portlet box green">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-globe"></i>
			<c:if test="${!empty parent}">[${parent.name}]</c:if>
			栏目管理
		</div>
		<div class="actions">
			<c:if test="${(!empty parent && parent.type ne 1) || empty parent}">
				<shiro:hasPermission name="category:category:add">
					<a href="javascript:;" class="btn btn-default btn-sm"
						onclick="$.CategoryController.toEdit('${ctx}/category/category/add?parentId=${param.search_parentId__EQ_L}');">
						<i class="fa fa-plus"></i>增加栏目
					</a>
				</shiro:hasPermission>
			</c:if>
			<c:if test="${param.from == 'uri'}">
				<a href="javascript:;" class="btn btn-default btn-sm"
					onclick="$.UriController.ajaxLoadLastContent()"> <i
					class="fa fa-reply"></i> 返回
				</a>
			</c:if>
		</div>
	</div>
	<div class="portlet-body">
		<div class="table-container">
			<div class="table-actions-wrapper-condition">
				<form action="${ctx}/category/category/">
					<input type="hidden" name="from" value="${param.from}">
					<input type="hidden" id="search_parentId__EQ_L"
						name="search_parentId__EQ_L"
						value="${param.search_parentId__EQ_L}">
				</form>
			</div>
		</div>
		<table
			class="table dataTable table-striped table-bordered table-hover table-checkable"
			id="content_list">
			<thead>
				<tr>
					<th><div class="checker">
							<span class=""><input type="checkbox"
								class="group-checkable" data-set="#content_list .checkboxes" /></span>
						</div></th>
					<th>序号</th>
					<th class="sorting" abbr="id">ID</th>
					<th class="sorting" abbr="name">栏目名称</th>
					<th class="sorting" abbr="code">栏目代码</th>
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
					<th><div class="checker">
							<span class=""><input type="checkbox"
								class="group-checkable" data-set="#content_list .checkboxes" /></span>
						</div></th>
					<th>序号</th>
					<th class="sorting" abbr="id">ID</th>
					<th class="sorting" abbr="name">栏目名称</th>
					<th class="sorting" abbr="code">栏目代码</th>
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
						<td><div class="checker">
								<span class=""><input type="checkbox" class="checkboxes"
									value="${t.id}" data-name="${t.name}" /></span>
							</div></td>
						<td>${status.index+1}</td>
						<td>${t.id}</td>
						<td>${t.name}</td>
						<td>${t.code}</td>
						<td><tags:status value='${t.status}' /></td>
						<c:if test="${fns:isInjectionCategory()}">
						<td><tags:injectionStatus value='${t.injectionStatus}' /></td>
						</c:if>
						<td>${t.sortIndex}</td>
						<td><c:set var="statusMethodName"
								value="${fns:getStatusMothodName(t.status)}" />
							<shiro:hasPermission name="category:category:status">
								<button class="btn btn-default btn-sm btn-outline green"
									onclick="$.CategoryController.toChangeStatus('${ctx}/category/category/${t.id}/status','${t.name}','${statusMethodName}');">
									<i class="fa fa-pencil"></i>${statusMethodName}
								</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="category:category:edit">
								<button class="btn btn-default btn-sm btn-outline green"
									onclick="$.CategoryController.toEdit('${ctx}/category/category/${t.id}/edit',${t.id});">
									<i class="fa fa-edit"></i>修改
								</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="category:category:delete">
								<tags:deleteByStatus value='${t.status}'
									onclick="$.CategoryController.toDelete('${ctx}/category/category/${t.id}/delete','${t.name}');" />
							</shiro:hasPermission>
							<c:if test="${t.type eq 1}">
								<shiro:hasPermission name="category:category:showDetail">
									<button class="btn btn-default btn-sm btn-outline green"
										onclick="$.CategoryController.showDetail(${t.id});">
										<i class="fa fa-gear"></i>管理栏目项
									</button>
								</shiro:hasPermission>
							</c:if>
							</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<tags:pageInfo containerId="categoryPageInfo" />

		<c:if test="${fns:isInjectionCategory()}">
			<shiro:hasPermission name="category:category:batchEdit">
				<div class="modal-footer">
					<button class="btn btn-default btn-sm btn-outline green"
						onclick="$.CategoryController.batchTo('${ctx}/category/category/batchInjection','5');">
						<i class="fa fa-edit"></i>批量分发
					</button>
					<button class="btn btn-default btn-sm btn-outline green"
						onclick="$.CategoryController.batchTo('${ctx}/category/category/batchOutInjection','5');">
						<i class="fa fa-edit"></i>批量回收
					</button>
					<button class="btn btn-default btn-sm btn-outline green"
						onclick="$.CategoryController.toResetInjectionStatus('${ctx}/category/category/resetInjectionStatus','5');">
						<i class="fa fa-edit"></i>重置分发状态
					</button>
				</div>
			</shiro:hasPermission>
		</c:if>
	</div>
</div>

<tags:contentModal />

<div class="modal fade bs-modal-sm" id="category_tree_modal_div"
	tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">选择所属栏目</h4>
			</div>
			<div class="modal-body">
				<div id="category_tree_div" class="tree-demo"></div>
			</div>
			<div class="modal-footer">
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$.CategoryController.init();
</script>
