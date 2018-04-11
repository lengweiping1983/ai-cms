<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="prefix" type="java.lang.String" required="false"
	description="id前缀"%>
<%@ attribute name="value" type="java.lang.Object" required="false"
	description="cpId的值"%>

<div class="form-group">
	<label class="control-label col-md-3">提供商: </label>

	<div class="col-md-9">
		<input type="hidden" id="${prefix}cpId" name="cpId" value="${value}" />
		<select class="form-control select2" id="${prefix}select_cpId"
			name="select_cpId" multiple="multiple">
			<c:forEach var="item" items="${cpList}">
				<c:set var="cpIdSelected" value="" />
				<c:forEach var="cpId" items="${value}">
					<c:if test="${item.id eq cpId}">
						<c:set var="cpIdSelected" value="1" />
					</c:if>
				</c:forEach>
				<option value="${item.id}"
					<c:if test="${cpIdSelected eq 1}">selected="selected"</c:if>>${item.name}</option>
			</c:forEach>
		</select>
	</div>
</div>
