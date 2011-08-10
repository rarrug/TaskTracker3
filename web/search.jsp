<%-- search block --%>

<fieldset class="block block-hide" id="find-block" title="Search">
    <legend>Search</legend>
    <form action="index.perform" method="post">
        <input type="text" name="findName" style="width:195px;"/>
        <br/>
        <select name="findKind" style="width:200px;">
            <option value="1">by id</option>
            <option value="2">by name</option>
            <option value="3">by user</option>
        </select>
        <input type="submit" value="Find" /> 
    </form>
</fieldset>