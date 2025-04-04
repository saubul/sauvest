package ru.sauvest.social.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(name = "user_email_uq", columnNames = "email"),
        @UniqueConstraint(name = "user_username_uq", columnNames = "username")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    private Long id;

    private String name;

    private String surname;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(name = "sso_token", nullable = false)
    private String ssoToken;

    @OneToMany(targetEntity = Subscription.class, mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Subscription> subscriptions = new HashSet<>();

    @OneToMany(targetEntity = InstrumentAnalysisHistory.class, fetch = FetchType.EAGER, mappedBy = "user")
    private List<InstrumentAnalysisHistory> instrumentAnalysisHistory = new ArrayList<>();

}
