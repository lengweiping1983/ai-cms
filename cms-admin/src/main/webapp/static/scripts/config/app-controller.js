$(function () {
    $.AppController = function (elm, config) {

    }

    $.extend(true, $.AppController, $.BaseController, {

        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                    $.extend($.validationEngineLanguage.allRules, {
                        "ajaxCodeCheck": {
                            "url": contextPath + "/config/app/check?id=" + id,
                            "extraData": "dt=" + (new Date()).getTime(),
                            "alertText": "代码不能使用!",
                            "alertTextLoad": "验证中，请稍候..."
                        }
                    });
                    $("#editForm").validationEngine({
                        ajaxFormValidationMethod: 'post',
                    });
                    
                    // $("input:checkbox[name='accessCpCode']").uniform();
                    // $("input:checkbox[name='accessAppCode']").uniform();
                },
            });
        },

        edit: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();
            json.accessCpCode = $.AppController.getAccessCpCode();
            json.accessAppCode = $.AppController.getAccessAppCode();

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
            json.event = "$.AppController._delete('" + path + "')";
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
        
        getAccessCpCode: function () {
            var accessCpCode = "";
            $('input:checkbox[name=accessCpCode]:checked').each(function (i) {
                if($(this).val() == "" || accessCpCode == "*") {
            		accessCpCode = '*';
            	} else {
            		if (0 == i) {
                        accessCpCode = $(this).val();
                    } else {
                        accessCpCode += ("," + $(this).val());
                    }
            	}
            });
            if (accessCpCode == "*") {
            	accessCpCode = "";
            }
            return accessCpCode;
        },
        
        clickCpCodeAll: function () {
        	if ($("#cpCodeAll").prop("checked")) {   
    	        $("input:checkbox[name=accessCpCode]").prop("checked", true);  
    	    } else {   
    	    	$("input:checkbox[name=accessCpCode]").prop("checked", false);
    	    }
        },
        
        getAccessAppCode: function () {
            var accessAppCode = "";
            $('input:checkbox[name=accessAppCode]:checked').each(function (i) {
                if($(this).val() == "" || accessAppCode == "*") {
            		accessAppCode = '*';
            	} else {
            		if (0 == i) {
                        accessAppCode = $(this).val();
                    } else {
                        accessAppCode += ("," + $(this).val());
                    }
            	}
            });
            if (accessAppCode == "*") {
            	accessAppCode = "";
            }
            return accessAppCode;
        },
        
        clickAppCodeAll: function () {
        	if ($("#appCodeAll").prop("checked")) {   
    	        $("input:checkbox[name=accessAppCode]").prop("checked", true);  
    	    } else {   
    	    	$("input:checkbox[name=accessAppCode]").prop("checked", false);
    	    }
        },

        changeAloneCharge: function (type) {
        	var chargeAppCode_div = $("#chargeAppCode_div");
        	
        	if (type == 1) {
        		chargeAppCode_div.hide();
        		$("#chargeAppCode").val("");
        	} else {
        		chargeAppCode_div.show();
        	}
        },
    })
});