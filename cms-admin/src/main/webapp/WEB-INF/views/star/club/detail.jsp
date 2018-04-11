<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade bs-modal-lg" tabindex="-1" role="basic"
	aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">查看俱乐部</h4>
			</div>
			<div class="modal-body" id="modal-body">
				<input type="hidden" id="image1Data" name="image1Data"> <input
					type="hidden" id="image2Data" name="image2Data">
				<form id="editForm">
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">类型: </label>

									<div class="col-md-9">
										<select id="type" name="type" class="form-control">
											<c:forEach var="item" items="${typeEnum}">
												<option value="${item.key}"
													<c:if test="${item.key eq club.type}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="name" value="${club.name}"
											class="form-control validate[required]" placeholder="请输入名称">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">简称: </label>

									<div class="col-md-9">
										<input type="text" name="shortName" value="${club.shortName}"
											class="form-control" placeholder="请输入简称">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">主帅名称: </label>

									<div class="col-md-9">
										<input type="text" name="coach" value="${club.coach}"
											class="form-control" placeholder="请输入主帅名称">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">地区: </label>

									<div class="col-md-9">
										<input type="text" name="area" value="${club.area}"
											class="form-control" placeholder="请输入地区">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">主场地点: </label>

									<div class="col-md-9">
										<input type="text" name="homeCourt" value="${club.homeCourt}"
											class="form-control" placeholder="请输入主场地点">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">成立时间: </label>
									<div class="col-md-9">
										<div class="input-group">
											<input type="text" class="form-control date date-picker"
												id="foundingTime" name="foundingTime"
												value='<fmt:formatDate value="${club.foundingTime}"
                                                                                       pattern="yyyy-MM-dd"/>'
												readonly> <span class="input-group-btn">
												<button class="btn default" type="button">
													<i class="fa fa-calendar"></i>
												</button>
											</span>
										</div>
									</div>
								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">荣誉: </label>

									<div class="col-md-9">
										<input type="text" name="honor" value="${club.honor}"
											class="form-control" placeholder="请输入荣誉">
									</div>
								</div>
							</div>
						</div>


						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">受欢迎度: </label>

									<div class="col-md-9">
										<input type="text" name="greetRate" value="${club.greetRate}"
											class="form-control validate[custom[number]]"
											placeholder="请输入受欢迎度" />
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
													<c:if test="${! empty club.status && item.key eq club.status}"> selected="selected" </c:if>>${item.value}</option>
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
											value="${club.image1}">

										<c:if test="${empty club.image1}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty club.image1}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(club.image1)}" />
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
											value="${club.image2}">
										<c:if test="${empty club.image2}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty club.image2}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(club.image2)}" />
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
										<textarea name="info" rows="8" class="form-control">${club.info}</textarea>
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