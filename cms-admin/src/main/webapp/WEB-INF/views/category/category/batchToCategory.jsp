<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">批量编排到栏目</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="itemType" value="${itemType}" /> <input
						type="hidden" name="itemIds" value="${itemIds}" />
					<div class="content form-horizontal">
						<div class="form-group">
							<label class="control-label col-md-4">所属APP(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<select class="form-control select2 validate[required]"
									name="appCode" id="appCode">
									<c:forEach var="app" items="${appList}">
										<option value="${app.code}"
											<c:if test="${currentAppCode eq app.code}">selected="selected"</c:if>>${app.name}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-4">所属栏目(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<div class="input-group">
									<input type="hidden" name="selectCategoryId"
										id="selectCategoryId" /> <input type="text"
										class="form-control validate[required]"
										name="selectCategoryName" id="selectCategoryName"
										placeholder="点击我选择所属栏目" readonly="readonly"
										onclick="$.CategoryController.selectCategoryTree();" /> <span
										class="input-group-btn">
										<button onclick="$.CategoryController.selectCategoryTree();"
											class="btn btn-success" type="button">
											<i class="fa fa-arrow-left fa-fw" /></i> 选择栏目
										</button>
									</span>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-4">状态(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<select name="status" class="form-control">
									<c:forEach var="item" items="${statusEnum}">
										<option value="${item.key}"
											<c:if test="${! empty category.status && item.key eq category.status}"> selected="selected" </c:if>>${item.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-4">排序值(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<input type="text" name="sortIndex" value="1"
									class="form-control validate[required,custom[integer]]"
									placeholder="请输入排序值" />
								<p class="help-block">当前元素会插入到该位置，之后的元素会自动往后排.</p>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button class="btn btn-outline green"
					onclick="$.CategoryController.batchToCategory('${ctx}/category/category/batchToCategory');">
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