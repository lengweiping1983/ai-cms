$(function () {
    $.ActivityController = function (elm, config) {

    }

    $.extend($.ActivityController, {

        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                    $.extend($.validationEngineLanguage.allRules, {
                        "ajaxCodeCheck": {
                            "url": contextPath + "/activity/activity/check?id=" + id,
                            "extraData": "dt=" + (new Date()).getTime(),
                            "alertText": "代码不能使用!",
                            "alertTextLoad": "验证中，请稍候..."
                        }
                    });
                    $("#editForm").validationEngine({
                        ajaxFormValidationMethod: 'post',
                    });
                    if (jQuery().datetimepicker) {
        				$(".form_datetime").datetimepicker({
        		            autoclose: true,
        		            isRTL: App.isRTL(),
        		            language : 'zh-CN',
        		            format: "yyyy-mm-dd hh:ii",
        		            pickerPosition: (App.isRTL() ? "bottom-left" : "bottom-left")
        		        });
        			}
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
            json.event = "$.ActivityController._delete('" + path + "')";
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

        toUser : function(url, activityId , activityName) {
            $.common.putLastContentParam();
            $.common.ajaxLoadContent({
                url : url ,
                type : "GET",
                dataType : "text",
                contentType : "default",
                data : {
                    search_activityId__EQ_I : activityId,
                    fromActivity : "true",
                    activityName : activityName
                },
                success : function(data) {
                }
            })
        },
    })
});