$(function () {
    $.AlbumItemController = function (elm, config) {

    }

    $.extend(true, $.AlbumItemController, $.BaseController, {

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
                	
                	$('.fileinput_backgroundImage').on('change.bs.fileinput', function(e, files) {
                		$("#backgroundImage").val("");
                		$("#backgroundImageData").val(files.result);
                	});
                	$('.fileinput_backgroundImage').on('clear.bs.fileinput', function() {
                    	$("#backgroundImage").val("");
                	});
                	
                	if (jQuery().datetimepicker) {
        				$(".form_datetime").datetimepicker({
        		            autoclose: true,
        		            isRTL: App.isRTL(),
        		            language : 'zh-CN',
        		            format: "yyyy-mm-dd hh:ii",
        		            pickerPosition: (App.isRTL() ? "bottom-left" : "bottom-left")
        		        });
        			}
                	$.AlbumItemController.onloadUriConfig();
                },
            });
        },

        edit: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var paramList = new Array();
            if ($("#jumpItemType").val() == 6 || $("#itemType").val() == 6) {
            	if (!$("#paramForm").validationEngine("validate")) return false;
            	
            	$.UriController.setAutoCreateItem();
            	
            	var paramData = $("#paramForm").serializeObject();
                var vCount = 0;
                // 计算json内部的数组最大长度
                for (var item in paramData) {
                    var tmp = $.isArray(paramData[item]) ? paramData[item].length : 1;
                    vCount = (tmp > vCount) ? tmp : vCount;
                }

                if (vCount == 1) {
                    paramList.push(paramData);
                } else if (vCount > 1) {
                    for (var i = 0; i < vCount; i++) {
                        var jsonObj = {};

                        for (var item in paramData) {
                            jsonObj[item] = paramData[item][i];
                        }

                        paramList.push(jsonObj);
                    }
                }
            }
            
            $("#itemStatus").removeAttr("disabled");
            $("#jumpItemType").removeAttr("disabled");
            
            var json = $("#editForm").serializeObject();
            json.groupCodes = $.AlbumItemController.getGroupCodes();
            
            var bean = {
                data: json,
                params: paramList,
                image1Data: $("#image1Data").val(),
                image2Data: $("#image2Data").val(),
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
        
        toEditByPosition: function (path, id) {
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
                	
                	$("#position").val($("#input-position").val());
                	
                	$.AlbumItemController.onloadUriConfig();
                },
            });
        },
        
        editByPosition: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var paramList = new Array();
            if ($("#jumpItemType").val() == 6 || $("#itemType").val() == 6) {
            	if (!$("#paramForm").validationEngine("validate")) return false;
            	
            	$.UriController.setAutoCreateItem();
            	
            	var paramData = $("#paramForm").serializeObject();
                var vCount = 0;
                // 计算json内部的数组最大长度
                for (var item in paramData) {
                    var tmp = $.isArray(paramData[item]) ? paramData[item].length : 1;
                    vCount = (tmp > vCount) ? tmp : vCount;
                }

                if (vCount == 1) {
                    paramList.push(paramData);
                } else if (vCount > 1) {
                    for (var i = 0; i < vCount; i++) {
                        var jsonObj = {};

                        for (var item in paramData) {
                            jsonObj[item] = paramData[item][i];
                        }

                        paramList.push(jsonObj);
                    }
                }
            }
            
            $("#itemStatus").removeAttr("disabled");
            $("#jumpItemType").removeAttr("disabled");
            
            var json = $("#editForm").serializeObject();
            json.groupCodes = $.AlbumItemController.getGroupCodes();
            
            var title = $('#itemType option:selected').text();
        	var text = $('#title').val();
			if (text == '') {
				text = $('#itemTitle').val();
			}
            var positionJson = eval("(" + json.position + ")");
            positionJson.title = title;
            positionJson.text = text;
            json.position = JSON.stringify(positionJson);
            
            var bean = {
                data: json,
                params: paramList,
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
                	var title = $('#itemType option:selected').text();
                	var text = $('#title').val();
        			if(text == '') {
        				text = $('#itemTitle').val();
        			}
        			annotator_canvas_update_item(title, text);
                	$.common.hideModal();
                }
            })
        },

        editByPositionFromMove: function (path, json) {
            var bean = {
                data: json,
            };
            
            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: bean,
                success: function (data) {
                	$.common.hideModal();
                }
            })
        },
        
        toDeleteByPositionBeforeNotSave: function () {
        	annotator_canvas_delete_item();
            $.common.hideModal();
        },
        
        toDeleteByPosition: function (path, name) {
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除[" + name + "]吗?";
            json.event = "$.AlbumItemController._deleteByPosition('" + path + "')";
            $.common.showConfirmModal(json);
        },

        _deleteByPosition: function (path) {
            $.common.ajaxActionText(path, function (data) {
            	annotator_canvas_delete_item();
                $.common.hideModal();
            })
        },
        
        toEditByPositionFromAnnotator: function (positionId) {
	        var albumId = $("#albumId").val();
			$.AlbumItemController.toEditByPosition(contextPath + '/album/' + albumId + '/albumItem/editByPosition?positionId='
					+ positionId + '', -1);
        },
        
        editByPositionFromAnnotator: function (json) {
        	var albumId = $("#albumId").val();
        	$.AlbumItemController.editByPositionFromMove(contextPath + '/album/' + albumId + '/albumItem/editByPosition', json);
        },
        
        toDelete: function (path, name) {
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除[" + name + "]吗?";
            json.event = "$.AlbumItemController._delete('" + path + "')";
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
            json.event = "$.AlbumItemController.changeStatus('" + path + "')";
            $.common.showConfirmModal(json);
        },
        
        changeStatus: function (path) {
        	$.common.ajaxActionText(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
            })
        },

        
        
        
        
        onloadUriConfig: function () {
        	if ($("#jumpItemType").val() == 6 && $("#jumpItemId").val() != '') {
        		$.UriController.loadUriConfigById(2, $("#jumpItemId").val());
            } else if ($("#itemType").val() == 6 && $("#itemId").val() != '') {
            	$.UriController.loadUriConfigById(1, $("#itemId").val());
            }
        },
        
        changeInternalParamCategory: function (value) {
        	$("#internalParamCategory").val(value);
        	if ($("#jumpItemType").val() == 6 && $("#jumpItemId").val() != '' && $("#itemType").val() != 6 && $("#itemId").val() != '') {
            	$.UriController.loadUriConfigById(2, $("#jumpItemId").val(), $("#itemType").val(), $("#itemId").val());
            } else if ($("#jumpItemType").val() == 6 && $("#jumpItemId").val() != '') {
        		$.UriController.loadUriConfigById(2, $("#jumpItemId").val());
            } else if ($("#itemType").val() == 6 && $("#itemId").val() != '') {
            	$.UriController.loadUriConfigById(1, $("#itemId").val());
            }
        },
        
        changeItemIdValue: function () {
        	if ($("#jumpItemType").val() == 6 && $("#jumpItemId").val() != '' && $("#itemType").val() != 6 && $("#itemId").val() != '') {
            	$.UriController.loadUriConfigById(2, $("#jumpItemId").val(), $("#itemType").val(), $("#itemId").val());
            }
        },
        
        changeSelectItem: function () {
        	$("#itemId").val("");
            $("#itemName").val("");
            $("#itemTitle").val("");
        	$("#itemImage1_img").attr("src", noImagePath);
        	$("#itemImage2_img").attr("src", noImagePath);
            $("#itemStatus").val(0);
            
            if ($("#jumpItemType").val() == 6 && $("#jumpItemId").val() != '') {
            	
            } else {
	            var uriParamConfig_div = $("#uriParamConfig_div");
	        	var backgroundImage_div = $("#backgroundImage_div");
	        	uriParamConfig_div.hide();
	    		backgroundImage_div.hide();
            }
            
            var itemType = $("#itemType").val();
            if (itemType == 5 || itemType == 9 || itemType == 10 || itemType == 11) {
            	if ($("#jumpItemType").val() != 6) {
            		$("#jumpItemType").val(6).change();
            		$("#jumpItemType").attr("disabled", "disabled");
            	}
            } else {
            	$("#jumpItemType").removeAttr("disabled");
            	if ($("#jumpItemType").val() == 6) {
            		$("#jumpItemType").val(0).change();
            	}
            }
        },
        
        
        
        changeSelectJumpItem: function () {
        	var jumpItemType = $("#jumpItemType").val();
        	if (jumpItemType == 0) {
        		$("#jump_item_id").hide();
        		$("#uri_url").hide();
        	} else if (jumpItemType == 99) {
        		$("#jump_item_id").hide();
        		$("#uri_url").show();
        	} else {
        		$("#jump_item_id").show();
        		$("#uri_url").hide();
        	}
        	// 清空值
            $("#jumpItemId").val("");
        	$("#jumpItemName").val("");
        	
            if ($("#jumpItemType").val() == 0 && $("#itemType").val() == 6 && $("#itemId").val() != '') {
            	$.UriController.loadUriConfigById(1, $("#itemId").val());
            } else {
	            var uriParamConfig_div = $("#uriParamConfig_div");
	        	var backgroundImage_div = $("#backgroundImage_div");
	        	uriParamConfig_div.hide();
	    		backgroundImage_div.hide();
            }
        },
        
        getGroupCodes: function () {
            var groupCodes = "";
            $('input:checkbox[name=groupCodes]:checked').each(function (i) {
                if (0 == i) {
                    groupCodes = $(this).val();
                } else {
                    groupCodes += ("," + $(this).val());
                }
            });
            return groupCodes;
        },
        
        toSelectItemFromBatchAdd: function () {
        	var itemType = $("#itemType").val();
        	if (itemType == 1) {
        		$.ProgramController.toSelectItem('multi');
        	} else if (itemType == 2) {
        		$.SeriesController.toSelectItem('multi');
        	} else if (itemType == 3) {
        		$.AlbumController.toSelectItem('multi');
        	} else if (itemType == 13) {
        		$.LeagueMatchController.toSelectItem('multi');
        	}
        },
        
        toBatchAdd: function (path, id) {
    		if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                	$("#editForm").validationEngine({
                        ajaxFormValidationMethod: 'post',
                    });
                },
            });
        },
        
        batchAdd: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;
            
            var json = {};
            var fileJsonData = $("#editForm").serializeObject();
            var vCount = 0;
            // 计算json内部的数组最大长度
            for (var item in fileJsonData) {
                var tmp = $.isArray(fileJsonData[item]) ? fileJsonData[item].length : 1;
                vCount = (tmp > vCount) ? tmp : vCount;
            }

            if (vCount == 1) {
                for (var item in fileJsonData) {
                	var jsonArr = new Array();
                	jsonArr[0] = fileJsonData[item];
                	json[item] = jsonArr;
                }
            } else if (vCount > 1) {
            	json = fileJsonData;
            }
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
    })
});

