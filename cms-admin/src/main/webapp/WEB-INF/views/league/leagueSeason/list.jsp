<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>赛季管理
					</div>
					<div class="actions">
						<a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.LeagueSeasonController.toEdit('${ctx}/league/leagueSeason/add')">
							<i class="fa fa-plus"></i>增加赛季
						</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/league/leagueSeason/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">管理名称: <input type="text"
												name="search_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_name__LIKE_S}">
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
											<label class="control-label">&#12288;关键字: <input
												type="text" name="search_keyword__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_keyword__LIKE_S}">
											</label>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">开赛时间: <input type="text"
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
										</div>
										<div class="col-md-4">
											<label class="control-label">&#12288;&#12288;&#12288;ID:
												<input type="text" name="search_id__EQ_L"
												class="form-control input-small input-inline"
												value="${param.search_id__EQ_L}">
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
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="name">管理名称</th>
								<th class="sorting" abbr="tag">TAG</th>
								<th class="sorting" abbr="keyword">关键字</th>
								<th class="sorting" abbr="beginTime">开赛时间</th>
								<th class="sorting" abbr="status">状态</th>
								<th class="sorting" abbr="channelId">频道</th>
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="name">管理名称</th>
								<th class="sorting" abbr="tag">TAG</th>
								<th class="sorting" abbr="keyword">关键字</th>
								<th class="sorting" abbr="beginTime">开赛时间</th>
								<th class="sorting" abbr="status">状态</th>
								<th class="sorting" abbr="channelId">频道</th>
								<th>操作</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="t" varStatus="status">
								<tr>
									<td>${status.index+1}</td>
									<td>${t.id}</td>
									<td><a href="javascript:;"
										onclick="$.LeagueSeasonController.detail('${t.id}');">${t.name}</a></td>
									<td>${t.tag}</td>
									<td>${t.keyword}</td>
									<td><fmt:formatDate value="${t.beginTime}"
											pattern="yyyy-MM-dd HH:mm" /></td>
									<td><tags:status value='${t.status}' /></td>
									<td><c:forEach var="item" items="${channelList}">
											<c:if test="${! empty t.channelId && item.id == t.channelId}"> ${item.name}</c:if>
										</c:forEach></td>
									<td><c:set var="statusMethodName"
											value="${fns:getStatusMothodName(t.status)}" />
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.LeagueSeasonController.toChangeStatus('${ctx}/league/leagueSeason/${t.id}/status','${t.name}','${statusMethodName}');">
											<i class="fa fa-pencil"></i>${statusMethodName}
										</button>
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.LeagueSeasonController.toEdit('${ctx}/league/leagueSeason/${t.id}/edit',${t.id});">
											<i class="fa fa-edit"></i>修改
										</button> <tags:deleteByStatus value='${t.status}'
											onclick="$.LeagueSeasonController.toDelete('${ctx}/league/leagueSeason/${t.id}/delete','${t.name}');" />
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.LeagueSeasonController.showDetail(${t.id});">
											<i class="fa fa-gear"></i>管理赛事
										</button>
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.LeagueSeasonController.showClubList(${t.id});">
											<i class="fa fa-gear"></i>关联俱乐部
										</button>
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.LeagueSeasonController.showStarList(${t.id});">
											<i class="fa fa-gear"></i>榜单管理
										</button>
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.TrailerController.showProgramList(${t.id}, 12, '${t.name}', '赛季管理');">
											<i class="fa fa-gear"></i>相关节目
										</button> <c:if test='${!empty currentSiteCode}'>
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.LeagueMatchController.toEdit('${ctx}/league/leagueSeason/${t.id}/code');">
												<i class="fa fa-eye"></i>代码输入
											</button>
										</c:if></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

					<tags:pageInfo />

				</div>
			</div>
		</div>
	</div>
</div>
<!-- END CONTENT BODY -->

<tags:contentModal />

<script>
    $(".table-actions-wrapper-condition").keypress(function (e) {
        var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
        if (keyCode == 13) {
        	$.Page.queryByForm();
        	return false;
        }
    });
</script>
