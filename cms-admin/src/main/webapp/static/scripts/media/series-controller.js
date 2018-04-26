$(function () {
    $.SeriesController = function (elm, config) {

    }
    
    $.extend(true, $.SeriesController, $.BaseController, {

    	init: function (prefix) {
    		$.SeriesController.initTimepicker();
    		
        	$.SeriesController.initCpSearch(prefix);
        	
        	$.SeriesController.initTableCheckBox(prefix);
        },
        
        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                	$.SeriesController.initTimepicker();
                	
                	$.SeriesController.initCpSelect();
                	
                	$.SeriesController.initImage('image1');
                	$.SeriesController.initImage('image2');
                	$.SeriesController.initImage('image3');
                	$.SeriesController.initImage('image4');
                	
                	$("#editForm").validationEngine({
                        ajaxFormValidationMethod: 'post',
                    });
                	
                	$.SeriesController.initFileUpload();
                },
            });
        },
        
		genSearchName: function(obj) {
			var pinyinResult = $.SeriesController.genNamePinyin(obj.value.trim());
			$("#searchName").val(pinyinResult);
		},
		genDirectorPinyin: function(obj) {
			var pinyinResult = $.SeriesController.genNamePinyin(obj.value.trim());
			$("#directorPinyin").val(pinyinResult);
		},
		genActorPinyin: function(obj) {
			var pinyinResult = $.SeriesController.genNamePinyin(obj.value.trim());
			$("#actorPinyin").val(pinyinResult);
		},

        edit: function (path) {
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
                    var json = {
                        body: "保存成功！",
                    };
                    $.common.showAlertModal(json);
                }
            })
        },
        
        detail: function (path) {
        	$.common.showModal({
                url: path,
                success: function () {
                	$.SeriesController.initTimepicker();
                	
                	$.SeriesController.initCpSelect();
                	
                	$.SeriesController.initImage('image1');
                	$.SeriesController.initImage('image2');
                	$.SeriesController.initImage('image3');
                	$.SeriesController.initImage('image4');
                },
            });
        },
        
        toBatchChangeMetadata: function (path, itemType) {
        	$.SeriesController.toBatch(path, itemType, function () {
                $('.make-switch').bootstrapSwitch();
                
                $.SeriesController.initSwitch("contentType");
                $.SeriesController.initSwitch("tag");
                $.SeriesController.initSwitch("keyword");
                
                $.SeriesController.initSwitch("director");
                $.SeriesController.initSwitch("actor");
                $.SeriesController.initSwitch("compere");
                $.SeriesController.initSwitch("guest");
                $.SeriesController.initSwitch("year");
                $.SeriesController.initSwitch("area");
                $.SeriesController.initSwitch("language");
                
                $.SeriesController.initSwitch("kuoZhanOne");
                $.SeriesController.initSwitch("kuoZhanTwo");
                
                $.SeriesController.initSwitch("cpCode", "select_cpCode");
                $.SeriesController.initCpSelect();
            });
        },
                
        changeSelectContentType: function (type) {
        	
        },
        
        toSelectSeries: function (selectMode, selectParam) {
        	var addParam = "";
        	if (selectMode) {
        		addParam = "?selectMode=" + selectMode;
        		if (selectParam) {
            		addParam += "&selectParam=" + selectParam;
            	}
        	}
        	$.SeriesController.toSelect(contextPath + "/media/series/selectSeries" + addParam, 2);
        },
        
        selectSeries: function (selectMode, selectParam, id,name,title,contentType,tag,internalTag,episodeTotal,duration,status,cpCode,filename,image1,image2,image3,image4) {
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
	            $("#cpCode").val(cpCode);
	            try {
	            	$("#episodeTotal").val(episodeTotal);
	            } catch(e) {
	            	
	            }
        	} else if (selectMode == 'batch_media') {
        		$("#mediaId_" + selectParam).val(id);
                $("#mediaId_" + selectParam).trigger("change");
                $("#mediaName_" + selectParam).val(name);
                $("#mediaFilename_" + selectParam).val(filename);
        	} else {
	        	$("#seriesId").val(id);
	            $("#seriesName").val(name);
	            try {
	            	$("#contentType").val(contentType).change();
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
        	$.SeriesController.toSelect(contextPath + "/media/series/selectItem" + addParam, 2);
        },
        
        selectItem: function (selectMode, selectParam, id,name,title,contentType,tag,internalTag,episodeTotal,duration,status,cpCode,filename,image1,image2,image3,image4) {
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
      
        programList: function (path, id) {
        	$.common.putLastContentParam();
        	
        	$.common.ajaxLoadContent({
        		url: path,
                type: "GET",
                dataType: "text",
                contentType: "default",
                data: {search_seriesId__EQ_L: id, from: 'series'},
                success: function (data) {
                }
            })
        },
        
        changeTab: function (type) {
        	var fileuploadArea = $("#fileuploadArea");
        	if (type == 1) {
        		fileuploadArea.hide();
        	} else if(type == 2) {
        		fileuploadArea.show();
        	}
        },
        
        initFileUpload: function () {
            // Initialize the jQuery File Upload widget:
           $('#fileupload').fileupload({
               disableImageResize: false,
               autoUpload: false,
               disableImageResize: /Android(?!.*Chrome)|Opera/.test(window.navigator.userAgent),
               maxFileSize: 5000000,
               acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
               // Uncomment the following to send cross-domain cookies:
               //xhrFields: {withCredentials: true},                
           });

           // Enable iframe cross-domain access via redirect option:
           $('#fileupload').fileupload(
               'option',
               'redirect',
               window.location.href.replace(
                   /\/[^\/]*$/,
                   '/cors/result.html?%s'
               )
           );

           // Upload server status check for browsers with CORS support:
           if ($.support.cors) {
               $.ajax({
                   type: 'HEAD'
               }).fail(function () {
                   $('<div class="alert alert-danger"/>')
                       .text('Upload server currently unavailable - ' +
                               new Date())
                       .appendTo('#fileupload');
               });
           }

           // Load & display existing files:
           $('#fileupload').addClass('fileupload-processing');
           $.ajax({
               // Uncomment the following to send cross-domain cookies:
               //xhrFields: {withCredentials: true},
               url: $('#fileupload').attr("action"),
               dataType: 'json',
               context: $('#fileupload')[0]
           }).always(function () {
               $(this).removeClass('fileupload-processing');
           }).done(function (result) {
               $(this).fileupload('option', 'done')
               .call(this, $.Event('done'), {result: result});
           });
       },
    })
});
