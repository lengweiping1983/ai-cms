$(function () {
    $.ProgramController = function (elm, config) {

    }
    
    $.extend(true, $.ProgramController, $.BaseController, {

    	init: function (prefix) {
    		$.ProgramController.initTimepicker();
    		
        	$.ProgramController.initCpSearch(prefix);
        	
        	$.ProgramController.initTableCheckBox(prefix);
        },
        
        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                	$.ProgramController.initTimepicker();
                	
                	$.ProgramController.initCpSelect();
                	
                	$.ProgramController.initImage('image1');
                	$.ProgramController.initImage('image2');
                	$.ProgramController.initImage('image3');
                	$.ProgramController.initImage('image4');
                	
                	$("#editForm").validationEngine({
                        ajaxFormValidationMethod: 'post',
                    });                	
                },
            });
        },
        
		genSearchName: function(obj) {
			var pinyinResult = $.ProgramController.genNamePinyin(obj.value.trim());
			$("#searchName").val(pinyinResult);
		},
		genDirectorPinyin: function(obj) {
			var pinyinResult = $.ProgramController.genNamePinyin(obj.value.trim());
			$("#directorPinyin").val(pinyinResult);
		},
		genActorPinyin: function(obj) {
			var pinyinResult = $.ProgramController.genNamePinyin(obj.value.trim());
			$("#actorPinyin").val(pinyinResult);
		},

        edit: function (path, defaultPrompt) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();

            var bean = {
                data: json,
                image1Data: $("#image1Data").val(),
                image2Data: $("#image2Data").val(),
                image3Data: $("#image3Data").val(),
                image4Data: $("#image4Data").val(),
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
                    var alertMessage = data.message || defaultPrompt;
                    if (alertMessage && alertMessage != "") {
	                    var json = {
	                        body: alertMessage,
	                    };
	                    $.common.showAlertModal(json);
                    }
                }
            })
        },
        
        detail: function (path) {
        	$.common.showModal({
                url: path,
                success: function () {
                	$.ProgramController.initTimepicker();
                	
                	$.ProgramController.initCpSelect();
                	
                	$.ProgramController.initImage('image1');
                	$.ProgramController.initImage('image2');
                	$.ProgramController.initImage('image3');
                	$.ProgramController.initImage('image4');
                },
            });
        },
        
        toBatchChangeMetadata: function (path, itemType) {
        	$.ProgramController.toBatch(path, itemType, function () {
                $('.make-switch').bootstrapSwitch();
                
                $.ProgramController.initSwitch("contentType");
                $.ProgramController.initSwitch("tag");
                $.ProgramController.initSwitch("keyword");
                
                $.ProgramController.initSwitch("director");
                $.ProgramController.initSwitch("actor");
                $.ProgramController.initSwitch("compere");
                $.ProgramController.initSwitch("guest");
                $.ProgramController.initSwitch("year");
                $.ProgramController.initSwitch("area");
                $.ProgramController.initSwitch("language");
                
                $.ProgramController.initSwitch("kuoZhanOne");
                $.ProgramController.initSwitch("kuoZhanTwo");
                
                $.ProgramController.initSwitch("cpId", "select_cpId");
                $.ProgramController.initCpSelect();
            });
        },
        
        changeSelectType: function (type) {
        	var program_div_1 = $("#program_div_1");
        	if (type == 1) {
        		program_div_1.hide();
        	} else if(type == 2) {
        		program_div_1.show();
        	}
        },
        
        changeSelectContentType: function (type) {
        	
        },
        
        toSelectProgram: function (selectMode, selectParam) {
        	var addParam = "";
        	if (selectMode) {
        		addParam = "?selectMode=" + selectMode;
        		if (selectParam) {
            		addParam += "&selectParam=" + selectParam;
            	}
        	}
        	$.ProgramController.toSelect(contextPath + "/media/program/selectProgram" + addParam, 1);
        },
        
        selectProgram: function (selectMode, selectParam, id,name,title,contentType,tag,internalTag,episodeIndex,duration,status,cpId,filename,image1,image2,image3,image4) {
        	if (selectMode == 'media') {
        		$("#mediaId").val(id);
		        $("#mediaName").val(name);
		        if (filename && filename != "") {
		        	$("#mediaFilename").val(filename);
		        } else {
		        	var pinyinResult = $.SeriesController.genNamePinyin(name);
					$("#mediaFilename").val(pinyinResult);
		        }
		        $("#contentType").val(contentType);
		        $("#tag").val(tag);
	            $("#internalTag").val(internalTag);
	            $("#cpId").val(cpId);
        	} else if (selectMode == 'batch_media') {
        		$("#mediaId_" + selectParam).val(id);
                $("#mediaId_" + selectParam).trigger("change");
                $("#mediaName_" + selectParam).val(name);
                $("#mediaFilename_" + selectParam).val(filename);
        	} else {
		    	$("#programId").val(id);
		        $("#programName").val(name);
		        try {
		        	$("#contentType").val(contentType).change();
		        	var liveProgramName = $("#liveProgramName").val();
		        	if (liveProgramName == '') {
		        		$("#liveProgramName").val(name).change();
		        	}
		        	var liveDuration = $("#liveDuration").val();
		        	if (liveDuration == '') {
		        		$("#liveDuration").val(duration).change();
		        	}
		        } catch (e) {
		        	
		        }
        	}
            $("#content_list_modal_container").modal('hide');
        },
        
        toSelectItem: function (selectMode, selectParam) {
        	var addParam = "";
        	if (selectMode) {
        		addParam = "?selectMode=" + selectMode;
        		if (selectParam) {
            		addParam += "&selectParam=" + selectParam;
            	}
        	}
        	$.ProgramController.toSelect(contextPath + "/media/program/selectItem" + addParam, 1);
        },
        
        selectItem: function (selectMode, selectParam, id,name,title,contentType,tag,internalTag,episodeIndex,duration,status,cpId,filename,image1,image2,image3,image4) {
        	if (selectMode == 'jump') {
        		$("#jumpItemId").val(id);
	            $("#jumpItemName").val(name);
        	} else {
	        	$("#itemId").val(id);
	            $("#itemName").val(name);
	            $("#itemTitle").val(title);
	            try {
	            	$("#itemDuration").val(duration);
	            } catch(e) {
	            	
	            }
	            $("#itemStatus").val(status);
	            if (image1 != "") {
	            	$("#itemImage1_img").attr("src", imageWebPath + image1);
	            } else {
	            	$("#itemImage1_img").attr("src", noImagePath);
	            }
	            if (image2 != "") {
	            	$("#itemImage2_img").attr("src", imageWebPath + image2);
	            } else {
	            	$("#itemImage2_img").attr("src", noImagePath);
	            }
        	} 
            $("#content_list_modal_container").modal('hide');
        },
        
        mediaFileList: function (path, id) {
        	$.common.pushStackContentParam();
        	
        	$.common.ajaxLoadContent({
        		url: path,
                type: "GET",
                dataType: "text",
                contentType: "default",
                data: {search_programId__EQ_L: id, from: 'program'},
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
                }
            })
        },
        
        toPlayCode: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                },
            });
        },
        
        playCode: function (path, defaultPrompt) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();

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
                    $.Page.refreshCurrentPage();
                    var alertMessage = data.message || defaultPrompt;
                    if (alertMessage && alertMessage != "") {
	                    var json = {
	                        body: alertMessage,
	                    };
	                    $.common.showAlertModal(json);
                    }
                }
            })
        },
                
    })
});
