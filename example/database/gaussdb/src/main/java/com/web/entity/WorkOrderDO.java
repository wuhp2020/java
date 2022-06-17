package com.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "work_order")
public class WorkOrderDO {
    private Integer id;

    private String workOrderId;

    private Integer relatedSize;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId == null ? null : workOrderId.trim();
    }

    public Integer getRelatedSize() {
        return relatedSize;
    }

    public void setRelatedSize(Integer relatedSize) {
        this.relatedSize = relatedSize;
    }
}