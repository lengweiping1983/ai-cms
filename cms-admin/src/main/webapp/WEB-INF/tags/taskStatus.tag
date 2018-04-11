<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="value" type="java.lang.String" required="false"
	description="状态值"%>

<c:if test="${not empty value}">
	<c:forEach var="item" items="${statusEnum}">
		<c:if test="${item.key eq value && item.key eq 0}">
			<span class="badge badge-warning">${item.value}</span>
		</c:if>
		<c:if test="${item.key eq value && (item.key eq 1 || item.key eq 6)}">
			<span class="badge badge-info">${item.value}</span>
		</c:if>
		<c:if test="${item.key eq value && item.key eq 2}">
			<span class="badge badge-primary">${item.value}</span>
		</c:if>
		<c:if
			test="${item.key eq value && (item.key eq 3)}">
			<span class="badge badge-success">${item.value}</span>
		</c:if>
		<c:if test="${item.key eq value && (item.key eq 4 || item.key eq 5 || item.key eq 7 || item.key eq 8 || item.key eq 9)}">
			<span class="badge badge-danger">${item.value}</span>
		</c:if>
	</c:forEach>
</c:if>
