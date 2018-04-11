<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<c:set var="containerId" value="content_list_container" />
<c:set var="formId" value="selectFile" />
<div class="table-container">
	<div class="table-actions-wrapper-condition">
		<form action="${ctx}/media/file/selectFile" id="selectFile">
			<input type="hidden" name="selectMode" value="${param.selectMode}">
			<input type="hidden" name="selectParam" value="${param.selectParam}">
			<div class="row">
				<div class="col-md-7">
					<label class="control-label">文件路径: <input type="text"
						id="selectFile_path" name="path"
						class="form-control input-xlarge input-inline" value="${path}">
					</label>
				</div>
				<div class="col-md-5">
					<label class="control-label">文件名称: <input type="text"
						name="name" class="form-control input-small input-inline"
						value="${param.name}">
					</label>
					<tags:searchButton containerId="${containerId}" formId="${formId}" />
				</div>
			</div>
		</form>
	</div>
</div>
<table
	class="table dataTable table-striped table-bordered table-hover table-checkable"
	id="${formId}content_list">
	<thead>
		<tr>
			<c:if test="${param.selectMode eq 'multi'}">
				<th><input type="checkbox" class="group-checkable"
					data-set="#${formId}content_list .checkboxes" /></th>
			</c:if>
			<th>序号</th>
			<th>文件名称</th>
			<th>文件类型</th>
			<th>文件大小</th>
		</tr>
	</thead>
	<tfoot>
		<tr>
			<c:if test="${param.selectMode eq 'multi'}">
				<th><input type="checkbox" class="group-checkable"
					data-set="#${formId}content_list .checkboxes" /></th>
			</c:if>
			<th>序号</th>
			<th>文件名称</th>
			<th>文件类型</th>
			<th>文件大小</th>
		</tr>
	</tfoot>
	<tbody>
		<c:forEach items="${files}" var="t" varStatus="status">
			<tr>
				<c:if test="${param.selectMode eq 'multi'}">
					<td><c:forEach var="item" items="${typeEnum}">
							<c:if test="${item.key eq t.type && item.key eq 1}">
								<input type="checkbox" class="checkboxes" disabled />
							</c:if>
							<c:if test="${item.key eq t.type && item.key eq 2}">
								<input type="checkbox" class="checkboxes" value="${t.path}"
									data-name="${t.name}" />
							</c:if>
						</c:forEach></td>
				</c:if>
				<td>${status.index+1}</td>
				<td><c:forEach var="item" items="${typeEnum}">
						<c:if test="${item.key eq t.type && item.key eq 1}">
							<a href="javascript:;"
								onclick="$.FileManageController.selectFileJump('${t.path}');"><i
								class="fa fa-folder"></i> ${t.name}</a>
						</c:if>
						<c:if test="${item.key eq t.type && item.key eq 2}">
							<i class="fa fa-file"></i>
							<c:choose>
								<c:when test="${param.selectMode eq 'multi'}">${t.name}</c:when>
								<c:otherwise>
									<a href="javascript:;"
										onclick="$.FileManageController.selectFile('${param.selectMode}','${param.selectParam}','${t.name}','${t.path}');">${t.name}</a>
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:forEach></td>
				<td><tags:type value='${t.type}' /></td>
				<td>${t.sizeS}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<script type="text/javascript">
	$.FileManageController.init('${formId}');
	$.FileManageController.keypress({
		containerId : '${containerId}',
		formId : '${formId}'
	});
</script>
