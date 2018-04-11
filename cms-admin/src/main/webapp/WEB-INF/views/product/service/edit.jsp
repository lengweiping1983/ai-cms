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
						<c:when test="${empty service.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					${methodDesc}服务
				</h4>
			</div>
			<div class="modal-body">
				<input type="hidden" id="image1Data" name="image1Data"> <input
					type="hidden" id="image2Data" name="image2Data">
				<form id="editForm">
					<input type="hidden" name="id" value="${service.id}" />
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">服务类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select id="type" name="type" class="form-control">
											<c:forEach var="item" items="${typeEnum}">
												<option value="${item.key}"
													<c:if test="${item.key eq service.type}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">服务代码(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<c:choose>
											<c:when test="${empty service.id}">
												<input type="text" id="code" name="code"
													value="${service.code}"
													class="form-control validate[required,custom[onlyLetterNumberUnderline],ajax[ajaxCodeCheck]]"
													placeholder="请输入服务代码">
											</c:when>
											<c:otherwise>
												<input type="text" id="code" name="code"
													value="${service.code}" class="form-control"
													placeholder="请输入服务代码" readonly="readonly"
													onclick="$.ServiceController.code();">
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">服务名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="name" value="${service.name}"
											class="form-control validate[required]" placeholder="请输入服务名称">
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">TAG: </label>

									<div class="col-md-9">
										<input type="text" name="tags" value="${service.tags}"
											class="form-control" placeholder="请输入TAG">
										<p class="help-block">多个使用','分割.</p>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">关键字: </label>

									<div class="col-md-9">
										<input type="text" name="keyword" value="${service.keyword}"
											class="form-control" placeholder="请输入关键字">
										<p class="help-block">多个使用','分割.</p>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">授权开始时间(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<div class="input-group date form_datetime">
											<input type="text" id="licensingWindowStart"
												name="licensingWindowStart"
												value='<fmt:formatDate value="${service.licensingWindowStart}"
                                                                                       pattern="yyyy-MM-dd HH:mm"/>'
												size="16" onchange="$.ServiceController.changeBeginDate();"
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
									<label class="control-label col-md-3">授权结束时间(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<div class="input-group date form_datetime">
											<input type="text" id="licensingWindowEnd"
												name="licensingWindowEnd"
												value='<fmt:formatDate value="${service.licensingWindowEnd}"
                                                                                       pattern="yyyy-MM-dd HH:mm"/>'
												size="16" onchange="$.ServiceController.changeEndDate();"
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
									<label class="control-label col-md-3">价格: </label>

									<div class="col-md-9">
										<input type="text" name="price" value="${service.price}"
											class="form-control validate[custom[integer]]"
											placeholder="请输入价格">
										<p class="help-block">单位分.</p>
									</div>
								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">描述:</label>

									<div class="col-md-9">
										<textarea name="description" rows="8" class="form-control">${service.description}</textarea>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">分发状态:</label>

									<div class="col-md-9">
										<p class="form-control-static">
											<tags:injectionStatus value='${service.injectionStatus}' />
										</p>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">运营商侧代码: </label>

									<div class="col-md-9">
										<input type="text" name="partnerItemCode"
											value="${service.partnerItemCode}" class="form-control"
											placeholder="请输入运营商侧代码">
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty service.id}">
						<button class="btn btn-outline green"
							onclick="$.ServiceController.edit('${ctx}/service/service/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.ServiceController.edit('${ctx}/service/service/${service.id}/edit');">
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