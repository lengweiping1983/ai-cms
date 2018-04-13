$(function () {
    $.InjectionPlatformController = function (elm, config) {

    }

    $.extend(true, $.InjectionPlatformController, $.BaseController, {

        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                	$("input:checkbox[name='templateId']").uniform();
                	$("input:checkbox[name='dependPlatformId']").uniform();
                	$("input:checkbox[name='injectionPlatformId']").uniform();
                	$("input:checkbox[name='indirectPlatformId']").uniform();
                	
                    $("#editForm").validationEngine({
                        ajaxFormValidationMethod: 'post',
                    });
                },
            });
        },
        
        changeSelectType: function (type) {
        	var type_div_1 = $("#type_div_1");
        	if (type == 1) {
        		type_div_1.hide();
        	} else if(type == 2) {
        		type_div_1.show();
        	}
        },
        
        changeSelectNeedAudit: function (type) {
        	var needInjection_div_1 = $("#needInjection_div_1");
        	if (type == 1) {
        		needInjection_div_1.hide();
        	} else if(type == 0) {
        		needInjection_div_1.show();
        	}
        },
        
        changeIsCallback: function (type) {
        	var isCallback_div_1 = $("#isCallback_div_1");
        	if (type == 1) {
        		isCallback_div_1.show();
        	} else if(type == 0) {
        		isCallback_div_1.hide();
        	}
        },
        
        changeTemplateCustom: function (type) {
        	var templateCustom_div_1 = $("#templateCustom_div_1");
        	if (type == 1) {
        		templateCustom_div_1.show();
        	} else if(type == 0) {
        		templateCustom_div_1.hide();
        	}
        },
        
        changeSelectNeedInjection: function (type) {
        	var needInjection_div_2 = $("#needInjection_div_2");
        	if (type == 0) {
        		needInjection_div_2.hide();
        	} else if(type == 1) {
        		needInjection_div_2.show();
        	}
        },
        
        changeSelectDirection: function (type) {
        	var direction_div_1 = $("#direction_div_1");
        	if (type == 0) {
        		type_div_1.hide();
        	} else if(type == 1) {
        		direction_div_1.show();
        	}
        },

        edit: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();
            json.templateId = $.InjectionPlatformController.getCheckboxValue('templateId');
            json.dependPlatformId = $.InjectionPlatformController.getCheckboxValue('dependPlatformId');
            json.injectionPlatformId = $.InjectionPlatformController.getCheckboxValue('injectionPlatformId');
            json.indirectPlatformId = $.InjectionPlatformController.getCheckboxValue('indirectPlatformId');

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
            json.event = "$.InjectionPlatformController._delete('" + path + "')";
            $.common.showConfirmModal(json);
        },

        _delete: function (path) {
            $.common.ajaxActionText(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
            })
        },
    })
});