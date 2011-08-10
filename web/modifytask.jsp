<!-- modify block -->

<div id="basic-modal-content-modify">

    <h3>Modify task</h3>

    <!-- get task info by request -->
    <%
        String id = request.getParameter("taskid");

        String name = "";
        String begin = "";
        String end = "";
        String status = "";
        String emp = "";
        int parentId = 0;
        String descr = "";

        for (Task task : taskList) {

            if ((task.getId() + "").equals(id)) {
                name = task.getName();
                begin = task.getDateBegin().toString();
                end = task.getDateEnd().toString();
                status = task.getStatus();
                emp = task.getEmp();
                parentId = task.getParentId();
                if (task.getDescription() != null) {
                    descr = task.getDescription();
                }

                break;
                //out.println(name + " - " + begin + " - " + end + " - " + status + " - " + emp + " - " + parentId);
            }
        }
    %>

    <!-- task modify form -->
    <div>
        <form action="modifytask.perform" method="post">

            <div class="modify-block-label">ID:</div>
            <input type="text" name="modifyId" value="<%=request.getParameter("taskid")%>" 
                   readonly="true"/>

            <br/>
            <div class="modify-block-label">Name:</div>
            <input type="text" name="modifyName" value="<%=name%>"/>

            <br/>
            <div class="modify-block-label">Parent:</div>
            <select name="modifyParent">
                <option value="null" <% if (parentId == 0) {
                        out.println("selected");
                    }%>>no
                </option>
                <%
                    for (Task parentTask : taskList) {
                        if (parentTask.getId() != Integer.parseInt(id)) {
                %>
                <option value="<%=parentTask.getName()%>" 
                        <% if (parentTask.getId() == parentId) {
                                out.println("selected");
                            }%>><%=(parentTask.getId() + " - " + parentTask.getName())%>
                </option>
                <%      }
                    }
                %>
            </select>

            <br/>
            <div class="modify-block-label">User:</div>
            <select name="modifyUser">
                <%
                    /* generate user list from task list */
                    for (String user : DAOFactory.getInstance().getUserList()) {
                %>
                <option value="<%=user%>" 
                        <% if (emp.equals(user)) {
                                out.println("selected");
                            }
                        %>><%=user%>
                </option>
                <%
                    }
                %>
            </select>

            <br/>
            <div class="modify-block-label">Begin:</div>
            <input type="text" name="modifyBegin" value="<%=begin%>" id="calendarBeginM" readonly="true"/>

            <div id="cCallbackBeginM" class="select-free"></div>

            <br/>
            <div class="modify-block-label">End:</div>
            <input type="text" name="modifyEnd" value="<%=end%>" id="calendarEndM" readonly="true"/>

            <div id="cCallbackEndM" class="select-free"></div>

            <br/>
            <div class="modify-block-label">Status:</div>
            <select name="modifyStatus">
                <option value="open" <% if ("open".equals(status)) {
                        out.println("selected");
                    }%>>open</option>
                <option value="process" <% if ("process".equals(status)) {
                        out.println("selected");
                    }%>>process</option>
                <option value="close" <% if ("close".equals(status)) {
                        out.println("selected");
                    }%>>close</option>
            </select>

            <br style="float:none;"/>
            <div class="modify-block-label">Description:</div>
            <br style="float:none;"/>
            <textarea name="modifyDescription"><%=descr%></textarea>

            <br/>
            <input type="submit" name="taskModify" value="Modify" id="modifyButton"/>

        </form>
    </div>

</div>

<div style='display:none;'>
    <img src='im/x.png' alt='' style="border:1px solid white;"/>
</div>


