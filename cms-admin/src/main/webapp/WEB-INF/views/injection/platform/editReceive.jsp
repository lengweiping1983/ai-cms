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
									<label class="control-label col-md-3">是否要下载视频(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="needDownloadVideo" class="form-control">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty injectionPlatform.needDownloadVideo && item.key eq injectionPlatform.needDownloadVideo}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">是否要审核(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="needAudit" class="form-control"
											onchange="$.InjectionPlatformController.changeSelectNeedAudit(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty injectionPlatform.needAudit && item.key eq injectionPlatform.needAudit}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row" id="needInjection_div_1"
							<c:if test="${injectionPlatform.needAudit == 1}"> style="display: none" </c:if>>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">是否要自动分发(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<select name="needInjection" class="form-control"
											onchange="$.InjectionPlatformController.changeSelectNeedInjection(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty injectionPlatform.needInjection && item.key eq injectionPlatform.needInjection}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group" id="needInjection_div_2"
									<c:if test="${injectionPlatform.needInjection != 1}"> style="display: none" </c:if>>
									<label class="control-label col-md-3">自动分发平台(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<div class="checkbox-list">
											<c:forEach var="item" items="${injectionPlatformList}">
												<c:set var="injectionPlatformSelected" value="" />
												<c:forEach var="injectionPlatformId"
													items="${injectionPlatform.injectionPlatformId}">
													<c:if test="${item.id eq injectionPlatformId}">
														<c:set var="injectionPlatformSelected" value="1" />
													</c:if>
												</c:forEach>
												<label><input name="injectionPlatformId"
													<c:if test="${injectionPlatformSelected eq 1}"> checked </c:if>
													class="validate[required]" type="checkbox"
													value="${item.id}"><span
													class="badge badge-success">${item.name}</span> </label>
											</c:forEach>
											<p class="help-block">自动分发.</p>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">接口方式(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="interfaceMode" class="form-control">
											<c:forEach var="item" items="${providerInterfaceModeEnum}">
												<option value="${item.key}"
													<c:if test="${! empty injectionPlatform.interfaceMode && item.key eq injectionPlatform.interfaceMode}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">依赖平台: </label>

									<div class="col-md-9">
										<div class="checkbox-list">
											<c:forEach var="item" items="${injectionPlatformList}">
												<c:if test="${item.direction eq 1 && item.type eq 1}">
													<c:set var="dependPlatformSelected" value="" />
													<c:forEach var="dependPlatformId"
														items="${injectionPlatform.dependPlatformId}">
														<c:if test="${item.id eq dependPlatformId}">
															<c:set var="dependPlatformSelected" value="1" />
														</c:if>
													</c:forEach>
													<label><input name="dependPlatformId"
														<c:if test="${dependPlatformSelected eq 1}"> checked </c:if>
														type="checkbox" value="${item.id}"><span
														class="badge badge-success">${item.name}</span> </label>
												</c:if>
											</c:forEach>
										</div>
										<p class="help-block">等待依赖平台都处理后，才回复通知消息.</p>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">CSPID(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="cspId"
											value="${injectionPlatform.cspId}"
											class="form-control validate[required]"
											placeholder="请输入CSPID">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">LSPID(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="lspId"
											value="${injectionPlatform.lspId}"
											class="form-control validate[required]"
											placeholder="请输入LSPID">
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">接口地址(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="serviceUrl"
											value="${injectionPlatform.serviceUrl}"
											class="form-control validate[required]" placeholder="请输入接口地址">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">直播接口地址: </label>

									<div class="col-md-9">
										<input type="text" name="liveServiceUrl"
											value="${injectionPlatform.liveServiceUrl}"
											class="form-control" placeholder="请输入直播接口地址">
										<p class="help-block">不填写即等同于接口地址.</p>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">是否WSDL(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="isWSDL" class="form-control">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty injectionPlatform.isWSDL && item.key eq injectionPlatform.isWSDL}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">namespace(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="namespace"
											value="${injectionPlatform.namespace}"
											class="form-control validate[required]"
											placeholder="请输入namespace">
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">代码为全局代码(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="useGlobalCode" class="form-control">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty injectionPlatform.useGlobalCode && item.key eq injectionPlatform.useGlobalCode}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
										<p class="help-block">
											<span class="badge badge-success">否</span>平台根据规则生成全局代码<br />
											<span class="badge badge-success">是</span>平台使用接收到的代码作为全局代码
										</p>
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