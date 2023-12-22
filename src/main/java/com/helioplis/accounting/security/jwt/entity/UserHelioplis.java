package com.helioplis.accounting.security.jwt.entity;

import java.util.Set;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name="users")
public class UserHelioplis {

    @Id
    @SequenceGenerator(name = "users_id_seq",sequenceName = "users_id_seq",allocationSize = 1)
    @GeneratedValue(
            generator = "users_id_seq"
    )
    @Column(name="id")
    private Integer id;

    @Column(name="user_name")
    private String username;

    @Column(name="user_passwd")
    private String password;

    @Column(name="user_email")
    private String email;

    @ElementCollection(fetch= FetchType.EAGER)
    @CollectionTable(
            name="roles",
            joinColumns = @JoinColumn(name="user_id")
    )
    @Column(name="user_role")
    private Set<String> roles;
}