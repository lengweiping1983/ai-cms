<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
    <div class="row">
        <div class="col-md-12 page-404">
            <div class="number font-green">403</div>
            <div class="details">
                <h3>Oops, 没有权限!</h3>

                <p> 请确认您有访问当前页面的权限。
                    <br/>
                    <a href="${ctx}/">返回首页</a></p>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table class="table table-striped table-bordered table-hover order-column" id="error_list_table">
                <thead>
                <tr>
                    <th>错误类别</th>
                    <th>错误描述</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td><b>错误信息</b></td>
                    <td>${pageContext.exception}</td>
                </tr>
                <tr>
                    <td><b>错误URI</b></td>
                    <td>${pageContext.errorData.requestURI}</td>
                </tr>
                <tr>
                    <td><b>状&nbsp;态&nbsp;码</b></td>
                    <td>${pageContext.errorData.statusCode}</td>
                </tr>
                <tr>
                    <td><b>错误详情</b></td>
                    <td>
                        <c:forEach var="trace" items="${pageContext.exception.stackTrace}">
                            ${trace}<br>
                        </c:forEach>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<!-- END CONTENT BODY -->

<script src="${ctx}/static/scripts/common/error-page-scroller.js" type="text/javascript"></script>