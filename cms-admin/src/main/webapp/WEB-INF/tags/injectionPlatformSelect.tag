<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="value" type="java.lang.Object" required="false"
	description=""%>

<div class="form-group">
	<label class="control-label col-md-3">注入平台(<span
		class="required">*</span>):
	</label>

	<div class="col-md-9">
		<div class="checkbox-list">
			<c:forEach var="item" items="${injectionPlatformList}">
				<c:set var="injectionPlatformSelected" value="" />
				<c:forEach var="injectionPlatformId" items="${value}">
					<c:if test="${item.id eq injectionPlatformId}">
						<c:set var="injectionPlatformSelected" value="1" />
					</c:if>
				</c:forEach>
				<label><input name="platformId"
					<c:if test="${injectionPlatformSelected eq 1}"> checked </c:if>
					class="validate[required]" type="checkbox" value="${item.id}">${item.name}
				</label>
			</c:forEach>
		</div>
	</div>
</div>
