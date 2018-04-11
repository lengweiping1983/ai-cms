<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>

<div class="page-content">
	<div class="row">
		<div class="col-md-3">
			<div class="category-left">
				<div class="portlet light bordered">
					<div class="portlet-body">
						<input type="hidden" id="currentNodeId" name="currentNodeId" value="${param.currentNodeId}">
						<input type="hidden" id="currentNodeFrom" name="currentNodeFrom" value="${param.from}">
						<div id="category_tree" class="tree-demo"></div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-9">
			<div class="category-content" id="categoryPageInfo"></div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$.CategoryController.showLeftTree();
</script>