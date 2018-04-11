$(function () {
    $.TemplateParamController = function (elm, config) {

    }

    $.extend($.TemplateParamController, {

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
                }
            })
        },
        
        toEditByPosition: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                	$("#editForm").validationEngine({
                        ajaxFormValidationMethod: 'post',
                    });
                	
                	$("#position").val($("#input-position").val());
                	$("#number").val($("#number").val().toUpperCase());
                },
            });
        },
        
        editByPosition: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();
            
            var type = $("#type").val();
            json.configItemTypes = $.TemplateParamController.getConfigItemTypes(type);

            var title = $('#type option:selected').text();
        	var text = $('#name').val();
            var positionJson = eval("(" + json.position + ")");
            positionJson.title = title;
            positionJson.text = text;
            json.position = JSON.stringify(positionJson);
            
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
                	var title = $('#type option:selected').text();
                	var text = $('#name').val();
        			annotator_canvas_update_item(title, text);
                	$.common.hideModal();
                }
            })
        },

        editByPositionFromMove: function (path, json) {
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
                }
            })
        },
        
        toDeleteByPositionBeforeNotSave: function () {
        	annotator_canvas_delete_item();
            $.common.hideModal();
        },
        
        toDeleteByPosition: function (path, name) {
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除[" + name + "]吗?";
            json.event = "$.TemplateParamController._deleteByPosition('" + path + "')";
            $.common.showConfirmModal(json);
        },

        _deleteByPosition: function (path) {
            $.common.ajaxActionText(path, function (data) {
            	annotator_canvas_delete_item();
                $.common.hideModal();
            })
        },
        
        toEditByPositionFromAnnotator: function (positionId) {
	        var templateId = $("#templateId").val();
			$.TemplateParamController.toEditByPosition(contextPath + '/template/' + templateId + '/templateParam/editByPosition?positionId='
					+ positionId + '', -1);
        },
        
        editByPositionFromAnnotator: function (json) {
        	var templateId = $("#templateId").val();
        	$.TemplateParamController.editByPositionFromMove(contextPath + '/template/' + templateId + '/templateParam/editByPosition', json);
        },
        
        toDelete: function (path, name) {
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除[" + name + "]吗?";
            json.event = "$.TemplateParamController._delete('" + path + "')";
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
        
        changeConfigImage1: function (type) {
        	var configImage1_content_div = $("#configImage1_content_div");
        	
        	if(type == 0) {
        		configImage1_content_div.hide();
        	} else if(type == 1) {
        		configImage1_content_div.show();
        	}
        },
        
        changeConfigImage2: function (type) {
        	var configImage2_content_div = $("#configImage2_content_div");
        	
        	if(type == 0) {
        		configImage2_content_div.hide();
        	} else if(type == 1) {
        		configImage2_content_div.show();
        	}
        },
        
        changeType: function (type) {
        	var ext_config_div = $("#ext_config_div");
        	var widget_div = $("#widget_div");
        	var configItemTypes_4_div = $("#configItemTypes_4_div");
        	var configItemTypes_5_div = $("#configItemTypes_5_div");
        	
        	if (type == 4) {
        		ext_config_div.show();
        		widget_div.show();
        		configItemTypes_4_div.show();
        		configItemTypes_5_div.hide();
        	} else if (type == 5) {
        		ext_config_div.show();
        		widget_div.hide();
        		configItemTypes_4_div.hide();
        		configItemTypes_5_div.show();
        	} else {
        		ext_config_div.hide();
        	}
        },
        
        
        getConfigItemTypes: function (type) {
            var configItemTypes = "";
            $('input:checkbox[name=configItemTypes_' + type + ']:checked').each(function (i) {
                if($(this).val() == "" || configItemTypes == "*") {
            		configItemTypes = '*';
            	} else {
            		if (0 == i) {
                        configItemTypes = $(this).val();
                    } else {
                        configItemTypes += ("," + $(this).val());
                    }
            	}
            });
            if (configItemTypes == "*") {
            	configItemTypes = "";
            }
            return configItemTypes;
        },
        
        clickConfigItemTypes: function (type) {
        	if ($('#configItemTypeAll_' + type + '').prop("checked")) {   
    	        $('input:checkbox[name=configItemTypes_' + type + ']').prop("checked", true);  
    	    } else {   
    	    	$('input:checkbox[name=configItemTypes_' + type + ']').prop("checked", false);
    	    }
        },
    })
});

