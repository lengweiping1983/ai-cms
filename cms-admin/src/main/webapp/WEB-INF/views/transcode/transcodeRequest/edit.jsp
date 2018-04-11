<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<c:set var="readOnly" value="${readOnly}" />
<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">
					<c:choose>
						<c:when test="${empty transcodeRequest.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="查看" />
							<c:if test="${!readOnly}">
								<c:set var="methodDesc" value="修改" />
							</c:if>
						</c:otherwise>
					</c:choose>
					${methodDesc}转码工单
					<c:forEach var="item" items="${typeEnum}">
						<c:if
							test="${! empty transcodeRequest.type && item.key eq transcodeRequest.type}">[${item.value}]</c:if>
					</c:forEach>
				</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<div class="content form-horizontal">
						<input type="hidden" name="id" value="${transcodeRequest.id}">
						<input type="hidden" id="type" name="type"
							value="${transcodeRequest.type}">

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">工单名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="name"
											value="${transcodeRequest.name}"
											class="form-control validate[required]" placeholder="请输入工单名称">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">媒资名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<div class="input-group">
											<input type="hidden" name="mediaId" id="mediaId"
												value="${transcodeRequest.mediaId}" /> <input type="text"
												class="form-control validate[required]" name="mediaName"
												id="mediaName" value="${transcodeRequest.mediaName}"
												placeholder="选择媒资或输入新增媒资名称"
												onChange="$.TranscodeRequestController.changeMediaName(this);" />
											<span class="input-group-btn">
												<button
													onclick="$.TranscodeRequestController.toSelectMedia('${transcodeRequest.type}');"
													class="btn btn-success" type="button">
													<i class="fa fa-arrow-left fa-fw" /></i> 选择媒资
												</button>
											</span>
										</div>
										<p class="help-block">
											<span class="badge badge-success">选择媒资</span>为在该媒资下新增对应的码率<br />
											<span class="badge badge-success">输入名称</span>为新增媒资
										</p>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">内容类型(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select id="contentType" name="contentType"
											class="form-control">
											<c:forEach var="item" items="${contentTypeEnum}">
												<option value="${item.key}"
													<c:if test="${item.key eq transcodeRequest.contentType}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">文件名标识(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" id="mediaFilename" name="mediaFilename"
											value="${transcodeRequest.mediaFilename}"
											class="form-control validate[required,custom[onlyLetterNumberUnderline]]"
											placeholder="请输入文件名标识">

										<p class="help-block">用作转码后的媒体文件名.</p>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">TAG: </label>

									<div class="col-md-9">
										<input type="text" id="tag" name="tag"
											value="${transcodeRequest.tag}" class="form-control"
											placeholder="请输入TAG">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">内部标签: </label>

									<div class="col-md-9">
										<input type="text" id="internalTag" name="internalTag"
											value="${transcodeRequest.internalTag}" class="form-control"
											placeholder="请输入内部标签">
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<tags:cpSelect value="${transcodeRequest.cpId}" />
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">优先级(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="priority" class="form-control">
											<c:forEach varStatus="status" begin="1" end="20">
												<option value="${status.index}"
													<c:if test="${status.index eq transcodeRequest.priority}"> selected="selected" </c:if>>${status.index}</option>
											</c:forEach>
										</select>
										<p class="help-block">数字越大优先级越高</p>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<tags:mediaTemplateSelect value="${transcodeRequest.templateId}" />
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">备注:</label>

									<div class="col-md-9">
										<textarea name="comment" rows="8" class="form-control">${transcodeRequest.comment}</textarea>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3" style="padding-top: 0px;">转码模式(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select id="genTask" name="genTask" class="form-control">
											<c:forEach var="item" items="${genTaskModeStatusEnum}">
												<option value="${item.key}"
													<c:if test="${item.key eq transcodeRequest.genTask}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3" style="padding-top: 0px;">是否覆盖媒体文件(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select id="coverFile" name="coverFile" class="form-control">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${item.key eq transcodeRequest.coverFile}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
										<p class="help-block">用新的媒体文件覆盖</p>
									</div>
								</div>
							</div>
						</div>

						<!-- 						<div class="row"> -->
						<!-- 							<div class="col-md-6"> -->
						<!-- 								<div class="form-group"> -->
						<!-- 									<label class="control-label col-md-3" style="padding-top: 0px;">是否需要截图(<span -->
						<!-- 										class="required">*</span>): -->
						<!-- 									</label> -->

						<!-- 									<div class="col-md-9"> -->
						<!-- 										<select id="needSnapshot" name="needSnapshot" -->
						<!-- 											class="form-control"> -->
						<%-- 											<c:forEach var="item" items="${needSnapshotEnum}"> --%>
						<%-- 												<option value="${item.key}" --%>
						<%-- 													<c:if test="${item.key eq transcodeRequest.needSnapshot}"> selected="selected" </c:if>>${item.value}</option> --%>
						<%-- 											</c:forEach> --%>
						<!-- 										</select> -->
						<!-- 									</div> -->
						<!-- 								</div> -->
						<!-- 							</div> -->

						<!-- 							<div class="col-md-6"> -->
						<!-- 								<div class="form-group"> -->
						<!-- 									<label class="control-label col-md-3" style="padding-top: 0px;">原始文件处理(<span -->
						<!-- 										class="required">*</span>): -->
						<!-- 									</label> -->

						<!-- 									<div class="col-md-9"> -->
						<!-- 										<select id="originFileDeal" name="originFileDeal" -->
						<!-- 											class="form-control"> -->
						<%-- 											<c:forEach var="item" items="${originFileDealEnum}"> --%>
						<%-- 												<option value="${item.key}" --%>
						<%-- 													<c:if test="${item.key eq transcodeRequest.originFileDeal}"> selected="selected" </c:if>>${item.value}</option> --%>
						<%-- 											</c:forEach> --%>
						<!-- 										</select> -->
						<!-- 									</div> -->
						<!-- 								</div> -->
						<!-- 							</div> -->
						<!-- 						</div> -->
					</div>
				</form>

				<h4 class="form-section">媒体文件列表</h4>

				<form id="fileForm">
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-12">
								<table
									class="table dataTable table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th width="60%">媒体文件路径</th>
											<th>媒体内容类型</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody id="transcodeRequest_file_list">
										<c:forEach var="file" items="${transcodeRequest.fileList}">
											<tr id="tr_file_${file.id}" class="tr_file_css">
												<td><input type="hidden" name="id" value="${file.id}">
													<input type="hidden" name="filePath"
													value="${file.filePath}">${file.filePath}</td>
												<td><select name="mediaFileType">
														<c:forEach var="item" items="${mediaFileTypeEnum}">
															<option value="${item.key}"
																<c:if test="${item.key eq file.mediaFileType}"> selected="selected" </c:if>>${item.value}</option>
														</c:forEach>
												</select></td>
												<td>
													<button type="button"
														class="btn btn-default btn-sm btn-outline green"
														onclick="$.TranscodeRequestController.toDeleteFile('${file.id}', '${file.filePath}');">
														<i class="fa fa-remove"></i>删除
													</button>
												</td>
											</tr>
										</c:forEach>
										<tr id="tr_file_hidden" style="display: none"></tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<c:if test="${!readOnly}">
					<button class="btn btn-outline green"
						onclick="$.FileManageController.toSelectItem('${ctx}/media/file/selectFile?selectMode=multi');">
						<i class="fa fa-plus"></i>增加文件
					</button>
					<button id="TranscodeRequestSave" class="btn btn-outline green"
						onclick="$.TranscodeRequestController.edit('${ctx}/transcode/transcodeRequest/edit');">
						<i class="fa fa-save"></i>保存
					</button>
					<button id="TranscodeRequestProduce" class="btn btn-outline green"
						onclick="$.TranscodeRequestController.produce('${ctx}/transcode/transcodeRequest/produce');">
						<i class="fa fa-hourglass-start"></i>执行
					</button>
				</c:if>

				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>