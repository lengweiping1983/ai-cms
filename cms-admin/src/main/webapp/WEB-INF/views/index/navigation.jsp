<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>
		
                    <!-- BEGIN PAGE BAR -->
<!--                     <div class="page-bar"> -->
                        <ul class="page-breadcrumb">
                        	<li>
                                <a href="${ctx}/">首页</a>
                            </li>
                        	<c:forEach items="${menuList}" var="menu">
                            <li>
                            	<i class="fa fa-circle"></i>
                            	<span>${menu.name}</span>
                            </li>
                            </c:forEach>
                        </ul>
<!--                     </div> -->
                    <!-- END PAGE BAR -->
