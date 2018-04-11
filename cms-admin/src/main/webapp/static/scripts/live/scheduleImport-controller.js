$(function () {

    $.ScheduleImportUpdateController = function (elm, config) {

    }

    $.extend($.ScheduleImportUpdateController, {

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
        	$("#ProgramImportUpdateSave").attr("disabled","disabled");
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
	            	$("#ProgramImportUpdateSave").removeAttr("disabled");
	            }
            });
        },
        
        edit: function (path) {
            if (!$("#editForm").validationEngine("validate"))
                return false;

            var json = {};
            json.title = "执行操作";
            json.body = "您确认要执行吗?";
            json.event = "$.ScheduleImportUpdateController.editSubmit('" + path + "','confirm_modal_dialog')";
            $.common.showConfirmModal(json);
        },

        toDelete: function (path, name) {
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除[" + name + "]吗?";
            json.event = "$.ScheduleImportUpdateController._delete('" + path + "')";
            $.common.showConfirmModal(json);
        },

        _delete: function (path) {
            $.common.ajaxActionText(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
            })
        },
                
        init: function () {
            var table = $('#content_list');

            table.find('.group-checkable').change(function () {
                var checked = jQuery(this).is(":checked");
                if (checked) {
                    $(this).parents('table').find('input[type=checkbox].group-checkable').parents('span').addClass("checked");
                } else {
                    $(this).parents('table').find('input[type=checkbox].group-checkable').parents('span').removeClass("checked");
                }
                $(this).parents('table').find('input[type=checkbox].group-checkable').prop('checked', checked);
                var set = jQuery(this).attr("data-set");
                jQuery(set).each(function () {
                    if (checked) {
                        $(this).prop('checked', true);
                        $(this).parents('span').addClass("checked");
                        $(this).parents('tr').addClass("active");
                    } else {
                        $(this).prop('checked', false);
                        $(this).parents('span').removeClass("checked");
                        $(this).parents('tr').removeClass("active");
                    }
                });
                jQuery.uniform.update(set);
            });

            table.on('change', 'tbody tr .checkboxes', function () {
                var checked = jQuery(this).is(":checked");
                if (checked) {
                    $(this).parents('span').addClass("checked");
                } else {
                    $(this).parents('span').removeClass("checked");
                }
                $(this).parents('tr').toggleClass("active");
            });

        },
        
        getItemIds: function () {
            var itemIds = [];
            $('#content_list tbody tr td input[type=checkbox].checkboxes:checked').each(function () {
                itemIds.push($(this).val());
            });
            return itemIds;
        },
        
        getItemIdsString: function () {
        	var itemIds = "";
        	$('#content_list tbody tr td input[type=checkbox].checkboxes:checked').each(function (i) {
                if (0 == i) {
                	itemIds = $(this).val();
                } else {
                	itemIds += ("," + $(this).val());
                }
            });
            return itemIds;
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
