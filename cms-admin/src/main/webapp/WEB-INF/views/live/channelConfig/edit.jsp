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
						<c:when test="${empty channelConfig.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					${methodDesc}频道配置
				</h4>
			</div>
			<div class="modal-body">
				<input type="hidden" id="image1Data" name="image1Data"> <input
					type="hidden" id="image2Data" name="image2Data">
				<form id="editForm">
					<input type="hidden" name="id" value="${channelConfig.id}" />
					<div class="content form-horizontal">

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">本地提供商: </label>

									<div class="col-md-9">
										<select id="type" name="providerType" class="form-control">
											<c:forEach var="item" items="${providerTypeEnum}">
												<option value="${item.key}"
													<c:if test="${item.key eq channelConfig.providerType}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">中央频道名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<div class="input-group">
											<input type="hidden" id="channelType" value="${channel.type}"
												onchange="$.ChannelConfigController.changeChannelType(this.value);" />
											<input type="hidden" name="channelId" id="channelId"
												value="${channel.id}" /> <input type="text"
												class="form-control validate[required]" name="channelName"
												id="channelName" value="${channel.name}"
												placeholder="点击我选择中央频道" readonly="readonly"
												onclick="$.ChannelController.toSelectChannel();" /> <span
												class="input-group-btn">
												<button onclick="$.ChannelController.toSelectChannel();"
													class="btn btn-success" type="button">
													<i class="fa fa-arrow-left fa-fw" /></i> 选择中央频道
												</button>
											</span>
										</div>
									</div>
								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">中央频道代码: </label>

									<div class="col-md-9">
										<input type="text" id=channelCode value="${channel.code}"
											readonly="readonly" class="form-control">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">本地频道代码(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="partnerChannelCode"
											value="${channelConfig.partnerChannelCode}"
											class="form-control validate[required]"
											placeholder="请输入本地频道代码">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">本地频道名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="partnerChannelName"
											value="${channelConfig.partnerChannelName}"
											class="form-control validate[required]"
											placeholder="请输入本地频道名称">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">本地频道号(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="partnerChannelNumber"
											value="${channelConfig.partnerChannelNumber}"
											class="form-control validate[required]"
											placeholder="请输入本地频道号">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">使用频道号来播放(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="playByChannelNumber" class="form-control">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty channelConfig.playByChannelNumber && item.key eq channelConfig.playByChannelNumber}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">直播地址(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="url" value="${channelConfig.url}"
											class="form-control validate[required]" placeholder="请输入直播地址">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">直播是否加密: </label>

									<div class="col-md-9">
										<select name="liveEncrypt" class="form-control">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty channelConfig.liveEncrypt && item.key eq channelConfig.liveEncrypt}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">是否支持时移: </label>

									<div class="col-md-9">
										<select name="timeShift" class="form-control"
											onchange="$.ChannelConfigController.changeTimeShift(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty channelConfig.timeShift && item.key eq channelConfig.timeShift}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div id="channelConfig_div_3"
								<c:if test="${channelConfig.timeShift != 1}"> style="display: none" </c:if>>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">时移时长(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="timeShiftDuration"
												value="${channelConfig.timeShiftDuration}"
												class="form-control validate[required,custom[integer]]"
												placeholder="请输入时移时长" /> <span class="help-block">单位分钟</span>
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">时移地址(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="timeShiftUrl"
												value="${channelConfig.timeShiftUrl}"
												class="form-control validate[required]"
												placeholder="请输入时移地址">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">时移是否加密: </label>

										<div class="col-md-9">
											<select name="timeShiftEncrypt" class="form-control">
												<c:forEach var="item" items="${yesNoEnum}">
													<option value="${item.key}"
														<c:if test="${! empty channelConfig.timeShiftEncrypt && item.key eq channelConfig.timeShiftEncrypt}"> selected="selected" </c:if>>${item.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">是否支持回看: </label>

									<div class="col-md-9">
										<select name="lookBack" class="form-control"
											onchange="$.ChannelConfigController.changeLookBack(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty channelConfig.lookBack && item.key eq channelConfig.lookBack}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div id="channelConfig_div_2"
								<c:if test="${channelConfig.lookBack != 1}"> style="display: none" </c:if>>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">回看时长(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="lookBackDuration"
												value="${channelConfig.lookBackDuration}"
												class="form-control validate[required,custom[integer]]"
												placeholder="请输入回看时长" /> <span class="help-block">单位小时</span>
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">回看地址(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="lookBackUrl"
												value="${channelConfig.lookBackUrl}"
												class="form-control validate[required]"
												placeholder="请输入回看地址">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">回看是否加密: </label>

										<div class="col-md-9">
											<select name="lookBackEncrypt" class="form-control">
												<c:forEach var="item" items="${yesNoEnum}">
													<option value="${item.key}"
														<c:if test="${! empty channelConfig.lookBackEncrypt && item.key eq channelConfig.lookBackEncrypt}"> selected="selected" </c:if>>${item.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div id="channelConfig_div_1"
							<c:if test="${channelConfig.lookBack != 1}"> style="display: none" </c:if>>
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">收录时长(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="enbodyDuration"
												value="${channelConfig.enbodyDuration}"
												class="form-control validate[required,custom[integer]]"
												placeholder="请输入收录时长" /> <span class="help-block">单位分钟</span>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">赛前时长(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="preTime"
											value="${channelConfig.preTime}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入赛前时长" /> <span class="help-block">单位分钟</span>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">赛后时长(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="sufTime"
											value="${channelConfig.sufTime}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入赛后时长" /> <span class="help-block">单位分钟</span>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">状态(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="status" class="form-control">
											<c:forEach var="item" items="${statusEnum}">
												<option value="${item.key}"
													<c:if test="${! empty channelConfig.status && item.key eq channelConfig.status}"> selected="selected" </c:if>>${item.value}</option>
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
											value="${channelConfig.sortIndex}"
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
										<input type="hidden" id="image1" name="image1"
											value="${channelConfig.image1}">

										<c:if test="${empty channelConfig.image1}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty channelConfig.image1}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(channelConfig.image1)}" />
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
									<label class="control-label col-md-6">激活图标: </label>

									<div class="col-md-6">
										<input type="hidden" id="image2" name="image2"
											value="${channelConfig.image2}">
										<c:if test="${empty channelConfig.image2}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty channelConfig.image2}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(channelConfig.image2)}" />
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
									<label class="control-label col-md-3">简介:</label>

									<div class="col-md-9">
										<textarea name="info" rows="8" class="form-control">${channelConfig.info}</textarea>
									</div>
								</div>
							</div>
						</div>

					</div>
				</form>
			</div>
			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty channelConfig.id}">
						<button class="btn btn-outline green"
							onclick="$.ChannelConfigController.edit('${ctx}/live/channelConfig/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.ChannelConfigController.edit('${ctx}/live/channelConfig/${channelConfig.id}/edit');">
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