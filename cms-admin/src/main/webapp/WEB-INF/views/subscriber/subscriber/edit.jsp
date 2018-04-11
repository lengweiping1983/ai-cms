<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">修改用户所属分组</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="id" value="${subscriber.id}" />

					<div class="content form-horizontal">

						<div class="form-group">
							<label class="control-label col-md-4">所属用户分组:
							</label>

							<div class="col-md-6">
								<select class="form-control select2"
									name="groupCode" id="groupCode">
									<option value="">请选择</option>
									<c:forEach var="subscriberGroup" items="${subscriberGroupList}">
										<option value="${subscriberGroup.code}"
											<c:if test="${subscriber.groupCode eq subscriberGroup.code}">selected="selected"</c:if>>${subscriberGroup.name}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty subscriber.id}">
						<button class="btn btn-outline green"
							onclick="$.SubscriberController.edit('${ctx}/subscriber/subscriber/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.SubscriberController.edit('${ctx}/subscriber/subscriber/${subscriber.id}/edit');">
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