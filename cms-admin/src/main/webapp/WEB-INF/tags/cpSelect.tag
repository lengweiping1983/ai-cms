<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="prefix" type="java.lang.String" required="false"
	description="id前缀"%>
<%@ attribute name="value" type="java.lang.Object" required="false"
	description="cpCode的值"%>

<div class="form-group">
	<label class="control-label col-md-3">提供商: </label>

	<div class="col-md-9">
		<input type="hidden" id="${prefix}cpCode" name="cpCode" value="${value}" />
		<select class="form-control select2" id="${prefix}select_cpCode"
			name="select_cpCode" multiple="multiple">
			<c:forEach var="item" items="${cpList}">
				<c:set var="cpCodeSelected" value="" />
				<c:forEach var="cpCode" items="${value}">
					<c:if test="${item.code eq cpCode}">
						<c:set var="cpCodeSelected" value="1" />
					</c:if>
				</c:forEach>
				<option value="${item.code}"
					<c:if test="${cpCodeSelected eq 1}">selected="selected"</c:if>>${item.name}</option>
			</c:forEach>
		</select>
	</div>
</div>
