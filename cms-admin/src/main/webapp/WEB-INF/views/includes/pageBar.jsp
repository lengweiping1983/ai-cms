<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>

<!-- BEGIN PAGE BAR -->
<div id="page-bar" class="page-bar">
</div>
<script type="text/javascript">
    var id = '<%=request.getParameter("navigation_menu_id")%>';
    $.ajax({
        type: "GET",
        url: "${ctx}/navigation/" + id,
        success: function (result) {
            $('#page-bar').html(result);
        },
        error: function () {

        }
    });
</script>
<!-- END PAGE BAR -->
