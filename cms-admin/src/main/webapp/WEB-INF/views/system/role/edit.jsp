<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>

<link href="${ctx}/assets/global/css/plugins.css" rel="stylesheet" type="text/css" />

<div class="modal fade" id="role_edit_modal_div" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-titlCommonControllere">
                    <c:choose>
                        <c:when test="${empty role.id}">
                            <c:set var="methodDesc" value="增加"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="methodDesc" value="修改"/>
                        </c:otherwise>
                    </c:choose>
                    ${methodDesc}角色
                </h4>
            </div>
            <div class=" modal-body">
                <form id="role_edit_form">
                    <div class="content form-horizontal">
                        <tags:message content="${message}"/>
                        <div class="form-group">
                            <label class="control-label col-md-3">角色名称(<span class="required">*</span>):
                            </label>

                            <div class="col-md-8">
                                <input type="text" id="name" name="name" value="${role.name}"
                                       class="form-control validate[required]" placeholder="请输入角色名称">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">选择菜单:</label>

                            <div class="col-md-8">
                                <div class="portlet light bordered">
                                    <div class="portlet-body">
                                        <div id="menu_tree_div" class="tree-demo"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <c:choose>
                    <c:when test="${empty role.id}">
                        <button class="btn btn-outline green"
                                onclick="$.RoleController.edit('${ctx}/system/role/add');">
                            <i class="fa fa-save"></i>保存
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-outline green"
                                onclick="$.RoleController.edit('${ctx}/system/role/${role.id}/edit');">
                            <i class="fa fa-save"></i>保存
                        </button>
                    </c:otherwise>
                </c:choose>
                <button class="btn grey-salsa btn-outline" data-dismiss="modal" aria-hidden="true">
                    <i class="fa fa-close"></i>关闭
                </button>
            </div>
        </div>
    </div>
</div>