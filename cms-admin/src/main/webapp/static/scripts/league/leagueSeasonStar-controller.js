$(function () {
    $.LeagueSeasonStarController = function (elm, config) {

    }

    $.extend($.LeagueSeasonStarController, {

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
            json.event = "$.LeagueSeasonStarController._delete('" + path + "')";
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
        
        toChangeStatus: function (path, name, statusMethodDesc) {
        	var json = {};
            json.title = statusMethodDesc + "操作";
            json.body = "您确认要" + statusMethodDesc + "[" + name + "]吗?";
            json.event = "$.LeagueSeasonStarController.changeStatus('" + path + "')";
            $.common.showConfirmModal(json);
        },
        
        changeStatus: function (path) {
        	$.common.ajaxActionText(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
            })
        },

        toSelectItem: function () {
        	var itemType = $("#itemType").val();
        	if(itemType == 1) {
        		$.ProgramController.toSelectItem();
        	} else if(itemType == 2) {
        		$.SeriesController.toSelectItem();
        	} else if(itemType == 3) {
        		$.AlbumController.toSelectItem();
        	} else if(itemType == 5) {
        		$.CategoryController.selectCategoryItemTree();
        	} else if(itemType == 6) {
        		$.UriController.toSelectItem();
        	} else if(itemType == 7) {
        		$.ChannelController.toSelectItem();
        	} else if(itemType == 8) {
        		$.ScheduleController.toSelectItem();
        	} else if(itemType == 9) {
        		$.LeagueController.toSelectItem();
        	} else if(itemType == 10) {
        		$.ClubController.toSelectItem();
        	} else if(itemType == 11) {
        		$.StarController.toSelectItemByType(2);
        	}
        },
        
        detail: function (itemType, id) {
        	if(itemType == 1) {
        		$.ProgramController.detail(id);
        	} else if(itemType == 2) {
        		$.SeriesController.detail(id);
        	} else if(itemType == 3) {
        		$.AlbumController.detail(id);
        	} else if(itemType == 5) {
        		$.CategoryController.detail(id);
        	} else if(itemType == 6) {
        		$.UriController.detail(id);
        	} else if(itemType == 7) {
        		$.ChannelController.detail(id);
        	} else if(itemType == 8) {
        		$.ScheduleController.detail(id);
        	} else if(itemType == 9) {
        		$.LeagueController.detail(id);
        	} else if(itemType == 10) {
        		$.ClubController.detail(id);
        	} else if(itemType == 11) {
        		$.StarController.detail(id);
        	}
        },
        
        changeSelectItem: function () {
        	$("#itemId").val("");
            $("#itemName").val("");
            $("#itemTitle").val("");
            try {
	        	$("#itemImage1_img").attr("src", noImagePath);
	        	$("#itemImage2_img").attr("src", noImagePath);
            }catch(e) {
            	
            }
            $("#itemStatus").val(0);
        }
    })
});

