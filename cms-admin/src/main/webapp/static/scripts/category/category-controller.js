$(function () {
    $.CategoryController = function (elm, config) {

    }

    $.extend($.CategoryController, {

        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                    $.extend($.validationEngineLanguage.allRules, {
                        "ajaxCodeCheck": {
                            "url": contextPath + "/category/category/check?id=" + id,
                            "extraData": "dt=" + (new Date()).getTime(),
                            "alertText": "代码不能使用!",
                            "alertTextLoad": "验证中，请稍候..."
                        }
                    });

                    $("#editForm").validationEngine({
                        ajaxFormValidationMethod: 'post',
                    });
                                    	
                	$("#code").val($("#code").val().toUpperCase());
                },
            });
        },

        edit: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();
            json.configItemTypes = $.CategoryController.getConfigItemTypes();
            
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
                    $.Page.refreshCurrentPage({containerId: 'categoryPageInfo'});
                }
            })
        },
        
        add: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();
            json.configItemTypes = $.CategoryController.getConfigItemTypes();

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
                    $.Page.refreshCurrentPage({containerId: 'categoryPageInfo'});
                    $.CategoryController.showLeftTree();
                }
            })
        },

        toDelete: function (path, name) {
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除[" + name + "]吗?";
            json.event = "$.CategoryController._delete('" + path + "')";
            $.common.showConfirmModal(json);
        },

        _delete: function (path) {
            $.common.ajaxActionText(path, function (data) {
        		$.common.hideModal();
                $.Page.refreshCurrentPage({containerId: 'categoryPageInfo'});
                $.CategoryController.showLeftTree();
            })
        },
        
        detail: function (id) {
        	$.common.showModal({
                url: contextPath + "/category/category/"+id+"/detail",
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
            json.event = "$.CategoryController.changeStatus('" + path + "')";
            $.common.showConfirmModal(json);
        },
        
        changeStatus: function (path) {
        	$.common.ajaxActionText(path, function (data) {
        		$.common.hideModal();
                $.Page.refreshCurrentPage({containerId: 'categoryPageInfo'});
            })
        },
        
        refreshLeft: function () {
            $(".category-left").load(contextPath + "/category/left", function () {
            });
        },
        
        showLeftTree: function () {
        	$("#category_tree").jstree("destroy");
            $.common.ajaxAction({
                url: contextPath + '/category/getTreeWithRoot',
                type: 'GET',
                dataType: 'json',
                contextType: "application/json",
                success: function (data) {
                    $('#category_tree').jstree({
                        'plugins': ["wholerow", "types"],
                        'core': {
                            "themes": {
                                "responsive": false
                            },
                            'data': data
                        },
                        "types": {
                            "default": {
                                "icon": "fa fa-folder icon-state-warning icon-lg"
                            },
                            "file": {
                                "icon": "fa fa-file icon-state-warning icon-lg"
                            }
                        }
                    }).on("loaded.jstree", function (event, data) {
                        var selectId = $("#currentNodeId").val();
                        if (selectId == undefined || selectId == "-1" || selectId == "") {
                            $('#-1_anchor').click();
                        } else {
                            // $('#category_tree').jstree(false).select_node('#' + selectId);
                            $('#' + selectId + '_anchor').click();
                        }
                    }).on('click.jstree', function (event) {
                    	var nodeId = $(event.target).attr("id");
                    	if(nodeId == undefined) {
                    		return;
                    	}
                        var parentId = nodeId.split("_")[0];
                        var url = $(event.target).attr("href");
                        var json = {};
                        if (parentId > 0) {
                        	$("#currentNodeId").val(parentId);
                        	json.search_parentId__EQ_L = parentId;
                        } else {
                        	json.search_parentId__EQ_L = null;
                        }
                        var currentNodeFrom = $("#currentNodeFrom").val();
                        json.from = currentNodeFrom;
                        $.common.ajaxLoadContent({
                            url: contextPath + url,
                            type: "GET",
                            dataType: "text",
                            contentType: "default",
                            data: json,
                            containerId: 'categoryPageInfo',
                            success: function (data) {
                                //$(".category-content").html(data);
                            }
                        });
                    });
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert('加载失败！');
                },
            });
        },
        
        showTree: function (filterCategoryId) {
            $("#category_tree_div").jstree("destroy");
            var json = {};
            if (filterCategoryId && filterCategoryId > 1) {
                json.filterCategoryId = filterCategoryId;
            }
            $.ajax({
            	url: contextPath + '/category/getTreeWithRoot',
                type: 'GET',
                dataType: 'json',
                contextType: "application/json",
                data: json,
                success: function (data) {
                    $("#category_tree_modal_div").modal({});
                    $('#category_tree_div').jstree({
                        'multiple': false,
                        'plugins': ["wholerow", "types"],
                        'core': {
                            "themes": {
                                "responsive": false
                            },
                            'data': data
                        },
                        "types": {
                            "default": {
                                "icon": "fa fa-folder icon-state-warning icon-lg"
                            },
                            "file": {
                                "icon": "fa fa-file icon-state-warning icon-lg"
                            }
                        }
                    }).on("loaded.jstree", function (event, data) {
                        // data.instance.open_all();
                        var selectCategoryId = $("#parentId").val();
                        $('#category_tree_div').jstree(false).select_node('#' + selectCategoryId);
                    }).on('click.jstree', function (event) {
                    	var nodeId = $(event.target).attr("id");
                    	if(nodeId == undefined) {
                    		return;
                    	}
                        var parentId = nodeId.split("_")[0];

                        if (parentId > 0) {
                        	$("#parentId").val(parentId);
                            $("#category_parent_name").val(event.target.text);
                        } else {
                        	$("#parentId").val('');
                            $("#category_parent_name").val('');
                        }

                        $("#category_tree_modal_div").modal('hide');
                        $("#category_tree_div").jstree("destroy");
                    });
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert('加载失败！');
                },
            });
        },

        
        
        showDetail: function (id) {
        	$.common.putLastContentParam();
        	
        	$.common.ajaxLoadContent({
        		url: contextPath + "/category/"+id+"/categoryItem/",
                type: "GET",
                dataType: "text",
                contentType: "default",
                containerId: 'categoryPageInfo',
                data: {search_categoryId__EQ_L: id},
                success: function (data) {
                }
            })
        },
        
        toSelectCategory: function () {
        	$.common.ajaxActionText(contextPath + "/category/category/selectCategory", function (data) {
        		$("#content_list_container").html(data);
        		$('#content_list_modal_container_title').html("选择栏目");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        selectCategory: function(id, name) {
        	$("#selectCategoryId").val(id);
            $("#selectCategoryName").val(name);
            $("#content_list_modal_container").modal('hide');
        },

        batchToCategory: function (path) {
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
                        body: "批量编排到栏目成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        selectCategoryTree: function (filterCategoryId) {
        	try{
        		$("#content_list_container").jstree("destroy");
        	} catch (e) {
        		
        	}
            var json = {};
            if (filterCategoryId && filterCategoryId > 1) {
                json.filterCategoryId = filterCategoryId;
            }
            var appCode = $("#appCode").val();
            json.appCode = appCode;
            $.ajax({
            	url: contextPath + '/category/getTreeWithRoot',
                type: 'GET',
                dataType: 'json',
                contextType: "application/json",
                data: json,
                success: function (data) {
                	$('#content_list_dialog_container').removeClass("modal-lg").addClass("modal-sm");
            		$('#content_list_modal_container_title').html("选择栏目");
            		$("#content_list_modal_container").modal({});
                    $('#content_list_container').jstree({
                        'multiple': false,
                        'plugins': ["wholerow", "types"],
                        'core': {
                            "themes": {
                                "responsive": false
                            },
                            'data': data
                        },
                        "types": {
                            "default": {
                                "icon": "fa fa-folder icon-state-warning icon-lg"
                            },
                            "file": {
                                "icon": "fa fa-file icon-state-warning icon-lg"
                            }
                        }
                    }).on("loaded.jstree", function (event, data) {
                    }).on('click.jstree', function (event) {
                    	var nodeId = $(event.target).attr("id");
                    	if(nodeId == undefined) {
                    		return;
                    	}
                        var parentId = nodeId.split("_")[0];

                        if (parentId > 0) {
                        	$("#selectCategoryId").val(parentId);
                            $("#selectCategoryName").val(event.target.text);
                        } else {
                        	$("#selectCategoryId").val('');
                            $("#selectCategoryName").val('');
                        }
                        $("#content_list_modal_container").modal('hide');
                        $("#content_list_container").jstree("destroy");
                    });
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert('加载失败！');
                },
            });
        },
        
        
        selectCategoryItemTree: function (selectMode) {
        	var _selectMode = selectMode || 1;
        	try{
        		$("#content_list_container").jstree("destroy");
        	} catch (e) {
        		
        	}
            var json = {};
            $.ajax({
            	url: contextPath + '/category/getTreeWithRoot',
                type: 'GET',
                dataType: 'json',
                contextType: "application/json",
                data: json,
                success: function (data) {
                	$('#content_list_dialog_container').removeClass("modal-lg").addClass("modal-sm");
            		$('#content_list_modal_container_title').html("选择栏目");
            		$("#content_list_modal_container").modal({});
                    $('#content_list_container').jstree({
                        'multiple': false,
                        'plugins': ["wholerow", "types"],
                        'core': {
                            "themes": {
                                "responsive": false
                            },
                            'data': data
                        },
                        "types": {
                            "default": {
                                "icon": "fa fa-folder icon-state-warning icon-lg"
                            },
                            "file": {
                                "icon": "fa fa-file icon-state-warning icon-lg"
                            }
                        }
                    }).on("loaded.jstree", function (event, data) {
                    }).on('click.jstree', function (event) {
                    	var nodeId = $(event.target).attr("id");
                    	var status = $(event.target).attr("title");
                    	if(nodeId == undefined || status == undefined) {
                    		return;
                    	}
                        var parentId = nodeId.split("_")[0];
                        
                        if (parentId > 0) {
                        	if(selectMode == 1) {
	                        	$("#itemId").val(parentId).change();
	                            $("#itemName").val(event.target.text);
	                            $("#itemStatus").val(status);
                        	} else if(selectMode == 2) {
                        		$("#jumpItemId").val(parentId).change();
	                            $("#jumpItemName").val(event.target.text);
                        	} else if(selectMode == 91) {
                        		$.UriController.selectParamByCategoryId(parentId);
                        	}
                        } else {
                        	if(selectMode == 1) {
                        		$("#itemId").val('');
                        		$("#itemName").val('');
                        	} else if(selectMode == 2) {
                        		$("#jumpItemId").val('');
	                            $("#jumpItemName").val('');
                        	}
                        }
                        $("#content_list_modal_container").modal('hide');
                        $("#content_list_container").jstree("destroy");
                    });
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert('加载失败！');
                },
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
        	var itemIds = $.CategoryController.getItemIds();
	        	if(itemIds==null || itemIds.length==0) {
	        	var json = {
	                body: "请选择栏目！",
	            };
	            $.common.showAlertModal(json);
	            return ;
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
        
        batchOutInjection: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;
            var password = $("#password").val();
            if(password != null && password == 'joyu1201') {
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
            } else {
            	$.common.hideModal();
        		var json = {
                    body: "密码错误！",
                };
                $.common.showAlertModal(json);
                return;
            }
        },
        
        init: function() {
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
        
        toResetInjectionStatus: function (path, itemType) {
        	var itemIds = $.CategoryController.getItemIds();
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
            json.event = "$.CategoryController.resetInjectionStatus('" + path + "'," + itemType + ")";
            $.common.showConfirmModal(json);
        },
        
        resetInjectionStatus: function (path, itemType) {
        	var itemIds = $.CategoryController.getItemIdsString();
            
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
    })
});