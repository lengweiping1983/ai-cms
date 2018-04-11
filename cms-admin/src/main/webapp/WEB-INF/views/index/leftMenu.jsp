<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>

<!-- BEGIN SIDEBAR -->
<!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
<!-- DOC: Change data-auto-speed="200" to adjust the sub menu slide up/down speed -->
<div class="page-sidebar navbar-collapse collapse">
    <!-- BEGIN SIDEBAR MENU -->
    <!-- DOC: Apply "page-sidebar-menu-light" class right after "page-sidebar-menu" to enable light sidebar menu style(without borders) -->
    <!-- DOC: Apply "page-sidebar-menu-hover-submenu" class right after "page-sidebar-menu" to enable hoverable(hover vs accordion) sub menu mode -->
    <!-- DOC: Apply "page-sidebar-menu-closed" class right after "page-sidebar-menu" to collapse("page-sidebar-closed" class must be applied to the body element) the sidebar sub menu mode -->
    <!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
    <!-- DOC: Set data-keep-expand="true" to keep the submenues expanded -->
    <!-- DOC: Set data-auto-speed="200" to adjust the sub menu slide up/down speed -->
    <ul id="leftMenu" class="page-sidebar-menu  page-header-fixed " data-keep-expanded="false" data-auto-scroll="true"
        data-slide-speed="200" style="padding-top: 20px">
        <c:forEach items="${menuList}" var="menu1">
            <li class="nav-item ">
                <a href="javascript:;" class="nav-link nav-toggle">
                    <c:set var="menuIcon" value="${menu1.icon}"/>
                    <c:if test="${empty menuIcon}">
                        <c:set var="menuIcon" value="glyphicon glyphicon-th-large"/>
                    </c:if>
                    <i class="${menuIcon}"></i>
                    <span class="title">${menu1.name}</span>
                    <span class=""></span>
                    <span class="arrow"></span>
                </a>
                <c:if test="${menu1.childList != null && fn:length(menu1.childList) > 0}">
                    <ul class="sub-menu">
                        <c:forEach items="${menu1.childList}" var="menu2">
                            <c:choose>
                                <c:when test="${menu2.childList != null && fn:length(menu2.childList) > 0}">
                                    <li class="nav-item ">
                                        <a href="javascript:;" class="nav-link nav-toggle">
                                            <c:set var="menuIcon" value="${menu2.icon}"/>
                                            <c:if test="${empty menuIcon}">
                                                <c:set var="menuIcon" value="glyphicon glyphicon-th-large"/>
                                            </c:if>
                                            <i class="${menuIcon}"></i>
                                            <span class="title">${menu2.name}</span>
                                            <span class=""></span>
                                            <span class="arrow"></span>
                                        </a>
                                        <ul class="sub-menu">
                                            <c:forEach items="${menu2.childList}" var="menu3">
                                                <c:if test="${menu3.type == 1}">
                                                    <li class="nav-item click-item">
                                                        <a href="javascript:;" data-url="${menu3.requestUrl}"
                                                           class="nav-link click-open-url">
                                                            <c:set var="menuIcon" value="${menu3.icon}"/>
                                                            <c:if test="${empty menuIcon}">
                                                                <c:set var="menuIcon"
                                                                       value="iglyphicon glyphicon-th-large"/>
                                                            </c:if>
                                                            <i class="${menuIcon}"></i>
                                                            <span class="title">${menu3.name}</span>
                                                        </a>
                                                    </li>
                                                </c:if>
                                            </c:forEach>
                                        </ul>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${menu2.type == 1}">
                                        <li class="nav-item click-item">
                                            <a href="javascript:;" data-url="${menu2.requestUrl}"
                                               class="nav-link click-open-url">
                                                <c:set var="menuIcon" value="${menu2.icon}"/>
                                                <c:if test="${empty menuIcon}">
                                                    <c:set var="menuIcon" value="glyphicon glyphicon-th-large"/>
                                                </c:if>
                                                <i class="${menuIcon}"></i>
                                                <span class="title">${menu2.name}</span>
                                            </a>
                                        </li>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </ul>
                </c:if>
            </li>
        </c:forEach>
    </ul>
    <!-- END SIDEBAR MENU -->
</div>
<!-- END SIDEBAR -->

<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="${ctx}/assets/layouts/layout/scripts/layout.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/layouts/layout/scripts/demo.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/layouts/global/scripts/quick-sidebar.min.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->

<script type="text/javascript">
    $('ul#leftMenu .click-open-url').click(function () {
        var _this = $(this);
        var url = _this.data('url');
        if (url.substring(0, 1) != "/") {
            url = "/" + url;
        }

        $("ul#leftMenu li").removeClass("active open");
        $(this).parent().addClass("active open");

        var _parent_li = $(this).parent().parent().parent();
        $(_parent_li).addClass("active open");
        $($($(_parent_li).children()[0]).children()[2]).addClass("selected");
        $($($(_parent_li).children()[0]).children()[3]).addClass("open");

        //var _parent_parent_li = $(_parent_li).parent().parent();
        //$(_parent_parent_li).addClass("active open");
        //$($($(_parent_parent_li).children()[0]).children()[2]).addClass("selected");
        //$($($(_parent_parent_li).children()[0]).children()[3]).addClass("open");

        $.common.ajaxLoadContent({
            url: contextPath + url,
            type: "GET",
            dataType: "text",
            contentType: "default",
        });
    });
    //$('ul#leftMenu .click-item a:first').click();
</script>