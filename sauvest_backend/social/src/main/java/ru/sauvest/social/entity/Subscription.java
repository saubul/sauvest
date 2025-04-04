package ru.sauvest.social.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @SequenceGenerator(name = "subscriptions_id_seq", sequenceName = "subscriptions_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscriptions_id_seq")
    private Long id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "users_id_fk"))
    private User user;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "subsciprtion_user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "subsciprtion_users_id_fk"))
    private User subscriptionUser;

}
