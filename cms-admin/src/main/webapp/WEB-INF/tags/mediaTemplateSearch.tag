<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<label class="control-label">&#12288;&#12288;码率: <select
	name="search_templateId__INMASK_S"
	class="form-control input-small input-inline">
		<option value="">全部</option>
		<c:forEach var="mediaTemplate" items="${mediaTemplateList}">
			<option value="${mediaTemplate.id}"
				<c:if test="${! empty param.search_templateId__INMASK_S && mediaTemplate.id eq param.search_templateId__INMASK_S}">selected="selected"</c:if>>${mediaTemplate.title}</option>
		</c:forEach>
</select>
</label>