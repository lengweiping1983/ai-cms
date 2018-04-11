<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="value" type="java.lang.Object" required="false"
	description=""%>

<div class="form-group">
	<label class="control-label col-md-3">码率(<span class="required">*</span>):
	</label>

	<div class="col-md-9">
		<div class="checkbox-list">
			<c:forEach var="item" items="${mediaTemplateList}">
				<c:set var="mediaTemplateSelected" value="" />
				<c:forEach var="mediaTemplateId" items="${value}">
					<c:if test="${item.id eq mediaTemplateId}">
						<c:set var="mediaTemplateSelected" value="1" />
					</c:if>
				</c:forEach>
				<label><input name="templateId"
					<c:if test="${mediaTemplateSelected eq 1}"> checked </c:if>
					class="validate[required]" type="checkbox" value="${item.id}"><span
					class="badge badge-success">${item.title}</span>
<%-- 					[${item.vFormat}-${item.vBitrateMode}-${item.vCodec}-${item.vResolution}-${item.vFramerate}-${item.aCodec}-${item.aBitrate}] --%>
				</label>
			</c:forEach>
		</div>
	</div>
</div>
