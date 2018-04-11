$(function () {
    $.TranscodeTaskController = function (elm, config) {

    }

    $.extend(true, $.TranscodeTaskController, $.BaseController, {

    	init: function (prefix) {
        	$.TranscodeTaskController.initTableCheckBox(prefix);
        },
        
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
            json.event = "$.TranscodeTaskController._delete('" + path + "')";
            $.common.showConfirmModal(json);
        },

        _delete: function (path) {
            $.common.ajaxActionText(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
            })
        },

        detail: function (id) {
            $.common.showModal({
                url: contextPath + "/transcode/transcodeTask/" + id + "/detail",
                success: function () {

                },
            });
        },

        toReset: function (path, name, methodDesc) {
            var json = {};
            json.title = methodDesc + "操作";
            json.body = "您确认要" + methodDesc + "[" + name + "]吗?";
            json.event = "$.TranscodeTaskController.reset('" + path + "')";
            $.common.showConfirmModal(json);
        },

        reset: function (path) {
            $.common.ajaxActionText(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
            })
        },

        toSend: function (path, name, methodDesc) {
            var json = {};
            json.title = methodDesc + "操作";
            json.body = "您确认要" + methodDesc + "[" + name + "]吗?";
            json.event = "$.TranscodeTaskController.send('" + path + "')";
            $.common.showConfirmModal(json);
        },

        send: function (path) {
            $.common.ajaxActionText(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
            })
        },
                
        batchChangePriority: function (path) {
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
                    var json = {
                        body: "批量调整优先级成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
    })
});
