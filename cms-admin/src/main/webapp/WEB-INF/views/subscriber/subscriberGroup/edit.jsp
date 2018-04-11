<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">
					<c:choose>
						<c:when test="${empty subscriberGroup.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					${methodDesc}分组
				</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="id" value="${subscriberGroup.id}" />

					<div class="content form-horizontal">
						
						<div class="form-group">
							<label class="control-label col-md-4">分组类型(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<select name="groupType" class="form-control">
									<c:forEach var="item" items="${typeEnum}">
										<option value="${item.key}"
											<c:if test="${! empty subscriberGroup.groupType && item.key eq subscriberGroup.groupType}"> selected="selected" </c:if>>${item.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
								
						<div class="form-group">
							<label class="control-label col-md-4">分组代码(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<c:choose>
									<c:when test="${empty subscriberGroup.id}">
										<input type="text" id="code" name="code" value="${subscriberGroup.code}"
											class="form-control validate[required,custom[onlyLetterNumberUnderline],ajax[ajaxCodeCheck]]"
											placeholder="请输入分组代码">
									</c:when>
									<c:otherwise>
										<input type="text" id="code" name="code" value="${subscriberGroup.code}"
											class="form-control" placeholder="请输入分组代码"
											readonly="readonly" onclick="$.SubscriberGroupController.code();">
									</c:otherwise>
								</c:choose>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-4">分组名称(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<input type="text" name="name" value="${subscriberGroup.name}"
									class="form-control validate[required]" placeholder="请输入分组名称">
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
											<c:if test="${! empty subscriberGroup.status && item.key eq subscriberGroup.status}"> selected="selected" </c:if>>${item.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-4">描述:</label>

							<div class="col-md-6">
								<textarea name="description" class="form-control">${subscriberGroup.description}</textarea>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty subscriberGroup.id}">
						<button class="btn btn-outline green"
							onclick="$.SubscriberGroupController.edit('${ctx}/subscriber/subscriberGroup/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.SubscriberGroupController.edit('${ctx}/subscriber/subscriberGroup/${subscriberGroup.id}/edit');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:otherwise>
				</c:choose>
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>