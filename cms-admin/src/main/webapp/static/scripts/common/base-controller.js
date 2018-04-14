$(function () {
    
	$.BaseController = function (elm, config) {

    }

    $.extend($.BaseController, {
    	
    	initTimepicker: function() {
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
					}
				);
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
					}
				);
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
		
		initImage: function(id) {
			$('.fileinput_' + id).on('change.bs.fileinput', function(e, files) {
            	$("#" + id + "").val("");
            	$("#" + id + "Data").val(files.result);
        	});
            $('.fileinput_' + id).on('clear.bs.fileinput', function() {
            	$("#" + id + "").val("");
        	});
		},
		
		initSwitch: function(switchId, objectId) {
			var _objectId = objectId || switchId;
			$('input[name="' + switchId + 'Switch"]').on('switchChange.bootstrapSwitch', function (event, state) {
	            if (state) {
	                $("#" + _objectId + "").removeAttr("disabled");
	            } else {
	                $("#" + _objectId + "").attr("disabled", "disabled");
	            }
	        });
		},
    	
    	initCpSearch: function (prefix) {
    		var _prefix = prefix || "";
    		$('#' + _prefix + 'select_cpId__INMASK_S').multiselect({
            	enableFiltering: true,
            	nonSelectedText:'请选择',
            	filterPlaceholder:'搜索',
            	nSelectedText:'项被选中',
            	includeSelectAllOption:false,
            	checkboxName:'select_cpId__INMASK_S',
            	selectAllText:'全选/取消全选',
            	allSelectedText:'已选中所有',
            });
            $('#' + _prefix + 'select_cpId__INMASK_S').bind("change",function() {
            	var value = $('#' + _prefix + 'select_cpId__INMASK_S').val();
            	$('#' + _prefix + 'search_cpId__INMASK_S').val(value);
            });
            $("input:checkbox[name=" + 'select_cpId__INMASK_S' + "]").uniform();
    	},
    
    	initCpSelect: function (prefix) {
    		var _prefix = prefix || "";
    		$('#' + _prefix + 'select_cpId').multiselect({
            	enableFiltering: true,
            	nonSelectedText:'请选择',
            	filterPlaceholder:'搜索',
            	nSelectedText:'项被选中',
            	includeSelectAllOption:false,
            	checkboxName:'select_cpId',
            	selectAllText:'全选/取消全选',
            	allSelectedText:'已选中所有',
            });
            $('#' + _prefix + 'select_cpId').bind("change",function() {
            	var value = $('#' + _prefix + 'select_cpId').val();
            	$('#' + _prefix + 'cpId').val(value);
            });
            $("input:checkbox[name=" + 'select_cpId' + "]").uniform();
    	},
    	
    	initMediaTemplateSelect: function (prefix) {
    		var _prefix = prefix || "";
    		$('#' + _prefix + 'select_templateId').multiselect({
            	enableFiltering: true,
            	nonSelectedText:'请选择',
            	filterPlaceholder:'搜索',
            	nSelectedText:'项被选中',
            	includeSelectAllOption:false,
            	checkboxName:'select_templateId',
            	selectAllText:'全选/取消全选',
            	allSelectedText:'已选中所有',
            });
            $('#' + _prefix + 'select_templateId').bind("change",function() {
            	var value = $('#' + _prefix + 'select_templateId').val();
            	$('#' + _prefix + 'templateId').val(value);
            });
            $("input:checkbox[name=" + 'select_templateId' + "]").uniform();
    	},
    	
        initTableCheckBox: function (tableId) {
        	var _tableId = tableId || "";
            var table = $('#' + _tableId + "content_list");

            table.find('.group-checkable').change(function () {
                var set = jQuery(this).attr("data-set");
                var checked = jQuery(this).is(":checked");
                
                jQuery(set).each(function () {
                    if (checked) {
                    	var disabled = jQuery(this).is(":disabled");
                    	if (!disabled) {
                    		$(this).prop('checked', true);
                    		$(this).parents('tr').addClass("active");
                    	}
                    } else {
                        $(this).prop('checked', false);
                        $(this).parents('tr').removeClass("active");
                    }
                });
                if (checked) {
                    $(this).parents('table').find('input[type=checkbox].group-checkable').parents('span').addClass("checked");
                } else {
                    $(this).parents('table').find('input[type=checkbox].group-checkable').parents('span').removeClass("checked");
                }
                $(this).parents('table').find('input[type=checkbox].group-checkable').prop('checked', checked);
                jQuery.uniform.update(set);
            });
            
            table.on('change', 'tbody tr .checkboxes', function () {
                $(this).parents('tr').toggleClass("active");
            });
            $("input:checkbox[class='checkboxes']").uniform();
            $("input:checkbox[class='group-checkable']").uniform();
        },
        
        getCheckboxValue: function (name) {
            var result = "";
            $('input:checkbox[name=' + name + ']:checked').each(function (i) {
                if (0 == i) {
                    result = $(this).val();
                } else {
                    result += ("," + $(this).val());
                }
            });
            return result;
        },
        
        getItemIds: function (tableId) {
        	var _tableId = tableId || "";
            var itemIds = [];
            $('#' + _tableId + 'content_list tbody tr td input[type=checkbox].checkboxes:checked').each(function () {
                itemIds.push($(this).val());
            });
            return itemIds;
        },
        
        getItemIdsString: function (tableId) {
        	var _tableId = tableId || "";
        	var itemIds = "";
        	$('#' + _tableId + 'content_list tbody tr td input[type=checkbox].checkboxes:checked').each(function (i) {
                if (0 == i) {
                	itemIds = $(this).val();
                } else {
                	itemIds += ("," + $(this).val());
                }
            });
            return itemIds;
        },
        
        itemDetail: function (itemType, id) {
        	if (itemType == 1) {
        		$.ProgramController.detail(id);
        	} else if (itemType == 2) {
        		$.SeriesController.detail(id);
        	} else if (itemType == 3) {
        		$.AlbumController.detail(id);
        	} else if (itemType == 5) {
        		$.CategoryController.detail(id);
        	} else if (itemType == 6) {
        		$.UriController.detail(id);
        	} else if (itemType == 7) {
        		$.ChannelController.detail(id);
        	} else if (itemType == 8) {
        		$.ScheduleController.detail(id);
        	} else if (itemType == 9) {
        		$.StarController.detail(id);
        	} else if (itemType == 10) {
        		$.ClubController.detail(id);
        	} else if (itemType == 11) {
        		$.LeagueController.detail(id);
        	} else if (itemType == 12) {
        		$.LeagueSeasonController.detail(id);
        	} else if (itemType == 13) {
        		$.LeagueMatchController.detail(id);
        	}
        },
        
        toSelectItem: function () {
        	var itemType = $("#itemType").val();
        	if (itemType == 1) {
        		$.ProgramController.toSelectItem();
        	} else if (itemType == 2) {
        		$.SeriesController.toSelectItem();
        	} else if (itemType == 3) {
        		$.AlbumController.toSelectItem();
        	} else if (itemType == 5) {
        		$.CategoryController.selectCategoryItemTree();
        	} else if (itemType == 6) {
        		$.UriController.toSelectItem();
        	} else if (itemType == 7) {
        		$.ChannelController.toSelectItem();
        	} else if (itemType == 8) {
        		$.ScheduleController.toSelectItem();
        	} else if (itemType == 9) {
        		$.StarController.toSelectItem();
        	} else if (itemType == 10) {
        		$.ClubController.toSelectItem();
        	} else if (itemType == 11) {
        		$.LeagueController.toSelectItem();
        	} else if (itemType == 12) {
        		$.LeagueSeasonController.toSelectItem();
        	} else if (itemType == 13) {
        		$.LeagueMatchController.toSelectItem();
        	}
        },
        
        toSelectJumpItem: function () {
        	var itemType = $("#jumpItemType").val();
        	if (itemType == 1) {
        		$.ProgramController.toSelectItem('jump');
        	} else if (itemType == 2) {
        		$.SeriesController.toSelectItem('jump');
        	} else if (itemType == 3) {
        		$.AlbumController.toSelectItem('jump');
        	} else if (itemType == 5) {
        		$.CategoryController.selectCategoryItemTree('jump');
        	} else if (itemType == 6) {
        		$.UriController.toSelectItem('jump');
        	} else if (itemType == 7) {
        		$.ChannelController.toSelectItem('jump');
        	} else if (itemType == 8) {
        		$.ScheduleController.toSelectItem('jump');
        	} else if (itemType == 9) {
        		$.StarController.toSelectItem('jump');
        	} else if (itemType == 10) {
        		$.ClubController.toSelectItem('jump');
        	} else if (itemType == 11) {
        		$.LeagueController.toSelectItem('jump');
        	} else if (itemType == 12) {
        		$.LeagueSeasonController.toSelectItem('jump');
        	} else if (itemType == 13) {
        		$.LeagueMatchController.toSelectItem('jump');
        	}
        },
        
        toDelete: function (path, name) {
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除[" + name + "]吗?";
            json.event = "$.BaseController._delete('" + path + "')";
            $.common.showConfirmModal(json);
        },

        _delete: function (path) {
            $.common.ajaxActionJson(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
                var alertMessage = data.message || "删除成功！";
                var json = {
                    body: alertMessage,
                };
                $.common.showAlertModal(json);
            })
        },
        
        toOnline: function (path, name) {
        	var json = {};
            json.title = "上线操作";
            json.body = "您确认要上线[" + name + "]吗?";
            json.event = "$.BaseController.online('" + path + "')";
            $.common.showConfirmModal(json);
        },
        
        online: function (path) {
        	$.common.ajaxActionJson(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
                var alertMessage = data.message || "上线成功！";
                var json = {
                    body: alertMessage,
                };
                $.common.showAlertModal(json);
            })
        },
        
        toOffline: function (path, name) {
        	var json = {};
            json.title = "下线操作";
            json.body = "您确认要下线[" + name + "]吗?";
            json.event = "$.BaseController.offline('" + path + "')";
            $.common.showConfirmModal(json);
        },
        
        offline: function (path) {
        	$.common.ajaxActionJson(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
                var alertMessage = data.message || "下线成功！";
                var json = {
                    body: alertMessage,
                };
                $.common.showAlertModal(json);
            })
        },
        
        toChangeStatus: function (path, name, statusMethodDesc) {
        	var json = {};
            json.title = statusMethodDesc + "操作";
            json.body = "您确认要" + statusMethodDesc + "[" + name + "]吗?";
            json.event = "$.BaseController.changeStatus('" + path + "', '"+statusMethodDesc+"')";
            $.common.showConfirmModal(json);
        },
        
        changeStatus: function (path, statusMethodDesc) {
        	$.common.ajaxActionJson(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
                var alertMessage = data.message || statusMethodDesc + "成功！";
                var json = {
                    body: alertMessage,
                };
                $.common.showAlertModal(json);
            })
        },
        
        getItemTypeName: function (itemType) {
        	var typeName = "";
        	if (itemType == 1) {
        		typeName = "节目";
        	} else if (itemType == 101) {
        		typeName = "媒体内容";
        	} else if (itemType == 2) {
        		typeName = "剧头";
        	} else if (itemType == 3) {
        		typeName = "专题";
        	} else if (itemType == 4) {
        		typeName = "推荐位";
        	} else if (itemType == 5) {
        		typeName = "栏目";
        	} else if (itemType == 103) {
        		typeName = "专题项";
        	} else if (itemType == 104) {
        		typeName = "推荐位项";
        	} else if (itemType == 105) {
        		typeName = "栏目项";
        	} else if (itemType == 6) {
        		typeName = "页面";
        	} else if (itemType == 7) {
        		typeName = "频道";
        	} else if (itemType == 8) {
        		typeName = "节目单";
        	} else if (itemType == 9) {
        		typeName = "明星";	
        	} else if (itemType == 10) {
        		typeName = "俱乐部";
        	} else if (itemType == 11) {
        		typeName = "联赛";	
        	} else if (itemType == 12) {
        		typeName = "赛季";
        	} else if (itemType == 13) {
        		typeName = "赛事";
        	} else if (itemType == 1101) {
        		typeName = "转码工单";
        	} else if (itemType == 1102) {
        		typeName = "转码任务";
        	} else {
        		typeName = "元素";
        	}
        	return typeName;
        },
        
        getItemTypeActionDesc: function (itemType, action) {
        	var typeName = $.BaseController.getItemTypeName(itemType);
        	var result = "";
        	if (action && (action == "delete" || action == "batchDelete")) {
        		result += "（只能删除非上线的" + typeName + "）";
        	}
        	return result;
        },
        
        batchTo: function (path, itemType, success) {
        	$.BaseController.toBatch(path, itemType, success);
        },
        
        toBatch: function (path, itemType, success) {
        	var itemIds = $.BaseController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
            	var alertMessage = "请选择" + $.BaseController.getItemTypeName(itemType) + "!";
                var json = {
                    body: alertMessage,
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
                success: success,
            });
        },
        
        batch: function (path, defaultPrompt) {
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
                    var alertMessage = data.message || defaultPrompt;
                    if (alertMessage && alertMessage != "") {
	                    var json = {
	                        body: alertMessage,
	                    };
	                    $.common.showAlertModal(json);
                    }
                }
            });
        },
        
        toBatchOnline: function (path, itemType) {
        	var itemIds = $.BaseController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
            	var alertMessage = "请选择" + $.BaseController.getItemTypeName(itemType) + "!";
                var json = {
                    body: alertMessage,
                };
                $.common.showAlertModal(json);
                return;
            }
            
            var json = {};
            json.title = "批量上线操作";
            json.body = "您确认要批量上线吗?";
            json.event = "$.BaseController.batchOnline('" + path + "'," + itemType + ")";
            $.common.showConfirmModal(json);
        },
        
        batchOnline: function (path, itemType) {
        	var itemIds = $.BaseController.getItemIdsString();
            
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
                    var alertMessage = data.message || "批量上线成功！";
                    var json = {
                        body: alertMessage,
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        toBatchOffline: function (path, itemType) {
        	var itemIds = $.BaseController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
            	var alertMessage = "请选择" + $.BaseController.getItemTypeName(itemType) + "!";
                var json = {
                    body: alertMessage,
                };
                $.common.showAlertModal(json);
                return;
            }
            
            var json = {};
            json.title = "批量下线操作";
            json.body = "您确认要批量下线吗?";
            json.event = "$.BaseController.batchOffline('" + path + "'," + itemType + ")";
            $.common.showConfirmModal(json);
        },
        
        batchOffline: function (path, itemType) {
        	var itemIds = $.BaseController.getItemIdsString();
            
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
                    var alertMessage = data.message || "批量下线成功！";
                    var json = {
                        body: alertMessage,
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        toBatchSubmit: function (path, itemType) {
        	var itemIds = $.BaseController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
            	var alertMessage = "请选择" + $.BaseController.getItemTypeName(itemType) + "!";
                var json = {
                    body: alertMessage,
                };
                $.common.showAlertModal(json);
                return;
            }
            
            var json = {};
            json.title = "批量送审操作";
            json.body = "您确认要批量送审吗?";
            json.event = "$.BaseController.batchSubmit('" + path + "'," + itemType + ")";
            $.common.showConfirmModal(json);
        },
        
        batchSubmit: function (path, itemType) {
        	var itemIds = $.BaseController.getItemIdsString();
            
            var json = {};
            json.itemType = itemType;
            json.itemIds = itemIds;

            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: json,
                success: function (data) {
                    $.common.hideModal();
                    $.Page.refreshCurrentPage();
                    var alertMessage = data.message || "批量送审成功！";
                    var json = {
                        body: alertMessage,
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        toBatchDelete: function (path, itemType) {
        	var itemIds = $.BaseController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
            	var alertMessage = "请选择" + $.BaseController.getItemTypeName(itemType) + "!" + $.BaseController.getItemTypeActionDesc(itemType, 'batchDelete');
                var json = {
                    body: alertMessage,
                };
                $.common.showAlertModal(json);
                return;
            }
            
            var json = {};
            json.title = "批量删除操作";
            json.body = "您确认要批量删除吗?" + $.BaseController.getItemTypeActionDesc(itemType, 'batchDelete');
            json.event = "$.BaseController.batchDelete('" + path + "'," + itemType + ")";
            $.common.showConfirmModal(json);
        },
        
        batchDelete: function (path, itemType) {
        	var itemIds = $.BaseController.getItemIdsString();
            
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
                    var alertMessage = data.message || "批量删除成功！";
                    var json = {
                        body: alertMessage,
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        batchType: function (path) {
        	$.BaseController.batch(path, "批量修改类型成功！");
        },

        batchChangeMetadata: function (path) {
        	$.BaseController.batch(path, "批量修改元数据成功！");
        },

        batchAudit: function (path) {
        	$.BaseController.batch(path, "批量审核成功！");
        },
        
        batchChangeStatus: function (path) {
        	$.BaseController.batch(path, "批量修改状态成功！");
        },
        
        batchChangeTitle: function (path) {
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
        
        toBatchInjection: function (path, itemType) {
        	var itemIds = $.BaseController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
            	var alertMessage = "请选择" + $.BaseController.getItemTypeName(itemType) + "!";
                var json = {
                    body: alertMessage,
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
                success: function() {
                	$("input:checkbox[name='platformId']").uniform();
                	$("input:checkbox[name='platformId']").each(function (i) {
                		var platformId = $(this).val();
                		$.BaseController.initMediaTemplateSelect(platformId);
                		var checked = jQuery(this).is(":checked");
                        if (checked) {
                            $('#' + platformId + 'templateId_parent').show();
                            $('#' + platformId + 'templateId').removeAttr("disabled");
                            $('#' + platformId + 'priority').removeAttr("disabled");
                        }
                    });
                	$("input:checkbox[name='platformId']").change(function () {
                		var platformId = $(this).val();
                        var checked = jQuery(this).is(":checked");
                        if (checked) {
                            $('#' + platformId + 'templateId_parent').show();
                            $('#' + platformId + 'templateId').removeAttr("disabled");
                            $('#' + platformId + 'priority').removeAttr("disabled");
                        } else {
                        	$('#' + platformId + 'templateId_parent').hide();
                            $('#' + platformId + 'templateId').attr("disabled", "disabled");
                        	$('#' + platformId + 'priority').attr("disabled", "disabled");
                        }
                	});
                },
            });
        },
        
        batchInjection: function (path, defaultPrompt) {
            if (!$("#editForm").validationEngine("validate")) return false;
            
            var json = $("#editForm").serializeObject();
            
            if (!$.isArray(json['platformId'])) {
            	var jsonArr = new Array();
            	jsonArr[0] = json['platformId'];
            	json['platformId'] = jsonArr;
            }
            if (!$.isArray(json['templateId'])) {
            	var jsonArr = new Array();
            	jsonArr[0] = json['templateId'];
            	json['templateId'] = jsonArr;
            }
            if (!$.isArray(json['priority'])) {
            	var jsonArr = new Array();
            	jsonArr[0] = json['priority'];
            	json['priority'] = jsonArr;
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
                    var alertMessage = data.message || defaultPrompt;
                    if (alertMessage && alertMessage != "") {
	                    var json = {
	                        body: alertMessage,
	                    };
	                    $.common.showAlertModal(json);
                    }
                }
            });
        },
        
        toResetInjectionStatus: function (path, itemType) {
        	var itemIds = $.BaseController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
            	var alertMessage = "请选择" + $.BaseController.getItemTypeName(itemType) + "!";
                var json = {
                    body: alertMessage,
                };
                $.common.showAlertModal(json);
                return;
            }
            
            var json = {};
            json.title = "重置分发状态操作";
            json.body = "您确认要重置分发状态吗?";
            json.event = "$.BaseController.resetInjectionStatus('" + path + "'," + itemType + ")";
            $.common.showConfirmModal(json);
        },
        
        resetInjectionStatus: function (path, itemType) {
        	var itemIds = $.BaseController.getItemIdsString();
            
            var json = {};
            json.itemType = itemType;
            json.itemIds = itemIds;

            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: json,
                success: function (data) {
                    $.common.hideModal();
                    $.Page.refreshCurrentPage();
                    var alertMessage = data.message || "重置分发状态成功！";
                    var json = {
                        body: alertMessage,
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        toSelect: function (path, itemType) {
        	var typeName = $.BaseController.getItemTypeName(itemType);
        	$.common.ajaxActionText(path, function (data) {
        		$("#content_list_modal_container_body").html(data);
        		$('#content_list_modal_container_dialog').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择" + typeName);
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        getBatchData: function (prefix) {
        	var result = "";
        	$('#' + prefix + 'content_list tbody tr td input[type=checkbox].checkboxes:checked').each(function (i) {
                if (0 == i) {
                	result = $(this).attr("id");
                } else {
                	result += ("," + $(this).attr("id"));
                }
            });
            return result;
        },
        
        selectCheckedItem: function(prefix, itemType) {
        	var typeName = $.BaseController.getItemTypeName(itemType);
        	var params = $.BaseController.getBatchData(prefix);
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
        		tr += '<input type="hidden" name="itemType" value="' + itemType + '" />';
        		tr += '<input type="hidden" name="itemId" value="' + params[i].id + '" />';
        		tr += params[i].id;
        		tr += '</td>';
        		tr += '<td>';
        		tr += '' + typeName + '';
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
        
        batchExport: function (path, itemType) {
            var itemIds = $.BaseController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
            	var alertMessage = "请选择" + $.BaseController.getItemTypeName(itemType) + "!";
                var json = {
                    body: alertMessage,
                };
                $.common.showAlertModal(json);
                return;
            }

            var requestPath = path + "?itemType=" + itemType + "&itemIds=" + itemIds;
            window.open(requestPath);
        },
        
        exportAll: function (path, itemType) {
            var form = $(document.forms[0]);
            var requestPath = path + "?1=1";
            var formData = form.serializeArray();
            formData.forEach(function (e) {
            	if (e.value && "" != e.value) {
            		requestPath = requestPath + "&" + e.name + "=" + e.value;
            	}
            });
            window.open(requestPath);
        },
        
        keypress: function (config) {
	        $(".table-actions-wrapper-condition").keypress(function(e) {
	    		var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
	    		if (keyCode == 13) {
	    			$.Page.queryByForm(config);
	    			return false;
	    		}
	    	});
        },
        
        genNamePinyin: function(name) {
			if (name == "") {
				return;
			}
			var pinyinResult = pinyin.getCamelChars(name.replace(/\s/g, ""));
			return pinyinResult;
		},
		
		toggleSearchCondition: function(prefix) {
			var _prefix = prefix || "";
			var condition_open_status = $("#" + _prefix + "condition_open_status");
			var condition_open_button = $("#" + _prefix + "condition_open_button");
			var condition_open_panel = $("#" + _prefix + "condition_open_panel");
        	if (condition_open_status.val() && condition_open_status.val() == 1) {
        		condition_open_status.val(0);
        		condition_open_panel.hide();
        		condition_open_button.removeClass("fa-level-up");
        		condition_open_button.addClass("fa-level-down");
        	} else {
        		condition_open_status.val(1);
        		condition_open_panel.show();
        		condition_open_button.removeClass("fa-level-down");
        		condition_open_button.addClass("fa-level-up");
        	}
		},
    })
});
