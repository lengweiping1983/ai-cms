$(function() {
	$.PlayController = function(elm, config) {

	}

	$.extend($.PlayController, {

		toPreview: function(path) {
        	$.common.ajaxActionText(contextPath + '/media/play/preview?path=' + path, function (data) {
        		$("#content_list_modal_container_body").html(data);
        		$('#content_list_modal_container_dialog').removeClass("modal-full").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("预览");
        		$("#content_list_modal_container").modal({});
            })
		},
		
		toPreviewMediaFile: function(id) {
        	$.common.ajaxActionText(contextPath + '/media/play/previewMediaFile?id=' + id, function (data) {
        		$("#content_list_modal_container_body").html(data);
        		$('#content_list_modal_container_dialog').removeClass("modal-full").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("预览");
        		$("#content_list_modal_container").modal({});
            })
		},
		
		toPreviewProgram: function(id) {
        	$.common.ajaxActionText(contextPath + '/media/play/previewProgram?id=' + id, function (data) {
        		$("#content_list_modal_container_body").html(data);
        		$('#content_list_modal_container_dialog').removeClass("modal-full").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("预览");
        		$("#content_list_modal_container").modal({});
            })
		},
		
		toPreviewSeries: function(id) {
        	$.common.ajaxActionText(contextPath + '/media/play/previewSeries?id=' + id, function (data) {
        		$("#content_list_modal_container_body").html(data);
        		$('#content_list_modal_container_dialog').removeClass("modal-full").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("预览");
        		$("#content_list_modal_container").modal({});
            })
		},

		toPreviewNewWindow: function(path) {
			window.open(contextPath + '/media/play/previewNewWindow?path=' + path);
		},
	})
});