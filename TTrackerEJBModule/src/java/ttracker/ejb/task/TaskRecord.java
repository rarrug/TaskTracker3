package ttracker.ejb.task;

import java.io.Serializable;
import java.util.Date;
import ttracker.ejb.emp.EmpRecord;

/**
 * Task information 
 */
public class TaskRecord implements Serializable {

    private Integer id;
    private String name;
    private Integer parentId;
    private Date begin;
    private Date end;
    private String status;
    private String description;
    private EmpRecord emp;

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EmpRecord getEmp() {
        return emp;
    }

    public void setEmp(EmpRecord emp) {
        this.emp = emp;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TaskRecord(Integer id) {
        this.id = id;
    }

    public TaskRecord(Integer id, String name, Integer parentId, Date begin, Date end, String status, EmpRecord emp, String descr) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.begin = begin;
        this.end = end;
        this.status = status;
        this.emp = emp;
        this.description = descr;
    }
}
