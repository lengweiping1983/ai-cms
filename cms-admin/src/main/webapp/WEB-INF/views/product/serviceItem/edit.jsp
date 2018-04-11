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
						<c:when test="${empty serviceItem.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					[${service.name}]${methodDesc}服务项
				</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="id" value="${serviceItem.id}" /> <input
						type="hidden" name="serviceId" value="${service.id}" /> <input
						type="hidden" name="itemId" id="itemId"
						value="${serviceItem.itemId}" />

					<div class="content form-horizontal">
						<c:choose>
							<c:when test="${empty serviceItem.id}">
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">元素类型: </label>

											<div class="col-md-9">
												<select id="itemType" name="itemType" class="form-control"
													onchange="$.ServiceItemController.changeSelectItem();">
													<c:forEach var="item" items="${typeEnum}">
														<option value="${item.key}"
															<c:if test="${item.key eq serviceItem.itemType}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">元素名称: </label>

											<div class="col-md-9">
												<div class="input-group">
													<input type="text" class="form-control validate[required]"
														name="itemName" id="itemName" placeholder="点击我选择元素"
														readonly="readonly"
														onclick="$.ServiceItemController.toSelectItem();" /> <span
														class="input-group-btn">
														<button onclick="$.ServiceItemController.toSelectItem();"
															class="btn btn-success" type="button">
															<i class="fa fa-arrow-left fa-fw" /></i> 选择元素
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
											<label class="control-label col-md-3">显示名称: </label>

											<div class="col-md-9">
												<input type="text" id="itemTitle"
													value="${serviceItem.itemTitle}" readonly="readonly"
													class="form-control">
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">元素状态: </label>

											<div class="col-md-9">
												<select id="itemStatus" name="itemStatus"
													disabled="disabled" class="form-control">
													<c:forEach var="item" items="${statusEnum}">
														<option value="${item.key}"
															<c:if test="${! empty serviceItem.itemStatus && item.key eq serviceItem.itemStatus}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</div>
								</div>
							</c:when>
							<c:otherwise>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">元素类型: </label>

											<div class="col-md-9">
												<select id="itemType" name="itemType" disabled="disabled"
													class="form-control">
													<c:forEach var="item" items="${typeEnum}">
														<option value="${item.key}"
															<c:if test="${item.key eq serviceItem.itemType}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">元素名称: </label>

											<div class="col-md-9">
												<input type="text" value="${serviceItem.itemName}"
													readonly="readonly" class="form-control">
											</div>
										</div>
									</div>
								</div>

								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">显示名称: </label>

											<div class="col-md-9">
												<input type="text" value="${serviceItem.itemTitle}"
													readonly="readonly" class="form-control">
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">元素状态: </label>

											<div class="col-md-9">
												<select id="itemStatus" name="itemStatus"
													disabled="disabled" class="form-control">
													<c:forEach var="item" items="${statusEnum}">
														<option value="${item.key}"
															<c:if test="${! empty serviceItem.itemStatus && item.key eq serviceItem.itemStatus}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</div>
								</div>
							</c:otherwise>
						</c:choose>

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
													<c:if test="${! empty serviceItem.status && item.key eq serviceItem.status}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">排序值(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="sortIndex"
											value="${serviceItem.sortIndex}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入排序值" />
										<c:if test="${empty serviceItem.id}">
											<p class="help-block">当前元素会插入到该位置，之后的元素会自动往后排.</p>
										</c:if>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">生效时间(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<div class="input-group date form_datetime">
											<input type="text" id="validTime" name="validTime"
												value='<fmt:formatDate value="${serviceItem.validTime}"
                                                                                       pattern="yyyy-MM-dd HH:mm"/>'
												size="16"
												readonly class="form-control validate[required]"> <span
												class="input-group-btn">
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
									<label class="control-label col-md-3">失效时间(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<div class="input-group date form_datetime">
											<input type="text" id="expiredTime" name="expiredTime"
												value='<fmt:formatDate value="${serviceItem.expiredTime}"
                                                                                       pattern="yyyy-MM-dd HH:mm"/>'
												size="16"
												readonly class="form-control validate[required]"> <span
												class="input-group-btn">
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
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty serviceItem.id}">
						<button class="btn btn-outline green"
							onclick="$.ServiceItemController.edit('${ctx}/service/${service.id}/serviceItem/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.ServiceItemController.edit('${ctx}/service/${service.id}/serviceItem/${serviceItem.id}/edit');">
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