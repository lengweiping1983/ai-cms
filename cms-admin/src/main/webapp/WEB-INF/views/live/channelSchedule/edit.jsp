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
						<c:when test="${empty schedule.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					[${channel.name}]${methodDesc}节目
				</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="id" value="${schedule.id}" /><input
						type="hidden" name="channelId" id="channelId"
						value="${channel.id}" />
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">中央频道名称: </label>

									<div class="col-md-9">
										<input type="text" value="${channel.name}" readonly="readonly"
											class="form-control">
									</div>
								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">中央频道代码: </label>

									<div class="col-md-9">
										<input type="text" value="${channel.code}" readonly="readonly"
											class="form-control">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">是否已拆条: </label>

									<div class="col-md-9">
										<select id="splitProgram" name="splitProgram"
											class="form-control"
											onchange="$.ScheduleController.changeSplitProgram(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty schedule.splitProgram && item.key eq schedule.splitProgram}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>

							<div id="schedule_div_1"
								<c:if test="${schedule.splitProgram != 1}"> style="display: none" </c:if>>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">关联节目(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<div class="input-group">
												<input type="hidden" name="mediaId" id="mediaId"
													value="${program.mediaId}" /> <input type="hidden"
													name="mediaEpisode" id="mediaEpisode"
													value="${program.episodeIndex}" /><input type="hidden"
													name="programId" id="programId" value="${program.id}" /> <input
													type="hidden" id="programCode" value="${program.code}" />
												<input type="text" class="form-control validate[required]"
													id="programName" value="${program.title}"
													placeholder="点击我选择关联节目" readonly="readonly"
													onclick="$.ProgramController.toSelectProgram();" /> <span
													class="input-group-btn">
													<button onclick="$.ProgramController.toSelectProgram();"
														class="btn btn-success" type="button">
														<i class="fa fa-arrow-left fa-fw" /></i> 选择关联节目
													</button>
												</span>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">节目名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" id="liveProgramName" name="programName"
											value="${schedule.programName}"
											class="form-control validate[required]" placeholder="请输入节目名称"
											onChange="$.ScheduleController.changeProgramName(this);">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">搜索名称: </label>

									<div class="col-md-9">
										<input type="text" id="searchName" name="searchName"
											value="${schedule.searchName}" class="form-control"
											placeholder="请输入搜索名称">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">类别: </label>

									<div class="col-md-9">
										<input type="text" name="genres" value="${schedule.genres}"
											class="form-control" placeholder="请输入类别">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">看点: </label>

									<div class="col-md-9">
										<input type="text" name="viewpoint"
											value="${schedule.viewpoint}" class="form-control"
											placeholder="请输入看点">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">更新信息: </label>

									<div class="col-md-9">
										<input type="text" name="updateInfo"
											value="${schedule.updateInfo}" class="form-control"
											placeholder="请输入更新信息">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">更新到几集: </label>

									<div class="col-md-9">
										<input type="text" name="episodeIndex"
											value="${schedule.episodeIndex}"
											class="form-control validate[custom[integer]]"
											placeholder="请输入更新到几集" />
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">TAG: </label>

									<div class="col-md-9">
										<input type="text" name="tag" value="${schedule.tag}"
											class="form-control" placeholder="请输入TAG">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">关键字: </label>

									<div class="col-md-9">
										<input type="text" name="keyword" value="${schedule.keyword}"
											class="form-control" placeholder="请输入关键字">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">开始时间(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<div class="input-group date form_datetime">
											<input type="text" id="beginTime" name="beginTime"
												value='<fmt:formatDate value="${schedule.beginTime}"
                                                                                       pattern="yyyy-MM-dd HH:mm"/>'
												size="16" onchange="$.ScheduleController.changeBeginDate();"
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
									<label class="control-label col-md-3">结束时间(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<div class="input-group date form_datetime">
											<input type="text" id="endTime" name="endTime"
												value='<fmt:formatDate value="${schedule.endTime}"
                                                                                       pattern="yyyy-MM-dd HH:mm"/>'
												size="16" onchange="$.ScheduleController.changeEndDate();"
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
									<label class="control-label col-md-3">时长(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" id="liveDuration" name="duration"
											value="${schedule.duration}"  onchange="$.ScheduleController.changeDuration();"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入时长">
										<p class="help-block">单位分钟.</p>
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
													<c:if test="${! empty schedule.status && item.key eq schedule.status}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">简介:</label>

									<div class="col-md-9">
										<textarea name="info" class="form-control">${schedule.info}</textarea>
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
											<tags:injectionStatus value='${schedule.injectionStatus}' />
										</p>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">运营商侧代码: </label>

									<div class="col-md-9">
										<input type="text" name="partnerItemCode" value="${schedule.partnerItemCode}"
											class="form-control" placeholder="运营商侧代码">
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty schedule.id}">
						<button class="btn btn-outline green"
							onclick="$.ChannelScheduleController.edit('${ctx}/live/${channel.id}/schedule/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.ChannelScheduleController.edit('${ctx}/live/${channel.id}/schedule/${schedule.id}/edit');">
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