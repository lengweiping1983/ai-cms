<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">
					<c:choose>
						<c:when test="${empty injectionPlatform.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					${methodDesc}
					<c:forEach var="item" items="${injectionDirectionEnum}">
						<c:if
							test="${! empty injectionPlatform.direction && item.key eq injectionPlatform.direction}">[${item.value}]</c:if>
					</c:forEach>
					平台
				</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="id" value="${injectionPlatform.id}" />
					<input type="hidden" name="direction"
						value="${injectionPlatform.direction}" />
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">所属渠道(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="siteCode"
											class="form-control validate[required]">
											<option value="">请选择</option>
											<c:forEach var="item" items="${siteList}">
												<option value="${item.code}"
													<c:if test="${item.code eq injectionPlatform.siteCode}"> selected="selected" </c:if>>${item.name}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">状态(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="status" class="form-control">
											<c:forEach var="item" items="${statusEnum}">
												<option value="${item.key}"
													<c:if test="${! empty injectionPlatform.status && item.key eq injectionPlatform.status}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">平台名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="name"
											value="${injectionPlatform.name}"
											class="form-control validate[required]" placeholder="请输入平台名称">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">平台服务公司(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<select name="provider" class="form-control">
											<c:forEach var="item" items="${providerTypeEnum}">
												<option value="${item.key}"
													<c:if test="${! empty injectionPlatform.provider && item.key eq injectionPlatform.provider}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">对方平台代码(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="platformCode"
											value="${injectionPlatform.platformCode}"
											class="form-control validate[required]"
											placeholder="请输入对方平台代码">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">描述:</label>

									<div class="col-md-9">
										<textarea name="description" rows="8" class="form-control">${injectionPlatform.description}</textarea>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty injectionPlatform.id}">
						<button class="btn btn-outline green"
							onclick="$.InjectionPlatformController.edit('${ctx}/injection/platform/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.InjectionPlatformController.edit('${ctx}/injection/platform/${injectionPlatform.id}/edit');">
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