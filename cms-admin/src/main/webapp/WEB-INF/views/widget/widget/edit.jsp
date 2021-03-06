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
						<c:when test="${empty widget.id}">
							<c:set var="methodDesc" value="增加" />
							<c:set var="appCode" value="${currentAppCode}" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
							<c:set var="appCode" value="${widget.appCode}" />
						</c:otherwise>
					</c:choose>
					${methodDesc}推荐位
				</h4>
			</div>
			<div class="modal-body">
				<input type="hidden" id="image1Data" name="image1Data"> <input
					type="hidden" id="image2Data" name="image2Data"> <input
					type="hidden" id="backgroundImageData" name="backgroundImageData">
				<form id="editForm">
					<input type="hidden" name="appCode" value="${appCode}">
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">推荐位代码(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<c:choose>
											<c:when test="${empty widget.id}">
												<input type="text" id="code" name="code"
													value="W_"
													onkeyup="this.value=this.value.toUpperCase()"
													class="form-control validate[required,custom[onlyLetterNumberUnderline],ajax[ajaxCodeCheck],maxSize[16]]"
													placeholder="请输入推荐位代码">
											</c:when>
											<c:otherwise>
												<input type="text" id="code" name="code"
													value="${widget.code}"
													onkeyup="this.value=this.value.toUpperCase()"
													class="form-control"
													placeholder="请输入推荐位代码" readonly="readonly"
													onclick="$.WidgetController.code();">
											</c:otherwise>
										</c:choose>
										<p class="help-block">推荐位代码以W_开头.</p>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">推荐位名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="name" value="${widget.name}"
											class="form-control validate[required]"
											placeholder="请输入推荐位名称">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">显示名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="title" value="${widget.title}"
											class="form-control validate[required]" placeholder="请输入显示名称">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">推荐位类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="type" class="form-control">
											<c:forEach var="item" items="${typeEnum}">
												<option value="${item.key}"
													<c:if test="${! empty widget.type && item.key eq widget.type}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
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
													<c:if test="${! empty widget.status && item.key eq widget.status}"> selected="selected" </c:if>>${item.value}</option>
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
											value="${widget.sortIndex}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入排序值" />
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">可配置的元素类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<div class="checkbox-list">
											<c:set var="configItemTypeAllSelected" value="0" />
											<c:if test="${empty widget.configItemTypes}">
												<c:set var="configItemTypeAllSelected" value="1" />
											</c:if>
											<label><input name="configItemTypes"
													<c:if test="${configItemTypeAllSelected eq 1 }"> checked </c:if>
													class="validate[required]" type="checkbox" value="" id="configItemTypeAll" onClick="$.WidgetController.clickConfigItemTypes()">全部</label>
											<c:forEach var="item" items="${itemTypeEnum}">
												<c:set var="configItemTypeSelected" value="" />
												<c:forEach var="configItem" items="${widget.configItemTypes}">
													<c:if test="${item.key eq configItem }">
														<c:set var="configItemTypeSelected" value="1" />
													</c:if>
												</c:forEach>
												<c:if test="${empty widget.configItemTypes}">
													<c:set var="configItemTypeSelected" value="0" />
												</c:if>
												<label><input name="configItemTypes"
													<c:if test="${configItemTypeSelected eq 1 || configItemTypeAllSelected eq 1}"> checked </c:if>
													class="validate[required]" type="checkbox" value="${item.key}">${item.value}</label>
											</c:forEach>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">描述:</label>

									<div class="col-md-9">
										<textarea name="description" class="form-control"
											placeholder="请输入栏目描述">${widget.description}</textarea>
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
										<select name="configImage1" class="form-control" onchange="$.WidgetController.changeConfigImage1(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty widget.configImage1 && item.key eq widget.configImage1}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6" id="configImage1_content_div" <c:if test="${widget.configImage1 != 1}"> style="display: none" </c:if>>
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
											value="${widget.configImage1Width}">
										<span class="">高:</span>
										<input type="text"
											class="form-control input-small-ext input-inline validate[required,custom[integer]]"
											id="configImage1Height"
											name="configImage1Height"
											value="${widget.configImage1Height}"> 
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
										<select name="configImage2" class="form-control" onchange="$.WidgetController.changeConfigImage2(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty widget.configImage2 && item.key eq widget.configImage2}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6" id="configImage2_content_div" <c:if test="${widget.configImage2 != 1}"> style="display: none" </c:if>>
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
											value="${widget.configImage2Width}">
										<span class="">高:</span>
										<input type="text"
											class="form-control input-small-ext input-inline validate[required,custom[integer]]"
											id="configImage2Height"
											name="configImage2Height"
											value="${widget.configImage2Height}"> 
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">显示元素个数(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="itemNum" value="${widget.itemNum}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入显示元素个数" />
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty widget.id}">
						<button class="btn btn-outline green"
							onclick="$.WidgetController.edit('${ctx}/widget/widget/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.WidgetController.edit('${ctx}/widget/widget/${widget.id}/edit');">
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