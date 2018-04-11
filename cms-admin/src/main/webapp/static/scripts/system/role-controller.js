$(function () {
    $.RoleController = function (elm, config) {

    }

    $.extend($.RoleController, {

        toEdit: function (path, id) {
            $.common.showModal({
                url: path,
                success: function () {
                    $("#role_edit_form").validationEngine();
                    $.RoleController.showTree(id);
                },
            });
        },

        edit: function (path) {
            if (!$("#role_edit_form").validationEngine("validate")) return false;

            var role = $("#role_edit_form").serializeObject();

            var menuIds = $("#menu_tree_div").jstree("get_selected");

            var bean = {};
            bean.data = role;
            bean.ids = menuIds;

            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: bean,
                success: function (data) {
                    $.common.hideModal();
                    $.Page.refreshCurrentPage();
                }
            });
        },

        toDelete: function (path, name) {
            var json = {};
            json.title = "删除操作";
            json.body = "您确认要删除[" + name + "]吗?";
            json.event = "$.RoleController._delete('" + path + "')";
            $.common.showConfirmModal(json);
        },

        _delete: function (path) {
            $.common.ajaxActionText(path, function (data) {
                $.common.hideConfirmModal();
                $.Page.refreshCurrentPage();
            })
        },

        showTree: function (roleId) {
            $.ajax({
                url: contextPath + '/system/menu/getTree',
                type: 'GET',
                dataType: 'json',
                contextType: "application/json",
                data: {showPermission: true, roleId: roleId},
                success: function (data) {
                    $('#menu_tree_div').jstree({
                        'plugins': ["wholerow", "checkbox", "types"],
                        'core': {
                            "themes": {
                                "responsive": false
                            },
                            'data': data
                        },
                        "types": {
                            "default": {
                                "icon": "fa fa-folder icon-state-warning icon-lg"
                            },
                            "file": {
                                "icon": "fa fa-file icon-state-warning icon-lg"
                            }
                        }
                    });
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert('加载失败！');
                },
            });
        },

        tipInit: function () {
            $(".atip").tooltip({
                title: function () {
                    var menuId = $(this).attr("data-menuId");
                    var roleId = $(this).attr("data-roleId");
                    var url = contextPath + "/system/role/" + roleId + "/menu/" + menuId;
                    var names = "";
                    $.common.ajaxAction({
                        url: url,
                        type: 'GET',
                        dataType: 'json',
                        success: function (data) {
                            $(data).each(function (n, m) {
                                names += m.name + " ";
                            });
                        },
                    });
                    if ("" == names) {
                        names = "没有操作权限";
                    }
                    return names;
                }
            });
        },

    })

});

$(document).ready(function () {
    $.RoleController.tipInit();
});