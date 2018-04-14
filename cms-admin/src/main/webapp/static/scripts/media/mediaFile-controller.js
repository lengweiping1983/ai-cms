$(function () {
    $.MediaFileController = function (elm, config) {

    }

    $.extend(true, $.MediaFileController, $.BaseController, {

    	init: function (prefix) {
    		$.MediaFileController.initTimepicker();
        	
        	$.MediaFileController.initTableCheckBox(prefix);
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
            json.event = "$.MediaFileController._delete('" + path + "')";
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
                url: contextPath + "/media/mediaFile/" + id + "/detail",
                success: function () {

                },
            });
        },
        
        changeFilePath: function () {
        	var filePath = $("#simplefilePath").val();
        	if (filePath && filePath != '') {
        		$("#filePath_div_1").removeAttr("disabled");
        	} else {
        		$("#filePath_div_1").attr("disabled", "disabled");
        	}
        },
        
        toSelectFile: function () {
        	$.FileManageController.toSelectItem(contextPath + '/media/file/selectFile?selectMode=simple&selectParam=simple');
        },
        
        toPreview: function () {
        	var filePath = $("#simplefilePath").val();
        	if (filePath) {
        		$.PlayController.toPreview(filePath);
        	}
        },
        
        toPlayCode: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                },
            });
        },
        
        playCode: function (path, defaultPrompt) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();

            var bean = {
                data: json,
            };
            
            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: bean,
                success: function (data) {
                    $.common.hideModal();
                    $.Page.refreshCurrentPage();
                    var alertMessage = data.message || defaultPrompt;
                    if (alertMessage && alertMessage != "") {
	                    var json = {
	                        body: alertMessage,
	                    };
	                    $.common.showAlertModal(json);
                    }
                }
            })
        },
    })
});



