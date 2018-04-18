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
						<c:when test="${empty mediaTemplate.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					${methodDesc}码率模板
				</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="id" value="${mediaTemplate.id}" />

					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">码率模板代码(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<c:choose>
											<c:when test="${empty mediaTemplate.id}">
												<input type="text" id="code" name="code"
													value="${mediaTemplate.code}"
													class="form-control validate[required,custom[onlyLetterNumberUnderline],ajax[ajaxCodeCheck]]"
													placeholder="请输入码率模板代码">
											</c:when>
											<c:otherwise>
												<input type="text" id="code" name="code"
													value="${mediaTemplate.code}" class="form-control"
													placeholder="请输入码率模板代码" readonly="readonly"
													onclick="$.MediaTemplateController.code();">
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">码率模板名称(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="title" value="${mediaTemplate.title}"
											class="form-control validate[required]"
											placeholder="请输入码率模板名称">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">视频编码格式(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="vCodec"
											value="${mediaTemplate.vCodec}"
											class="form-control validate[required]"
											placeholder="请输入视频编码格式">
										<p class="help-block">视频编码格式:H264、H265.</p>
									</div>
								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">模式(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="vBitrateMode"
											value="${mediaTemplate.vBitrateMode}"
											class="form-control validate[required]" placeholder="请输入模式">
										<p class="help-block">模式:CBR、VBR.</p>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">分辨率(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="vResolution"
											value="${mediaTemplate.vResolution}"
											class="form-control validate[required]" placeholder="请输入分辨率">
										<p class="help-block">分辨率:1080P、720P.</p>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">清晰度(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="definition"
											value="${mediaTemplate.definition}"
											class="form-control validate[required]" placeholder="请输入清晰度">
										<p class="help-block">清晰度:SD、HD.</p>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">视频包装格式(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="vFormat"
											value="${mediaTemplate.vFormat}"
											class="form-control validate[required]"
											placeholder="请输入视频包装格式">
										<p class="help-block">视频包装格式:TS、MP4.</p>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">视频码率(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="vBitrate"
											value="${mediaTemplate.vBitrate}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入视频码率">
										<p class="help-block">视频码率，单位为K.</p>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">最大码率(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="vMaxBitrate"
											value="${mediaTemplate.vMaxBitrate}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入最大码率">
										<p class="help-block">最大码率，单位为K.</p>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">最小码率(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="vMinBitrate"
											value="${mediaTemplate.vMinBitrate}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入最小码率">
										<p class="help-block">最小码率，单位为K.</p>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">帧率(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="vFramerate"
											value="${mediaTemplate.vFramerate}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入帧率">
										<p class="help-block">帧率:25.</p>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">GOP: </label>

									<div class="col-md-9">
										<input type="text" name="vGop" value="${mediaTemplate.vGop}"
											class="form-control validate[custom[integer]]"
											placeholder="请输入GOP">
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">音频编码格式(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="aCodec"
											value="${mediaTemplate.aCodec}"
											class="form-control validate[required]"
											placeholder="请输入音频编码格式">
										<p class="help-block">音频编码格式:AAC、MP2.</p>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">音频码率(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<input type="text" name="aBitrate"
											value="${mediaTemplate.aBitrate}"
											class="form-control validate[required,custom[integer]]"
											placeholder="请输入音频码率">
										<p class="help-block">音频码率:128.</p>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">转码方式(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="transcodeMode"
											class="form-control validate[required]">
											<c:forEach var="item" items="${transcodeModeEnum}">
												<option value="${item.key}"
													<c:if test="${! empty mediaTemplate.transcodeMode && item.key eq mediaTemplate.transcodeMode}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">外部系统转码代码: </label>

									<div class="col-md-9">
										<input type="text" name="externalCode"
											value="${mediaTemplate.externalCode}" class="form-control"
											placeholder="请输入外部系统转码代码">
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-3">是否做2pass(<span
										class="required">*</span>):
									</label>

									<div class="col-md-9">
										<select name="v2pass" class="form-control validate[required]">
											<c:forEach var="item" items="${yesNoEnum}">
												<option value="${item.key}"
													<c:if test="${! empty mediaTemplate.v2pass && item.key eq mediaTemplate.v2pass}"> selected="selected" </c:if>>${item.value}</option>
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
										<select name="status" class="form-control validate[required]">
											<c:forEach var="item" items="${statusEnum}">
												<option value="${item.key}"
													<c:if test="${! empty mediaTemplate.status && item.key eq mediaTemplate.status}"> selected="selected" </c:if>>${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty mediaTemplate.id}">
						<button class="btn btn-outline green"
							onclick="$.MediaTemplateController.edit('${ctx}/config/mediaTemplate/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.MediaTemplateController.edit('${ctx}/config/mediaTemplate/${mediaTemplate.id}/edit');">
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