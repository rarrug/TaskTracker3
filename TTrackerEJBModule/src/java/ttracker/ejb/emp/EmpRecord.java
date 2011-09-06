package ttracker.ejb.emp;

import java.io.Serializable;
import ttracker.ejb.dept.DeptRecord;


public class EmpRecord implements Serializable {
    
    public int empId;
    private String empName;
    private String job;
    private DeptRecord dept;
    
    public EmpRecord(int empId) {
        this.empId = empId;
    }
    
    public EmpRecord(int empId, String empName, String job, DeptRecord dept) {
        this.empId = empId;
        this.empName = empName;
        this.job = job;
        this.dept = dept;
    }

    public DeptRecord getDept() {
        return dept;
    }

    public void setDept(DeptRecord dept) {
        this.dept = dept;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
    
}
