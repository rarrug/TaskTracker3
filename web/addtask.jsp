<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- add block -->

<div id="basic-modal-content-add">

    <h3>Add task</h3>

    <!-- build and fill form whith task parameters -->
    <div id="add-block" title="Add task">
        <form action="addtask.perform" method="post">
            <div class="add-block-label">Name:</div>
            <input type="text" name="taskName" value="default"/>

            <br style="float:none;"/>
            <div class="add-block-label">Parent:</div>
            <select name="taskParent">
                <option value="0">no</option>
                <c:forEach items="${taskList}" var="parentTask">
                    <option value="${parentTask.id}">${parentTask.name}</option>
                </c:forEach>
            </select>

            <br style="float:none;"/>
            <div class="add-block-label">User:</div>
            <select name="taskUser">
                <c:forEach items="${userList}" var="user">
                    <option value="${user}">${user}</option>
                </c:forEach>
            </select>

            <br style="float:none;"/>
            <div class="add-block-label">Begin:</div>
            <input type="text" name="taskBegin" value="${today}" id="calendarBeginA" readonly />

            <div id="cCallbackBeginA" class="select-free"></div>

            <br style="float:none;"/>
            <div class="add-block-label">End:</div>
            <input type="text" name="taskEnd" value="${today}" id="calendarEndA" readonly />

            <div id="cCallbackEndA" class="select-free"></div>

            <br style="float:none;"/>
            <div class="add-block-label">Status:</div>
            <select name="taskStatus">
                <option value="open" selected>open</option>
                <option value="process">process</option>
                <option value="close">close</option>
            </select>

            <br style="float:none;"/>
            <div class="add-block-label">Description:</div>
            <br style="float:none;"/>
            <textarea name="taskDescription">...</textarea>

            <br style="float:none;"/>
            <input type="submit" name="taskAdd" value="Add" id="addButton"/>

        </form>
    </div> 
</div>

<div style='display:none;'>
    <img src='im/x.png' alt='' style="border:1px solid white;"/>
</div>
