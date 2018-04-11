<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="value" type="java.lang.String" required="false"
	description="值"%>

<c:if test="${not empty value}">
	<c:forEach var="tag" items="${value}">
		<span class="badge badge-success">${tag}</span>
	</c:forEach>
</c:if>