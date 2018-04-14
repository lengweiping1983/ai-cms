<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="table-container">
	<div class="table-actions-wrapper-condition">
		<form action="${ctx}/live/schedule/selectSchedule" id="selectSchedule">
			<div class="row">
				<div class="col-md-12">
					<div class="col-md-4">
						<label class="control-label">中央频道代码: <input type="text"
							class="form-control input-small input-inline"
							name="search_channel_code__LIKE_S"
							value="${param.search_channel_code__LIKE_S}">
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">中央频道名称: <input type="text"
							name="search_channel_name__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_channel_name__LIKE_S}">
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">节目名称: <input type="text"
							name="search_programName__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_programName__LIKE_S}">
						</label>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="col-md-4">
						<label class="control-label">&#12288;&#12288;开始时间: <input type="text"
							class="form-control input-small-ext input-inline date date-picker"
							id="search_beginTime__GE_D" name="search_beginTime__GE_D"
							value="${param.search_beginTime__GE_D}"> <span class="">到</span>
							<input type="text"
							class="form-control input-small-ext input-inline date date-picker"
							id="search_beginTime__LE_D" name="search_beginTime__LE_D"
							value="${param.search_beginTime__LE_D}">
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">&#12288;&#12288;&#12288;&#12288;状态: <select
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
						<div style="float: right">
							<button class="btn btn-default btn-sm btn-outline green"
								type="button"
								onclick="$.Page.queryByForm({containerId: 'content_list_modal_container_body', formId: 'selectSchedule'});">
								<i class="fa fa-search"></i> 查询
							</button>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
<div class="dataTables_wrapper no-footer">
	<table class="table dataTable table-striped table-bordered table-hover">
		<thead>
			<tr>
				<th class="sorting" abbr="channel.code">中央频道代码</th>
				<th class="sorting" abbr="channel.name">中央频道名称</th>
				<th class="sorting" abbr="programName">节目名称</th>
				<th class="sorting" abbr="beginTime">开始时间</th>
				<th class="sorting" abbr="duration">时长(分钟)</th>
				<th class="sorting" abbr="status">状态</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<th class="sorting" abbr="channel.code">中央频道代码</th>
				<th class="sorting" abbr="channel.name">中央频道名称</th>
				<th class="sorting" abbr="programName">节目名称</th>
				<th class="sorting" abbr="beginTime">开始时间</th>
				<th class="sorting" abbr="duration">时长(分钟)</th>
				<th class="sorting" abbr="status">状态</th>
			</tr>
		</tfoot>
		<tbody>
			<c:forEach items="${page.content}" var="t">
				<tr>
					<td><a href="javascript:;"
						onclick="$.ScheduleController.selectSchedule('${t.id}','${t.programName}','${t.beginTime}','${t.duration}','${t.channel.id}','${t.channel.name}','${t.channel.code}');">${t.channel.code}</a></td>
					<td><a href="javascript:;"
						onclick="$.ScheduleController.selectSchedule('${t.id}','${t.programName}','${t.beginTime}','${t.duration}','${t.channel.id}','${t.channel.name}','${t.channel.code}');">${t.channel.name}</a></td>
					<td><a href="javascript:;"
						onclick="$.ScheduleController.selectSchedule('${t.id}','${t.programName}','${t.beginTime}','${t.duration}','${t.channel.id}','${t.channel.name}','${t.channel.code}');">${t.programName}</a></td>
					<td><fmt:formatDate value="${t.beginTime}"
							pattern="yyyy-MM-dd HH:mm" /></td>
					<td>${t.duration}</td>
					<td><tags:status value='${t.status}' /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<tags:pageInfo containerId="content_list_modal_container_body"
		formId="selectSchedule" />
</div>

<script>
	$(".table-actions-wrapper-condition").keypress(function(e) {
		var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
		if (keyCode == 13) {
			$.Page.queryByForm({
				containerId : 'content_list_modal_container_body',
				formId : 'selectSchedule'
			});
			return false;
		}
	});
</script>
