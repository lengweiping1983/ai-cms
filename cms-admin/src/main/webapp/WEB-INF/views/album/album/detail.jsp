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
					查看专题
				</h4>
			</div>
			<div class="modal-body" id="modal-body">
				<input type="hidden" id="image1Data" name="image1Data"> <input
					type="hidden" id="image2Data" name="image2Data"> <input
					type="hidden" id="backgroundImageData" name="backgroundImageData">

				<input type="hidden" id="bgUpData" name="bgUpData"> <input
					type="hidden" id="bgDownData" name="bgDownData"> <input
					type="hidden" id="bgLeftData" name="bgLeftData"> <input
					type="hidden" id="bgRightData" name="bgRightData">

				<form id="editForm">
					<input type="hidden" name="appCode" value="${appCode}">
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">专题类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select id="type" name="type" class="form-control"
											onchange="$.AlbumController.changeType(this.value);">
											<c:forEach var="item" items="${typeEnum}">
												<option value="${item.key}"
													<c:if test="${item.key eq album.type}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">专题代码(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<c:choose>
											<c:when test="${empty album.id}">
												<input type="text" id="code" name="code"
													value="A_"
													onkeyup="this.value=this.value.toUpperCase()"
													class="form-control validate[required,custom[onlyLetterNumberUnderline],ajax[ajaxCodeCheck],maxSize[16]]"
													placeholder="请输入专题代码">
											</c:when>
											<c:otherwise>
												<input type="text" id="code" name="code"
													value="${album.code}"
													onkeyup="this.value=this.value.toUpperCase()"
													class="form-control"
													placeholder="请输入专题代码" readonly="readonly"
													onclick="$.AlbumController.code();">
											</c:otherwise>
										</c:choose>
										<p class="help-block">专题代码以A_开头.</p>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">专题名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="name" value="${album.name}"
											class="form-control validate[required]" placeholder="请输入专题名称">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">显示名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="title" value="${album.title}"
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
										<input type="text" name="tag" value="${album.tag}"
											class="form-control" placeholder="请输入TAG">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">关键字: </label>

									<div class="col-md-9">
										<input type="text" name="keyword" value="${album.keyword}"
											class="form-control" placeholder="请输入关键字">
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
													<c:if test="${! empty album.status && item.key eq album.status}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">使用模板(<span
										class="required">*</span>):
									</label>
									
									<div class="col-md-9">
										<div class="input-group">
											<input type="hidden" id="templateId" name="templateId" value="${template.id}">
											<input type="hidden" id="templateCode" name="templateCode" value="${template.code}">
											<input type="text" class="form-control validate[required]"
												id="templateName" name="templateName"
												value="${template.name}" placeholder="点击我选择模板"
												readonly="readonly"
												onclick="$.TemplateController.toSelectItem(1,3);" /> <span
												class="input-group-btn">
												<button onclick="$.TemplateController.toSelectItem(1,3);"
													class="btn btn-success" type="button">
													<i class="fa fa-arrow-left fa-fw" /></i> 选择模板
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
									<label class="control-label col-md-3">可配置的元素类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<div class="checkbox-list">
											<c:set var="configItemTypeAllSelected" value="0" />
											<c:if test="${empty album.configItemTypes}">
												<c:set var="configItemTypeAllSelected" value="1" />
											</c:if>
											<label><input name="configItemTypes"
													<c:if test="${configItemTypeAllSelected eq 1 }"> checked </c:if>
													class="validate[required]" type="checkbox" value="" id="configItemTypeAll" onClick="$.AlbumController.clickConfigItemTypes()">全部</label>
											<c:forEach var="item" items="${itemTypeEnum}">
												<c:set var="configItemTypeSelected" value="" />
												<c:forEach var="configItem" items="${album.configItemTypes}">
													<c:if test="${item.key eq configItem }">
														<c:set var="configItemTypeSelected" value="1" />
													</c:if>
												</c:forEach>
												<c:if test="${empty album.configItemTypes}">
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
										<textarea name="description" class="form-control">${album.description}</textarea>
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
										<select name="configImage1" class="form-control" onchange="$.AlbumController.changeConfigImage1(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty album.configImage1 && item.key eq album.configImage1}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6" id="configImage1_content_div" <c:if test="${album.configImage1 != 1}"> style="display: none" </c:if>>
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
											value="${album.configImage1Width}">
										<span class="">高:</span>
										<input type="text"
											class="form-control input-small-ext input-inline validate[required,custom[integer]]"
											id="configImage1Height"
											name="configImage1Height"
											value="${album.configImage1Height}"> 
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
										<select name="configImage2" class="form-control" onchange="$.AlbumController.changeConfigImage2(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty album.configImage2 && item.key eq album.configImage2}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6" id="configImage2_content_div" <c:if test="${album.configImage2 != 1}"> style="display: none" </c:if>>
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
											value="${album.configImage2Width}">
										<span class="">高:</span>
										<input type="text"
											class="form-control input-small-ext input-inline validate[required,custom[integer]]"
											id="configImage2Height"
											name="configImage2Height"
											value="${album.configImage2Height}"> 
									</div>
								</div>
							</div>
						</div>
						
						<div id="albumType_3"
							<c:if test="${album.type != 3}"> style="display: none" </c:if>>
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">推荐位代码(Trailer)(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<div class="input-group">
												<input type="text" class="form-control validate[required]"
													name="widgetCode" id="widgetCode"
													value="${album.widgetCode}" placeholder="点击我选择推荐位"
													readonly="readonly"
													onclick="$.WidgetController.toSelectItem();" /> <span
													class="input-group-btn">
													<button onclick="$.WidgetController.toSelectItem();"
														class="btn btn-success" type="button">
														<i class="fa fa-arrow-left fa-fw" /></i> 选择推荐位
													</button>
												</span>
											</div>
											<p class="help-block">推荐位中的内容只支持点播节目或直播节目.</p>
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">定时检测时长(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="checkDuration"
												value="${album.checkDuration}"
												class="form-control validate[required,custom[integer]]"
												placeholder="请输入定时检测时长">
											<p class="help-block">单位秒，0表时不检测（仅点播的情况下设为0）.</p>
										</div>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">推荐位left坐标(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="widgetLeft"
												value="${album.widgetLeft}"
												class="form-control validate[required,custom[integer]]"
												placeholder="请输入推荐位left坐标">
											<p class="help-block">推荐位left坐标.</p>
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">推荐位top坐标(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="widgetTop"
												value="${album.widgetTop}"
												class="form-control validate[required,custom[integer]]"
												placeholder="请输入推荐位top坐标">
											<p class="help-block">推荐位top坐标.</p>
										</div>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">推荐位width坐标(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="widgetWidth"
												value="${album.widgetWidth}"
												class="form-control validate[required,custom[integer]]"
												placeholder="请输入推荐位width坐标">
											<p class="help-block">推荐位width坐标.</p>
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">推荐位height坐标(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="widgetHeight"
												value="${album.widgetHeight}"
												class="form-control validate[required,custom[integer]]"
												placeholder="请输入推荐位height坐标">
											<p class="help-block">推荐位height坐标.</p>
										</div>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">是否显示节目信息条(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<select name="showProgramInfo" class="form-control">
												<c:forEach var="item" items="${yesNoEnum}">
													<option value="${item.key}"
														<c:if test="${! empty album.showProgramInfo && item.key eq album.showProgramInfo}"> selected="selected" </c:if>>${item.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">节目信息条高度(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="programInfoBarHeight"
												value="${album.programInfoBarHeight}"
												class="form-control validate[required,custom[integer]]"
												placeholder="请输入节目信息条高度">
											<p class="help-block">节目信息条高度.</p>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label class="control-label col-md-6">横海报: </label>

									<div class="col-md-6">
										<input type="hidden" id="image1" name="image1"
											value="${album.image1}">

										<c:if test="${empty album.image1}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty album.image1}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(album.image1)}" />
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
											<span class="label label-info">大小0X0</span>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="control-label col-md-6">竖海报: </label>

									<div class="col-md-6">
										<input type="hidden" id="image2" name="image2"
											value="${album.image2}">
										<c:if test="${empty album.image2}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty album.image2}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(album.image2)}" />
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
											<span class="label label-info">大小0X0</span>
										</div>
									</div>
								</div>
							</div>
						
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">背景是否是整张图(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="backgroundWhole" class="form-control" onchange="$.AlbumController.changeBackgroundWhole(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty album.backgroundWhole && item.key eq album.backgroundWhole}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>

						<div id="backgroundImage_full_div"
							<c:if test="${album.backgroundWhole != 1}"> style="display: none" </c:if>>
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">背景图片(整张图): </label>

										<div class="col-md-9">
											<input type="hidden" id="backgroundImage"
												name="backgroundImage" value="${album.backgroundImage}">

											<c:if test="${empty album.backgroundImage}">
												<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
												<c:set var="fileinputMethod" value="fileinput-new" />
											</c:if>
											<c:if test="${! empty album.backgroundImage}">
												<c:set var="imageUrl"
													value="${fns:getImagePath(album.backgroundImage)}" />
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
							</div>
						</div>

						<div id="backgroundImage_not_full_div"
							<c:if test="${album.backgroundWhole == 1}"> style="display: none" </c:if>>
							<div class="row">
								<div class="col-md-3">
									<div class="form-group">
										<label class="control-label col-md-6">背景图片(上): </label>

										<div class="col-md-6">
											<input type="hidden" id="bgUp" name="bgUp"
												value="${album.bgUp}">

											<c:if test="${empty album.bgUp}">
												<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
												<c:set var="fileinputMethod" value="fileinput-new" />
											</c:if>
											<c:if test="${! empty album.bgUp}">
												<c:set var="imageUrl"
													value="${fns:getImagePath(album.bgUp)}" />
												<c:set var="fileinputMethod" value="fileinput-exists" />
											</c:if>
											<div class="fileinput ${fileinputMethod} fileinput_bgUp"
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
										<label class="control-label col-md-6">背景图片(下): </label>

										<div class="col-md-6">
											<input type="hidden" id="bgDown" name="bgDown"
												value="${album.bgDown}">
											<c:if test="${empty album.bgDown}">
												<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
												<c:set var="fileinputMethod" value="fileinput-new" />
											</c:if>
											<c:if test="${! empty album.bgDown}">
												<c:set var="imageUrl"
													value="${fns:getImagePath(album.bgDown)}" />
												<c:set var="fileinputMethod" value="fileinput-exists" />
											</c:if>
											<div class="fileinput ${fileinputMethod} fileinput_bgDown"
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
										<label class="control-label col-md-6">背景图片(左): </label>

										<div class="col-md-6">
											<input type="hidden" id="bgLeft" name="bgLeft"
												value="${album.bgLeft}">

											<c:if test="${empty album.bgLeft}">
												<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
												<c:set var="fileinputMethod" value="fileinput-new" />
											</c:if>
											<c:if test="${! empty album.bgLeft}">
												<c:set var="imageUrl"
													value="${fns:getImagePath(album.bgLeft)}" />
												<c:set var="fileinputMethod" value="fileinput-exists" />
											</c:if>
											<div class="fileinput ${fileinputMethod} fileinput_bgLeft"
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
										<label class="control-label col-md-6">背景图片(右): </label>

										<div class="col-md-6">
											<input type="hidden" id="bgRight" name="bgRight"
												value="${album.bgRight}">
											<c:if test="${empty album.bgRight}">
												<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
												<c:set var="fileinputMethod" value="fileinput-new" />
											</c:if>
											<c:if test="${! empty album.bgRight}">
												<c:set var="imageUrl"
													value="${fns:getImagePath(album.bgRight)}" />
												<c:set var="fileinputMethod" value="fileinput-exists" />
											</c:if>
											<div class="fileinput ${fileinputMethod} fileinput_bgRight"
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
							</div>
						</div>
					</div>
				</form>
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