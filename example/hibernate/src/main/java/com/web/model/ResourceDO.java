package com.web.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "common_resource")
@Data
public class ResourceDO {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column
    private String id;

    @Column
    private String resourceName;

    @Column
    private String url;

    @Column
    private String methodName;

    @Column
    private String resourceGroupName;

}
