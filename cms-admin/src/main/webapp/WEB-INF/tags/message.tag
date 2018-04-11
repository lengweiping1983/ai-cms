<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="content" type="java.lang.String" required="true"
              description="消息内容" %>
<%@ attribute name="type" type="java.lang.String"
              description="消息类型：info、success、warning、danger" %>
<c:if test="${empty type}">
    <c:set var="type" value="danger"/>
</c:if>
<c:if test="${empty content}">
    <c:set var="display" value="display-hide"/>
</c:if>

<div class="alert alert-${type} ${display}">
    <button class="close" data-close="alert"></button>
    <span class="message-span-css"> ${content} </span>
</div>
