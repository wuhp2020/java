package com.web.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "common_user")
@Data
public class UserDO {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column
    private String id;

    @Column
    private String username;

    @Column
    private String password;

    @OneToMany(mappedBy = "user",
            cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER)
    private List<UserLinkedRoleDO> userLinkedRoles;

}
