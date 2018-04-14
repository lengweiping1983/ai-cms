<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>[${leagueSeason.name}]俱乐部列表
					</div>
					<div class="actions">
						<a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.LeagueSeasonClubController.toEdit('${ctx}/league/${leagueSeason.id}/leagueSeasonClub/add','${leagueSeasonClub.id}')">
							<i class="fa fa-plus"></i>增加关联俱乐部
						</a> <a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.common.ajaxLoadLastContent()"> <i
							class="fa fa-reply"></i> 返回到赛季管理
						</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/league/${leagueSeason.id}/leagueSeasonClub/">
								<input type="hidden" name="search_leagueSeasonId__EQ_L"
									value="${param.search_leagueSeasonId__EQ_L}" />
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">俱乐部名称: <input
												type="text" name="search_itemName__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_itemName__LIKE_S}">
											</label>
										</div>
										<div class="col-md-8">
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
					<div class="dataTables_wrapper no-footer">
						<table
							class="table dataTable table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th class="sorting" abbr="itemName">俱乐部名称</th>
									<th class="sorting" abbr="itemStatus">俱乐部状态</th>
									<th class="sorting" abbr="winNum">胜</th>
									<th class="sorting" abbr="flatNum">平</th>
									<th class="sorting" abbr="loseNum">输</th>
									<th class="sorting" abbr="enterNum">进球</th>
									<th class="sorting" abbr="pointNum">点球</th>
									<th class="sorting" abbr="fumbleNum">失球</th>
									<th class="sorting" abbr="creditNum">积分</th>
									<th class="sorting" abbr="sortIndex">排序值</th>
									<th>操作</th>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<th class="sorting" abbr="itemName">俱乐部名称</th>
									<th class="sorting" abbr="itemStatus">俱乐部状态</th>
									<th class="sorting" abbr="winNum">胜</th>
									<th class="sorting" abbr="flatNum">平</th>
									<th class="sorting" abbr="loseNum">输</th>
									<th class="sorting" abbr="enterNum">进球</th>
									<th class="sorting" abbr="pointNum">点球</th>
									<th class="sorting" abbr="fumbleNum">失球</th>
									<th class="sorting" abbr="creditNum">积分</th>
									<th class="sorting" abbr="sortIndex">排序值</th>
									<th>操作</th>
								</tr>
							</tfoot>
							<tbody>
								<c:forEach items="${page.content}" var="t">
									<tr>
										<td><a href="javascript:;"
											onclick="$.LeagueSeasonClubController.detail('${t.itemType}','${t.itemId}');">${t.itemName}</a></td>
										<td><tags:status value='${t.itemStatus}' /></td>
										<td>${t.winNum}</td>
										<td>${t.flatNum}</td>
										<td>${t.loseNum}</td>
										<td>${t.enterNum}</td>
										<td>${t.pointNum}</td>
										<td>${t.fumbleNum}</td>
										<td>${t.creditNum}</td>
										<td>${t.sortIndex}</td>
										<td>
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.LeagueSeasonClubController.toEdit('${ctx}/league/${leagueSeason.id}/leagueSeasonClub/${t.id}/edit',${t.id});">
												<i class="fa fa-edit"></i>修改
											</button>
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.LeagueSeasonClubController.toDelete('${ctx}/league/${leagueSeason.id}/leagueSeasonClub/${t.id}/delete','${t.itemName}');">
												<i class="fa fa-remove"></i>删除
											</button>
										</td>
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
