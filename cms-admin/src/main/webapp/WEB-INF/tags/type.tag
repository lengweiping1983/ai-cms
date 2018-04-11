<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="value" type="java.lang.String" required="false"
	description="类型值"%>

<c:if test="${not empty value}">
	<c:forEach var="item" items="${typeEnum}">
		<c:if test="${item.key eq value}">
			<span>${item.value}</span>
		</c:if>
	</c:forEach>
</c:if>

