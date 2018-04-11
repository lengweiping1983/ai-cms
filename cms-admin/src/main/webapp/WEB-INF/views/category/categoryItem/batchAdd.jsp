<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-full">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">
					<c:choose>
						<c:when test="${empty categoryItem.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					[${category.name}]批量${methodDesc}栏目项
				</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label col-md-3">元素类型: </label>

							<div class="col-md-9">
								<div class="input-group">
									<select id="itemType" name="itemType" class="form-control">
										<c:forEach var="item" items="${typeEnum}">
											<c:set var="configItemSelected" value="" />
											<c:forEach var="configItem" items="${category.configItemTypes}">
												<c:if test="${item.key eq configItem }">
													<c:set var="configItemSelected" value="1" />
												</c:if>
											</c:forEach>
											<c:if test="${empty category.configItemTypes}">
												<c:set var="configItemSelected" value="1" />
											</c:if>
											<c:if test="${item.key eq 1 || item.key eq 2 || item.key eq 3}">
												<c:if test="${configItemSelected eq 1 }">
													<option value="${item.key}"
														<c:if test="${item.key eq categoryItem.itemType}"> selected="selected" </c:if>>${item.value}</option>
												</c:if>
											</c:if>
										</c:forEach>
									</select> <span class="input-group-btn">
										<button onclick="$.CategoryItemController.toSelectItemFromBatchAdd();"
											class="btn btn-success" type="button">
											<i class="fa fa-arrow-left fa-fw" /></i> 选择元素
										</button>
									</span>
									
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<label class="control-label col-md-2">排序值: </label>
						<input type="text" id="sort_index_all" class="form-control input-small input-inline col-md-6" value="1">
					</div>
				</div>
				<form id="editForm">
					<div class="content form-horizontal">
						<table
							class="table dataTable table-striped table-bordered table-hover table-checkable">
							<thead>
								<tr>
									<th width="6%">元素id</th>
									<th width="8%">元素类型</th>
									<th width="25%">显示名称</th>
									<th width="25%">定制名称</th>
									<th width="8%">元素状态</th>
									<th width="15%">上线状态</th>
									<th width="15%" style="display: none">排序值</th>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<th>元素id</th>
									<th>元素类型</th>
									<th>显示名称</th>
									<th>定制名称</th>
									<th>元素状态</th>
									<th>上线状态</th>
									<th style="display: none">排序值</th>
								</tr>
							</tfoot>
							<tbody id="batchTbody">
							</tbody>
						</table>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button class="btn btn-outline green"
					onclick="$.CategoryItemController.batchAdd('${ctx}/category/${category.id}/categoryItem/batchAdd');">
					<i class="fa fa-save"></i>保存
				</button>
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>