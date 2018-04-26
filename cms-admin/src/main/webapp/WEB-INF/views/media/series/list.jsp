<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<c:set var="containerId" value="" />
<c:set var="formId" value="" />
<c:set var="contextPathPrefix" value="${ctx}/media/series/" />
<c:set var="permissionPrefix" value="media:series:" />
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>剧头管理
					</div>
					<div class="actions">
						<shiro:hasPermission name="${permissionPrefix}add">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.SeriesController.toEdit('${contextPathPrefix}add')">
								<i class="fa fa-plus"></i>增加剧头
							</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${contextPathPrefix}">
								<input type="hidden" name="search_auditStatus__IN_S"
									value="${param.search_auditStatus__IN_S}"> <input
									type="hidden" name="selectMode" value="${param.selectMode}">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">剧头类型: <select
												name="search_type__EQ_I"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${typeEnum}">
														<option value="${item.key}"
															<c:if test="${! empty param.search_type__EQ_I && item.key eq param.search_type__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
											</select>
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">剧头名称: <input type="text"
												name="search_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_name__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">&#12288;&#12288;标题: <input
												type="text" name="search_title__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_title__LIKE_S}">
											</label>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">内容类型: <select
												name="search_contentType__EQ_I"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${contentTypeEnum}">
														<option value="${item.key}"
															<c:if test="${! empty param.search_contentType__EQ_I && item.key eq param.search_contentType__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
											</select>
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">&#12288;&#12288;TAG: <input
												type="text" name="search_tag__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_tag__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">内部标签: <input type="text"
												name="search_internalTag__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_internalTag__LIKE_S}">
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
												<tags:cpSearch prefix="${formId}" />
											</div>
											<div class="col-md-4">
												<tags:mediaTemplateSearch />
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
									<div class="row">
										<div class="col-md-12">
											<div class="col-md-4">
												<label class="control-label">分发状态: <select
													name="search_injectionStatus__INMASK_S"
													class="form-control input-small input-inline">
														<option value="">全部</option>
														<c:forEach var="item" items="${injectionStatusEnum}">
															<option value="${item.key}"
																<c:if test="${! empty param.search_injectionStatus__INMASK_S && item.key eq param.search_injectionStatus__INMASK_S}"> selected="selected" </c:if>>${item.value}</option>
														</c:forEach>
												</select>
												</label>
											</div>
											<div class="col-md-4">
												<label class="control-label">播放代码: <select
													name="search_playCodeStatus__EQ_I"
													class="form-control input-small input-inline">
														<option value="">全部</option>
														<c:forEach var="item" items="${playCodeStatusEnum}">
															<option value="${item.key}"
																<c:if test="${! empty param.search_playCodeStatus__EQ_I && item.key eq param.search_playCodeStatus__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
														</c:forEach>
												</select>
												</label>
											</div>
											<div class="col-md-4">
												<label class="control-label">入库时间: <input
													type="text"
													class="form-control input-small-ext input-inline date date-picker"
													id="search_storageTime__GE_D"
													name="search_storageTime__GE_D"
													value="${param.search_storageTime__GE_D}"> <span
													class="">到</span> <input type="text"
													class="form-control input-small-ext input-inline date date-picker"
													id="search_storageTime__LE_D"
													name="search_storageTime__LE_D"
													value="${param.search_storageTime__LE_D}">
												</label>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-12">
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
												<label class="control-label">上线时间: <input
													type="text"
													class="form-control input-small-ext input-inline date date-picker"
													id="search_onlineTime__GE_D" name="search_onlineTime__GE_D"
													value="${param.search_onlineTime__GE_D}"> <span
													class="">到</span> <input type="text"
													class="form-control input-small-ext input-inline date date-picker"
													id="search_onlineTime__LE_D" name="search_onlineTime__LE_D"
													value="${param.search_onlineTime__LE_D}">
												</label>
											</div>
											<div class="col-md-4">
												<label class="control-label">&#12288;&#12288;&#12288;ID:
													<input type="text" name="search_id__EQ_L"
													class="form-control input-small input-inline"
													value="${param.search_id__EQ_L}">
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
								<th class="sorting" abbr="type">剧头类型</th>
								<th class="sorting" abbr="name">剧头名称</th>
								<th class="sorting" abbr="title">标题</th>
								<th class="sorting" abbr="episodeTotal">总集数</th>
								<th class="sorting" abbr="contentType">内容类型</th>
								<th class="sorting" abbr="tag">TAG</th>
								<th class="sorting" abbr="internalTag">内部标签</th>
								<th class="sorting" abbr="cpCode">提供商</th>
								<th class="sorting" abbr="templateId">码率</th>
								<th class="sorting" abbr="mediaStatus">媒资状态</th>
								<th class="sorting" abbr="injectionStatus">分发状态</th>
								<th class="sorting" abbr="playCodeStatus">播放代码</th>
								<th class="sorting" abbr="status">上线状态</th>
								<th class="sorting" abbr="storageTime">入库时间</th>
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th><input type="checkbox" class="group-checkable"
									data-set="#${formId}content_list .checkboxes" /></th>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="type">剧头类型</th>
								<th class="sorting" abbr="name">剧头名称</th>
								<th class="sorting" abbr="title">标题</th>
								<th class="sorting" abbr="episodeTotal">总集数</th>
								<th class="sorting" abbr="contentType">内容类型</th>
								<th class="sorting" abbr="tag">TAG</th>
								<th class="sorting" abbr="internalTag">内部标签</th>
								<th class="sorting" abbr="cpCode">提供商</th>
								<th class="sorting" abbr="templateId">码率</th>
								<th class="sorting" abbr="mediaStatus">媒资状态</th>
								<th class="sorting" abbr="injectionStatus">分发状态</th>
								<th class="sorting" abbr="playCodeStatus">播放代码</th>
								<th class="sorting" abbr="status">上线状态</th>
								<th class="sorting" abbr="storageTime">入库时间</th>
								<th>操作</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="t" varStatus="status">
								<tr>
									<td><input type="checkbox" class="checkboxes"
										value="${t.id}" data-name="${t.name}" /></td>
									<td>${status.index+1}</td>
									<td>${t.id}</td>
									<td><tags:enum className='class="badge badge-success"'
											enumList="${typeEnum}" value="${t.type}" /></td>
									<td><a href="javascript:;"
										onclick="$.SeriesController.detail('${contextPathPrefix}${t.id}/detail');">${t.name}</a></td>
									<td>${t.title}</td>
									<td>${t.episodeTotal}</td>
									<td><tags:enum className='class="badge badge-success"'
											enumList="${contentTypeEnum}" value="${t.contentType}" /></td>
									<td><tags:tagView value="${t.tag}" /></td>
									<td><tags:tagView value="${t.internalTag}" /></td>
									<td><tags:cpView value="${t.cpCode}" /></td>
									<td><tags:mediaTemplateView value="${t.templateId}" /></td>

									<td>${fns:getMediaStatusDesc(t.mediaStatus)}</td>
									<td><tags:injectionPlatformAndInjectionStatus
											value="${t.id}" /></td>
									<td><tags:playCode value="${t.playCodeStatus}" /></td>
									<td><tags:status value="${t.status}" /></td>
									<td><fmt:formatDate value="${t.storageTime}"
											pattern="yyyy-MM-dd" /></td>

									<td><c:if test="${t.status eq 0 || t.status eq 2}">
											<shiro:hasPermission name="${permissionPrefix}online">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.SeriesController.toOnline('${contextPathPrefix}${t.id}/online','${t.name}');">
													<i class="fa fa-pencil"></i>上线
												</button>
											</shiro:hasPermission>
										</c:if> <c:if test="${t.status eq 1}">
											<shiro:hasPermission name="${permissionPrefix}offline">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.SeriesController.toOffline('${contextPathPrefix}${t.id}/offline','${t.name}');">
													<i class="fa fa-pencil"></i>下线
												</button>
											</shiro:hasPermission>
										</c:if> <shiro:hasPermission name="${permissionPrefix}edit">
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.SeriesController.toEdit('${contextPathPrefix}${t.id}/edit',${t.id});">
												<i class="fa fa-edit"></i>修改
											</button>
										</shiro:hasPermission> <shiro:hasPermission name="${permissionPrefix}delete">
											<tags:deleteByStatus value='${t.status}'
												onclick="$.SeriesController.toDelete('${contextPathPrefix}${t.id}/delete','${t.name}');" />
										</shiro:hasPermission> <shiro:hasPermission name="media:program:view">
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.SeriesController.programList('${ctx}/media/program/listBySeriesId',${t.id});">
												<i class="fa fa-gear"></i>节目列表
											</button>
										</shiro:hasPermission>
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.PlayController.toPreviewSeries('${t.id}'); return false;">
											<i class="fa fa-eye"></i>预览
										</button></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

					<tags:pageInfo containerId="${containerId}" formId="${formId}" />

					<div class="modal-footer">
						<shiro:hasPermission name="${permissionPrefix}batchEdit">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.SeriesController.batchTo('${contextPathPrefix}batchType', 2);">
								<i class="fa fa-edit"></i>批量修改剧头类型
							</button>
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.SeriesController.toBatchChangeMetadata('${contextPathPrefix}batchChangeMetadata', 2);">
								<i class="fa fa-edit"></i>批量修改元数据
							</button>
						</shiro:hasPermission>

						<shiro:hasPermission name="${permissionPrefix}batchOnline">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.SeriesController.toBatchOnline('${contextPathPrefix}batchOnline', 2);">
								<i class="fa fa-edit"></i>批量上线
							</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="${permissionPrefix}batchOffline">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.SeriesController.toBatchOffline('${contextPathPrefix}batchOffline', 2);">
								<i class="fa fa-edit"></i>批量下线
							</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="${permissionPrefix}batchDelete">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.SeriesController.toBatchDelete('${contextPathPrefix}batchDelete', 2);">
								<i class="fa fa-remove"></i>批量删除
							</button>
						</shiro:hasPermission>
					</div>

					<shiro:hasAnyPermissions
						name="${permissionPrefix}batchInjection,${permissionPrefix}batchOutInjection">
						<div class="modal-footer">
							<!-- 							<button class="btn btn-default btn-sm btn-outline green" -->
							<%-- 								onclick="$.SeriesController.batchTo('${ctx}/album/album/batchToAlbum', 2);"> --%>
							<!-- 								<i class="fa fa-pencil"></i>批量编排到专题 -->
							<!-- 							</button> -->
							<!-- 							<button class="btn btn-default btn-sm btn-outline green" -->
							<%-- 								onclick="$.SeriesController.batchTo('${ctx}/widget/widget/batchToWidget', 2);"> --%>
							<!-- 								<i class="fa fa-pencil"></i>批量编排到推荐位 -->
							<!-- 							</button> -->
							<!-- 							<button class="btn btn-default btn-sm btn-outline green" -->
							<%-- 								onclick="$.SeriesController.batchTo('${ctx}/category/category/batchToCategory', 2);"> --%>
							<!-- 								<i class="fa fa-pencil"></i>批量编排到栏目 -->
							<!-- 							</button> -->
							<c:if test="${fns:isInjectionMedia()}">
								<shiro:hasPermission name="${permissionPrefix}batchInjection">
									<button class="btn btn-default btn-sm btn-outline green"
										onclick="$.SeriesController.toBatchInjection('${contextPathPrefix}batchInjection', 2);">
										<i class="fa fa-edit"></i>批量分发
									</button>
								</shiro:hasPermission>
								<shiro:hasPermission name="${permissionPrefix}batchOutInjection">
									<button class="btn btn-default btn-sm btn-outline green"
										onclick="$.SeriesController.toBatchInjection('${contextPathPrefix}batchOutInjection', 2);">
										<i class="fa fa-edit"></i>批量回收
									</button>
								</shiro:hasPermission>
								<shiro:hasPermission name="${permissionPrefix}batchInjection">
									<button class="btn btn-default btn-sm btn-outline green"
										onclick="$.SeriesController.toResetInjectionStatus('${contextPathPrefix}resetInjectionStatus', 2);">
										<i class="fa fa-edit"></i>重置分发状态
									</button>
								</shiro:hasPermission>
							</c:if>
						</div>
					</shiro:hasAnyPermissions>

					<shiro:hasPermission name="${permissionPrefix}batchExport">
						<div class="modal-footer">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.SeriesController.batchExport('${ctx}/media/program/batchExportBySeries', 2);">
								<i class="fa fa-cloud-download"></i>导出媒体内容元数据(已选择)
							</button>
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.SeriesController.exportAll('${ctx}/media/program/exportAllBySeries', 2);">
								<i class="fa fa-cloud-download"></i>导出媒体内容元数据(符合条件)
							</button>
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.SeriesController.batchExport('${contextPathPrefix}batchExportMetadata', 2);">
								<i class="fa fa-cloud-download"></i>导出内容元数据(已选择)
							</button>
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.SeriesController.exportAll('${contextPathPrefix}exportAllMetadata', 2);">
								<i class="fa fa-cloud-download"></i>导出内容元数据(符合条件)
							</button>
						</div>
					</shiro:hasPermission>
				</div>
			</div>
		</div>
	</div>
</div>

<tags:contentModal />

<script type="text/javascript">
	$.SeriesController.init('${formId}');
	$.SeriesController.keypress({containerId: '${containerId}', formId: '${formId}'});
</script>
