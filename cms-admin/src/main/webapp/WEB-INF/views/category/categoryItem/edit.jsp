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
						<c:when test="${empty categoryItem.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					[${category.name}]${methodDesc}栏目项
				</h4>
			</div>
			<div class="modal-body">
				<input type="hidden" id="image1Data" name="image1Data"> <input
					type="hidden" id="image2Data" name="image2Data">
				<form id="editForm">
					<input type="hidden" name="id" value="${categoryItem.id}" /> <input
						type="hidden" name="categoryId" value="${category.id}" /> <input
						type="hidden" name="itemId" id="itemId"
						value="${categoryItem.itemId}" />

					<div class="content form-horizontal">
						<c:choose>
							<c:when test="${empty categoryItem.id}">
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">元素类型(<span
												class="required">*</span>):
											</label>

											<div class="col-md-9">
												<select id="itemType" name="itemType" class="form-control"
													onchange="$.CategoryItemController.changeSelectItem();">
													<c:forEach var="item" items="${typeEnum}">
														<c:set var="configItemSelected" value="" />
														<c:forEach var="configItem" items="${category.configItemTypes}">
															<c:if test="${item.key eq configItem }">
																<c:set var="configItemSelected" value="1" />
															</c:if>
														</c:forEach>
														<c:if test="${empty category.configItemTypes}">
															<c:set var="configItemSelected" value="1" />
														</c:if>
														<c:if test="${configItemSelected eq 1 }">
														<option value="${item.key}"
															<c:if test="${item.key eq categoryItem.itemType}"> selected="selected" </c:if>>${item.value}</option>
														</c:if>
													</c:forEach>
												</select>
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">元素名称(<span
												class="required">*</span>): </label>

											<div class="col-md-9">
												<div class="input-group">
													<input type="text" class="form-control validate[required]"
														name="itemName" id="itemName" placeholder="点击我选择元素"
														readonly="readonly"
														onclick="$.CategoryItemController.toSelectItem();" /> <span
														class="input-group-btn">
														<button onclick="$.CategoryItemController.toSelectItem();"
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
												class="required">*</span>): </label>

											<div class="col-md-9">
												<input type="text" id="itemTitle"
													value="${categoryItem.itemTitle}" readonly="readonly"
													class="form-control">
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">元素状态(<span
												class="required">*</span>): </label>

											<div class="col-md-9">
												<select id="itemStatus" name="itemStatus"
													disabled="disabled" class="form-control">
													<c:forEach var="item" items="${statusEnum}">
														<option value="${item.key}"
															<c:if test="${! empty categoryItem.itemStatus && item.key eq categoryItem.itemStatus}"> selected="selected" </c:if>>${item.value}</option>
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
												class="required">*</span>): </label>

											<div class="col-md-9">
												<select id="itemType" name="itemType" disabled="disabled"
													class="form-control">
													<c:forEach var="item" items="${typeEnum}">
														<option value="${item.key}"
															<c:if test="${item.key eq categoryItem.itemType}"> selected="selected" </c:if>>${item.value}</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">元素名称(<span
												class="required">*</span>): </label>

											<div class="col-md-9">
												<input type="text" value="${categoryItem.itemName}"
													readonly="readonly" class="form-control">
											</div>
										</div>
									</div>
								</div>

								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">显示名称(<span
												class="required">*</span>): </label>

											<div class="col-md-9">
												<input type="text" value="${categoryItem.itemTitle}"
													readonly="readonly" class="form-control">
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">元素状态(<span
												class="required">*</span>): </label>

											<div class="col-md-9">
												<select id="itemStatus" name="itemStatus"
													disabled="disabled" class="form-control">
													<c:forEach var="item" items="${statusEnum}">
														<option value="${item.key}"
															<c:if test="${! empty categoryItem.itemStatus && item.key eq categoryItem.itemStatus}"> selected="selected" </c:if>>${item.value}</option>
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
										<input type="text" name="title" value="${categoryItem.title}"
											class="form-control" placeholder="请输入定制名称" />
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
													<c:if test="${! empty categoryItem.status && item.key eq categoryItem.status}"> selected="selected" </c:if>>${item.value}</option>
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
											value="${categoryItem.sortIndex}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入排序值" />
										<c:if test="${empty categoryItem.id}">
											<p class="help-block">当前元素会插入到该位置，之后的元素会自动往后排.</p>
										</c:if>
									</div>
								</div>
							</div>
						</div>


						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label class="control-label col-md-6">元素横海报: </label>

									<div class="col-md-6">
										<c:if test="${empty categoryItem.itemImage1}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty categoryItem.itemImage1}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(categoryItem.itemImage1)}" />
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
										<c:if test="${empty categoryItem.itemImage2}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty categoryItem.itemImage2}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(categoryItem.itemImage2)}" />
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

							<div class="col-md-3" <c:if test="${category.configImage1 != 1}"> style="display: none" </c:if>>
								<div class="form-group">
									<label class="control-label col-md-6">定制横海报: </label>

									<div class="col-md-6">
										<input type="hidden" id="image1" name="image1"
											value="${categoryItem.image1}">

										<c:if test="${empty categoryItem.image1}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty categoryItem.image1}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(categoryItem.image1)}" />
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
											<span class="label label-info">大小${category.configImage1Width}X${category.configImage1Height}</span>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-3" <c:if test="${category.configImage2 != 1}"> style="display: none" </c:if>>
								<div class="form-group">
									<label class="control-label col-md-6">定制竖海报: </label>

									<div class="col-md-6">
										<input type="hidden" id="image2" name="image2"
											value="${categoryItem.image2}">
										<c:if test="${empty categoryItem.image2}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty categoryItem.image2}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(categoryItem.image2)}" />
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
											<span class="label label-info">大小${category.configImage2Width}X${category.configImage2Height}</span>
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
												value='<fmt:formatDate value="${categoryItem.validTime}"
                                                                                       pattern="yyyy-MM-dd HH:mm"/>'
												size="16"
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
									<label class="control-label col-md-3">失效时间(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<div class="input-group date form_datetime">
											<input type="text" id="expiredTime" name="expiredTime"
												value='<fmt:formatDate value="${categoryItem.expiredTime}"
                                                                                       pattern="yyyy-MM-dd HH:mm"/>'
												size="16"
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
									<label class="control-label col-md-3">用户分组: </label>

									<div class="col-md-9">
										<div class="checkbox-list">
											<c:forEach var="item" items="${subscriberGroupList}">
												<c:set var="subscriberGroupSelected" value="" />
												<c:forEach var="subscriberGroup" items="${categoryItem.groupCodes}">
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
			</div>
			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty categoryItem.id}">
						<button class="btn btn-outline green"
							onclick="$.CategoryItemController.edit('${ctx}/category/${category.id}/categoryItem/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.CategoryItemController.edit('${ctx}/category/${category.id}/categoryItem/${categoryItem.id}/edit');">
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