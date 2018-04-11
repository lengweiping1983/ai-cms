$(function () {
    $.Page = function (elm, config) {

    }

    $.extend($.Page, {
        init: function (config) {
        	var containerId = config.containerId || 'page-content-wrapper';
        	var idPrefix = config.idPrefix || containerId + 'bottom';
            $("." + idPrefix + "left_prev").click(function () {
                var disable = $(this).hasClass("disabled");
                if (disable) {
                    return false;
                }
                var currentPageOrderField = $("#" + idPrefix + "currentPageOrderField").val();
                var currentPageIndex = $("#" + idPrefix + "currentPageIndex").val();
                --currentPageIndex;
                config.page = currentPageIndex;
                config.order = "&order=" + currentPageOrderField;
                $.Page.goToPage(config);
            });

            $("." + idPrefix + "right_next").click(function () {
                var disable = $(this).hasClass("disabled");
                if (disable) {
                    return false;
                }
                var currentPageOrderField = $("#" + idPrefix + "currentPageOrderField").val();
                var currentPageIndex = $("#" + idPrefix + "currentPageIndex").val();
                ++currentPageIndex;
                config.page = currentPageIndex;
                config.order = "&order=" + currentPageOrderField;
                $.Page.goToPage(config);
            });

            $("." + idPrefix + "left_double_prev").click(function () {
                var disable = $(this).hasClass("disabled");
                if (disable) {
                    return false;
                }
                var currentPageOrderField = $("#" + idPrefix + "currentPageOrderField").val();
                config.page = 0;
                config.order = "&order=" + currentPageOrderField;
                $.Page.goToPage(config);
            });

            $("." + idPrefix + "right_double_next").click(function () {
                var disable = $(this).hasClass("disabled");
                if (disable) {
                    return false;
                }
                var currentPageOrderField = $("#" + idPrefix + "currentPageOrderField").val();
                var currentPageIndex = $("#" + idPrefix + "totalPages").val();
                config.page = currentPageIndex - 1;
                config.order = "&order=" + currentPageOrderField;
                $.Page.goToPage(config);
            });

            $("." + idPrefix + "index_page_number").click(function () {
                var currentPageOrderField = $("#" + idPrefix + "currentPageOrderField").val();
                var currentPageIndex = $(this).attr("id");
                config.page = currentPageIndex;
                config.order = "&order=" + currentPageOrderField;
                $.Page.goToPage(config);
            });

            $("." + idPrefix + "go_page").click(function(e) {
                var totalPages = parseInt($("#" + idPrefix + "totalPages").val());
                var currentPageIndex = parseInt($("#" + idPrefix + "goPage").val());
                if (currentPageIndex > totalPages) {
                    currentPageIndex = totalPages;
                }
                --currentPageIndex;
                if (currentPageIndex < 0) {    
                    currentPageIndex = 0;
                }
                var currentPageOrderField = $("#" + idPrefix + "currentPageOrderField").val();
                config.page = currentPageIndex;
                config.order = "&order=" + currentPageOrderField;
                $.Page.goToPage(config);
            });

            //列表排序
            $("table th.sorting").click(function () {
                var currentPageIndex = $("#" + idPrefix + "currentPageIndex").val();
                var orderField = $(this).attr("abbr");
                var currentPageOrderField = "";

                if ($(this).is(".sorting") || $(this).is(".sorting_asc")) {
                    $(this).removeClass("sorting sorting_asc");
                    $(this).addClass("sorting_desc");
                    currentPageOrderField = "-" + orderField;
                } else if ($(this).is(".sorting_desc")) {
                    $(this).removeClass("sorting");
                    $(this).addClass("sorting_asc");
                    currentPageOrderField = "" + orderField;
                }
                $("#" + idPrefix + "currentPageOrderField").val(currentPageOrderField);
                
                config.page = currentPageIndex;
                config.order = "&order=" + currentPageOrderField;
                $.Page.goToPage(config);
            });

            var currentPageOrderField = $("#" + idPrefix + "currentPageOrderField").val();
            if ("" != currentPageOrderField) {
                var isAsc = false;
                if (-1 == currentPageOrderField.indexOf("-")) {
                    isAsc = true;
                } else {
                    currentPageOrderField = currentPageOrderField.substring(1);
                    isAsc = false;
                }
                var orderTh = $("table th[abbr='" + currentPageOrderField + "']");
                if (!isAsc) {
                    $(orderTh).removeClass("sorting sorting_desc sorting_asc");
                    $(orderTh).addClass("sorting_desc");
                } else {
                    $(orderTh).removeClass("sorting sorting_desc sorting_asc");
                    $(orderTh).addClass("sorting_asc");
                }
            }
        },

        goToPage: function (config) {
        	var containerId = config.containerId || 'page-content-wrapper';
        	var idPrefix = config.idPrefix || containerId + 'bottom';
        	var formId = config.formId || "";
            var page = config.page || "0";
            var order = config.order || "";
            
            var pageSize = $("#" + idPrefix + "currentPageSize").val();
            
            
            var form = $(document.forms[0]);
            if(formId != "") {
            	form = $("#" + formId);
            }
            var symbol = "";
            if(form[0].action.indexOf("?") > 0 ) {
            	symbol = "&";
            } else {
            	symbol = "?";
            }
            var requestPath = form[0].action + symbol + "page=" + page + "&size=" + pageSize;
            if (order && "" != order) {
                requestPath += order;
            }
            var data = {};
            var formData = form.serializeArray();
            formData.forEach(function (e) {
                data[e.name] = e.value;
            });

            $.common.ajaxLoadContent({
                url: requestPath,
                type: "POST",
                dataType: "text",
                contentType: "default",
                data: data,
                containerId: containerId,
            });
        },

        refreshCurrentPage: function (config) {
        	if (config == undefined) {
        		var config = {};
        	}
        	var containerId = config.containerId || 'page-content-wrapper';
        	var idPrefix = config.idPrefix || containerId + 'bottom';
            var currentPageOrderField = $("#" + idPrefix + "currentPageOrderField").val();
            var currentPageIndex = $("#" + idPrefix + "currentPageIndex").val();
            
            config.page = currentPageIndex;
            config.order = "&order=" + currentPageOrderField;
            $.Page.goToPage(config);
        },

        refreshFirstPage: function (config) {
        	var containerId = config.containerId || 'page-content-wrapper';
        	var idPrefix = config.idPrefix || containerId + 'bottom';
            var currentPageOrderField = $("#" + idPrefix + "currentPageOrderField").val();
            
            config.page = 0;
            config.order = "&order=" + currentPageOrderField;
            $.Page.goToPage(config);
        },

        queryByForm: function (config) {
        	if (config == undefined) {
        		var config = {};
        	}
        	config.page = 0;
            $.Page.goToPage(config);
        }

    });
});