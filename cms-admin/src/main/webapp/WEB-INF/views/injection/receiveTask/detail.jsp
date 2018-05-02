<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">查看工单</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-12">
						<div class="portlet light bordered">
							<div class="portlet-body form">
								<div class="form-body">
									<h3 class="form-section">工单详情</h3>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">接收时间:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<fmt:formatDate value="${receiveTask.receiveTime}"
															pattern="yyyy-MM-dd HH:mm:ss" />
													</p>
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
																test="${item.key eq receiveTask.status && item.key eq 0}">
																<span class="badge badge-info">${item.value}</span>
															</c:if>
															<c:if
																test="${item.key eq receiveTask.status && (item.key eq 1 || item.key eq 2 || item.key eq 6)}">
																<span class="badge badge-primary">${item.value}</span>
															</c:if>
															<c:if
																test="${item.key eq receiveTask.status && item.key eq 3}">
																<span class="badge badge-success">${item.value}</span>
															</c:if>
															<c:if
																test="${item.key eq receiveTask.status && (item.key eq 4 || item.key eq 5)}">
																<span class="badge badge-danger">${item.value}</span>
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
												<label class="control-label col-md-4">优先级:</label>

												<div class="col-md-8">
													<p class="form-control-static">${receiveTask.priority}</p>
												</div>
											</div>
										</div>

										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">下载文件次数:</label>

												<div class="col-md-8">
													<p class="form-control-static">${receiveTask.downloadTimes}</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">第一次发送响应时间:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<fmt:formatDate value="${receiveTask.firstResponseTime}"
															pattern="yyyy-MM-dd HH:mm:ss" />
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">最后发送响应时间:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<fmt:formatDate value="${receiveTask.lastResponseTime}"
															pattern="yyyy-MM-dd HH:mm:ss" />
													</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">发送响应次数:</label>

												<div class="col-md-8">
													<p class="form-control-static">${receiveTask.responseTimes}</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">发送响应总次数:</label>

												<div class="col-md-8">
													<p class="form-control-static">${receiveTask.responseTotalTimes}</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">请求结果:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<c:choose>
															<c:when test="${!empty receiveTask.requestResult}">
																<c:if test="${receiveTask.requestResult eq 0}">
																	<span class="badge badge-success">成功</span>
																</c:if>
																<c:if test="${receiveTask.requestResult ne 0}">
																	<span class="badge badge-danger">失败</span>
																</c:if>
															</c:when>
															<c:otherwise>
															</c:otherwise>
														</c:choose>
													</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-12">
											<div class="form-group">
												<label class="control-label col-md-2">请求结果描述:</label>

												<div class="col-md-10">
													<p style="word-break: break-all;"
														class="form-control-static" style="">${receiveTask.requestErrorDescription}</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">响应结果:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<c:choose>
															<c:when test="${!empty receiveTask.responseResult}">
																<c:if test="${receiveTask.responseResult eq 0}">
																	<span class="badge badge-success">成功</span>
																</c:if>
																<c:if test="${receiveTask.responseResult ne 0}">
																	<span class="badge badge-danger">失败</span>
																</c:if>
															</c:when>
															<c:otherwise>
															</c:otherwise>
														</c:choose>
													</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-12">
											<div class="form-group">
												<label class="control-label col-md-2">响应结果描述:</label>

												<div class="col-md-10">
													<p style="word-break: break-all;"
														class="form-control-static" style="">${receiveTask.responseErrorDescription}</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-12">
											<div class="form-group">
												<label class="control-label col-md-2">指令XML文件的URL:</label>

												<div class="col-md-10">
													<p style="word-break: break-all;"
														class="form-control-static" style="">${receiveTask.cmdFileURL}</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-12">
											<div class="form-group">
												<label class="control-label col-md-2">结果XML文件的URL:</label>

												<div class="col-md-10">
													<p style="word-break: break-all;"
														class="form-control-static" style="">${receiveTask.resultFileURL}</p>
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