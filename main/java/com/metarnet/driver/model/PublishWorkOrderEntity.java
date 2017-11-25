package com.metarnet.driver.model;

import com.metarnet.core.common.model.BaseForm;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/11/15/0015.
 */
@Entity
@Table(name = "publish_launch_work_order")
@Where(clause = "DELETED_FLAG=0")
public class PublishWorkOrderEntity extends CommonEntity {

    private String name;    //发起工单名称

    private String customFormID;    //自定义表单ID

    private String customFormName;    //自定义表单名称

    private String processChName;    //流程显示名称

    private String processModelName;    //流程定义名称

    private Integer formType;   //配置的表单类型;1：云表单；2：自定义url；3、技术手段；4、自行开发表单

    /**
     * 按钮设置
     */
    private String btnIDs;
    private String btnNames;


    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "CUSTOM_FORM_ID")
    public String getCustomFormID() {
        return customFormID;
    }

    public void setCustomFormID(String customFormID) {
        this.customFormID = customFormID;
    }

    @Column(name = "CUSTOM_FORM_Name")
    public String getCustomFormName() {
        return customFormName;
    }

    public void setCustomFormName(String customFormName) {
        this.customFormName = customFormName;
    }

    @Column(name = "PROCESS_CH_NAME")
    public String getProcessChName() {
        return processChName;
    }

    public void setProcessChName(String processChName) {
        this.processChName = processChName;
    }

    @Column(name = "PROCESS_MODEL_NAME")
    public String getProcessModelName() {
        return processModelName;
    }

    public void setProcessModelName(String processModelName) {
        this.processModelName = processModelName;
    }

    @Column(name = "BTN_IDS")
    public String getBtnIDs() {
        return btnIDs;
    }

    public void setBtnIDs(String btnIDs) {
        this.btnIDs = btnIDs;
    }

    @Column(name = "BTN_NAMES")
    public String getBtnNames() {
        return btnNames;
    }

    public void setBtnNames(String btnNames) {
        this.btnNames = btnNames;
    }

    @Column(name = "FORM_TYPE")
    public Integer getFormType() {
        return formType;
    }

    public void setFormType(Integer formType) {
        this.formType = formType;
    }
}
