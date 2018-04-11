<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>


<!-- BEGIN HEADER INNER -->
<div class="page-header-inner ">
    <!-- BEGIN LOGO -->
    <div class="page-logo">
        <a href="javascript:;">
            <img src="${ctx}/static/img/logo.png" alt="CMS" class="logo-default"/>
        </a>

        <div class="menu-toggler sidebar-toggler"></div>
    </div>
    <!-- END LOGO -->

    <!-- BEGIN RESPONSIVE MENU TOGGLER -->
    <a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse"
       data-target=".navbar-collapse"> </a>
    <!-- END RESPONSIVE MENU TOGGLER -->

    <!-- BEGIN TOP NAVIGATION MENU -->
    <div class="top-menu">
        <ul class="nav navbar-nav pull-right">
            <!-- BEGIN USER LOGIN DROPDOWN -->
            <!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
            <li class="dropdown dropdown-user">
                <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown"
                   data-close-others="true">
                    <!--<img alt="" class="img-circle" src="${ctx}/assets/layouts/layout/img/avatar3_small.jpg" />-->
                    <span class="username username-hide-on-mobile"> 欢迎您：${user.name} </span>
                    <i class="fa fa-angle-down"></i>
                </a>
                <ul class="dropdown-menu dropdown-menu-default">
                    <li>
                        <a id="index-profile" href="javascript:;">
                            <i class="icon-user"></i> 个人信息 </a>
                    </li>
                    <li>
                        <a id="index-editPassword" href="javascript:;">
                            <i class="fa fa-key"></i> 修改密码 </a>
                    </li>
                    <li class="divider"></li>
                    <!--
                                <li>
                                    <a href="${ctx}/lock"><i class="icon-lock"></i> 锁屏 </a>
                                </li>
                                -->
                    <li>
                        <a href="${ctx}/logout">
                            <i class="fa fa-sign-out"></i> 退出 </a>
                    </li>
                </ul>
            </li>
            <!-- END USER LOGIN DROPDOWN -->
            <!-- BEGIN QUICK SIDEBAR TOGGLER -->
            <!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
            <li class="dropdown dropdown-quick-sidebar-toggler">
                <a href="${ctx}/logout" class="dropdown-toggle">
                    <i class="icon-logout"></i>
                </a>
            </li>
            <!-- END QUICK SIDEBAR TOGGLER -->
        </ul>
    </div>
    <!-- END TOP NAVIGATION MENU -->
</div>
<!-- END HEADER INNER -->
<script src="${ctx}/static/scripts/system/user-profile-controller.js" type="text/javascript"></script>


<script type="text/javascript">
    $("#index-profile").click(function () {
        $.common.ajaxLoadContent({
            url: contextPath + "/system/user/profile",
            type: "GET",
            dataType: "text",
            contentType: "default",
        });
    });

    $("#index-editPassword").click(function () {
        $.UserProfileController.toEditPassword(contextPath + "/system/user/profile/editPassword");
    });

    $("#index-profile").click();
</script>
