<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>

<div class="modal fade" id="user_edit_modal_div" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-titlCommonControllere">
                    修改密码
                </h4>
            </div>
            <div class="modal-body">
                <div class="content form-horizontal">
                    <form id="user_edit_password_form">
                        <tags:message content="${message}"/>
                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="password">新密码(<span
                                    class="required">*</span>):</label>

                            <div class="col-md-7">
                                <div class="input_tip">
                                    <input type="password"
                                           class="form-control validate[required,minSize[6],maxSize[32]]"
                                           name="password" id="password" placeholder="新密码"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="confirmPassword">确认密码(<span
                                    class="required">*</span>):</label>

                            <div class="col-md-7">
                                <div class="input_tip">
                                    <input type="password"
                                           class="form-control validate[required,equals[password]]"
                                           name="confirmPassword" id="confirmPassword" placeholder="确认密码"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-outline green"
                        onclick="$.UserController.editPassword('${ctx}/system/user/${user.id}/editPassword');">
                    <i class="fa fa-save"></i>保存
                </button>
                <button class="btn grey-salsa btn-outline" data-dismiss="modal" aria-hidden="true">
                    <i class="fa fa-close"></i>关闭
                </button>
            </div>
        </div>
    </div>
</div>