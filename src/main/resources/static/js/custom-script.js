/*================================================================================
	Item Name: Materialize - Material Design Admin Template
	Version: 4.0
	Author: PIXINVENT
	Author URL: https://themeforest.net/user/pixinvent/portfolio
================================================================================

NOTE:
------
PLACE HERE YOUR OWN JS CODES AND IF NEEDED.
WE WILL RELEASE FUTURE UPDATES SO IN ORDER TO NOT OVERWRITE YOUR CUSTOM SCRIPT IT'S BETTER LIKE THIS. */
$(document).ready(function(){
    $('.modal').modal();
    $('.timepicker').pickatime({
        default: 'now', // Set default time: 'now', '1:30AM', '16:30'
        fromnow: 0,       // set default time to * milliseconds from now (using with default = 'now')
        twelvehour: false, // Use AM/PM or 24-hour format
        donetext: okBtn, // text for done-button
        cleartext: clearBtn, // text for clear-button
        canceltext: cancelBtn, // Text for cancel-button,
        container: undefined, // ex. 'body' will append picker to body
        autoclose: false, // automatic close timepicker
        ampmclickable: true, // make AM PM clickable
        aftershow: function(){} //Function for after opening timepicker
    });
    $('.datepicker').pickadate({
        selectMonths: true, // Creates a dropdown to control month
        selectYears: 10, // Creates a dropdown of 5 years to control year
        format: 'yyyy-mm-dd',
        today: todayBtn,
        clear: clearBtn,
        close: okBtn,
        labelMonthPrev: labelMonthPrev,
        labelMonthNext: labelMonthNext,
        monthsFull: monthsFull.split(","),
        monthsShort: monthsShort.split(","),
        weekdaysFull: weekdaysFull.split(","),
        weekdaysShort: weekdaysShort.split(","),
        weekdaysLetter: weekdaysLetter.split(","),
        labelMonthSelect: labelMonthSelect,
        labelYearSelect: labelYearSelect
    });
});
