<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="value" type="java.lang.Object" required="false"
	description=""%>

<c:if test="${not empty value}">
	<c:if test="${empty injectionObjectMap[value]}">
		<c:forEach var="item" items="${injectionStatusEnum}">
			<c:if test="${(item.key eq 0)}">
				<span class="badge badge-info">${item.value}</span>
			</c:if>
		</c:forEach>
	</c:if>			
	<c:forEach var="t" items="${injectionObjectMap[value]}">
		<c:forEach var="item" items="${injectionStatusEnum}">
			<c:if test="${item.key eq t.injectionStatus && (item.key eq 0 || item.key eq 1 || item.key eq 5)}">
				<span class="badge badge-info">${injectionPlatformMap[t.platformId].name}${item.value}</span>
			</c:if>
			<c:if
				test="${item.key eq t.injectionStatus && (item.key eq 2 || item.key eq 6)}">
				<span class="badge badge-primary">${injectionPlatformMap[t.platformId].name}${item.value}</span>
			</c:if>
			<c:if
				test="${item.key eq t.injectionStatus && (item.key eq 3 || item.key eq 7)}">
				<span class="badge badge-success">${injectionPlatformMap[t.platformId].name}${item.value}</span>
			</c:if>
			<c:if
				test="${item.key eq t.injectionStatus && (item.key eq 4 || item.key eq 8)}">
				<span class="badge badge-danger">${injectionPlatformMap[t.platformId].name}${item.value}</span>
			</c:if>
		</c:forEach>
	</c:forEach>
</c:if>