package com.ywy.activiti;

import java.io.Serializable;

/**
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-02 10:47
 */
public class UELPojo implements Serializable {
    private String user;
    private String pay;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}
