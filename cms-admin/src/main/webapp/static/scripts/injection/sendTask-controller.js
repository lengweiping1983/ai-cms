$(function () {
    $.SendTaskController = function (elm, config) {

    }

    $.extend(true, $.SendTaskController, $.BaseController, {

    	init: function (prefix) {
    		$.SendTaskController.initTimepicker();
    		
    		$.SendTaskController.initTableCheckBox(prefix);
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
            json.event = "$.SendTaskController._delete('" + path + "')";
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
                url: contextPath + "/injection/sendTask/" + id + "/detail",
                success: function () {

                },
            });
        },

        toSend: function (path, name, methodDesc) {
            var json = {};
            json.title = methodDesc + "操作";
            json.body = "您确认要" + methodDesc + "[" + name + "]吗?";
            json.event = "$.SendTaskController.send('" + path + "')";
            $.common.showConfirmModal(json);
        },

        send: function (path) {
            $.common.ajaxActionText(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
            })
        },
        
        batchTo: function (path, itemType) {
            var itemIds = $.SendTaskController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
                var json = {
                    body: "请选择任务！",
                };
                $.common.showAlertModal(json);
                return;
            }

            var requestPath = path + "?itemType=" + itemType + "&itemIds=" + itemIds;

            $.common.showModal({
                url: requestPath,
                type: "GET",
                dataType: "text",
                contentType: "default",
                success: function () {
                },
            });
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
        
        toBatchChangeStatus: function (path, itemType, desc) {
        	var itemIds = $.SendTaskController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
                var json = {
                    body: "请选择工单！",
                };
                $.common.showAlertModal(json);
                return;
            }
            
            var json = {};
            json.title = desc+ "操作";
            json.body = "您确认要" + desc + "吗?";
            json.event = "$.SendTaskController.batchChangeStatus('" + path + "'," + itemType + ",'" + desc + "')";
            $.common.showConfirmModal(json);
        },
        
        batchChangeStatus: function (path, itemType, desc) {
        	var itemIds = $.SendTaskController.getItemIdsString();
            
            var json = {};
            json.itemType = itemType;
            json.itemIds = itemIds;

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
                        body: desc + "成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
    })
});
