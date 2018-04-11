$(function() {
	$.PlayController = function(elm, config) {

	}

	$.extend($.PlayController, {

		toPreview : function(path) {
			$.common.showModal({
				url : contextPath + '/media/play/preview?path=' + path,
				success : function() {
				},
			});
		},

		toPreviewNewWindow : function(path) {
			window.open(contextPath + '/media/play/previewNewWindow?path=' + path);
		},
	})
});