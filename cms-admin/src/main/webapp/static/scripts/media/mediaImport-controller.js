$(function () {

    $.MediaImportController = function (elm, config) {

    }

    $.extend(true, $.MediaImportController, $.BaseController, {

    	init: function () {
    		$.MediaImportController.initTimepicker();
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

        detail: function (path) {
            $.common.showModal({
                url: path,
                success: function () {

                },
            });
        },

        editSubmit: function (path, submitContainerId) {
        	$("#MediaImportSave").attr("disabled","disabled");
        	App.blockUI({
                target: '#' + submitContainerId + '',
                overlayColor: 'none',
                animate: true
            });
        	
            var formData = new FormData($("#editForm")[0]);
            
            $.common.ajaxActionFile({
            	url: path,
                type: 'POST',
                data: formData,
                async: true,
                cache: false,
                contentType: false,
                processData: false,
                submitContainerId: submitContainerId,
                success: function (data) {
                    $.common.hideModal();
                    $.Page.refreshCurrentPage();
                },
                errorCallBack: function (XMLHttpRequest, textStatus, errorThrown) {
	            	$("#MediaImportSave").removeAttr("disabled");
	            }
            });
        },
        
        edit: function (path) {
            if (!$("#editForm").validationEngine("validate"))
                return false;

            var json = {};
            json.title = "导入操作";
            json.body = "您确认要导入吗?";
            json.event = "$.MediaImportController.editSubmit('" + path + "','confirm_modal_dialog')";
            $.common.showConfirmModal(json);
        },

        toDelete: function (path, name) {
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除[" + name + "]吗?";
            json.event = "$.MediaImportController._delete('" + path + "')";
            $.common.showConfirmModal(json);
        },

        _delete: function (path) {
            $.common.ajaxActionText(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
            })
        },

        setUploadFileName: function(obj) {
        	var filePath = obj.value;
        	if (filePath != '') {
        		return ;
        	}
        	var suf = filePath.substring(filePath.lastIndexOf('.') + 1);
        	if (suf != '' && suf.toLowerCase() != 'xls' && suf.toLowerCase() != 'xlsx') {
        		$('#fileName').val('');
        		alert('只支持导入“xls”或“xlsx”格式文件!');
        		return;
        	}
        }
    });

});
