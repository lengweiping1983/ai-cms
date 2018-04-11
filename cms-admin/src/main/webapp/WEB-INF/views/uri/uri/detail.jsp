<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade bs-modal-lg" tabindex="-1" role="basic"
	aria-hidden="true">
	<div class="modal-dialog modal-full">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">
					查看页面
				</h4>
			</div>
			<div class="modal-body" id="modal-body">
				<input type="hidden" id="iconData" name="iconData"> <input
					type="hidden" id="iconFocusData" name="iconFocusData">
				<input type="hidden" id="backgroundImageData" name="backgroundImageData">
				<input type="hidden" id="logoImageData" name="logoImageData">
				<form id="editForm">
					<input type="hidden" name="appCode" value="${appCode}">
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">页面类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="type" class="form-control">
											<c:forEach var="item" items="${uriTypeEnum}">
												<option value="${item.key}"
													<c:if test="${! empty uri.type && item.key eq uri.type}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">页面代码(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<c:choose>
											<c:when test="${empty uri.id}">
												<input type="text" id="code" name="code"
													value="P_"
													onkeyup="this.value=this.value.toUpperCase()"
													class="form-control validate[required,custom[onlyLetterNumberUnderline],ajax[ajaxCodeCheck],maxSize[16]]"
													placeholder="请输入页面代码">
											</c:when>
											<c:otherwise>
												<input type="text" id="code" name="code"
													value="${uri.code}"
													onkeyup="this.value=this.value.toUpperCase()"
													class="form-control"
													placeholder="请输入页面代码" readonly="readonly"
													onclick="$.UriController.code();">
											</c:otherwise>
										</c:choose>
										<p class="help-block">页面代码以P_开头.</p>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">管理名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="name" value="${uri.name}"
											class="form-control validate[required]" placeholder="请输入管理名称">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">显示名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="title" value="${uri.title}"
											class="form-control validate[required]" placeholder="请输入显示名称">
									</div>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">TAG: </label>

									<div class="col-md-9">
										<input type="text" name="tag" value="${uri.tag}"
											class="form-control" placeholder="请输入TAG">
										<p class="help-block">多个使用','分割.</p>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">关键字: </label>

									<div class="col-md-9">
										<input type="text" name="keyword" value="${uri.keyword}"
											class="form-control" placeholder="请输入关键字">
										<p class="help-block">多个使用','分割.</p>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">上线状态(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="status" class="form-control">
											<c:forEach var="item" items="${statusEnum}">
												<option value="${item.key}"
													<c:if test="${! empty uri.status && item.key eq uri.status}"> selected="selected" </c:if>>${item.value}</option>
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
										<input type="text" name="sortIndex" value="${uri.sortIndex}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入排序值" />
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label class="control-label col-md-6">默认图标: </label>

									<div class="col-md-6">
										<input type="hidden" id="icon" name="icon" value="${uri.icon}">

										<c:if test="${empty uri.icon}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty uri.icon}">
											<c:set var="imageUrl" value="${fns:getImagePath(uri.icon)}" />
											<c:set var="fileinputMethod" value="fileinput-exists" />
										</c:if>
										<div class="fileinput ${fileinputMethod} fileinput_icon"
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
							<div class="col-md-3">
								<div class="form-group">
									<label class="control-label col-md-6">激活图标: </label>

									<div class="col-md-6">
										<input type="hidden" id="iconFocus" name="iconFocus"
											value="${uri.iconFocus}">
										<c:if test="${empty uri.iconFocus}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty uri.iconFocus}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(uri.iconFocus)}" />
											<c:set var="fileinputMethod" value="fileinput-exists" />
										</c:if>
										<div class="fileinput ${fileinputMethod} fileinput_iconFocus"
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
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">描述:</label>

									<div class="col-md-9">
										<textarea name="description" rows="8" class="form-control">${uri.description}</textarea>
									</div>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">本系统页面(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="internal" class="form-control"
											onchange="$.UriController.changeSelectType(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty uri.internal && item.key eq uri.internal}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>

							<div id="uri_url"
								<c:if test="${uri.internal != 0}"> style="display: none" </c:if>>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">链接地址(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="url" value="${uri.url}"
												class="form-control validate[required]"
												placeholder="请输入链接地址">
										</div>
									</div>
								</div>
							</div>

							<div id="uri_template_code"
								<c:if test="${uri.internal != 1}"> style="display: none" </c:if>>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">使用模板(<span
											class="required">*</span>):
										</label>
										<div class="col-md-9">
											<div class="input-group">
												<input type="hidden" id="configBackgroundImage" name="configBackgroundImage" value="">
												<input type="hidden" id="lastTemplateId" name="lastTemplateId" value="${template.id}">
												<input type="hidden" id="templateId" name="templateId" value="${template.id}" onchange="$.UriController.changeTemplateId(this.value);">
												<input type="hidden" id="templateCode" name="templateCode" value="${template.code}">
												<input type="text" class="form-control validate[required]"
													id="templateName" name="templateName"
													value="${template.name}" placeholder="点击我选择模板"
													readonly="readonly"
													onclick="$.TemplateController.toSelectItem();" /> <span
													class="input-group-btn">
													<button onclick="$.TemplateController.toSelectItem();"
														class="btn btn-success" type="button">
														<i class="fa fa-arrow-left fa-fw" /></i> 选择模板
													</button>
												</span>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
				
				<div id="uriParamConfig_div" 
					<c:if test="${uri.internal != 1}"> style="display: none" </c:if>>
				
				<h4 class="form-section">页面配置:</h4>
				<div class="row">
					<div class="col-md-6" id="logoImage_div" <c:if test="${uri.internal != 1}"> style="display: none" </c:if>>
						<div class="form-group">
							<label class="control-label col-md-3">LOGO: </label>

							<div class="col-md-9">
								<input type="hidden" id="logoImage"
									name="logoImage" value="${uri.logoImage}">

								<c:if test="${empty uri.logoImage}">
									<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
									<c:set var="fileinputMethod" value="fileinput-new" />
								</c:if>
								<c:if test="${! empty uri.logoImage}">
									<c:set var="imageUrl"
										value="${fns:getImagePath(uri.logoImage)}" />
									<c:set var="fileinputMethod" value="fileinput-exists" />
								</c:if>
								<div
									class="fileinput ${fileinputMethod} fileinput_logoImage"
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
					<div class="col-md-6" id="backgroundImage_div" <c:if test="${uri.internal != 1 || empty template.code}"> style="display: none" </c:if>>
						<div class="form-group">
							<label class="control-label col-md-3">背景图片: </label>

							<div class="col-md-9">
								<input type="hidden" id="backgroundImage"
									name="backgroundImage" value="${uri.backgroundImage}">

								<c:if test="${empty uri.backgroundImage}">
									<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
									<c:set var="fileinputMethod" value="fileinput-new" />
								</c:if>
								<c:if test="${! empty uri.backgroundImage}">
									<c:set var="imageUrl"
										value="${fns:getImagePath(uri.backgroundImage)}" />
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
					<div class="col-md-6" id="internalParamCategory_div" style="display: none">
					</div>
				</div>
					
				<h4 class="form-section">页面参数配置列表:</h4>
				<input type="hidden" id="urlParamConfigMode" name="urlParamConfigMode" value="0">
				<input type="hidden" id="currentUriId" name="currentUriId" value="${uri.id}">
				<input type="hidden" id="currentParamTemplateId" name="currentParamTemplateId" value="${uri.templateId}">
				<input type="hidden" id="currentParamTrId" name="currentParamTrId" value="">
				<input type="hidden" id="currentParamType" name="currentParamType" value="">
				<input type="hidden" id="currentParamNumber" name="currentParamNumber" value="">
				<form id="paramForm">
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-12">
								<table
									class="table dataTable table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>类型</th>
											<th>模式</th>
											<th>参数编号</th>
											<th>参数值</th>
											<th>名称</th>
											<th>是否存在</th>
											<th>自动创建</th>
											<th>展示风格</th>
											<th>元素项横海报</th>
											<th>元素项竖海报</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody id="param_list">
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</form>
				<p class="help-block">模式说明:<br/>
				<font color="red">必选</font>->页面定义时需要指定该元素!<br/>
				<font color="red">可选</font>->未选择该元素时，页面上不会展示该元素!
				</p>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>