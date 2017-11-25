package com.metarnet.driver.model;

import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2016/11/15/0015.
 */
@Entity
@Table(name = "busi_flow")
@Where(clause = "DELETED_FLAG=0")
public class BusiFlowEntity extends CommonEntity {

    private String name;    //业务流程名字

    private String processDefIDs;    //关联开发流程ID

    private String processDefNames;    //关联开发流程名称

    private String processDefCodes;    //关联开发流程code

    private String specialtyIDs;    //关联专业ID

    private String specialtyNames;    //关联专业名称


    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "PROCESS_DEF_IDS")
    public String getProcessDefIDs() {
        return processDefIDs;
    }

    public void setProcessDefIDs(String processDefIDs) {
        this.processDefIDs = processDefIDs;
    }

    @Column(name = "PROCESS_DEF_NAMES")
    public String getProcessDefNames() {
        return processDefNames;
    }

    public void setProcessDefNames(String processDefNames) {
        this.processDefNames = processDefNames;
    }

    @Column(name = "PROCESS_DEF_CODES")
    public String getProcessDefCodes() {
        return processDefCodes;
    }

    public void setProcessDefCodes(String processDefCodes) {
        this.processDefCodes = processDefCodes;
    }

    @Column(name = "SPECIALTY_IDS")
    public String getSpecialtyIDs() {
        return specialtyIDs;
    }

    public void setSpecialtyIDs(String specialtyIDs) {
        this.specialtyIDs = specialtyIDs;
    }

    @Column(name = "SPECIALTY_NAMES")
    public String getSpecialtyNames() {
        return specialtyNames;
    }

    public void setSpecialtyNames(String specialtyNames) {
        this.specialtyNames = specialtyNames;
    }
}
