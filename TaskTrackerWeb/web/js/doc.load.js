var simpleTreeCollection;

$(document).ready(function(){
            
    simpleTreeCollection = $('.simpleTree').simpleTree({
        autoclose: true,
        afterClick:function(node){
        //alert("text-"+$('span:first',node).text());
        },
        afterDblClick:function(node){
        //alert("text-"+$('span:first',node).text());
        },
        afterMove:function(destination, source, pos){
        //alert("destination-"+destination.attr('id')+" source-"+source.attr('id')+" pos-"+pos);
        },
        afterAjax:function()
        {
        //alert('Loaded');
        },
        animate:true
    //,docToFolderConvert:true
    });
            
    $(function() {		
        $("#tablesorter").tablesorter({
            sortList:[[0,0]], 
            widgets: ['zebra']
        });
    });
                
    /* search */
    $('#find-label').click(function() {
        $('#find-block').slideToggle('normal');
    });
                
    $('#saveArea, #hierar-block, #tablesorter').hide();
    $('#saveArea, #hierar-block, #tablesorter').fadeIn('normal');
                
    /* build calendar to blocks */
    openWindow('#cCallbackBeginA', '#calendarBeginA');
    openWindow('#cCallbackEndA', '#calendarEndA');
    openWindow('#cCallbackBeginM', '#calendarBeginM');
    openWindow('#cCallbackEndM', '#calendarEndM');
});
            
/* run calendar function */
function openWindow(block, inpt) {
    $(block).calendarLite({
        showYear: true,
        prevArrow: '<',
        nextArrow: '>',
        months: ['January', 'February', 'March', 'April', 'May', 'June',
        'July', 'August', 'September', 'October', 'November', 'December'],
        days: ['Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa', 'Su'],
        dateFormat: '{%yyyy}-{%m}-{%d}',
        onSelect: function(date) {
            $(inpt).attr('value', date);
            $(block).slideUp('normal');
            return false;
        }
    });
    $(block).hide();
    $(inpt).click(function() {
        if ($(block).is(':visible'))
            $(block).slideUp('normal')();
        else {
            $(block).slideDown('normal');
            $(block).css('margin-top', '-40px');
        }
    });
    
}

