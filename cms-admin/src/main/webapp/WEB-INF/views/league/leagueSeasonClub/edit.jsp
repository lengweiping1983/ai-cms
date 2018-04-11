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
						<c:when test="${empty leagueSeasonClub.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					[${leagueSeason.name}]${methodDesc}关联俱乐部信息
				</h4>
			</div>
			<div class="modal-body">
				<input type="hidden" id="image1Data" name="image1Data"> <input
					type="hidden" id="image2Data" name="image2Data">
				<form id="editForm">
					<input type="hidden" name="id" value="${leagueSeasonClub.id}" /> <input
						type="hidden" name="leagueSeasonId" value="${leagueSeason.id}" />
					<input type="hidden" name="itemId" id="itemId"
						value="${leagueSeasonClub.itemId}" /> <input type="hidden"
						id="itemType" name="itemType" value="10" /> <input type="hidden"
						id="itemTitle" value="${leagueSeasonClub.itemTitle}"> <input
						type="hidden" id="status" name="status" value="1">

					<div class="content form-horizontal">
						<c:choose>
							<c:when test="${empty leagueSeasonClub.id}">
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">俱乐部名称: </label>

											<div class="col-md-9">
												<div class="input-group">
													<input type="text" class="form-control validate[required]"
														name="itemName" id="itemName" placeholder="点击我选择俱乐部"
														readonly="readonly"
														onclick="$.LeagueSeasonClubController.toSelectItem();" />
													<span class="input-group-btn">
														<button
															onclick="$.LeagueSeasonClubController.toSelectItem();"
															class="btn btn-success" type="button">
															<i class="fa fa-arrow-left fa-fw" /></i> 选择俱乐部
														</button>
													</span>
												</div>
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">俱乐部状态: </label>

											<div class="col-md-9">
												<select id="itemStatus" name="itemStatus"
													disabled="disabled" class="form-control">
													<c:forEach var="item" items="${statusEnum}">
														<option value="${item.key}"
															<c:if test="${! empty leagueSeasonClub.itemStatus && item.key eq leagueSeasonClub.itemStatus}"> selected="selected" </c:if>>${item.value}</option>
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
											<label class="control-label col-md-3">俱乐部名称: </label>

											<div class="col-md-9">
												<input type="text" value="${leagueSeasonClub.itemName}"
													readonly="readonly" class="form-control">
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">俱乐部状态: </label>

											<div class="col-md-9">
												<select id="itemStatus" name="itemStatus"
													disabled="disabled" class="form-control">
													<c:forEach var="item" items="${statusEnum}">
														<option value="${item.key}"
															<c:if test="${! empty leagueSeasonClub.itemStatus && item.key eq leagueSeasonClub.itemStatus}"> selected="selected" </c:if>>${item.value}</option>
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
									<label class="control-label col-md-3">胜数(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="winNum"
											value="${leagueSeasonClub.winNum}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入胜数" />
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">平数(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="flatNum"
											value="${leagueSeasonClub.flatNum}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入平数" />
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">输数(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="loseNum"
											value="${leagueSeasonClub.loseNum}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入输数" />
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">进球数(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="enterNum"
											value="${leagueSeasonClub.enterNum}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入进球数" />
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">点球数(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="pointNum"
											value="${leagueSeasonClub.pointNum}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请点球入点球数" />
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">失球数(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="fumbleNum"
											value="${leagueSeasonClub.fumbleNum}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入失球数" />
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">积分(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="creditNum"
											value="${leagueSeasonClub.creditNum}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入总积分" />
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
											value="${leagueSeasonClub.sortIndex}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入排序值" />
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty leagueSeasonClub.id}">
						<button class="btn btn-outline green"
							onclick="$.LeagueSeasonClubController.edit('${ctx}/league/${leagueSeason.id}/leagueSeasonClub/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.LeagueSeasonClubController.edit('${ctx}/league/${leagueSeason.id}/leagueSeasonClub/${leagueSeasonClub.id}/edit');">
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