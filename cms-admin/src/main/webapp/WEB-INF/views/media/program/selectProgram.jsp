<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<c:set var="containerId" value="content_list_container" />
<c:set var="formId" value="selectProgram" />
<div class="table-container">
	<div class="table-actions-wrapper-condition">
		<form action="${ctx}/media/program/selectProgram" id="selectProgram">
			<input type="hidden" name="selectMode" value="${param.selectMode}">
			<input type="hidden" name="selectParam" value="${param.selectParam}">
			<div class="row">
				<div class="col-md-12">
					<div class="col-md-4">
						<label class="control-label">节目类型: <select
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
						<label class="control-label">节目名称: <input type="text"
							name="search_name__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_name__LIKE_S}">
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">&#12288;&#12288;标题: <input
							type="text" name="search_title__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_title__LIKE_S}">
						</label>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="col-md-4">
						<label class="control-label">内容类型: <select
							name="search_contentType__EQ_I"
							class="form-control input-small input-inline">
								<option value="">全部</option>
								<c:forEach var="item" items="${contentTypeEnum}">
									<option value="${item.key}"
										<c:if test="${! empty param.search_contentType__EQ_I && item.key eq param.search_contentType__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
								</c:forEach>
						</select>
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">&#12288;&#12288;TAG: <input
							type="text" name="search_tag__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_tag__LIKE_S}">
						</label>
					</div>
					<div class="col-md-4">
						<label class="control-label">内部标签: <input type="text"
							name="search_internalTag__LIKE_S"
							class="form-control input-small input-inline"
							value="${param.search_internalTag__LIKE_S}">
						</label>
						<tags:searchButton containerId="${containerId}" formId="${formId}"
							toggle="true" />
					</div>
				</div>
			</div>
			<div id="${formId}condition_open_panel"
				<c:if test="${param.condition_open_status != 1}"> style="display: none" </c:if>>
				<div class="row">
					<div class="col-md-12">
						<div class="col-md-4">
							<tags:cpSearch prefix="${formId}" />
						</div>
						<div class="col-md-4">
							<tags:mediaTemplateSearch />
						</div>
						<div class="col-md-4">
							<label class="control-label">媒资状态: <select
								name="search_mediaStatus__BITMASK_I"
								class="form-control input-small input-inline">
									<option value="">全部</option>
									<c:forEach var="item" items="${mediaStatusEnum}">
										<option value="${item.key}"
											<c:if test="${! empty param.search_mediaStatus__BITMASK_I && item.key eq param.search_mediaStatus__BITMASK_I}"> selected="selected" </c:if>>${item.value}</option>
									</c:forEach>
							</select>
							</label>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<div class="col-md-4">
							<label class="control-label">分发状态: <select
								name="search_injectionStatus__INMASK_S"
								class="form-control input-small input-inline">
									<option value="">全部</option>
									<c:forEach var="item" items="${injectionStatusEnum}">
										<option value="${item.key}"
											<c:if test="${! empty param.search_injectionStatus__INMASK_S && item.key eq param.search_injectionStatus__INMASK_S}"> selected="selected" </c:if>>${item.value}</option>
									</c:forEach>
							</select>
							</label>
						</div>
						<div class="col-md-4">
							<label class="control-label">播放代码: <select
								name="search_playCodeStatus__EQ_I"
								class="form-control input-small input-inline">
									<option value="">全部</option>
									<c:forEach var="item" items="${playCodeStatusEnum}">
										<option value="${item.key}"
											<c:if test="${! empty param.search_playCodeStatus__EQ_I && item.key eq param.search_playCodeStatus__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
									</c:forEach>
							</select>
							</label>
						</div>
						<div class="col-md-4">
							<label class="control-label">入库时间: <input type="text"
								class="form-control input-small-ext input-inline date date-picker"
								id="search_storageTime__GE_D" name="search_storageTime__GE_D"
								value="${param.search_storageTime__GE_D}"> <span
								class="">到</span> <input type="text"
								class="form-control input-small-ext input-inline date date-picker"
								id="search_storageTime__LE_D" name="search_storageTime__LE_D"
								value="${param.search_storageTime__LE_D}">
							</label>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
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
							<label class="control-label">上线时间: <input type="text"
								class="form-control input-small-ext input-inline date date-picker"
								id="search_onlineTime__GE_D" name="search_onlineTime__GE_D"
								value="${param.search_onlineTime__GE_D}"> <span class="">到</span>
								<input type="text"
								class="form-control input-small-ext input-inline date date-picker"
								id="search_onlineTime__LE_D" name="search_onlineTime__LE_D"
								value="${param.search_onlineTime__LE_D}">
							</label>
						</div>
						<div class="col-md-4">
							<label class="control-label">&#12288;&#12288;&#12288;ID:
								<input type="text" name="search_id__EQ_L"
								class="form-control input-small input-inline"
								value="${param.search_id__EQ_L}">
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
			<c:if test="${param.selectMode eq 'multi'}">
				<th><input type="checkbox" class="group-checkable"
					data-set="#${formId}content_list .checkboxes" /></th>
			</c:if>
			<th>序号</th>
			<th class="sorting" abbr="id">ID</th>
			<th class="sorting" abbr="type">节目类型</th>
			<th class="sorting" abbr="name">节目名称</th>
			<th class="sorting" abbr="title">标题</th>
			<th class="sorting" abbr="episodeIndex">第几集</th>
			<th class="sorting" abbr="contentType">内容类型</th>
			<th class="sorting" abbr="tag">TAG</th>
			<th class="sorting" abbr="internalTag">内部标签</th>
			<th class="sorting" abbr="cpId">提供商</th>
			<th class="sorting" abbr="templateId">码率</th>
			<th class="sorting" abbr="mediaStatus">媒资状态</th>
			<th class="sorting" abbr="injectionStatus">分发状态</th>
			<th class="sorting" abbr="playCodeStatus">播放代码</th>
			<th class="sorting" abbr="status">上线状态</th>
			<th class="sorting" abbr="storageTime">入库时间</th>
		</tr>
	</thead>
	<tfoot>
		<tr>
			<c:if test="${param.selectMode eq 'multi'}">
				<th><input type="checkbox" class="group-checkable"
					data-set="#${formId}content_list .checkboxes" /></th>
			</c:if>
			<th>序号</th>
			<th class="sorting" abbr="id">ID</th>
			<th class="sorting" abbr="type">节目类型</th>
			<th class="sorting" abbr="name">节目名称</th>
			<th class="sorting" abbr="title">标题</th>
			<th class="sorting" abbr="episodeIndex">第几集</th>
			<th class="sorting" abbr="contentType">内容类型</th>
			<th class="sorting" abbr="tag">TAG</th>
			<th class="sorting" abbr="internalTag">内部标签</th>
			<th class="sorting" abbr="cpId">提供商</th>
			<th class="sorting" abbr="templateId">码率</th>
			<th class="sorting" abbr="mediaStatus">媒资状态</th>
			<th class="sorting" abbr="injectionStatus">分发状态</th>
			<th class="sorting" abbr="playCodeStatus">播放代码</th>
			<th class="sorting" abbr="status">上线状态</th>
			<th class="sorting" abbr="storageTime">入库时间</th>
		</tr>
	</tfoot>
	<tbody>
		<c:forEach items="${page.content}" var="t" varStatus="status">
			<tr>
				<c:if test="${param.selectMode eq 'multi'}">
					<td><input type="checkbox" class="checkboxes"
						id="{'id':'${t.id}','name':'${t.name}','title':'${t.title}','status':'${t.status}'}"
						value="${t.id}" data-name="${t.name}" /></td>
				</c:if>
				<td>${status.index+1}</td>
				<td>${t.id}</td>
				<td><tags:enum className='class="badge badge-success"'
						enumList="${typeEnum}" value="${t.type}" /></td>
				<td><c:choose>
						<c:when test="${param.selectMode eq 'multi'}">
						${t.name}
					</c:when>
						<c:otherwise>
							<a href="javascript:;"
								onclick="$.ProgramController.selectProgram('${param.selectMode}','${param.selectParam}','${t.id}','${t.name}','${t.title}','${t.contentType}','${t.tag}','${t.internalTag}','${t.episodeIndex}','${t.duration}','${t.status}','${t.cpId}','${t.filename}','${t.image1}','${t.image2}','${t.image3}','${t.image4}');">${t.name}</a>
						</c:otherwise>
					</c:choose></td>
				<td>${t.title}</td>
				<td>${t.episodeIndex}</td>
				<td><tags:enum className='class="badge badge-success"'
						enumList="${contentTypeEnum}" value="${t.contentType}" /></td>
				<td><tags:tagView value="${t.tag}" /></td>
				<td><tags:tagView value="${t.internalTag}" /></td>
				<td><tags:cpView value="${t.cpId}" /></td>
				<td><tags:mediaTemplateView value="${t.templateId}" /></td>
				<td>${fns:getMediaStatusDesc(t.mediaStatus)}</td>
				<td><tags:injectionPlatformAndInjectionStatus value="${t.id}" /></td>
				<td><tags:playCode value="${t.playCodeStatus}" /></td>
				<td><tags:status value="${t.status}" /></td>
				<td><fmt:formatDate value="${t.storageTime}"
						pattern="yyyy-MM-dd" /></td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<tags:pageInfo containerId="${containerId}" formId="${formId}" />

<script type="text/javascript">
	$.ProgramController.init('${formId}');
	$.ProgramController.keypress({
		containerId : '${containerId}',
		formId : '${formId}'
	});

	if ('${param.selectMode}' == 'multi') {
		if ($("#content_list_modal_container_ok")[0]) {
			$('#content_list_modal_container_ok').attr("onclick",
					"$.ProgramController.selectCheckedItem('${formId}', 1);");
			$('#content_list_modal_container_ok').show();
		}
	}
</script>
