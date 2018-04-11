$(function () {
    $.WidgetController = function (elm, config) {

    }

    $.extend($.WidgetController, {

        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                    $.extend($.validationEngineLanguage.allRules, {
                        "ajaxCodeCheck": {
                            "url": contextPath + "/widget/widget/check?id=" + id,
                            "extraData": "dt=" + (new Date()).getTime(),
                            "alertText": "代码不能使用!",
                            "alertTextLoad": "验证中，请稍候..."
                        }
                    });
                    $("#editForm").validationEngine({
                        ajaxFormValidationMethod: 'post',
                    });
                    
                    $('.fileinput_backgroundImage').on('change.bs.fileinput', function(e, files) {
                		$("#backgroundImage").val("");
                		$("#backgroundImageData").val(files.result);
                	});
                	$('.fileinput_backgroundImage').on('clear.bs.fileinput', function() {
                    	$("#backgroundImage").val("");
                	});
                	
                	$("#code").val($("#code").val().toUpperCase());
                },
            });
        },

        edit: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();
            json.configItemTypes = $.WidgetController.getConfigItemTypes();

            var bean = {
                data: json,
                image1Data: $("#image1Data").val(),
                image2Data: $("#image2Data").val(),
                backgroundImageData: $("#backgroundImageData").val(),
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

        toDelete: function (path, name) {
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除[" + name + "]吗?";
            json.event = "$.WidgetController._delete('" + path + "')";
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
            $code.addClass("validate[required,custom[onlyLetterNumberUnderline],ajax[ajaxCodeCheck],maxSize[16]]");
            $code.removeAttr("readonly");
            $code.removeAttr("onclick");
            $.common.focus({id: "code"});
        },
        
        toChangeStatus: function (path, name, statusMethodDesc) {
        	var json = {};
            json.title = statusMethodDesc + "操作";
            json.body = "您确认要" + statusMethodDesc + "[" + name + "]吗?";
            json.event = "$.WidgetController.changeStatus('" + path + "')";
            $.common.showConfirmModal(json);
        },
        
        changeStatus: function (path) {
        	$.common.ajaxActionText(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
            })
        },

        showDetail: function (id) {
        	$.common.putLastContentParam();
        	
        	$.common.ajaxLoadContent({
        		url: contextPath + "/widget/"+id+"/widgetItem/",
                type: "GET",
                dataType: "text",
                contentType: "default",
                data: {search_widgetId__EQ_L: id},
                success: function (data) {
                }
            })
        },
        
        toSelectWidget: function () {
        	var appCode = $("#appCode").val();
        	$.common.ajaxActionText(contextPath + "/widget/widget/selectWidget?appCode="+appCode, function (data) {
        		$("#content_list_container").html(data);
        		$('#content_list_dialog_container').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择推荐位");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        selectWidget: function(id, name) {
        	$("#selectWidgetId").val(id);
            $("#selectWidgetName").val(name);
            $("#content_list_modal_container").modal('hide');
        },
        
        selectWidgets: function(prefix) {
        	var result = $.WidgetController.getItemIdsAndItemNamesString(prefix);
        	$("#selectWidgetId").val(result.itemIds);
            $("#selectWidgetName").val(result.itemNames);
            $("#content_list_modal_container").modal('hide');
            $('#content_list_modal_container_ok').hide();
        },
        
        toSelectItem: function (selectMode) {
        	var _selectMode = selectMode || 1;
        	$.common.ajaxActionText(contextPath + "/widget/widget/selectItem?selectMode=" + _selectMode, function (data) {
        		$("#content_list_container").html(data);
        		$('#content_list_dialog_container').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择推荐位");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        selectItem: function(selectMode, id, name, title, code, status, appCode) {
        	var _selectMode = selectMode || 1;
        	if (_selectMode == 1) {
	        	$("#widgetCode").val(code + "@" + appCode);
        	} else if(_selectMode == 91) {
        		$.UriController.selectParamByWidgetId(id);
        	}
        	
            $("#content_list_modal_container").modal('hide');
        },

        batchToWidget: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();
            
            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "text",
                contentType: "default",
                data: json,
                success: function (data) {
                	$.common.hideModal();
                	$.Page.refreshCurrentPage();
                	var json = {
                        body: "批量编排到推荐位成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        init: function (prefix) {      
            var table = $('#'+prefix+'content_list');

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
        
        getItemIdsAndItemNamesString: function (prefix) {
        	var result = {};
        	var itemIds = "";
        	var itemNames = "";
        	$('#'+prefix+'content_list tbody tr td input[type=checkbox].checkboxes:checked').each(function (i) {
                if (0 == i) {
                	itemIds = $(this).val();
                	itemNames = $(this).data('name');
                } else {
                	itemIds += ("," + $(this).val());
                	itemNames += ("," + $(this).data('name'));
                }
            });
        	result.itemIds = itemIds;
        	result.itemNames = itemNames;
            return result;
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
        
        getConfigItemTypes: function () {
            var configItemTypes = "";
            $('input:checkbox[name=configItemTypes]:checked').each(function (i) {
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
        
        clickConfigItemTypes: function () {
        	if ($("#configItemTypeAll").prop("checked")) {   
    	        $("input:checkbox[name=configItemTypes]").prop("checked", true);  
    	    } else {   
    	    	$("input:checkbox[name=configItemTypes]").prop("checked", false);
    	    }
        },
        
    })
});