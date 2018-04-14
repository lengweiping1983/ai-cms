$(function() {
	$.ChannelConfigController = function(elm, config) {

	}

	$.extend($.ChannelConfigController, {

		toEdit : function(path, id) {
			if (id == undefined) {
				id = -1;
			}
			$.common.showModal({
				url : path,
				success : function() {
					$("#editForm").validationEngine({
						ajaxFormValidationMethod : 'post',
					});

					$('.fileinput_image1').on('change.bs.fileinput',
							function(e, files) {
								$("#image1").val("");
								$("#image1Data").val(files.result);
							});
					$('.fileinput_image1').on('clear.bs.fileinput', function() {
						$("#image1").val("");
					});

					$('.fileinput_image2').on('change.bs.fileinput',
							function(e, files) {
								$("#image2").val("");
								$("#image2Data").val(files.result);
							});
					$('.fileinput_image2').on('clear.bs.fileinput', function() {
						$("#image2").val("");
					});

					$.ChannelConfigController.initDatetimepicker();

				},
			});
		},

		edit : function(path) {
			if (!$("#editForm").validationEngine("validate"))
				return false;

			var json = $("#editForm").serializeObject();

			var bean = {
				data : json,
				image1Data : $("#image1Data").val(),
				image2Data : $("#image2Data").val(),
			};

			$.common.ajaxAction({
				url : path,
				type : "POST",
				dataType : "json",
				contentType : "application/json;charset=utf-8",
				data : bean,
				success : function(data) {
					$.common.hideModal();
					$.Page.refreshCurrentPage();
				}
			})
		},

		toDelete : function(path, name) {
			var json = {};
			json.title = "删除操作";
			json.body = "您确认要删除[" + name + "]吗?";
			json.event = "$.ChannelConfigController._delete('" + path + "')";
			$.common.showConfirmModal(json);
		},

		_delete : function(path) {
			$.common.ajaxActionText(path, function(data) {
				$.common.hideModal();
				$.Page.refreshCurrentPage();
			})
		},

		detail : function(id) {
			$.common.showModal({
				url : contextPath + "/live/channelConfig/" + id + "/detail",
				success : function() {

				},
			});
		},

		toChangeStatus : function(path, name, statusMethodDesc) {
			var json = {};
			json.title = statusMethodDesc + "操作";
			json.body = "您确认要" + statusMethodDesc + "[" + name + "]吗?";
			json.event = "$.ChannelConfigController.changeStatus('" + path + "')";
			$.common.showConfirmModal(json);
		},

		changeStatus : function(path) {
			$.common.ajaxActionText(path, function(data) {
				$.common.hideModal();
				$.Page.refreshCurrentPage();
			})
		},

		changeTimeShift: function (type) {
        	var channelConfig_div_3 = $("#channelConfig_div_3");
        	if(type == 0) {
        		channelConfig_div_3.hide();
        	} else if(type == 1) {
        		channelConfig_div_3.show();
        	}
        },
        
		changeLookBack: function (type) {
        	var channelConfig_div_1 = $("#channelConfig_div_1");
        	var channelConfig_div_2 = $("#channelConfig_div_2");
        	if(type == 0) {
        		channelConfig_div_1.hide();
        		channelConfig_div_2.hide();
        	} else if(type == 1) {
        		channelConfig_div_1.show();
        		channelConfig_div_2.show();
        	}
        },
        
        changeChannelType: function (type) {
        },
        
		initDatetimepicker : function() {
			if (jQuery().datetimepicker) {
				$(".form_datetime").datetimepicker({
		            autoclose: true,
		            isRTL: App.isRTL(),
		            language : 'zh-CN',
		            format: "yyyy-mm-dd hh:ii",
		            pickerPosition: (App.isRTL() ? "bottom-left" : "bottom-left")
		        });
			}
		},

		changeBeginDate : function() {
		},

		changeEndDate : function() {
		},

		changeProgramName : function(obj) {
			$.ChannelConfigController.genProgramNamePinyin(obj.value.trim());
		},

		genProgramNamePinyin : function(name) {
			if (name == "") {
				return;
			}
			var pinyinResult = pinyin.getCamelChars(name.replace(/\s/g, ""));
			$("#searchName").val(pinyinResult);
		},

		toSelectItem : function() {
			$.common.ajaxActionText(contextPath + "/live/channelConfig/selectItem",
					function(data) {
						$("#content_list_modal_container_body").html(data);
						$('#content_list_modal_container_dialog').removeClass(
								"modal-sm").addClass("modal-lg");
						$('#content_list_modal_container_title').html("选择节目");
						$("#content_list_modal_container").modal({});

						$("#editForm").validationEngine({
							ajaxFormValidationMethod : 'post',
						});
					})
		},

		selectItem : function(id, name, title, image1, image2, status) {
			$("#itemId").val(id);
			$("#itemName").val(name);
			$("#itemTitle").val(title);
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
			$("#itemStatus").val(status);
			$("#content_list_modal_container").modal('hide');
		},

	})
});
