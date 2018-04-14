$(function () {
    $.LeagueSeasonController = function (elm, config) {

    }

    $.extend($.LeagueSeasonController, {

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
                	
                	$.ProgramController.initTimepicker();
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
            json.event = "$.LeagueSeasonController._delete('" + path + "')";
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
                url: contextPath + "/league/leagueSeason/"+id+"/detail",
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
        
        toChangeStatus: function (path, name, statusMethodDesc) {
        	var json = {};
            json.title = statusMethodDesc + "操作";
            json.body = "您确认要" + statusMethodDesc + "[" + name + "]吗?";
            json.event = "$.LeagueSeasonController.changeStatus('" + path + "')";
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
        		url: contextPath + "/league/"+id+"/leagueMatch/",
                type: "GET",
                dataType: "text",
                contentType: "default",
                data: {search_leagueSeasonId__EQ_L: id},
                success: function (data) {
                }
            })
        },
        
        showClubList: function (id) {
        	$.common.putLastContentParam();
        	
        	$.common.ajaxLoadContent({
        		url: contextPath + "/league/"+id+"/leagueSeasonClub/",
                type: "GET",
                dataType: "text",
                contentType: "default",
                data: {search_leagueSeasonId__EQ_L: id},
                success: function (data) {
                }
            })
        },
        
        showStarList: function (id) {
        	$.common.putLastContentParam();
        	
        	$.common.ajaxLoadContent({
        		url: contextPath + "/league/"+id+"/leagueSeasonStar/",
                type: "GET",
                dataType: "text",
                contentType: "default",
                data: {search_leagueSeasonId__EQ_L: id},
                success: function (data) {
                }
            })
        },
                
        toSelectLeagueSeason: function () {
        	$.common.ajaxActionText(contextPath + "/league/leagueSeason/selectLeagueSeason", function (data) {
        		$("#content_list_modal_container_body").html(data);
        		$('#content_list_modal_container_dialog').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择赛季");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        selectLeagueSeason: function(id, name) {
        	$("#leagueSeasonId").val(id);
            $("#leagueSeasonName").val(name);
            $("#content_list_modal_container").modal('hide');
        },
        
        toSelectItem: function () {
        	$.common.ajaxActionText(contextPath + "/league/leagueSeason/selectItem", function (data) {
        		$("#content_list_modal_container_body").html(data);
        		$('#content_list_modal_container_dialog').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择赛季");
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
