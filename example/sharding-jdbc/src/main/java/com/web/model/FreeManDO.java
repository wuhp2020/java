package com.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "freeman",
        indexes = {
            @Index(name = "index_freeman_identity", columnList = "identity")
        })
@Data
@Accessors(chain = true)
public class FreeManDO {

    @Id
    @Column
    private Long id;

    @Column
    private String name;

    @Column(unique = true)
    private String identity;

    @Column
    private String sex;

    @Column
    private Integer age;

    @Column
    private String address;
}
