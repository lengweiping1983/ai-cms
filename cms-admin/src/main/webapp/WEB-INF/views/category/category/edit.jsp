<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade bs-modal-lg" tabindex="-1" role="basic"
	aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">
					<c:choose>
						<c:when test="${empty category.id}">
							<c:set var="methodDesc" value="增加" />
							<c:set var="appCode" value="${currentAppCode}" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
							<c:set var="appCode" value="${category.appCode}" />
						</c:otherwise>
					</c:choose>
					${methodDesc}栏目
					<c:choose>
						<c:when test="${!empty parent}">
							<c:set var="parentId" value="${parent.id}" />
							<c:set var="parentName" value="${parent.name}" />
						</c:when>
						<c:otherwise>
							<c:set var="parentId" value="" />
							<c:set var="parentName" value="" />
						</c:otherwise>
					</c:choose>
				</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="appCode" value="${appCode}"> <input
						type="hidden" name="id" value="${category.id}" /> <input
						type="hidden" name="parentId" id="parentId" value="${parentId}" />

					<div class="content form-horizontal">

						<tags:message content="${message}" />

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">所属栏目:</label>

									<div class="col-md-9">
										<div class="input-group">
											<input type="text" class="form-control"
												name="category_parent_name" id="category_parent_name"
												value="${parentName}" placeholder="点击我选择所属栏目"
												readonly="readonly"
												onclick="$.CategoryController.showTree('${category.id}');" />
											<span class="input-group-btn">
												<button
													onclick="$.CategoryController.showTree('${category.id}');"
													class="btn btn-success" type="button">
													<i class="fa fa-arrow-left fa-fw" /></i> 选择栏目
												</button>
											</span>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">栏目代码(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<c:choose>
											<c:when test="${empty category.id}">
												<input type="text" id="code" name="code"
													value="C_"
													onkeyup="this.value=this.value.toUpperCase()"
													class="form-control validate[required,custom[onlyLetterNumberUnderline],ajax[ajaxCodeCheck],maxSize[16]]"
													placeholder="请输入栏目代码">
											</c:when>
											<c:otherwise>
												<input type="text" id="code" name="code"
													value="${category.code}"
													onkeyup="this.value=this.value.toUpperCase()"
													class="form-control"
													placeholder="请输入栏目代码" readonly="readonly"
													onclick="$.CategoryController.code();">
											</c:otherwise>
										</c:choose>
										<p class="help-block">栏目代码以C_开头.</p>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">栏目名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="name" value="${category.name}"
											class="form-control validate[required]" placeholder="请输入栏目名称">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">显示名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="title" value="${category.title}"
											class="form-control validate[required]" placeholder="请输入显示名称">
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">栏目类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="type" class="form-control">
											<c:forEach var="item" items="${typeEnum}">
												<option value="${item.key}"
													<c:if test="${! empty category.type && item.key eq category.type}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">可配置的元素类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<div class="checkbox-list">
											<c:set var="configItemTypeAllSelected" value="0" />
											<c:if test="${empty category.configItemTypes}">
												<c:set var="configItemTypeAllSelected" value="1" />
											</c:if>
											<label><input name="configItemTypes"
													<c:if test="${configItemTypeAllSelected eq 1 }"> checked </c:if>
													class="validate[required]" type="checkbox" value="" id="configItemTypeAll" onClick="$.CategoryController.clickConfigItemTypes()">全部</label>
											<c:forEach var="item" items="${itemTypeEnum}">
												<c:set var="configItemTypeSelected" value="" />
												<c:forEach var="configItem" items="${category.configItemTypes}">
													<c:if test="${item.key eq configItem }">
														<c:set var="configItemTypeSelected" value="1" />
													</c:if>
												</c:forEach>
												<c:if test="${empty category.configItemTypes}">
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
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">元素横海报可配置(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="configImage1" class="form-control" onchange="$.CategoryController.changeConfigImage1(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty category.configImage1 && item.key eq category.configImage1}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6" id="configImage1_content_div" <c:if test="${category.configImage1 != 1}"> style="display: none" </c:if>>
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
											value="${category.configImage1Width}">
										<span class="">高:</span>
										<input type="text"
											class="form-control input-small-ext input-inline validate[required,custom[integer]]"
											id="configImage1Height"
											name="configImage1Height"
											value="${category.configImage1Height}"> 
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
										<select name="configImage2" class="form-control" onchange="$.CategoryController.changeConfigImage2(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty category.configImage2 && item.key eq category.configImage2}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6" id="configImage2_content_div" <c:if test="${category.configImage2 != 1}"> style="display: none" </c:if>>
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
											value="${category.configImage2Width}">
										<span class="">高:</span>
										<input type="text"
											class="form-control input-small-ext input-inline validate[required,custom[integer]]"
											id="configImage2Height"
											name="configImage2Height"
											value="${category.configImage2Height}"> 
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
													<c:if test="${! empty category.status && item.key eq category.status}"> selected="selected" </c:if>>${item.value}</option>
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
											value="${category.sortIndex}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入排序值" />
									</div>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">描述:</label>

									<div class="col-md-9">
										<textarea name="description" class="form-control"
											placeholder="请输入栏目描述">${category.description}</textarea>
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
											<tags:injectionStatus value='${category.injectionStatus}' />
										</p>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">运营商侧代码: </label>

									<div class="col-md-9">
										<input type="text" name="partnerItemCode"
											value="${category.partnerItemCode}" class="form-control"
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
					<c:when test="${empty category.id}">
						<button class="btn btn-outline green"
							onclick="$.CategoryController.add('${ctx}/category/category/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.CategoryController.edit('${ctx}/category/category/${category.id}/edit');">
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