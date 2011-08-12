package model;

import java.util.Date;

/**
 * Save information about task
 */
public class Task {

    private int id;//number
    private String name;//name
    private int parentId;//parent task number
    private String emp;//user
    private Date begin;//begin date
    private Date end;//end date
    private String status;//status
    private String dept;//deparment
    private String description;

    public Task(int id, String name, int parentId, Date begin,
            Date end, String status, String emp, String dept, String description) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.begin = begin;
        this.end = end;
        this.status = status;
        this.emp = emp;
        this.dept = dept;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date dateBegin) {
        this.begin = dateBegin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date dateEnd) {
        this.end = dateEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmp() {
        return emp;
    }

    public void setEmp(String emp) {
        this.emp = emp;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String toString() {
        return "" + getId() + " - " + getName() + " - " + getParentId() + " - "
                + getBegin() + " - " + getEnd()
                + " - " + getStatus() + " - " + getEmp() + " - " + getDept() + " - " + getDescription();
    }

    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.id;
        hash = 17 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 17 * hash + this.parentId;
        hash = 17 * hash + (this.emp != null ? this.emp.hashCode() : 0);
        hash = 17 * hash + (this.begin != null ? this.begin.hashCode() : 0);
        hash = 17 * hash + (this.end != null ? this.end.hashCode() : 0);
        hash = 17 * hash + (this.status != null ? this.status.hashCode() : 0);
        hash = 17 * hash + (this.dept != null ? this.dept.hashCode() : 0);
        return hash;
    }
}
