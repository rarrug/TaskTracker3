<!-- search block -->

<fieldset class="block block-hide" id="find-block" title="Search">
    <legend>Search</legend>
    <form action="index.perform" method="post">
        <input type="text" name="findName" style="width:195px;"/>
        <br/>
        <select name="findKind" style="width:200px;">
            <option value="by_id">by id</option>
            <option value="by_name">by name</option>
            <option value="by_user">by user</option>
        </select>
        <input type="submit" value="Find" /> 
    </form>
</fieldset>