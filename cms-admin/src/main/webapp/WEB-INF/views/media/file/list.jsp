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
						<i class="fa fa-globe"></i>媒资文件浏览
					</div>
					<div class="actions"></div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/media/file/">
								<input type="hidden" id="refresh" name="refresh" value="0">
								<div class="row">
									<div class="col-md-7">
										<label class="control-label">文件路径: <input type="text"
											id="path" name="path"
											class="form-control input-xlarge input-inline"
											value="${path}">
										</label>
									</div>
									<div class="col-md-5">
										<label class="control-label">文件名称: <input type="text"
											name="name" class="form-control input-small input-inline"
											value="${param.name}">
										</label>
										<div style="float: right">
											<button class="btn btn-default btn-sm btn-outline green"
												type="button" onclick="$.Page.queryByForm(); return false;">
												<i class="fa fa-search"></i> 查询
											</button>
											<button class="btn btn-default btn-sm btn-outline green"
												type="button"
												onclick="$.FileManageController.refresh(); return false;">
												<i class="fa fa-refresh"></i> 刷新
											</button>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
					<div class="modal-footer">
						<c:forEach var="item" items="${transcodeRequestTypeEnum}">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.FileManageController.toEditRequest('${formId}','${ctx}/transcode/transcodeRequest/add/${item.key}')">
								<i class="fa fa-plus"></i>增加${item.value}
							</button>
						</c:forEach>

						<!-- 						<button class="btn btn-default btn-sm btn-outline green" -->
						<%-- 							onclick="$.FileManageController.toTrimRequest('${ctx}/media/file/trim')"> --%>
						<!-- 							<i class="fa fa-edit"></i>批量去文件名中的特殊字符 -->
						<!-- 						</button> -->

						<%-- 						<shiro:hasPermission name="transcode:file:batchDelete"> --%>
						<!-- 							<button class="btn btn-default btn-sm btn-outline green" -->
						<%-- 								onclick="$.FileManageController.toBatchDelete('${formId}', '${ctx}/media/file/batchDelete');"> --%>
						<!-- 								<i class="fa fa-remove"></i>批量删除 -->
						<!-- 							</button> -->
						<%-- 						</shiro:hasPermission> --%>
					</div>
					<table
						class="table dataTable table-striped table-bordered table-hover table-checkable"
						id="${formId}content_list">
						<thead>
							<tr>
								<th><input type="checkbox" class="group-checkable"
									data-set="#${formId}content_list .checkboxes" /></th>
								<th>序号</th>
								<th>文件名称</th>
								<th>文件类型</th>
								<th>文件大小</th>
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th><input type="checkbox" class="group-checkable"
									data-set="#${formId}content_list .checkboxes" /></th>
								<th>序号</th>
								<th>文件名称</th>
								<th>文件类型</th>
								<th>文件大小</th>
								<th>操作</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${files}" var="t" varStatus="status">
								<tr>
									<td><c:forEach var="item" items="${typeEnum}">
											<c:if test="${item.key eq t.type && item.key eq 1}">
												<input type="checkbox" class="checkboxes" disabled />
											</c:if>
											<c:if test="${item.key eq t.type && item.key eq 2}">
												<input type="checkbox" class="checkboxes" value="${t.path}"
													data-name="${t.name}" />
											</c:if>
										</c:forEach></td>
									<td>${status.index+1}</td>
									<td><c:forEach var="item" items="${typeEnum}">
											<c:if test="${item.key eq t.type && item.key eq 1}">
												<a href="javascript:;"
													onclick="$.FileManageController.jump('${t.path}');"><i
													class="fa fa-folder"></i> ${t.name}</a>
											</c:if>
											<c:if test="${item.key eq t.type && item.key eq 2}">
												<i class="fa fa-file"></i> ${t.name}
											</c:if>
										</c:forEach></td>
									<td><tags:type value='${t.type}' /></td>
									<td>${t.sizeS}</td>
									<td><c:if test="${t.type eq 2}">
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.PlayController.toPreview('${t.path}');">
												<i class="fa fa-eye"></i>预览
											</button>
										</c:if> <c:if test="${t.name ne '..'}">
											<!-- 											<button class="btn btn-default btn-sm btn-outline green" -->
											<%-- 												onclick="$.FileManageController.toRename('${ctx}/media/file/rename?from=${t.path}','${t.path}');"> --%>
											<!-- 												<i class="fa fa-edit"></i>重命名 -->
											<!-- 											</button> -->
											<%-- 											<shiro:hasPermission name="transcode:file:delete"> --%>
											<!-- 											<button class="btn btn-default btn-sm btn-outline green" -->
											<%-- 												onclick="$.FileManageController.toDelete('${ctx}/media/file/delete?path=${t.path}','${t.path}');"> --%>
											<!-- 												<i class="fa fa-remove"></i>删除 -->
											<!-- 											</button> -->
											<%-- 											</shiro:hasPermission> --%>
										</c:if></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

					<div class="modal-footer">
						<c:forEach var="item" items="${transcodeRequestTypeEnum}">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.FileManageController.toEditRequest('${formId}', '${ctx}/transcode/transcodeRequest/add/${item.key}')">
								<i class="fa fa-plus"></i>增加${item.value}
							</button>
						</c:forEach>

						<!-- 						<button class="btn btn-default btn-sm btn-outline green" -->
						<%-- 							onclick="$.FileManageController.toTrimRequest('${ctx}/media/file/trim')"> --%>
						<!-- 							<i class="fa fa-edit"></i>批量去文件名中的特殊字符 -->
						<!-- 						</button> -->

						<%-- 						<shiro:hasPermission name="transcode:file:batchDelete"> --%>
						<!-- 							<button class="btn btn-default btn-sm btn-outline green" -->
						<%-- 								onclick="$.FileManageController.toBatchDelete('${formId}', '${ctx}/media/file/batchDelete');"> --%>
						<!-- 								<i class="fa fa-remove"></i>批量删除 -->
						<!-- 							</button> -->
						<%-- 						</shiro:hasPermission> --%>
					</div>
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
			<div class="modal-footer">
				<button class="btn btn-outline green"
					onclick="$.FileManageController.toBatch('selectFile');"
					id="content_list_modal_container_ok">
					<i class="fa fa-save"></i>确定
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
	$.FileManageController.init("${formId}");
	$.FileManageController.keypress({
		containerId : '${containerId}',
		formId : '${formId}'
	});
</script>
