$(function () {
    $.ActivityUserController = function (elm, config) {

    }

    $.extend($.ActivityUserController, {

        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
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
            json.event = "$.ActivityUserController._delete('" + path + "')";
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

        exportAll: function (path, itemType) {
            var form = $(document.forms[0]);
            var requestPath = path + "?1=1";
            var formData = form.serializeArray();
            formData.forEach(function (e) {
            	if (e.value && "" != e.value) {
            		requestPath = requestPath + "&" + e.name + "=" + e.value;
            	}
            });
            window.open(requestPath);
        },
    })
});