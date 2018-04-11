$(function () {
    $.UserController = function (elm, config) {

    }

    $.extend($.UserController, {

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

                    $("#user_edit_form").validationEngine({
                        ajaxFormValidationMethod: 'post',
                    });

                    $.UserController.showTree(id);
                },
            });

        },

        edit: function (path) {
            var validationResult = $("#user_edit_form").validationEngine("validate");
            if (!validationResult) return false;

            var user = $("#user_edit_form").serializeObject();
            var roleIds = $("#role_tree_div").jstree("get_selected");
            
            var bean = {};
            bean.data = user;
            bean.ids = roleIds;

            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: bean,
                success: function (data) {
                    $.common.hideModal();
                    $.Page.refreshCurrentPage();
                },
            })
        },

        loginName: function () {
            var $loginName = $("#loginName");
            $loginName.addClass("validate[required,minSize[1],maxSize[64],ajax[ajaxLoginNameCheck]]");
            $loginName.removeAttr("readonly");
            $loginName.removeAttr("onclick");
            $loginName[0].focus();
            $.common.focus({id: "loginName"});
        },


        toDelete: function (path, name) {
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除[" + name + "]吗?";
            json.event = "$.UserController._delete('" + path + "')";
            $.common.showConfirmModal(json);
        },

        _delete: function (path) {
            $.common.ajaxActionText(path, function (data) {
                $.common.hideConfirmModal();
                $.Page.refreshCurrentPage();
            });
        },

        toEditPassword: function (path) {
            $.common.showModal({
                url: path,
                success: function () {
                    $("#user_edit_password_form").validationEngine();
                },
                shown: function () {
                    $("#password")[0].focus();
                }
            });
        },

        editPassword: function (path) {
            if (!$("#user_edit_password_form").validationEngine("validate")) return false;

            var password = $("#password").val();

            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: {password: password},
                success: function (data) {
                    $.common.hideModal();
                }
            });
        },

        showTree: function (userId) {
            $.ajax({
                url: contextPath + '/system/user/roles',
                type: 'GET',
                dataType: 'json',
                contextType: "application/json",
                data: {userId: userId},
                success: function (data) {
                    $('#role_tree_div').jstree({
                        'plugins': ["wholerow", "checkbox", "types"],
                        'core': {
                            "themes": {
                                "responsive": false
                            },
                            'data': data
                        },
                        "types": {
                            "default": {
                                "icon": "fa fa-folder icon-state-warning icon-lg"
                            },
                            "file": {
                                "icon": "fa fa-file icon-state-warning icon-lg"
                            }
                        }
                    });
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert('加载失败！');
                },
            });
        },

    })

});