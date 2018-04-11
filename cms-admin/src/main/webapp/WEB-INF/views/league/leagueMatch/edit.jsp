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
						<c:when test="${empty leagueMatch.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					${methodDesc}赛事
				</h4>
			</div>
			<div class="modal-body">
				<input type="hidden" id="image1Data" name="image1Data"> <input
					type="hidden" id="image2Data" name="image2Data">
				<form id="editForm">
					<input type="hidden" name="id" value="${leagueMatch.id}" />
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">赛事类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="type" class="form-control">
											<c:forEach var="item" items="${typeEnum}">
												<option value="${item.key}"
													<c:if test="${! empty leagueMatch.type && item.key eq leagueMatch.type}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">内容类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="sportContentType" class="form-control">
											<c:forEach var="item" items="${sportContentTypeEnum}">
												<option value="${item.key}"
													<c:if test="${! empty leagueMatch.sportContentType && item.key eq leagueMatch.sportContentType}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">所属赛季: </label>

									<div class="col-md-9">
										<div class="input-group">
											<input type="hidden" name="leagueSeasonId"
												id="leagueSeasonId" value="${leagueSeason.id}" /> <input
												type="text" class="form-control" name="leagueSeasonName"
												id="leagueSeasonName" value="${leagueSeason.name}"
												placeholder="点击我选择赛季" readonly="readonly"
												onclick="$.LeagueSeasonController.toSelectLeagueSeason();" />
											<span class="input-group-btn">
												<button
													onclick="$.LeagueSeasonController.toSelectLeagueSeason();"
													class="btn btn-success" type="button">
													<i class="fa fa-arrow-left fa-fw" /></i> 选择赛季
												</button>
											</span>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">管理名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="name" value="${leagueMatch.name}"
											class="form-control validate[required]" placeholder="请输入管理名称">
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
										<input type="text" name="title" value="${leagueMatch.title}"
											class="form-control" placeholder="请输入显示名称"
											onChange="$.LeagueMatchController.genSearchName(this);">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">搜索名称: </label>

									<div class="col-md-9">
										<input type="text" id="searchName" name="searchName"
											value="${leagueMatch.searchName}" class="form-control"
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
										<input type="text" name="area" value="${leagueMatch.area}"
											class="form-control" placeholder="请输入地点">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">看点: </label>

									<div class="col-md-9">
										<input type="text" name="viewpoint"
											value="${leagueMatch.viewpoint}" class="form-control"
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
										<input type="text" name="tag" value="${leagueMatch.tag}"
											class="form-control" placeholder="请输入TAG">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">关键字: </label>

									<div class="col-md-9">
										<input type="text" name="keyword"
											value="${leagueMatch.keyword}" class="form-control"
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
												value='<fmt:formatDate value="${leagueMatch.beginTime}"
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
											value="${leagueMatch.duration}"
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
									<label class="control-label col-md-3">赛程(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="leagueIndex"
											value="${leagueMatch.leagueIndex}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入赛程">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">场次(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="episodeIndex"
											value="${leagueMatch.episodeIndex}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入场次" />
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">主场(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<div class="input-group">
											<input type="hidden" name="homeId" id="homeId"
												value="${leagueMatch.homeId}" /> <input type="hidden"
												name="homeType" id="homeType"
												value="${leagueMatch.homeType}" /> <input type="text"
												class="form-control validate[required]" name="homeName"
												id="homeName" value="${leagueMatch.homeName}"
												onChange="$.LeagueMatchController.changeHomeName(this);" />
											<span class="input-group-btn">
												<button onclick="$.ClubController.toSelectClub(2);"
													class="btn btn-success" type="button">选择团队</button>
												<button onclick="$.StarController.toSelectStar(2);"
													class="btn btn-info" type="button">选择个人</button>
											</span>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">客场(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<div class="input-group">
											<input type="hidden" name="guestId" id="guestId"
												value="${leagueMatch.guestId}" /> <input type="hidden"
												name="guestType" id="guestType"
												value="${leagueMatch.guestType}" /><input type="text"
												class="form-control validate[required]" name="guestName"
												id="guestName" value="${leagueMatch.guestName}"
												onChange="$.LeagueMatchController.changeGuestName(this);" />
											<span class="input-group-btn">
												<button onclick="$.ClubController.toSelectClub(3);"
													class="btn btn-success" type="button">选择团队</button>
												<button onclick="$.StarController.toSelectStar(3);"
													class="btn btn-info" type="button">选择个人</button>
											</span>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">主场得分: </label>

									<div class="col-md-9">
										<input type="text" name="homeScore"
											value="${leagueMatch.homeScore}"
											class="form-control validate[custom[integer]]"
											placeholder="请输入主场得分">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">客场得分: </label>

									<div class="col-md-9">
										<input type="text" name="guestScore"
											value="${leagueMatch.guestScore}"
											class="form-control validate[custom[integer]]"
											placeholder="请输入客场得分">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">是否有点球大战: </label>

									<div class="col-md-9">
										<select id="pointStatus" name="pointStatus"
											class="form-control"
											onchange="$.LeagueMatchController.changePointStatus(this.value);">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty leagueMatch.pointStatus && item.key eq leagueMatch.pointStatus}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">提供商: </label>

									<div class="col-md-9">
										<select class="form-control select2" id="cpId" name="cpId">
											<option value="">请选择</option>
											<c:forEach var="item" items="${cpList}">
												<option value="${item.id}"
													<c:if test="${leagueMatch.cpId == item.id}">selected="selected"</c:if>>${item.name}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>

						<div id="leagueMatch_div_2"
							<c:if test="${leagueMatch.pointStatus != 1}"> style="display: none" </c:if>>
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">主场点球数(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="homePointNum"
												value="${leagueMatch.homePointNum}"
												class="form-control validate[required,custom[integer]]"
												placeholder="请输入主场点球数">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">客场点球数(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="guestPointNum"
												value="${leagueMatch.guestPointNum}"
												class="form-control validate[required,custom[integer]]"
												placeholder="请输入客场点球数">
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
											value="${leagueMatch.image1}">

										<c:if test="${empty leagueMatch.image1}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty leagueMatch.image1}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(leagueMatch.image1)}" />
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
											value="${leagueMatch.image2}">
										<c:if test="${empty leagueMatch.image2}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty leagueMatch.image2}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(leagueMatch.image2)}" />
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
										<textarea name="info" rows="8" class="form-control">${leagueMatch.info}</textarea>
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
												<button onclick="$.LeagueMatchController.clearChannel();"
													class="btn btn-info" type="button">清空</button>
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
												<button
													onclick="$.LeagueMatchController.clearChannelProgram();"
													class="btn btn-info" type="button">清空</button>
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
													<c:if test="${! empty leagueMatch.splitProgram && item.key eq leagueMatch.splitProgram}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>

							<div id="leagueMatch_div_1"
								<c:if test="${leagueMatch.splitProgram != 1}"> style="display: none" </c:if>>
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
													<button
														onclick="$.LeagueMatchController.clearSplitProgram();"
														class="btn btn-info" type="button">清空</button>
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
				<c:choose>
					<c:when test="${empty leagueMatch.id}">
						<button class="btn btn-outline green"
							onclick="$.LeagueMatchController.edit('${ctx}/league/leagueMatch/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.LeagueMatchController.edit('${ctx}/league/leagueMatch/${leagueMatch.id}/edit');">
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