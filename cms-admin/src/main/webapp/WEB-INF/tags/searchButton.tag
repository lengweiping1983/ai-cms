<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld"%>
<%@ attribute name="containerId" type="java.lang.String"
	required="false" description="容器Id"%>
<%@ attribute name="formId" type="java.lang.String" required="false"
	description="formId"%>
<%@ attribute name="toggle" type="java.lang.String" required="false"
	description=""%>

<div style="float: right">
	<button class="btn btn-default btn-sm btn-outline green" type="button"
		onclick="$.Page.queryByForm({containerId: '${containerId}', formId: '${formId}'}); return false;">
		<i class="fa fa-search"></i> 查询
	</button>
	<input type="hidden" id="${formId}condition_open_status"
		name="condition_open_status" value="${param.condition_open_status}" />
	<c:if test="${!empty toggle}">
		<button class="btn btn-default btn-sm btn-outline green"
			onclick="$.BaseController.toggleSearchCondition('${formId}'); return false;">
			<i
				<c:if test="${param.condition_open_status != 1}"> class="fa fa-level-down" </c:if>
				<c:if test="${param.condition_open_status == 1}"> class="fa fa-level-up" </c:if>
				id="${formId}condition_open_button"></i>
		</button>
	</c:if>
</div>