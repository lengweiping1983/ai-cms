<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-full">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">
					<c:choose>
						<c:when test="${empty albumItem.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					[${album.name}]${methodDesc}专题项
				</h4>
			</div>
			<div class="modal-body">
				<input type="hidden" id="image1Data" name="image1Data"> <input
					type="hidden" id="image2Data" name="image2Data"> <input
					type="hidden" id="backgroundImageData" name="backgroundImageData">

				<form id="editForm">
					<input type="hidden" id="internalParamCategory" name="internalParamCategory" value="${albumItem.internalParamCategory}" />
					<input type="hidden" id="internalParam" name="internalParam"
						value="${albumItem.internalParam}" /> <input type="hidden"
						name="id" value="${albumItem.id}" /> <input type="hidden"
						name="albumId" value="${album.id}" /> <input type="hidden"
						name="itemId" id="itemId" value="${albumItem.itemId}"
						onchange="$.AlbumItemController.changeItemIdValue();" /> <input
						type="hidden" id="positionId" name="positionId"
						value="${albumItem.positionId}" /> <input type="hidden"
						id="position" name="position" value="${albumItem.position}" />

					<div class="content form-horizontal">
						<c:choose>
							<c:when test="${empty albumItem.id}">
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">元素类型(<span
												class="required">*</span>):
											</label>

											<div class="col-md-9">
												<select id="itemType" name="itemType" class="form-control"
													onchange="$.AlbumItemController.changeSelectItem();">
													<c:forEach var="item" items="${typeEnum}">
														<c:set var="configItemSelected" value="" />
														<c:forEach var="configItem"
															items="${album.configItemTypes}">
															<c:if test="${item.key eq configItem }">
																<c:set var="configItemSelected" value="1" />
															</c:if>
														</c:forEach>
														<c:if test="${empty album.configItemTypes}">
															<c:set var="configItemSelected" value="1" />
														</c:if>
														<c:if test="${configItemSelected eq 1 }">
															<option value="${item.key}"
																<c:if test="${item.key eq albumItem.itemType}"> selected="selected" </c:if>>${item.value}</option>
														</c:if>
													</c:forEach>
												</select>
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">元素名称(<span
												class="required">*</span>):
											</label>

											<div class="col-md-9">
												<div class="input-group">
													<input type="text" class="form-control validate[required]"
														name="itemName" id="itemName" placeholder="点击我选择元素"
														readonly="readonly"
														onclick="$.AlbumItemController.toSelectItem();" /> <span
														class="input-group-btn">
														<button onclick="$.AlbumItemController.toSelectItem();"
															class="btn btn-success" type="button">
															<i class="fa fa-arrow-left fa-fw" /></i> 选择元素
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
											<label class="control-label col-md-3">显示名称(<span
												class="required">*</span>):
											</label>

											<div class="col-md-9">
												<input type="text" id="itemTitle"
													value="${albumItemView.itemTitle}" readonly="readonly"
													class="form-control">
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">元素状态(<span
												class="required">*</span>):
											</label>

											<div class="col-md-9">
												<select id="itemStatus" name="itemStatus"
													disabled="disabled" class="form-control">
													<c:forEach var="item" items="${statusEnum}">
														<option value="${item.key}"
															<c:if test="${! empty albumItemView.itemStatus && item.key eq albumItemView.itemStatus}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</div>
								</div>
							</c:when>
							<c:otherwise>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">元素类型(<span
												class="required">*</span>):
											</label>

											<div class="col-md-9">
												<select id="itemType" name="itemType" disabled="disabled"
													class="form-control">
													<c:forEach var="item" items="${typeEnum}">
														<option value="${item.key}"
															<c:if test="${item.key eq albumItem.itemType}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">元素名称(<span
												class="required">*</span>):
											</label>

											<div class="col-md-9">
												<input type="text" value="${albumItemView.itemName}"
													readonly="readonly" class="form-control">
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
												<input type="text" id="itemTitle"
													value="${albumItemView.itemTitle}" readonly="readonly"
													class="form-control">
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">元素状态(<span
												class="required">*</span>):
											</label>

											<div class="col-md-9">
												<select id="itemStatus" name="itemStatus"
													disabled="disabled" class="form-control">
													<c:forEach var="item" items="${statusEnum}">
														<option value="${item.key}"
															<c:if test="${! empty albumItemView.itemStatus && item.key eq albumItemView.itemStatus}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</div>
								</div>
							</c:otherwise>
						</c:choose>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">定制名称: </label>

									<div class="col-md-9">
										<input type="text" id="title" name="title"
											value="${albumItem.title}" class="form-control"
											placeholder="请输入定制名称" />
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">上线状态(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="status" class="form-control">
											<c:forEach var="item" items="${statusEnum}">
												<option value="${item.key}"
													<c:if test="${! empty albumItem.status && item.key eq albumItem.status}"> selected="selected" </c:if>>${item.value}</option>
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
											value="${albumItem.sortIndex}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入排序值" />
										<c:if test="${empty albumItem.id}">
											<p class="help-block">当前元素会插入到该位置，之后的元素会自动往后排.</p>
										</c:if>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">跳转方式(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select id="jumpType" name="jumpType" class="form-control">
											<c:forEach var="item" items="${jumpTypeEnum}">
												<option value="${item.key}"
													<c:if test="${item.key eq albumItem.jumpType}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">跳转元素类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select id="jumpItemType" name="jumpItemType"
											class="form-control"
											onchange="$.AlbumItemController.changeSelectJumpItem();">
											<c:forEach var="item" items="${albumJumpItemTypeEnum}">
												<option value="${item.key}"
													<c:if test="${item.key eq albumItem.jumpItemType}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div id="jump_item_id"
								<c:if test="${empty albumItem.jumpItemType || albumItem.jumpItemType eq 0 || albumItem.jumpItemType eq 99}"> style="display: none" </c:if>>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">跳转元素名称(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<div class="input-group">
												<input type="hidden" name="jumpItemId" id="jumpItemId"
													value="${albumItem.jumpItemId}" /> <input type="text"
													class="form-control validate[required]" name="jumpItemName"
													id="jumpItemName" value="${jumpItemName}"
													placeholder="点击我选择元素" readonly="readonly"
													onclick="$.AlbumItemController.toSelectJumpItem();" /> <span
													class="input-group-btn">
													<button onclick="$.AlbumItemController.toSelectJumpItem();"
														class="btn btn-success" type="button">
														<i class="fa fa-arrow-left fa-fw" /></i> 选择元素
													</button>
												</span>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div id="uri_url" <c:if test="${empty albumItem.jumpItemType || albumItem.jumpItemType eq 0 || albumItem.jumpItemType ne 99}"> style="display: none" </c:if>>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">链接地址(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="url" value="${albumItem.url}"
												class="form-control validate[required]"
												placeholder="请输入链接地址">
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">跳转参数1: </label>

									<div class="col-md-9">
										<input type="text" id="jumpParam1" name="jumpParam1"
											value="${albumItem.jumpParam1}" class="form-control"
											placeholder="请输入跳转参数1" />
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">跳转参数2: </label>

									<div class="col-md-9">
										<input type="text" id="jumpParam2" name="jumpParam2"
											value="${albumItem.jumpParam2}" class="form-control"
											placeholder="请输入跳转参数2" />
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label class="control-label col-md-6">元素横海报: </label>

									<div class="col-md-6">
										<c:if test="${empty albumItemView.itemImage1}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty albumItemView.itemImage1}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(albumItemView.itemImage1)}" />
											<c:set var="fileinputMethod" value="fileinput-exists" />
										</c:if>
										<div class="fileinput ${fileinputMethod} fileinput_itemImage1"
											data-provides="fileinput">
											<div class="fileinput-preview thumbnail"
												data-trigger="fileinput" style="width: 80px; height: 80px;">

												<img id="itemImage1_img" src="${imageUrl}" alt="" />
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="control-label col-md-6">元素竖海报: </label>

									<div class="col-md-6">
										<c:if test="${empty albumItemView.itemImage2}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty albumItemView.itemImage2}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(albumItemView.itemImage2)}" />
											<c:set var="fileinputMethod" value="fileinput-exists" />
										</c:if>
										<div class="fileinput ${fileinputMethod} fileinput_itemImage2"
											data-provides="fileinput">
											<div class="fileinput-preview thumbnail"
												data-trigger="fileinput" style="width: 80px; height: 80px;">
												<img id="itemImage2_img" src="${imageUrl}" alt="" />
											</div>
										</div>
									</div>
								</div>
							</div>

							<div class="col-md-3"
								<c:if test="${album.configImage1 != 1}"> style="display: none" </c:if>>
								<div class="form-group">
									<label class="control-label col-md-6">定制横海报: </label>

									<div class="col-md-6">
										<input type="hidden" id="image1" name="image1"
											value="${albumItem.image1}">

										<c:if test="${empty albumItem.image1}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty albumItem.image1}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(albumItem.image1)}" />
											<c:set var="fileinputMethod" value="fileinput-exists" />
										</c:if>
										<div class="fileinput ${fileinputMethod} fileinput_image1"
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
											<span class="label label-info">大小${album.configImage1Width}X${album.configImage1Height}</span>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-3"
								<c:if test="${album.configImage2 != 1}"> style="display: none" </c:if>>
								<div class="form-group">
									<label class="control-label col-md-6">定制竖海报: </label>

									<div class="col-md-6">
										<input type="hidden" id="image2" name="image2"
											value="${albumItem.image2}">
										<c:if test="${empty albumItem.image2}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty albumItem.image2}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(albumItem.image2)}" />
											<c:set var="fileinputMethod" value="fileinput-exists" />
										</c:if>
										<div class="fileinput ${fileinputMethod} fileinput_image2"
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
											<span class="label label-info">大小${album.configImage2Width}X${album.configImage2Height}</span>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">生效时间(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<div class="input-group date form_datetime">
											<input type="text" id="validTime" name="validTime"
												value='<fmt:formatDate value="${albumItem.validTime}"
                                                                                       pattern="yyyy-MM-dd HH:mm"/>'
												size="16" readonly class="form-control validate[required]">
											<span class="input-group-btn">
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
									<label class="control-label col-md-3">失效时间(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<div class="input-group date form_datetime">
											<input type="text" id="expiredTime" name="expiredTime"
												value='<fmt:formatDate value="${albumItem.expiredTime}"
                                                                                       pattern="yyyy-MM-dd HH:mm"/>'
												size="16" readonly class="form-control validate[required]">
											<span class="input-group-btn">
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
									<label class="control-label col-md-3">用户分组: </label>

									<div class="col-md-9">
										<div class="checkbox-list">
											<c:forEach var="item" items="${subscriberGroupList}">
												<c:set var="subscriberGroupSelected" value="" />
												<c:forEach var="subscriberGroup"
													items="${albumItem.groupCodes}">
													<c:if test="${item.code == subscriberGroup }">
														<c:set var="subscriberGroupSelected" value="1" />
													</c:if>
												</c:forEach>
												<label><input name="groupCodes"
													<c:if test="${subscriberGroupSelected eq 1 }"> checked </c:if>
													type="checkbox" value="${item.code}">${item.name}</label>
											</c:forEach>
										</div>
										<span class="label label-info">不选择所有用户分组可见.</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>

				<div id="uriParamConfig_div" style="display: none">
				
					<h4 class="form-section">页面配置:</h4>
					<div class="row">
						<div class="col-md-6" id="backgroundImage_div" style="display: none">
							<div class="form-group">
								<label class="control-label col-md-3">背景图片: </label>
	
								<div class="col-md-9">
									<input type="hidden" id="backgroundImage"
										name="backgroundImage" value="${albumItem.backgroundImage}">
	
									<c:if test="${empty albumItem.backgroundImage}">
										<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
										<c:set var="fileinputMethod" value="fileinput-new" />
									</c:if>
									<c:if test="${! empty albumItem.backgroundImage}">
										<c:set var="imageUrl"
											value="${fns:getImagePath(albumItem.backgroundImage)}" />
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
							<div class="form-group">
								<label class="control-label col-md-3">参数分类(<span
									class="required">*</span>):
								</label>
	
								<div class="col-md-9">
									<select class="form-control" onchange="$.AlbumItemController.changeInternalParamCategory(this.value);">
										<c:forEach var="item" items="${templateParamCategoryEnum}">
											<option value="${item.key}"
												<c:if test="${item.key eq albumItem.internalParamCategory}"> selected="selected" </c:if>>${item.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
					</div>
					
					<h4 class="form-section">页面参数配置列表:</h4>
					<input type="hidden" id="urlParamConfigMode" name="urlParamConfigMode" value="1">
					<input type="hidden" id="currentUriId" name="currentUriId" value="">
					<input type="hidden" id="currentParamTemplateId" name="currentParamTemplateId" value="">
					<input type="hidden" id="currentParamTrId" name="currentParamTrId" value="">
					<input type="hidden" id="currentParamType" name="currentParamType" value="">
					<input type="hidden" id="currentParamNumber" name="currentParamNumber" value="">
					<form id="paramForm">
						<div class="content form-horizontal">
							<div class="row">
								<div class="col-md-12">
									<table id="uriParam_table"
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
				<c:choose>
					<c:when test="${empty albumItem.id}">
						<button class="btn btn-outline green"
							onclick="$.AlbumItemController.edit('${ctx}/album/${album.id}/albumItem/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.AlbumItemController.edit('${ctx}/album/${album.id}/albumItem/${albumItem.id}/edit');">
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