<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="value" type="java.lang.String" required="false"
	description="值"%>
<%@ attribute name="enumList" type="java.lang.Object" required="true"
	description="枚举列表"%>
<%@ attribute name="className" type="java.lang.String" required="false"
	description="样式"%>	

<c:if test="${not empty value}">
	<c:forEach var="item" items="${enumList}">
		<c:if test="${item.key eq value}">
			<span ${className}>${item.value}</span>
		</c:if>
	</c:forEach>
</c:if>