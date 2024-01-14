package com.helioplis.accounting.security.jwt.entity;

import java.math.BigDecimal;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Column(name="user_name")
    private String username;

    @NotNull
    @Column(name="user_passwd")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name="user_email")
    private String email;

    @NotNull
    @Column(name="hourly_rate", precision = 5,scale = 5, nullable = false)
    private BigDecimal hourlyRate;

    @ElementCollection(fetch= FetchType.EAGER)
    @CollectionTable(
            name="roles",
            joinColumns = @JoinColumn(name="user_id")
    )
    @Column(name="user_role")
    private Set<String> roles;
}