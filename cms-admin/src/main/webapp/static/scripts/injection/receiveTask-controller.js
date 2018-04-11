$(function() {
	$.ReceiveTaskController = function(elm, config) {

	}

	$.extend(true, $.ReceiveTaskController, $.BaseController, {

		init : function(prefix) {
			$.ReceiveTaskController.initTimepicker();
		},

		detail : function(id) {
			$.common.showModal({
				url : contextPath + "/injection/receiveTask/" + id + "/detail",
				success : function() {

				},
			});
		},

		taskList : function(id) {
			$.common.putLastContentParam();

			$.common.ajaxLoadContent({
				url : contextPath + "/injection/downloadTask/",
				type : "GET",
				dataType : "text",
				contentType : "default",
				data : {
					search_pid__EQ_L : id,
					from : 'ReceiveTask'
				},
				success : function(data) {
				}
			})
		},

	})
});
