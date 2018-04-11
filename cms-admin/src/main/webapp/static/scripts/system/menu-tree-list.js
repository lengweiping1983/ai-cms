var TableDatatablesFixedHeader = function () {

    var initMenuTable = function () {

        var fixedHeaderOffset = 0;
        if (App.getViewPort().width < App.getResponsiveBreakpoint('md')) {
            if ($('.page-header').hasClass('page-header-fixed-mobile')) {
                fixedHeaderOffset = $('.page-header').outerHeight(true);
            }
        } else if ($('.page-header').hasClass('navbar-fixed-top')) {
            fixedHeaderOffset = $('.page-header').outerHeight(true);
        }

        var table = $('#menu_list_id');

        var oTable = table.dataTable({
            //fixedHeader: {
            //header: true,
            //footer: true,
            //headerOffset: fixedHeaderOffset
            //},
            "searching": false,
            "ordering": false,
            "bPaginate": false,
            "bInfo": false,
            "order": [],
        });

        table.treetable({expandable: true});
        //table.treetable("expandAll");
    }

    return {

        //main function to initiate the module
        init: function () {
            if (!jQuery().dataTable) {
                return;
            }

            initMenuTable();
        }

    };

}();

jQuery(document).ready(function () {
    TableDatatablesFixedHeader.init();
});
