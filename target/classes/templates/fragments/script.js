$(document).ready(function () {

    var fullHeight = function () {

        $('#sidebar').css('height', $(window).height());
        $(window).resize(function () {
            $('#sidebar').css('height', $(window).height());
        });
        

    };
    fullHeight();

    $("#sidebarCollapse").on("click", function () {
        $("#sidebar").fadeToggle();
    });

});
