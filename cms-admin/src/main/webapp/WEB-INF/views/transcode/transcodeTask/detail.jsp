<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">查看任务</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-12">
						<div class="portlet light bordered">
							<div class="portlet-body form">
								<div class="form-body">
									<h3 class="form-section">任务详情</h3>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">任务ID:</label>

												<div class="col-md-8">
													<p class="form-control-static">${transcodeTask.id}</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">任务名称:</label>

												<div class="col-md-8">
													<p class="form-control-static">${transcodeTask.name}</p>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">任务类型:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<c:forEach var="item" items="${typeEnum}">
															<c:if test="${item.key eq transcodeTask.type}">
															${item.value}
															</c:if>
														</c:forEach>
													</p>
												</div>
											</div>
										</div>

										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">&#12288;&#12288;码率:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<c:forEach var="mediaTemplate"
															items="${mediaTemplateList}">
															<c:if
																test="${! empty transcodeTask.templateId && mediaTemplate.id eq transcodeTask.templateId}">
																<span class="badge badge-success">${mediaTemplate.code}</span>
															</c:if>
														</c:forEach>
													</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">工单类型:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<c:forEach var="item" items="${transcodeRequestTypeEnum}">
															<c:if test="${item.key eq transcodeRequest.type}">
															${item.value}
															</c:if>
														</c:forEach>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">工单名称:</label>

												<div class="col-md-8">
													<p class="form-control-static">${transcodeRequest.name}</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">优先级:</label>

												<div class="col-md-8">
													<p class="form-control-static">${transcodeTask.priority}</p>
												</div>
											</div>
										</div>

										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">任务状态:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<c:forEach var="item" items="${statusEnum}">
															<c:if
																test="${item.key eq transcodeTask.status && item.key eq 1}">
																<span class="badge badge-info">${item.value}</span>
															</c:if>
															<c:if
																test="${item.key eq transcodeTask.status && item.key eq 2}">
																<span class="badge badge-primary">${item.value}</span>
															</c:if>
															<c:if
																test="${item.key eq transcodeTask.status && item.key eq 3}">
																<span class="badge badge-success">${item.value}</span>
															</c:if>
															<c:if
																test="${item.key eq transcodeTask.status && (item.key eq 4 || item.key eq 5 || item.key eq 9)}">
																<span class="badge badge-danger">${item.value}</span>
															</c:if>
														</c:forEach>
													</p>
												</div>
											</div>
										</div>
									</div>

									<c:if test="${!empty preTranscodeTask.id}">
										<div class="row">
											<div class="col-md-6">
												<div class="form-group">
													<label class="control-label col-md-4">前置任务ID:</label>

													<div class="col-md-8">
														<p class="form-control-static">${preTranscodeTask.id}</p>
													</div>
												</div>
											</div>

											<div class="col-md-6">
												<div class="form-group">
													<label class="control-label col-md-4">前置任务状态:</label>

													<div class="col-md-8">
														<p class="form-control-static">
															<c:forEach var="item" items="${statusEnum}">
																<c:if
																	test="${item.key eq preTranscodeTask.status && item.key eq 1}">
																	<span class="badge badge-info">${item.value}</span>
																</c:if>
																<c:if
																	test="${item.key eq preTranscodeTask.status && item.key eq 2}">
																	<span class="badge badge-primary">${item.value}</span>
																</c:if>
																<c:if
																	test="${item.key eq preTranscodeTask.status && item.key eq 3}">
																	<span class="badge badge-success">${item.value}</span>
																</c:if>
																<c:if
																	test="${item.key eq preTranscodeTask.status && (item.key eq 4 || item.key eq 5)}">
																	<span class="badge badge-danger">${item.value}</span>
																</c:if>
															</c:forEach>
														</p>
													</div>
												</div>
											</div>
										</div>
									</c:if>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">第一次发送指令时间:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<fmt:formatDate value="${transcodeTask.firstRequestTime}"
															pattern="yyyy-MM-dd HH:mm:ss" />
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">最后发送指令时间:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<fmt:formatDate value="${transcodeTask.lastRequestTime}"
															pattern="yyyy-MM-dd HH:mm:ss" />
													</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">发送指令次数:</label>

												<div class="col-md-8">
													<p class="form-control-static">${transcodeTask.requestTimes}</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">发送指令总次数:</label>

												<div class="col-md-8">
													<p class="form-control-static">${transcodeTask.requestTotalTimes}</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">外部响应时间:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<fmt:formatDate value="${transcodeTask.responseTime}"
															pattern="yyyy-MM-dd HH:mm:ss" />
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">外部任务Id:</label>

												<div class="col-md-8">
													<p class="form-control-static">${transcodeTask.cloudTaskId}</p>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-12">
											<div class="form-group">
												<label class="control-label col-md-2">外部响应信息:</label>

												<div class="col-md-10">
													<p style="word-break: break-all;"
														class="form-control-static">${transcodeTask.responseMsg}</p>
												</div>
											</div>
										</div>
									</div>

									<h3 class="form-section">文件信息</h3>

									<div class="row">
										<div class="col-md-12">
											<div class="form-group">
												<label class="control-label col-md-2">输入文件路径:</label>

												<div class="col-md-10">
													<p style="word-break: break-all;"
														class="form-control-static">${transcodeTask.inputFilePath}</p>
												</div>
											</div>
										</div>
									</div>

									<c:if test="${!empty transcodeTask.inputSubtitleFilePath}">
										<div class="row">
											<div class="col-md-12">
												<div class="form-group">
													<label class="control-label col-md-2">字幕文件路径:</label>

													<div class="col-md-10">
														<p style="word-break: break-all;"
															class="form-control-static">${transcodeTask.inputSubtitleFilePath}</p>
													</div>
												</div>
											</div>
										</div>
									</c:if>

									<div class="row">
										<div class="col-md-12">
											<div class="form-group">
												<label class="control-label col-md-2">输出文件路径:</label>

												<div class="col-md-10">
													<p style="word-break: break-all;"
														class="form-control-static" style="">${transcodeTask.outputFilePath}</p>
												</div>
											</div>
										</div>
									</div>

								</div>
							</div>
						</div>
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