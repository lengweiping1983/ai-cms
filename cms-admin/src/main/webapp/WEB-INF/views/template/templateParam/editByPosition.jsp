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
						<c:when test="${empty templateParam.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					[${template.name}]${methodDesc}参数配置
				</h4>
			</div>
			<div class="modal-body">
				<input type="hidden" id="image1Data" name="image1Data"> <input
					type="hidden" id="image2Data" name="image2Data">
				<form id="editForm">
					<input type="hidden" name="id" value="${templateParam.id}" /> <input
						type="hidden" name="templateId" value="${template.id}" /><input
						type="hidden" id="positionId" name="positionId"
						value="${templateParam.positionId}" /> <input type="hidden"
						id="position" name="position" value="${templateParam.position}" />

					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select id="type" name="type" class="form-control" onchange="$.TemplateParamController.changeType(this.value);">
											<c:forEach var="item" items="${typeEnum}">
												<option value="${item.key}"
													<c:if test="${item.key eq templateParam.type}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="name" id="name"
											value="${templateParam.name}"
											class="form-control validate[required]">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">模式(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select id="mode" name="mode" class="form-control">
											<c:forEach var="item" items="${templateParamModeEnum}">
												<option value="${item.key}"
													<c:if test="${item.key eq templateParam.mode}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
										<p class="help-block">模式说明:<br/>
										<font color="red">必选</font>->页面定义时需要指定该元素!<br/>
										<font color="red">可选</font>->未选择该元素时，页面上不会展示该元素!
										</p>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">参数编号(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<c:if test="${empty templateParam.id}">
										<input type="text" name="number" id="number"
											value="NO"
											onkeyup="this.value=this.value.toUpperCase()"
											class="form-control validate[required]">
										</c:if>
										<c:if test="${! empty templateParam.id}">
										<input type="text" name="number" id="number"
											value="${templateParam.number}"
											onkeyup="this.value=this.value.toUpperCase()"
											readonly
											class="form-control validate[required]">
										</c:if>
										<p class="help-block">参数编号以NO开头.</p>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">参数值:
									</label>

									<div class="col-md-9">
										<input type="text" name="code" id="code"
											value="${templateParam.code}"
											onkeyup="this.value=this.value.toUpperCase()"
											class="form-control">
										<p class="help-block">默认值</p>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">参数值未写死在页面上(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="configCode" class="form-control">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty templateParam.configCode && item.key eq templateParam.configCode}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">参数分类(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select id="internalParamCategory" name="internalParamCategory" class="form-control">
											<c:forEach var="item" items="${templateParamCategoryEnum}">
												<option value="${item.key}"
													<c:if test="${item.key eq templateParam.internalParamCategory}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">展示风格:
									</label>

									<div class="col-md-9">
										<textarea name="styleConfig" class="form-control">${templateParam.styleConfig}</textarea>
										<p class="help-block">展示风格格式如下:<br/>
										<font color="red">风格1</font>=风格1名称<font color="blue">$</font><font color="red">风格2</font>=风格2名称<font color="blue">$</font><font color="red">风格3</font>=风格3名称
										</p>
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
											value="${templateParam.sortIndex}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入排序值" />
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">描述:
									</label>

									<div class="col-md-9">
										<textarea name="description" class="form-control">${templateParam.description}</textarea>
									</div>
								</div>
							</div>
						</div>
						
						<div id="ext_config_div" <c:if test="${templateParam.type != 4 && templateParam.type != 5}"> style="display: none" </c:if>>
						<div class="row">
							<div class="col-md-6" id="configItemTypes_4_div" <c:if test="${templateParam.type != 4}"> style="display: none" </c:if>>
								<div class="form-group">
									<label class="control-label col-md-3">可配置的元素类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<div class="checkbox-list">
											<c:set var="configItemTypeAllSelected" value="0" />
											<c:if test="${empty templateParam.configItemTypes}">
												<c:set var="configItemTypeAllSelected" value="1" />
											</c:if>
											<label><input name="configItemTypes_4"
													<c:if test="${configItemTypeAllSelected eq 1 }"> checked </c:if>
													class="validate[required]" type="checkbox" value="" id="configItemTypeAll_4" onClick="$.TemplateParamController.clickConfigItemTypes(4)">全部</label>
											<c:forEach var="item" items="${widgetItemTypeEnum}">
												<c:set var="configItemTypeSelected" value="" />
												<c:forEach var="configItem" items="${templateParam.configItemTypes}">
													<c:if test="${item.key eq configItem }">
														<c:set var="configItemTypeSelected" value="1" />
													</c:if>
												</c:forEach>
												<c:if test="${empty templateParam.configItemTypes}">
													<c:set var="configItemTypeSelected" value="0" />
												</c:if>
												<label><input name="configItemTypes_4"
													<c:if test="${configItemTypeSelected eq 1 || configItemTypeAllSelected eq 1}"> checked </c:if>
													class="validate[required]" type="checkbox" value="${item.key}">${item.value}</label>
											</c:forEach>
										</div>
									</div>
								</div>
							</div>
							
							<div class="col-md-6" id="configItemTypes_5_div" <c:if test="${templateParam.type != 5}"> style="display: none" </c:if>>
								<div class="form-group">
									<label class="control-label col-md-3">可配置的元素类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<div class="checkbox-list">
											<c:set var="configItemTypeAllSelected" value="0" />
											<c:if test="${empty templateParam.configItemTypes}">
												<c:set var="configItemTypeAllSelected" value="1" />
											</c:if>
											<label><input name="configItemTypes_5"
													<c:if test="${configItemTypeAllSelected eq 1 }"> checked </c:if>
													class="validate[required]" type="checkbox" value="" id="configItemTypeAll_5" onClick="$.TemplateParamController.clickConfigItemTypes(5)">全部</label>
											<c:forEach var="item" items="${categoryItemTypeEnum}">
												<c:set var="configItemTypeSelected" value="" />
												<c:forEach var="configItem" items="${templateParam.configItemTypes}">
													<c:if test="${item.key eq configItem }">
														<c:set var="configItemTypeSelected" value="1" />
													</c:if>
												</c:forEach>
												<c:if test="${empty templateParam.configItemTypes}">
													<c:set var="configItemTypeSelected" value="0" />
												</c:if>
												<label><input name="configItemTypes_5"
													<c:if test="${configItemTypeSelected eq 1 || configItemTypeAllSelected eq 1}"> checked </c:if>
													class="validate[required]" type="checkbox" value="${item.key}">${item.value}</label>
											</c:forEach>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="row" id="widget_div" <c:if test="${templateParam.type != 4}"> style="display: none" </c:if>>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">推荐位类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="widgetType" class="form-control">
											<c:forEach var="item" items="${widgetTypeEnum}">
												<option value="${item.key}"
													<c:if test="${! empty templateParam.widgetType && item.key eq templateParam.widgetType}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">显示元素个数(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="widgetItemNum" value="${templateParam.widgetItemNum}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入显示元素个数" />
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">元素横海报可配置(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="configImage1" class="form-control" onchange="$.TemplateParamController.changeConfigImage1(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty templateParam.configImage1 && item.key eq templateParam.configImage1}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6" id="configImage1_content_div" <c:if test="${templateParam.configImage1 != 1}"> style="display: none" </c:if>>
								<div class="form-group">
									<label class="control-label col-md-3">元素横海报大小(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<span class="">宽:</span>
										<input type="text"
											class="form-control input-small-ext input-inline validate[required,custom[integer]]"
											id="configImage1Width"
											name="configImage1Width"
											value="${templateParam.configImage1Width}">
										<span class="">高:</span>
										<input type="text"
											class="form-control input-small-ext input-inline validate[required,custom[integer]]"
											id="configImage1Height"
											name="configImage1Height"
											value="${templateParam.configImage1Height}"> 
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">元素竖海报可配置(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="configImage2" class="form-control" onchange="$.TemplateParamController.changeConfigImage2(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty templateParam.configImage2 && item.key eq templateParam.configImage2}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6" id="configImage2_content_div" <c:if test="${templateParam.configImage2 != 1}"> style="display: none" </c:if>>
								<div class="form-group">
									<label class="control-label col-md-3">元素竖海报大小(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<span class="">宽:</span>
										<input type="text"
											class="form-control input-small-ext input-inline validate[required,custom[integer]]"
											id="configImage2Width"
											name="configImage2Width"
											value="${templateParam.configImage2Width}">
										<span class="">高:</span>
										<input type="text"
											class="form-control input-small-ext input-inline validate[required,custom[integer]]"
											id="configImage2Height"
											name="configImage2Height"
											value="${templateParam.configImage2Height}"> 
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
					<c:when test="${empty templateParam.id}">
						<button class="btn btn-outline green"
							onclick="$.TemplateParamController.toDeleteByPositionBeforeNotSave();">
							<i class="fa fa-remove"></i>删除
						</button>
						<button class="btn btn-outline green"
							onclick="$.TemplateParamController.editByPosition('${ctx}/template/${template.id}/templateParam/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>

						<button class="btn btn-outline green"
							onclick="$.TemplateParamController.toDeleteByPosition('${ctx}/template/${template.id}/templateParam/${templateParam.id}/delete','${templateParam.name}');">
							<i class="fa fa-remove"></i>删除
						</button>
						<button class="btn btn-outline green"
							onclick="$.TemplateParamController.editByPosition('${ctx}/template/${template.id}/templateParam/${templateParam.id}/edit');">
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