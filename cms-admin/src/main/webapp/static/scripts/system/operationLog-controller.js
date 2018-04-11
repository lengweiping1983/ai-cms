$(function () {
    $.OperationLogController = function (elm, config) {

    }

    $.extend(true, $.OperationLogController, $.BaseController, {

        toClear: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                	$.OperationLogController.initTimepicker();
                },
            });

        },

        clear: function () {
            if (!$("#editForm").validationEngine("validate")) return false;

            var createTime = $("#createTime").val();
    		var path = contextPath + "/system/operationlog/clear/" + createTime + "/";
    		$.common.ajaxActionJson(path, function(data) {
    			$.common.hideModal();
    			$.Page.refreshCurrentPage();
    			var alertMessage = data.message || "清空历史日志成功！";
                var json = {
                    body: alertMessage,
                };
                $.common.showAlertModal(json);
    		})
        },
    })
});