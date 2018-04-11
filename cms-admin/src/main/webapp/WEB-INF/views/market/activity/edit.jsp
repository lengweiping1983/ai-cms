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
						<c:when test="${empty activity.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					${methodDesc}活动
				</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="id" value="${activity.id}" />

					<div class="content form-horizontal">

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">活动代码(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<c:choose>
											<c:when test="${empty activity.id}">
												<input type="text" id="code" name="code"
													value="${activity.code}"
													class="form-control validate[required,custom[onlyLetterNumberUnderline],ajax[ajaxCodeCheck]]"
													placeholder="请输入活动代码">
											</c:when>
											<c:otherwise>
												<input type="text" id="code" name="code"
													value="${activity.code}" class="form-control"
													placeholder="请输入活动代码" readonly="readonly"
													onclick="$.ActivityController.code();">
											</c:otherwise>
										</c:choose>
										<p class="help-block">活动代码自定义，可以是任意字符.</p>
									</div>
								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">活动名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="name" value="${activity.name}"
											class="form-control validate[required]" placeholder="请输入活动名称">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="type" class="form-control">
											<c:forEach var="item" items="${typeEnum}">
												<option value="${item.key}"
													<c:if test="${! empty activity.type && item.key eq activity.type}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">地址:</label>

									<div class="col-md-9">
										<input type="text" name="address" value="${activity.address}"
											class="form-control" placeholder="请输入活动地址">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">开始时间(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<div class="input-group date form_datetime">
											<input type="text" id="beginTime" name="beginTime"
												value='<fmt:formatDate value="${activity.beginTime}"
                                                                                       pattern="yyyy-MM-dd HH:mm"/>'
												size="16" readonly class="form-control validate[required]">
											<span class="input-group-btn">
												<button class="btn default date-reset" type="button">
													<i class="fa fa-times"></i>
												</button>
												<button class="btn default date-set" type="button">
													<i class="fa fa-calendar"></i>
												</button>
											</span>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">结束时间(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<div class="input-group date form_datetime">
											<input type="text" id="endTime" name="endTime"
												value='<fmt:formatDate value="${activity.endTime}"
                                                                                       pattern="yyyy-MM-dd HH:mm"/>'
												size="16" readonly class="form-control validate[required]">
											<span class="input-group-btn">
												<button class="btn default date-reset" type="button">
													<i class="fa fa-times"></i>
												</button>
												<button class="btn default date-set" type="button">
													<i class="fa fa-calendar"></i>
												</button>
											</span>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">状态(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="status" class="form-control">
											<c:forEach var="item" items="${statusEnum}">
												<option value="${item.key}"
													<c:if test="${! empty activity.status && item.key eq activity.status}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">描述:</label>

									<div class="col-md-9">
										<textarea name="description" class="form-control">${activity.description}</textarea>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty activity.id}">
						<button class="btn btn-outline green"
							onclick="$.ActivityController.edit('${ctx}/activity/activity/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.ActivityController.edit('${ctx}/activity/activity/${activity.id}/edit');">
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