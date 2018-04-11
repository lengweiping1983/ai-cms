<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>

<script src="${ctx}/static/scripts/system/user-profile-controller.js" type="text/javascript"></script>

<!-- BEGIN CONTENT BODY -->
<div class="page-content">
    <div class="row">
        <div class="col-md-12">
            <div class="portlet light bordered">
                <div class="portlet-body form">
                    <div class="form-body">
                        <h2 class="margin-bottom-20"> 个人信息 -
                            <span class="label label-success">${user.name}</span>
                        </h2>

                        <h3 class="form-section">基本信息</h3>

                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-4">帐号:</label>

                                    <div class="col-md-8">
                                        <p class="form-control-static">${user.loginName}</p>
                                    </div>
                                </div>
                            </div>
                            <!--/span-->
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-4">姓名:</label>

                                    <div class="col-md-8">
                                        <p class="form-control-static">${user.name}</p>
                                    </div>
                                </div>
                            </div>
                            <!--/span-->
                        </div>
                        <!--/row-->
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-4">工号:</label>

                                    <div class="col-md-8">
                                        <p class="form-control-static">${user.no}</p>
                                    </div>
                                </div>
                            </div>
                            <!--/span-->
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-4">邮箱:</label>

                                    <div class="col-md-8">
                                        <p class="form-control-static">${user.email}</p>
                                    </div>
                                </div>
                            </div>
                            <!--/span-->
                        </div>
                        <!--/row-->
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-4">固定电话:</label>

                                    <div class="col-md-8">
                                        <p class="form-control-static">${user.phone}</p>
                                    </div>
                                </div>
                            </div>
                            <!--/span-->
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-4">移动电话:</label>

                                    <div class="col-md-8">
                                        <p class="form-control-static">${user.mobile}</p>
                                    </div>
                                </div>
                            </div>
                            <!--/span-->
                        </div>
                        <!--/row-->
                        <h3 class="form-section">登录信息</h3>

                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-4">登录次数:</label>

                                    <div class="col-md-8">
                                        <p class="form-control-static"><span
                                                class="badge badge-success">${user.loginNum}</span></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-4">最后登录时间:</label>

                                    <div class="col-md-8">
                                        <p class="form-control-static"><fmt:formatDate value="${user.loginTime}"
                                                                                       pattern="yyyy-MM-dd HH:mm:ss"/></p>
                                    </div>
                                </div>
                            </div>
                            <!--/span-->
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-4">最后登录IP:</label>

                                    <div class="col-md-8">
                                        <p class="form-control-static">${user.loginIp}</p>
                                    </div>
                                </div>
                            </div>
                            <!--/span-->
                        </div>
                    </div>
                    <div class="form-actions">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="row">
                                    <div class="col-md-offset-3 col-md-8">
                                        <button class="btn btn-outline green"
                                                onclick="$.UserProfileController.toEdit('${ctx}/system/user/profile/edit','${user.id}');">
                                            <i class="fa fa-key"></i>修改个人信息
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- END CONTENT BODY -->

<tags:contentModal/>