<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="value" type="java.lang.Object" required="false"
	description="mediaTemplateId的值"%>

<c:if test="${not empty value}">
	<c:forEach var="item" items="${mediaTemplateList}">
		<c:forEach var="mediaTemplateId" items="${value}">
			<c:if test="${item.id eq mediaTemplateId}">
				<span class="badge badge-success">${item.title}</span>
			</c:if>
		</c:forEach>
	</c:forEach>
</c:if>