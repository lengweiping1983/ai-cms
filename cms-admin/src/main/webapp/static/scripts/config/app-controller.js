$(function () {
    $.AppController = function (elm, config) {

    }

    $.extend($.AppController, {

        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                    $.extend($.validationEngineLanguage.allRules, {
                        "ajaxCodeCheck": {
                            "url": contextPath + "/app/app/check?id=" + id,
                            "extraData": "dt=" + (new Date()).getTime(),
                            "alertText": "代码不能使用!",
                            "alertTextLoad": "验证中，请稍候..."
                        }
                    });
                    $("#editForm").validationEngine({
                        ajaxFormValidationMethod: 'post',
                    });
                },
            });
        },

        edit: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();
            json.cpCodes = $.AppController.getCpCodes();
            json.appCodes = $.AppController.getAppCodes();

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
        
        getCpCodes: function () {
            var cpCodes = "";
            $('input:checkbox[name=cpCodes]:checked').each(function (i) {
                if($(this).val() == "" || cpCodes == "*") {
            		cpCodes = '*';
            	} else {
            		if (0 == i) {
                        cpCodes = $(this).val();
                    } else {
                        cpCodes += ("," + $(this).val());
                    }
            	}
            });
            if (cpCodes == "*") {
            	cpCodes = "";
            }
            return cpCodes;
        },
        
        clickCpCodes: function () {
        	if ($("#cpCodeAll").prop("checked")) {   
    	        $("input:checkbox[name=cpCodes]").prop("checked", true);  
    	    } else {   
    	    	$("input:checkbox[name=cpCodes]").prop("checked", false);
    	    }
        },
        
        getAppCodes: function () {
            var appCodes = "";
            $('input:checkbox[name=appCodes]:checked').each(function (i) {
                if($(this).val() == "" || appCodes == "*") {
            		appCodes = '*';
            	} else {
            		if (0 == i) {
                        appCodes = $(this).val();
                    } else {
                        appCodes += ("," + $(this).val());
                    }
            	}
            });
            if (appCodes == "*") {
            	appCodes = "";
            }
            return appCodes;
        },
        
        clickAppCodes: function () {
        	if ($("#appCodeAll").prop("checked")) {   
    	        $("input:checkbox[name=appCodes]").prop("checked", true);  
    	    } else {   
    	    	$("input:checkbox[name=appCodes]").prop("checked", false);
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