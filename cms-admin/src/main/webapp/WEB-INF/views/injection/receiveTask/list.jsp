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
						<i class="fa fa-globe"></i>接收工单查询
					</div>
					<div class="actions"></div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/injection/receiveTask/">
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
											<label class="control-label">接收时间: <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_receiveTime__GE_D"
												name="search_receiveTime__GE_D"
												value="${param.search_receiveTime__GE_D}"> <span
												class="">到</span> <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_receiveTime__LE_D"
												name="search_receiveTime__LE_D"
												value="${param.search_receiveTime__LE_D}">
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
										<div class="col-md-4"></div>
										<div class="col-md-4">
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
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="platformId">分发平台</th>
								<th class="sorting" abbr="receiveTime">接收时间</th>
								<th class="sorting" abbr="status">工单状态</th>
								<th class="sorting" abbr="priority">优先级</th>
								<th class="sorting" abbr="correlateId">工单标识</th>
								<th class="sorting" abbr="responseTotalTimes">发送响应总次数</th>
								<th class="sorting" abbr="lastResponseTime">最后发送响应时间</th>
								<th class="sorting" abbr="requestResult">请求结果</th>
								<th class="sorting" abbr="responseResult">响应结果</th>
								<th>工单文件</th>
								<th class="sorting" abbr="downloadTotal">下载总数</th>
								<th class="sorting" abbr="downloadSuccess">下载成功</th>
								<th class="sorting" abbr="downloadFail">下载失败</th>
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="platformId">分发平台</th>
								<th class="sorting" abbr="receiveTime">接收时间</th>
								<th class="sorting" abbr="status">工单状态</th>
								<th class="sorting" abbr="priority">优先级</th>
								<th class="sorting" abbr="correlateId">工单标识</th>
								<th class="sorting" abbr="responseTotalTimes">发送响应总次数</th>
								<th class="sorting" abbr="lastResponseTime">最后发送响应时间</th>
								<th class="sorting" abbr="requestResult">请求结果</th>
								<th class="sorting" abbr="responseResult">响应结果</th>
								<th>工单文件</th>
								<th class="sorting" abbr="downloadTotal">下载总数</th>
								<th class="sorting" abbr="downloadSuccess">下载成功</th>
								<th class="sorting" abbr="downloadFail">下载失败</th>
								<th>操作</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="t" varStatus="status">
								<tr>
									<td>${status.index+1}</td>
									<td>${t.id}</td>
									<td><span class="badge badge-success">${injectionPlatformMap[t.platformId].name}</span></td>
									<td><a href="javascript:;"
										onclick="$.ReceiveTaskController.detail('${t.id}');"><fmt:formatDate
												value="${t.receiveTime}" pattern="yyyy-MM-dd HH:mm:ss" /></a></td>
									<td><c:forEach var="item" items="${statusEnum}">
											<c:if test="${item.key eq t.status && item.key eq 0}">
												<span class="badge badge-info">${item.value}</span>
											</c:if>
											<c:if test="${item.key eq t.status && (item.key eq 1 || item.key eq 2 || item.key eq 6)}">
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
									<td>${t.priority}</td>
									<td>${t.correlateId}</td>
									<td>${t.responseTotalTimes}</td>
									<td><fmt:formatDate value="${t.lastResponseTime}"
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
													class="glyphicon glyphicon-cloud-download"></span></a>
											</c:when>
											<c:otherwise>
												<span title="请求文件"
													class="glyphicon glyphicon-cloud-download"></span>
											</c:otherwise>
										</c:choose> <c:choose>
											<c:when test="${!empty t.responseXmlFilePath}">
												<a href="${fns:getXmlWebPath(t.responseXmlFilePath)}"
													target="_blank"><span title="响应文件"
													class="glyphicon glyphicon-cloud-upload"></span></a>
											</c:when>
											<c:otherwise>
												<span title="响应文件" class="glyphicon glyphicon-cloud-upload"></span>
											</c:otherwise>
										</c:choose></td>
									<td>${t.downloadTotal}</td>
									<td>${t.downloadSuccess}</td>
									<td>${t.downloadFail}</td>
									<td><button
											class="btn btn-default btn-sm btn-outline green"
											onclick="$.ReceiveTaskController.taskList(${t.id});">
											<i class="fa fa-gear"></i>管理任务
										</button></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<tags:pageInfo containerId="${containerId}" formId="${formId}" />
				</div>
			</div>
		</div>
	</div>
</div>

<tags:contentModal />

<script>
	$.ReceiveTaskController.init('${formId}');
	$.ReceiveTaskController.keypress({
		containerId : '${containerId}',
		formId : '${formId}'
	});
</script>