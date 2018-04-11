$(function () {
    $.LeagueController = function (elm, config) {

    }

    $.extend($.LeagueController, {

        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                    $.extend($.validationEngineLanguage.allRules, {
                        "ajaxCodeCheck": {
                            "url": contextPath + "/league/league/check?id=" + id,
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

        toDelete: function (path, name) {
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除[" + name + "]吗?";
            json.event = "$.LeagueController._delete('" + path + "')";
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
                url: contextPath + "/league/league/"+id+"/detail",
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
            json.event = "$.LeagueController.changeStatus('" + path + "')";
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
        		url: contextPath + "/star/"+id+"/leagueItem/",
                type: "GET",
                dataType: "text",
                contentType: "default",
                data: {search_league_id__EQ_L: id},
                success: function (data) {
                }
            })
        },
        
        toSelectLeague: function () {
        	$.common.ajaxActionText(contextPath + "/league/league/selectLeague", function (data) {
        		$("#content_list_container").html(data);
        		$('#content_list_dialog_container').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择联赛");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        selectLeague: function(id, name) {
        	$("#leagueId").val(id);
            $("#leagueName").val(name);
            $("#content_list_modal_container").modal('hide');
        },

        batchToLeague: function (path) {
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
                        body: "批量编排到联赛成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        toSelectItem: function () {
        	$.common.ajaxActionText(contextPath + "/league/league/selectItem", function (data) {
        		$("#content_list_container").html(data);
        		$('#content_list_dialog_container').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择联赛");
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