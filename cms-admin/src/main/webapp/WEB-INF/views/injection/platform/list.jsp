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
						<i class="fa fa-globe"></i>分发平台配置
					</div>
					<div class="actions">
						<c:forEach var="item" items="${injectionDirectionEnum}">
							<a href="javascript:;" class="btn btn-default btn-sm"
								onclick="$.InjectionPlatformController.toEdit('${ctx}/injection/platform/add/${item.key}')">
								<i class="fa fa-plus"></i>增加${item.value}平台
							</a>
						</c:forEach>
					</div>
				</div>
				<div class="portlet-body">
					<div class="table-container">
						<div class="table-actions-wrapper-condition">
							<form action="${ctx}/injection/platform/">
								<div class="row">
									<div class="col-md-12">
										<div class="col-md-4">
											<label class="control-label">平台名称: <input type="text"
												class="form-control input-small input-inline"
												name="search_name__LIKE_S"
												value="${param.search_name__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">CSPID: <input
												type="text" class="form-control input-small input-inline"
												name="search_cspId__LIKE_S"
												value="${param.search_cspId__LIKE_S}">
											</label>
										</div>
										<div class="col-md-4">
											<label class="control-label">LSPID: <input
												type="text" name="search_lspId__LIKE_S"
												class="form-control input-small input-inline"
												value="${param.search_lspId__LIKE_S}">
											</label>
											<tags:searchButton containerId="${containerId}" />
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
					<tags:pageInfo containerId="${containerId}" formId="${formId}"
						levelId="up" />
					<table
						class="table dataTable table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="direction">数据分发方向</th>
								<th class="sorting" abbr="siteCode">所属渠道</th>
								<th class="sorting" abbr="name">平台名称</th>
								<th class="sorting" abbr="provider">平台服务公司</th>
								<th class="sorting" abbr="type">平台类型</th>
								<th class="sorting" abbr="interfaceMode">接口方式</th>
								<th class="sorting" abbr="cspId">CSPID</th>
								<th class="sorting" abbr="lspId">LSPID</th>
								<th class="sorting" abbr="status">状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th>序号</th>
								<th class="sorting" abbr="id">ID</th>
								<th class="sorting" abbr="direction">数据分发方向</th>
								<th class="sorting" abbr="siteCode">所属渠道</th>
								<th class="sorting" abbr="name">平台名称</th>
								<th class="sorting" abbr="provider">平台服务公司</th>
								<th class="sorting" abbr="type">平台类型</th>
								<th class="sorting" abbr="interfaceMode">接口方式</th>
								<th class="sorting" abbr="cspId">CSPID</th>
								<th class="sorting" abbr="lspId">LSPID</th>
								<th class="sorting" abbr="status">状态</th>
								<th>操作</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${page.content}" var="t" varStatus="status">
								<tr>
									<td>${status.index+1}</td>
									<td>${t.id}</td>
									<td><tags:enum className='class="badge badge-success"'
											enumList="${injectionDirectionEnum}" value="${t.direction}" /></td>
									<td><c:if test="${not empty t.siteCode}">
											<c:forEach var="item" items="${siteList}">
												<c:if test="${item.code eq t.siteCode}">
													<span class="badge badge-success">${item.name}</span>
												</c:if>
											</c:forEach>
										</c:if></td>
									<td>${t.name}</td>
									<td><tags:enum enumList="${providerTypeEnum}"
											value="${t.provider}" /></td>
									<td><tags:enum className='class="badge badge-success"'
											enumList="${typeEnum}" value="${t.type}" /></td>
									<td><tags:enum className='class="badge badge-success"'
											enumList="${providerInterfaceModeEnum}"
											value="${t.interfaceMode}" /></td>
									<td>${t.cspId}</td>
									<td>${t.lspId}</td>
									<td><c:forEach var="item" items="${statusEnum}">
											<c:if test="${item.key eq t.status && item.key eq 0}">
												<span class="badge badge-danger">${item.value}</span>
											</c:if>
											<c:if test="${item.key eq t.status && item.key eq 1}">
												<span class="badge badge-success">${item.value}</span>
											</c:if>
										</c:forEach></td>
									<td>
										<button class="btn btn-default btn-sm btn-outline green"
											onclick="$.InjectionPlatformController.toEdit('${ctx}/injection/platform/${t.id}/edit',${t.id});">
											<i class="fa fa-edit"></i>修改
										</button> <c:forEach var="item" items="${statusEnum}">
											<c:if test="${item.key eq t.status && item.key eq 0}">
												<button class="btn btn-default btn-sm btn-outline green"
													onclick="$.InjectionPlatformController.toDelete('${ctx}/injection/platform/${t.id}/delete','${t.cspId}');">
													<i class="fa fa-remove"></i>删除
												</button>
											</c:if>
											<c:if test="${item.key eq t.status && item.key eq 1}">
											</c:if>
										</c:forEach>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<tags:pageInfo containerId="${containerId}" formId="${formId}" />
				</div>
			</div>
		</div>
	</div>
</div>

<tags:contentModal />

<script>
$.InjectionPlatformController.keypress({containerId: '${containerId}', formId: '${formId}'});
</script>
