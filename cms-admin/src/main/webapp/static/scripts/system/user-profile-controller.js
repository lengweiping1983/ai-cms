$(function () {
    $.UserProfileController = function (elm, config) {

    }

    $.extend($.UserProfileController, {

        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                    $.extend($.validationEngineLanguage.allRules, {
                        "ajaxLoginNameCheck": {
                            "url": contextPath + "/system/user/check?id=" + id,
                            "extraData": "dt=" + (new Date()).getTime(),
                            "alertText": "帐号不能使用!",
                            "alertTextLoad": "验证中，请稍候..."
                        }
                    });
                    $("#user_profile_edit_form").validationEngine();
                },
            });
        },

        edit: function (path) {
            if (!$("#user_profile_edit_form").validationEngine("validate")) return false;

            var json = $("#user_profile_edit_form").serializeObject();

            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: json,
                success: function (data) {
                    $.common.hideModal();
                    $.common.ajaxLoadContent({
                        url: contextPath + "/system/user/profile",
                        type: "GET",
                        dataType: "html",
                        contentType: "text/html",
                    });
                },
            })
        },

        loginName: function () {
            var $loginName = $("#loginName");
            $loginName.addClass("validate[required,minSize[1],maxSize[64],ajax[ajaxLoginNameCheck]]");
            $loginName.removeAttr("readonly");
            $loginName.removeAttr("onclick");
        },

        toEditPassword: function (path) {
            $.common.showModal({
                url: path,
                success: function () {
                    $("#user_profile_edit_password_form").validationEngine();
                },
                shown: function () {
                    $("#plainPassword")[0].focus();
                },
            });
        },

        editPassword: function (path) {
            if (!$("#user_profile_edit_password_form").validationEngine("validate")) return false;

            var plainPassword = $("#plainPassword").val();
            var password = $("#password").val();
            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: {plainPassword: plainPassword, password: password},
                success: function (data) {
                    $.common.hideModal();

                    var json = {
                        body: "您的密码修改成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },

    })

});