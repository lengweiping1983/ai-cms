<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true"></button>
				<h4 class="modal-title">导入体验订单</h4>
			</div>

			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="id" value="${orderRecord.id}" />
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">产品代码(<span
											class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select class="form-control validate[required] select2" id="productCode" name="productCode">
											<option value="">请选择</option>
											<c:forEach var="item" items="${chargeList}">
												<option value="${item.partnerProductCode},${item.type},${item.price},${item.originPrice}">
														${item.name}
												</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">订单号(<span
											class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="orderCode" value="${orderRecord.orderCode}"
											   class="form-control validate[required]" placeholder="请输入体验订单号"
											   onChange="$.OrderRecordController.genSearchName(this);">
									</div>
								</div>
							</div>
						</div>

							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">订购时间(<span
												class="required">*</span>):
										</label>
										<div class="col-md-9">
											<div class="input-group date form_datetime">
												<input type="text" id="subscriptionTime"
													   name="subscriptionTime"
													   value='<fmt:formatDate value="${orderRecord.subscriptionTime}"
																										   pattern="yyyy-MM-dd HH:mm"/>'
													   size="16" onchange="$.OrderRecordController.changeOrderDate();"
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
										<label class="control-label col-md-3">生效时间(<span
												class="required">*</span>):
										</label>
										<div class="col-md-9">
											<div class="input-group date form_datetime">
												<input type="text" id="validTime"
													   name="validTime"
													   value='<fmt:formatDate value="${orderRecord.validTime}"
																										   pattern="yyyy-MM-dd HH:mm"/>'
													   size="16" onchange="$.OrderRecordController.changeValidTime();"
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

							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">失效时间(<span
												class="required">*</span>):
										</label>
										<div class="col-md-9">
											<div class="input-group date form_datetime">
												<input type="text" id="expiredTime"
													   name="expiredTime"
													   value='<fmt:formatDate value="${orderRecord.expiredTime}"
																										   pattern="yyyy-MM-dd HH:mm"/>'
													   size="16" onchange="$.OrderRecordController.changeExpiredTime();"
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
										<label class="control-label col-md-3">所属APP(<span
												class="required">*</span>):
										</label>
										<div class="col-md-9">
											<select class="form-control validate[required] select2" id="appCode" name="appCode">
												<option value="">请选择</option>
												<c:forEach var="app" items="${appList}">
													<option value="${app.code}">
															${app.name}
													</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">帐号列表:(<span
											class="required">*</span>):
									</label>

									<div class="col-md-9">
										<textarea name="partnerUserId" rows="8" placeholder="一个账号一行" class="form-control validate[required]">${orderRecord.partnerUserId}</textarea>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
				</div>

			<div class="modal-footer">
				<button class="btn btn-outline green"
						onclick="$.OrderRecordController.edit('${ctx}/orderRecord/orderRecord/saveAccount')">
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
</div>