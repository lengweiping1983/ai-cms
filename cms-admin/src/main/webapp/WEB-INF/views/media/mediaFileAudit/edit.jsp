<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<c:set var="readOnly" value="${readOnly}" />
<c:set var="contextPathPrefix" value="${ctx}/media/mediaFileAudit/" />
<c:set var="permissionPrefix" value="media:mediaFileAudit:" />
<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">
					<c:choose>
						<c:when test="${empty mediaFile.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="查看" />
							<c:if test="${!readOnly}">
								<c:set var="methodDesc" value="修改" />
							</c:if>
						</c:otherwise>
					</c:choose>
					${methodDesc}媒体内容[${program.name}]
				</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="id" value="${mediaFile.id}" /> <input
						type="hidden" name="seriesId" value="${mediaFile.seriesId}" /> <input
						type="hidden" name="programId" value="${mediaFile.programId}" />
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-12">
								<div class="tabbable-line boxless tabbable-reversed">
									<ul class="nav nav-tabs">
										<li class="active"><a href="#tab_0" data-toggle="tab">媒体内容元数据</a></li>
										<li><a href="#tab_1" data-toggle="tab">扩展数据</a></li>
										<li><a href="#tab_4" data-toggle="tab">分发</a></li>
									</ul>
									<div class="tab-content">
										<div class="tab-pane active" id="tab_0">
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">所属节目: </label>

														<div class="col-md-9">
															<p style="word-break: break-all;"
																class="form-control-static">${program.name}</p>
														</div>
													</div>
												</div>
											</div>
											<c:if test="${!empty mediaFile.id}">
												<div class="row">
													<div class="col-md-6">
														<div class="form-group">
															<label class="control-label col-md-3">媒资状态: </label>

															<div class="col-md-9">
																<p style="word-break: break-all;"
																	class="form-control-static">${fns:getMediaStatusDesc(mediaFile.mediaStatus)}</p>
															</div>
														</div>
													</div>
													<div class="col-md-6">
														<div class="form-group">
															<label class="control-label col-md-3">上线状态: </label>

															<div class="col-md-9">
																<p style="word-break: break-all;"
																	class="form-control-static">
																	<tags:status value="${mediaFile.status}" />
																</p>
															</div>
														</div>
													</div>
												</div>
												<div class="row">
													<div class="col-md-6">
														<div class="form-group">
															<label class="control-label col-md-3">码率: </label>

															<div class="col-md-9">
																<p style="word-break: break-all;"
																	class="form-control-static">${mediaFile.bitrate}</p>
																<p class="help-block">单位为K.</p>
															</div>
														</div>
													</div>
													<div class="col-md-6">
														<div class="form-group">
															<label class="control-label col-md-3">分辨率: </label>

															<div class="col-md-9">
																<p style="word-break: break-all;"
																	class="form-control-static">${mediaFile.resolution}</p>
															</div>
														</div>
													</div>
												</div>
												<div class="row">
													<div class="col-md-6">
														<div class="form-group">
															<label class="control-label col-md-3">格式: </label>

															<div class="col-md-9">
																<p style="word-break: break-all;"
																	class="form-control-static">${mediaFile.format}</p>
															</div>
														</div>
													</div>
													<div class="col-md-6">
														<div class="form-group">
															<label class="control-label col-md-3">规格: </label>

															<div class="col-md-9">
																<p style="word-break: break-all;"
																	class="form-control-static">${mediaFile.mediaSpec}</p>
															</div>
														</div>
													</div>
												</div>
											</c:if>
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">码率(<span
															class="required">*</span>):
														</label>

														<div class="col-md-9">
															<select name="templateId"
																class="form-control select2 validate[required]">
																<option value="">请选择</option>
																<c:forEach var="item" items="${mediaTemplateList}">
																	<option value="${item.id}"
																		<c:if test="${! empty mediaFile.templateId && item.id eq mediaFile.templateId}"> selected="selected" </c:if>>${item.title}</option>
																</c:forEach>
															</select>
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">类型(<span
															class="required">*</span>):
														</label>

														<div class="col-md-9">
															<select id="type" name="type"
																class="form-control select2 validate[required]">
																<c:forEach var="item" items="${typeEnum}">
																	<option value="${item.key}"
																		<c:if test="${item.key eq mediaFile.type}"> selected="selected" </c:if>>${item.value}</option>
																</c:forEach>
															</select>
														</div>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">文件路径:</label>

														<div class="col-md-9">
															<textarea id="simplefilePath" name="filePath" rows="3"
																onchange="$.MediaFileController.changeFilePath();"
																class="form-control">${mediaFile.filePath}</textarea>
															<p class="help-block">
																<button class="btn btn-default btn-sm btn-outline green"
																	onclick="$.MediaFileController.toSelectFile(); return false;">
																	<i class="fa fa-select"></i>选择文件
																</button>
																<button class="btn btn-default btn-sm btn-outline green"
																	id="filePath_div_1"
																	<c:if test="${empty mediaFile.filePath}"> disabled </c:if>
																	onclick="$.MediaFileController.toPreview(); return false;">
																	<i class="fa fa-eye"></i>预览
																</button>
															</p>
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">文件md5值: </label>

														<div class="col-md-9">
															<input type="text" name="fileMd5"
																value="${mediaFile.fileMd5}" class="form-control"
																placeholder="请输入文件md5值">
														</div>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">文件大小: </label>

														<div class="col-md-9">
															<input type="text" name="fileSize"
																value="${mediaFile.fileSize}"
																class="form-control validate[custom[integer]]"
																placeholder="请输入文件大小">
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">播放时长: </label>

														<div class="col-md-9">
															<input type="text" name="duration"
																value="${mediaFile.duration}"
																class="form-control validate[custom[integer]]"
																placeholder="请输入播放时长">
															<p class="help-block">单位为秒.</p>
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
																value="${mediaFile.reserved1}" class="form-control"
																placeholder="扩展字段1">
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">扩展字段2: </label>
														<div class="col-md-9">
															<input type="text" name="reserved2"
																value="${mediaFile.reserved2}" class="form-control"
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
																value="${mediaFile.reserved3}" class="form-control"
																placeholder="扩展字段3">
														</div>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">扩展字段4: </label>
														<div class="col-md-9">
															<input type="text" name="reserved4"
																value="${mediaFile.reserved4}" class="form-control"
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
																value="${mediaFile.reserved5}" class="form-control"
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
																	value="${mediaFile.source}"
																	className="class='badge badge-success'" />
															</p>
														</div>
													</div>
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
			</div>
			<div class="modal-footer">
				<c:if test="${!readOnly}">
					<c:choose>
						<c:when test="${empty mediaFile.id}">
							<button class="btn btn-outline green"
								onclick="$.MediaFileController.edit('${contextPathPrefix}add');">
								<i class="fa fa-save"></i>保存
							</button>
						</c:when>
						<c:otherwise>
							<button class="btn btn-outline green"
								onclick="$.MediaFileController.edit('${contextPathPrefix}${mediaFile.id}/edit');">
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