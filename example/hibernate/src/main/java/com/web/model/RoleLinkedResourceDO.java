package com.web.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "common_role_linked_resource")
@Data
public class RoleLinkedResourceDO {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column
    private String id;

    @ManyToOne
    @JoinColumn(name = "role_id",
            referencedColumnName = "id")
    private RoleDO role;

    @Column
    private String resourceId;

}
