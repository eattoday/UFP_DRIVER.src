package com.metarnet.driver.model;


import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2016/3/8.
 */
@MappedSuperclass
public class FlowNodeSettingBase {


    private Long id;
    private String settingID;/*业务主键*/
    private Long createdBy;/*创建人*/
    private Timestamp creationTime;/*创建时间*/
    private Long lastUpdatedBy;/*最后修改人*/
    private Timestamp lastUpdateTime;/*最后修改时间*/
    private Boolean deletedFlag;/*删除标记*/
    private Long deletedBy;/*删除人*/
    private Timestamp deletionTime;/*删除时间*/

    private String processModelId;
    private String processModelName;
    private String activityDefID;

    /**
     * 表单设置
     */
    private Integer formType;   //配置的表单类型;1：表单；2：自定义url；3、技术手段；4、自行开发表单

    private String formID;
    private String formName;

    private String customURL;
    private String tenantId;    /*租户ID*/
    private Boolean extand;/*是否支持扩展*/

    private String componentID;
    private String componentName;

    private String settingStatus;
    private String bpsVersion;

    /**
     * 技术手段
     */
    private String areaName;
    private String component;
    private String editLinks;
    private String showLinks;

    /**
     * 按钮设置
     */
    private String btnIDs;
    private String btnNames;

    /**
     * 前处理、后处理
     */
    private String preProcessor;
    private String postProcessor;

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "SETTING_ID", nullable = false, updatable = false)
    public String getSettingID() {
        return settingID;
    }

    public void setSettingID(String settingID) {
        this.settingID = settingID;
    }

    @Column(name = "CREATED_BY")
    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "CREATION_TIME")
    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    @Column(name = "LAST_UPDATED_BY")
    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Column(name = "LAST_UPDATE_TIME")
    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Column(name = "DELETED_FLAG")
    public Boolean getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(Boolean deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    @Column(name = "DELETED_BY")
    public Long getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
    }

    @Column(name = "DELETION_TIME")
    public Timestamp getDeletionTime() {
        return deletionTime;
    }

    public void setDeletionTime(Timestamp deletionTime) {
        this.deletionTime = deletionTime;
    }

    @Column(name = "PROCESS_MODEL_ID")
    public String getProcessModelId() {
        return processModelId;
    }

    public void setProcessModelId(String processModelId) {
        this.processModelId = processModelId;
    }

    @Column(name = "PROCESS_MODEL_NAME")
    public String getProcessModelName() {
        return processModelName;
    }

    public void setProcessModelName(String processModelName) {
        this.processModelName = processModelName;
    }

    @Column(name = "ACTIVITY_DEF_ID")
    public String getActivityDefID() {
        return activityDefID;
    }

    public void setActivityDefID(String activityDefID) {
        this.activityDefID = activityDefID;
    }

    @Column(name = "FORM_ID")
    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    @Column(name = "FORM_NAME")
    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    @Column(name = "CUSTOM_URL")
    public String getCustomURL() {
        return customURL;
    }

    public void setCustomURL(String customURL) {
        this.customURL = customURL;
    }

    @Column(name = "COMPONENT_ID")
    public String getComponentID() {
        return componentID;
    }

    public void setComponentID(String componentID) {
        this.componentID = componentID;
    }

    @Column(name = "COMPONENT_NAME")
    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    @Column(name = "SETTING_STATUS")
    public String getSettingStatus() {
        return settingStatus;
    }

    public void setSettingStatus(String settingVersion) {
        this.settingStatus = settingVersion;
    }

    @Column(name = "BPS_VERSION")
    public String getBpsVersion() {
        return bpsVersion;
    }

    public void setBpsVersion(String bpsVersion) {
        this.bpsVersion = bpsVersion;
    }

    @Column(name = "FORM_TYPE")
    public Integer getFormType() {
        return formType;
    }

    public void setFormType(Integer formType) {
        this.formType = formType;
    }

    @Column(name = "EXTAND")
    public Boolean getExtand() {
        return extand;
    }

    public void setExtand(Boolean extand) {
        this.extand = extand;
    }

    @Column(name = "TENANT_ID")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "AREA_NAME")
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Column(name = "COMPONENT")
    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    @Column(name = "EDIT_LINKS")
    public String getEditLinks() {
        return editLinks;
    }

    public void setEditLinks(String editLinks) {
        this.editLinks = editLinks;
    }

    @Column(name = "SHOW_LINKS")
    public String getShowLinks() {
        return showLinks;
    }

    public void setShowLinks(String showLinks) {
        this.showLinks = showLinks;
    }

    @Column(name = "BTN_NAMES")
    public String getBtnNames() {
        return btnNames;
    }

    public void setBtnNames(String btnNames) {
        this.btnNames = btnNames;
    }

    @Column(name = "BTN_IDS")
    public String getBtnIDs() {
        return btnIDs;
    }

    public void setBtnIDs(String btnIDs) {
        this.btnIDs = btnIDs;
    }

    @Column(name = "PRE_PROCESSOR")
    public String getPreProcessor() {
        return preProcessor;
    }

    public void setPreProcessor(String preProcessor) {
        this.preProcessor = preProcessor;
    }

    @Column(name = "POST_PROCESSOR")
    public String getPostProcessor() {
        return postProcessor;
    }

    public void setPostProcessor(String postProcessor) {
        this.postProcessor = postProcessor;
    }
}
