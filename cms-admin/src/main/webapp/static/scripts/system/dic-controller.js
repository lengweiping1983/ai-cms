$(function () {
    $.DicController = function (ele, config) {

    }

    $.extend($.DicController, {

        toAdd: function () {
            var url = contextPath + "/system/dic/add";
            $.common.showModal({url: url});
        },

        toEdit: function (id) {
            var url = contextPath + "/system/dic/" + id + "/edit";
            $.common.showModal({url: url});
        },

        toDelete: function (id, name) {
            var json = {
                title: "删除字典",
                body: "您确定要删除[" + name + "]吗?",
                event: "$.DicController._delete('" + id + "')"
            };
            $.common.showConfirmModal(json);
        },

        _delete: function (id) {
            var url = contextPath + "/system/dic/" + id + "/delete";
            $.common.ajaxActionText(url, function (data) {
                $.common.hideConfirmModal();
                $.Page.refreshCurrentPage();
            })
        },

        edit: function () {
            if (!$("#dic_edit_form").validationEngine("validate")) {
                return false;
            }
            var json = $("#dic_edit_form").serializeObject();
            var url = contextPath + "/system/dic/edit";
            $.common.ajaxAction({
                url: url,
                data: json,
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                success: function (data) {
                    $.common.hideModal();
                    $.Page.refreshCurrentPage();
                }
            });
        },

        toAddDicItem: function (id) {
            var url = contextPath + "/system/dic/" + id + "/item/edit";
            $.common.showModal({
                url: url,
                success: function () {
                    $.extend($.validationEngineLanguage.allRules, {
                        "ajaxDicItemCheck": {
                            "url": contextPath + "/system/dic/check?id=" + id,
                            "extraData": "dt=" + (new Date()).getTime(),
                            "alertText": "不能使用!",
                            "alertTextLoad": "验证中，请稍候...",
                        }
                    });

                    $("#dic_edit_form").validationEngine({
                        ajaxFormValidationMethod: 'post',
                    });
                }
            });
        },

        addDicItem: function (id) {
            if (!$("#dic_edit_form").validationEngine("validate")) {
                return false;
            }
            var code = $("#dic_item_code").val();
            var name = $("#dic_item_name").val();
            var value = $("#dic_item_value").val();
            var sort = $("#dic_item_sort").val();
            var json = {code: code, name: name, value: value, sort: sort};
            var url = contextPath + "/system/dic/" + id + "/item/edit";
            $.common.ajaxAction({
                url: url,
                data: json,
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                success: function (result) {
                    $("#dic_item_list .tr_dic_item_css").each(function () {
                        $(this).remove();
                    });
                    for (var i in result) {
                        var data = result[i];
                        var tr = '<tr id="tr_dic_item_' + data.id + '" class="tr_dic_item_css"><td>' + data.code + '</td><td>' + data.name + '</td><td>' + data.value + '</td><td>' + data.sort + '</td><td>';
                        var button = '<button type="button" class="btn btn-default btn-sm btn-outline green" onclick="$.DicController.toDeleteDicItem(' + data.id + ',\'' + data.name + '\');">' +
                            '<i class="fa fa-remove"></i>删除</button>';
                        var html = tr + button + '</td></tr>';
                        $("#dic_item_form_vs").before(html);
                    }
                    $("#dic_item_code").val("");
                    $("#dic_item_name").val("");
                    $("#dic_item_value").val("");
                    $("#dic_item_sort").val("999");
                }
            });
        },

        toDeleteDicItem: function (id, name) {
            var json = {
                title: "删除字典项",
                body: "您确定要删除[" + name + "]吗?",
                event: "$.DicController.deleteDicItem('" + id + "')",
            };
            $.common.showConfirmModal(json);
        },

        deleteDicItem: function (id) {
            var url = contextPath + "/system/dic/value/" + id + "/delete";
            $.common.ajaxActionText(url, function (data) {
                $("#tr_dic_item_" + id).remove();
                $.common.hideConfirmModal();
            });
        },

    });
});