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
						<c:when test="${empty template.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					${methodDesc}模板
				</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="id" value="${template.id}" />
					<input type="hidden" id="backgroundImageData" name="backgroundImageData">
					<div class="content form-horizontal">

						<div class="form-group">
							<label class="control-label col-md-4">所属APP(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<c:choose>
									<c:when test="${empty template.id}">
										<select class="form-control select2 validate[required]"
											name="appCode" id="appCode">
											<option value="SYSTEM" <c:if test="${template.appCode eq 'SYSTEM'}">selected="selected"</c:if>>系统</option>
											<c:forEach var="app" items="${appList}">
												<option value="${app.code}"
													<c:if test="${template.appCode eq app.code}">selected="selected"</c:if>>${app.name}</option>
											</c:forEach>
										</select>
									</c:when>
									<c:otherwise>
										<select class="form-control select2" disabled name="appCode"
											id="appCode">
											<option value="SYSTEM" <c:if test="${template.appCode eq 'SYSTEM'}">selected="selected"</c:if>>系统</option>
											<c:forEach var="app" items="${appList}">
												<option value="${app.code}"
													<c:if test="${template.appCode eq app.code}">selected="selected"</c:if>>${app.name}</option>
											</c:forEach>
										</select>
									</c:otherwise>
								</c:choose>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-4">模板类型(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<select class="form-control select2 validate[required]"
									name="type" id="type">
									<option value="">请选择</option>
									<c:forEach var="item" items="${typeEnum}">
										<option value="${item.key}"
											<c:if test="${template.type eq item.key}">selected="selected"</c:if>>${item.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label class="control-label col-md-4">模板代码(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<c:choose>
									<c:when test="${empty template.id}">
										<input type="text" id="code" name="code"
											value="${template.code}"
											onkeyup="this.value=this.value.toUpperCase()"
											class="form-control validate[required,custom[onlyLetterNumberUnderline],ajax[ajaxCodeCheck]]"
											placeholder="请输入模板代码">
									</c:when>
									<c:otherwise>
										<input type="text" id="code" name="code"
											value="${template.code}"
											onkeyup="this.value=this.value.toUpperCase()"
											class="form-control"
											placeholder="请输入模板代码" readonly="readonly">
									</c:otherwise>
								</c:choose>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-4">模板名称(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<input type="text" name="name" value="${template.name}"
									class="form-control validate[required]" placeholder="请输入模板名称">
							</div>
						</div>
						
						<div class="form-group">
							<label class="control-label col-md-4">是否共享(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<select name="share" class="form-control">
									<c:forEach var="item" items="${yesNoEnum}">
										<option value="${item.key}"
											<c:if test="${! empty template.share && item.key eq template.share}"> selected="selected" </c:if>>${item.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label class="control-label col-md-4">背景图片可配置(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<select name="configBackgroundImage" class="form-control">
									<c:forEach var="item" items="${yesNoEnum}">
										<option value="${item.key}"
											<c:if test="${! empty template.configBackgroundImage && item.key eq template.configBackgroundImage}"> selected="selected" </c:if>>${item.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label class="control-label col-md-4">排序值(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<input type="text" name="sortIndex"
									value="${template.sortIndex}"
									class="form-control validate[required,custom[integer]]"
									placeholder="请输入排序值" />
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-4">状态(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<select name="status" class="form-control">
									<c:forEach var="item" items="${statusEnum}">
										<option value="${item.key}"
											<c:if test="${! empty template.status && item.key eq template.status}"> selected="selected" </c:if>>${item.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label class="control-label col-md-4">描述:</label>

							<div class="col-md-6">
								<textarea name="description" class="form-control">${template.description}</textarea>
							</div>
						</div>
						
						<div class="form-group">
							<label class="control-label col-md-4">效果图片:
							</label>

							<div class="col-md-6">
								<input type="hidden" id="backgroundImage"
									name="backgroundImage" value="${template.backgroundImage}">

								<c:if test="${empty template.backgroundImage}">
									<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
									<c:set var="fileinputMethod" value="fileinput-new" />
								</c:if>
								<c:if test="${! empty template.backgroundImage}">
									<c:set var="imageUrl"
										value="${fns:getImagePath(template.backgroundImage)}" />
									<c:set var="fileinputMethod" value="fileinput-exists" />
								</c:if>
								<div
									class="fileinput ${fileinputMethod} fileinput_backgroundImage"
									data-provides="fileinput">
									<div class="fileinput-preview thumbnail"
										data-trigger="fileinput" style="width: 80px; height: 80px;">
										<img src="${imageUrl}" alt="" />
									</div>
									<div>
										<span class="btn red btn-outline btn-file"> <span
											class="fileinput-new"> 选择图片 </span> <span
											class="fileinput-exists"> 更换图片 </span> <input type="file"
											name="..." accept=".jpg,.png">
										</span> <a href="javascript:;" class="btn red fileinput-exists"
											data-dismiss="fileinput"> 移除图片 </a>
									</div>
								</div>
								<div class="clearfix margin-top-10">
									<span class="label label-info">大小0X0</span>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty template.id}">
						<button class="btn btn-outline green"
							onclick="$.TemplateController.edit('${ctx}/template/template/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.TemplateController.edit('${ctx}/template/template/${template.id}/edit');">
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