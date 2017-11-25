package com.metarnet.driver.model;


import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2016/3/8.
 */
@Entity
@Table(name = "component_form")
@Where(clause = "DELETED_FLAG=0")
public class ComponentFormEntity implements Serializable{


    private Long id;
    private Long createdBy;/*创建人*/
    private Timestamp creationTime;/*创建时间*/
    private Long lastUpdatedBy;/*最后修改人*/
    private Timestamp lastUpdateTime;/*最后修改时间*/
    private Boolean deletedFlag;/*删除标记*/
    private Long deletedBy;/*删除人*/
    private Timestamp deletionTime;/*删除时间*/

    private String componentName;   //表单名称

    private String pcEditURL;
    private String pcShowURL;

    private String mobileEditURL;
    private String mobileShowURL;

    private Boolean release;    //是否发布

    private String tenantId;    //租户


    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Column(name = "COMPONENT_NAME")
    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    @Column(name = "TENANT_ID")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "PC_EDIT_URL")
    public String getPcEditURL() {
        return pcEditURL;
    }

    public void setPcEditURL(String pcEditURL) {
        this.pcEditURL = pcEditURL;
    }

    @Column(name = "PC_SHOW_URL")
    public String getPcShowURL() {
        return pcShowURL;
    }

    public void setPcShowURL(String pcShowURL) {
        this.pcShowURL = pcShowURL;
    }

    @Column(name = "MOBILE_EDIT_URL")
    public String getMobileEditURL() {
        return mobileEditURL;
    }

    public void setMobileEditURL(String mobileEditURL) {
        this.mobileEditURL = mobileEditURL;
    }

    @Column(name = "MOBILE_SHOW_URL")
    public String getMobileShowURL() {
        return mobileShowURL;
    }

    public void setMobileShowURL(String mobileShowURL) {
        this.mobileShowURL = mobileShowURL;
    }

    @Column(name = "RELEASE_FLAG")
    public Boolean getRelease() {
        return release;
    }

    public void setRelease(Boolean release) {
        this.release = release;
    }
}
