package com.web.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "common_operationlog",
        indexes = {
                @Index(name = "index_operationlog_clientip", columnList = "clientIP"),
                @Index(name = "index_operationlog_requesturl", columnList = "requestURL"),
                @Index(name = "index_operationlog_username", columnList = "username"),
                @Index(name = "index_operationlog_requesttime", columnList = "requestTime")
        })
@Data
public class OperationLogDO {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column
    private String id;

    @Column
    private String clientIP;

    @Column
    private Date requestTime;

    @Column
    private String requestURL;

    @Column
    private String username;

    @Column(columnDefinition = "text")
    private String inputParams;

    @Column(columnDefinition = "text")
    private String outputParams;

}
