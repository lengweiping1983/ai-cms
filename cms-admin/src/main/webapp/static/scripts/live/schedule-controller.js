Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "H+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

$(function() {
	$.ScheduleController = function(elm, config) {

	}

	$.extend($.ScheduleController, {

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

					$.ScheduleController.initDatetimepicker();

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
			json.event = "$.ScheduleController._delete('" + path + "')";
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
				url : contextPath + "/live/schedule/" + id + "/detail",
				success : function() {

				},
			});
		},

		toChangeStatus : function(path, name, statusMethodDesc) {
			var json = {};
			json.title = statusMethodDesc + "操作";
			json.body = "您确认要" + statusMethodDesc + "[" + name + "]吗?";
			json.event = "$.ScheduleController.changeStatus('" + path + "')";
			$.common.showConfirmModal(json);
		},

		changeStatus : function(path) {
			$.common.ajaxActionText(path, function(data) {
				$.common.hideModal();
				$.Page.refreshCurrentPage();
			})
		},

		changeSplitProgram: function (type) {
        	var schedule_div_1 = $("#schedule_div_1");
        	
        	if(type == 0) {
        		schedule_div_1.hide();
        	} else if(type == 1) {
        		schedule_div_1.show();
        	}
        },
        
        changeChannelType: function (type) {
        	if(type == 3) {
        		$("#splitProgram").val(1).change();
        	}
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
			var beginTimeStr = $("#beginTime").val();
			var endTimeStr = $("#endTime").val();
			var liveDurationStr = $("#liveDuration").val();
			if(	beginTimeStr != "" && endTimeStr != "" ) {
				var beginTime = new Date(beginTimeStr);
				var endTime = new Date(endTimeStr);
				if(endTime.getTime() < beginTime.getTime()) {
					alert('开始时间应小于结束时间！');
				}
				var duration = (endTime.getTime() - beginTime.getTime())/(60 * 1000);
				$("#liveDuration").val(duration);
			} else if(	beginTimeStr != "" && liveDurationStr != "" ) {
				var beginTime = new Date(beginTimeStr);
				var endTimeLong = beginTime.getTime() + parseInt(liveDurationStr) * (60 * 1000);
				var endTime = new Date();
				endTime.setTime(endTimeLong);
				$("#endTime").val(endTime.Format("yyyy-MM-dd HH:mm"));
			}
		},

		changeEndDate : function() {
			var beginTimeStr = $("#beginTime").val();
			var endTimeStr = $("#endTime").val();
			var liveDurationStr = $("#liveDuration").val();
			if(	beginTimeStr != "" && endTimeStr != "" ) {
				var beginTime = new Date(beginTimeStr);
				var endTime = new Date(endTimeStr);
				if(endTime.getTime() < beginTime.getTime()) {
					alert('结束时间应大于开始时间！');
				}
				var duration = (endTime.getTime() - beginTime.getTime())/(60 * 1000);
				$("#liveDuration").val(duration);
			} else if(	endTimeStr != "" && liveDurationStr != "" ) {
				var endTime = new Date(endTimeStr);
				var beginTimeLong = endTime.getTime() - parseInt(liveDurationStr) * (60 * 1000);
				var beginTime = new Date();
				beginTime.setTime(beginTimeLong);
				$("#beginTime").val(beginTime.Format("yyyy-MM-dd HH:mm"));
			}
		},
		
		changeDuration : function() {
			var beginTimeStr = $("#beginTime").val();
			var endTimeStr = $("#endTime").val();
			var liveDurationStr = $("#liveDuration").val();
			if(	beginTimeStr != "" && liveDurationStr != "" ) {
				var beginTime = new Date(beginTimeStr);
				var endTimeLong = beginTime.getTime() + parseInt(liveDurationStr) * (60 * 1000);
				var endTime = new Date();
				endTime.setTime(endTimeLong);
				$("#endTime").val(endTime.Format("yyyy-MM-dd HH:mm"));
			} else if(	endTimeStr != "" && liveDurationStr != "" ) {
				var endTime = new Date(endTimeStr);
				var beginTimeLong = endTime.getTime() - parseInt(liveDurationStr) * (60 * 1000);
				var beginTime = new Date();
				beginTime.setTime(beginTimeLong);
				$("#beginTime").val(beginTime.Format("yyyy-MM-dd HH:mm"));
			}
		},

		changeProgramName : function(obj) {
			$.ScheduleController.genProgramNamePinyin(obj.value.trim());
		},

		genProgramNamePinyin : function(name) {
			if (name == "") {
				return;
			}
			var pinyinResult = pinyin.getCamelChars(name.replace(/\s/g, ""));
			$("#searchName").val(pinyinResult);
		},

		toSelectSchedule: function () {
        	$.common.ajaxActionText(contextPath + "/live/schedule/selectSchedule", function (data) {
        		$("#content_list_modal_container_body").html(data);
        		$('#content_list_modal_container_dialog').removeClass("modal-sm").addClass("modal-lg");
        		$('#content_list_modal_container_title').html("选择直播节目");
        		$("#content_list_modal_container").modal({});
        		
        		$("#editForm").validationEngine({
                    ajaxFormValidationMethod: 'post',
                });
            })
        },
        
        selectSchedule: function(id, programName, beginTime, duration, channelId, channelName, channelCode) {
        	$("#scheduleId").val(id);
            $("#scheduleProgramName").val(programName);
            $('#beginTime_div').datetimepicker('setDate', new Date(Date.parse(beginTime.replace(/-/g, "/"))));
            $("#duration").val(duration);
            $("#channelId").val(channelId);
            $("#channelName").val(channelName);
            $("#channelCode").val(channelCode);
            
            $("#content_list_modal_container").modal('hide');
        },
        
		toSelectItem : function() {
			$.common.ajaxActionText(contextPath + "/live/schedule/selectItem",
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

       init: function() {
        	var table = $('#content_list');

            table.find('.group-checkable').change(function () {
            	var checked = jQuery(this).is(":checked");
                if (checked) {
                	$(this).parents('table').find('input[type=checkbox].group-checkable').parents('span').addClass("checked");
                } else {
                	$(this).parents('table').find('input[type=checkbox].group-checkable').parents('span').removeClass("checked");
                }
                $(this).parents('table').find('input[type=checkbox].group-checkable').prop('checked', checked);
                var set = jQuery(this).attr("data-set");
                jQuery(set).each(function () {
                    if (checked) {
                    	$(this).prop('checked', true);
                    	$(this).parents('span').addClass("checked");
                        $(this).parents('tr').addClass("active");
                    } else {
                    	$(this).prop('checked', false);
                    	$(this).parents('span').removeClass("checked");
                        $(this).parents('tr').removeClass("active");
                    }
                });
                jQuery.uniform.update(set);
            });

            table.on('change', 'tbody tr .checkboxes', function () {
            	var checked = jQuery(this).is(":checked");
                if (checked) {
                	$(this).parents('span').addClass("checked");
                } else {
                    $(this).parents('span').removeClass("checked");
                }
                $(this).parents('tr').toggleClass("active");
            });

        },
        
        getItemIds: function () {
        	var itemIds = [];
            $('#content_list tbody tr td input[type=checkbox].checkboxes:checked').each(function () {
            	itemIds.push($(this).val());
            });
            return itemIds;
        },
        
        getItemIdsString: function () {
        	var itemIds = "";
        	$('#content_list tbody tr td input[type=checkbox].checkboxes:checked').each(function (i) {
                if (0 == i) {
                	itemIds = $(this).val();
                } else {
                	itemIds += ("," + $(this).val());
                }
            });
            return itemIds;
        },
        
        batchTo: function (path, itemType) {
        	var itemIds = $.ScheduleController.getItemIds();
	        	if(itemIds==null || itemIds.length==0) {
	        	var json = {
	                body: "请选择节目！",
	            };
	            $.common.showAlertModal(json);
	            return ;
        	}
        	
        	var requestPath = path + "?itemType=" + itemType + "&itemIds=" + itemIds;
        	
            $.common.showModal({
            	url: requestPath,
                type: "POST",
                dataType: "text",
                contentType: "default",
                success: function () {
                },
            });
        },

        batchInjection: function (path) {
            if (!$("#editForm").validationEngine("validate")) return false;

            var json = $("#editForm").serializeObject();

            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: json,
                success: function (data) {
                    $.common.hideModal();
                    $.Page.refreshCurrentPage();
                }
            });
        },
        
        toResetInjectionStatus: function (path, itemType) {
        	var itemIds = $.ScheduleController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
                var json = {
                    body: "请选择元素！",
                };
                $.common.showAlertModal(json);
                return;
            }
            
            var json = {};
            json.title = "重置分发状态操作";
            json.body = "您确认要重置分发状态吗?";
            json.event = "$.ScheduleController.resetInjectionStatus('" + path + "'," + itemType + ")";
            $.common.showConfirmModal(json);
        },
        
        resetInjectionStatus: function (path, itemType) {
        	var itemIds = $.ScheduleController.getItemIdsString();
            
            var json = {};
            json.itemType = itemType;
            json.itemIds = itemIds;
            json.status = 1;

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
                        body: "重置分发状态成功！",
                    };
                    $.common.showAlertModal(json);
                }
            });
        },

		toBatchOffline: function (path, itemType) {
			var itemIds = $.BaseController.getItemIds();
			if (itemIds == null || itemIds.length == 0) {
				var json = {
					body: "请选择节目！",
				};
				$.common.showAlertModal(json);
				return;
			}

			var json = {};
			json.title = "下线操作";
			json.body = "您确认要下线吗?";
			json.event = "$.ScheduleController.batchChangeStatusOffline('" + path + "'," + itemType + ")";
			$.common.showConfirmModal(json);
		},
		batchChangeStatusOffline: function (path, itemType) {
			var itemIds = $.BaseController.getItemIdsString();

			var json = {};
			json.itemType = itemType;
			json.itemIds = itemIds;
			json.status = 2;

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
						body: "下线成功！",
					};
					$.common.showAlertModal(json);
				}
			});
		},

		toBatchOnline: function (path, itemType) {
			var itemIds = $.BaseController.getItemIds();
			if (itemIds == null || itemIds.length == 0) {
				var json = {
					body: "请选择节目！",
				};
				$.common.showAlertModal(json);
				return;
			}

			var json = {};
			json.title = "上线操作";
			json.body = "您确认要上线吗?";
			json.event = "$.BaseController.batchChangeStatusOnline('" + path + "'," + itemType + ")";
			$.common.showConfirmModal(json);
		},

		batchChangeStatusOnline: function (path, itemType) {
			var itemIds = $.BaseController.getItemIdsString();

			var json = {};
			json.itemType = itemType;
			json.itemIds = itemIds;
			json.status = 1;

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
						body: "上线成功！",
					};
					$.common.showAlertModal(json);
				}
			});
		},

		toBatchDelete: function (path, itemType) {
			var itemIds = $.BaseController.getItemIds();
			if (itemIds == null || itemIds.length == 0) {
				var json = {
					body: "请选择节目！",
				};
				$.common.showAlertModal(json);
				return;
			}

			var json = {};
			json.title = "删除操作";
			json.body = "您确认要删除吗?";
			json.event = "$.BaseController.batchDelete('" + path + "'," + itemType + ")";
			$.common.showConfirmModal(json);
		},

		batchDelete: function (path, itemType) {
			var itemIds = $.BaseController.getItemIdsString();

			var json = {};
			json.itemType = itemType;
			json.itemIds = itemIds;
			json.status = 1;

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
						body: "删除成功！",
					};
					$.common.showAlertModal(json);
				}
			});
		},
	})
});
