<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">单场收费[${leagueMatch.name}]</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-12">
						<h3 class="form-section">默认平台</h3>

						<div class="portlet light bordered">
							<div class="row">
								<div class="col-md-12">
									<div class="form-group">
										<label class="control-label col-md-4">计费代码:</label>

										<div class="col-md-8">
											<p style="word-break: break-all;" class="form-control-static">
												<a href="javascript:;" class="cdnFileCodeEditable"
													data-type="text" data-id="${leagueMatch.id}"
													data-providertype="0" data-pk="1" data-placement="right"
													data-original-title="请输入计费代码">${leagueMatchCode.cdnFileCode}</a>
											</p>
										</div>
									</div>
								</div>
							</div>

<!-- 							<div class="row"> -->
<!-- 								<div class="col-md-12"> -->
<!-- 									<div class="form-group"> -->
<!-- 										<label class="control-label col-md-4">回看代码:</label> -->
<!-- 										<div class="col-md-8"> -->
<!-- 											<p style="word-break: break-all;" class="form-control-static"> -->
<!-- 												<a href="javascript:;" class="partnerScheduleCodeEditable" -->
<%-- 													data-type="text" data-id="${leagueMatch.id}" --%>
<!-- 													data-providertype="10" data-pk="1" data-placement="right" -->
<%-- 													data-original-title="请输入回看代码">${leagueMatchCode.partnerScheduleCode}</a> --%>
<!-- 											</p> -->
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 								</div> -->
<!-- 							</div> -->
						</div>

						<c:if test="${fn:contains(providerType, '1')}">
							<h3 class="form-section">华为平台</h3>

							<div class="portlet light bordered">
								<div class="row">
									<div class="col-md-12">
										<div class="form-group">
											<label class="control-label col-md-4">计费代码:</label>

											<div class="col-md-8">
												<p style="word-break: break-all;"
													class="form-control-static">
													<a href="javascript:;" class="cdnFileCodeEditable"
														data-type="text" data-id="${leagueMatch.id}"
														data-providertype="1" data-pk="1" data-placement="right"
														data-original-title="请输入计费代码">${leagueMatchCode.huaweiFileCode}</a>
												</p>
											</div>
										</div>
									</div>
								</div>

<!-- 								<div class="row"> -->
<!-- 									<div class="col-md-12"> -->
<!-- 										<div class="form-group"> -->
<!-- 											<label class="control-label col-md-4">回看代码:</label> -->
<!-- 											<div class="col-md-8"> -->
<!-- 												<p style="word-break: break-all;" -->
<!-- 													class="form-control-static"> -->
<!-- 													<a href="javascript:;" class="partnerScheduleCodeEditable" -->
<%-- 														data-type="text" data-id="${leagueMatch.id}" --%>
<!-- 														data-providertype="11" data-pk="1" data-placement="right" -->
<%-- 														data-original-title="请输入回看代码">${leagueMatchCode.huaweiScheduleCode}</a> --%>
<!-- 												</p> -->
<!-- 											</div> -->
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 								</div> -->
							</div>
						</c:if>

						<c:if test="${fn:contains(providerType, '2')}">
							<h3 class="form-section">中兴平台</h3>

							<div class="portlet light bordered">
								<div class="row">
									<div class="col-md-12">
										<div class="form-group">
											<label class="control-label col-md-4">计费代码:</label>

											<div class="col-md-8">
												<p style="word-break: break-all;"
													class="form-control-static">
													<a href="javascript:;" class="cdnFileCodeEditable"
														data-type="text" data-id="${leagueMatch.id}"
														data-providertype="2" data-pk="1" data-placement="right"
														data-original-title="请输入计费代码">${leagueMatchCode.zteFileCode}</a>
												</p>
											</div>
										</div>
									</div>
								</div>

<!-- 								<div class="row"> -->
<!-- 									<div class="col-md-12"> -->
<!-- 										<div class="form-group"> -->
<!-- 											<label class="control-label col-md-4">回看代码:</label> -->
<!-- 											<div class="col-md-8"> -->
<!-- 												<p style="word-break: break-all;" -->
<!-- 													class="form-control-static"> -->
<!-- 													<a href="javascript:;" class="partnerScheduleCodeEditable" -->
<%-- 														data-type="text" data-id="${leagueMatch.id}" --%>
<!-- 														data-providertype="12" data-pk="1" data-placement="right" -->
<%-- 														data-original-title="请输入回看代码">${leagueMatchCode.zteScheduleCode}</a> --%>
<!-- 												</p> -->
<!-- 											</div> -->
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 								</div> -->
							</div>
						</c:if>

						<c:if test="${fn:contains(providerType, '3')}">
							<h3 class="form-section">UT平台</h3>

							<div class="portlet light bordered">
								<div class="row">
									<div class="col-md-12">
										<div class="form-group">
											<label class="control-label col-md-4">计费代码:</label>

											<div class="col-md-8">
												<p style="word-break: break-all;"
													class="form-control-static">
													<a href="javascript:;" class="cdnFileCodeEditable"
														data-type="text" data-id="${leagueMatch.id}"
														data-providertype="3" data-pk="1" data-placement="right"
														data-original-title="请输入计费代码">${leagueMatchCode.utFileCode}</a>
												</p>
											</div>
										</div>
									</div>
								</div>

<!-- 								<div class="row"> -->
<!-- 									<div class="col-md-12"> -->
<!-- 										<div class="form-group"> -->
<!-- 											<label class="control-label col-md-4">回看代码:</label> -->
<!-- 											<div class="col-md-8"> -->
<!-- 												<p style="word-break: break-all;" -->
<!-- 													class="form-control-static"> -->
<!-- 													<a href="javascript:;" class="partnerScheduleCodeEditable" -->
<%-- 														data-type="text" data-id="${leagueMatch.id}" --%>
<!-- 														data-providertype="13" data-pk="1" data-placement="right" -->
<%-- 														data-original-title="请输入回看代码">${leagueMatchCode.utScheduleCode}</a> --%>
<!-- 												</p> -->
<!-- 											</div> -->
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 								</div> -->
							</div>
						</c:if>

						<c:if test="${fn:contains(providerType, '4')}">
							<h3 class="form-section">烽火平台</h3>

							<div class="portlet light bordered">
								<div class="row">
									<div class="col-md-12">
										<div class="form-group">
											<label class="control-label col-md-4">计费代码:</label>

											<div class="col-md-8">
												<p style="word-break: break-all;"
													class="form-control-static">
													<a href="javascript:;" class="cdnFileCodeEditable"
														data-type="text" data-id="${leagueMatch.id}"
														data-providertype="4" data-pk="1" data-placement="right"
														data-original-title="请输入计费代码">${leagueMatchCode.fiberhomeFileCode}</a>
												</p>
											</div>
										</div>
									</div>
								</div>

<!-- 								<div class="row"> -->
<!-- 									<div class="col-md-12"> -->
<!-- 										<div class="form-group"> -->
<!-- 											<label class="control-label col-md-4">回看代码:</label> -->
<!-- 											<div class="col-md-8"> -->
<!-- 												<p style="word-break: break-all;" -->
<!-- 													class="form-control-static"> -->
<!-- 													<a href="javascript:;" class="partnerScheduleCodeEditable" -->
<%-- 														data-type="text" data-id="${leagueMatch.id}" --%>
<!-- 														data-providertype="14" data-pk="1" data-placement="right" -->
<%-- 														data-original-title="请输入回看代码">${leagueMatchCode.fiberhomeScheduleCode}</a> --%>
<!-- 												</p> -->
<!-- 											</div> -->
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 								</div> -->
							</div>
						</c:if>

					</div>
				</div>
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

<script>
	if (App.getURLParameter('mode') == 'inline') {
		$.fn.editable.defaults.mode = 'inline';
		$('#inline').attr("checked", true);
		jQuery.uniform.update('#inline');
	} else {
		$('#inline').attr("checked", false);
		jQuery.uniform.update('#inline');
	}

	$.fn.editable.defaults.inputclass = 'form-control';

	$('.cdnFileCodeEditable').editable(
			{
				validate : function(value) {
					//if ($.trim(value) == '') return 'This field is required';
				},
				type : 'text',
				success : function(response, newValue) {
					var _this = $(this);
					var id = _this.data('id');
					var param = {};
					param['type'] = _this.data('providertype');
					param['data'] = newValue;
					$.ajax({
						type : "POST",
						url : contextPath + "/league/leagueMatch/" + id
								+ "/changeData",
						data : param,
						success : function(result) {
						},
						error : function() {
							alert("保存错误!");
						}
					});
				}
			});

	$('.partnerScheduleCodeEditable').editable(
			{
				validate : function(value) {
					//if ($.trim(value) == '') return 'This field is required';
				},
				type : 'text',
				success : function(response, newValue) {
					var _this = $(this);
					var id = _this.data('id');
					var param = {};
					param['type'] = _this.data('providertype');
					param['data'] = newValue;
					$.ajax({
						type : "POST",
						url : contextPath + "/league/leagueMatch/" + id
								+ "/changeData",
						data : param,
						success : function(result) {
						},
						error : function() {
							alert("保存错误!");
						}
					});
				}
			});
</script>
