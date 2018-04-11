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
						<c:when test="${empty leagueSeasonStar.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					[${leagueSeason.name}]${methodDesc}关联球星信息
				</h4>
			</div>
			<div class="modal-body">
				<input type="hidden" id="image1Data" name="image1Data"> <input
					type="hidden" id="image2Data" name="image2Data">
				<form id="editForm">
					<input type="hidden" name="id" value="${leagueSeasonStar.id}" /> <input
						type="hidden" name="leagueSeasonId" value="${leagueSeason.id}" />
					<input type="hidden" name="itemId" id="itemId"
						value="${leagueSeasonStar.itemId}" /> <input type="hidden"
						id="itemType" name="itemType" value="11" /> <input type="hidden"
						id="itemTitle" value="${leagueSeasonStar.itemTitle}"> <input
						type="hidden" id="status" name="status" value="1">

					<div class="content form-horizontal">
						<c:choose>
							<c:when test="${empty leagueSeasonStar.id}">
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">球星名称: </label>

											<div class="col-md-9">
												<div class="input-group">
													<input type="text" class="form-control validate[required]"
														name="itemName" id="itemName" placeholder="点击我选择球星"
														readonly="readonly"
														onclick="$.LeagueSeasonStarController.toSelectItem();" />
													<span class="input-group-btn">
														<button
															onclick="$.LeagueSeasonStarController.toSelectItem();"
															class="btn btn-success" type="button">
															<i class="fa fa-arrow-left fa-fw" /></i> 选择球星
														</button>
													</span>
												</div>
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">球星状态: </label>

											<div class="col-md-9">
												<select id="itemStatus" name="itemStatus"
													disabled="disabled" class="form-control">
													<c:forEach var="item" items="${statusEnum}">
														<option value="${item.key}"
															<c:if test="${! empty leagueSeasonStar.itemStatus && item.key eq leagueSeasonStar.itemStatus}"> selected="selected" </c:if>>${item.value}</option>
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
											<label class="control-label col-md-3">球星名称: </label>

											<div class="col-md-9">
												<input type="text" value="${leagueSeasonStar.itemName}"
													readonly="readonly" class="form-control">
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">球星状态: </label>

											<div class="col-md-9">
												<select id="itemStatus" name="itemStatus"
													disabled="disabled" class="form-control">
													<c:forEach var="item" items="${statusEnum}">
														<option value="${item.key}"
															<c:if test="${! empty leagueSeasonStar.itemStatus && item.key eq leagueSeasonStar.itemStatus}"> selected="selected" </c:if>>${item.value}</option>
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
									<label class="control-label col-md-3">场次(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="siteNum"
											value="${leagueSeasonStar.siteNum}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入场次" />
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">助攻(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="assistNum"
											value="${leagueSeasonStar.assistNum}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入助攻" />
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">进球(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="enterNum"
											value="${leagueSeasonStar.enterNum}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入进球" />
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">点球(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="pointNum"
											value="${leagueSeasonStar.pointNum}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入点球" />
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty leagueSeasonStar.id}">
						<button class="btn btn-outline green"
							onclick="$.LeagueSeasonStarController.edit('${ctx}/league/${leagueSeason.id}/leagueSeasonStar/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.LeagueSeasonStarController.edit('${ctx}/league/${leagueSeason.id}/leagueSeasonStar/${leagueSeasonStar.id}/edit');">
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