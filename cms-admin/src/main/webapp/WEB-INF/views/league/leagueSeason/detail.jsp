<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">
					查看赛季
				</h4>
			</div>
			<div class="modal-body">
				<input type="hidden" id="image1Data" name="image1Data"> <input
					type="hidden" id="image2Data" name="image2Data">
				<form id="editForm">
					<input type="hidden" name="id" value="${leagueSeason.id}" />
					<div class="content form-horizontal">

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">联赛名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<div class="input-group">
											<input type="hidden" name="leagueId" id="leagueId"
												value="${league.id}" /> <input type="text"
												class="form-control validate[required]" name="leagueName"
												id="leagueName" value="${league.name}" placeholder="点击我选择联赛"
												readonly="readonly"
												onclick="$.LeagueController.toSelectLeague();" /> <span
												class="input-group-btn">
												<button onclick="$.LeagueController.toSelectLeague();"
													class="btn btn-success" type="button">
													<i class="fa fa-arrow-left fa-fw" /></i> 选择联赛
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
									<label class="control-label col-md-3">管理名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="name" value="${leagueSeason.name}"
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
										<input type="text" name="title"
											value="${leagueSeason.title}" class="form-control"
											placeholder="请输入显示名称" onChange="$.LeagueMatchController.genSearchName(this);">
									</div>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">搜索名称: </label>

									<div class="col-md-9">
										<input type="text" id="searchName" name="searchName"
											value="${leagueSeason.searchName}" class="form-control"
											placeholder="请输入搜索名称">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">地点: </label>

									<div class="col-md-9">
										<input type="text" name="area" value="${leagueSeason.area}"
											class="form-control" placeholder="请输入地点">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">看点: </label>

									<div class="col-md-9">
										<input type="text" name="viewpoint"
											value="${leagueSeason.viewpoint}" class="form-control"
											placeholder="请输入看点">
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">TAG: </label>

									<div class="col-md-9">
										<input type="text" name="tag" value="${leagueSeason.tag}"
											class="form-control" placeholder="请输入TAG">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">关键字: </label>

									<div class="col-md-9">
										<input type="text" name="keyword"
											value="${leagueSeason.keyword}" class="form-control"
											placeholder="请输入关键字">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">开赛时间(<span
										class="required">*</span>):
									</label>
									<div class="col-md-9">
										<div class="input-group date form_datetime" id="beginTime_div">
											<input type="text" id="beginTime" name="beginTime"
												value='<fmt:formatDate value="${leagueSeason.beginTime}"
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
									<label class="control-label col-md-3">时长(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" id="duration" name="duration"
											value="${leagueSeason.duration}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入时长">
										<p class="help-block">单位分钟.</p>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">提供商: </label>

									<div class="col-md-9">
										<select class="form-control select2" id="cpId" name="cpId">
											<option value="">请选择</option>
											<c:forEach var="item" items="${cpList}">
												<option value="${item.id}"
													<c:if test="${leagueSeason.cpId == item.id}">selected="selected"</c:if>>${item.name}</option>
											</c:forEach>
										</select>
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
													<c:if test="${! empty leagueSeason.status && item.key eq leagueSeason.status}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
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
											value="${leagueSeason.image1}">

										<c:if test="${empty leagueSeason.image1}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty leagueSeason.image1}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(leagueSeason.image1)}" />
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
											value="${leagueSeason.image2}">
										<c:if test="${empty leagueSeason.image2}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty leagueSeason.image2}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(leagueSeason.image2)}" />
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
										<textarea name="info" rows="8" class="form-control">${leagueSeason.info}</textarea>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">中央频道名称: </label>

									<div class="col-md-9">
										<div class="input-group">
											<input type="hidden" id="channelType" value="${channel.type}" />
											<input type="hidden" id="channelCode" value="${channel.code}" />
											<input type="hidden" name="channelId" id="channelId"
												value="${channel.id}"
												onChange="$.LeagueMatchController.changeChannelId(this);" />
											<input type="text" class="form-control" name="channelName"
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
									<label class="control-label col-md-3">直播节目名称: </label>

									<div class="col-md-9">
										<div class="input-group">
											<input type="hidden" name="scheduleId" id="scheduleId"
												value="${schedule.id}" /> <input type="text"
												class="form-control" name="scheduleProgramName"
												id="scheduleProgramName" value="${schedule.programName}"
												placeholder="点击我选择直播节目" readonly="readonly"
												onclick="$.ScheduleController.toSelectSchedule();" /> <span
												class="input-group-btn">
												<button onclick="$.ScheduleController.toSelectSchedule();"
													class="btn btn-success" type="button">
													<i class="fa fa-arrow-left fa-fw" /></i> 选择直播节目
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
									<label class="control-label col-md-3">是否已拆条: </label>

									<div class="col-md-9">
										<select id="splitProgram" name="splitProgram"
											class="form-control"
											onchange="$.LeagueMatchController.changeSplitProgram(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty leagueSeason.splitProgram && item.key eq leagueSeason.splitProgram}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>

							<div id="leagueMatch_div_1"
								<c:if test="${leagueSeason.splitProgram != 1}"> style="display: none" </c:if>>
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