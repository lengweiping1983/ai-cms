function trim(str){ //删除左右两端的空格   
	return str.replace(/(^\s*)|(\s*$)/g, "");
}  
function ltrim(str){ //删除左边的空格   
	return str.replace(/(^\s*)/g,"");  
}  
function rtrim(str){ //删除右边的空格   
	return str.replace(/(\s*$)/g,"");  
}

//对form表单json格式化,可以直接json提交到后台,直接封装成对象
$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name] != undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(trim(this.value || ''));
        } else {
            o[this.name] = trim(this.value || '');
        }
    });
    return o;
}

var currentContentRequestPath = "";
var currentContentData = {};

var lastContentRequestPath = "";
var lastContentData = {};

var stackContentRequestPath = [];
var stackContentData = [];

$(function () {
    $.common = function (config) {
        this.config = config;
    }

    $.extend($.common, {

        /**
         * 自定义ajax请求
         * contextType:
         * text/html                            html
         * text/plain                           text
         * application/xml                      xml
         * application/json                     json
         * application/x-www-form-urlencoded   default
         * @param config
         */
        ajaxAction: function (config) {
            var url = config.url;
            var type = config.type || "POST";
            var dataType = config.dataType || "JSON";
            var contentType = config.contentType || "application/json;charset=utf-8";
            var data = config.data || [];
            var success = config.success;
            var error = config.error;
            var errorCallBack = config.errorCallBack;
            var args = data;
            if (-1 != contentType.toLowerCase().indexOf("json")) {
                args = JSON.stringify(data);
            }
            if (contentType.toLowerCase() == 'default') {
                contentType = "application/x-www-form-urlencoded";
            }
            $.common.blockSubmitUI(config);
            $.ajax({
                url: url,
                type: type,
                cache: false,
                async: true,
                dataType: dataType,
                contentType: contentType,
                data: args,
                success: function (data) {
                	$.common.unblockSubmitUI(config);
                    if (success) {
                        success(data);
                    }
                    return data;
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                	$.common.unblockSubmitUI(config);
                    if (XMLHttpRequest.status == 911) {
                        return;
                    }
                    if (error) {
                        error(XMLHttpRequest, textStatus, errorThrown);
                    } else if (XMLHttpRequest.responseText && XMLHttpRequest.responseText != '') {
                        var $arr = $('form div .message-span-css');
                        if ($arr && $arr.length > 0) {
                            $arr.each(function () {
                                $(this).html(XMLHttpRequest.responseText);
                            });
                            $('form .alert-danger').show();
                        } else {
                            $.common.hideConfirmModal();
                            $.common.hideAlertModal();
                            var json = {
                                body: XMLHttpRequest.responseText,
                            };
                            $.common.showAlertModal(json);
                        }
                    }
                    if (errorCallBack) {
                    	errorCallBack(XMLHttpRequest, textStatus, errorThrown);
                    }
                }
            });
        },
        
        /**
         * 自定义ajax请求
         * contextType:
         * text/html                            html
         * text/plain                           text
         * application/xml                      xml
         * application/json                     json
         * application/x-www-form-urlencoded   default
         * @param config
         */
        ajaxActionFile: function (config) {
            var url = config.url;
            var data = config.data || [];
            var success = config.success;
            var error = config.error;
            var errorCallBack = config.errorCallBack;
            $.common.blockSubmitUI(config);
            $.ajax({
                url: url,
                type: 'POST',
                data: data,
                async: true,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                	$.common.unblockSubmitUI(config);
                    if (success) {
                        success(data);
                    }
                    return data;
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                	$.common.unblockSubmitUI(config);
                    if (XMLHttpRequest.status == 911) {
                        return;
                    }
                    if (error) {
                        error(XMLHttpRequest, textStatus, errorThrown);
                    } else if (XMLHttpRequest.responseText && XMLHttpRequest.responseText != '') {
                        var $arr = $('form div .message-span-css');
                        if ($arr && $arr.length > 0) {
                            $arr.each(function () {
                                $(this).html(XMLHttpRequest.responseText);
                            });
                            $('form .alert-danger').show();
                        } else {
                            $.common.hideConfirmModal();
                            $.common.hideAlertModal();
                            var json = {
                                body: XMLHttpRequest.responseText,
                            };
                            $.common.showAlertModal(json);
                        }
                    }
                    if (errorCallBack) {
                    	errorCallBack(XMLHttpRequest, textStatus, errorThrown);
                    }
                }
            });
        },

        /**
         * ajax加载右边的内容
         */
        ajaxLoadContent: function (config) {
        	if (config == undefined) {
        		var config = {};
        	}
            var containerId = config.containerId || "page-content-wrapper";
            $.common.blockUI(config);

            currentContentRequestPath = config.url;
            currentContentData = config.data;

            $.common.ajaxAction({
                url: config.url,
                type: config.type,
                dataType: config.dataType,
                contentType: config.contentType,
                data: config.data,
                success: function (data) {
                	$.common.unblockUI(config);
                	
                    $("#" + containerId).html(data);
                    
                    if (config.success) {
                        config.success(data);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                	$.common.unblockUI(config);
                	
                    if (XMLHttpRequest.status != 911) {
                        $("#" + containerId).html(XMLHttpRequest.responseText);
                    }
                },
            });
        },

        pushStackContentParam: function () {
        	stackContentRequestPath.push(currentContentRequestPath);
        	stackContentData.push(currentContentData);
        },
                
        putLastContentParam: function () {
            lastContentRequestPath = currentContentRequestPath;
            lastContentData = currentContentData;
        },
        
        ajaxLoadLastContent: function (config) {
        	if (config == undefined) {
        		var config = {};
        	}
            var containerId = config.containerId || "page-content-wrapper";
            $.common.ajaxLoadContent({
                url: lastContentRequestPath,
                type: "GET",
                dataType: "text",
                contentType: "default",
                containerId: containerId,
                data: lastContentData,
                success: function (data) {
                }
            })
        },

        showModal: function (config) {
        	if (config == undefined) {
        		var config = {};
        	}
            var modalId = config.modalId || "content_modal_container";
            $.common.ajaxAction({
                url: config.url,
                type: 'GET',
                dataType: 'text',
                success: function (data) {
                    var $modal = $("#" + modalId);
                    $modal.html(data);
                    var fadeModal = $modal.find(".modal");
                    $(fadeModal).addClass("content_modal_css");

                    $modal.find(".content_modal_css").modal('show').on('shown.bs.modal', function () {
                        if (config.shown) {
                            config.shown();
                        } else {
                            $modal.find("input[type=text][readonly!=readonly]:visible:enabled:first").each(function () {
                                this.focus();
                            });
                        }
                    });
                    if (config.success) {
                        config.success(data);
                    }
                },
                error: config.error,
            });
        },

        hideModal: function () {
            $.common.hideContentModal();
            $.common.hideAlertModal();
            $.common.hideConfirmModal();

            $(".content_modal_css").modal('hide');
            $(".modal-open").removeClass("modal-open");
            $(".modal-backdrop").remove();
        },

        hideContentModal: function () {
            $(".content_modal_css").modal('hide').on('hidden.bs.modal', function () {
                $("#content_modal_container").html("");
            });
        },

        showAlertModal: function (json) {
            var tmpl = doT.template($("#alert_modal_template").html());
            var html = tmpl(json);

            var modalId = "alert_modal_container";

            if ($("#" + modalId).length == 0) {
                var modalContainer = $('<div id="' + modalId + '"></div>');
                $("#page-content-wrapper").append(modalContainer);
            }
            $("#" + modalId).html(html);
            $("#alert_modal_dialog").modal('show');
        },

        hideAlertModal: function (json) {
            $("#alert_modal_dialog").modal('hide').on('hidden.bs.modal', function () {
                $("#alert_modal_container").html("");
            });
        },

        showConfirmModal: function (json) {
            var tmpl = doT.template($("#confirm_modal_template").html());
            var html = tmpl(json);

            var modalId = "confirm_modal_container";

            if ($("#" + modalId).length == 0) {
                var modalContainer = $('<div id="' + modalId + '"></div>');
                $("#page-content-wrapper").append(modalContainer);
            }
            $("#" + modalId).html(html);
            $("#confirm_modal_dialog").modal('show');
        },

        hideConfirmModal: function () {
            $("#confirm_modal_dialog").modal('hide').on('hidden.bs.modal', function () {
                $("#confirm_modal_container").html("");
            });
        },

        focus: function (config) {
        	if (config == undefined) {
        		var config = {};
        	}
            var modalId = config.modalId || "content_modal_container";
            var $modal = $("#" + modalId);
            $modal.find("input:focus").each(function () {
                this.blur();
            });
            $("#" + config.id).focus();
        },

        blockUI: function (config) {
        	if (config == undefined) {
        		var config = {};
        	}
            var containerId = config.containerId || "page-content-wrapper";
            var message = config.message || "努力加载中...";
            App.blockUI({
                target: "#" + containerId,
                message: message,
                boxed: true
            });
            
            window.setTimeout(function() {
                App.unblockUI('#' + containerId);
            }, 2000);
            
        },

        unblockUI: function (config) {
        	if (config == undefined) {
        		var config = {};
        	}
            var containerId = config.containerId || "page-content-wrapper";
            App.unblockUI("#" + containerId);
        },
        
        blockSubmitUI: function (config) {
        	if (config == undefined) {
        		return;
        	}
        	var submitContainerId = config.submitContainerId || "";
        	if(submitContainerId != "") {
        		App.blockUI({
                    target: '#' + submitContainerId + '',
                    overlayColor: 'none',
                    animate: true
                });
        	}
        },

        unblockSubmitUI: function (config) {
        	if (config == undefined) {
        		return;
        	}
            var submitContainerId = config.submitContainerId || "";
            if(submitContainerId != "") {
            	App.unblockUI('#' + submitContainerId + '');
            }
        },

        /**
         * 一个ajax get请求,要求返回值为json
         * @param url
         * @param success
         * @param error
         */
        ajaxActionJson: function (url, success, error) {
            $.common.ajaxAction({
                url: url,
                type: 'GET',
                dataType: 'json',
                success: success,
                error: error,
            });
        },

        /**
         * 一个ajax get请求,要求返回值为text
         * @param url
         * @param success
         * @param error
         */
        ajaxActionText: function (url, success, error) {
            $.common.ajaxAction({
                url: url,
                type: 'GET',
                dataType: 'text',
                success: success,
                error: error,
            });
        },

    });
});