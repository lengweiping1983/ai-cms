<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>频道配置管理
					</div>
					<div class="actions">
						<shiro:hasPermission name="live:channelConfig:add">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.ChannelConfigController.toEdit('${ctx}/live/channelConfig/add')">
								<i class="fa fa-plus"></i>增加频道配置
							</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/live/channelConfig/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">中央频道代码: <input
												type="text" class="form-control input-small input-inline"
												name="search_channel_code__LIKE_S"
												value="${param.search_channel_code__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">中央频道名称: <input
												type="text" name="search_channel_name__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_channel_name__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">本地频道代码: <input
												type="text" name="search_partnerChannelCode__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_partnerChannelCode__LIKE_S}">
											</label>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">本地频道名称: <input
												type="text" name="search_partnerChannelName__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_partnerChannelName__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">&#12288;本地频道号: <input
												type="text" name="search_partnerChannelNumber__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_partnerChannelNumber__LIKE_S}">
											</label>
										</div>

										<div class="col-md-4">
											<label class="control-label">&#12288;&#12288;&#12288;&#12288;状态:
												<select name="search_status__EQ_I"
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
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="channel.code">中央频道代码</th>
								<th class="sorting" abbr="channel.name">中央频道名称</th>
								<th class="sorting" abbr="providerType">本地提供商</th>
								<th class="sorting" abbr="partnerChannelCode">本地频道代码</th>
								<th class="sorting" abbr="partnerChannelName">本地频道名称</th>
								<th class="sorting" abbr="partnerChannelNumber">本地频道号</th>
								<th class="sorting" abbr="status">状态</th>
								<th class="sorting" abbr="sortIndex">排序值</th>
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="channel.code">中央频道代码</th>
								<th class="sorting" abbr="channel.name">中央频道名称</th>
								<th class="sorting" abbr="providerType">本地提供商</th>
								<th class="sorting" abbr="partnerChannelCode">本地频道代码</th>
								<th class="sorting" abbr="partnerChannelName">本地频道名称</th>
								<th class="sorting" abbr="partnerChannelNumber">本地频道号</th>
								<th class="sorting" abbr="status">状态</th>
								<th class="sorting" abbr="sortIndex">排序值</th>
								<th>操作</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="t" varStatus="status">
								<tr>
									<td>${status.index+1}</td>
									<td>${t.id}</td>
									<td><c:if test="${t.channel != null}">${t.channel.code}</c:if></td>
									<td><c:if test="${t.channel != null}">${t.channel.name}</c:if></td>
									<td><c:forEach var="item" items="${providerTypeEnum}">
											<c:if test="${item.key eq t.providerType}">
													${item.value}
													</c:if>
										</c:forEach></td>
									<td>${t.partnerChannelCode}</td>
									<td>${t.partnerChannelName}</td>
									<td>${t.partnerChannelNumber}</td>
									<td><tags:status value='${t.status}' /></td>
									<td>${t.sortIndex}</td>
									<td><c:set var="statusMethodName"
											value="${fns:getStatusMothodName(t.status)}" />
										<shiro:hasPermission name="live:channelConfig:status">
											<button class="btn btn-default btn-sm btn-outline green"
												onclick="$.ChannelConfigController.toChangeStatus('${ctx}/live/channelConfig/${t.id}/status','${t.partnerChannelName}','${statusMethodName}');">
												<i class="fa fa-pencil"></i>${statusMethodName}
											</button>
										</shiro:hasPermission>
										<shiro:hasPermission name="live:channelConfig:edit">
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.ChannelConfigController.toEdit('${ctx}/live/channelConfig/${t.id}/edit',${t.id});">
											<i class="fa fa-edit"></i>修改
										</button>
										</shiro:hasPermission>
										<shiro:hasPermission name="live:channelConfig:delete">
										<tags:deleteByStatus value='${t.status}'
											onclick="$.ChannelConfigController.toDelete('${ctx}/live/channelConfig/${t.id}/delete','${t.partnerChannelName}');" />
										</shiro:hasPermission>
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
<!-- END CONTENT BODY -->

<tags:contentModal />

<script>
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
