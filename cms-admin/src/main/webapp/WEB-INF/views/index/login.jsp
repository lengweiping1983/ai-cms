<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->

<head>
<meta charset="utf-8" />
<title>登录</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />

<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link
	href="${ctx}/assets/global/plugins/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="${ctx}/assets/global/plugins/simple-line-icons/simple-line-icons.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="${ctx}/assets/global/plugins/bootstrap/css/bootstrap.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="${ctx}/assets/global/plugins/uniform/css/uniform.default.css"
	rel="stylesheet" type="text/css" />
<link
	href="${ctx}/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css"
	rel="stylesheet" type="text/css" />
<!-- END GLOBAL MANDATORY STYLES -->

<!-- BEGIN PAGE LEVEL PLUGINS -->
<link href="${ctx}/assets/global/plugins/select2/css/select2.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="${ctx}/assets/global/plugins/select2/css/select2-bootstrap.min.css"
	rel="stylesheet" type="text/css" />
<!-- END PAGE LEVEL PLUGINS -->

<!-- BEGIN THEME GLOBAL STYLES -->
<link href="${ctx}/assets/global/css/components.min.css"
	rel="stylesheet" id="style_components" type="text/css" />
<link href="${ctx}/assets/global/css/plugins.css" rel="stylesheet"
	type="text/css" />
<!-- END THEME GLOBAL STYLES -->

<!-- BEGIN PAGE LEVEL STYLES -->
<link href="${ctx}/assets/pages/css/login-3.min.css" rel="stylesheet"
	type="text/css" />
<!-- END PAGE LEVEL STYLES -->

<!-- BEGIN THEME LAYOUT STYLES -->
<!-- END THEME LAYOUT STYLES -->
<link rel="shortcut icon" href="${ctx}/static/img/favicon.ico" />
</head>
<!-- END HEAD -->

<body class="login">
	<!-- BEGIN LOGO -->
	<div class="logo">
		<a href="javascript:;"> <img src="${ctx}/static/img/logo-big.png"
			alt="CMS" />
		</a>
	</div>
	<!-- END LOGO -->
	<!-- BEGIN LOGIN -->
	<div class="content">
		<!-- BEGIN LOGIN FORM -->
		<form class="login-form" action="${ctx}/login" method="post">
			<h3 class="form-title">系统登录</h3>
			<tags:message content="${message}" />
			<div class="form-group">
				<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
				<label class="control-label visible-ie8 visible-ie9">帐号:</label>

				<div class="input-icon">
					<i class="fa fa-user"></i> <input
						class="form-control placeholder-no-fix" type="text"
						autocomplete="off" placeholder="请输入帐号" name="loginName"
						id="loginName" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label visible-ie8 visible-ie9">密码:</label>

				<div class="input-icon">
					<i class="fa fa-lock"></i> <input
						class="form-control placeholder-no-fix" type="password"
						autocomplete="off" placeholder="请输入密码" name="password" />
				</div>
			</div>
			<c:if test="${verificationFlag}">
				<div class="form-group">
					<label class="control-label visible-ie8 visible-ie9">验证码:</label>
					<div class="form-inline">
						<div class="form-inline input-icon">
							<i class="fa fa-image"></i> <input
								class="form-control placeholder-no-fix" type="text"
								autocomplete="off" placeholder="请输入验证码" name="vrifyCode" /> <img
								alt="验证码" title="点击刷新验证码" src="${ctx}/kaptcha/defaultKaptcha"
								onclick="this.src='${ctx}/kaptcha/defaultKaptcha?d='+new Date()*1" />
						</div>
					</div>
				</div>
			</c:if>
			<div class="form-actions">
				<label class="checkbox"> <input type="checkbox"
					name="rememberMe" value="true" /> 记住我
				</label>
				<button type="submit" class="btn green pull-right">
					<i class="icon-login"></i>登录
				</button>
			</div>

		</form>
		<!-- END LOGIN FORM -->
	</div>
	<!-- END LOGIN -->
	<!-- BEGIN COPYRIGHT -->
	<div class="copyright">2018 &copy; AI智能运营.</div>
	<!-- END COPYRIGHT -->
	<!--[if lt IE 9]>
<script src="${ctx}/assets/global/plugins/respond.min.js"></script>
<script src="${ctx}/assets/global/plugins/excanvas.min.js"></script>
<![endif]-->
	<!-- BEGIN CORE PLUGINS -->
	<script src="${ctx}/assets/global/plugins/jquery.min.js"
		type="text/javascript"></script>
	<script
		src="${ctx}/assets/global/plugins/bootstrap/js/bootstrap.min.js"
		type="text/javascript"></script>
	<script src="${ctx}/assets/global/plugins/js.cookie.min.js"
		type="text/javascript"></script>
	<script
		src="${ctx}/assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js"
		type="text/javascript"></script>
	<script
		src="${ctx}/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js"
		type="text/javascript"></script>
	<script src="${ctx}/assets/global/plugins/jquery.blockui.min.js"
		type="text/javascript"></script>
	<script
		src="${ctx}/assets/global/plugins/uniform/jquery.uniform.min.js"
		type="text/javascript"></script>
	<script
		src="${ctx}/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js"
		type="text/javascript"></script>
	<!-- END CORE PLUGINS -->

	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<script
		src="${ctx}/assets/global/plugins/jquery-validation/js/jquery.validate.min.js"
		type="text/javascript"></script>
	<script
		src="${ctx}/assets/global/plugins/jquery-validation/js/additional-methods.min.js"
		type="text/javascript"></script>
	<script
		src="${ctx}/assets/global/plugins/select2/js/select2.full.min.js"
		type="text/javascript"></script>

	<!-- END PAGE LEVEL PLUGINS -->

	<!-- BEGIN THEME GLOBAL SCRIPTS -->
	<script src="${ctx}/assets/global/scripts/app.min.js"
		type="text/javascript"></script>
	<!-- END THEME GLOBAL SCRIPTS -->

	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<script src="${ctx}/static/scripts/common/login.js"
		type="text/javascript"></script>
	<!-- END PAGE LEVEL SCRIPTS -->

	<!-- BEGIN THEME LAYOUT SCRIPTS -->
	<!-- END THEME LAYOUT SCRIPTS -->
</body>

</html>