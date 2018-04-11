<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="value" type="java.lang.String" required="false"
	description="状态值"%>

<c:if test="${not empty value}">
	<c:forEach var="item" items="${yesNoEnum}">
		<c:if test="${item.key eq value && (item.key eq 0)}">
			<span class="badge badge-info">未正常</span>
		</c:if>
		<c:if test="${item.key eq value && (item.key eq 1)}">
			<span class="badge badge-success">正常</span>
		</c:if>
	</c:forEach>
</c:if>
