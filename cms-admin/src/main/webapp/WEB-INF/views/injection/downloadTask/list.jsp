<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<c:set var="containerId" value="" />
<c:set var="formId" value="" />
<c:set var="contextPathPrefix" value="${ctx}/injection/downloadTask/" />
<c:set var="permissionPrefix" value="injection:downloadTask:" />
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>下载任务管理
					</div>
					<div class="actions">
						<c:if test="${param.from eq 'ReceiveTask'}">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.common.ajaxLoadLastContent()"> <i
								class="fa fa-reply"></i> 返回
							</a>
						</c:if>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${contextPathPrefix}">
								<input type="hidden" name="from" value="${param.from}" /> <input
									type="hidden" name="search_pid__EQ_L"
									value="${param.search_pid__EQ_L}" />
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
											<label class="control-label">下载时间: <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_lastRequestTime__GE_D"
												name="search_lastRequestTime__GE_D"
												value="${param.search_lastRequestTime__GE_D}"> <span
												class="">到</span> <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_lastRequestTime__LE_D"
												name="search_lastRequestTime__LE_D"
												value="${param.search_lastRequestTime__LE_D}">
											</label>
										</div>
									</div>
								</div>

								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">&#12288;优先级: <select
												name="search_priority__EQ_I"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach varStatus="status" begin="1" end="20">
														<option value="${status.index}"
															<c:if test="${status.index eq param.search_priority__EQ_I}"> selected="selected" </c:if>>${status.index}</option>
													</c:forEach>
											</select>
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">输入路径: <input type="text"
												name="search_inputFilePath__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_inputFilePath__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">输出路径: <input type="text"
												name="search_outputFilePath__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_outputFilePath__LIKE_S}">
											</label>
											<tags:searchButton containerId="${containerId}"
												formId="${formId}" />
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
						id="content_list">
						<thead>
							<tr>
								<th><input type="checkbox" class="group-checkable"
									data-set="#content_list .checkboxes" /></th>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="name">任务名称</th>
								<th class="sorting" abbr="status">任务状态</th>
								<th class="sorting" abbr="priority">优先级</th>
								<th class="sorting" abbr="requestTotalTimes">下载总次数</th>
								<th class="sorting" abbr="lastRequestTime">最后下载时间</th>
								<th class="sorting" abbr="fileSize">文件大小</th>
								<th class="sorting" abbr="percent">完成百分比</th>
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th><input type="checkbox" class="group-checkable"
									data-set="#content_list .checkboxes" /></th>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="name">任务名称</th>
								<th class="sorting" abbr="status">任务状态</th>
								<th class="sorting" abbr="priority">优先级</th>
								<th class="sorting" abbr="requestTotalTimes">下载总次数</th>
								<th class="sorting" abbr="lastRequestTime">最后下载时间</th>
								<th class="sorting" abbr="fileSize">文件大小</th>
								<th class="sorting" abbr="percent">完成百分比</th>
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
										onclick="$.DownloadTaskController.detail('${t.id}');">${t.name}</a></td>
									<td><tags:taskStatus value='${t.status}' /></td>
									<td>${t.priority}</td>
									<td>${t.requestTotalTimes}</td>
									<td><fmt:formatDate value="${t.lastRequestTime}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td>${fns:getFileSizeS(t.fileSize)}</td>
									<td><span class="badge badge-success">${t.percent}%</span></td>
									<td><c:if test="${t.status ne 3}">
											<shiro:hasPermission name="injection:downloadTask:pause">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.DownloadTaskController.toSend('${contextPathPrefix}${t.id}/pause','${t.name}','暂停下载');">
													<i class="fa fa-pause"></i>暂停
												</button>
											</shiro:hasPermission>
											<shiro:hasPermission name="injection:downloadTask:stop">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.DownloadTaskController.toSend('${contextPathPrefix}${t.id}/stop','${t.name}','暂停下载');">
													<i class="fa fa-stop"></i>停止
												</button>
											</shiro:hasPermission>
										</c:if> <c:if
											test="${t.status ne 3 && t.status ne 0 && t.status ne 1 && t.status ne 6}">
											<shiro:hasPermission name="injection:downloadTask:reset">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.DownloadTaskController.toSend('${contextPathPrefix}${t.id}/reset','${t.name}','继续下载');">
													<i class="fa fa-play"></i>继续
												</button>
											</shiro:hasPermission>
										</c:if> <c:if
											test="${t.status ne 0 && t.status ne 1 && t.status ne 6}">
											<shiro:hasPermission name="injection:downloadTask:renew">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.DownloadTaskController.toSend('${contextPathPrefix}${t.id}/renew','${t.name}','重新下载');">
													<i class="fa fa-play-circle"></i>重新下载
												</button>
											</shiro:hasPermission>
										</c:if></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

					<tags:pageInfo containerId="${containerId}" formId="${formId}" />

					<div class="modal-footer">
						<shiro:hasPermission name="injection:downloadTask:batchPause">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.DownloadTaskController.toBatchChangeStatus('${contextPathPrefix}batchPause', 109, '批量暂停下载');">
								<i class="fa fa-pause"></i>批量暂停
							</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="injection:downloadTask:batchStop">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.DownloadTaskController.toBatchChangeStatus('${contextPathPrefix}batchStop', 109, '批量停止下载');">
								<i class="fa fa-stop"></i>批量停止
							</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="injection:downloadTask:batchReset">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.DownloadTaskController.toBatchChangeStatus('${contextPathPrefix}batchReset', 109, '批量继续下载');">
								<i class="fa fa-play"></i>批量继续
							</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="injection:downloadTask:batchRenew">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.DownloadTaskController.toBatchChangeStatus('${contextPathPrefix}batchRenew', 109, '批量重新下载');">
								<i class="fa fa-play-circle"></i>批量重新下载
							</button>
						</shiro:hasPermission>
						<shiro:hasPermission
							name="injection:downloadTask:batchChangePriority">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.DownloadTaskController.batchTo('${contextPathPrefix}batchChangePriority', 109);">
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

<script type="text/javascript">
	$.DownloadTaskController.init('${formId}');
	$.DownloadTaskController.keypress({
		containerId : '${containerId}',
		formId : '${formId}'
	});
</script>