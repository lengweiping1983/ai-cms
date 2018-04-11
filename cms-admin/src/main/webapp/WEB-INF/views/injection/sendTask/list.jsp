<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<c:set var="containerId" value="" />
<c:set var="formId" value="" />
<c:set var="contextPathPrefix" value="${ctx}/injection/sendTask/" />
<c:set var="permissionPrefix" value="injection:sendTask:" />
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>分发工单查询
					</div>
					<div class="actions"></div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${contextPathPrefix}">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">对象类型: <select
												name="search_requestXmlFileContent__LIKE_SS"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${injectionItemTypeEnum}">
														<c:set value="ElementType=&quot;${item.value}&quot;"
															var="itemValue"></c:set>
														<option value='${itemValue}'
															<c:if test='${! empty param.search_requestXmlFileContent__LIKE_SS && itemValue eq param.search_requestXmlFileContent__LIKE_SS}'> selected="selected" </c:if>>${item.name}</option>
													</c:forEach>
											</select>
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">操作类型: <select
												name="search_requestXmlFileContent__LIKE_SSS"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${injectionActionTypeEnum}">
														<c:set value="Action=&quot;${item.value}&quot;"
															var="itemValue"></c:set>
														<option value='${itemValue}'
															<c:if test="${! empty param.search_requestXmlFileContent__LIKE_SSS && itemValue eq param.search_requestXmlFileContent__LIKE_SSS}"> selected="selected" </c:if>>${item.name}</option>
													</c:forEach>
											</select>
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">&#12288;关键词: <input
												type="text" name="search_requestXmlFileContent__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_requestXmlFileContent__LIKE_S}">
											</label>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">分发平台: <select
												name="search_platformId__EQ_I"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${injectionPlatformList}">
														<option value="${item.id}"
															<c:if test="${! empty param.search_platformId__EQ_I && item.id eq param.search_platformId__EQ_I}"> selected="selected" </c:if>>${item.name}</option>
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
											<label class="control-label">工单状态: <select
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
										<div class="col-md-4">
											<label class="control-label">工单标识: <input type="text"
												name="search_correlateId__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_correlateId__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">发送时间: <input type="text"
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
										<tags:searchButton containerId="${containerId}"
											formId="${formId}" />
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
								<th class="sorting" abbr="platformId">分发平台</th>
								<th class="sorting" abbr="name">工单名称</th>
								<th class="sorting" abbr="status">工单状态</th>
								<th class="sorting" abbr="priority">优先级</th>
								<th class="sorting" abbr="correlateId">工单标识</th>
								<th class="sorting" abbr="preTaskId">前置工单ID</th>
								<th class="sorting" abbr="preTaskStatus">前置工单是否完成</th>
								<th class="sorting" abbr="requestTotalTimes">发送请求总次数</th>
								<th class="sorting" abbr="lastRequestTime">最后发送请求时间</th>
								<th class="sorting" abbr="requestResult">请求结果</th>
								<th class="sorting" abbr="responseResult">响应结果</th>
								<th>工单文件</th>
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th><input type="checkbox" class="group-checkable"
									data-set="#content_list .checkboxes" /></th>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="platformId">分发平台</th>
								<th class="sorting" abbr="name">工单名称</th>
								<th class="sorting" abbr="status">工单状态</th>
								<th class="sorting" abbr="priority">优先级</th>
								<th class="sorting" abbr="correlateId">工单标识</th>
								<th class="sorting" abbr="preTaskId">前置工单ID</th>
								<th class="sorting" abbr="preTaskStatus">前置工单是否完成</th>
								<th class="sorting" abbr="requestTotalTimes">发送请求总次数</th>
								<th class="sorting" abbr="lastRequestTime">最后发送请求时间</th>
								<th class="sorting" abbr="requestResult">请求结果</th>
								<th class="sorting" abbr="responseResult">响应结果</th>
								<th>工单文件</th>
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
									<td><span class="badge badge-success">${injectionPlatformMap[t.platformId].name}</span></td>
									<td><a href="javascript:;"
										onclick="$.SendTaskController.detail('${t.id}');">${t.name}</a></td>
									<td><tags:taskStatus value='${t.status}' /></td>
									<td>${t.priority}</td>
									<td>${t.correlateId}</td>
									<td>${t.preTaskId}</td>
									<td><tags:yesNo value='${t.preTaskStatus}' /></td>
									<td>${t.requestTotalTimes}</td>
									<td><fmt:formatDate value="${t.lastRequestTime}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td><c:choose>
											<c:when test="${!empty t.requestResult}">
												<c:if test="${t.requestResult eq 0}">
													<span class="badge badge-success">成功</span>
												</c:if>
												<c:if test="${t.requestResult ne 0}">
													<span class="badge badge-danger">失败</span>
												</c:if>
											</c:when>
											<c:otherwise>
											</c:otherwise>
										</c:choose></td>
									<td><c:choose>
											<c:when test="${!empty t.responseResult}">
												<c:if test="${t.responseResult eq 0}">
													<span class="badge badge-success">成功</span>
												</c:if>
												<c:if test="${t.responseResult ne 0}">
													<span class="badge badge-danger">失败</span>
												</c:if>
											</c:when>
											<c:otherwise>
											</c:otherwise>
										</c:choose></td>
									<td><c:choose>
											<c:when test="${!empty t.requestXmlFilePath}">
												<a href="${fns:getXmlWebPath(t.requestXmlFilePath)}"
													target="_blank"><span title="请求文件"
													class="glyphicon glyphicon-cloud-upload"></span></a>
											</c:when>
											<c:otherwise>
												<span title="请求文件" class="glyphicon glyphicon-cloud-upload"></span>
											</c:otherwise>
										</c:choose> <c:choose>
											<c:when test="${!empty t.responseXmlFilePath}">
												<a href="${fns:getXmlWebPath(t.responseXmlFilePath)}"
													target="_blank"><span title="响应文件"
													class="glyphicon glyphicon-cloud-download"></span></a>
											</c:when>
											<c:otherwise>
												<span title="响应文件"
													class="glyphicon glyphicon-cloud-download"></span>
											</c:otherwise>
										</c:choose></td>
									<td><c:forEach var="item" items="${statusEnum}">
											<c:if test="${item.key eq t.status && (item.key eq 1)}">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.SendTaskController.toSend('${contextPathPrefix}${t.id}/pause','${t.name}','暂停');">
													<i class="fa fa-pause"></i>暂停
												</button>
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.SendTaskController.toSend('${contextPathPrefix}${t.id}/stop','${t.name}','停止');">
													<i class="fa fa-stop"></i>停止
												</button>
											</c:if>
											<c:if
												test="${item.key eq t.status && (item.key eq 2 || item.key eq 3 || item.key eq 4 || item.key eq 5 ||item.key eq 7)}">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.SendTaskController.toSend('${contextPathPrefix}${t.id}/reset','${t.name}','重发');">
													<i class="fa fa-play-circle"></i>重发
												</button>
											</c:if>
										</c:forEach></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

					<tags:pageInfo containerId="${containerId}" formId="${formId}" />

					<div class="modal-footer">
						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.SendTaskController.toBatchChangeStatus('${contextPathPrefix}batchPause', 109, '批量暂停工单');">
							<i class="fa fa-pause"></i>批量暂停
						</button>
						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.SendTaskController.toBatchChangeStatus('${contextPathPrefix}batchStop', 109, '批量停止工单');">
							<i class="fa fa-stop"></i>批量停止
						</button>
						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.SendTaskController.toBatchChangeStatus('${contextPathPrefix}batchReset', 109, '批量重发工单');">
							<i class="fa fa-play"></i>批量重发
						</button>

						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.SendTaskController.batchTo('${contextPathPrefix}batchChangePriority', 109);">
							<i class="fa fa-edit"></i>批量调整优先级
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<tags:contentModal />

<script>
	$.SendTaskController.init('${formId}');
	$.SendTaskController.keypress({
		containerId : '${containerId}',
		formId : '${formId}'
	});
</script>