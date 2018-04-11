<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>活动管理
					</div>
					<div class="actions">
						<a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.ActivityController.toEdit('${ctx}/activity/activity/add')">
							<i class="fa fa-plus"></i>增加活动
						</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/activity/activity/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">活动代码: <input type="text"
												class="form-control input-small input-inline"
												name="search_code__LIKE_S"
												value="${param.search_code__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">活动名称: <input type="text"
												name="search_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_name__LIKE_S}">
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
					<div class="dataTables_wrapper no-footer">
						<table
							class="table dataTable table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th>序号</th>
									<th class="sorting" abbr="id">活动id</th>
									<th class="sorting" abbr="code">活动代码</th>
									<th class="sorting" abbr="name">活动名称</th>
									<th class="sorting" abbr="beginTime">开始时间</th>
									<th class="sorting" abbr="endTime">结束时间</th>
									<th class="sorting" abbr="status">状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<th>序号</th>
									<th class="sorting" abbr="id">活动id</th>
									<th class="sorting" abbr="code">活动代码</th>
									<th class="sorting" abbr="name">活动名称</th>
									<th class="sorting" abbr="beginTime">开始时间</th>
									<th class="sorting" abbr="endTime">结束时间</th>
									<th class="sorting" abbr="status">状态</th>
									<th>操作</th>
								</tr>
							</tfoot>
							<tbody>
								<c:forEach items="${page.content}" var="t" varStatus="status">
									<tr>
										<td>${status.index+1}</td>
										<td>${t.id}</td>
										<td>${t.code}</td>
										<td>${t.name}</td>
										<td><fmt:formatDate value="${t.beginTime}"
												pattern="yyyy-MM-dd HH:mm" /></td>
										<td><fmt:formatDate value="${t.endTime}"
												pattern="yyyy-MM-dd HH:mm" /></td>
										<td><tags:status value='${t.status}' /></td>		
										<td>
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.ActivityController.toEdit('${ctx}/activity/activity/${t.id}/edit',${t.id});">
												<i class="fa fa-edit"></i>修改
											</button>
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.ActivityController.toDelete('${ctx}/activity/activity/${t.id}/delete','${t.name}');">
												<i class="fa fa-remove"></i>删除
											</button>
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.ActivityController.toUser('${ctx}/activity/activityUser',${t.id},'${t.name}');">
												<i class="fa fa-eye"></i>查看参与用户
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
