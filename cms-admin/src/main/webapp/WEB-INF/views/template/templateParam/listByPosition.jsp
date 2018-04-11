<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-globe"></i>[${template.name}]设置热点
					</div>
					<div class="actions">
						<a href="javascript:;" class="btn btn-default btn-sm"
							onclick="$.common.ajaxLoadLastContent()"> <i
							class="fa fa-reply"></i> 返回到模板管理
						</a>
					</div>
				</div>
				<div class="portlet-body">
					<!-- 					<div id="ndd-annotations-global-container"></div> -->
					<!-- 					<div id="wrap"> -->
					<!-- 						<div class="container-fluid"> -->
					<!-- 						<header> -->
					<%-- 							<h1>[${template.name}]设置热点</h1> --%>
					<!-- 							<p class="lead"></p> -->
					<!-- 						</header> -->
					<form role="form" class="form">
						<div class="btn-group" data-toggle="buttons"
							style="display: none;">
							<label class="btn btn-lg btn-default active"> <input
								type="radio" name="radio-editor-mode"
								id="radio-editor-mode-edit" checked="">编辑
							</label> <label class="btn btn-lg btn-default"
								id="radio-editor-mode-preview-label"> <input
								type="radio" name="radio-editor-mode"
								id="radio-editor-mode-preview">预览
							</label> <label class="btn btn-lg btn-default"
								id="radio-editor-mode-jquery-label"> <input type="radio"
								name="radio-editor-mode" id="radio-editor-mode-jquery">jQuery代码
							</label> <label class="btn btn-lg btn-default"
								id="radio-editor-mode-load-label"> <input type="radio"
								name="radio-editor-mode" id="radio-editor-mode-load">测试代码正确性
							</label>
						</div>
					</form>
					<input type="hidden" id="editType" name="editType" value="template" />
					<input type="hidden" id="templateId" name="templateId"
						value="${template.id}" /> <input type="hidden" id="positionStr"
						name="positionStr" value='${positionStr}' /> <input type="hidden"
						id="backgroundImage" name="backgroundImage"
						value="${template.backgroundImage}">

					<c:if test="${empty template.backgroundImage}">
						<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
					</c:if>
					<c:if test="${! empty template.backgroundImage}">
						<c:set var="imageUrl"
							value="${fns:getImagePath(template.backgroundImage)}" />
					</c:if>
					<div class="panel panel-default-not" id="panel-editor"
						style="display: block;">
						<div class="panel-body-not" id="panel-canvas">
							<div class="ndd-drawable-canvas"
								style="width: 100px; height: 100px; left: 0px; top: 0px;">
								<img src="${imageUrl}" class="ndd-drawable-canvas-image"
									id="the-edit-img-tag">
								<div class="ndd-drawables-container"></div>
							</div>
						</div>
					</div>
					<div class="panel panel-default" id="panel-preview"
						style="display: none;">
						<div class="panel-body" id="plugin-container"
							style="width: 100%; height: 600px;">
							<div class="ndd-annotator-container"
								id="ndd-annotator-container-1">
								<div class="ndd-annotator-content"
									style="position: absolute; width: 100px; height: 100px; left: 0px; top: 0px;">
									<img src="${imageUrl}" id="the-img-tag"
										class="ndd-annotator-main-image"
										style="width: 100%; height: 100%; opacity: 1;">
								</div>
								<div class="ndd-annotator-interface"></div>
							</div>
						</div>
					</div>
					<div class="panel panel-default" id="panel-jquery"
						style="display: none;">
						<div class="panel-body">
							<label class="btn btn-primary" id="button-select-jquery">
								全选</label><br> <br>
							<div class="well" id="well-jquery">
								<code> $("#the-img-tag").annotatorPro({}); </code>
							</div>
						</div>
					</div>
					<div class="panel panel-default" id="panel-load"
						style="display: none;">
						<div class="panel-body">
							<textarea class="form-control" rows="10" id="textarea-load"></textarea>
							<br> <label class="btn btn-lg btn-primary" id="button-load"
								disabled=""> 验证</label>
						</div>
					</div>
					<h2 style="display: none;">设置</h2>
					<div class="row" style="display: none;">
						<!-- Popups -->
						<div class="col-md-8">
							<div class="panel panel-default">
								<div id="panel-disabler"></div>
								<div class="panel-heading">注释</div>
								<div class="panel-body">
									<form role="form" class="form-horizontal">
										<div class="col-md-6 col-sm-6">
											<!-- Content Type -->
											<div class="form-group">
												<label class="col-md-3 control-label"> 内容类别</label>
												<div class="col-md-9">
													<div class="btn-group" data-toggle="buttons">
														<label class="btn btn-default active"> <input
															type="radio" name="radio-content-type"
															id="radio-content-type-text" checked="">文本
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-content-type"
															id="radio-content-type-custom-html">自定义HTML
														</label>
													</div>
												</div>
											</div>
											<!-- Title -->
											<div class="form-group">
												<label class="col-md-3 control-label"> 标题</label>
												<div class="col-md-9">
													<input type="text" class="form-control" id="input-title"
														placeholder="Enter title">
												</div>
											</div>
											<!-- Text -->
											<div class="form-group">
												<label class="col-md-3 control-label"> 文本</label>
												<div class="col-md-9">
													<textarea class="form-control" rows="3" id="textarea-text"></textarea>
												</div>
											</div>
											<!-- Text Color -->
											<div class="form-group">
												<label for="color-text-color" class="col-md-3 control-label">
													文本颜色</label>
												<div class="col-md-9">
													<div class="input-group">
														<input type="color" class="form-control"
															id="color-text-color" value="#ffffff"> <span
															class="input-group-addon" id="color-text-color-hex">#000000</span>
													</div>
												</div>
											</div>
											<!-- HTML -->
											<div class="form-group">
												<label class="col-md-3 control-label"> HTML</label>
												<div class="col-md-9">
													<textarea class="form-control" rows="3" id="textarea-html"
														disabled="disabled"></textarea>
												</div>
											</div>

											<div class="form-group">
												<label class="col-md-3 control-label"> 位置</label>
												<div class="col-md-9">
													<input type="text" class="form-control" id="input-position">
												</div>
											</div>
											<!-- ID -->
											<div class="form-group">
												<label class="col-md-3 control-label"> ID</label>
												<div class="col-md-9">
													<input type="text" class="form-control" id="input-id"
														placeholder="Enter ID">
													<p class="help-block">用于深度链接</p>
												</div>
											</div>
											<!-- Deep linking URL -->
											<div class="form-group">
												<label class="col-md-3 control-label"> 深度链接URL</label>
												<div class="col-md-9">
													<div id="input-deep-link-url" class="well"></div>
													<p id="input-deep-link-url-help" class="help-block"></p>
												</div>
											</div>
											<!-- Delete -->
											<div class="form-group">
												<label class="col-md-3 control-label"> 删除</label>
												<div class="col-md-9">
													<label class="btn btn-danger" data-toggle="modal"
														data-target="#modal-delete"> 删除注释</label>
												</div>
											</div>
										</div>
										<div class="col-md-6 col-sm-6">
											<!-- Tint Color -->
											<div class="form-group">
												<label for="color-tint-color" class="col-md-3 control-label">
													提示框颜色</label>
												<div class="col-md-9">
													<div class="input-group">
														<input type="color" class="form-control"
															id="color-tint-color" value="#000000"> <span
															class="input-group-addon" id="color-tint-color-hex">#000000</span>
													</div>
												</div>
											</div>
											<!-- Style -->
											<div class="form-group">
												<label class="col-md-3 control-label"> 风格</label>
												<div class="col-md-9">
													<div class="btn-group btn-group-no-margin"
														data-toggle="buttons" id="btn-group-style-circle">
														<label class="btn btn-default active"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-1" checked="">
															<div class="icon-in-label ndd-spot-icon icon-style-1">
																<div class="ndd-icon-main-element"></div>
																<div class="ndd-icon-border-element"></div>
															</div>
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-2">
															<div class="icon-in-label ndd-spot-icon icon-style-2">
																<div class="ndd-icon-main-element"></div>
																<div class="ndd-icon-border-element"></div>
															</div>
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-3">
															<div class="icon-in-label ndd-spot-icon icon-style-3">
																<div class="ndd-icon-main-element"></div>
																<div class="ndd-icon-border-element"></div>
															</div>
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-4">
															<div class="icon-in-label ndd-spot-icon icon-style-4">
																<div class="ndd-icon-main-element"></div>
																<div class="ndd-icon-border-element"></div>
															</div>
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-5">
															<div class="icon-in-label ndd-spot-icon icon-style-5">
																<img
																	src="${ctx}/static/plugins/annotator/icon_loc_01.png">
															</div>
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-6">
															<div class="icon-in-label ndd-spot-icon icon-style-6">
																<img
																	src="${ctx}/static/plugins/annotator/icon_loc_02.png">
															</div>
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-7">
															<div class="icon-in-label ndd-spot-icon icon-style-7">
																<img
																	src="${ctx}/static/plugins/annotator/icon_loc_03.png">
															</div>
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-8">
															<div class="icon-in-label ndd-spot-icon icon-style-8">
																<img
																	src="${ctx}/static/plugins/annotator/icon_loc_04.png">
															</div>
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-9">
															<div class="icon-in-label ndd-spot-icon icon-style-9">
																<img
																	src="${ctx}/static/plugins/annotator/icon_loc_05.png">
															</div>
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-10">
															<div class="icon-in-label ndd-spot-icon icon-style-10">
																<img
																	src="${ctx}/static/plugins/annotator/icon_loc_06.png">
															</div>
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-11">
															<div class="icon-in-label ndd-spot-icon icon-style-11">
																<img
																	src="${ctx}/static/plugins/annotator/icon_loc_07.png">
															</div>
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-12">
															<div class="icon-in-label ndd-spot-icon icon-style-12">
																<img
																	src="${ctx}/static/plugins/annotator/icon_loc_08.png">
															</div>
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-13">
															<div class="icon-in-label ndd-spot-icon icon-style-13">
																<img
																	src="${ctx}/static/plugins/annotator/icon_loc_09.png">
															</div>
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-0">
															<div style="height: 44px; line-height: 44px;">隐藏</div>
														</label>
													</div>
													<div class="btn-group btn-group-no-margin"
														data-toggle="buttons" id="btn-group-style-rect">
														<label class="btn btn-default active"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-rect-1" checked="">
															<div
																class="icon-in-label ndd-spot-icon icon-style-rect-1">
																<div class="ndd-icon-main-element"></div>
																<div class="ndd-icon-border-element"></div>
															</div>
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-rect-2">
															<div
																class="icon-in-label ndd-spot-icon icon-style-rect-2">
																<div class="ndd-icon-main-element"></div>
																<div class="ndd-icon-border-element"></div>
															</div>
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-rect-3">
															<div
																class="icon-in-label ndd-spot-icon icon-style-rect-3">
																<div class="ndd-icon-main-element"></div>
																<div class="ndd-icon-border-element"></div>
															</div>
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-rect-4">
															<div
																class="icon-in-label ndd-spot-icon icon-style-rect-4">
																<div class="ndd-icon-main-element"></div>
																<div class="ndd-icon-border-element"></div>
															</div>
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-style-rect-0">
															<div style="height: 44px; line-height: 44px;">隐藏</div>
														</label>
													</div>
												</div>
											</div>
											<!-- Popup Width -->
											<div class="form-group">
												<label class="col-md-3 control-label"> 提示框宽度</label>
												<div class="col-md-7">
													<div class="input-group">
														<input type="text" class="form-control"
															id="input-popup-width"> <span
															class="input-group-addon" id="input-popup-width-addon">px</span>
													</div>
												</div>
												<div class="col-md-2">
													<div class="checkbox">
														<label> <input type="checkbox"
															id="checkbox-popup-width-auto" checked="">Auto
														</label>
													</div>
												</div>
											</div>
											<!-- Popup Height -->
											<div class="form-group">
												<label class="col-md-3 control-label"> 提示框高度</label>
												<div class="col-md-7">
													<div class="input-group">
														<input type="text" class="form-control"
															id="input-popup-height"> <span
															class="input-group-addon" id="input-popup-height-addon">px</span>
													</div>
												</div>
												<div class="col-md-2">
													<div class="checkbox">
														<label> <input type="checkbox"
															id="checkbox-popup-height-auto" checked="">Auto
														</label>
													</div>
												</div>
											</div>
											<!-- Popup Position -->
											<div class="form-group">
												<label for="radio-popup-position"
													class="col-md-3 control-label"> 提示框位置</label>
												<div class="col-md-9">
													<div class="btn-group" data-toggle="buttons">
														<label class="btn btn-default active"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-position-top" checked="">上面
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-position-bottom">下面
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-position-left">左面
														</label> <label class="btn btn-default"> <input
															type="radio" name="radio-popup-position"
															id="radio-popup-position-right">右面
														</label>
													</div>
												</div>
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
						<!-- Navigation -->
						<div class="col-md-4">
							<div class="panel panel-default">
								<div class="panel-heading">导航</div>
								<div class="panel-body">
									<form role="form" class="form-horizontal">
										<!-- Width -->
										<div class="form-group">
											<label class="col-md-3 control-label"> 宽</label>
											<div class="col-md-7">
												<div class="input-group">
													<input type="text" class="form-control" id="input-width">
													<span class="input-group-addon" id="input-width-addon">px</span>
												</div>
											</div>
											<div class="col-md-2">
												<div class="checkbox">
													<label> <input type="checkbox"
														id="checkbox-width-auto" checked="">Auto
													</label>
												</div>
											</div>
										</div>
										<!-- Height -->
										<div class="form-group">
											<label class="col-md-3 control-label"> 高</label>
											<div class="col-md-9">
												<div class="input-group">
													<input type="text" class="form-control" id="input-height">
													<span class="input-group-addon" id="input-height-addon">px</span>
												</div>
											</div>
										</div>
										<!-- Max Zoom -->
										<div class="form-group">
											<label class="col-md-3 control-label"> 放大</label>
											<div class="col-md-9">
												<div class="btn-group" data-toggle="buttons">
													<label class="btn btn-default active"> <input
														type="radio" name="radio-max-zoom" id="radio-max-zoom-1-1"
														checked="">1:1
													</label> <label class="btn btn-default"> <input
														type="radio" name="radio-max-zoom" id="radio-max-zoom-1">1x
													</label> <label class="btn btn-default"> <input
														type="radio" name="radio-max-zoom" id="radio-max-zoom-2">2x
													</label> <label class="btn btn-default"> <input
														type="radio" name="radio-max-zoom" id="radio-max-zoom-3">3x
													</label> <label class="btn btn-default"> <input
														type="radio" name="radio-max-zoom" id="radio-max-zoom-4">4x
													</label> <label class="btn btn-default"> <input
														type="radio" name="radio-max-zoom"
														id="radio-max-zoom-custom">自定义
													</label>
												</div>
											</div>
										</div>
										<div class="form-group">
											<div class="col-md-offset-3 col-md-9">
												<input type="text" class="form-control" id="input-max-zoom"
													value="4" disabled="disabled">
											</div>
										</div>
										<!-- Show Navigator -->
										<div class="form-group">
											<label for="checkbox-navigator"
												class="col-md-3 control-label"> 导航</label>
											<div class="col-md-9">
												<div class="checkbox">
													<label> <input type="checkbox"
														id="checkbox-navigator">
													</label>
												</div>
											</div>
										</div>
										<!-- Show Navigator Image Preview -->
										<div class="form-group">
											<label for="checkbox-navigator-image-preview"
												class="col-md-3 control-label"> 导航图片预览</label>
											<div class="col-md-9">
												<div class="checkbox">
													<label> <input type="checkbox"
														id="checkbox-navigator-image-preview">
													</label>
												</div>
											</div>
										</div>
										<!-- Enable Fullscreen -->
										<div class="form-group">
											<label for="checkbox-fullscreen"
												class="col-md-3 control-label"> 宽屏</label>
											<div class="col-md-9">
												<div class="checkbox">
													<label> <input type="checkbox"
														id="checkbox-fullscreen">
													</label>
												</div>
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>
					<!-- 						</div> -->
					<!-- 					</div> -->
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END CONTENT BODY -->

<tags:contentModal />

<div class="modal fade" id="content_list_modal_container" tabindex="-1"
	role="basic" aria-hidden="true">
	<div class="modal-dialog modal-full" id="content_list_dialog_container">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title" id="content_list_modal_container_title">选择</h4>
			</div>
			<div class="modal-body" id="content_list_container"></div>
			<div class="modal-footer">
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>


<script>
	
</script>
