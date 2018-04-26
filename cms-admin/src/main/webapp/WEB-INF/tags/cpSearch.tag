<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="prefix" type="java.lang.String" required="false"
	description="id前缀"%>

<label class="control-label">&#12288;提供商: <input type="hidden"
	id="${prefix}search_cpCode__INMASK_S" name="search_cpCode__INMASK_S"
	value="${param.search_cpCode__INMASK_S}" /> <select
	id="${prefix}select_cpCode__INMASK_S" name="select_cpCode__INMASK_S"
	multiple="multiple">
		<c:forEach var="item" items="${cpList}">
			<c:set var="cpCodeSelected" value="" />
			<c:forEach var="cpCode" items="${param.search_cpCode__INMASK_S}">
				<c:if test="${item.code eq cpCode}">
					<c:set var="cpCodeSelected" value="1" />
				</c:if>
			</c:forEach>
			<option value="${item.code}"
				<c:if test="${cpCodeSelected eq 1}">selected="selected"</c:if>>${item.name}</option>
		</c:forEach>
</select>
</label>