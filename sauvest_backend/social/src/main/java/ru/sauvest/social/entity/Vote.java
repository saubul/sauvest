package ru.sauvest.social.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "votes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {

    @Id
    @SequenceGenerator(name = "votes_id_seq", sequenceName = "votes_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "votes_id_seq")
    private Long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "user_id_fk"))
    private User user;

    @ManyToOne(targetEntity = Post.class)
    @JoinColumn(name = "post_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "post_id_fk"))
    private Post post;

}
