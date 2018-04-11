$(function () {
    $.AlbumController = function (elm, config) {

    }

    $.extend($.AlbumController, {

        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                    $.extend($.validationEngineLanguage.allRules, {
                        "ajaxCodeCheck": {
                            "url": contextPath + "/album/album/check?id=" + id,
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
                	
                	
                	$('.fileinput_backgroundImage').on('change.bs.fileinput', function(e, files) {
                		$("#backgroundImage").val("");
                		$("#backgroundImageData").val(files.result);
                	});
                	$('.fileinput_backgroundImage').on('clear.bs.fileinput', function() {
                    	$("#backgroundImage").val("");
                	});
                	
                	$('.fileinput_bgUp').on('change.bs.fileinput', function(e, files) {
                		$("#bgUp").val("");
                		$("#bgUpData").val(files.result);
                	});
                	$('.fileinput_bgUp').on('clear.bs.fileinput', function() {
                    	$("#bgUp").val("");
                	});
                	
                	$('.fileinput_bgDown').on('change.bs.fileinput', function(e, files) {
                		$("#bgDown").val("");
                		$("#bgDownData").val(files.result);
                	});
                	$('.fileinput_bgDown').on('clear.bs.fileinput', function() {
                    	$("#bgDown").val("");
                	});
                	
                	$('.fileinput_bgLeft').on('change.bs.fileinput', function(e, files) {
                		$("#bgLeft").val("");
                		$("#bgLeftData").val(files.result);
                	});
                	$('.fileinput_bgLeft').on('clear.bs.fileinput', function() {
                    	$("#bgLeft").val("");
                	});
                	
                	$('.fileinput_bgRight').on('change.bs.fileinput', function(e, files) {
                		$("#bgRight").val("");
                		$("#bgRightData").val(files.result);
                	});
                	$('.fileinput_bgRight').on('clear.bs.fileinput', function() {
                    	$("#bgRight").val("");
                	});
                	
                	$("#code").val($("#code").val().toUpperCase());
                },
            });
        },

        edit: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();
            json.configItemTypes = $.AlbumController.getConfigItemTypes();

            var bean = {
                data: json,
                image1Data: $("#image1Data").val(),
                image2Data: $("#image2Data").val(),
                backgroundImageData: $("#backgroundImageData").val(),
                bgUpData: $("#bgUpData").val(),
                bgDownData: $("#bgDownData").val(),
                bgLeftData: $("#bgLeftData").val(),
                bgRightData: $("#bgRightData").val(),
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
            json.event = "$.AlbumController._delete('" + path + "')";
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
                url: contextPath + "/album/album/"+id+"/detail",
                success: function () {
                	
                },
            });
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
            json.event = "$.AlbumController.changeStatus('" + path + "')";
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
        		url: contextPath + "/album/"+id+"/albumItem/",
                type: "GET",
                dataType: "text",
                contentType: "default",
                data: {search_albumId__EQ_L: id},
                success: function (data) {
                }
            })
        },
        
        toSelectAlbum: function () {
        	var appCode = $("#appCode").val();
        	$.common.ajaxActionText(contextPath + "/album/album/selectAlbum?appCode="+appCode, function (data) {
        		$("#content_list_container").html(data);
        		$('#content_list_dialog_container').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择专题");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        selectAlbum: function(id, name) {
        	$("#selectAlbumId").val(id);
            $("#selectAlbumName").val(name);
            $("#content_list_modal_container").modal('hide');
        },

        batchToAlbum: function (path) {
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
                        body: "批量编排到专题成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        selectAlbums: function(prefix) {
        	var result = $.AlbumController.getItemIdsAndItemNamesString(prefix);
        	$("#selectAlbumId").val(result.itemIds);
            $("#selectAlbumName").val(result.itemNames);
            $("#content_list_modal_container").modal('hide');
            $('#content_list_modal_container_ok').hide();
        },
        
        changeType: function (type) {
        	var albumType_3 = $("#albumType_3");
        	if(type == 3) {
        		albumType_3.show();
        	} else {
        		albumType_3.hide();
        	}
        },
        
        changeBackgroundWhole: function (type) {
        	var backgroundImage_full_div = $("#backgroundImage_full_div");
        	var backgroundImage_not_full_div = $("#backgroundImage_not_full_div");
        	
        	if (type == 1) {
        		backgroundImage_full_div.show();
        		backgroundImage_not_full_div.hide();
        	} else {
        		backgroundImage_full_div.hide();
        		backgroundImage_not_full_div.show();
        	}
        },
        
        toSelectItem: function (selectMode) {
        	var _selectMode = selectMode || 1;
        	$.common.ajaxActionText(contextPath + "/album/album/selectItem?selectMode=" + _selectMode, function (data) {
        		$("#content_list_container").html(data);
        		$('#content_list_dialog_container').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择专题");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        selectItem: function(selectMode, id, name, title, image1, image2, status) {
        	var _selectMode = selectMode || 1;
        	if(_selectMode == 1) {
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
        	} else if(_selectMode == 2) {
        		$("#jumpItemId").val(id);
	            $("#jumpItemName").val(name);
        	}
            $("#content_list_modal_container").modal('hide');
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
        getBatchData: function (prefix) {
        	var result = "";
        	$('#'+prefix+'content_list tbody tr td input[type=checkbox].checkboxes:checked').each(function (i) {
                if (0 == i) {
                	result = $(this).attr("id");
                } else {
                	result += ("," + $(this).attr("id"));
                }
            });
            return result;
        },
        selectCheckedItem: function(selectMode, prefix) {
        	var params = $.AlbumController.getBatchData(prefix);
        	params = eval("["+params+"]");
        	var trNum = $("#batchTbody").children().length;
        	for (var i = 0; i < params.length; i++) {
        		var itemStatus = "";
        		if (params[i].status == 0) {
        			itemStatus = '<span class="badge badge-info">未上线</span>';
        		} else if(params[i].status == 1) {
        			itemStatus = '<span class="badge badge-success">已上线</span>';
        		} else if(params[i].status == 2) {
        			itemStatus = '<span class="badge badge-danger">已下线</span>';
        		}
        		var sortIndex = (trNum + i + 1);
        		var tr = '<tr>';
        		tr += '<td>';
        		tr += '<input type="hidden" name="itemType" value="3" />';
        		tr += '<input type="hidden" name="itemId" value="' + params[i].id + '" />';
        		tr += params[i].id;
        		tr += '</td>';
        		tr += '<td>';
        		tr += '专题';
        		tr += '</td>';
        		tr += '<td>';
        		tr += params[i].title;
        		tr += '</td>';
        		tr += '<td>';
        		tr += '<input type="text" name="title" class="form-control input-inline" />';
        		tr += '</td>';
        		tr += '<td>';
        		tr += '<input type="hidden" name="itemStatus" value="' + params[i].status + '" />';
        		tr += itemStatus;
        		tr += '</td>';
        		tr += '<td>';
        		tr += '<select name="status" class="form-control input-small input-inline" \>';
        		tr += '<option value="0">未上线</option><option selected="selected" value="1">已上线</option><option value="2">已下线</option>';
        		tr += '</select>';
        		tr += '</td>';
        		tr += '<td style=\"display: none;\">';
        		tr += '<input name="sortIndex" class="form-control input-small input-inline validate[custom[integer]]" value="' + sortIndex + '" />';
        		tr += '</td>';
        		tr += '</tr>';
        		$("#batchTbody").append(tr);
        	}
            $("#content_list_modal_container").modal('hide');
            $('#content_list_modal_container_ok').hide();
        },
    })
});