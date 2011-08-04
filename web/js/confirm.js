
jQuery(function ($) {
    $('#confirm-dialog input.confirm, #confirm-dialog a.confirm').click(function (e) {
        e.preventDefault();
        var user = this;
        var id = user.href.substr(user.href.lastIndexOf("/")+1, user.href.length - user.href.lastIndexOf("/"));
        confirm("You really want to delete task number " + id
            + " ?", function () {
                //alert('user:' + user.val);
                window.location.href = 'removetask.perform?rem=' + id;
            });
    });
});

function confirm(message, callback) {
    $('#confirm').modal({
        closeHTML: "<a href='#' title='Close' class='modal-close'>x</a>",
        position: ["20%",],
        overlayId: 'confirm-overlay',
        containerId: 'confirm-container', 
        onShow: function (dialog) {
            var modal = this;

            $('.message', dialog.data[0]).append(message);

            // if the user clicks "yes"
            $('.yes', dialog.data[0]).click(function () {
                // call the callback
                if ($.isFunction(callback)) {
                    callback.apply();
                }
                // close the dialog
                modal.close(); // or $.modal.close();
            });
        }
    });
}