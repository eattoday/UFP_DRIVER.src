package com.metarnet.driver.model;

import com.metarnet.core.common.model.BaseForm;
import com.metarnet.core.common.model.GeneralInfoModel;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/4/14.
 */
@Entity
@Table(name = "general_process")
@Where(clause = "DELETED_FLAG=0")
@AttributeOverrides({
        @AttributeOverride(name = "objectId", column = @Column(name = "GENERAL_ID", nullable = false))})
public class GeneralProcessEntity extends BaseForm {

    private String formDataId;  //表单数据ID
    private String formId;      //表单ID
    private String tenantId;    //租户ID
    private String processModelName;    //流程定义名称

    @Column(name = "FORM_DATA_ID")
    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
    }

    @Column(name = "FORM_ID")
    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    @Column(name = "TENANT_ID")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "PROCESS_MODEL_NAME")
    public String getProcessModelName() {
        return processModelName;
    }

    public void setProcessModelName(String processModelName) {
        this.processModelName = processModelName;
    }
}
