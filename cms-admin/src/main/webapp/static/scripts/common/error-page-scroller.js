var ErrorListTableScroller = function () {

    var initErrorListTable = function () {
        var table = $('#error_list_table');

        var oTable = table.dataTable({

            // scroller extension: http://datatables.net/extensions/scroller/
            //scrollY:        300,
            deferRender:    true,
            //scroller:       true,

            "searching": false,
            "ordering": false,
            "bPaginate": false,
            "bInfo": false,
            "order": [
            ],
            
        });
    }

    return {

        //main function to initiate the module
        init: function () {

            if (!jQuery().dataTable) {
                return;
            }
            initErrorListTable();
        }

    };

}();

jQuery(document).ready(function() {
    ErrorListTableScroller.init();
});