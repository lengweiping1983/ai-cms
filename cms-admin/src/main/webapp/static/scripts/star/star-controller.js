$(function () {
    $.StarController = function (elm, config) {

    }

    $.extend($.StarController, {

        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                    $.extend($.validationEngineLanguage.allRules, {
                        "ajaxCodeCheck": {
                            "url": contextPath + "/star/star/check?id=" + id,
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
        
        changeSelectType: function (type) {
        	var star_div_2 = $("#star_div_2");
        	var star_div_3 = $("#star_div_3");
        	if(type == 2) {
        		star_div_2.show();
        		star_div_3.hide();
        	} else if(type == 3) {
        		star_div_3.show();
        		star_div_2.hide();
        	} else {
        		star_div_3.show();
        		star_div_3.hide();
        	}
        },

        toDelete: function (path, name) {
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除[" + name + "]吗?";
            json.event = "$.StarController._delete('" + path + "')";
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
                url: contextPath + "/star/star/"+id+"/detail",
                success: function () {
                	
                },
            });
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
            json.event = "$.StarController.changeStatus('" + path + "')";
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
        		url: contextPath + "/star/"+id+"/starItem/",
                type: "GET",
                dataType: "text",
                contentType: "default",
                data: {search_star_id__EQ_L: id},
                success: function (data) {
                }
            })
        },
        
        toSelectStar: function (type) {
        	$.common.ajaxActionText(contextPath + "/star/star/selectStar?type=" + type, function (data) {
        		$("#content_list_modal_container_body").html(data);
        		$('#content_list_modal_container_dialog').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择明星");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        selectStar: function(id, name, type) {
        	if(type == 1) {
        		$("#selectStarId").val(id);
                $("#selectStarName").val(name);
        	} else if(type == 2) {
        		$("#homeId").val(id);
        		$("#homeName").val(name);
        		$("#homeType").val(2);
        	} else if(type == 3) {
        		$("#guestId").val(id);
        		$("#guestName").val(name);
        		$("#guestType").val(2);
        	} else {
        		$("#selectStarId").val(id);
                $("#selectStarName").val(name);
        	}
            $("#content_list_modal_container").modal('hide');
        },

        batchToStar: function (path) {
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
                        body: "批量编排到明星成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        toSelectItem: function (selectMode) {
        	var _selectMode = selectMode || 1;
        	$.common.ajaxActionText(contextPath + "/star/star/selectItem?selectMode=" + _selectMode, function (data) {
        		$("#content_list_modal_container_body").html(data);
        		$('#content_list_modal_container_dialog').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择明星");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        toSelectItemByType: function (selectMode, type) {
        	var _selectMode = selectMode || 1;
        	$.common.ajaxActionText(contextPath + "/star/star/selectItem?selectMode=" + _selectMode + "&search_type__EQ_I=" + type, function (data) {
        		$("#content_list_modal_container_body").html(data);
        		$('#content_list_modal_container_dialog').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择明星");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        selectItem: function(selectMode, id, name, title, image1, image2, status) {
        	var _selectMode = selectMode || 1;
        	if (_selectMode == 1) {
	        	$("#itemId").val(id).change();
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
        	} else if(_selectMode == 91) {
        		$.UriController.selectParamByStarId(id);
        	}
            $("#content_list_modal_container").modal('hide');
        },
    })
});