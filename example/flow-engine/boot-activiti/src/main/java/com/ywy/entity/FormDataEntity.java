package com.ywy.entity;

/**
 * 表单业务数据Entity
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-08 17:05
 */
public class FormDataEntity {
    /**
     * ID
     */
    private Long id;

    /**
     * 流程定义ID
     */
    private String procDefId;

    /**
     * 流程实例ID
     */
    private String procInstId;

    /**
     * 表单key
     */
    private String formKey;

    /**
     * 控件ID
     */
    private String controlId;

    /**
     * 控件存放值
     */
    private String controlValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getControlId() {
        return controlId;
    }

    public void setControlId(String controlId) {
        this.controlId = controlId;
    }

    public String getControlValue() {
        return controlValue;
    }

    public void setControlValue(String controlValue) {
        this.controlValue = controlValue;
    }
}
