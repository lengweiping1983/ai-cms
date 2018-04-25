<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<c:set var="contextPathPrefix" value="${ctx}/media/series/" />
<c:set var="permissionPrefix" value="media:series:" />
<c:set var="readOnly" value="${readOnly}" />
<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">
					<c:choose>
						<c:when test="${empty series.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="查看" />
							<c:if test="${!readOnly}">
								<c:set var="methodDesc" value="修改" />
							</c:if>
						</c:otherwise>
					</c:choose>
					${methodDesc}剧头
				</h4>
			</div>
			<div class="modal-body">
				<input type="hidden" id="image1Data" name="image1Data" /> <input
					type="hidden" id="image2Data" name="image2Data" /> <input
					type="hidden" id="image3Data" name="image3Data" /> <input
					type="hidden" id="image4Data" name="image4Data" />
				<form id="editForm">
					<input type="hidden" name="id" value="${series.id}" />
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-12">
								<div class="tabbable-line boxless tabbable-reversed">
									<ul class="nav nav-tabs">
										<li class="active"><a href="#tab_0"
											onclick="$.SeriesController.changeTab(1)" data-toggle="tab">剧头元数据</a></li>
										<li><a href="#tab_1"
											onclick="$.SeriesController.changeTab(1)" data-toggle="tab">扩展数据</a></li>
										<li><a href="#tab_2"
											onclick="$.SeriesController.changeTab(1)" data-toggle="tab">版权</a></li>
										<li><a href="#tab_3"
											onclick="$.SeriesController.changeTab(1)" data-toggle="tab">海报</a></li>
										<c:if test="${!empty series.id}">
											<li><a href="#tab_4"
												onclick="$.SeriesController.changeTab(2)" data-toggle="tab">剧照</a></li>
										</c:if>
									</ul>
									<div class="tab-content">
										<div class="tab-pane active" id="tab_0">
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">剧头类型(<span
															class="required">*</span>):
														</label>

														<div class="col-md-9">
															<select id="type" name="type" class="form-control"
																onchange="$.SeriesController.changeSelectType(this.value);">
																<c:forEach var="item" items="${typeEnum}">
																	<option value="${item.key}"
																		<c:if test="${item.key eq series.type}"> selected="selected" </c:if>>${item.value}</option>
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
															<select id="contentType" name="contentType"
																class="form-control"
																onchange="$.SeriesController.changeSelectContentType(this.value);">
																<c:forEach var="item" items="${contentTypeEnum}">
																	<option value="${item.key}"
																		<c:if test="${item.key eq series.contentType}"> selected="selected" </c:if>>${item.value}</option>
																</c:forEach>
															</select>
														</div>
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">剧头名称(<span
															class="required">*</span>):
														</label>

														<div class="col-md-9">
															<input type="text" name="name" value="${series.name}"
																class="form-control validate[required]"
																placeholder="请输入剧头名称">
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">英文名称: </label>

														<div class="col-md-9">
															<input type="text" name="enName" value="${series.enName}"
																class="form-control" placeholder="请输入英文名称">
														</div>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">标题(<span
															class="required">*</span>):
														</label>

														<div class="col-md-9">
															<input type="text" name="title" value="${series.title}"
																class="form-control validate[required]"
																placeholder="请输入标题"
																onChange="$.SeriesController.genSearchName(this);">
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">副标题: </label>

														<div class="col-md-9">
															<input type="text" name="caption"
																value="${series.caption}" class="form-control"
																placeholder="请输入副标题">
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
																value="${series.searchName}" class="form-control"
																placeholder="请输入搜索名称">
															<p class="help-block">拼音首字母.</p>
														</div>
													</div>
												</div>

												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">内部标签: </label>

														<div class="col-md-9">
															<input type="text" name="internalTag"
																value="${series.internalTag}" class="form-control"
																placeholder="请输入内部标签">
															<p class="help-block">内部使用.</p>
														</div>
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">导演: </label>

														<div class="col-md-9">
															<input type="text" name="director"
																value="${series.director}" class="form-control"
																placeholder="请输入导演"
																onChange="$.SeriesController.genDirectorPinyin(this);">
															<p class="help-block">多个使用','分割.</p>
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">演员: </label>

														<div class="col-md-9">
															<input type="text" name="actor" value="${series.actor}"
																class="form-control" placeholder="请输入演员"
																onChange="$.SeriesController.genActorPinyin(this);">
															<p class="help-block">多个使用','分割.</p>
														</div>
													</div>
												</div>
											</div>

											<div class="row" style="display: none">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">导演拼音: </label>

														<div class="col-md-9">
															<input type="text" name="directorPinyin"
																id="directorPinyin" value="${series.directorPinyin}"
																class="form-control" placeholder="请输入导演拼音">
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">演员拼音: </label>

														<div class="col-md-9">
															<input type="text" name="actorPinyin" id="actorPinyin"
																value="${series.actorPinyin}" class="form-control"
																placeholder="请输入演员演员">
														</div>
													</div>
												</div>
											</div>


											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">主持人: </label>

														<div class="col-md-9">
															<input type="text" name="compere"
																value="${series.compere}" class="form-control"
																placeholder="请输入主持人">
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">嘉宾: </label>

														<div class="col-md-9">
															<input type="text" name="guest" value="${series.guest}"
																class="form-control" placeholder="请输入嘉宾">
														</div>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">年份: </label>

														<div class="col-md-9">
															<input type="text" name="year" value="${series.year}"
																class="form-control validate[custom[integer]]"
																placeholder="请输入年份">
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">地区: </label>

														<div class="col-md-9">
															<input type="text" name="area" value="${series.area}"
																class="form-control" placeholder="请输入地区">
														</div>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">语言: </label>

														<div class="col-md-9">
															<input type="text" name="language"
																value="${series.language}" class="form-control"
																placeholder="请输入语言">
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">评分: </label>

														<div class="col-md-9">
															<input type="text" name="rating" value="${series.rating}"
																class="form-control validate[custom[number]]"
																placeholder="请输入评分">
															<p class="help-block">10分制，有一个小数.</p>
														</div>
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">时长: </label>

														<div class="col-md-9">
															<input type="text" name="duration"
																value="${series.duration}"
																class="form-control validate[custom[integer]]"
																placeholder="请输入时长">
															<p class="help-block">单位分钟.</p>
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">是否有字幕: </label>

														<div class="col-md-9">
															<select name="subtitle" class="form-control">
																<option value="">请选择</option>
																<c:forEach var="item" items="${yesNoEnum}">
																	<option value="${item.key}"
																		<c:if test="${! empty series.subtitle && item.key eq series.subtitle}"> selected="selected" </c:if>>${item.value}</option>
																</c:forEach>
															</select>
														</div>
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">TAG: </label>

														<div class="col-md-9">
															<input type="text" name="tag" value="${series.tag}"
																class="form-control" placeholder="请输入TAG">
															<p class="help-block">多个使用','分割.</p>
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">关键字: </label>

														<div class="col-md-9">
															<input type="text" name="keyword"
																value="${series.keyword}" class="form-control"
																placeholder="请输入关键字">
															<p class="help-block">多个使用','分割.</p>
														</div>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">首播时间: </label>
														<div class="col-md-9">
															<div class="input-group date form_datetime">
																<input type="text" id="orgAirDate" name="orgAirDate"
																	value='<fmt:formatDate value="${series.orgAirDate}" pattern="yyyy-MM-dd HH:mm"/>'
																	size="16" readonly class="form-control"> <span
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
														<label class="control-label col-md-3">看点: </label>

														<div class="col-md-9">
															<input type="text" name="viewpoint"
																value="${series.viewpoint}" class="form-control"
																placeholder="请输入看点">
														</div>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">总集数(<span
															class="required">*</span>):
														</label>

														<div class="col-md-9">
															<input type="text" name="episodeTotal"
																value="${series.episodeTotal}"
																class="form-control validate[required,custom[integer]]"
																placeholder="请输入总集数" />
														</div>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-12">
													<div class="form-group">
														<label class="control-label col-md-3"
															style="width: 12.5%;">简介:</label>

														<div class="col-md-9" style="width: 87.5%;">
															<textarea name="info" rows="8" class="form-control">${series.info}</textarea>
														</div>
													</div>
												</div>
											</div>
										</div>
										<div class="tab-pane" id="tab_1">
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">扩展字段1: </label>
														<div class="col-md-9">
															<input type="text" name="reserved1"
																value="${series.reserved1}" class="form-control"
																placeholder="扩展字段1">
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">扩展字段2: </label>
														<div class="col-md-9">
															<input type="text" name="reserved2"
																value="${series.reserved2}" class="form-control"
																placeholder="扩展字段2">
														</div>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">扩展字段3: </label>
														<div class="col-md-9">
															<input type="text" name="reserved3"
																value="${series.reserved3}" class="form-control"
																placeholder="扩展字段3">
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">扩展字段4: </label>
														<div class="col-md-9">
															<input type="text" name="reserved4"
																value="${series.reserved4}" class="form-control"
																placeholder="扩展字段4">
														</div>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">扩展字段5: </label>
														<div class="col-md-9">
															<input type="text" name="reserved5"
																value="${series.reserved5}" class="form-control"
																placeholder="扩展字段5">
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">来源:</label>

														<div class="col-md-9">
															<p class="form-control-static">
																<tags:enum enumList="${sourceEnum}"
																	value="${series.source}"
																	className="class='badge badge-success'" />
															</p>
														</div>
													</div>
												</div>
											</div>
										</div>
										<div class="tab-pane" id="tab_2">
											<div class="row">
												<c:choose>
													<c:when test="${empty currentCpId}">
														<div class="col-md-6">
															<tags:cpSelect value="${series.cpId}" />
														</div>
													</c:when>
													<c:otherwise>
														<input type="hidden" id="cpId" name="cpId"
															value="${currentCpId}" />
													</c:otherwise>
												</c:choose>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">播出许可证: </label>

														<div class="col-md-9">
															<input type="text" name="broadcastLicense"
																value="${series.broadcastLicense}" class="form-control"
																placeholder="请输入播出许可证">
														</div>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">版权性质: </label>

														<div class="col-md-9">
															<input type="text" name="authorizeInfo"
																value="${series.authorizeInfo}" class="form-control"
																placeholder="请输入版权性质">
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">授权地址: </label>

														<div class="col-md-9">
															<input type="text" name="authorizeAddress"
																value="${series.authorizeAddress}" class="form-control"
																placeholder="请输入授权地址">
														</div>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">授权开始时间(<span
															class="required">*</span>):
														</label>
														<div class="col-md-9">
															<div class="input-group date form_datetime">
																<input type="text" id="licensingWindowStart"
																	name="licensingWindowStart"
																	value='<fmt:formatDate value="${series.licensingWindowStart}"
                                                                                       pattern="yyyy-MM-dd HH:mm"/>'
																	size="16" readonly
																	class="form-control validate[required]"> <span
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
														<label class="control-label col-md-3">授权结束时间(<span
															class="required">*</span>):
														</label>
														<div class="col-md-9">
															<div class="input-group date form_datetime">
																<input type="text" id="licensingWindowEnd"
																	name="licensingWindowEnd"
																	value='<fmt:formatDate value="${series.licensingWindowEnd}"
                                                                                       pattern="yyyy-MM-dd HH:mm"/>'
																	size="16" readonly
																	class="form-control validate[required]"> <span
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
										</div>
										<div class="tab-pane" id="tab_3">
											<div class="row">
												<div class="col-md-6">
													<tags:imageSelect name="image1" desc="横海报"
														value="${series.image1}" readOnly="${readOnly}" />
												</div>
												<div class="col-md-6">
													<tags:imageSelect name="image2" desc="竖海报"
														value="${series.image2}" readOnly="${readOnly}" />
												</div>
											</div>
											<div class="row">
												<div class="col-md-6">
													<tags:imageSelect name="image3" desc="横海报(大)"
														value="${series.image3}" readOnly="${readOnly}" />
												</div>
												<div class="col-md-6">
													<tags:imageSelect name="image4" desc="竖海报(大)"
														value="${series.image4}" readOnly="${readOnly}" />
												</div>
											</div>
										</div>
										<div class="tab-pane" id="tab_4"></div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
				<div id="fileuploadArea" style="display: none">
					<form id="fileupload"
						action="${contextPathPrefix}${series.id}/batchUploadStills"
						method="POST" enctype="multipart/form-data">
						<!-- The table listing the files available for upload/download -->
						<table role="presentation" class="table table-striped clearfix">
							<tbody class="files">
							</tbody>
						</table>
						<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
						<div class="row fileupload-buttonbar">
							<div class="col-lg-7">
								<!-- The fileinput-button span is used to style the file input field as button -->
								<span class="btn green fileinput-button"> <i
									class="fa fa-plus"></i> <span> 批量增加... </span> <input
									type="file" name="files[]" multiple
									accept=".JPEG,.JPG,.PNG,.GIF">
								</span>
								<button type="submit" class="btn blue start">
									<i class="fa fa-upload"></i> <span> 批量上传 </span>
								</button>
								<button type="reset" class="btn red cancel">
									<i class="fa fa-ban"></i> <span> 批量取消 </span>
								</button>
								<button type="button" class="btn red delete">
									<i class="fa fa-trash"></i> <span> 批量删除 </span>
								</button>
								<input type="checkbox" class="toggle"> 全选
								<!-- The global file processing state -->
								<span class="fileupload-process"> </span>
							</div>
							<!-- The global progress information -->
							<div class="col-lg-5 fileupload-progress fade">
								<!-- The global progress bar -->
								<div class="progress progress-striped active" role="progressbar"
									aria-valuemin="0" aria-valuemax="100">
									<div class="progress-bar progress-bar-success"
										style="width: 0%;"></div>
								</div>
								<!-- The extended global progress information -->
								<div class="progress-extended">&nbsp;</div>
							</div>
						</div>
					</form>
				</div>
				<tags:uploadDownload />
			</div>
			<div class="modal-footer">
				<c:if test="${!readOnly}">
					<c:choose>
						<c:when test="${empty series.id}">
							<button class="btn btn-outline green"
								onclick="$.SeriesController.edit('${contextPathPrefix}add', '保存成功！');">
								<i class="fa fa-save"></i>保存
							</button>
						</c:when>
						<c:otherwise>
							<c:if
								test="${series.auditStatus eq 0 || series.auditStatus eq 9}">
								<shiro:hasPermission name="${permissionPrefix}submit">
									<button class="btn btn-outline green"
										onclick="$.SeriesController.edit('${contextPathPrefix}${series.id}/submit', '送审成功！');">
										<i class="fa fa-save"></i>送审
									</button>
								</shiro:hasPermission>
							</c:if>
							<c:if test="${series.auditStatus eq 1}">
								<shiro:hasPermission name="${permissionPrefix}audit">
									<button class="btn btn-outline green"
										onclick="$.SeriesController.edit('${contextPathPrefix}${series.id}/audit/2', '审核成功！');">
										<i class="fa fa-save"></i>审核通过
									</button>
									<button class="btn btn-outline green"
										onclick="$.SeriesController.edit('${contextPathPrefix}${series.id}/audit/9', '审核成功！');">
										<i class="fa fa-save"></i>审核不通过
									</button>
								</shiro:hasPermission>
							</c:if>

							<button class="btn btn-outline green"
								onclick="$.SeriesController.edit('${contextPathPrefix}${series.id}/edit', '保存成功！');">
								<i class="fa fa-save"></i>保存
							</button>
						</c:otherwise>
					</c:choose>
				</c:if>
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>