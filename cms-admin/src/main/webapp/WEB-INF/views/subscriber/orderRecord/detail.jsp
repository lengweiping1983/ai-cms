<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="modal fade" tabindex="-1" role="basic" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true"></button>
				<h4 class="modal-title">查看订单信息</h4>
			</div>
			<div class="modal-body">
				<form id="editForm">
					<input type="hidden" name="id" value="${orderRecord.id}" />

					<div class="content form-horizontal">
						<div class="form-group">
							<label class="control-label col-md-4">结果:
							</label>

							<div class="col-md-6">
								<textarea name="description" class="form-control">${orderRecord.result}</textarea>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-md-4">描述:</label>

							<div class="col-md-6">
								<textarea name="description" class="form-control">${orderRecord.description}</textarea>
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