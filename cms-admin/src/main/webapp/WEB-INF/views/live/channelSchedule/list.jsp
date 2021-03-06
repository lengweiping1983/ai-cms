<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>[${channel.name}]节目管理
					</div>
					<div class="actions">
						<a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.ChannelScheduleController.toEdit('${ctx}/live/${channel.id}/schedule/add')">
							<i class="fa fa-plus"></i>增加节目
						</a> <a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.common.ajaxLoadLastContent()"> <i
							class="fa fa-reply"></i> 返回到中央频道管理
						</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/live/${channel.id}/schedule/">
								<input type="hidden" name="search_channelId__EQ_L"
									value="${param.search_channelId__EQ_L}" />
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">节目名称: <input type="text"
												name="search_programName__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_programName__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">开始时间: <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_beginTime__GE_D" name="search_beginTime__GE_D"
												value="${param.search_beginTime__GE_D}"> <span
												class="">到</span> <input type="text"
												class="form-control input-small-ext input-inline date date-picker"
												id="search_beginTime__LE_D" name="search_beginTime__LE_D"
												value="${param.search_beginTime__LE_D}">
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
								<th><div class="checker">
										<span class=""><input type="checkbox"
											class="group-checkable" data-set="#content_list .checkboxes" /></span>
									</div></th>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="programName">节目名称</th>
								<th class="sorting" abbr="beginTime">开始时间</th>
								<th class="sorting" abbr="duration">时长(分钟)</th>
								<th class="sorting" abbr="status">状态</th>
								<c:if test="${fns:isInjectionLive()}">
									<th>分发状态</th>
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
								<th class="sorting" abbr="programName">节目名称</th>
								<th class="sorting" abbr="beginTime">开始时间</th>
								<th class="sorting" abbr="duration">时长(分钟)</th>
								<th class="sorting" abbr="status">状态</th>
								<c:if test="${fns:isInjectionLive()}">
									<th>分发状态</th>
								</c:if>
								<th>操作</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="t" varStatus="status">
								<tr>
									<td><div class="checker">
											<span class=""><input type="checkbox"
												class="checkboxes" value="${t.id}" data-name="${t.programName}" /></span>
										</div></td>
									<td>${status.index+1}</td>
									<td>${t.id}</td>
									<td>${t.programName}</td>
									<td><fmt:formatDate value="${t.beginTime}"
											pattern="yyyy-MM-dd HH:mm" /></td>
									<td>${t.duration}</td>
									<td><tags:status value='${t.status}' /></td>
									<c:if test="${fns:isInjectionLive()}">
										<td><tags:injectionStatus value='${t.injectionStatus}' /></td>
									</c:if>
									<td><c:set var="statusMethodName"
											value="${fns:getStatusMothodName(t.status)}" />
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.ChannelScheduleController.toChangeStatus('${ctx}/live/${channel.id}/schedule/${t.id}/status','${t.programName}','${statusMethodName}');">
											<i class="fa fa-pencil"></i>${statusMethodName}
										</button>
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.ChannelScheduleController.toEdit('${ctx}/live/${channel.id}/schedule/${t.id}/edit',${t.id});">
											<i class="fa fa-edit"></i>修改
										</button> <tags:deleteByStatus value='${t.status}'
											onclick="$.ChannelScheduleController.toDelete('${ctx}/live/${channel.id}/schedule/${t.id}/delete','${t.programName}');" />
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

					<tags:pageInfo />
					<div class="modal-footer">
						<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.ScheduleController.toBatchOnline('${ctx}/live/${channel.id}/schedule/batchChangeStatus', 1);">
							<i class="fa fa-edit"></i>批量上线
						</button>

						<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.ScheduleController.toBatchOffline('${ctx}/live/${channel.id}/schedule/batchChangeStatus', 1);">
							<i class="fa fa-edit"></i>批量下线
						</button>

						<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.ScheduleController.toBatchDelete('${ctx}/live/${channel.id}/schedule/batchDelete', 1);">
							<i class="fa fa-remove"></i>批量删除
						</button>
					</div>

					<c:if test="${fns:isInjectionLive()}">
						<div class="modal-footer">
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.ScheduleController.batchTo('${ctx}/live/schedule/batchInjection','8');">
								<i class="fa fa-edit"></i>批量分发
							</button>
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.ScheduleController.batchTo('${ctx}/live/schedule/batchOutInjection','8');">
								<i class="fa fa-edit"></i>批量回收
							</button>
							<button class="btn btn-default btn-sm btn-outline green"
								onclick="$.ScheduleController.toResetInjectionStatus('${ctx}/live/schedule/resetInjectionStatus','8');">
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
	$.ScheduleController.init();
	if (jQuery().datepicker) {
	    $('.date-picker').datepicker({
	        rtl: App.isRTL(),
	        orientation: "left",
	        format: 'yyyy-mm-dd',
	        language: 'zh-CN',
	        autoclose: true
	    });
	}
    $(".table-actions-wrapper-condition").keypress(function (e) {
        var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
        if (keyCode == 13) {
        	$.Page.queryByForm();
        	return false;
        }
    });
</script>
