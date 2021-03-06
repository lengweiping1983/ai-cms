<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">
					<c:choose>
						<c:when test="${empty entry.id}">
							<c:set var="methodDesc" value="增加" />
						</c:when>
						<c:otherwise>
							<c:set var="methodDesc" value="修改" />
						</c:otherwise>
					</c:choose>
					${methodDesc}外部入口
				</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="id" value="${entry.id}" />

					<div class="content form-horizontal">
						<div class="form-group">
							<label class="control-label col-md-4">外部入口代码(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<c:choose>
									<c:when test="${empty entry.id}">
										<input type="text" id="code" name="code" value="${entry.code}"
											class="form-control validate[required,custom[onlyLetterNumberUnderline],ajax[ajaxCodeCheck]]"
											placeholder="请输入外部入口代码">
									</c:when>
									<c:otherwise>
										<input type="text" id="code" name="code" value="${entry.code}"
											class="form-control" placeholder="请输入外部入口代码"
											readonly="readonly" onclick="$.EntryController.code();">
									</c:otherwise>
								</c:choose>
								<p class="help-block">外部入口代码可自定义，可以是任意字符.</p>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-4">外部入口名称(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<input type="text" name="name" value="${entry.name}"
									class="form-control validate[required]" placeholder="请输入外部入口名称">
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-4">服务URL(<span
								class="required">*</span>):
							</label>

							<div class="col-md-6">
								<textarea name="serviceUrl"
									class="form-control validate[required]">${entry.serviceUrl}</textarea>
								<p class="help-block">需要推荐具体内容的URL.</p>
							</div>
						</div>

						<div class="form-group">
							<div class="col-md-12">
								<p class="help-block">
									给到外部推荐时地址为http://ip:port/index/<font color="red">YYY</font>/entry?code=<font
										color="red">XXX</font><br /> 例如:<br />
									&#12288;&#12288;上海为http://222.68.210.92:8086/index/shdx/entry?code=<font
										color="red">对应的外部入口代码</font><br />
								</p>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<c:choose>
					<c:when test="${empty entry.id}">
						<button class="btn btn-outline green"
							onclick="$.EntryController.edit('${ctx}/config/entry/add');">
							<i class="fa fa-save"></i>保存
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-outline green"
							onclick="$.EntryController.edit('${ctx}/config/entry/${entry.id}/edit');">
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