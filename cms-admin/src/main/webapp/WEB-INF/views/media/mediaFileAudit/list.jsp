<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<c:set var="containerId" value="" />
<c:set var="formId" value="" />
<c:set var="contextPathPrefix" value="${ctx}/media/mediaFileAudit/" />
<c:set var="permissionPrefix" value="media:mediaFileAudit:" />
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>[${program.name}]媒体内容管理
					</div>
					<div class="actions">
						<shiro:hasPermission name="${permissionPrefix}add">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.MediaFileController.toEdit('${contextPathPrefix}add?programId=${program.id}')">
								<i class="fa fa-plus"></i>增加媒体内容
							</a>
						</shiro:hasPermission>
						<c:if test="${param.from eq 'program'}">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.ProgramController.ajaxLoadLastContent()"> <i
								class="fa fa-reply"></i> 返回
							</a>
						</c:if>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/media/mediaFile?programId=${program.id}">
								<input type="hidden" name="from" value="${param.from}" /> <input
									type="hidden" name="search_seriesId__EQ_L"
									value="${param.search_seriesId__EQ_L}" /> <input type="hidden"
									name="search_programId__EQ_L"
									value="${param.search_programId__EQ_L}" /> <input
									type="hidden" name="selectMode" value="${param.selectMode}">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">&#12288;&#12288;&#12288;ID:
												<input type="text" name="search_id__EQ_L"
												class="form-control input-small input-inline"
												value="${param.search_id__EQ_L}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">&#12288;&#12288;码率: <select
												name="search_templateId__EQ_L"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="mediaTemplate" items="${mediaTemplateList}">
														<option value="${mediaTemplate.id}"
															<c:if test="${! empty param.search_templateId__EQ_L && mediaTemplate.id eq param.search_templateId__EQ_L}">selected="selected"</c:if>>${mediaTemplate.code}</option>
													</c:forEach>
											</select>
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">文件路径: <input type="text"
												name="search_filePath__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_filePath__LIKE_S}">
											</label>
											<tags:searchButton containerId="${containerId}"
												formId="${formId}" toggle="true" />
										</div>
									</div>
								</div>
								<div id="${formId}condition_open_panel"
									<c:if test="${param.condition_open_status != 1}"> style="display: none" </c:if>>
									<div class="row">
										<div class="col-md-12">
											<div class="col-md-4">
											</div>
											<div class="col-md-4">
											</div>
											<div class="col-md-4">
												<label class="control-label">媒资状态: <select
													name="search_mediaStatus__BITMASK_I"
													class="form-control input-small input-inline">
														<option value="">全部</option>
														<c:forEach var="item" items="${mediaStatusEnum}">
															<option value="${item.key}"
																<c:if test="${! empty param.search_mediaStatus__BITMASK_I && item.key eq param.search_mediaStatus__BITMASK_I}"> selected="selected" </c:if>>${item.value}</option>
														</c:forEach>
												</select>
												</label>
											</div>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
					<tags:pageInfo containerId="${containerId}" formId="${formId}"
						levelId="up" />
					<table
						class="table dataTable table-striped table-bordered table-hover table-checkable"
						id="${formId}content_list">
						<thead>
							<tr>
								<th><input type="checkbox" class="group-checkable"
									data-set="#${formId}content_list .checkboxes" /></th>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="type">类型</th>
								<th class="sorting" abbr="templateId">码率</th>
								<th class="sorting" abbr="filePath">文件路径</th>
								<th class="sorting" abbr="fileSize">文件大小</th>
								<th class="sorting" abbr="mediaStatus">媒资状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th><input type="checkbox" class="group-checkable"
									data-set="#${formId}content_list .checkboxes" /></th>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="type">类型</th>
								<th class="sorting" abbr="templateId">码率</th>
								<th class="sorting" abbr="filePath">文件路径</th>
								<th class="sorting" abbr="fileSize">文件大小</th>
								<th class="sorting" abbr="mediaStatus">媒资状态</th>
								<th>操作</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="t" varStatus="status">
								<tr>
									<td><input type="checkbox" class="checkboxes"
										value="${t.id}" data-name="${t.id}" /></td>
									<td>${status.index+1}</td>
									<td><a href="javascript:;"
										onclick="$.MediaFileController.detail('${t.id}');">${t.id}</a></td>
									<td><tags:enum enumList="${typeEnum}" value="${t.type}" /></td>
									<td><c:if test="${! empty t.templateId}">
											<c:forEach var="mediaTemplate" items="${mediaTemplateList}">
												<c:if test="${mediaTemplate.id eq t.templateId}">
													<span class="badge badge-success">${mediaTemplate.code}</span>
												</c:if>
											</c:forEach>
										</c:if></td>
									<td>${t.filePath}</td>
									<td>${t.fileSize}</td>
									<td>${fns:getMediaStatusDesc(t.mediaStatus)}</td>
									<td><c:if test="${t.status eq 0 || t.status eq 2}">
											<shiro:hasPermission name="${permissionPrefix}online">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.MediaFileController.toOnline('${contextPathPrefix}${t.id}/online','id为${t.id}的媒体内容');">
													<i class="fa fa-pencil"></i>上线
												</button>
											</shiro:hasPermission>
										</c:if> <c:if test="${t.status eq 1}">
											<shiro:hasPermission name="${permissionPrefix}offline">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.MediaFileController.toOffline('${contextPathPrefix}${t.id}/offline','id为${t.id}的媒体内容');">
													<i class="fa fa-pencil"></i>下线
												</button>
											</shiro:hasPermission>
										</c:if> <shiro:hasPermission name="${permissionPrefix}edit">
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.MediaFileController.toEdit('${contextPathPrefix}${t.id}/edit',${t.id});">
												<i class="fa fa-edit"></i>修改
											</button>
										</shiro:hasPermission> <shiro:hasPermission name="${permissionPrefix}delete">
											<tags:deleteByStatus value='${t.status}'
												onclick="$.MediaFileController.toDelete('${contextPathPrefix}${t.id}/delete','id为${t.id}的媒体内容');" />
										</shiro:hasPermission>
										<button class="btn btn-default btn-sm btn-outline green"
											<c:if test="${empty t.filePath}"> disabled </c:if>
											onclick="$.PlayController.toPreviewNewWindow('${t.filePath}'); return false;">
											<i class="fa fa-eye"></i>预览
										</button></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

					<tags:pageInfo containerId="${containerId}" formId="${formId}" />

					<shiro:hasAnyPermissions
						name="${permissionPrefix}batchOnline,${permissionPrefix}batchOffline,${permissionPrefix}batchDelete">
						<div class="modal-footer">
							<shiro:hasPermission name="${permissionPrefix}batchOnline">
								<button class="btn btn-default btn-sm btn-outline green"
									onclick="$.MediaFileController.toBatchOnline('${contextPathPrefix}batchOnline', 101);">
									<i class="fa fa-edit"></i>批量上线
								</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="${permissionPrefix}batchOffline">
								<button class="btn btn-default btn-sm btn-outline green"
									onclick="$.MediaFileController.toBatchOffline('${contextPathPrefix}batchOffline', 101);">
									<i class="fa fa-edit"></i>批量下线
								</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="${permissionPrefix}batchDelete">
								<button class="btn btn-default btn-sm btn-outline green"
									onclick="$.MediaFileController.toBatchDelete('${contextPathPrefix}batchDelete', 101);">
									<i class="fa fa-remove"></i>批量删除
								</button>
							</shiro:hasPermission>
						</div>
					</shiro:hasAnyPermissions>
				</div>
			</div>
		</div>
	</div>
</div>

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
			<div class="modal-footer" id="content_list_modal_container_footer">
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

<script type="text/javascript">
	$.MediaFileController.init('${formId}');
	$.MediaFileController.keypress({containerId: '${containerId}', formId: '${formId}'});
</script>
