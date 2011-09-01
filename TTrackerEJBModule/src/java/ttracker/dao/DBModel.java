package ttracker.dao;

import ttracker.ejb.dept.DeptRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.NoSuchEntityException;
import ttracker.ejb.emp.EmpRecord;
import ttracker.ejb.task.TaskRecord;

/**
 * Make operation with database
 */
public class DBModel implements IModel {

    /**
     * Check by id task exists
     * @param taskId Task number Task number
     * @param con Connection to database
     * @return true if exists and false another
     * @throws SQLException Error in work with database
     */
    public boolean isTaskExists(Integer taskId, Connection con) throws SQLException {
        boolean rowExists = false;
        PreparedStatement st = con.prepareStatement(SQLConsts.EXISTS_TASK);
        st.setInt(1, taskId.intValue());
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            rowExists = true;
        }
        st.close();
        st = null;
        return rowExists;
    }

    /**
     * Select task by id
     * @param taskId Task number
     * @param con Connection to database
     * @return
     * @throws SQLException Error in work with database
     */
    public TaskRecord selectTaskById(Integer taskId, Connection con) throws SQLException {
        PreparedStatement st = con.prepareStatement(SQLConsts.SELECT_TASK_BY_ID);
        st.setInt(1, taskId.intValue());
        ResultSet rs = st.executeQuery();
        if (!rs.next()) {
            throw new NoSuchEntityException("In selectRow: Row does not exist");
        }

        TaskRecord record = new TaskRecord(rs.getInt("id_task"));
        record.setName(rs.getString("task_name"));
        record.setParentId(new Integer(rs.getInt("parent_task")));
        record.setBegin(rs.getDate("date_begin"));
        record.setEnd(rs.getDate("date_end"));
        record.setStatus(rs.getString("status"));
        record.setDescription(rs.getString("descr"));
        record.setEmp(new EmpRecord(rs.getInt("id_emp"), rs.getString("emp_fio"), rs.getString("job"),
                new DeptRecord(rs.getInt("id_dept"), rs.getString("dept_name"))));

        st.close();
        st = null;
        return record;
    }

    /**
     * Get list of task keys by some string parameter
     * @param param Some string parameter (name, user name, etc.)
     * @param con Connection to database
     * @param sql
     * @return
     * @throws SQLException Error in work with database
     */
    public Collection selectTaskByParam(String param, Connection con, String sql) throws SQLException {
        PreparedStatement st = con.prepareStatement(sql);
        st.setString(1, param);
        ResultSet rs = st.executeQuery();
        Collection list = new ArrayList();
        while (rs.next()) {
            list.add(new Integer(rs.getInt("id_task")));
        }
        st.close();
        st = null;
        return list;
    }

    /**
     * Get list of all task keys
     * @param con Connection to database
     * @param hier true if need hierarchical list or false another
     * @return List of task keys
     * @throws SQLException Error in work with database
     */
    public Collection getAllTaskKeys(Connection con, boolean hier) throws SQLException {
        Collection list = new ArrayList();
        PreparedStatement st = null;
        if (hier) {
            st = con.prepareStatement(SQLConsts.GET_TASK_HIERARCHY);
        } else {
            st = con.prepareStatement(SQLConsts.GET_TASK_KEYS);
        }
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            list.add(new Integer(rs.getInt("id_task")));
        }
        st.close();
        st = null;
        return list;
    }

    /**
     * Insert task record to database
     * @param newTask New task object
     * @param con Connection to database
     * @return
     * @throws SQLException Error in work with database
     */
    public Integer insertTask(TaskRecord newTask, Connection con) throws SQLException {
        PreparedStatement statement = con.prepareStatement(SQLConsts.INSERT_TASK);

        statement.setString(1, newTask.getName());
        if (newTask.getParentId().compareTo(Integer.valueOf(0)) == 0) {
            statement.setNull(2, java.sql.Types.INTEGER);
        } else {
            statement.setInt(2, newTask.getParentId());
        }
        statement.setString(3, newTask.getEmp().getEmpName());
        statement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(newTask.getBegin()));
        statement.setString(5, new SimpleDateFormat("yyyy-MM-dd").format(newTask.getEnd()));
        statement.setString(6, newTask.getStatus());
        statement.setString(7, newTask.getDescription());

        statement.executeQuery();

        statement = con.prepareStatement(SQLConsts.GET_TASK_ID_BY_NAME);
        statement.setString(1, newTask.getName());

        ResultSet rs = statement.executeQuery();

        Integer newTaskId = null;
        if (rs.next()) {
            newTaskId = Integer.valueOf(rs.getInt("id_task"));
        }

        statement.close();
        statement = null;

        return newTaskId;
    }

    /**
     * Delete task from database by id
     * @param taskId Task number
     * @param con Connection to database
     * @throws SQLException Error in work with database
     */
    public void deleteTask(Integer taskId, Connection con) throws SQLException {
        PreparedStatement st = con.prepareStatement(SQLConsts.DELETE_TASK);
        st.setInt(1, taskId.intValue());
        st.executeQuery();
        st.close();
        st = null;
    }

    /**
     * Update task in database
     * @param task Modify task object
     * @param con Connection to database
     * @throws SQLException Error in work with database
     */
    public void updateTask(TaskRecord task, Connection con) throws SQLException {
        System.out.println("TaskDAO.updateRow()");
        PreparedStatement statement = con.prepareStatement(SQLConsts.UPDATE_TASK);

        statement.setString(1, task.getName());
        if (task.getParentId() == 0) {
            statement.setNull(2, java.sql.Types.INTEGER);
        } else {
            statement.setInt(2, task.getParentId());
        }
        statement.setString(3, task.getEmp().getEmpName());
        statement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(task.getBegin()));
        statement.setString(5, new SimpleDateFormat("yyyy-MM-dd").format(task.getEnd()));
        statement.setString(6, task.getStatus());
        statement.setString(7, task.getDescription());
        statement.setInt(8, task.getId());

        statement.executeQuery();
        statement.close();
        statement = null;
    }

    /**
     * Check exists employee in database by id
     * @param empId Employee number
     * @param con Connection to database
     * @return true if exists and false another
     * @throws SQLException Error in work with database
     */
    public boolean isEmpExists(Integer empId, Connection con) throws SQLException {
        boolean rowExists = false;
        PreparedStatement st = con.prepareStatement(SQLConsts.EXISTS_EMP);
        st.setInt(1, empId.intValue());
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            rowExists = true;
        }
        st.close();
        st = null;
        return rowExists;
    }

    /**
     * Get employee from database by id
     * @param empId Employee number
     * @param con Connection to database
     * @return Employee object
     * @throws SQLException Error in work with database
     */
    public EmpRecord selectEmp(Integer empId, Connection con) throws SQLException {
        PreparedStatement st = con.prepareStatement(SQLConsts.EMP_INFO_BY_ID);
        st.setInt(1, empId.intValue());
        ResultSet rs = st.executeQuery();
        if (!rs.next()) {
            throw new NoSuchEntityException("In selectRow: Row does not exist");
        }

        EmpRecord record = new EmpRecord(rs.getInt("id_emp"));
        record.setEmpName(rs.getString("emp_fio"));
        record.setJob(rs.getString("job"));
        record.setDept(new DeptRecord(rs.getInt("id_dept"), rs.getString("dept_name")));

        st.close();
        st = null;
        return record;
    }

    /**
     * Get list of employee keys
     * @param con Connection to database
     * @return List of employee keys
     * @throws SQLException Error in work with database
     */
    public Collection getAllEmpKeys(Connection con) throws SQLException {
        Collection list = new ArrayList();
        PreparedStatement st = null;
        st = con.prepareStatement(SQLConsts.GET_EMP_KEYS);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            list.add(new Integer(rs.getInt("id_emp")));
        }
        st.close();
        st = null;
        return list;
    }
}
