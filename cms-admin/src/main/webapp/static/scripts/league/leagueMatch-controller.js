$(function () {
    $.LeagueMatchController = function (elm, config) {

    }

    $.extend($.LeagueMatchController, {

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
                	
                	$.LeagueMatchController.initTimepicker();
                },
            });
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

			$.LeagueMatchController.initBeginAndEndDate();
		},
        
        initBeginAndEndDate : function() {
		},
        
        changeBeginDate : function() {
		},

		changeEndDate : function() {
		},

		changeHomeName: function (obj) {
            $("#homeId").val("");
            $("#homeType").val("");
        },
        changeGuestName: function (obj) {
            $("#guestId").val("");
            $("#guestType").val("");
        },
        
        changeChannelId: function (obj) {
        	$("#scheduleId").val("");
            $("#scheduleProgramName").val("");
        },
        
		genSearchName : function(obj) {
			$.LeagueMatchController.genSearchNamePinyin(obj.value.trim());
		},

		genSearchNamePinyin : function(name) {
			if (name == "") {
				return;
			}
			var pinyinResult = pinyin.getCamelChars(name.replace(/\s/g, ""));
			$("#searchName").val(pinyinResult);
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
            json.event = "$.LeagueMatchController._delete('" + path + "')";
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
                url: contextPath + "/league/leagueMatch/"+id+"/detail",
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
        
        detailJson: function (id) {
        	$.common.showModal({
                url: contextPath + "/league/leagueMatch/"+id+"/detailJson",
                success: function () {
                	
                },
            });
        },
        
        toChangeStatus: function (path, name, statusMethodDesc) {
        	var json = {};
            json.title = statusMethodDesc + "操作";
            json.body = "您确认要" + statusMethodDesc + "[" + name + "]吗?";
            json.event = "$.LeagueMatchController.changeStatus('" + path + "')";
            $.common.showConfirmModal(json);
        },
        
        changeStatus: function (path) {
        	$.common.ajaxActionText(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
            })
        },        
        
        changeSplitProgram: function (type) {
        	var leagueMatch_div_1 = $("#leagueMatch_div_1");
        	
        	if(type == 0) {
        		leagueMatch_div_1.hide();
        	} else if(type == 1) {
        		leagueMatch_div_1.show();
        	}
        },
        
        changePointStatus: function (type) {
        	var leagueMatch_div_2 = $("#leagueMatch_div_2");
        	
        	if(type == 0) {
        		leagueMatch_div_2.hide();
        	} else if(type == 1) {
        		leagueMatch_div_2.show();
        	}
        },
        
        clearChannel: function () {				
        	$("#channelCode").val("");
        	$("#channelId").val("");
        	$("#channelName").val("");
        	$("#scheduleId").val("");
        	$("#scheduleProgramName").val("");
        },
        
        clearChannelProgram: function () {
        	$("#scheduleId").val("");
        	$("#scheduleProgramName").val("");
        },
        
        clearSplitProgram: function () {					
        	$("#mediaId").val("");
        	$("#mediaEpisode").val("");
        	$("#programId").val("");
        	$("#programName").val("");
        },
        
        
        toSelectLeagueMatch: function () {
        	$.common.ajaxActionText(contextPath + "/league/leagueMatch/selectLeagueMatch", function (data) {
        		$("#content_list_modal_container_body").html(data);
        		$('#content_list_modal_container_dialog').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择赛事");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        selectLeagueMatch: function(id, title, code, contentType, duration) {
        	$("#leagueMatchId").val(id);
            $("#leagueMatchName").val(title);
            $("#leagueMatchCode").val(code);
            try {
            	$("#contentType").val(contentType).change();
            	var liveLeagueMatchName = $("#liveLeagueMatchName").val();
            	if(liveLeagueMatchName == '') {
            		$("#liveLeagueMatchName").val(title).change();
            	}
            	var liveDuration = $("#liveDuration").val();
            	if(liveDuration == '') {
            		$("#liveDuration").val(duration).change();
            	}
            } catch (e) {
            	
            }
            $("#content_list_modal_container").modal('hide');
        },
        
        toSelectItem: function (selectMode) {
        	var _selectMode = selectMode || 1;
        	$.common.ajaxActionText(contextPath + "/league/leagueMatch/selectItem?selectMode=" + _selectMode, function (data) {
        		$("#content_list_modal_container_body").html(data);
        		$('#content_list_modal_container_dialog').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择赛事");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        selectItem: function(id, name, title, image1, image2, status, duration) {
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
            try {
            	$("#itemDuration").val(duration);
            } catch(e) {
            	
            }
            $("#content_list_modal_container").modal('hide');
        },
        
        init: function (prefix) {
//            var table = $('#content_list');
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
        
        getItemIds: function () {
            var itemIds = [];
            $('#content_list tbody tr td input[type=checkbox].checkboxes:checked').each(function () {
                itemIds.push($(this).val());
            });
            return itemIds;
        },
        
        getItemIdsString: function () {
        	var itemIds = "";
        	$('#content_list tbody tr td input[type=checkbox].checkboxes:checked').each(function (i) {
                if (0 == i) {
                	itemIds = $(this).val();
                } else {
                	itemIds += ("," + $(this).val());
                }
            });
            return itemIds;
        },

        batchTo: function (path, itemType) {
            var itemIds = $.LeagueMatchController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
                var json = {
                    body: "请选择赛事！",
                };
                $.common.showAlertModal(json);
                return;
            }

            var requestPath = path + "?itemType=" + itemType + "&itemIds=" + itemIds;

            $.common.showModal({
                url: requestPath,
                type: "POST",
                dataType: "text",
                contentType: "default",
                success: function () {
                },
            });
        },
        
        toBatchChangeMetadata: function (path, itemType) {
            var itemIds = $.LeagueMatchController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
                var json = {
                    body: "请选择赛事！",
                };
                $.common.showAlertModal(json);
                return;
            }

            var requestPath = path + "?itemType=" + itemType + "&itemIds=" + itemIds;

            $.common.showModal({
                url: requestPath,
                type: "POST",
                dataType: "text",
                contentType: "default",
                success: function () {
                    $('.make-switch').bootstrapSwitch();
                    $('input[name="cpIdSwitch"]').on('switchChange.bootstrapSwitch', function (event, state) {
                        if (state) {
                            $("#cpIdFlag").val(1);
                            $("#cpId").removeAttr("disabled");
                        } else {
                            $("#cpIdFlag").val(0);
                            $("#cpId").attr("disabled", "disabled");
                        }
                    });
                    $('input[name="tagSwitch"]').on('switchChange.bootstrapSwitch', function (event, state) {
                        if (state) {
                            $("#tagFlag").val(1);
                            $("#tag").removeAttr("disabled");
                        } else {
                            $("#tagFlag").val(0);
                            $("#tag").attr("disabled", "disabled");
                        }
                    });
                    $('input[name="keywordSwitch"]').on('switchChange.bootstrapSwitch', function (event, state) {
                        if (state) {
                            $("#keywordFlag").val(1);
                            $("#keyword").removeAttr("disabled");
                        } else {
                            $("#keywordFlag").val(0);
                            $("#keyword").attr("disabled", "disabled");
                        }
                    });
                },
            });
        },
        
        batchChangeMetadata: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();

            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: json,
                success: function (data) {
                    $.common.hideModal();
                    $.Page.refreshCurrentPage();
                    var json = {
                        body: "批量修改元数据成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        batchChangeStatus: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();

            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: json,
                success: function (data) {
                    $.common.hideModal();
                    $.Page.refreshCurrentPage();
                    var json = {
                        body: "上线成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        toBatchOffline: function (path, itemType) {
        	var itemIds = $.LeagueMatchController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
                var json = {
                    body: "请选择赛事！",
                };
                $.common.showAlertModal(json);
                return;
            }
            
            var json = {};
            json.title = "下线操作";
            json.body = "您确认要下线吗?";
            json.event = "$.LeagueMatchController.batchChangeStatusOffline('" + path + "'," + itemType + ")";
            $.common.showConfirmModal(json);
        },
        
        batchChangeStatusOffline: function (path, itemType) {
        	var itemIds = $.LeagueMatchController.getItemIdsString();
            
            var json = {};
            json.itemType = itemType;
            json.itemIds = itemIds;
            json.status = 2;

            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: json,
                success: function (data) {
                    $.common.hideModal();
                    $.Page.refreshCurrentPage();
                    var json = {
                        body: "下线成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        toBatchOnline: function (path, itemType) {
        	var itemIds = $.LeagueMatchController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
                var json = {
                    body: "请选择赛事！",
                };
                $.common.showAlertModal(json);
                return;
            }
            
            var json = {};
            json.title = "上线操作";
            json.body = "您确认要上线吗?";
            json.event = "$.LeagueMatchController.batchChangeStatusOnline('" + path + "'," + itemType + ")";
            $.common.showConfirmModal(json);
        },
        
        batchChangeStatusOnline: function (path, itemType) {
        	var itemIds = $.LeagueMatchController.getItemIdsString();
            
            var json = {};
            json.itemType = itemType;
            json.itemIds = itemIds;
            json.status = 1;

            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: json,
                success: function (data) {
                    $.common.hideModal();
                    $.Page.refreshCurrentPage();
                    var json = {
                        body: "上线成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        toChangeStatusOnline: function (path, itemType, itemIds) {
            var requestPath = path + "?itemType=" + itemType + "&itemIds=" + itemIds;

            $.common.showModal({
                url: requestPath,
                type: "GET",
                dataType: "text",
                contentType: "default",
                success: function () {
                	$.common.hideModal();
                    $.Page.refreshCurrentPage();
                    var json = {
                        body: "上线成功！",
                    };
                    $.common.showAlertModal(json);
                },
            });
        },
        
        toBatchDelete: function (path, itemType) {
        	var itemIds = $.LeagueMatchController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
                var json = {
                    body: "请选择赛事！",
                };
                $.common.showAlertModal(json);
                return;
            }
            
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除吗?";
            json.event = "$.LeagueMatchController.batchDelete('" + path + "'," + itemType + ")";
            $.common.showConfirmModal(json);
        },
        
        batchDelete: function (path, itemType) {
        	var itemIds = $.LeagueMatchController.getItemIdsString();
            
            var json = {};
            json.itemType = itemType;
            json.itemIds = itemIds;
            json.status = 1;

            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: json,
                success: function (data) {
                    $.common.hideModal();
                    $.Page.refreshCurrentPage();
                    var json = {
                        body: "删除成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
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
        	var params = $.LeagueMatchController.getBatchData(prefix);
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
        		tr += '<input type="hidden" name="itemType" value="13" />';
        		tr += '<input type="hidden" name="itemId" value="' + params[i].id + '" />';
        		tr += params[i].id;
        		tr += '</td>';
        		tr += '<td>';
        		tr += '赛事';
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
        		tr += '<td>';
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



