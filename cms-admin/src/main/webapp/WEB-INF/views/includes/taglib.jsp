<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="/WEB-INF/tlds/shiros.tld"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld"%>
<%@ taglib prefix="dic" uri="/WEB-INF/tlds/dic.tld"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="noImagePath" value="${ctx}/static/img/no-image.png" />
<c:set var="imageWebPath" value="${fns:getImageWebPath()}" />
<c:set var="currentSiteCode" value="${fns:getSiteCode()}" />