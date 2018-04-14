$(function () {
    $.TemplateController = function (elm, config) {

    }

    $.extend($.TemplateController, {

        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                    $.extend($.validationEngineLanguage.allRules, {
                        "ajaxCodeCheck": {
                            "url": contextPath + "/template/template/check?id=" + id,
                            "extraData": "dt=" + (new Date()).getTime(),
                            "extraDataDynamic": "#appCode",
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
                },
            });
        },

        edit: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();

            var bean = {
                data: json,
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
            json.event = "$.TemplateController._delete('" + path + "')";
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

        changeAppCode: function () {
        },

        listByPosition: function (path) {
        	$.common.putLastContentParam();
        	
        	$.common.ajaxLoadContent({
        		url: path,
                type: "GET",
                dataType: "text",
                contentType: "default",
                success: function (data) {
                	var text = '$("#the-img-tag").annotatorPro({annotations : [';
                	text += $('#positionStr').val();
					text +="]});";
					annotator_canvas_open(text);
                }
            })
        },
        
        toSelectItem: function (selectMode, type) {
        	var _selectMode = selectMode || 1;
        	var _type = type || '';
        	$.common.ajaxActionText(contextPath + "/template/template/selectItem?selectMode=" + _selectMode + "&type=" + _type + "&search_type__EQ_I=" + _type, function (data) {
        		$("#content_list_modal_container_body").html(data);
        		$('#content_list_modal_container_dialog').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择模板");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        selectItem: function(selectMode, id, name, code, appCode) {
        	var _selectMode = selectMode || 1;
        	if (_selectMode == 1) {
        		$("#templateId").val(id).change();
        		$("#templateName").val(name);
	        	$("#templateCode").val(code + "@" + appCode);
        	}
            $("#content_list_modal_container").modal('hide');
        },
    })
});