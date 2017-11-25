package com.metarnet.driver.model;


import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2016/3/8.
 */
@Entity
    @Table(name = "flow_button")
@Where(clause = "DELETED_FLAG=0")
public class FlowButtonEntity extends CommonEntity{

    private String buttonName;      //按钮显示名称
    private Integer buttonType;     //按钮类型：1、后台按钮；2、弹出云表单；3、弹出自行开发表单

    private String triggerURL;      //按钮触发URL

    private String beforeSaveURL;       //保存表单前
    private String afSaveBefSubmitURL;  //保存表单后提交工作项前
    private String afterSubmitURL;      //提交工作项后

    private String cloudFormID;     //云表单ID
    private String cloudFormName;   //云表单名称

    private String cusDevFromID;    //自行开发表单ID
    private String cusDevFromName;  //自行开发表单名称

    @Column(name = "BUTTON_NAME")
    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    @Column(name = "BUTTON_TYPE")
    public Integer getButtonType() {
        return buttonType;
    }

    public void setButtonType(Integer buttonType) {
        this.buttonType = buttonType;
    }

    @Column(name = "TRIGGER_URL")
    public String getTriggerURL() {
        return triggerURL;
    }

    public void setTriggerURL(String triggerURL) {
        this.triggerURL = triggerURL;
    }

    @Column(name = "BEFORE_SAVE_URL")
    public String getBeforeSaveURL() {
        return beforeSaveURL;
    }

    public void setBeforeSaveURL(String beforeSaveURL) {
        this.beforeSaveURL = beforeSaveURL;
    }

    @Column(name = "AF_SAVE_BEF_SUBMIT_URL")
    public String getAfSaveBefSubmitURL() {
        return afSaveBefSubmitURL;
    }

    public void setAfSaveBefSubmitURL(String afSaveBefSubmitURL) {
        this.afSaveBefSubmitURL = afSaveBefSubmitURL;
    }

    @Column(name = "AFTER_SUBMIT_URL")
    public String getAfterSubmitURL() {
        return afterSubmitURL;
    }

    public void setAfterSubmitURL(String afterSubmitURL) {
        this.afterSubmitURL = afterSubmitURL;
    }

    @Column(name = "CLOUD_FORM_ID")
    public String getCloudFormID() {
        return cloudFormID;
    }

    public void setCloudFormID(String cloudFormID) {
        this.cloudFormID = cloudFormID;
    }

    @Column(name = "CLOUD_FORM_NAME")
    public String getCloudFormName() {
        return cloudFormName;
    }

    public void setCloudFormName(String cloudFormName) {
        this.cloudFormName = cloudFormName;
    }

    @Column(name = "CUS_DEV_FROM_ID")
    public String getCusDevFromID() {
        return cusDevFromID;
    }

    public void setCusDevFromID(String cusDevFromID) {
        this.cusDevFromID = cusDevFromID;
    }

    @Column(name = "CUS_DEV_FROM_NAME")
    public String getCusDevFromName() {
        return cusDevFromName;
    }

    public void setCusDevFromName(String cusDevFromName) {
        this.cusDevFromName = cusDevFromName;
    }
}
