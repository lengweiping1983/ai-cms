var MENU_TYPE = {menu: 1, permission: 2}

$(function () {
    $.MenuController = function (ele, config) {

    }

    $.extend($.MenuController, {

        toEdit: function (path) {
            $.common.showModal({
                url: path,
                success: function () {
                    $("#menu_edit_form").validationEngine();
                },
            });
        },

        edit: function (path) {
            if (!$("#menu_edit_form").validationEngine("validate")) return false;

            var json = $.MenuController.bornJson();
            
            $.common.ajaxAction({
                url: path,
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: json,
                success: function (data) {
                    $.common.hideModal();
                    $.MenuController.refreshPage();
                },
            });
        },

        toDelete: function (id, name, type) {
            var url = contextPath + "/system/menu/" + id + "/validate";

            $.common.ajaxAction({
                url: url,
                type: 'GET',
                dataType: 'json',
                success: function (data) {
                    if (data && data.result == 'false') {
                        var json = {title: "提示", body: data.description}
                        $.common.showAlertModal(json);
                    } else {
                        var title = "菜单";
                        if (type == MENU_TYPE.permission) {
                            title = "权限";
                        }
                        var json = {
                            title: "删除" + title,
                            body: "您确定要删除[" + name + "]吗?",
                            event: "$.MenuController._delete(" + id + ")"
                        }
                        $.common.showConfirmModal(json);
                    }
                },
            });
        },

        _delete: function (id) {
            var url = contextPath + "/system/menu/" + id + "/delete";
            $.common.ajaxActionText(url, function () {
                $.common.hideConfirmModal();
                $.MenuController.refreshPage();
            })
        },

        bornJson: function () {
            var isMenu = $("#menu_div").is(":visible");
            var json = {};

            var parentId = $("#parent_id").val();
            if (-1 == parentId) {
                json.parentId = null;
            } else {
                json.parentId = parentId;
            }

            if (isMenu) {
                var name = $("#menu_name").val();
                var href = $("#menu_href").val();
                var sort = $("#menu_sort").val();
                var isShow = $("#menu_isShow").val();
                var icon = $("#menu_icon").val();

                json.type = MENU_TYPE.menu;
                json.name = name;
                json.href = href;
                json.sort = sort;
                json.isShow = isShow;
                json.icon = icon;

            } else {
                var name = $("#permission_name").val();
                var permission = $("#permission_permission").val();
                var sort = $("#permission_sort").val();
                var isShow = $("#permission_isShow").val();

                json.type = MENU_TYPE.permission;
                json.name = name;
                json.permission = permission;
                json.sort = sort;
                json.isShow = isShow;
            }
            return json;
        },

        refreshPage: function () {
            $.common.ajaxLoadContent({
                url: contextPath + "/system/menu/",
                type: "GET",
                dataType: "html",
                contentType: "text/html",
            });
        },

        showIcon: function () {
            $("#menu_icon_modal_div").modal({});
        },

        showTree: function (filterMenuId) {
            $("#menu_tree_div").jstree("destroy");
            var json = {};
            if (filterMenuId && filterMenuId > 1) {
                json.filterMenuId = filterMenuId;
            }
            $.ajax({
                url: contextPath + '/system/menu/getTreeWithRoot',
                type: 'GET',
                dataType: 'json',
                contextType: "application/json",
                data: json,
                success: function (data) {
                    $("#menu_tree_modal_div").modal({});
                    $('#menu_tree_div').jstree({
                        'multiple': false,
                        'plugins': ["wholerow", "types"],
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
                    }).on("loaded.jstree", function (event, data) {
                        //data.instance.open_all();
                        var selectMenuId = $("#parent_id").val();
                        $('#menu_tree_div').jstree(false).select_node('#' + selectMenuId);
                    }).on("click.jstree", function (e, data) {
                        var node = e.target;
                        if (!node.id) {
                            return;
                        }
                        var id = node.id.replace("_anchor", "");

                        $("#parent_id").val(id);

                        if (id != -1) {
                            $("#menu_parent_name").val(node.text);
                            $("#permission_parent_name").val(node.text);
                        } else {
                            $("#menu_parent_name").val('');
                            $("#permission_parent_name").val('');
                        }

                        $("#menu_tree_modal_div").modal('hide');
                        $("#menu_tree_div").jstree("destroy");
                    });
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert('加载失败！');
                },
            });
        },

    });

});