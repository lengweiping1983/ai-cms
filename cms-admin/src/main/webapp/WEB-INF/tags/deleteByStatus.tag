<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="value" type="java.lang.String" required="false"
	description="状态值"%>
<%@ attribute name="onclick" type="java.lang.String" required="false"
	description=""%>

<c:forEach var="item" items="${statusEnum}">
	<c:if test="${item.key eq value && item.key eq 0}">
		<button class="btn btn-default btn-sm btn-outline green"
			onclick="${onclick}">
			<i class="fa fa-remove"></i>删除
		</button>
	</c:if>
	<c:if test="${item.key eq value && item.key eq 1}">
	</c:if>
	<c:if test="${item.key eq value && item.key eq 2}">
		<button class="btn btn-default btn-sm btn-outline green"
			onclick="${onclick}">
			<i class="fa fa-remove"></i>删除
		</button>
	</c:if>
</c:forEach>

