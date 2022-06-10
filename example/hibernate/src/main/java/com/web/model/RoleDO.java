package com.web.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "common_role")
@Data
public class RoleDO {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column
    private String id;

    @Column
    private String roleName;

    @OneToMany(mappedBy = "role",
            cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER)
    private List<RoleLinkedResourceDO> roleLinkedResources;

}
