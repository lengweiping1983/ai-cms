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
					<c:choose>
						<c:when test="${empty star.id}">
							<c:set var="methodDesc" value="增加" />
							<c:set var="tag" value="演员" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
							<c:set var="tag" value="${star.tag}" />
						</c:otherwise>
					</c:choose>
					${methodDesc}明星
				</h4>
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
										<select id="type" name="type" class="form-control"
											onchange="$.StarController.changeSelectType(this.value);">
											<c:forEach var="item" items="${typeEnum}">
												<option value="${item.key}"
													<c:if test="${item.key eq star.type}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>

						<div id="star_div_2"
							<c:if test="${star.type != 2}"> style="display: none" </c:if>>
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">俱乐部名称(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<div class="input-group">
												<input type="hidden" name="clubId" id="clubId"
													value="${club.id}" /> <input type="text"
													class="form-control validate[required]" name="clubName"
													id="clubName" value="${club.name}" placeholder="点击我选择俱乐部"
													readonly="readonly"
													onclick="$.ClubController.toSelectClub(1);" /> <span
													class="input-group-btn">
													<button onclick="$.ClubController.toSelectClub(1);"
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
										<label class="control-label col-md-3">球星编号(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="no" value="${star.no}"
												class="form-control validate[required]"
												placeholder="请输入球星编号">
										</div>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">球星位置(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="position" value="${star.position}"
												class="form-control validate[required]"
												placeholder="请输入球星位置">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">球星荣誉: </label>

										<div class="col-md-9">
											<input type="text" name="honor" value="${star.honor}"
												class="form-control" placeholder="请输入球星荣誉">
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">明星姓名(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="name" value="${star.name}"
											class="form-control validate[required]" placeholder="请输入明星姓名">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">明星英文名: </label>

									<div class="col-md-9">
										<input type="text" name="ename" value="${star.ename}"
											class="form-control" placeholder="请输入明星英文名">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">生日: </label>
									<div class="col-md-9">
										<div class="input-group">
											<input type="text" class="form-control date date-picker"
												id="birthday" name="birthday"
												value='<fmt:formatDate value="${star.birthday}"
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
									<label class="control-label col-md-3">年龄: </label>

									<div class="col-md-9">
										<input type="text" name="age" value="${star.age}"
											class="form-control validate[custom[integer]]"
											placeholder="请输入年龄" />
										<p class="help-block">如输入了生日，可以不输入年龄.</p>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">身高: </label>

									<div class="col-md-9">
										<input type="text" name="height" value="${star.height}"
											class="form-control validate[custom[integer]]"
											placeholder="请输入身高" /> <span class="help-block">单位(CM)</span>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">体重: </label>

									<div class="col-md-9">
										<input type="text" name="weight" value="${star.weight}"
											class="form-control validate[custom[integer]]"
											placeholder="请输入体重" /> <span class="help-block">单位(KG)</span>
									</div>
								</div>
							</div>
						</div>

						<div id="star_div_3"
							<c:if test="${star.type != 3}"> style="display: none" </c:if>>
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">臂展(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="arm" value="${star.arm}"
												class="form-control validate[required,custom[integer]]"
												placeholder="请输入臂展" /> <span class="help-block">单位(英寸)</span>
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">腿展(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="leg" value="${star.leg}"
												class="form-control validate[required,custom[integer]]"
												placeholder="请输入腿展" /> <span class="help-block">单位(英寸)</span>
										</div>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">战绩(<span
											class="required">*</span>):
										</label>

										<div class="col-md-9">
											<input type="text" name="militaryExploits"
												value="${star.militaryExploits}"
												class="form-control validate[required]" placeholder="请输入战绩">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">KO/TKO: </label>

										<div class="col-md-9">
											<input type="text" name="ko" value="${star.ko}"
												class="form-control" placeholder="请输入KO/TKO">
										</div>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">降服: </label>

										<div class="col-md-9">
											<input type="text" name="surrender" value="${star.surrender}"
												class="form-control" placeholder="请输入降服">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">判定: </label>

										<div class="col-md-9">
											<input type="text" name="determine" value="${star.determine}"
												class="form-control" placeholder="请输入判定">
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">国家: </label>

									<div class="col-md-9">
										<input type="text" name="country" value="${star.country}"
											class="form-control" placeholder="请输入国家">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">地区: </label>

									<div class="col-md-9">
										<input type="text" name="area" value="${star.area}"
											class="form-control" placeholder="请输入地区">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">民族: </label>

									<div class="col-md-9">
										<input type="text" name="nation" value="${star.nation}"
											class="form-control" placeholder="请输入民族">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">星座: </label>

									<div class="col-md-9">
										<input type="text" name="constellation"
											value="${star.constellation}" class="form-control"
											placeholder="请输入星座">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">受欢迎度: </label>

									<div class="col-md-9">
										<input type="text" name="greetRate" value="${star.greetRate}"
											class="form-control validate[custom[number]]"
											placeholder="请输入受欢迎度" />
									</div>
								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">评价: </label>

									<div class="col-md-9">
										<input type="text" name="evaluate" value="${star.evaluate}"
											class="form-control" placeholder="请输入评价">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">TAG: </label>

									<div class="col-md-9">
										<input type="text" name="tag" value="${tag}"
											class="form-control" placeholder="请输入TAG">
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
													<c:if test="${! empty star.status && item.key eq star.status}"> selected="selected" </c:if>>${item.value}</option>
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
											value="${star.image1}">

										<c:if test="${empty star.image1}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty star.image1}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(star.image1)}" />
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
											value="${star.image2}">
										<c:if test="${empty star.image2}">
											<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
											<c:set var="fileinputMethod" value="fileinput-new" />
										</c:if>
										<c:if test="${! empty star.image2}">
											<c:set var="imageUrl"
												value="${fns:getImagePath(star.image2)}" />
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
										<textarea name="info" rows="8" class="form-control">${star.info}</textarea>
									</div>
								</div>
							</div>
						</div>

					</div>
				</form>
			</div>

			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty star.id}">
						<button class="btn btn-outline green"
							onclick="$.StarController.edit('${ctx}/star/star/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.StarController.edit('${ctx}/star/star/${star.id}/edit');">
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