package ttracker.dao;

public class SQLConsts {

    /* Connect propepties */
    public static final String DB_NAME = "multTTrTest1";
    public static final String JNDI_TASK = "TrackerTask";
    public static final String JNDI_EMP = "TrackerEmp";
    /* TASK SQL */
    public static final String EXISTS_TASK = "select ID_TASK from TASK where ID_TASK = ?";
    public static final String SELECT_TASK_BY_ID = "select * from TASK T, EMPLOYEE E, DEPARTMENT D where T.ID_TASK = ?"
            + " and T.EMPLOYEE = E.ID_EMP and E.DEPT = D.ID_DEPT";
    public static final String SELECT_TASK_BY_NAME = "select ID_TASK from TASK where TASK_NAME = ?";
    public static final String SELECT_TASK_BY_EMP = "select ID_TASK from TASK where EMPLOYEE = (select ID_EMP from EMPLOYEE where EMP_FIO = ?)";
    public static final String GET_TASK_KEYS = "select ID_TASK from TASK";
    public static final String GET_TASK_HIERARCHY = "SELECT id_task ID_TASK, task_name TASK_NAME, parent_task PARENT_TASK, NVL(descr,'&lt;empty&gt;') DESCR"
            + " FROM Task START WITH PARENT_TASK IS null CONNECT BY PRIOR ID_TASK = PARENT_TASK"
            + " ORDER SIBLINGS BY ID_TASK";
    public static final String DELETE_TASK = "delete from TASK where ID_TASK = ?";
    public static final String UPDATE_TASK = "UPDATE Task SET task_name = ?, parent_task = ?,"
            + " employee = (SELECT id_emp FROM Employee WHERE emp_fio = ?), date_begin = "
            + "TO_DATE(?, 'YYYY-MM-DD'), date_end = TO_DATE(?, 'YYYY-MM-DD'), status = ?, "
            + "descr = ? WHERE id_task = ?";
    public static final String INSERT_TASK = "INSERT INTO Task VALUES(task_seq.nextval, ?, ?, "
            + "(SELECT id_emp FROM Employee WHERE emp_fio = ?), TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'), ?, ?)";
    public static String GET_TASK_ID_BY_NAME = "select id_task from task where task_name = ?";
    /* EMPLOYEE SQL */
    public static final String EXISTS_EMP = "select ID_EMP from EMPLOYEE where ID_EMP = ?";
    public static String EMP_INFO_BY_ID = "select * from EMPLOYEE E, DEPARTMENT D where E.ID_EMP = ?"
            + " and E.DEPT = D.ID_DEPT";
    public static final String GET_EMP_KEYS = "select ID_EMP from EMPLOYEE";
}
