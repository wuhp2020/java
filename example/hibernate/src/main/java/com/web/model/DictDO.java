package com.web.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "common_dict",
        indexes = {
            @Index(name = "index_dict_type_code", columnList = "type"),
            @Index(name = "index_dict_type_code", columnList = "code")
        })
@Data
public class DictDO {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column
    private String id;

    @Column
    private String type;

    @Column
    private String code;

    @Column
    private String name;

    @Column
    private String relation;

}
