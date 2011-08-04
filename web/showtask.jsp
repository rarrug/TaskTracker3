
<%@page import="org.apache.log4j.Logger"%>
<%@page import="javax.swing.JOptionPane"%>
<%@page import="actions.ShowTask"%>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.Set"%>
<%@page import="model.Task"%>
<%@page import="model.TaskList" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

        <!-- Main page CSS file -->
        <link rel="stylesheet" type="text/css" href="css/index.css" />

        <!-- Hierarchical tree CSS file -->
        <link rel="stylesheet" type="text/css" href="css/tree.css" />

        <!-- Sort table CSS file -->
        <link rel="stylesheet" type="text/css" href="css/table.css" />

        <!-- Calendar CSS file -->
        <link rel="stylesheet" href="css/calendarlite.css" />

        <!-- Window CSS files -->
        <link type='text/css' href='css/basic.css' rel='stylesheet' media='screen' />
        <link type='text/css' href='css/confirm.css' rel='stylesheet' media='screen' />

        <!-- IE6 "fix" for the close png image -->
        <!--[if lt IE 7]>
        <link type='text/css' href='css/basic_ie.css' rel='stylesheet' media='screen' />
        <![endif]-->

        <!-- Load jQuery -->
        <script type="text/javascript" src="js/jquery-1.6.2.js"></script>

        <!-- Load Simple Tree JS files -->
        <script type="text/javascript" src="js/jquery.simple.tree.js"></script>

        <!-- Load Table Sorter JS files -->
        <script type="text/javascript" src="js/jquery.tablesorter.js"></script>

        <!-- Load Basic and Confirm window JS files -->
        <script type='text/javascript' src='js/jquery.simplemodal.js'></script>
        <script type='text/javascript' src='js/basic.js'></script>
        <script type='text/javascript' src='js/confirm.js'></script>

        <!-- Calendar JS file -->
        <script type="text/javascript" src="js/jquery.calendarlite.js"></script>

        <title>Task Tracker</title>
        <script type="text/javascript">
            
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
                    $("#tablesorter").tablesorter({sortList:[[0,0]], widgets: ['zebra']});
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
            
        </script>

    </head>
    <body>

        <%-- get parameters from request and session--%>

        <jsp:useBean id="taskList" scope="session" class="model.TaskList" />

        <%
            /* Logger */
            final Logger logger = Logger.getLogger(this.getClass());

            try {
                String userId = request.getParameter("taskid");
                String findKind = request.getParameter("findKind");
                String saveToFile = (String) session.getAttribute("saveToFile");
                String hierarchy = (String) session.getAttribute("hierarchy");

        %>

        <!-- left menu -->

        <div class='box'>
            <div class='boxtop'><div></div></div>
            <div class='boxcontent'>
                <p><a href="index.jsp" class="menu-link">Show all</a></p>
                <p><a href="index.jsp?findKind=hier" class="menu-link">Show hierarchy</a></p>
                <p id="find-label">Search</p>
                <p id='basic-modal'><a href="#" class='basic-add menu-link'>Add task</a></p>
                <p><a href="index.jsp?save=1" class="menu-link">Import</a></p>
            </div>
            <div class='boxbottom'><div></div></div>
        </div>

        <!-- main content -->

        <div id="content">

            <%-- show window if task must be modify (userId exists in url) --%>

            <%
                if (userId != null) {
                    logger.info("Show modal modify window");
            %>
            <%@include file="modifytask.jsp" %>
            <script type="text/javascript">
                $(document).ready(function() {
                    $('#basic-modal-content-modify').modal()  
                });
            </script>
            <%}%>

            <%-- include pages (search, add task, modify task, confirm dialog)--%>

            <%@include file="search.jsp" %>
            <%@include file="addtask.jsp" %>
            <%@include file="confirm.jsp" %>

            <%-- output to main content table or hierarchical list --%>

            <%
                if (hierarchy != null) {
                    logger.info("Show hierarchical structure");
            %>

            <!-- hierarchical block -->

            <%
                /* output hierarchical structure of tasks */
                out.println("<fieldset id=\"hierar-block\" title=\"Hierarchical structure\">");
                out.println("<legend>Hierarchical structure</legend>");
                ShowTask.printHirerachicalStructure(out,
                        (TaskList) session.getAttribute("hierarchyTaskList"));
                out.println("</fieldset>");
                session.setAttribute("hierarchy", null);
            } else if (saveToFile != null) {
                ShowTask.generateImportFile(out, ((TaskList) session.getAttribute("taskList")).getTaskList());
                session.setAttribute("saveToFile", null);
            } else {
                logger.info("Show tasks");
            %>

            <%-- task table header --%>

            <table id="tablesorter" class="tablesorter" border="0" cellpadding="0" cellspacing="1">
                <caption><b>Task table</b></caption>
                <thead>
                    <tr>
                        <th>
                            ID 
                        </th>
                        <th>
                            NAME 
                        </th>
                        <th>
                            PARENT
                        </th>
                        <th>
                            BEGIN
                        </th>
                        <th>
                            END
                        </th>
                        <th>
                            STATUS
                        </th>
                        <th>
                            USER
                        </th>
                        <th>
                            DEPT
                        </th>
                        <th>&nbsp;</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        /* output task list */

                        taskList = (TaskList) session.getAttribute("taskList");
                        for (Task task : taskList.getTaskList()) {
                            session.setAttribute("task", task);

                            /* output all tasks */
                            if (findKind == null) {

                    %>

                    <jsp:include page="tasktable.jsp" />

                    <%} else if (findKind != null) {
                        /* output by find request (id, name, user)*/
                        if ((findKind.equals("1") && (task.getId() + "").equals(session.getAttribute("findName")))
                                || (findKind.equals("2") && task.getName().equals(session.getAttribute("findName")))
                                || (findKind.equals("3") && task.getEmp().equals(session.getAttribute("findName")))) {
                    %>

                    <jsp:include page="tasktable.jsp" />

                    <%}
                            }
                        }
                        if (findKind != null) {
                            session.setAttribute("findKind", null);
                        }
                    %>
                </tbody></table>
                <%
                        }
                    } catch (Exception e) {
                        logger.error("JSP page exception", e);
                    }
                %>
        </div>

    </body>
</html>
