<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<script src="${ctx}/static/scripts/system/operationLog-controller.js"
	type="text/javascript"></script>

<c:set var="containerId" value="" />
<c:set var="formId" value="" />
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>操作行为日志
					</div>
					<div class="actions">
						<shiro:hasPermission name="system:log:clear">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.OperationLogController.toClear('${ctx}/system/operationlog/clear')">
								<i class="fa fa-plus"></i>清空历史日志
							</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/system/operationlog/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">操作时间: <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_createTime__GE_D" name="search_createTime__GE_D"
												value="${param.search_createTime__GE_D}"> <span
												class="">到</span> <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_createTime__LE_D" name="search_createTime__LE_D"
												value="${param.search_createTime__LE_D}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">&#12288;操作人: <input
												type="text" name="search_userName__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_userName__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">操作模块: <input type="text"
												name="search_subModule__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_subModule__LIKE_S}">
											</label>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">操作动作: <input type="text"
												name="search_action__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_action__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">操作描述: <input type="text"
												name="search_message__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_message__LIKE_S}">
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
						class="table dataTable table-striped table-bordered table-hover"
						style="table-layout: fixed">
						<thead>
							<tr>
								<th class="sorting" abbr="createTime">操作时间</th>
								<th class="sorting" abbr="userName">操作人</th>
								<th class="sorting" abbr="subModule">操作模块</th>

								<th class="sorting" abbr="action">操作动作</th>
								<th class="sorting" abbr="actionResult">操作结果</th>
								<th class="sorting" abbr="message">操作描述</th>

								<th class="sorting">操作对象ID</th>
								<th class="sorting">操作对象名称</th>
								<th class="sorting" abbr="ip">操作IP</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th class="sorting" abbr="createTime">操作时间</th>
								<th class="sorting" abbr="userName">操作人</th>
								<th class="sorting" abbr="subModule">操作模块</th>

								<th class="sorting" abbr="action">操作动作</th>
								<th class="sorting" abbr="actionResult">操作结果</th>
								<th class="sorting" abbr="message">操作描述</th>

								<th class="sorting">操作对象ID</th>
								<th class="sorting">操作对象名称</th>
								<th class="sorting" abbr="ip">操作IP</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="log">
								<tr>
									<td><span class="badge badge-default"><fmt:formatDate
												value="${log.createTime}" pattern="yyyy-MM-dd HH:mm" /></span></td>
									<td><span class="small">${log.userName}</span></td>
									<td><span class="small">${log.subModule}</span></td>

									<td><span class="badge badge-info">${log.action}</span></td>
									<td><c:if test="${log.actionResult eq 0}">
											<span class="badge badge-success">成功</span>
										</c:if> <c:if test="${log.actionResult ne 0}">
											<span class="badge badge-danger">失败</span>
										</c:if></td>
									<td><span class="small">${log.message}</span></td>

									<td><c:forEach items="${log.objectId.split('\\\\n')}"
											var="objectId">
											<span class="badge badge-info">${objectId}</span>
										</c:forEach></td>
									<td><c:forEach items="${log.objectName.split('\\\\n')}"
											var="objectName">
											<span
												style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis;"
												class="badge badge-info">${objectName}</span>
											<br />
										</c:forEach></td>
									<td><span class="badge icon-badge">${log.ip}</span></td>
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
	$.OperationLogController.initTimepicker();
	$.OperationLogController.keypress({
		containerId : '${containerId}',
		formId : '${formId}'
	});
</script>