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
									<label class="control-label col-md-3">平台类型(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<select name="type" class="form-control"
											onchange="$.InjectionPlatformController.changeSelectType(this.value);">
											<c:forEach var="item" items="${typeEnum}">
												<option value="${item.key}"
													<c:if test="${! empty injectionPlatform.type && item.key eq injectionPlatform.type}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group" id="type_div_1"
									<c:if test="${injectionPlatform.type != 2}"> style="display: none" </c:if>>
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
										<p class="help-block">先分发到依赖平台，等依赖平台都成功后，才分发到该平台.</p>
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
									<label class="control-label col-md-3">是否要回写(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="isCallback" class="form-control"
											onchange="$.InjectionPlatformController.changeIsCallback(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty injectionPlatform.isCallback && item.key eq injectionPlatform.isCallback}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group" id="isCallback_div_1"
									<c:if test="${injectionPlatform.isCallback != 1}"> style="display: none" </c:if>>
									<label class="control-label col-md-3">间接平台(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<div class="checkbox-list">
											<c:forEach var="item" items="${indirectPlatformList}">
												<c:set var="indirectPlatformSelected" value="" />
												<c:forEach var="indirectPlatformId"
													items="${injectionPlatform.indirectPlatformId}">
													<c:if test="${item.id eq indirectPlatformId}">
														<c:set var="indirectPlatformSelected" value="1" />
													</c:if>
												</c:forEach>
												<label><input name="indirectPlatformId"
													<c:if test="${indirectPlatformSelected eq 1}"> checked </c:if>
													class="validate[required]" type="checkbox"
													value="${item.id}"><span
													class="badge badge-success">${item.name}</span> </label>
											</c:forEach>
										</div>
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
									<label class="control-label col-md-3">自定义模板(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="templateCustom" class="form-control"
											onchange="$.InjectionPlatformController.changeTemplateCustom(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty injectionPlatform.templateCustom && item.key eq injectionPlatform.templateCustom}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
										<p class="help-block">工单模板是否是标准统一模板.</p>
									</div>
								</div>
							</div>
							<div class="col-md-6" id="templateCustom_div_1"
								<c:if test="${injectionPlatform.templateCustom != 1}"> style="display: none" </c:if>>
								<div class="form-group">
									<label class="control-label col-md-3">模板文件名(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="templateFilename"
											value="${injectionPlatform.templateFilename}"
											class="form-control validate[required]"
											placeholder="请输入模板文件名">
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">优先使用全局代码(<span
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
											<span class="badge badge-success">否</span>代码根据规则生成，再发送给下平台<br />
											<span class="badge badge-success">是</span>如全局代码存在时，直接使用全局代码发送给下平台
										</p>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">下平台是否改变代码(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="playCodeCustom" class="form-control">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty injectionPlatform.playCodeCustom && item.key eq injectionPlatform.playCodeCustom}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
										<p class="help-block">
											<span class="badge badge-success">否</span>下平台不改变代码<br /> <span
												class="badge badge-success">是</span>下平台改变代码
										</p>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">代码前缀(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="codePrefix"
											value="${injectionPlatform.codePrefix}"
											class="form-control validate[required]" placeholder="请输入代码前缀">
										<p class="help-block">代码前8位.</p>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">工单标识前缀: </label>

									<div class="col-md-9">
										<input type="text" name="correlatePrefix"
											value="${injectionPlatform.correlatePrefix}"
											class="form-control" placeholder="请输入工单标识前缀">
										<p class="help-block">可以不填写.</p>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">是否要单片包装成剧头(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="needPackingProgram" class="form-control">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty injectionPlatform.needPackingProgram && item.key eq injectionPlatform.needPackingProgram}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">是否要海报对象(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="needImageObject" class="form-control">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty injectionPlatform.needImageObject && item.key eq injectionPlatform.needImageObject}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">是否要删除媒体内容(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="needDeleteMediaFile" class="form-control">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty injectionPlatform.needDeleteMediaFile && item.key eq injectionPlatform.needDeleteMediaFile}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">分隔字符(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="separateChar"
											value="${injectionPlatform.separateChar}"
											class="form-control validate[required]" placeholder="请输入分隔字符">
										<p class="help-block">如演员字段，多个演示使用“,”分隔字符.</p>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">默认支持码率: </label>

									<div class="col-md-9">
										<div class="checkbox-list">
											<c:forEach var="item" items="${mediaTemplateList}">
												<c:set var="mediaTemplateSelected" value="" />
												<c:forEach var="mediaTemplateId"
													items="${injectionPlatform.templateId}">
													<c:if test="${item.id eq mediaTemplateId}">
														<c:set var="mediaTemplateSelected" value="1" />
													</c:if>
												</c:forEach>
												<label><input name="templateId"
													<c:if test="${mediaTemplateSelected eq 1}"> checked </c:if>
													type="checkbox" value="${item.id}"><span
													class="badge badge-success">${item.title}</span> </label>
											</c:forEach>
										</div>
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