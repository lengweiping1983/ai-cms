<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">批量分发</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="itemType" value="${itemType}" /> <input
						type="hidden" name="itemIds" value="${itemIds}" />
					<div class="content form-horizontal">
						<div class="row">
							<div class="col-md-12">
								<table
									class="table dataTable table-striped table-bordered table-hover table-checkable">
									<thead>
										<tr>
											<th>序号</th>
											<th>平台</th>
											<th width="8%">是否需要分发</th>
											<th>选择码率</th>
											<th>优先级</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="t" items="${injectionPlatformList}"
											varStatus="status">
											<c:if test="${t.direction eq 1}">
												<tr>
													<c:set var="injectionPlatformSelected" value="" />
													<c:if test="${t.siteCode eq fns:getSiteCode()}">
														<c:set var="injectionPlatformSelected" value="1" />
													</c:if>
													<td>${status.index+1}</td>
													<td><tags:enum className='class="badge badge-success"'
															enumList="${platformTypeEnum}" value="${t.type}" />${t.name}</td>
													<td><input type="checkbox" name="platformId"
														<c:if test="${injectionPlatformSelected eq 1}"> checked </c:if>
														value="${t.id}" class="form-control validate[required]" /></td>
													<td><input type="hidden" id="${t.id}templateId"
														name="templateId" value="${t.templateId}" disabled />
														<div id="${t.id}templateId_parent"
															<c:if test="${!empty t.name}"> style="display: none" </c:if>>
															<select class="form-control select2 validate[required]"
																id="${t.id}select_templateId" name="select_templateId"
																multiple="multiple">
																<c:forEach var="item" items="${mediaTemplateList}">
																	<c:set var="templateIdSelected" value="" />
																	<c:forEach var="templateId" items="${t.templateId}">
																		<c:if test="${item.id eq templateId}">
																			<c:set var="templateIdSelected" value="2" />
																			<c:if test="${canInjectionTemplateIdMap[item.id]}">
																				<c:set var="templateIdSelected" value="1" />
																			</c:if>
																		</c:if>
																	</c:forEach>
																	<option value="${item.id}"
																		<c:if test="${!canInjectionTemplateIdMap[item.id]}">disabled="disabled"</c:if>
																		<c:if test="${templateIdSelected eq 1}">selected="selected"</c:if>>
																		${item.title}
																		<c:if test="${templateIdSelected eq 2}">[缺媒体内容]</c:if>
																	</option>
																</c:forEach>
															</select>
														</div></td>
													<td><select id="${t.id}priority" name="priority"
														class="form-control" disabled>
															<c:forEach varStatus="status" begin="1" end="20">
																<option value="${status.index}"
																	<c:if test="${status.index eq 5}"> selected="selected" </c:if>>${status.index}</option>
															</c:forEach>
													</select></td>
												</tr>
											</c:if>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button class="btn btn-outline green"
					onclick="$.SeriesController.batchInjection('${ctx}/media/series/batchInjection');">
					<i class="fa fa-save"></i>确定分发
				</button>
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>