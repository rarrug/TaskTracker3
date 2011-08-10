
<%@page import="model.Task"%>

<% Task task = (Task) session.getAttribute("task");%>

<tr>
    <td width="100px">
        <div id='basic-modal'>
            <a href="index.jsp?taskid=<%=task.getId()%>" class="basic-modify" title="Modify">
                <img src="im/modify.png" alt="Modify"/>
            </a>
        </div>
    </td>
    <td width="100px"><%=task.getId()%></td>
    <td width="100px"><%=task.getName()%></td>
    <% if (task.getParentId() == 0) {
            out.println("<td width=\"120px\">no</td>");
        } else {
            out.println("<td width=\"120px\">" + task.getParentId() + "</td>");
        }
    %>
    <td width="110px"><%=task.getDateBegin()%></td>
    <td width="110px"><%=task.getDateEnd()%></td>
    <td width="110px"><%=task.getStatus()%></td>
    <td width="100px"><%=task.getEmp()%></td>
    <td width="100px"><%=task.getDept()%></td>
    <%
        String descr = task.getDescription();
        if (descr == null) {
            descr = " - ";
        } else if (descr.length() > 17) {
            descr = descr.substring(0, 17) + "...";
        }
    %>
    <td width="120px"><%=descr%></td>
    <td width="50px" align="center" id='confirm-dialog'>
        <a href="<%=task.getId()%>" class='confirm' title="Delete">
            <img src="im/delete.png" alt="Delete"/>
        </a>
    </td>
</tr>

