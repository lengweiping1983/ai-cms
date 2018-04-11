$(function () {
    $.UriController = function (elm, config) {

    }

    $.extend($.UriController, {

        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                    $.extend($.validationEngineLanguage.allRules, {
                        "ajaxCodeCheck": {
                            "url": contextPath + "/uri/uri/check?id=" + id,
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
                	
                	$('.fileinput_logoImage').on('change.bs.fileinput', function(e, files) {
                		$("#logoImage").val("");
                		$("#logoImageData").val(files.result);
                	});
                	$('.fileinput_logoImage').on('clear.bs.fileinput', function() {
                    	$("#logoImage").val("");
                	});
                	
                	$("#code").val($("#code").val().toUpperCase());
                	
                	$.UriController.onloadUriConfig();
                },
            });
        },

        edit: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;
            if (!$("#paramForm").validationEngine("validate")) return false;
            
            $.UriController.setAutoCreateItem();
            
            var json = {};
            json.title = "保存操作";
            json.body = "您确认要保存吗?";
            json.event = "$.UriController.editSubmit('" + path + "')";
            $.common.showConfirmModal(json);
        },
        
        editSubmit: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;
            if (!$("#paramForm").validationEngine("validate")) return false;
            
            $.UriController.setAutoCreateItem();
            
            var json = $("#editForm").serializeObject();
            var paramData = $("#paramForm").serializeObject();
            var vCount = 0;
            // 计算json内部的数组最大长度
            for (var item in paramData) {
                var tmp = $.isArray(paramData[item]) ? paramData[item].length : 1;
                vCount = (tmp > vCount) ? tmp : vCount;
            }

            var paramList = new Array();
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

            var bean = {
                data: json,
                params: paramList,
                image1Data: $("#image1Data").val(),
                image2Data: $("#image2Data").val(),
                backgroundImageData: $("#backgroundImageData").val(),
                logoImageData: $("#logoImageData").val(),
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
            json.event = "$.UriController._delete('" + path + "')";
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
                url: contextPath + "/uri/uri/"+id+"/detail",
                success: function () {
                	$.UriController.onloadUriConfig();
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
        
        changeSelectType: function (type) {
        	var uri_url = $("#uri_url");
        	var uri_template_code = $("#uri_template_code");
        	var uriParamConfig_div = $("#uriParamConfig_div");
        	var backgroundImage_div = $("#backgroundImage_div");
        	var logoImage_div = $("#logoImage_div");
        	
        	if (type == 1) {
        		uri_url.hide();
        		uri_template_code.show();
        		uriParamConfig_div.show();
        		if ($("#configBackgroundImage").val() == "1") {
        			backgroundImage_div.show();
        			logoImage_div.show();
        		}
        	} else if(type == 0) {
        		uri_url.show();
        		uri_template_code.hide();
        		uriParamConfig_div.hide();
        		backgroundImage_div.hide();
        		logoImage_div.hide();
        	}
        },
        
        toChangeStatus: function (path, name, statusMethodDesc) {
        	var json = {};
            json.title = statusMethodDesc + "操作";
            json.body = "您确认要" + statusMethodDesc + "[" + name + "]吗?";
            json.event = "$.UriController.changeStatus('" + path + "')";
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
        		url: contextPath + "/uri/"+id+"/uriItem/",
                type: "GET",
                dataType: "text",
                contentType: "default",
                data: {search_uri_id__EQ_L: id},
                success: function (data) {
                }
            })
        },
        
        toSelectUri: function () {
        	$.common.ajaxActionText(contextPath + "/uri/uri/selectUri", function (data) {
        		$("#content_list_container").html(data);
        		$('#content_list_dialog_container').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择页面");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        selectUri: function(id, name) {
        	$("#selectUriId").val(id);
            $("#selectUriName").val(name);
            $("#content_list_modal_container").modal('hide');
        },

        batchToUri: function (path) {
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
                        body: "批量编排到页面成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        toSelectItem: function (selectMode) {
        	var _selectMode = selectMode || 1;
        	$.common.ajaxActionText(contextPath + "/uri/uri/selectItem?selectMode=" + _selectMode, function (data) {
        		$("#content_list_container").html(data);
        		$('#content_list_dialog_container').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择页面");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        selectItem: function(selectMode, id, name, title, image1, image2, status) {
        	var _selectMode = selectMode || 1;
        	if (_selectMode == 1) {
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
	            $.UriController.loadUriConfigById(selectMode, id);
        	} else if(_selectMode == 2) {
        		$("#jumpItemId").val(id);
	            $("#jumpItemName").val(name);
	            $.UriController.loadUriConfigById(selectMode, id, $("#itemType").val(), $("#itemId").val());
        	}
            $("#content_list_modal_container").modal('hide');
        },
                
        changeTemplateId: function (templateId) {
        	if (templateId == "") {
        		var backgroundImage_div = $("#backgroundImage_div");
            	backgroundImage_div.hide();
        		$.UriController.clearParam();
        		return;
        	}
			$.UriController.loadUriConfig(templateId);
        },
        
        loadUriConfigCancel: function () {
        	var lastTemplateId = $("#lastTemplateId").val();
        	if(lastTemplateId && lastTemplateId > 0) {
        		$("#templateId").val(lastTemplateId);
        		$.UriController.loadUriConfig(lastTemplateId);
        	}
        },
        
        clearParam: function () {
        	$("#param_list").empty();
        	var tr = '<tr><td colspan="11" align="center">无参数!</td></tr>';
            $("#param_list").append(tr);
        },
        
        onloadUriConfig: function () {
        	$.UriController.clearParam();
        	var templateId = $("#currentParamTemplateId").val();
        	if (templateId && templateId != "") {
        		$.UriController.loadUriConfig(templateId);
        	}
        },
        
        loadUriConfigById: function (selectMode, id, itemType, itemId) {
        	var _selectMode = selectMode || 1;
        	if (_selectMode == 1 && $("#jumpItemType").val() != 0) {
            	return;
            }
        	
        	var uriParamConfig_div = $("#uriParamConfig_div");
        	uriParamConfig_div.show();
        	
            var json = {};
            json.id = id;
            if (itemType != "" && itemId != "") {
	            json.itemType = itemType;
	            json.itemId = itemId;
	        }
            json.internalParam = $("#internalParam").val();
            var internalParamCategory = 1;
            if ($("#internalParamCategory") && $("#internalParamCategory").val() != undefined ) {
        		internalParamCategory = $("#internalParamCategory").val();
        	}
            json.internalParamCategory = internalParamCategory;
            $.ajax({
            	url: contextPath + '/uri/uri/loadUriConfigById',
                type: 'GET',
                dataType: 'json',
                contextType: "application/json",
                data: json,
                success: function (data) {
                	$.common.hideConfirmModal();
                	$("#param_list").empty();
                	
                	var templateId = data.id;
                	$("#lastTemplateId").val(templateId);
                	
                	$("#currentUriId").val(id);
                	$("#currentParamTemplateId").val(templateId);
                	    				
                	$("#configBackgroundImage").val(data.configBackgroundImage);
                	var backgroundImage_div = $("#backgroundImage_div");
                	
                	if (data.configBackgroundImage == 1) {
                		backgroundImage_div.show();
                	} else {
                		backgroundImage_div.hide();
                	}
                	
                	var internalParamCategory_div = $("#internalParamCategory_div");
                	
                	if (data.dynamicCategory) {
                		internalParamCategory_div.show();
                	} else {
                		internalParamCategory_div.hide();
                	}
                	
                    var params = data.params;
                    if (params == null || params.length == 0) {
                    	$.UriController.clearParam();
                        return;
                    }
                    var time = new Date().getTime();
                    for (var i = 0; i < params.length; i++) {
                        var trId = "tr_" + time + "_" + i;
                        var tr = '<tr id="' + trId  + '">"' + '</tr>';
                        $("#param_list").append(tr);
                        $.UriController.addParam(trId, templateId, params[i]);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert('加载失败！');
                },
            });
        },
        
        loadUriConfig: function (templateId) {
        	var id = $("#currentUriId").val();
            var json = {};
            json.id = id;
            json.templateId = templateId;
            var internalParamCategory = 1;
            if ($("#internalParamCategory") && $("#internalParamCategory").val() != undefined ) {
        		internalParamCategory = $("#internalParamCategory").val();
        	}
            json.internalParamCategory = internalParamCategory;
            $.ajax({
            	url: contextPath + '/uri/uri/loadUriConfig',
                type: 'GET',
                dataType: 'json',
                contextType: "application/json",
                data: json,
                success: function (data) {
                	$.common.hideConfirmModal();
                	$("#param_list").empty();
                	
                	$("#lastTemplateId").val(templateId);
                	
                	var templateCode = $("#templateCode");
                	if (templateCode) {
                		templateCode.val(data.code);
                	}
                	
                	$("#configBackgroundImage").val(data.configBackgroundImage);
                	var backgroundImage_div = $("#backgroundImage_div");
                	
                	if (data.configBackgroundImage == 1) {
                		backgroundImage_div.show();
                	} else {
                		backgroundImage_div.hide();
                	}
                	
                	var internalParamCategory_div = $("#internalParamCategory_div");
                	
                	if (data.dynamicCategory) {
                		internalParamCategory_div.show();
                	} else {
                		internalParamCategory_div.hide();
                	}
                	
                    var params = data.params;
                    if (params == null || params.length == 0) {
                    	$.UriController.clearParam();
                        return;
                    }
                    var time = new Date().getTime();
                    for (var i = 0; i < params.length; i++) {
                        var trId = "tr_" + time + "_" + i;
                        var tr = '<tr id="' + trId  + '">"' + '</tr>';
                        $("#param_list").append(tr);
                        $.UriController.addParam(trId, templateId, params[i]);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert('加载失败！');
                },
            });
        },
        
        addParam: function (trId, templateId, param) {
        	var validateStr = "";
        	var codeValidateStr = "";
        	var urlParamConfigMode = $("#urlParamConfigMode");
        	if (urlParamConfigMode && urlParamConfigMode.val() == "1" && param.mode == 2) {
        		validateStr =" validate[required]";
        		codeValidateStr =" validate[required,maxSize[24]]";
        	}
        	if (param.mode == 1) { // 必选
        		validateStr =" validate[required]";
        		codeValidateStr =" validate[required,maxSize[24]]";
        	}
        	var tr = '';
            tr += '<td><input type="hidden" name="type" id="' + trId + '_type" value="' + param.type + '">' + param.typeName + '</td>';
            tr += '<td><input type="hidden" name="mode" id="' + trId + '_mode" value="' + param.mode + '">';
            if (param.mode == 2) {
            	tr += '<span class="badge badge-danger">' + param.modeName + '</span>';
            } else {
            	tr += param.modeName;
            }
            tr += '</td>';
        	tr += '<td>';
        	tr += '<input type="hidden" name="id" id="' + trId + '_id" value="' + param.id + '">';
        	tr += '<input type="hidden" name="itemId" id="' + trId + '_itemId" value="' + param.itemId + '">';
        	tr += '<input type="hidden" name="autoCreateItem" id="' + trId + '_autoCreateItem" value="0">';
        	tr += '<input type="hidden" name="number" id="' + trId + '_number" value="' + param.number + '">' + param.number + '';
        	tr += '</td>';
            if (param.configCode == 1) {
            	tr += '<td><input type="text" name="code" id="' + trId + '_code" value="' + param.code + '" onkeyup="this.value=this.value.toUpperCase()" class="form-control input-inline ' + codeValidateStr + '" onchange="$.UriController.toChangeParamCode(\'' + trId + '\',\'' + templateId + '\');"><span class="badge badge-info">可通过参数传递</span>' + '</td>';
            } else {
            	tr += '<td><input type="text" name="code" id="' + trId + '_code" value="' + param.code + '" onkeyup="this.value=this.value.toUpperCase()" readOnly class="form-control input-inline ' + codeValidateStr + '" >' + '</td>';
            }
            if (param.exist) {
            	tr += '<td><input type="text" name="name" value="' + param.name + '" readOnly class="form-control input-inline ' + validateStr + '">' + '</td>';
            	tr += '<td><span class="badge badge-success">已存在</span></td>';
            	tr += '<td><input disabled type="checkbox"></td>';
            } else {
            	tr += '<td><input type="text" name="name" value="' + param.name + '" class="form-control input-inline ' + validateStr + '">' + '</td>';
            	tr += '<td>';
            	if (param.mode == 1 || param.mode == 2) {
            		tr += '<span class="badge badge-danger">不存在</span>';
                	tr += '<span class="badge badge-danger">(上线时，请确保该元素存在!)</span>';
            	} else {
            		tr += '<span class="badge badge-danger">不存在</span>';
            		tr += '<span class="badge badge-info">页面上不会展示该元素!</span>';
            	}
            	tr += '</td>';
            	if (param.type == 4 || param.type == 5) {
            		tr += '<td><input name="autoCreateItemBox" checked type="checkbox" value="' + trId + '"></td>';
            	} else {
            		tr += '<td><input disabled type="checkbox"></td>';
            	}
            }
            
            if (param.styleConfig && param.styleConfig != "null") {
            	tr = tr + '<td><select name="style" id="' + trId + '_style">';
            	tr = tr + '<option value="">请选择</option>';
            	var styleArrAll = param.styleConfig.split("$");
            	for (var i = 0; i < styleArrAll.length; i++) {
    				var styleArr = styleArrAll[i].split("=");
    				tr = tr + '<option value=' + styleArr[0];
    				if (param.style && param.style != "null" && param.style == styleArr[0]) {
    					tr = tr + ' selected="selected" ';
    				}
    				tr = tr + '>' + styleArr[1] + '</option>';
    			}
            	tr = tr + '</select></td>';
            } else {
            	tr += '<input type="hidden" name="style" id="' + trId + '_style" value="">';
            	tr += '<td>默认' + '</td>';
            }
            if ((param.type == 4 || param.type == 5) && param.configImage1 == 1) {
            	tr += '<td>' + param.configImage1Width + 'X' + param.configImage1Height + '</td>';
            } else {
            	tr += '<td>无' + '</td>';
            }
            if ((param.type == 4 || param.type == 5) && param.configImage2 == 1) {
            	tr += '<td>' + param.configImage2Width + 'X' + param.configImage2Height + '</td>';
            } else {
            	tr += '<td>无' + '</td>';
            }
            tr += '<td>';
            if (param.configCode == 1) {
	            tr += '<button type="button" class="btn btn-default btn-sm btn-outline green" onclick="$.UriController.toSelectParamTypeItem(\'' + trId + '\',\'' + templateId + '\');">';
	            tr += '<i class="fa fa-arrow-left fa-fw""></i>选择' + param.typeName;
	            tr += '</button>';
            }
            tr += '</td>';
            $("#" + trId).html(tr);
        },
        
        setAutoCreateItem: function () {
            $('input:checkbox[name=autoCreateItemBox]').each(function (i) {
            	var trId = $(this).val();
            	$("#" + trId + "_autoCreateItem").val(0);
            });
            $('input:checkbox[name=autoCreateItemBox]:checked').each(function (i) {
            	var trId = $(this).val();
            	$("#" + trId + "_autoCreateItem").val(1);
            });
        },
        
        toChangeParamCode: function (trId, templateId) {
        	if (trId ==  "" || templateId == "") {
        		return;
        	}
        	var id = $("#currentUriId").val();
        	var type = $("#" + trId + "_type").val();
        	var number = $("#" + trId + "_number").val();
        	var code = $("#" + trId + "_code").val();
        	if (type == "" || number == "") {
    			return;
    		}
        	$("#currentParamTemplateId").val(templateId);
        	$("#currentParamTrId").val(trId);
        	$("#currentParamType").val(type);
        	$("#currentParamNumber").val(number);
    		var json = {};
    		json.id = id;
            json.templateId = templateId;
            json.type = type;
            json.number = number;
            json.code = code;
            $.ajax({
            	url: contextPath + '/uri/uri/loadUriParamConfig',
                type: 'GET',
                dataType: 'json',
                contextType: "application/json",
                data: json,
                success: function (data) {
                	$.UriController.addParam(trId, templateId, data);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert('加载失败！');
                },
            });
        },
        
        selectParamByItemId: function (itemId) {
        	var id = $("#currentUriId").val();
        	var templateId = $("#currentParamTemplateId").val();
        	var trId = $("#currentParamTrId").val();
        	if (trId ==  "" || templateId == "") {
        		return;
        	}
        	var type = $("#currentParamType").val();
        	var number = $("#currentParamNumber").val();
        	if (type == "" || number == "" || itemId == "") {
    			return;
    		}
    		var json = {};
    		json.id = id;
            json.templateId = templateId;
            json.type = type;
            json.number = number;
            json.itemId = itemId;
            $.ajax({
            	url: contextPath + '/uri/uri/loadUriParamConfig',
                type: 'GET',
                dataType: 'json',
                contextType: "application/json",
                data: json,
                success: function (data) {
                	$.UriController.addParam(trId, templateId, data);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert('加载失败！');
                },
            });
        },
        
        selectParamByWidgetId: function (widgetId) {
        	$.UriController.selectParamByItemId(widgetId);
        },
        selectParamByCategoryId: function (categoryId) {
        	$.UriController.selectParamByItemId(categoryId);
        },
        selectParamByStarId: function (starId) {
        	$.UriController.selectParamByItemId(starId);
        },
        
        toSelectParamTypeItem: function (trId, templateId) {
        	var type = $("#" + trId + "_type").val();
        	var number = $("#" + trId + "_number").val();
        	$("#currentParamTemplateId").val(templateId);
        	$("#currentParamTrId").val(trId);
        	$("#currentParamType").val(type);
        	$("#currentParamNumber").val(number);
        	if (type == 4) {
        		$.WidgetController.toSelectItem(91);
        	} else if(type == 5) {
        		$.CategoryController.selectCategoryItemTree(91);
        	} else if(type == 11) {
        		$.StarController.toSelectItem(91);
        	}
        },
        
        listByPosition: function (path) {
        	$.common.pushStackContentParam();
        	
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
        
        showWidgetItemList: function (id, from) {
        	$.common.pushStackContentParam();
        	
        	$.common.ajaxLoadContent({
        		url: contextPath + "/widget/"+id+"/widgetItem/",
                type: "GET",
                dataType: "text",
                contentType: "default",
                data: {search_widgetId__EQ_L: id, from: from},
                success: function (data) {
                }
            })
        },
        
        showCategoryItemList: function (id, from) {
        	$.common.pushStackContentParam();
        	
        	$.common.ajaxLoadContent({
        		url: contextPath + "/category/",
                type: "GET",
                dataType: "text",
                contentType: "default",
                data: {currentNodeId: id, from: from},
                success: function (data) {
                }
            })
        },
        
        ajaxLoadLastContent: function (config) {
        	if (config == undefined) {
        		var config = {};
        	}
        	var url = stackContentRequestPath.pop();
        	var data = stackContentData.pop();
            var containerId = config.containerId || "page-content-wrapper";
            $.common.ajaxLoadContent({
                url: url,
                type: "GET",
                dataType: "text",
                contentType: "default",
                containerId: containerId,
                data: data,
                success: function (data) {
                	var text = '$("#the-img-tag").annotatorPro({annotations : [';
                	text += $('#positionStr').val();
					text +="]});";
					annotator_canvas_open(text);
                }
            })
        },
        
        toEditByPositionFromAnnotator: function (positionId) {
        	var uriId = $("#uriId").val();
        	var internalParam = $("#internalParam").val();
        	var json = {};
            $.ajax({
            	url: contextPath + '/uri/uri/' + uriId + '/getUriParam?positionId=' + positionId + '&internalParam=' + internalParam,
                type: 'GET',
                dataType: 'json',
                contextType: "application/json",
                data: json,
                success: function (data) {
                	if (data) {
                		if (data.itemId && data.type == 4) {
                			$.UriController.showWidgetItemList(data.itemId, 'uri');
                		} else if (data.itemId && data.type == 5) {
                			$.UriController.showCategoryItemList(data.itemId, 'uri');
                		} else if (data.internalParamCategory == 2) {
                			alert('连动元素，需要进入到项中查看！');
                		}
                	} else {
                		alert('访问错误，可能原因模板已发生改变！');
                	}
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert('加载失败！');
                },
            });
        },
        
    })
});