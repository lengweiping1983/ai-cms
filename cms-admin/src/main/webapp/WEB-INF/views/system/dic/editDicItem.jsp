<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade bs-modal-lg" id="dic_edit_modal_div" role="basic"
	aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-titlCommonControllere">字典项管理</h4>
			</div>
			<div class="modal-body">
				<form id="dic_edit_form">
					<div class="content form-horizontal">
						<div class="form-group">
							<div class="col-md-12">
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-4">字典名称:</label>

											<div class="col-md-8">
												<p class="form-control-static">${dic.name}</p>
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-4">字典代码:</label>

											<div class="col-md-8">
												<p class="form-control-static">${dic.code}</p>
											</div>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="form-group">
											<label class="control-label col-md-2">字典描述:</label>

											<div class="col-md-10">
												<p class="form-control-static">${dic.description}</p>
											</div>
										</div>
									</div>
								</div>
								<tags:message content="${message}" />
								<div class="col-md-12">
									<table class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th>项代码</th>
												<th>项名称</th>
												<th>项值</th>
												<th>排序值</th>
												<th>操作</th>
											</tr>
										</thead>
										<tfoot>
											<tr>
												<th>项代码</th>
												<th>项名称</th>
												<th>项值</th>
												<th>排序值</th>
												<th>操作</th>
											</tr>
										</tfoot>
										<tbody id="dic_item_list">
											<c:forEach var="dicItem" items="${dic.dicItemList}">
												<tr id="tr_dic_item_${dicItem.id}" class="tr_dic_item_css">
													<td>${dicItem.code}</td>
													<td>${dicItem.name}</td>
													<td>${dicItem.value}</td>
													<td>${dicItem.sort}</td>
													<td>
														<button type="button"
															class="btn btn-default btn-sm btn-outline green"
															onclick="$.DicController.toDeleteDicItem('${dicItem.id}','${dicItem.name}');">
															<i class="fa fa-remove"></i>删除
														</button>
													</td>
												</tr>
											</c:forEach>
											<tr id="dic_item_form_vs">
												<td><input type="text"
													class="form-control validate[required,custom[onlyLetterNumberUnderline],ajax[ajaxDicItemCheck]]"
													name="code" id="dic_item_code" placeholder="项代码" /></td>
												<td><input type="text"
													class="form-control validate[required,ajax[ajaxDicItemCheck]]"
													name="name" id="dic_item_name" placeholder="项名称" /></td>
												<td><input type="text"
													class="form-control validate[required,ajax[ajaxDicItemCheck]"
													name="value" id="dic_item_value" placeholder="项值" /></td>
												<td><input type="text"
													class="form-control validate[required,custom[integer]]"
													id="dic_item_sort" value="999" name="sort"
													placeholder="排序值" /></td>
												<td>
													<button type="button"
														class="btn btn-default btn-sm btn-outline green"
														onclick="$.DicController.addDicItem(${dic.id});">
														<i class="fa fa-save"></i>保存
													</button>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</form>
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