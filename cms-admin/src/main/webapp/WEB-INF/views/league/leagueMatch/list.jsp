<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>赛事管理
					</div>
					<div class="actions">
						<a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.LeagueMatchController.toEdit('${ctx}/league/leagueMatch/add')">
							<i class="fa fa-plus"></i>增加赛事
						</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/league/leagueMatch/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">内容类型: <select
												name="search_sportContentType__EQ_I"
												class="form-control input-small input-inline">
													<option value="">全部</option>
													<c:forEach var="item" items="${sportContentTypeEnum}">
														<option value="${item.key}"
															<c:if test="${! empty param.search_sportContentType__EQ_I && item.key eq param.search_sportContentType__EQ_I}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
											</select>
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">所属赛季: <input type="text"
												name="search_leagueSeason_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_leagueSeason_name__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">管理名称: <input type="text"
												name="search_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_name__LIKE_S}">
											</label>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">&#12288;&#12288;赛程: <input
												type="text" name="search_leagueIndex__EQ_I"
												class="form-control input-small input-inline"
												value="${param.search_leagueIndex__EQ_I}">
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
											<label class="control-label">&#12288;&#12288;主场: <input
												type="text" class="form-control input-small input-inline"
												name="search_homeName__LIKE_S"
												value="${param.search_homeName__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">&#12288;&#12288;客场: <input
												type="text" class="form-control input-small input-inline"
												name="search_guestName__LIKE_S"
												value="${param.search_guestName__LIKE_S}">
											</label>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
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
										</div>
										<div class="col-md-4">
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
								<th>
									<div class="checker">
										<span class=""><input type="checkbox"
											class="group-checkable" data-set="#content_list .checkboxes" /></span>
									</div>
								</th>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="sportContentType">内容类型</th>
								<th class="sorting" abbr="leagueSeason.name">所属赛季</th>
								<th class="sorting" abbr="name">管理名称</th>
								<th class="sorting" abbr="leagueIndex">赛程</th>
								<th class="sorting" abbr="tag">TAG</th>
								<th class="sorting" abbr="keyword">关键字</th>
								<th class="sorting" abbr="beginTime">开赛时间</th>
								<th class="sorting" abbr="duration">时长(分钟)</th>
								<th class="sorting" abbr="homeName">主场</th>
								<th class="sorting" abbr="guestName">客场</th>
								<th class="sorting" abbr="homeScore">主场得分</th>
								<th class="sorting" abbr="guestScore">客场得分</th>
								<th class="sorting" abbr="status">状态</th>
								<th class="sorting" abbr="channelId">频道</th>
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th>
									<div class="checker">
										<span class=""><input type="checkbox"
											class="group-checkable" data-set="#content_list .checkboxes" /></span>
									</div>
								</th>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="sportContentType">内容类型</th>
								<th class="sorting" abbr="leagueSeason.name">所属赛季</th>
								<th class="sorting" abbr="name">管理名称</th>
								<th class="sorting" abbr="leagueIndex">赛程</th>
								<th class="sorting" abbr="tag">TAG</th>
								<th class="sorting" abbr="keyword">关键字</th>
								<th class="sorting" abbr="beginTime">开赛时间</th>
								<th class="sorting" abbr="duration">时长(分钟)</th>
								<th class="sorting" abbr="homeName">主场</th>
								<th class="sorting" abbr="guestName">客场</th>
								<th class="sorting" abbr="homeScore">主场得分</th>
								<th class="sorting" abbr="guestScore">客场得分</th>
								<th class="sorting" abbr="status">状态</th>
								<th class="sorting" abbr="channelId">频道</th>
								<th>操作</th>
							</tr>
						</tfoot>
						<tbody>
							<c:set var="currentTime" value="<%=System.currentTimeMillis()%>"></c:set>
							<c:forEach items="${page.content}" var="t" varStatus="status">
								<c:set var="tr_style" value='' />
								<c:if
									test="${t.status eq 1 and t.splitProgram ne 1 and empty t.channelId}">
									<c:set var="tr_style" value='style="color: blue;"' />
								</c:if>
								<c:if
									test="${t.status eq 1 and t.splitProgram ne 1 and (currentTime - t.beginTime.time) > 2 * 24 * 3600 * 1000}">
									<c:set var="tr_style" value='style="color: red;"' />
								</c:if>
								<tr ${tr_style}>
									<td>
										<div class="checker">
											<span class=""><input type="checkbox"
												class="checkboxes" value="${t.id}" data-name="${t.name}" /></span>
										</div>
									</td>
									<td>${status.index+1}</td>
									<td><a href="javascript:;"
										onclick="$.LeagueMatchController.detailJson('${t.id}');">${t.id}</a></td>
									<td><c:forEach var="item" items="${sportContentTypeEnum}">
											<c:if test="${item.key eq t.sportContentType}">${item.value}</c:if>
										</c:forEach></td>
									<td><c:if test="${t.leagueSeason != null}">${t.leagueSeason.name}</c:if></td>
									<td><a href="javascript:;"
										onclick="$.LeagueMatchController.detail('${t.id}');">${t.name}</a></td>
									<td>${t.leagueIndex}</td>
									<td>${t.tag}</td>
									<td>${t.keyword}</td>
									<td><fmt:formatDate value="${t.beginTime}"
											pattern="yyyy-MM-dd HH:mm" /></td>
									<td>${t.duration}</td>
									<td>${t.homeName}</td>
									<td>${t.guestName}</td>
									<td>${t.homeScore}</td>
									<td>${t.guestScore}</td>
									<td><tags:status value='${t.status}' /></td>
									<td><c:forEach var="item" items="${channelList}">
											<c:if test="${! empty t.channelId && item.id == t.channelId}"> ${item.name}</c:if>
										</c:forEach></td>
									<td><c:set var="statusMethodName"
											value="${fns:getStatusMothodName(t.status)}" />
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.LeagueMatchController.toChangeStatus('${ctx}/league/leagueMatch/${t.id}/status','${t.name}','${statusMethodName}');">
											<i class="fa fa-pencil"></i>${statusMethodName}
										</button>
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.LeagueMatchController.toEdit('${ctx}/league/leagueMatch/${t.id}/edit',${t.id});">
											<i class="fa fa-edit"></i>修改
										</button> <tags:deleteByStatus value='${t.status}'
											onclick="$.LeagueMatchController.toDelete('${ctx}/league/leagueMatch/${t.id}/delete','${t.name}');" />
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.TrailerController.showProgramList(${t.id}, 13, '${t.name}', '赛事管理');">
											<i class="fa fa-gear"></i>相关节目
										</button> <c:if test='${!empty currentSiteCode}'>
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.LeagueMatchController.toEdit('${ctx}/league/leagueMatch/${t.id}/code');">
												<i class="fa fa-eye"></i>代码输入
											</button>
										</c:if></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

					<tags:pageInfo />

					<div class="modal-footer">
						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.LeagueMatchController.toBatchChangeMetadata('${ctx}/league/leagueMatch/batchChangeMetadata', 1);">
							<i class="fa fa-edit"></i>批量修改元数据
						</button>

						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.LeagueMatchController.batchTo('${ctx}/album/album/batchToAlbum', 13);">
							<i class="fa fa-pencil"></i>批量编排到专题
						</button>
						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.LeagueMatchController.batchTo('${ctx}/widget/widget/batchToWidget', 13);">
							<i class="fa fa-pencil"></i>批量编排到推荐位
						</button>
						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.LeagueMatchController.batchTo('${ctx}/category/category/batchToCategory', 13);">
							<i class="fa fa-pencil"></i>批量编排到栏目
						</button>

						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.LeagueMatchController.toBatchOnline('${ctx}/league/leagueMatch/batchChangeStatus', 1);">
							<i class="fa fa-edit"></i>批量上线
						</button>

						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.LeagueMatchController.toBatchOffline('${ctx}/league/leagueMatch/batchChangeStatus', 1);">
							<i class="fa fa-edit"></i>批量下线
						</button>

						<button class="btn btn-default btn-sm btn-outline green"
							onclick="$.LeagueMatchController.toBatchDelete('${ctx}/league/leagueMatch/batchDelete', 1);">
							<i class="fa fa-remove"></i>批量删除
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END CONTENT BODY -->

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
				<button type="button" class="btn btn-outline green"
					style="display: none" id="content_list_modal_container_ok">
					<i class="fa fa-check"></i>确定
				</button>
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>

<script>
$.LeagueMatchController.init();
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
