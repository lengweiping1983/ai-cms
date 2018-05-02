$(function () {

    $.TranscodeRequestController = function (elm, config) {

    }

    $.extend(true, $.TranscodeRequestController, $.BaseController, {

    	init: function (prefix) {
        	$.TranscodeRequestController.initCpSearch(prefix);
        	
        	$.TranscodeRequestController.initTableCheckBox(prefix);
        },
        
        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                	
                	$.TranscodeRequestController.initCpSelect();
                	$("input:checkbox[name='templateId']").uniform();
                	
                    $("#editForm").validationEngine({
                        ajaxFormValidationMethod: 'post',
                    });

                    $("#fileForm").validationEngine({
                        ajaxFormValidationMethod: 'post',
                    });
                },
            });
        },

        detail: function (path) {
            $.common.showModal({
                url: path,
                success: function () {
                	$.TranscodeRequestController.initCpSelect();
                	$("input:checkbox[name='templateId']").uniform();
                },
            });
        },

        taskList: function (id) {
            $.common.putLastContentParam();

            $.common.ajaxLoadContent({
                url: contextPath + "/transcode/" + id + "/transcodeTask/",
                type: "GET",
                dataType: "text",
                contentType: "default",
                data: {search_requestId__EQ_L: id},
                success: function (data) {
                }
            })
        },

        editSubmit: function (path, submitContainerId) {
            var json = $("#editForm").serializeObject();

            $("#TranscodeRequestSave").attr("disabled","disabled");
            $("#TranscodeRequestProduce").attr("disabled","disabled");
            
            json.templateId = $.TranscodeRequestController.getCheckboxValue('templateId');

            var fileJsonData = $("#fileForm").serializeObject();
            var vCount = 0;
            // 计算json内部的数组最大长度
            for (var item in fileJsonData) {
                var tmp = $.isArray(fileJsonData[item]) ? fileJsonData[item].length : 1;
                vCount = (tmp > vCount) ? tmp : vCount;
            }

            var fileJsonList = new Array();
            if (vCount == 1) {
                fileJsonList.push(fileJsonData);
            } else if (vCount > 1) {
                for (var i = 0; i < vCount; i++) {
                    var jsonObj = {};

                    for (var item in fileJsonData) {
                        jsonObj[item] = fileJsonData[item][i];
                    }

                    fileJsonList.push(jsonObj);
                }
            }

            json.fileList = fileJsonList;
            
            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: json,
                submitContainerId: submitContainerId,
                success: function (data) {
                    $.common.hideModal();
                    $.Page.refreshCurrentPage();
                },
                errorCallBack: function (XMLHttpRequest, textStatus, errorThrown) {
	            	$("#TranscodeRequestSave").removeAttr("disabled");
	                $("#TranscodeRequestProduce").removeAttr("disabled");
	            }
            });
        },

        edit: function (path) {
            if (!$("#editForm").validationEngine("validate"))
                return false;
            if (!$("#fileForm").validationEngine("validate"))
                return false;

            $.TranscodeRequestController.editSubmit(path,'editForm');
        },

        produce: function (path) {
            if (!$("#editForm").validationEngine("validate"))
                return false;
            if (!$("#fileForm").validationEngine("validate"))
                return false;

            var json = {};
            json.title = "执行操作";
            json.body = "您确认要执行吗?";
            json.event = "$.TranscodeRequestController.editSubmit('" + path + "','confirm_modal_dialog')";
            $.common.showConfirmModal(json);
        },

        toDelete: function (path, name) {
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除[" + name + "]吗?";
            json.event = "$.TranscodeRequestController._delete('" + path + "')";
            $.common.showConfirmModal(json);
        },

        _delete: function (path) {
            $.common.ajaxActionText(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
            })
        },
        
        toDeleteFile: function (id, filePath) {
            var json = {
                title: "删除文件记录",
                body: "您确定要删除文件记录[" + filePath + "]吗?",
                event: "$.TranscodeRequestController.deleteFile('" + id + "')",
            };
            $.common.showConfirmModal(json);
        },

        deleteFile: function (id) {
            $("#tr_file_" + id).remove();
            $.common.hideConfirmModal();
        },

        toCopy: function (path, itemType, itemIds) {
            var requestPath = path + "?itemType=" + itemType + "&itemIds=" + itemIds;

            $.common.showModal({
                url: requestPath,
                type: "GET",
                dataType: "text",
                contentType: "default",
                success: function () {
                	$("input:checkbox[name='templateId']").uniform();
                },
            });
        },
        
        batchCopy: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();
            json.templateId = $.TranscodeRequestController.getCheckboxValue('templateId');

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
                        body: "复制工单成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },

        toBatchCopy: function (path, itemType) {
        	var itemIds = $.TranscodeRequestController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
            	var alertMessage = "请选择" + $.TranscodeRequestController.getItemTypeName(itemType) + "!";
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
                success: function (data) {
                	$("input:checkbox[name='templateId']").uniform();
                }
            });
        },
        
        toBatchProduce: function (path, itemType) {
        	var itemIds = $.TranscodeRequestController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
                var json = {
                    body: "请选择工单！",
                };
                $.common.showAlertModal(json);
                return;
            }
            
            var json = {};
            json.title = "批量执行操作";
            json.body = "您确认要批量执行吗?";
            json.event = "$.TranscodeRequestController.batchProduce('" + path + "'," + itemType + ")";
            $.common.showConfirmModal(json);
        },
        
        batchProduce: function (path, itemType) {
        	var itemIds = $.TranscodeRequestController.getItemIdsString();
            
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
                    var json = {
                        body: "批量执行成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },
        
        toSelectMedia: function (type) {
        	if (type == 1) {
        		$.ProgramController.toSelectProgram('media');
        	} else if (type == 2) {
        		$.SeriesController.toSelectSeries('media');
        	}
        },
        
        toBatchSelectMedia: function (type, batch_index) {
        	if (type == 1) {
        		$.ProgramController.toSelectProgram('batch_media', batch_index);
        	} else if (type == 2) {
        		$.SeriesController.toSelectSeries('batch_media', batch_index);
        	}
        },
        
        changeMediaName: function (obj) {
        	var pinyinResult = $.TranscodeRequestController.genNamePinyin(obj.value.trim());
			$("#mediaFilename").val(pinyinResult);
            $("#mediaId").val("");
        },
        
        changeMediaNameFromBatch: function (obj, batch_index) {
        	var pinyinResult = $.TranscodeRequestController.genNamePinyin(obj.value.trim());
            $("#mediaFilename_" + batch_index).val(pinyinResult);
            $("#mediaId_" + batch_index).val("");
        },
        
        changeMediaIdFromBatch: function (obj, batch_index) {
            var mediaId = obj.value.trim();
            if (mediaId == "") {
            	$("#new_media_" + batch_index).addClass('label-danger');
            	$("#new_media_" + batch_index).removeClass('label-success');
            	$("#new_media_" + batch_index).html('新');
            } else {
            	$("#new_media_" + batch_index).addClass('label-success');
            	$("#new_media_" + batch_index).removeClass('label-danger');
            	$("#new_media_" + batch_index).html('存');
            }
        },
    });
});
