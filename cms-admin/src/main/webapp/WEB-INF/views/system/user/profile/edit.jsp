<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>

<div class="modal fade" id="user_profile_edit_modal_div" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-titlCommonControllere">
                    修改个人信息
                </h4>
            </div>
            <div class="modal-body">
                <div class="content form-horizontal">
                    <div class="form-group">
                        <form id="user_profile_edit_form">
                            <div class="col-md-10">
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
                                                   onclick="$.UserProfileController.loginName();"/>
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
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-outline green"
                        onclick="$.UserProfileController.edit('${ctx}/system/user/profile/edit');">
                    <i class="fa fa-save"></i>保存
                </button>
                <button class="btn grey-salsa btn-outline" data-dismiss="modal" aria-hidden="true">
                    <i class="fa fa-close"></i>关闭
                </button>
            </div>
        </div>
    </div>
</div>