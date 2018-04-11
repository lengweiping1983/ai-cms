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
						<c:when test="${empty charge.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					${methodDesc}计费点
				</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="id" value="${charge.id}" />

					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">所属APP(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<c:choose>
											<c:when test="${empty charge.id}">
												<select class="form-control select2 validate[required]"
													name="appCode" id="appCode">
													<c:forEach var="app" items="${appList}">
														<option value="${app.code}"
															<c:if test="${charge.appCode eq app.code}">selected="selected"</c:if>>${app.name}</option>
													</c:forEach>
												</select>
											</c:when>
											<c:otherwise>
												<select class="form-control select2" disabled name="appCode"
													id="appCode">
													<c:forEach var="app" items="${appList}">
														<option value="${app.code}"
															<c:if test="${charge.appCode eq app.code}">selected="selected"</c:if>>${app.name}</option>
													</c:forEach>
												</select>
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">类型: </label>

									<div class="col-md-9">
										<select id="type" name="type" class="form-control">
											<c:forEach var="item" items="${typeEnum}">
												<option value="${item.key}"
													<c:if test="${item.key eq charge.type}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">时段单位: </label>

									<div class="col-md-9">
										<select id="type" name="timeUnit" class="form-control">
											<c:forEach var="item" items="${chargeTimeUnitEnum}">
												<option value="${item.key}"
													<c:if test="${item.key eq charge.timeUnit}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">时长(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="duration"
											value="${charge.duration}"
											class="form-control validate[custom[integer]]"
											placeholder="请输入时长">
									</div>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">计费点代码(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<c:choose>
											<c:when test="${empty charge.id}">
												<input type="text" id="code" name="code"
													value="Fee_"
													onkeyup="this.value=this.value"
													class="form-control validate[required,custom[onlyLetterNumberUnderline],ajax[ajaxCodeCheck]]"
													placeholder="请输入计费点代码">
											</c:when>
											<c:otherwise>
												<input type="text" id="code" name="code"
													value="${charge.code}"
													onkeyup="this.value=this.value"
													class="form-control"
													placeholder="请输入计费点代码" readonly="readonly">
											</c:otherwise>
										</c:choose>
										<p class="help-block">计费点代码以Fee_开头.</p>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">计费点名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="name" value="${charge.name}"
											class="form-control validate[required]"
											placeholder="请输入计费点名称">
									</div>
								</div>
							</div>
						</div>
				
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">原价(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="originPrice"
											value="${charge.originPrice}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入原价"><span class="help-block">单位分</span>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">现价(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="price" value="${charge.price}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入现价"> <span class="help-block">单位分</span>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">是否自动续订: </label>

									<div class="col-md-9">
										<select name="subscriptionExtend" class="form-control">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty charge.subscriptionExtend && item.key eq charge.subscriptionExtend}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">本地产品代码(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="partnerProductCode"
											value="${charge.partnerProductCode}"
											class="form-control validate[required]"
											placeholder="请输入本地产品代码">
									</div>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">SPID:
									</label>

									<div class="col-md-9">
										<input type="text" name="spId"
											value="${charge.spId}"
											class="form-control"
											placeholder="请输入SPID">
										<p class="help-block">运营商分配的SPID或者叫appid.</p>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">SPKEY:
									</label>

									<div class="col-md-9">
										<input type="text" name="spKey"
											value="${charge.spKey}"
											class="form-control"
											placeholder="请输入spKey">
										<p class="help-block">运营商分配的钥匙或者叫appkey.</p>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">有效开始时间(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<div class="input-group date form_datetime">
											<input type="text" id="beginTime" name="beginTime"
												value='<fmt:formatDate value="${charge.beginTime}"
                                                                                       pattern="yyyy-MM-dd HH:mm"/>'
												size="16" onchange="$.ChargeController.changeBeginDate();"
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
									<label class="control-label col-md-3">有效结束时间(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<div class="input-group date form_datetime">
											<input type="text" id="endTime" name="endTime"
												value='<fmt:formatDate value="${charge.endTime}"
                                                                                       pattern="yyyy-MM-dd HH:mm"/>'
												size="16" onchange="$.ChargeController.changeEndDate();"
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
									<label class="control-label col-md-3">状态(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="status" class="form-control">
											<c:forEach var="item" items="${statusEnum}">
												<option value="${item.key}"
													<c:if test="${! empty charge.status && item.key eq charge.status}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">是否支持退订(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="supportUnsubscribe" class="form-control">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty charge.supportUnsubscribe && item.key eq charge.supportUnsubscribe}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">排序值(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="sortIndex"
											value="${charge.sortIndex}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入排序值" />
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">介绍(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="intro" value="${charge.intro}"
											class="form-control validate[required]"
											placeholder="请输入介绍">
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">描述:</label>

									<div class="col-md-9">
										<textarea name="description" class="form-control">${charge.description}</textarea>
									</div>
								</div>
							</div>
							<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">共享APP(<span
										class="required">*</span>): </label>

										<div class="col-md-9">
											<div class="checkbox-list">
												<c:set var="appCodeAllSelected" value="0" />
												<c:if test="${empty charge.appCodes || charge.appCodes eq '*'}">
													<c:set var="appCodeAllSelected" value="1" />
												</c:if>
												<label><input name="appCodes"
														<c:if test="${appCodeAllSelected eq 1 }"> checked </c:if>
														class="validate[required]" type="checkbox" value="*" id="appCodeAll" onClick="$.ChargeController.clickAppCodes()">全部应用</label>
												<c:forEach var="item" items="${appList}">
													<c:set var="appCodeSelected" value="" />
													<c:forEach var="app" items="${charge.appCodes}">
														<c:if test="${item.code eq app }">
															<c:set var="appCodeSelected" value="1" />
														</c:if>
													</c:forEach>
													<c:if test="${empty charge.appCodes || charge.appCodes eq '*'}">
														<c:set var="appCodeSelected" value="0" />
													</c:if>
													<label><input name="appCodes"
														<c:if test="${appCodeSelected eq 1 || appCodeAllSelected eq 1}"> checked </c:if>
														class="validate[required]" type="checkbox" value="${item.code}">${item.name}</label>
												</c:forEach>
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
					<c:when test="${empty charge.id}">
						<button class="btn btn-outline green"
							onclick="$.ChargeController.edit('${ctx}/config/charge/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.ChargeController.edit('${ctx}/config/charge/${charge.id}/edit');">
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