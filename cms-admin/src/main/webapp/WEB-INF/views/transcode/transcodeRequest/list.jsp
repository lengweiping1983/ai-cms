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
						<i class="fa fa-globe"></i>转码工单管理
					</div>
					<div class="actions">
						<c:forEach var="item" items="${typeEnum}">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.TranscodeRequestController.toEdit('${ctx}/transcode/transcodeRequest/add/${item.key}')">
								<i class="fa fa-plus"></i>增加${item.value}
							</a>
						</c:forEach>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/transcode/transcodeRequest/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">工单类型: <select
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
											<label class="control-label">工单名称: <input type="text"
												name="search_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_name__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">媒资名称: <input type="text"
												name="search_mediaName__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_mediaName__LIKE_S}">
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
												<label class="control-label">内部标签: <input
													type="text" name="search_internalTag__LIKE_S"
													class="form-control input-small input-inline"
													value="${param.search_internalTag__LIKE_S}">
												</label>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-12">
											<div class="col-md-4">
												<tags:cpSearch prefix="${formId}" />
											</div>
											<div class="col-md-4">
												<label class="control-label">&#12288;&#12288;码率: <select
													name="search_templateId__CHARMASK_S"
													class="form-control input-small input-inline">
														<option value="">全部</option>
														<c:forEach var="mediaTemplate"
															items="${mediaTemplateList}">
															<option value="${mediaTemplate.id}"
																<c:if test="${! empty param.search_templateId__CHARMASK_S && mediaTemplate.id == param.search_templateId__CHARMASK_S}">selected="selected"</c:if>>${mediaTemplate.code}</option>
														</c:forEach>
												</select>
												</label>
											</div>
											<div class="col-md-4">
												<label class="control-label">&#12288;&#12288;状态: <select
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
								<th class="sorting" abbr="type">工单类型</th>
								<th class="sorting" abbr="name">工单名称</th>
								<th class="sorting" abbr="mediaName">媒资名称</th>
								<th class="sorting" abbr="contentType">内容类型</th>
								<th class="sorting" abbr="tag">TAG</th>
								<th class="sorting" abbr="internalTag">内部标签</th>
								<th class="sorting" abbr="cpCode">提供商</th>
								<th class="sorting" abbr="templateId">码率</th>
								<th class="sorting" abbr="status">状态</th>
								<th class="sorting" abbr="taskTotal">总任务数</th>
								<th class="sorting" abbr="taskSuccess">成功任务数</th>
								<th class="sorting" abbr="taskFail">失败任务数</th>
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th><input type="checkbox" class="group-checkable"
									data-set="#${formId}content_list .checkboxes" /></th>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="type">工单类型</th>
								<th class="sorting" abbr="name">工单名称</th>
								<th class="sorting" abbr="mediaName">媒资名称</th>
								<th class="sorting" abbr="contentType">内容类型</th>
								<th class="sorting" abbr="tag">TAG</th>
								<th class="sorting" abbr="internalTag">内部标签</th>
								<th class="sorting" abbr="cpCode">提供商</th>
								<th class="sorting" abbr="templateId">码率</th>
								<th class="sorting" abbr="status">状态</th>
								<th class="sorting" abbr="taskTotal">总任务数</th>
								<th class="sorting" abbr="taskSuccess">成功任务数</th>
								<th class="sorting" abbr="taskFail">失败任务数</th>
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
									<td><tags:type value='${t.type}' /></td>
									<td><a href="javascript:;"
										onclick="$.TranscodeRequestController.detail('${ctx}/transcode/transcodeRequest/${t.id}/detail');">${t.name}</a>
									</td>
									<td>${t.mediaName}</td>
									<td><tags:enum enumList="${contentTypeEnum}"
											value="${t.contentType}" /></td>
									<td><tags:tagView value="${t.tag}" /></td>
									<td><tags:tagView value="${t.internalTag}" /></td>
									<td><tags:cpView value="${t.cpCode}" /></td>
									<td><c:if test="${! empty t.templateId}">
											<c:forEach items="${t.templateId}" var="templateId">
												<c:forEach var="mediaTemplate" items="${mediaTemplateList}">
													<c:if test="${mediaTemplate.id eq templateId}">
														<span class="badge badge-success">${mediaTemplate.code}</span>
													</c:if>
												</c:forEach>
											</c:forEach>
										</c:if></td>
									<td><c:forEach var="item" items="${statusEnum}">
											<c:if test="${item.key eq t.status && item.key eq 1}">
												<span class="badge badge-info">${item.value}</span>
											</c:if>
											<c:if test="${item.key eq t.status && item.key eq 2}">
												<span class="badge badge-primary">${item.value}</span>
											</c:if>
											<c:if test="${item.key eq t.status && item.key eq 3}">
												<span class="badge badge-success">${item.value}</span>
											</c:if>
											<c:if
												test="${item.key eq t.status && (item.key eq 4 || item.key eq 5)}">
												<span class="badge badge-danger">${item.value}</span>
											</c:if>
										</c:forEach></td>
									<td>${t.taskTotal}</td>
									<td>${t.taskSuccess}</td>
									<td>${t.taskFail}</td>
									<td><c:forEach var="item" items="${statusEnum}">
											<c:if test="${item.key eq t.status && item.key ne 1}">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.TranscodeRequestController.taskList(${t.id});">
													<i class="fa fa-gear"></i>管理任务
												</button>
											</c:if>
											<c:if test="${item.key eq t.status && item.key eq 1}">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.TranscodeRequestController.toEdit('${ctx}/transcode/transcodeRequest/${t.id}/edit',${t.id});">
													<i class="fa fa-edit"></i>修改
												</button>
											</c:if>
										</c:forEach>
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.TranscodeRequestController.toDelete('${ctx}/transcode/transcodeRequest/${t.id}/delete','${t.name}');">
											<i class="fa fa-remove"></i>删除
										</button> <c:if test="${t.status ne 1}">
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.TranscodeRequestController.toCopy('${ctx}/transcode/transcodeRequest/batchCopy',1101,${t.id});">
												<i class="fa fa-copy"></i>快速复制
											</button>
										</c:if></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<tags:pageInfo containerId="${containerId}" formId="${formId}" />

					<div class="modal-footer">
						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.TranscodeRequestController.toBatchProduce('${ctx}/transcode/transcodeRequest/batchProduce', 1101);">
							<i class="fa fa-edit"></i>批量执行
						</button>
						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.TranscodeRequestController.toBatch('${ctx}/transcode/transcodeRequest/batchCopy', 1101);">
							<i class="fa fa-copy"></i>批量复制
						</button>

						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.TranscodeRequestController.batchExport('${ctx}/transcode/transcodeRequest/batchExport', 1101);">
							<i class="fa fa-cloud-download"></i>批量导出
						</button>
						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.TranscodeRequestController.exportAll('${ctx}/transcode/transcodeRequest/exportAll', 1101);">
							<i class="fa fa-cloud-download"></i>导出全部
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<tags:contentModal />

<script>
	$.TranscodeRequestController.init('${formId}');
	$.TranscodeRequestController.keypress({containerId: '${containerId}', formId: '${formId}'});
</script>
