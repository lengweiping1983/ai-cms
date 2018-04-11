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
												<label class="control-label col-md-4">ID:</label>

												<div class="col-md-8">
													<p class="form-control-static">${downloadTask.id}</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">任务名称:</label>

												<div class="col-md-8">
													<p class="form-control-static">${downloadTask.name}</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">优先级:</label>

												<div class="col-md-8">
													<p class="form-control-static">${downloadTask.priority}</p>
												</div>
											</div>
										</div>

										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">任务状态:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<tags:taskStatus value='${downloadTask.status}' />
													</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">第一次下载时间:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<fmt:formatDate value="${downloadTask.firstRequestTime}"
															pattern="yyyy-MM-dd HH:mm:ss" />
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">最后下载时间:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<fmt:formatDate value="${downloadTask.lastRequestTime}"
															pattern="yyyy-MM-dd HH:mm:ss" />
													</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">下载次数:</label>

												<div class="col-md-8">
													<p class="form-control-static">${downloadTask.requestTimes}</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">下载总次数:</label>

												<div class="col-md-8">
													<p class="form-control-static">${downloadTask.requestTotalTimes}</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">完成百分比:</label>

												<div class="col-md-8">
													<p class="form-control-static">
														<span class="badge badge-success">${downloadTask.percent}%</span>
													</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">文件大小:</label>

												<div class="col-md-8">
													<p class="form-control-static">${fns:getFileSizeS(downloadTask.fileSize)}
														<c:if test="${downloadTask.fileSize gt 0}">
													(${downloadTask.fileSize})
													</c:if>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">文件md5值:</label>

												<div class="col-md-8">
													<p class="form-control-static">${downloadTask.fileMd5}</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">原始文件大小:</label>

												<div class="col-md-8">
													<p class="form-control-static">${fns:getFileSizeS(downloadTask.sourceFileSize)}
														<c:if test="${downloadTask.sourceFileSize gt 0}">
													(${downloadTask.sourceFileSize})
													</c:if>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-4">原始文件md5值:</label>

												<div class="col-md-8">
													<p class="form-control-static">${downloadTask.sourceFileMd5}</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-12">
											<div class="form-group">
												<label class="control-label col-md-2">输入文件路径:</label>

												<div class="col-md-10">
													<p style="word-break: break-all;"
														class="form-control-static" style="">${downloadTask.inputFilePath}</p>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-md-12">
											<div class="form-group">
												<label class="control-label col-md-2">输出文件路径:</label>

												<div class="col-md-10">
													<p style="word-break: break-all;"
														class="form-control-static" style="">${downloadTask.outputFilePath}</p>
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