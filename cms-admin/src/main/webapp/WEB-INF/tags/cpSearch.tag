<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="prefix" type="java.lang.String" required="false"
	description="id前缀"%>

<label class="control-label">&#12288;提供商: <input type="hidden"
	id="${prefix}search_cpId__INMASK_S" name="search_cpId__INMASK_S"
	value="${param.search_cpId__INMASK_S}" /> <select
	id="${prefix}select_cpId__INMASK_S" name="select_cpId__INMASK_S"
	multiple="multiple">
		<c:forEach var="item" items="${cpList}">
			<c:set var="cpIdSelected" value="" />
			<c:forEach var="cpId" items="${param.search_cpId__INMASK_S}">
				<c:if test="${item.id eq cpId}">
					<c:set var="cpIdSelected" value="1" />
				</c:if>
			</c:forEach>
			<option value="${item.id}"
				<c:if test="${cpIdSelected eq 1}">selected="selected"</c:if>>${item.name}</option>
		</c:forEach>
</select>
</label>