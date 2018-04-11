<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="value" type="java.lang.Object" required="false"
	description="cpId的值"%>

<c:if test="${not empty value}">
	<c:forEach var="item" items="${cpList}">
		<c:forEach var="cpId" items="${value}">
			<c:if test="${item.id.toString() eq cpId}">
				<span class="badge badge-success">${item.shortName}</span>
			</c:if>
		</c:forEach>
	</c:forEach>
</c:if>