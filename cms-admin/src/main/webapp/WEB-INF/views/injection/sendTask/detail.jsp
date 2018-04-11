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
												<label class="control-label col-md-4">工单ID:</label>

												<div class="col-md-8">
													<p class="form-control-static">${sendTask.id}</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">分发平台:</label>

												<div class="col-md-8">
													<p class="form-control-static">${injectionPlatformMap[sendTask.platformId].name}</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">工单名称:</label>

												<div class="col-md-8">
													<p class="form-control-static">${sendTask.name}</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">工单状态:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<tags:taskStatus value='${sendTask.status}' />
													</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">工单标识:</label>

												<div class="col-md-8">
													<p class="form-control-static">${sendTask.correlateId}</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">优先级:</label>

												<div class="col-md-8">
													<p class="form-control-static">${sendTask.priority}</p>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">前置工单ID:</label>

												<div class="col-md-8">
													<p class="form-control-static">${sendTask.preTaskId}</p>
												</div>
											</div>
										</div>

										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">前置工单是否完成:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<tags:yesNo value='${sendTask.preTaskStatus}' />
													</p>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">第一次发送请求时间:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<fmt:formatDate value="${sendTask.firstRequestTime}"
															pattern="yyyy-MM-dd HH:mm:ss" />
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">最后发送请求时间:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<fmt:formatDate value="${sendTask.lastRequestTime}"
															pattern="yyyy-MM-dd HH:mm:ss" />
													</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">发送请求次数:</label>

												<div class="col-md-8">
													<p class="form-control-static">${sendTask.requestTimes}</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">发送请求总次数:</label>

												<div class="col-md-8">
													<p class="form-control-static">${sendTask.requestTotalTimes}</p>
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
															<c:when test="${!empty sendTask.requestResult}">
																<c:if test="${sendTask.requestResult eq 0}">
																	<span class="badge badge-success">成功</span>
																</c:if>
																<c:if test="${sendTask.requestResult ne 0}">
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
														class="form-control-static" style="">${sendTask.requestErrorDescription}</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">响应时间:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<fmt:formatDate value="${sendTask.responseTime}"
															pattern="yyyy-MM-dd HH:mm:ss" />
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">响应结果:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<c:choose>
															<c:when test="${!empty sendTask.responseResult}">
																<c:if test="${sendTask.responseResult eq 0}">
																	<span class="badge badge-success">成功</span>
																</c:if>
																<c:if test="${sendTask.responseResult ne 0}">
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
														class="form-control-static" style="">${sendTask.responseErrorDescription}</p>
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
														class="form-control-static" style="">${sendTask.cmdFileURL}</p>
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
														class="form-control-static" style="">${sendTask.resultFileURL}</p>
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