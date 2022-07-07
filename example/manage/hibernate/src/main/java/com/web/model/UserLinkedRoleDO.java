package com.web.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "common_user_linked_role")
@Data
public class UserLinkedRoleDO {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "id")
    private UserDO user;

    @Column
    private String roleId;

}
