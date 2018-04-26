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
						<i class="fa fa-globe"></i>转码任务管理
					</div>
					<div class="actions"></div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/transcode/transcodeTask/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">任务名称: <input type="text"
												name="search_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_name__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">任务类型: <select
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
											<label class="control-label">&#12288;&#12288;码率: <select
												name="search_templateId__EQ_L"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="mediaTemplate" items="${mediaTemplateList}">
														<option value="${mediaTemplate.id}"
															<c:if test="${! empty param.search_templateId__EQ_L && mediaTemplate.id == param.search_templateId__EQ_L}">selected="selected"</c:if>>${mediaTemplate.code}</option>
													</c:forEach>
											</select>
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
												<label class="control-label">任务状态: <select
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
												<label class="control-label">输入路径: <input
													type="text" name="search_inputFilePath__LIKE_S"
													class="form-control input-small input-inline"
													value="${param.search_inputFilePath__LIKE_S}">
												</label>
											</div>
											<div class="col-md-4">
												<label class="control-label">输出路径: <input
													type="text" name="search_outputFilePath__LIKE_S"
													class="form-control input-small input-inline"
													value="${param.search_outputFilePath__LIKE_S}">
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
								<th class="idrting" abbr="id">任务id</th>
								<th class="sorting" abbr="name">任务名称</th>
								<th class="sorting" abbr="type">任务类型</th>

								<th class="sorting" abbr="templateId">码率</th>
								<th class="sorting" abbr="requestTotalTimes">发送指令总次数</th>
								<th class="sorting" abbr="lastRequestTime">最后发送指令时间</th>
								<th class="sorting" abbr="priority">优先级</th>
								<th class="sorting" abbr="status">任务状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th><input type="checkbox" class="group-checkable"
									data-set="#${formId}content_list .checkboxes" /></th>
								<th>序号</th>
								<th class="sorting" abbr="id">任务id</th>
								<th class="sorting" abbr="name">任务名称</th>
								<th class="sorting" abbr="type">任务类型</th>

								<th class="sorting" abbr="templateId">码率</th>
								<th class="sorting" abbr="requestTotalTimes">发送指令总次数</th>
								<th class="sorting" abbr="lastRequestTime">最后发送指令时间</th>
								<th class="sorting" abbr="priority">优先级</th>
								<th class="sorting" abbr="status">任务状态</th>
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
									<td><a href="javascript:;"
										onclick="$.TranscodeTaskController.detail('${t.id}');">${t.name}</a></td>

									<td><tags:type value='${t.type}' /></td>
									<td><c:forEach var="mediaTemplate"
											items="${mediaTemplateList}">
											<c:if
												test="${! empty t.templateId && mediaTemplate.id eq t.templateId}">
												<span class="badge badge-success">${mediaTemplate.code}</span>
											</c:if>
										</c:forEach></td>
									<td>${t.requestTotalTimes}</td>
									<td><fmt:formatDate value="${t.lastRequestTime}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td>${t.priority}</td>
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
												test="${item.key eq t.status && (item.key eq 4 || item.key eq 5 || item.key eq 9)}">
												<span class="badge badge-danger">${item.value}</span>
											</c:if>
										</c:forEach></td>
									<td><c:forEach var="item" items="${statusEnum}">
											<c:if test="${item.key eq t.status && (item.key eq 1)}">
												<shiro:hasPermission name="transcode:transcodeTask:send">
													<button class="btn btn-default btn-sm btn-outline green"
														onclick="$.TranscodeTaskController.toSend('${ctx}/transcode/transcodeTask/${t.id}/send','${t.name}','发送指令');">
														<i class="fa fa-pencil"></i>发送指令
													</button>
												</shiro:hasPermission>
											</c:if>
											<c:if test="${item.key eq t.status && (item.key ne 1)}">
												<shiro:hasPermission name="transcode:transcodeTask:reset">
													<button class="btn btn-default btn-sm btn-outline green"
														onclick="$.TranscodeTaskController.toReset('${ctx}/transcode/transcodeTask/${t.id}/reset','${t.name}','重发指令');">
														<i class="fa fa-pencil"></i>重发指令
													</button>
												</shiro:hasPermission>
											</c:if>
										</c:forEach> <shiro:hasPermission name="transcode:transcodeTask:delete">
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.TranscodeTaskController.toDelete('${ctx}/transcode/transcodeTask/${t.id}/delete','${t.name}');">
												<i class="fa fa-remove"></i>删除
											</button>
										</shiro:hasPermission></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

					<tags:pageInfo containerId="${containerId}" formId="${formId}" />

					<div class="modal-footer">
						<shiro:hasPermission
							name="transcode:transcodeTask:batchChangePriority">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.TranscodeTaskController.toBatch('${ctx}/transcode/transcodeTask/batchChangePriority', '1102');">
								<i class="fa fa-edit"></i>批量调整优先级
							</button>
						</shiro:hasPermission>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<tags:contentModal />

<script>
	$.TranscodeTaskController.init('${formId}');
	$.TranscodeTaskController.keypress({
		containerId : '${containerId}',
		formId : '${formId}'
	});
</script>