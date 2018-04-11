<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>[${album.name}]专题项管理
					</div>
					<div class="actions">
						<shiro:hasPermission name="album:item:add">
							<shiro:hasPermission name="album:item:batchEdit">
								<a href="javascript:;" class="btn btn-default btn-sm"
									onclick="$.AlbumItemController.toBatchAdd('${ctx}/album/${album.id}/albumItem/batchAdd','${albumItem.id}')">
									<i class="fa fa-plus"></i>批量增加专题项
								</a>
							</shiro:hasPermission>
								<a href="javascript:;" class="btn btn-default btn-sm"
									onclick="$.AlbumItemController.toEdit('${ctx}/album/${album.id}/albumItem/add','${albumItem.id}')">
									<i class="fa fa-plus"></i>增加专题项
								</a>
						</shiro:hasPermission>
							<a href="javascript:;" class="btn btn-default btn-sm"
									onclick="$.common.ajaxLoadLastContent()"> <i
									class="fa fa-reply"></i> 返回到专题管理
								</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/album/${album.id}/albumItem/">
								<input type="hidden" name="search_albumId__EQ_L"
									value="${param.search_albumId__EQ_L}" />
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
										onclick="
											<shiro:hasPermission name="album:item:view">
												$.AlbumItemController.detail('${t.itemType}','${t.itemId}');
											</shiro:hasPermission>
												">${t.itemId}</a></td>
									<td><a href="javascript:;"
										onclick="
											<shiro:hasPermission name="album:item:view">
												$.AlbumItemController.detail('${t.itemType}','${t.itemId}');
											</shiro:hasPermission>
												">${t.itemName}</a></td>
									<td><a href="javascript:;"
										onclick="
											<shiro:hasPermission name="album:item:view">
												$.AlbumItemController.detail('${t.itemType}','${t.itemId}');
											</shiro:hasPermission>
												">${t.itemTitle}</a></td>
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
										<shiro:hasPermission name="album:item:status">
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.AlbumItemController.toChangeStatus('${ctx}/album/${album.id}/albumItem/${t.id}/status','${t.itemName}','${statusMethodName}');">
											<i class="fa fa-pencil"></i>${statusMethodName}
										</button>
										</shiro:hasPermission>
										<shiro:hasPermission name="album:item:edit">
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.AlbumItemController.toEdit('${ctx}/album/${album.id}/albumItem/${t.id}/edit',${t.id});">
											<i class="fa fa-edit"></i>修改
										</button>
										</shiro:hasPermission>
										<shiro:hasPermission name="album:item:delete">
										<tags:deleteByStatus value='${t.status}'
											onclick="$.AlbumItemController.toDelete('${ctx}/album/${album.id}/albumItem/${t.id}/delete','${t.itemName}');" />
										</shiro:hasPermission>
										<shiro:hasPermission name="album:item:layout">
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

					<shiro:hasPermission name="album:item:status">
						<div class="modal-footer">
						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.BaseController.batchTo('${ctx}/album/${album.id}/albumItem/batchChangeTitle', 1);">
							<i class="fa fa-edit"></i>批量修改
						</button>
						
						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.BaseController.toBatchOnline('${ctx}/album/${album.id}/albumItem/batchChangeStatus', 1);">
							<i class="fa fa-edit"></i>批量上线
						</button>

						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.BaseController.toBatchOffline('${ctx}/album/${album.id}/albumItem/batchChangeStatus', 1);">
							<i class="fa fa-edit"></i>批量下线
						</button>

						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.BaseController.toBatchDelete('${ctx}/album/${album.id}/albumItem/batchDelete', 1);">
							<i class="fa fa-remove"></i>批量删除
						</button>
					</div>
					</shiro:hasPermission>
				</div>
			</div>
		</div>
	</div>
</div>
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
	$.BaseController.init();
    $(".table-actions-wrapper-condition").keypress(function (e) {
        var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
        if (keyCode == 13) {
        	$.Page.queryByForm();
        	return false;
        }
    });
</script>
