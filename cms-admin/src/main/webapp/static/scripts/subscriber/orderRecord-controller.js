
$(function () {
    $.OrderRecordController = function (elm, config) {

    }

    $.extend($.OrderRecordController, {

        toEdit: function (path, id) {
            if (id == undefined) {
                id = -1;
            }
            $.common.showModal({
                url: path,
                success: function () {
                	
                },
            });
        },

        toUnsubscribe: function (path, desc) {
            var json = {};
            json.title = "退订操作";
            json.body = desc;
            json.event = "$.OrderRecordController.unsubscribe('" + path + "')";
            $.common.showConfirmModal(json);
        },

        unsubscribe: function (path) {
            $.common.ajaxActionText(path, function (data) {
                $.common.hideModal();
                $.Page.refreshCurrentPage();
            })
        },
        
        detail: function (id) {
        	$.common.showModal({
                url: contextPath + "/orderRecord/orderRecord/"+id+"/detail",
                success: function () {
                	
                },
            });
        },
        
        batchExport: function (path, itemType) {
            var itemIds = $.ProgramController.getItemIds();
            if (itemIds == null || itemIds.length == 0) {
                var json = {
                    body: "请选择订单！",
                };
                $.common.showAlertModal(json);
                return;
            }

            var requestPath = path + "?itemType=" + itemType + "&itemIds=" + itemIds;
            window.open(requestPath);
        },
        
        exportAll: function (path, itemType) {
            var form = $(document.forms[0]);
            var requestPath = path + "?1=1";
            var formData = form.serializeArray();
            formData.forEach(function (e) {
            	if (e.value && "" != e.value) {
            		requestPath = requestPath + "&" + e.name + "=" + e.value;
            	}
            });
            window.open(requestPath);
        },

        changeOrderDate:function(){
        },

        changeValidTime:function(){
        },

        changeExpiredTime:function(){
        },

        toAdd:function(path){
            $.common.showModal({
                url: path,
                success: function () {
                    $.ProgramController.initTimepicker();
                },
            });
        },

        edit: function (path) {
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
            })
        },
        
        init: function (prefix) {
        	try {
	        	$('#'+prefix+'select_cpId__IN_S').multiselect({
	            	enableFiltering: true,
	            	nonSelectedText:'请选择',
	            	filterPlaceholder:'搜索',
	            	nSelectedText:'项被选中',
	            	includeSelectAllOption:false,
	            	selectAllText:'全选/取消全选',
	            	allSelectedText:'已选中所有',
	            });
	            $('#'+prefix+'select_cpId__IN_S').bind("change",function() {
	            	var value = $('#'+prefix+'select_cpId__IN_S').val();
	            	$('#'+prefix+'search_cpId__IN_S').val(value);
	            });
        	} catch(e) {
        		
        	}
            
            var table = $('#'+prefix+'content_list');

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

    })
});