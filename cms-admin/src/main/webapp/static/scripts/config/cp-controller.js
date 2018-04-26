$(function () {
    $.CpController = function (elm, config) {

    }

    $.extend(true, $.CpController, $.BaseController, {

        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                    $.extend($.validationEngineLanguage.allRules, {
                        "ajaxCodeCheck": {
                            "url": contextPath + "/config/cp/check?id=" + id,
                            "extraData": "dt=" + (new Date()).getTime(),
                            "alertText": "代码不能使用!",
                            "alertTextLoad": "验证中，请稍候..."
                        }
                    });
                    $("#editForm").validationEngine({
                        ajaxFormValidationMethod: 'post',
                    });
                },
            });
        },

        edit: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();

            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: json,
                success: function (data) {
                    $.common.hideModal();
                    $.Page.refreshCurrentPage();
                }
            })
        },

        toDelete: function (path, name) {
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除[" + name + "]吗?";
            json.event = "$.CpController._delete('" + path + "')";
            $.common.showConfirmModal(json);
        },

        _delete: function (path) {
            $.common.ajaxActionText(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
            })
        },

        code: function () {
            var $code = $("#code");
            $code.addClass("validate[required,custom[onlyLetterNumberUnderline],ajax[ajaxCodeCheck]]");
            $code.removeAttr("readonly");
            $code.removeAttr("onclick");
            $.common.focus({id: "code"});
        },
        
        userList: function (path, id) {
        	$.common.pushStackContentParam();
        	
        	$.common.ajaxLoadContent({
        		url: path,
                type: "GET",
                dataType: "text",
                contentType: "default",
                data: {search_type__EQ_I: 2, search_cpCode__EQ_S: id, from: 'cp'},
                success: function (data) {
                }
            })
        },
        
        ajaxLoadLastContent: function (config) {
        	if (config == undefined) {
        		var config = {};
        	}
        	var url = stackContentRequestPath.pop();
        	var data = stackContentData.pop();
            var containerId = config.containerId || "page-content-wrapper";
            $.common.ajaxLoadContent({
                url: url,
                type: "GET",
                dataType: "text",
                contentType: "default",
                containerId: containerId,
                data: data,
                success: function (data) {
                }
            })
        },

    })
});