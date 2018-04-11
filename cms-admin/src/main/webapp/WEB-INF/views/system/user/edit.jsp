<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>

<link href="${ctx}/assets/global/css/plugins.css" rel="stylesheet" type="text/css"/>

<div class="modal fade" id="user_edit_modal_div" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-titlCommonControllere">
                    <c:choose>
                        <c:when test="${empty user.id}">
                            <c:set var="methodDesc" value="增加"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="methodDesc" value="修改"/>
                        </c:otherwise>
                    </c:choose>
                    ${methodDesc}用户
                </h4>
            </div>
            <div class="modal-body">
                <div class="content form-horizontal">
                    <div class="form-group">
                        <form id="user_edit_form">
                            <div class="col-md-7">
                                <tags:message content="${message}"/>
                                <div class="form-group">
                                    <label class="col-md-4 control-label">帐号(<span class="required">*</span>):</label>

                                    <div class="col-md-8">
                                        <c:if test="${empty user.id}">
                                            <input type="text" autocomplete="off" id="loginName" name="loginName"
                                                   class="form-control validate[required,minSize[1],maxSize[64],ajax[ajaxLoginNameCheck]]"
                                                   value="${user.loginName}"/>
                                        </c:if>
                                        <c:if test="${!empty user.id}">
                                            <input type="text" autocomplete="off" id="loginName" name="loginName"
                                                   class="form-control"
                                                   value="${user.loginName}" readonly="readonly"
                                                   onclick="$.UserController.loginName();"/>
                                        </c:if>
                                    </div>
                                </div>
                                <c:if test="${empty user.id}">
                                    <div class="form-group">
                                        <label class="control-label col-md-4" for="password">密码(<span
                                                class="required">*</span>):</label>

                                        <div class="col-md-8">
                                            <input type="password" autocomplete="off" name="password" id="password"
                                                   class="form-control validate[required,minSize[6],maxSize[32]]"
                                                   value=""/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-md-4" for="confirmPassword">确认密码(<span
                                                class="required">*</span>):</label>

                                        <div class="col-md-8">
                                            <input type="password" autocomplete="off"
                                                   class="form-control validate[required,minSize[6],maxSize[32],equals[password]]"
                                                   name="confirmPassword" id="confirmPassword"/>
                                        </div>
                                    </div>
                                </c:if>
                                <div class="form-group">
                                    <label class="col-md-4 control-label">姓名(<span class="required">*</span>):</label>

                                    <div class="col-md-8">
                                        <input type="text" name="name"
                                               class="form-control validate[required,minSize[1],maxSize[64]]"
                                               value="${user.name}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4 control-label">工号:</label>

                                    <div class="col-md-8">
                                        <input type="text" name="no" class="form-control" value="${user.no}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4 control-label">邮箱:</label>

                                    <div class="col-md-8">
                                        <input type="text" name="email" class="form-control validate[custom[email]]"
                                               value="${user.email}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4 control-label">固定电话:</label>

                                    <div class="col-md-8">
                                        <input type="text" name="phone" class="form-control validate[custom[phone]]"
                                               value="${user.phone}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4 control-label">移动电话:</label>

                                    <div class="col-md-8">
                                        <input type="text" name="mobile" class="form-control validate[custom[phone]]"
                                               value="${user.mobile}"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-md-4 control-label">状态:</label>

                                    <div class="col-md-8">
                                        <select name="status" id="status"
                                                class="form-control">
                                            <option value="1" <c:if
                                                    test="${user.status == 1}"> selected="selected" </c:if> >正常
                                            </option>
                                            <option value="0" <c:if
                                                    test="${user.status == 0}"> selected="selected" </c:if> >停用
                                            </option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </form>

                        <div class="col-md-5">
                            <div class="portlet light bordered">
                                <div class="portlet-body">
                                    <div class="form-group">
                                        <h5>选择角色:</h5>

                                        <div id="role_tree_div" class="tree-demo">

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <c:choose>
                    <c:when test="${empty user.id}">
                        <button class="btn btn-outline green"
                                onclick="$.UserController.edit('${ctx}/system/user/add');">
                            <i class="fa fa-save"></i>保存
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-outline green"
                                onclick="$.UserController.edit('${ctx}/system/user/${user.id}/edit');">
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