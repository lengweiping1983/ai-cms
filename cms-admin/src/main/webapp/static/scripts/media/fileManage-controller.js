$(function() {
	$.FileManageController = function(elm, config) {

	}

	$.extend(true, $.FileManageController, $.BaseController, {

		init: function (prefix) {
        	$.TranscodeRequestController.initTableCheckBox(prefix);
        },
        
		toRename : function(path, name) {
			$.common.showModal({
				url : path,
				success : function() {
					$("#editForm").validationEngine({
						ajaxFormValidationMethod : 'post',
					});
				},
			});
		},

		toPathChange : function() {
			var from = $("#from").val();
			var to = $("#to").val();

			var $renameButton = $("#renameButton");
			if (from == to) {
				$renameButton.removeAttr("disabled");
			} else {
				$renameButton.attr("disabled", "disabled");
			}
		},

		rename : function(path) {
			if (!$("#editForm").validationEngine("validate"))
				return false;

			var from = $("#from").val();
			var to = $("#to").val();

			if (from == to) {
				$.common.hideAlertModal();
				var json = {
					body : '原路径与目标路径不能相同!',
				};
				$.common.showAlertModal(json);
				return;
			}

			var json = {};
			json.title = "重命名操作";
			json.body = "您确认要重命名吗?";
			json.event = "$.FileManageController.renameSubmit('" + path + "')";
			$.common.showConfirmModal(json);
		},

		renameSubmit : function(path) {
			if (!$("#editForm").validationEngine("validate"))
				return false;

			var json = $("#editForm").serializeObject();

			$.common.ajaxAction({
				url : path,
				type : "POST",
				dataType : "json",
				contentType : "application/json;charset=utf-8",
				data : json,
				success : function(data) {
					$.common.hideModal();
					$.Page.refreshCurrentPage();
				}
			})
		},

		toDelete : function(path, name) {
			var json = {};
			json.title = "删除操作,谨慎";
			json.body = "您确认要删除[" + name + "]吗?";
			json.event = "$.FileManageController._delete('" + path + "')";
			$.common.showConfirmModal(json);
		},

		_delete : function(path) {
			$.common.ajaxActionText(path, function(data) {
				$.common.hideModal();
				$.FileManageController.refresh();
			})
		},
		
		toBatchDelete: function (prefix, path) {
            var filePaths = $.FileManageController.getFilePaths(prefix);
            if (filePaths == null || filePaths.length == 0) {
                var json = {
                    body: "请选择文件！",
                };
                $.common.showAlertModal(json);
                return;
            }
            var filePathInput = "";
            for (var i = 0; i < filePaths.length; i++) {
            	filePathInput += '<input type="hidden" name="paths" value="' + filePaths[i] + '">';
            }
            $.common.showModal({
                url: path,
                type: "GET",
                dataType: "text",
                contentType: "default",
                success: function () {
                	$("#filePathList").html(filePathInput);
                },
            });
        },
		
        batchDelete: function (prefix, path) {
        	if (!$("#editForm").validationEngine("validate")) return false;
            var password = $("#password").val();
            if(password != null && password == '123456') {
            	var json = $("#editForm").serializeObject();
            	$.common.ajaxAction({
    				url : path,
    				type : "POST",
    				dataType : "json",
    				contentType : "application/json;charset=utf-8",
    				data : json,
    				success : function(data) {
    					$.common.hideModal();
    					$.FileManageController.refresh();
    				}
    			})
            } else {
            	$.common.hideModal();
        		var json = {
                    body: "密码错误！",
                };
                $.common.showAlertModal(json);
                return;
            }
        },
        
        jump : function(path) {
			$("#path").val(path);
			$.Page.queryByForm();
		},
        
		refresh : function() {
			$("#refresh").val(1);
			$.Page.queryByForm();
		},
		
		selectFileJump: function (path) {
            $("#selectFile_path").val(path);
            $.Page.queryByForm({containerId: 'content_list_modal_container_body', formId: 'selectFile'});
        },

        getFilePaths: function (prefix) {
            var filePaths = [];
            $('#' + prefix + 'content_list tbody tr td input[type=checkbox].checkboxes:checked')
                .each(function () {
                    filePaths.push($(this).val());
                });
            return filePaths;
        },

        toBatch: function (prefix) {
            var filePaths = $.FileManageController.getFilePaths(prefix);
            if (filePaths == null || filePaths.length == 0) {
                var json = {
                    body: "请选择文件！",
                };
                $.common.showAlertModal(json);
                return;
            }
            var type = $("#type").val();
            var trNum = $("#transcodeRequest_file_list").children().length;
            var time = new Date().getTime();
            for (var i = 0; i < filePaths.length; i++) {
            	var batch_index = i + trNum;
                var fileId = time + "_" + i;
                var filePath = filePaths[i];
                var tr = '<tr id="tr_file_' + fileId
                    + '" class="tr_file_css">';
                tr = tr
                    + '<td><input type="hidden" name="id" value=""><input type="hidden" name="filePath" value="'
                    + filePath + '">' + filePath + '</td>';

                if (type == 1) {

                } else if (type == 2) {
                    tr = tr
                        + '<td><input type="text" name="episodeIndex" value="'
                        + (i + trNum)
                        + '" class="form-control input-small input-inline validate[required,custom[integer]]"></td>';
                }
                if (type == 1 || type == 2) {
                	tr = tr + '<td><select name="mediaFileType"><option value="1">正片</option><option value="2">片花</option></select></td>';
                } else if (type == 3) {
                	var filename = filePath;
                	if(filename.indexOf('/') >= 0) {
                		filename = filename.substr(filename.indexOf('/') + 1);
                	}
                	if(filename.lastIndexOf('/') > 0) {
                		filename = filename.substr(filename.lastIndexOf('/') + 1);
                	}
                	if(filename.lastIndexOf('.') > 0) {
                		filename = filename.substr(0, filename.lastIndexOf('.'));
                	}
                	var pinyinResult = pinyin.getCamelChars(filename.replace(/\s/g, ""));
                	tr = tr + '<td>';
                	tr = tr + '<span class="label label-sm label-danger" id="new_media_' + batch_index + '">新</span>';
                	tr = tr + '<input type="hidden" name="mediaId" id="mediaId_' + batch_index + '" onChange="$.TranscodeRequestController.changeMediaIdFromBatch(this, ' + batch_index + ');"/>';
                	tr = tr + '<input type="text" name="mediaName" id="mediaName_' + batch_index + '" value="' + filename + '" placeholder="点击我选择媒资" onChange="$.TranscodeRequestController.changeMediaNameFromBatch(this, ' + batch_index + ');" class="form-control input-inline validate[required]">';
                	tr = tr + '<button class="btn btn-default btn-sm btn-outline green" type="button" onclick="$.TranscodeRequestController.toBatchSelectMedia(\'1\', ' + batch_index + ');"> <i class="fa fa-arrow-left fa-fw" /></i> 选择媒资 </button>';
                	tr = tr + '</td>';
                	tr = tr + '<td><input type="text" name="mediaFilename" id="mediaFilename_' + batch_index + '" value="' + pinyinResult + '" class="form-control input-inline validate[required]"></td>';
                }
                tr = tr
                    + '<td><button type="button" class="btn btn-default btn-sm btn-outline green" onclick="$.TranscodeRequestController.toDeleteFile(\''
                    + fileId + '\',\'' + filePath
                    + '\');">';
                tr = tr + '<i class="fa fa-remove"></i>删除';
                tr = tr + '</button></td></tr>';
                $("#tr_file_hidden").before(tr);
            }
            $("#content_list_modal_container").modal('hide');
        },
        
        toTrimRequest: function (path) {
        	var json = {};
			json.title = "去特殊字符操作";
			json.body = "您确认要去文件名中的特殊字符吗?";
			json.event = "$.FileManageController.trimSubmit('" + path + "')";
			$.common.showConfirmModal(json);
        },
        
        trimSubmit: function (path) {
        	var filePath = $("#path").val();
        	path = path + "?path=" + filePath;
            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: {path: filePath},
                success: function (data) {
                    $.common.hideModal();
                    $.FileManageController.refresh();
                }
            });
        },
        
        toEditRequest: function (prefix, path) {
            var filePaths = $.FileManageController.getFilePaths(prefix);
            if (filePaths == null || filePaths.length == 0) {
                var json = {
                    body: "请选择文件！",
                };
                $.common.showAlertModal(json);
                return;
            }
            $.common.showModal({
                url: path,
                success: function () {
                    var type = $("#type").val();
                    var trNum = $("#transcodeRequest_file_list").children().length;
                    var time = new Date().getTime();
                    for (var i = 0; i < filePaths.length; i++) {
                    	var batch_index = i + trNum;
                        var fileId = time + "_" + i;
                        var filePath = filePaths[i];
                        var tr = '<tr id="tr_file_' + fileId
                            + '" class="tr_file_css">';
                        tr = tr
                            + '<td><input type="hidden" name="id" value=""><input type="hidden" name="filePath" value="'
                            + filePath + '">' + filePath + '</td>';

                        if (type == 1) {

                        } else if (type == 2) {
                            tr = tr
                                + '<td><input type="text" name="episodeIndex" value="'
                                + (i + trNum)
                                + '" class="form-control input-small input-inline validate[required,custom[integer]]"></td>';
                        }
                        if (type == 1 || type == 2) {
                        	tr = tr + '<td><select name="mediaFileType"><option value="1">正片</option><option value="2">片花</option></select></td>';
                        } else if (type == 3) {							
                        	var filename = filePath;
                        	if(filename.indexOf('/') >= 0) {
                        		filename = filename.substr(filename.indexOf('/') + 1);
                        	}
                        	if(filename.lastIndexOf('/') > 0) {
                        		filename = filename.substr(filename.lastIndexOf('/') + 1);
                        	}
                        	if(filename.lastIndexOf('.') > 0) {
                        		filename = filename.substr(0, filename.lastIndexOf('.'));
                        	}
                        	var pinyinResult = pinyin.getCamelChars(filename.replace(/\s/g, ""));
                        	tr = tr + '<td>';
                        	tr = tr + '<span class="label label-sm label-danger" id="new_media_' + batch_index + '">新</span>';
                        	tr = tr + '<input type="hidden" name="mediaId" id="mediaId_' + batch_index + '" onChange="$.TranscodeRequestController.changeMediaIdFromBatch(this, ' + batch_index + ');"/>';
                        	tr = tr + '<input type="text" name="mediaName" id="mediaName_' + batch_index + '" value="' + filename + '" placeholder="点击我选择媒资" onChange="$.TranscodeRequestController.changeMediaNameFromBatch(this, ' + batch_index + ');" class="form-control input-inline validate[required]">';
                        	tr = tr + '<button class="btn btn-default btn-sm btn-outline green" type="button" onclick="$.TranscodeRequestController.toBatchSelectMedia(\'1\', ' + batch_index + ');"> <i class="fa fa-arrow-left fa-fw" /></i> 选择媒资 </button>';
                        	tr = tr + '</td>';
                        	tr = tr + '<td><input type="text" name="mediaFilename" id="mediaFilename_' + batch_index + '" value="' + pinyinResult + '" class="form-control input-inline validate[required]"></td>';
                        }
                        
                        tr = tr
                            + '<td><button type="button" class="btn btn-default btn-sm btn-outline green" onclick="$.TranscodeRequestController.toDeleteFile(\''
                            + fileId + '\',\'' + filePath
                            + '\');">';
                        tr = tr + '<i class="fa fa-remove"></i>删除';
                        tr = tr + '</button></td></tr>';
                        $("#tr_file_hidden").before(tr);
                    }
                    
                    $.TranscodeRequestController.initCpSelect(prefix);
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

        toSelectItem: function (path) {
            $.common.ajaxActionText(path, function (data) {
                $("#content_list_modal_container_body").html(data);
                $('#content_list_modal_container_dialog')
                    .removeClass("modal-sm").addClass(
                    "modal-lg");
                $('#content_list_modal_container_title').html(
                    "选择FTP服务器上的文件");
                $("#content_list_modal_container").modal({});
            })
        },
        
        selectFile: function (selectMode,selectParam,name,path) {
        	if (path && path != '') {
        		$("#" + selectParam + "filePath").val(path).change();
        	}
        	$("#content_list_modal_container").modal('hide');
        },
	})
});
