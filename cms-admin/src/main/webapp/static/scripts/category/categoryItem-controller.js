$(function () {
    $.CategoryItemController = function (elm, config) {

    }

    $.extend($.CategoryItemController, {
    	
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
            });
        },

        edit: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            $("#itemStatus").removeAttr("disabled");
            
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
                    $.Page.refreshCurrentPage({containerId: 'categoryPageInfo'});
                }
            })
        },

        toDelete: function (path, name) {
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除[" + name + "]吗?";
            json.event = "$.CategoryItemController._delete('" + path + "')";
            $.common.showConfirmModal(json);
        },

        _delete: function (path) {
            $.common.ajaxActionText(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage({containerId: 'categoryPageInfo'});
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
            json.event = "$.CategoryItemController.changeStatus('" + path + "')";
            $.common.showConfirmModal(json);
        },
        
        changeStatus: function (path) {
        	$.common.ajaxActionText(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage({containerId: 'categoryPageInfo'});
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
        	} else if(itemType == 6) {
        		$.UriController.toSelectItem();
        	} else if(itemType == 7) {
        		$.ChannelController.toSelectItem();
        	} else if(itemType == 9) {
        		$.LeagueController.toSelectItem();
        	} else if(itemType == 10) {
        		$.ClubController.toSelectItem();
        	} else if(itemType == 11) {
        		$.StarController.toSelectItem();
        	} else if(itemType == 12) {
        		$.LeagueSeasonController.toSelectItem();
        	} else if(itemType == 13) {
        		$.LeagueMatchController.toSelectItem();
        	}
        },
        
        detail: function (itemType, id) {
        	if(itemType == 1) {
        		$.ProgramController.detail(id);
        	} else if(itemType == 2) {
        		$.SeriesController.detail(id);
        	} else if(itemType == 3) {
        		$.AlbumController.detail(id);
        	} else if(itemType == 6) {
        		$.UriController.detail(id);
        	} else if(itemType == 7) {
        		$.ChannelController.detail(id);
        	} else if(itemType == 9) {
        		$.LeagueController.detail(id);
        	} else if(itemType == 10) {
        		$.ClubController.detail(id);
        	} else if(itemType == 11) {
        		$.StarController.detail(id);
        	} else if(itemType == 12) {
        		$.LeagueSeasonController.detail(id);
        	} else if(itemType == 13) {
        		$.LeagueMatchController.detail(id);
        	}
        },
        
        changeSelectItem: function () {
        	$("#itemId").val("");
            $("#itemName").val("");
            $("#itemTitle").val("");
        	$("#itemImage1_img").attr("src", noImagePath);
        	$("#itemImage2_img").attr("src", noImagePath);
            $("#itemStatus").val(0);
        },
        
        init: function () {
            var table = $('#content_list');

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
            var itemIds = $.CategoryItemController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
                var json = {
                    body: "请选择元素！",
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
        
        batchInjection: function (path) {
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
                    $.Page.refreshCurrentPage({containerId: 'categoryPageInfo'});
                }
            });
        },
        
        toBatchOffline: function (path, itemType) {
        	var itemIds = $.CategoryItemController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
                var json = {
                    body: "请选择元素！",
                };
                $.common.showAlertModal(json);
                return;
            }
            
            var json = {};
            json.title = "下线操作";
            json.body = "您确认要下线吗?";
            json.event = "$.CategoryItemController.batchChangeStatusOffline('" + path + "'," + itemType + ")";
            $.common.showConfirmModal(json);
        },
        
        batchChangeStatusOffline: function (path, itemType) {
        	var itemIds = $.CategoryItemController.getItemIdsString();
            
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
                    $.Page.refreshCurrentPage({containerId: 'categoryPageInfo'});
                    var json = {
                        body: "下线成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
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
                    $.Page.refreshCurrentPage({containerId: 'categoryPageInfo'});
                }
            })
        },
        
        toBatchOnline: function (path, itemType) {
        	var itemIds = $.CategoryItemController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
                var json = {
                    body: "请选择元素！",
                };
                $.common.showAlertModal(json);
                return;
            }
            
            var json = {};
            json.title = "上线操作";
            json.body = "您确认要上线吗?";
            json.event = "$.CategoryItemController.batchChangeStatusOnline('" + path + "'," + itemType + ")";
            $.common.showConfirmModal(json);
        },
        
        batchChangeStatusOnline: function (path, itemType) {
        	var itemIds = $.CategoryItemController.getItemIdsString();
            
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
                    $.Page.refreshCurrentPage({containerId: 'categoryPageInfo'});
                    var json = {
                        body: "上线成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        toBatchDelete: function (path, itemType) {
        	var itemIds = $.CategoryItemController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
                var json = {
                    body: "请选择元素！",
                };
                $.common.showAlertModal(json);
                return;
            }
            
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除吗?";
            json.event = "$.CategoryItemController.batchDelete('" + path + "'," + itemType + ")";
            $.common.showConfirmModal(json);
        },
        
        batchDelete: function (path, itemType) {
        	var itemIds = $.CategoryItemController.getItemIdsString();
            
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
                    $.Page.refreshCurrentPage({containerId: 'categoryPageInfo'});
                    var json = {
                        body: "删除成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        toResetInjectionStatus: function (path, itemType) {
        	var itemIds = $.CategoryItemController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
                var json = {
                    body: "请选择元素！",
                };
                $.common.showAlertModal(json);
                return;
            }
            
            var json = {};
            json.title = "重置分发状态操作";
            json.body = "您确认要重置分发状态吗?";
            json.event = "$.CategoryItemController.resetInjectionStatus('" + path + "'," + itemType + ")";
            $.common.showConfirmModal(json);
        },
        
        resetInjectionStatus: function (path, itemType) {
        	var itemIds = $.CategoryItemController.getItemIdsString();
            
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
                    $.Page.refreshCurrentPage({containerId: 'categoryPageInfo'});
                }
            });
        },
        
        toSelectItemFromBatchAdd: function () {
        	var itemType = $("#itemType").val();
        	if (itemType == 1) {
        		$.ProgramController.toSelectItem('multi');
        	} else if(itemType == 2) {
        		$.SeriesController.toSelectItem('multi');
        	} else if(itemType == 3) {
        		$.AlbumController.toSelectItem('multi');
        	} else if(itemType == 13) {
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
            var sortIndex = $("#sort_index_all").val() || 1;
            $("input[name=sortIndex]").each(function(){
            	$(this).val(sortIndex);
            })
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
                    $.Page.refreshCurrentPage({containerId: 'categoryPageInfo'});
                }
            })
        },
    })
});

