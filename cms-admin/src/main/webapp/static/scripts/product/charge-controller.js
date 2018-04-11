$(function () {
    $.ChargeController = function (elm, config) {

    }

    $.extend(true, $.ChargeController, $.BaseController, {

        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                    $.extend($.validationEngineLanguage.allRules, {
                        "ajaxCodeCheck": {
                            "url": contextPath + "/config/charge/check?id=" + id,
                            "extraData": "dt=" + (new Date()).getTime(),
                            "extraDataDynamic": "#appCode",
                            "alertText": "代码不能使用!",
                            "alertTextLoad": "验证中，请稍候..."
                        }
                    });
                    $("#editForm").validationEngine({
                        ajaxFormValidationMethod: 'post',
                    });
                    
                    $("#code").val($("#code").val());
                    
                    $.ChargeController.initTimepicker();
                },
            });
        },

        edit: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();
            json.appCodes = $.ChargeController.getAppCodes();

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
            json.event = "$.ChargeController._delete('" + path + "')";
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
        
        initTimepicker : function() {
			if (jQuery().timepicker) {
				$('.timepicker-24').timepicker({
					autoclose : true,
					minuteStep : 5,
					showSeconds : false,
					showMeridian : false
				});
				$('.timepicker').parent('.input-group').on(
						'click',
						'.input-group-btn',
						function(e) {
							e.preventDefault();
							$(this).parent('.input-group').find('.timepicker')
									.timepicker('showWidget');
						});
			}
			if (jQuery().datepicker) {
				$('.date-picker').datepicker({
					rtl : App.isRTL(),
					orientation : "left",
					format : 'yyyy-mm-dd',
					language : 'zh-CN',
					autoclose : true
				});
				$('.date-picker').parent('.input-group').on(
					'click',
					'.input-group-btn',
					function(e) {
						e.preventDefault();
						$(this).parent('.input-group').find('.date-picker')
								.datepicker('show');
					});
			}
			if (jQuery().datetimepicker) {
				$(".form_datetime").datetimepicker({
		            autoclose: true,
		            isRTL: App.isRTL(),
		            language : 'zh-CN',
		            format: "yyyy-mm-dd hh:ii",
		            pickerPosition: (App.isRTL() ? "bottom-left" : "bottom-left")
		        });
			}
		},
        		
		getCharges: function () {
            var charges = "";
            $('input:checkbox[name=charges]:checked').each(function (i) {
                if (0 == i) {
                    charges = $(this).val();
                } else {
                    charges += ("," + $(this).val());
                }
            });
            return charges;
        },
        
		getAppCodes: function () {
            var appCodes = "";
            $('input:checkbox[name=appCodes]:checked').each(function (i) {
                if($(this).val() == "*" || appCodes == "*") {
            		appCodes = '*';
            	} else {
            		if (0 == i) {
                        appCodes = $(this).val();
                    } else {
                        appCodes += ("," + $(this).val());
                    }
            	}
            });
            return appCodes;
        },
        
        clickAppCodes: function () {
        	if ($("#appCodeAll").prop("checked")) {   
    	        $("input:checkbox[name=appCodes]").prop("checked", true);  
    	    } else {   
    	    	$("input:checkbox[name=appCodes]").prop("checked", false);
    	    }
        },

    })
});