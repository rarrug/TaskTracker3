<%@page import="controller.Operations"%>
<!-- add block -->

<div id="basic-modal-content-add">

    <h3>Add task</h3>

    <div class="block-show" id="add-block" title="Add task">
        <form action="addtask.perform" method="post">
            <div class="add-block-label">Name:</div>
            <input type="text" name="taskName" value="default"/>

            <br style="float:none;"/>
            <div class="add-block-label">Parent:</div>
            <select name="taskParent">
                <option value="null">no</option>
                <%
                    for (Task parentTask : taskList.getTaskList()) {
                %>
                <option value="<%=parentTask.getName()%>"><%=parentTask.getName()%></option>
                <%  }%>
            </select>

            <br style="float:none;"/>
            <div class="add-block-label">User:</div>
            <select name="taskUser">
                <%
                    /* generate user list from task list */
                    Operations.connect(Operations.SETTINGS_FILE);
                    for (String user : Operations.getAllUsers()) {
                %>
                <option value="<%=user%>"><%=user%></option>
                <%
                    }
                    Operations.disconnect();
                %>
            </select>

            <br style="float:none;"/>
            <div class="add-block-label">Begin:</div>
            <input type="text" name="taskBegin" value="2011-11-17" id="calendarBeginA" />

            <div id="cCallbackBeginA" class="select-free"></div>

            <br style="float:none;"/>
            <div class="add-block-label">End:</div>
            <input type="text" name="taskEnd" value="2011-11-17" id="calendarEndA"/>

            <div id="cCallbackEndA" class="select-free"></div>

            <br style="float:none;"/>
            <div class="add-block-label">Status:</div>
            <select name="taskStatus">
                <option value="open" selected>open</option>
                <option value="process">process</option>
                <option value="close">close</option>
            </select>

            <br style="float:none;"/>
            <input type="submit" name="taskAdd" value="Add" id="addButton"/>

        </form>
    </div> 
</div>

<div style='display:none;'>
    <img src='im/x.png' alt='' style="border:1px solid white;"/>
</div>
