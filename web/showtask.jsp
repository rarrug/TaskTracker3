<%@page import="java.util.Collection"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="controller.ShowTask"%>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.Set"%>
<%@page import="model.Task"%>
<%@page import="model.DAOFactory"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@ taglib uri="/WEB-INF/lib/mylib.tld" prefix="myTag" --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>Task Tracker</title>

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

        <script type='text/javascript' src='js/doc.load.js'></script>

    </head>
    <body>

        <!-- get parameters from request and session -->
        <c:set var="taskid" scope="page" value="${param.taskid}" />
        <c:set var="hierarchy" scope="page" value="${sessionScope.hierarchy}" />
        <c:set var="message" scope="page" value="${sessionScope.message}" />
        <c:set var="taskList" scope="session" value="${sessionScope.taskList}" />
        <c:set var="userList" scope="session" value="${sessionScope.userList}" />
        <c:set var="today" scope="session" value="${sessionScope.today}" />

        <%
            /* Logger */
            final Logger logger = Logger.getLogger(this.getClass());
            try {
        %>

        <!-- left menu -->
        <div class='box'>
            <div class='boxtop'><div></div></div>
            <div class='boxcontent'>
                <p><a href="index.jsp" class="menu-link">Show all</a></p>
                <p><a href="index.jsp?findKind=hier" class="menu-link">Show hierarchy</a></p>
                <p id="find-label">Search</p>
                <p id='basic-modal'><a href="#" class='basic-add menu-link'>Add task</a></p>
                <p><a href="savetask.perform" class="menu-link">Export to Outlook</a></p>
            </div>
            <div class='boxbottom'><div></div></div>
        </div>

        <!-- main content -->

        <div id="content">
            
            <!-- message block. show when some action happened -->
            <c:if test="${message ne null}" >
                <script type="text/javascript">
                    $(document).ready(function() {
                        $('#content #message').hide().fadeIn(1000).delay(3000).fadeOut(1000);
                    });
                </script>
                <div id="message" class="${sessionScope.messageType}">${message}</div>
                <%
                    session.setAttribute("message", null);
                    session.setAttribute("messageType", null);
                %>
            </c:if>

            <!-- include pages (search, add task, modify task, confirm dialog)-->
            <c:import url="search.jsp" />
            <c:import url="addtask.jsp" />
            <c:import url="confirm.jsp" />

            <!-- show window if task must be modify (taskId exists in url) -->
            <c:if test="${taskid ne null}">
                <c:import url="modifytask.jsp" />
                <script type="text/javascript">
                    $(document).ready(function() {
                        $('#basic-modal-content-modify').modal()  
                    });
                </script>
                <%logger.info("Show modal modify window");%>
            </c:if>

            <!-- hierarchical block. activate when press hirerachical link -->
            <c:choose>
                <c:when test="${hierarchy ne null}">
                    <fieldset id="hierar-block" class="block" title="Hierarchical structure">
                        <legend>Hierarchical structure</legend>
                        <%
                            logger.info("Show hierarchical structure");
                            out.println(session.getAttribute("hierarchyList"));
                            session.setAttribute("hierarchy", null);
                        %>
                    </fieldset>
                </c:when>
                <c:otherwise>
                    <!-- if not activate hierarchical output, show all tasks in table -->
                    <table id="tablesorter" class="tablesorter" border="0" cellpadding="0" cellspacing="1">
                        <caption><b>Task table</b></caption>
                        <thead>
                            <tr>
                                <th>&nbsp;</th>
                                <th>ID</th>
                                <th>NAME</th>
                                <th>PARENT</th>
                                <th>BEGIN</th>
                                <th>END</th>
                                <th>STATUS</th>
                                <th>USER</th>
                                <th>DEPT</th>
                                <th>DESCRIPTION</th>
                                <th>&nbsp;</th>
                            </tr>
                        </thead>
                        <tbody>
                            
                            <!-- output row table with parameters of each task -->
                            <c:forEach items="${taskList}" var="currTask">
                                <tr>
                                    <td width="100px">
                                        <div id='basic-modal'>
                                            <a href="index.jsp?taskid=${currTask.id}" class="basic-modify" title="Modify">
                                                <img src="im/modify.png" alt="Modify"/>
                                            </a>
                                        </div>
                                    </td>
                                    <td width="100px">${currTask.id}</td>
                                    <td width="100px">${currTask.name}</td>

                                    <c:choose>
                                        <c:when test="${currTask.parentId eq 0}">
                                            <td width="100px">no</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td width="100px">${currTask.parentId}</td>
                                        </c:otherwise>
                                    </c:choose>

                                    <td width="110px">${currTask.begin}</td>
                                    <td width="110px">${currTask.end}</td>
                                    <td width="110px">${currTask.status}</td>
                                    <td width="100px">${currTask.emp}</td>
                                    <td width="100px">${currTask.dept}</td>

                                    <c:choose>
                                        <c:when test="${currTask.description eq null}">
                                            <td width="120px"> - </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td width="120px">${currTask.description}</td>
                                        </c:otherwise>
                                    </c:choose>

                                    <td width="50px" align="center" id='confirm-dialog'>
                                        <a href="${currTask.id}" class='confirm' title="Delete">
                                            <img src="im/delete.png" alt="Delete"/>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>

            <%-- catch exception and write to log --%>
            <%
                } catch (Exception e) {
                    logger.error("JSP page exception", e);
                }
            %>
        </div>

    </body>
</html>
