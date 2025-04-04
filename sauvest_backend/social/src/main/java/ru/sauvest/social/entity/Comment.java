package ru.sauvest.social.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @SequenceGenerator(name = "comments_id_seq", sequenceName = "comments_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_id_seq")
    private Long id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "users_id_fk"))
    private User user;

    private String content;

    @CreationTimestamp
    @Column(name = "creation_date_time")
    private LocalDateTime creationDateTime;

    @ManyToOne(targetEntity = Post.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "posts_id_fk"))
    private Post post;
}
