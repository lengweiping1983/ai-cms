<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>

<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->

<head>
    <meta charset="utf-8"/>
    <title>EPG</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1" name="viewport"/>
    <meta content="" name="description"/>
    <meta content="" name="author"/>
    <!-- BEGIN GLOBAL MANDATORY STYLES -->
    <link href="${ctx}/assets/global/plugins/googleapis/googleapis.css" rel="stylesheet" type="text/css" />
    <link href="${ctx}/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <link href="${ctx}/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
    <link href="${ctx}/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <link href="${ctx}/assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css" />
    <link href="${ctx}/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css" />
    <!-- END GLOBAL MANDATORY STYLES -->
    <!-- BEGIN THEME GLOBAL STYLES -->
    <link href="${ctx}/assets/global/css/components.css" rel="stylesheet" id="style_components" type="text/css" />
    <link href="${ctx}/assets/global/css/plugins.css" rel="stylesheet" type="text/css" />
    <!-- END THEME GLOBAL STYLES -->
    <!-- BEGIN THEME LAYOUT STYLES -->
    <link href="${ctx}/assets/layouts/layout/css/layout.css" rel="stylesheet" type="text/css" />
    <link href="${ctx}/assets/layouts/layout/css/themes/darkblue.min.css" rel="stylesheet" type="text/css" id="style_color" />
    <link href="${ctx}/assets/layouts/layout/css/custom.min.css" rel="stylesheet" type="text/css" />

    <!-- BEGIN PAGE LEVEL PLUGINS -->
    <link href="${ctx}/assets/global/plugins/bootstrap-daterangepicker/daterangepicker-bs3.css" rel="stylesheet"
          type="text/css"/>
    <link href="${ctx}/assets/global/plugins/morris/morris.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/assets/global/plugins/fullcalendar/fullcalendar.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/assets/global/plugins/jqvmap/jqvmap/jqvmap.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/assets/global/plugins/datatables/datatables.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.css" rel="stylesheet"
          type="text/css"/>

    <link href="${ctx}/static/plugins/jquery-treetable/css/jquery.treetable.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/static/plugins/jquery-treetable/css/jquery.treetable.theme.default.css" rel="stylesheet"
          type="text/css"/>
          
    <link href="${ctx}/assets/global/plugins/bootstrap-fileinput/bootstrap-fileinput.css" rel="stylesheet" type="text/css" />
    <link href="${ctx}/assets/global/plugins/bootstrap-editable/bootstrap-editable/css/bootstrap-editable.css" rel="stylesheet" type="text/css" />
    <link href="${ctx}/assets/global/plugins/bootstrap-wysihtml5/bootstrap-wysihtml5.css" rel="stylesheet" type="text/css" />
    <link href="${ctx}/assets/global/plugins/bootstrap-editable/inputs-ext/address/address.css" rel="stylesheet" type="text/css" />
    
    <link href="${ctx}/assets/pages/css/error.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/static/css/common.css" rel="stylesheet" type="text/css" />
    <!-- END PAGE LEVEL STYLES -->

    <link rel="shortcut icon" href="${ctx}/static/img/favicon.ico"/>
</head>
<!-- END HEAD -->

<body class="page-header-fixed page-sidebar-closed-hide-logo page-content-white">
<!-- BEGIN HEADER -->
<div id="page-header" class="page-header navbar navbar-fixed-top">
</div>
<!-- END HEADER -->

<!-- BEGIN HEADER & CONTENT DIVIDER -->
<div class="clearfix"></div>
<!-- END HEADER & CONTENT DIVIDER -->

<!-- BEGIN CONTAINER -->
<div class="page-container">
    <!-- BEGIN SIDEBAR -->
    <div id="page-sidebar-wrapper" class="page-sidebar-wrapper">
    </div>
    <!-- END SIDEBAR -->

    <tags:alertModal/>
    <tags:confirmModal/>

    <!-- BEGIN CONTENT -->
    <div id="page-content-wrapper" class="page-content-wrapper">
        <!-- BEGIN CONTENT BODY -->
        <div class="page-content">

        </div>
        <!-- END CONTENT BODY -->
    </div>

    <div id="confirm_modal_container">

    </div>

    <div id="alert_modal_container">

    </div>

    <!-- END CONTENT -->
</div>
<!-- END CONTAINER -->
<!-- BEGIN FOOTER -->
<div id="page-footer" class="page-footer">
</div>
<!-- END FOOTER -->
<!--[if lt IE 9]>
<script src="${ctx}/assets/global/plugins/respond.min.js"></script>
<script src="${ctx}/assets/global/plugins/excanvas.min.js"></script>
<![endif]-->

<!-- BEGIN CORE PLUGINS -->
<script src="${ctx}/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/js.cookie.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js"
        type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->

<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="${ctx}/assets/global/scripts/app.min.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->

<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="${ctx}/static/plugins/jquery-treetable/jquery.treetable.js" type="text/javascript"></script>

<script src="${ctx}/assets/global/plugins/bootstrap-fileinput/bootstrap-fileinput.js" type="text/javascript"></script>

<script src="${ctx}/assets/global/plugins/bootstrap-editable/bootstrap-editable/js/bootstrap-editable.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/bootstrap-editable/inputs-ext/address/address.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/bootstrap-editable/inputs-ext/wysihtml5/wysihtml5.js" type="text/javascript"></script>
        
<script src="${ctx}/assets/global/scripts/datatable.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/datatables/datatables.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.js"
        type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->

<%@ include file="/WEB-INF/views/includes/validate.jsp" %>
<%@ include file="/WEB-INF/views/includes/jslib.jsp"%>

<script type="text/javascript">
    var contextPath = "${ctx}";
    var imageWebPath = "${imageWebPath}";
    var noImagePath = "${noImagePath}";
    App.setAssetsPath(contextPath + "/assets/");

    $("#page-header").load("${ctx}/header", function () {
    });
    $("#page-sidebar-wrapper").load("${ctx}/leftMenu", function () {
    });
    $("#page-footer").load("${ctx}/footer", function () {
    });
    $.ajaxSetup({
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        global: true,
        statusCode: {
            911: function (XMLHttpRequest) {
                var json = {
                    body: "您的登录已超时, 请重新登录！",
                    event: "window.location = '${ctx}/login';"
                };
                $.common.showConfirmModal(json);
            }
        }
    });
</script>
</body>

</html>