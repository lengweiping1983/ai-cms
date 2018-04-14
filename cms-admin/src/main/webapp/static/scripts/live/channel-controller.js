$(function () {
    $.ChannelController = function (elm, config) {

    }

    $.extend($.ChannelController, {

        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                	
                	try {
        	        	$('#cpId').multiselect({
        	            	enableFiltering: true,
        	            	nonSelectedText:'请选择',
        	            	filterPlaceholder:'搜索',
        	            });
                	} catch(e) {
                		
                	}
                	
                    $.extend($.validationEngineLanguage.allRules, {
                        "ajaxCodeCheck": {
                            "url": contextPath + "/live/channel/check?id=" + id,
                            "extraData": "dt=" + (new Date()).getTime(),
                            "alertText": "代码不能使用!",
                            "alertTextLoad": "验证中，请稍候..."
                        }
                    });
                    $("#editForm").validationEngine({
                        ajaxFormValidationMethod: 'post',
                    });
                    
                    $('.fileinput_image1').on('change.bs.fileinput', function(e, files) {
                    	$("#image1").val("");
                    	$("#image1Data").val(files.result);
                	});
                    $('.fileinput_image1').on('clear.bs.fileinput', function() {
                    	$("#image1").val("");
                	});
                    

                	$('.fileinput_image2').on('change.bs.fileinput', function(e, files) {
                		$("#image2").val("");
                		$("#image2Data").val(files.result);
                	});
                	$('.fileinput_image2').on('clear.bs.fileinput', function() {
                    	$("#image2").val("");
                	});
                	
                },
            });
        },

        edit: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();

            var bean = {
                data: json,
                image1Data: $("#image1Data").val(),
                image2Data: $("#image2Data").val(),
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
            json.event = "$.ChannelController._delete('" + path + "')";
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
                url: contextPath + "/live/channel/"+id+"/detail",
                success: function () {
                	try {
        	        	$('#cpId').multiselect({
        	            	enableFiltering: true,
        	            	nonSelectedText:'请选择',
        	            	filterPlaceholder:'搜索',
        	            });
                	} catch(e) {
                		
                	}
                },
            });
        },

        changeSelectType: function (type) {
        	var channel_div_1 = $("#channel_div_1");
        	
        	if(type == 0) {
        		channel_div_1.hide();
        	} else if(type == 1) {
        		channel_div_1.show();
        	}
        },
        
        code: function () {
            var $code = $("#code");
            $code.addClass("validate[required,custom[onlyLetterNumberUnderline],ajax[ajaxCodeCheck]]");
            $code.removeAttr("readonly");
            $code.removeAttr("onclick");
            $.common.focus({id: "code"});
        },
        
        toChangeStatus: function (path, name, statusMethodDesc) {
        	var json = {};
            json.title = statusMethodDesc + "操作";
            json.body = "您确认要" + statusMethodDesc + "[" + name + "]吗?";
            json.event = "$.ChannelController.changeStatus('" + path + "')";
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
        		url: contextPath + "/live/"+id+"/schedule/",
                type: "GET",
                dataType: "text",
                contentType: "default",
                data: {search_channelId__EQ_L: id},
                success: function (data) {
                }
            })
        },
        
        toSelectChannel: function () {
        	$.common.ajaxActionText(contextPath + "/live/channel/selectChannel", function (data) {
        		$("#content_list_modal_container_body").html(data);
        		$('#content_list_modal_container_dialog').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择中央频道");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        selectChannel: function(id, name, code, type) {
        	$("#channelId").val(id).change();
            $("#channelName").val(name);
            $("#channelCode").val(code);
            try {
            	$("#channelType").val(type).change();
            } catch(e) {
            	
            }
            $("#content_list_modal_container").modal('hide');
        },

        batchToChannel: function (path) {
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
                	
                	var json = {
                        body: "批量编排到中央频道成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        toSelectItem: function () {
        	$.common.ajaxActionText(contextPath + "/live/channel/selectItem", function (data) {
        		$("#content_list_modal_container_body").html(data);
        		$('#content_list_modal_container_dialog').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择中央频道");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        selectItem: function(id, name, title, image1, image2, status) {
        	$("#itemId").val(id);
            $("#itemName").val(name);
            $("#itemTitle").val(title);
            if(image1 != "") {
            	$("#itemImage1_img").attr("src", imageWebPath + image1);
            } else {
            	$("#itemImage1_img").attr("src", noImagePath);
            }
            if(image2 != "") {
            	$("#itemImage2_img").attr("src", imageWebPath + image2);
            } else {
            	$("#itemImage2_img").attr("src", noImagePath);
            }
            $("#itemStatus").val(status);
            $("#content_list_modal_container").modal('hide');
        },
    })
});