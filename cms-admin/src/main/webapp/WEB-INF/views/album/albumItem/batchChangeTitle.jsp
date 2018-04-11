<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-full">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">[${album.name}]批量修改</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-12">
								<table class="table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>序号</th>
											<th>元素类型</th>
											<th>元素id</th>
											<th>元素名称</th>
											<th>显示名称</th>
											<th>定制名称</th>
											<th>排序值</th>
										</tr>
									</thead>
									<tfoot>
										<tr>
											<th>序号</th>
											<th>元素类型</th>
											<th>元素id</th>
											<th>元素名称</th>
											<th>显示名称</th>
											<th>定制名称</th>
											<th>排序值</th>
										</tr>
									</tfoot>
									<tbody>
										<c:forEach items="${albumItemList}" var="t"
											varStatus="status">
											<tr>
												<td>${status.index+1}</td>
												<td><tags:type value='${t.itemType}' /></td>
												<td>${t.itemId}</td>
												<td>${t.itemName}</td>
												<td>${t.itemTitle}</td>
												<td><input type="hidden" name="ids" value="${t.id}">
													<input type="text" name="titles" value="${t.title}"
													class="form-control"></td>
												<td><input type="text" name="sortIndexs"
													value="${t.sortIndex}"
													class="form-control input-small input-inline validate[required,custom[integer]]"></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button class="btn btn-outline green"
					onclick="$.BaseController.batchChangeTitle('${ctx}/album/${album.id}/albumItem/batchChangeTitle');">
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