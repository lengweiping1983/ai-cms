<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>

<script src="${ctx}/static/plugins/jquery-treetable/jquery.treetable.js" type="text/javascript"></script>

<script src="${ctx}/static/scripts/system/menu-tree-list.js" type="text/javascript"></script>
<script src="${ctx}/static/scripts/system/menu-controller.js" type="text/javascript"></script>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
    <div class="row">
        <div class="col-md-12">
            <!-- BEGIN EXAMPLE TABLE PORTLET-->
            <div class="portlet box green">
                <div class="portlet-title">
                    <div class="caption">
                        <i class="fa fa-globe"></i>菜单管理
                    </div>
                    <div class="actions">
                        <shiro:hasPermission name="system:menu:add">
                            <a onclick="$.MenuController.toEdit('${ctx}/system/menu/add/1');" href="javascript:;"
                               class="btn btn-default btn-sm">
                                <i class="fa fa-plus"></i>增加菜单
                            </a>

                            <a onclick="$.MenuController.toEdit('${ctx}/system/menu/add/2');" href="javascript:;"
                               class="btn btn-default btn-sm">
                                <i class="fa fa-plus"></i>增加权限
                            </a>
                        </shiro:hasPermission>
                    </div>
                </div>
                <div class="portlet-body">
                    <table class="table table-striped table-bordered table-hover table-header-fixed" id="menu_list_id">
                        <thead>
                        <tr>
                            <th width="20%">菜单名称</th>
                            <th width="10%">菜单类型</th>
                            <th width="20%">菜单链接</th>
                            <th width="15%">权限标识</th>
                            <th width="10%">排序值</th>
                            <shiro:hasAnyPermissions name="system:menu:edit,system:menu:delete">
                                <th width="15%">操作</th>
                            </shiro:hasAnyPermissions>
                        </tr>
                        </thead>
                        <tfoot>
                        <tr>
                            <th>菜单名称</th>
                            <th>菜单类型</th>
                            <th>菜单链接</th>
                            <th>权限标识</th>
                            <th>排序值</th>
                            <shiro:hasAnyPermissions name="system:menu:edit,system:menu:delete">
                                <th>操作</th>
                            </shiro:hasAnyPermissions>
                        </tr>
                        </tfoot>
                        <tbody>
                        <c:forEach items="${menuList}" var="menu">
                            <tr class="odd gradeX" id="${menu.id}" data-tt-id="${menu.id}"
                                data-tt-parent-id="${menu.parent.id}">
                                <td>
                                    <c:if test="${menu.type == 1}">
                                        <c:set var="menuIcon" value="${menu.icon}"/>
                                        <c:if test="${empty menuIcon}">
                                            <c:set var="menuIcon" value="glyphicon glyphicon-th-large"/>
                                        </c:if>
                                        <i class="${menuIcon}"></i>
                                    </c:if>
                                        ${menu.name}
                                </td>
                                <td>${menu.type == 1 ? '菜单' : '权限'}</td>
                                <td>${menu.href}</td>
                                <td>${menu.permission}</td>
                                <td>${menu.sort}</td>
                                <shiro:hasAnyPermissions name="system:menu:edit,system:menu:delete">
                                    <td>
                                        <shiro:hasPermission name="system:menu:edit">
                                            <button class="btn btn-default btn-sm btn-outline green"
                                                    onclick="$.MenuController.toEdit('${ctx}/system/menu/${menu.id}/edit');">
                                                <i class="fa fa-edit"></i>修改
                                            </button>
                                        </shiro:hasPermission>

                                        <shiro:hasPermission name="system:menu:delete">
                                            <button class="btn btn-default btn-sm btn-outline green"
                                                    onclick="$.MenuController.toDelete('${menu.id}','${menu.name}','${menu.type}');">
                                                <i class="fa fa-remove"></i>删除
                                            </button>
                                        </shiro:hasPermission>
                                    </td>
                                </shiro:hasAnyPermissions>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <!-- END EXAMPLE TABLE PORTLET-->
        </div>
    </div>
</div>
<!-- END CONTENT BODY -->

<tags:contentModal/>

<div class="modal fade bs-modal-sm" id="menu_tree_modal_div" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择所属菜单</h4>
            </div>
            <div class="modal-body">
                <div id="menu_tree_div" class="tree-demo"></div>
            </div>
            <div class="modal-footer">
                <button class="btn grey-salsa btn-outline" data-dismiss="modal" aria-hidden="true">
                    <i class="fa fa-close"></i>关闭
                </button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="menu_icon_modal_div" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="width:650px">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择图标</h4>
            </div>
            <div class="modal-body" style="width:645px;height:450px;border:0px solid red;overflow-y:scroll">
                <jsp:include page="icons.jsp"/>
            </div>
            <div class="modal-footer">
                <button class="btn grey-salsa btn-outline" data-dismiss="modal" aria-hidden="true">
                    <i class="fa fa-close"></i>关闭
                </button>
            </div>
        </div>
    </div>
</div>