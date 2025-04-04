package ru.sauvest.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @SequenceGenerator(name = "roles_id_seq", sequenceName = "roles_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_seq")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleEnum roleName;

    public enum RoleEnum {

        ROLE_USER, ROLE_ADMIN, ROLE_MODERATOR

    }

}
