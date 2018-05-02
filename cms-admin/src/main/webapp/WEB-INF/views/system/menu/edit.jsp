<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<link href="${ctx}/assets/global/css/plugins.css" rel="stylesheet" type="text/css" />

<div class="modal fade" id="menu_edit_modal_div" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
				<h4 class="modal-titlCommonControllere">
					<c:choose>
						<c:when test="${empty menu.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					<c:if test="${menu.type == 1}">
                        ${methodDesc}菜单
                    </c:if>
					<c:if test="${menu.type == 2}">
                        ${methodDesc}权限
                    </c:if>
					<c:choose>
						<c:when test="${!empty menu.parent}">
							<c:set var="parentId" value="${menu.parent.id}" />
							<c:set var="parentName" value="${menu.parent.name}" />
						</c:when>
						<c:otherwise>
							<c:set var="parentId" value="-1" />
							<c:set var="parentName" value="" />
						</c:otherwise>
					</c:choose>
				</h4>
			</div>
			<div class="modal-body">
				<form id="menu_edit_form">
					<div class="content form-horizontal">
						<!-- 内容开始  -->
						<div class="form-group">
							<div class="col-md-12">
								<!-- 菜单表单开始 -->
								<input type="hidden" name="parent_id" id="parent_id" value="${parentId}" />

								<div id="menu_div" <c:if test="${menu.type == 2}"> style="display: none" </c:if>>
									<tags:message content="${message}" />
									<div class="form-group">
										<label class="control-label col-md-3">所属菜单:</label>

										<div class="col-md-8">

											<div class="input-group">
												<input type="text" class="form-control" name="menu_parent_name" id="menu_parent_name" value="${parentName}" placeholder="点击我选择所属菜单" readonly="readonly"
													onclick="$.MenuController.showTree('${menu.id}');" /> <span class="input-group-btn">
													<button onclick="$.MenuController.showTree('${menu.id}');" class="btn btn-success" type="button">
														<i class="fa fa-arrow-left fa-fw" /></i> 选择菜单
													</button>
												</span>
											</div>
										</div>
									</div>

									<div class="form-group">
										<label class="control-label col-md-3">菜单名称(<span class="required">*</span>):
										</label>

										<div class="col-md-8">
											<div class="input_tip">
												<input type="text" class="form-control validate[required]" name="menu_name" id="menu_name" value="${menu.name}" placeholder="菜单名称" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-md-3">菜单链接(<span class="required">*</span>):
										</label>

										<div class="col-md-8">
											<div class="input_tip">
												<input type="text" class="form-control validate[required]" name="menu_href" id="menu_href" value="${menu.href}" placeholder="菜单链接" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-md-3">排序值(<span class="required">*</span>):
										</label>

										<div class="col-md-8">
											<div class="input_tip">
												<input type="text" class="form-control validate[required,custom[integer]]" name="menu_sort" id="menu_sort" value="${menu.sort}" placeholder="排序值" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-md-3">是否显示(<span class="required">*</span>): </label>

										<div class="col-md-8">
											<select name="menu_isShow" id="menu_isShow" class="form-control">
												<option value="">请选择</option>
												<c:forEach var="item" items="${yesNoEnum}">
													<option value="${item.key}"
														<c:if test="${! empty menu.isShow && item.key eq menu.isShow}"> selected="selected" </c:if>>${item.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-md-3">菜单图标:</label>

										<div class="col-md-8">
											<div class="input-group">
												<div class="input-icon">
													<c:set var="menuIcon" value="${menu.icon}" />
													<c:if test="${empty menuIcon}">
														<c:set var="menuIcon" value="glyphicon glyphicon-th-large" />
													</c:if>
													<i id="menu_icon_show" class="${menuIcon}"></i> <input type="text" class="form-control" name="menu_icon" id="menu_icon" placeholder="点击我选择图标" readonly="readonly"
														onclick="$.MenuController.showIcon();" value="${menu.icon}" />
												</div>
												<span class="input-group-btn">
													<button onclick="$.MenuController.showIcon();" class="btn btn-success" type="button">
														<i class="fa fa-arrow-left fa-fw" /></i> 选择图标
													</button>
												</span>
											</div>
										</div>
									</div>
								</div>
								<!-- 菜单表单结束 -->

								<!-- 权限表单开始 -->
								<div id="permission_div" <c:if test="${menu.type == 1}"> style="display: none" </c:if>>
									<tags:message content="${message}" />
									<div class="form-group">
										<label class="control-label col-md-3">所属菜单(<span class="required">*</span>):
										</label>

										<div class="col-md-8">

											<div class="input-group">
												<input type="text" class="form-control validate[required]" name="permission_parent_name" value="${parentName}" id="permission_parent_name" placeholder="点击我选择所属菜单" readonly="readonly"
													onclick="$.MenuController.showTree('${menu.id}');" /> <span class="input-group-btn">
													<button onclick="$.MenuController.showTree('${menu.id}');" class="btn btn-success" type="button">
														<i class="fa fa-arrow-left fa-fw" /></i> 选择菜单
													</button>
												</span>
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-md-3">权限名称(<span class="required">*</span>):
										</label>

										<div class="col-md-8">
											<div class="input_tip">
												<input type="text" class="form-control validate[required]" name="permission_name" value="${menu.name}" id="permission_name" placeholder="权限名称" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-md-3">权限标识(<span class="required">*</span>):
										</label>

										<div class="col-md-8">
											<div class="input_tip">
												<input type="text" class="form-control validate[required]" name="permission_permission" value="${menu.permission}" id="permission_permission" placeholder="权限标识" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-md-3">排序值(<span class="required">*</span>):
										</label>

										<div class="col-md-8">
											<div class="input_tip">
												<input type="text" class="form-control validate[required,custom[integer]]" name="permission_sort" id="permission_sort" value="${menu.sort}" placeholder="排序值" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-md-3">是否显示(<span class="required">*</span>): </label>

										<div class="col-md-8">
											<select name="permission_isShow" id="permission_isShow" class="form-control">
												<option value="">请选择</option>
												<c:forEach var="item" items="${yesNoEnum}">
													<option value="${item.key}"
														<c:if test="${! empty menu.isShow && item.key eq menu.isShow}"> selected="selected" </c:if>>${item.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
								<!-- 权限表单结束 -->

							</div>
						</div>
						<!-- 内容结束  -->
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty menu.id}">
						<button class="btn btn-outline green" onclick="$.MenuController.edit('${ctx}/system/menu/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green" onclick="$.MenuController.edit('${ctx}/system/menu/${menu.id}/edit');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:otherwise>
				</c:choose>
				<button class="btn grey-salsa btn-outline" data-dismiss="modal" aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>