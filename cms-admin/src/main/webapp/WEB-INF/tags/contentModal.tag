<%@ tag language="java" pageEncoding="UTF-8"%>

<div id="content_modal_container"></div>

<div class="modal fade" id="content_list_modal_container" tabindex="-1"
	role="basic" aria-hidden="true">
	<div class="modal-dialog modal-full"
		id="content_list_modal_container_dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true" id="content_list_modal_container_x"></button>
				<h4 class="modal-title" id="content_list_modal_container_title">选择</h4>
			</div>
			<div class="modal-body" id="content_list_modal_container_body"></div>
			<div class="modal-footer" id="content_list_modal_container_footer">
				<button type="button" class="btn btn-outline green"
					style="display: none" id="content_list_modal_container_ok">
					<i class="fa fa-check"></i>确定
				</button>
				<button class="btn grey-salsa btn-outline" data-dismiss="modal"
					aria-hidden="true" id="content_list_modal_container_close">
					<i class="fa fa-close"></i>关闭
				</button>
			</div>
		</div>
	</div>
</div>