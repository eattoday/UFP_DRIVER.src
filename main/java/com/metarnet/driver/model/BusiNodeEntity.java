package com.metarnet.driver.model;

import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by Administrator on 2015/9/2.
 */
@Entity
@Table(name="busi_flow_node")
@Where(clause = "DELETED_FLAG=0")
public class BusiNodeEntity extends CommonEntity{

    private Long processId;

    private String proNodeName;
    private String proNodeEnName;
    private String proNodeCode;
    private String processName;
    private String processCode;
    private String processEnName;


    @Column(name = "PROCESS_ID")
    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    @Column(name = "PRO_NODE_NAME")
    public String getProNodeName() {
        return proNodeName;
    }

    public void setProNodeName(String proNodeName) {
        this.proNodeName = proNodeName;
    }

    @Column(name = "PRO_NODE_EN_NAME")
    public String getProNodeEnName() {
        return proNodeEnName;
    }

    public void setProNodeEnName(String proNodeEnName) {
        this.proNodeEnName = proNodeEnName;
    }

    @Column(name = "PRO_NODE_CODE")
    public String getProNodeCode() {
        return proNodeCode;
    }

    public void setProNodeCode(String proNodeCode) {
        this.proNodeCode = proNodeCode;
    }

    @Column(name = "PROCESS_NAME")
    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    @Column(name = "PROCESS_CODE")
    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    @Column(name = "PROCESS_EN_NAME")
    public String getProcessEnName() {
        return processEnName;
    }

    public void setProcessEnName(String processEnName) {
        this.processEnName = processEnName;
    }

}
