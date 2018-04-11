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
						<c:when test="${empty app.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					${methodDesc}APP
				</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="id" value="${app.id}" />

					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select id="type" name="type" class="form-control">
											<c:forEach var="item" items="${typeEnum}">
												<option value="${item.key}"
													<c:if test="${item.key eq app.type}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">APP代码(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<c:choose>
											<c:when test="${empty app.id}">
												<input type="text" id="code" name="code" value="${app.code}"
													onkeyup="this.value=this.value.toUpperCase()"
													class="form-control validate[required,custom[onlyLetterNumberUnderline],ajax[ajaxCodeCheck],maxSize[8]]"
													placeholder="请输入APP代码">
											</c:when>
											<c:otherwise>
												<input type="text" id="code" name="code" value="${app.code}"
													onkeyup="this.value=this.value.toUpperCase()"
													class="form-control" placeholder="请输入APP代码"
													readonly="readonly">
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">APP名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="name" value="${app.name}"
											class="form-control validate[required]"
											placeholder="请输入APP名称">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">独立计费点(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="aloneCharge" class="form-control" onchange="$.AppController.changeAloneCharge(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty app.aloneCharge && item.key eq app.aloneCharge}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>

							<div class="col-md-6" id="chargeAppCode_div"
								<c:if test="${app.aloneCharge == 1}"> style="display: none" </c:if>>
								<div class="form-group">
									<label class="control-label col-md-3">计费点所属应用(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select class="form-control select2 validate[required]"
											name="chargeAppCode" id="chargeAppCode">
											<option value="">请选择</option>
											<c:forEach var="temp" items="${appList}">
												<c:if test="${temp.code ne app.code }">
													<option value="${temp.code}"
														<c:if test="${temp.code eq app.chargeAppCode}">selected="selected"</c:if>>${temp.name}</option>
												</c:if>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<!--
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">独立订购页(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="aloneOrderPage" class="form-control">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty app.aloneOrderPage && item.key eq app.aloneOrderPage}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							-->
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">可访问的内容提供商(<span
										class="required">*</span>):
									</label>
									
									<div class="col-md-9">
										<div class="checkbox-list">
											<c:set var="cpCodeAllSelected" value="0" />
											<c:if test="${empty app.cpCodes}">
												<c:set var="cpCodeAllSelected" value="1" />
											</c:if>
											<label><input name="cpCodes"
													<c:if test="${cpCodeAllSelected eq 1 }"> checked </c:if>
													class="validate[required]" type="checkbox" value="" id="cpCodeAll" onClick="$.AppController.clickCpCodes()">全部</label>
											<c:forEach var="item" items="${cpList}">
												<c:set var="cpCodeSelected" value="" />
												<c:forEach var="cpCode" items="${app.cpCodes}">
													<c:if test="${item.code eq cpCode }">
														<c:set var="cpCodeSelected" value="1" />
													</c:if>
												</c:forEach>
												<c:if test="${empty app.cpCodes}">
													<c:set var="cpCodeSelected" value="0" />
												</c:if>
												<label><input name="cpCodes"
													<c:if test="${cpCodeSelected eq 1 || cpCodeAllSelected eq 1}"> checked </c:if>
													class="validate[required]" type="checkbox" value="${item.code}">${item.name}</label>
											</c:forEach>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">可访问的应用(<span
										class="required">*</span>):
									</label>
									
									<div class="col-md-9">
										<div class="checkbox-list">
											<c:set var="appCodeAllSelected" value="0" />
											<c:if test="${empty app.appCodes}">
												<c:set var="appCodeAllSelected" value="1" />
											</c:if>
											<label><input name="appCodes"
													<c:if test="${appCodeAllSelected eq 1 }"> checked </c:if>
													class="validate[required]" type="checkbox" value="" id="appCodeAll" onClick="$.AppController.clickAppCodes()">全部</label>
											<c:forEach var="item" items="${appList}">
												<c:set var="appCodeSelected" value="" />
												<c:forEach var="appCode" items="${app.appCodes}">
													<c:if test="${item.code eq appCode }">
														<c:set var="appCodeSelected" value="1" />
													</c:if>
												</c:forEach>
												<c:if test="${empty app.appCodes}">
													<c:set var="appCodeSelected" value="0" />
												</c:if>
												<label>
												<c:if test="${app.code ne item.code}">
													<input name="appCodes"
														<c:if test="${appCodeSelected eq 1 || appCodeAllSelected eq 1}"> checked </c:if>
														class="validate[required]" type="checkbox" value="${item.code}">${item.name}
												</c:if>
												</label>
											</c:forEach>
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
													<c:if test="${! empty app.status && item.key eq app.status}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">描述:</label>

									<div class="col-md-9">
										<textarea name="description" class="form-control">${app.description}</textarea>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty app.id}">
						<button class="btn btn-outline green"
							onclick="$.AppController.edit('${ctx}/app/app/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.AppController.edit('${ctx}/app/app/${app.id}/edit');">
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