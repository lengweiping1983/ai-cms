<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="action" type="java.lang.String" required="false"
	description="分页请求地址"%>
<%@ attribute name="containerId" type="java.lang.String"
	required="false" description="容器Id"%>
<%@ attribute name="formId" type="java.lang.String" required="false"
	description="formId"%>
<%@ attribute name="levelId" type="java.lang.String"
	required="false" description="层Id"%>
<c:if test="${empty containerId}">
	<c:set var="containerId" value="page-content-wrapper" />
</c:if>
<c:if test="${empty levelId}">
	<c:set var="levelId" value="bottom" />
</c:if>	
<c:set var="idPrefix" value="${containerId}${levelId}" />

<div class="row">
	<div class="col-md-5" style="top:15px">
		<div class="dataTables_info" role="status" aria-live="polite">
			<input type="hidden" id="${idPrefix}totalPages"
				value="${page.totalPages}"></input> <input type="hidden"
				id="${idPrefix}currentPageIndex" value="${page.number}"></input>
			<input type="hidden" id="${idPrefix}currentPageOrderField"
				value="${param.order}"></input> <select
				class="form-control input-sm input-xsmall input-inline"
				id="${idPrefix}currentPageSize"
				onchange="$.Page.refreshFirstPage({containerId: '${containerId}',idPrefix: '${idPrefix}',formId: '${formId}'})">
				<option value="1"
					<c:if test="${page.size == 1}"> selected="selected" </c:if>>1</option>
				<option value="10"
					<c:if test="${page.size == 10}"> selected="selected" </c:if>>10</option>
				<option value="15"
					<c:if test="${page.size == 15}"> selected="selected" </c:if>>15</option>
				<option value="20"
					<c:if test="${page.size == 20}"> selected="selected" </c:if>>20</option>
				<option value="50"
					<c:if test="${page.size == 50}"> selected="selected" </c:if>>50</option>
				<option value="100"
					<c:if test="${page.size == 100}"> selected="selected" </c:if>>100</option>
			</select>
			<c:set var="totalPages" value="${page.totalPages}" />
			<c:if test="${page.totalPages eq 0}">
				<c:set var="totalPages" value="1" />
			</c:if>
			页码 : <span class="badge badge-success">${page.number+1}/${totalPages}</span>,
			每页<span class="badge badge-success">${page.size}</span>条, 共<span
				class="badge badge-success">${page.totalElements}</span>条记录.
		</div>
	</div>
	<div class="col-md-7">
		<div class="dataTables_paginate paging_bootstrap_number"
			style="float: right">
			<ul class="pagination" style="visibility: visible;">
				<c:choose>
					<c:when test="${page.first}">
						<li class="${idPrefix}left_double_prev prev disabled"><a title="第一页"
							href="javascript:;"> <i class="fa fa-angle-double-left"></i>
						</a></li>
					</c:when>
					<c:otherwise>
						<li class="${idPrefix}left_double_prev prev"><a title="第一页"
							href="javascript:;"> <i class="fa fa-angle-double-left"></i>
						</a></li>
					</c:otherwise>
				</c:choose>

				<c:choose>
					<c:when test="${page.first}">
						<li class="${idPrefix}left_prev prev disabled"><a title="上一页"
							href="javascript:;"> <i class="fa fa-angle-left"></i>
						</a></li>
					</c:when>
					<c:otherwise>
						<li class="${idPrefix}left_prev prev"><a title="上一页" href="javascript:;">
								<i class="fa fa-angle-left"></i>
						</a></li>
					</c:otherwise>
				</c:choose>

				<c:choose>
					<c:when test="${totalPages - page.number <= 2}">
						<c:set var="startPage" value="${totalPages - 5}" />
						<c:set var="endPage" value="${totalPages - 1}" />
					</c:when>
					<c:otherwise>
						<c:if test="${page.number >= 2}">
							<c:set var="startPage" value="${page.number - 2}" />
							<c:set var="endPage" value="${page.number + 2}" />
						</c:if>
						<c:if test="${page.number < 2}">
							<c:set var="startPage" value="0" />
							<c:set var="endPage" value="4" />
						</c:if>
					</c:otherwise>
				</c:choose>

				<c:if test="${totalPages <= 5}">
					<c:set var="startPage" value="0" />
					<c:set var="endPage" value="${totalPages - 1}" />
				</c:if>

				<c:forEach var="indexPage" begin="${startPage}" end="${endPage}"
					step="1">
					<c:choose>
						<c:when test="${indexPage == page.number}">
							<li id="${indexPage}" class="${idPrefix}index_page_number active"><a
								href="javascript:;">${indexPage + 1}</a></li>
						</c:when>
						<c:otherwise>
							<li id="${indexPage}" class="${idPrefix}index_page_number"><a
								href="javascript:;">${indexPage + 1}</a></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>

				<c:choose>
					<c:when test="${page.last}">
						<li class="${idPrefix}right_next next disabled"><a title="下一页"
							href="javascript:;"> <i class="fa fa-angle-right"></i>
						</a></li>
					</c:when>
					<c:otherwise>
						<li class="${idPrefix}right_next next"><a title="下一页"
							href="javascript:;"> <i class="fa fa-angle-right"></i>
						</a></li>
					</c:otherwise>
				</c:choose>

				<c:choose>
					<c:when test="${page.last}">
						<li class="${idPrefix}right_double_next next disabled"><a title="最后一页"
							href="javascript:;"> <i class="fa fa-angle-double-right"></i>
						</a></li>
					</c:when>
					<c:otherwise>
						<li class="${idPrefix}right_double_next next"><a title="最后一页"
							href="javascript:;"> <i class="fa fa-angle-double-right"></i>
						</a></li>
					</c:otherwise>
				</c:choose>
				<li>&nbsp;&nbsp;跳到<input id="${idPrefix}goPage" value="${page.number + 1}" class="input-text" style="width:40px; height:34px">页</li>
				<li style="float:right"><a class="btn btn-default ${idPrefix}go_page">GO</a></li>
			</ul>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$.Page.init({
			containerId: '${containerId}',
			idPrefix: '${idPrefix}',
			formId: '${formId}'
		});
	});
</script>


