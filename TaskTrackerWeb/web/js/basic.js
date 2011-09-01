
/*
 * Function for add modal window
 */
jQuery(function ($) {
    // Load dialog on page load
    //$('#basic-modal-content').modal();

    // Load dialog on click
    $('#basic-modal .basic-add').click(function (e) {
        $('#basic-modal-content-add').modal();
        return false;
    });
});

/*
 * Function for modify modal window
 */
//jQuery(function ($) {
//    $('#basic-modal .basic-modify').click(function (e) {
//        $('#basic-modal-content-modify').modal();
//        return false;
//    });
//});
