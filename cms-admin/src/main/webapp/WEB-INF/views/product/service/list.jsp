<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>服务管理
					</div>
					<div class="actions">
						<a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.ServiceController.toEdit('${ctx}/service/service/add')">
							<i class="fa fa-plus"></i>增加服务
						</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/service/service/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">服务类型: <select
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
											<label class="control-label">服务名称: <input
												type="text" name="search_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_name__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">&#12288;&#12288;TAG: <input
												type="text" name="search_tags__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_tags__LIKE_S}">
											</label>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">&#12288;关键字: <input
												type="text" name="search_keyword__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_keyword__LIKE_S}">
											</label>
										</div>
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
											<label class="control-label">分发状态: <select
												name="search_injectionStatus__EQ_I"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${injectionStatusEnum}">
														<option value="${item.key}"
															<c:if test="${! empty param.search_injectionStatus__EQ_I && item.key eq param.search_injectionStatus__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
											</select>
											</label>
											<div style="float: right">
												<button class="btn btn-default btn-sm btn-outline green"
													type="button" onclick="$.Page.queryByForm();">
													<i class="fa fa-search"></i> 查询
												</button>
											</div>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
					<table
						class="table dataTable table-striped table-bordered table-hover table-checkable"
						id="content_list">
						<thead>
							<tr>
								<th width="6%"><div class="checker">
										<span class=""><input type="checkbox"
											class="group-checkable" data-set="#content_list .checkboxes" /></span>
									</div></th>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="type">服务类型</th>
								<th class="sorting" abbr="name">服务名称</th>
								<th class="sorting" abbr="tags">TAG</th>
								<th class="sorting" abbr="keyword">关键字</th>
								<th class="sorting" abbr="status">上线状态</th>
								<c:if test="${fns:isInjectionService()}">
									<th class="sorting" abbr="injectionStatus">分发状态</th>
								</c:if>
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th><div class="checker">
										<span class=""><input type="checkbox"
											class="group-checkable" data-set="#content_list .checkboxes" /></span>
									</div></th>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="type">服务类型</th>
								<th class="sorting" abbr="name">服务名称</th>
								<th class="sorting" abbr="tags">TAG</th>
								<th class="sorting" abbr="keyword">关键字</th>
								<th class="sorting" abbr="status">上线状态</th>
								<c:if test="${fns:isInjectionService()}">
									<th class="sorting" abbr="injectionStatus">分发状态</th>
								</c:if>
								<th>操作</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="t" varStatus="status">
								<tr>
									<td><div class="checker">
											<span class=""><input type="checkbox"
												class="checkboxes" value="${t.id}" data-name="${t.name}" /></span>
										</div></td>
									<td>${status.index+1}</td>
									<td>${t.id}</td>
									<td><tags:type value='${t.type}' /></td>
									<td>${t.name}</td>
									<td>${t.tag}</td>
									<td>${t.keyword}</td>
									<td><tags:status value='${t.status}' /></td>
									<c:if test="${fns:isInjectionService()}">
										<td><tags:injectionStatus value='${t.injectionStatus}' /></td>
									</c:if>
									<td>
										<c:set var="statusMethodName"
											value="${fns:getStatusMothodName(t.status)}" />
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.ServiceController.toChangeStatus('${ctx}/service/service/${t.id}/status','${t.name}','${statusMethodName}');">
											<i class="fa fa-pencil"></i>${statusMethodName}
										</button>
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.ServiceController.toEdit('${ctx}/service/service/${t.id}/edit',${t.id});">
											<i class="fa fa-edit"></i>修改
										</button> <c:forEach var="item" items="${statusEnum}">
											<c:if test="${item.key eq t.status && item.key eq 0}">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.ServiceController.toDelete('${ctx}/service/service/${t.id}/delete','${t.name}');">
													<i class="fa fa-remove"></i>删除
												</button>
											</c:if>
											<c:if test="${item.key eq t.status && item.key eq 1}">
											</c:if>
										</c:forEach>
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.ServiceController.showDetail(${t.id});">
											<i class="fa fa-gear"></i>管理服务项
										</button>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

					<tags:pageInfo />

					<c:if test="${fns:isInjectionService()}">
						<div class="modal-footer">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.ServiceController.batchTo('${ctx}/service/service/batchInjection','5');">
								<i class="fa fa-edit"></i>批量分发
							</button>
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.ServiceController.batchTo('${ctx}/service/service/batchOutInjection','5');">
								<i class="fa fa-edit"></i>批量回收
							</button>
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.ServiceController.toResetInjectionStatus('${ctx}/service/service/resetInjectionStatus','5');">
								<i class="fa fa-edit"></i>重置分发状态
							</button>
						</div>
					</c:if>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END CONTENT BODY -->

<tags:contentModal />

<script type="text/javascript">
	$.ServiceController.init();
	if (jQuery().datepicker) {
		$('.date-picker').datepicker({
			rtl : App.isRTL(),
			orientation : "left",
			format : 'yyyy-mm-dd',
			language : 'zh-CN',
			autoclose : true
		});
	}
	$(".table-actions-wrapper-condition").keypress(function(e) {
		var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
		if (keyCode == 13) {
			$.Page.queryByForm();
			return false;
		}
	});
</script>
