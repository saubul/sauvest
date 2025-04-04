package ru.sauvest.social.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @SequenceGenerator(name = "posts_id_seq", sequenceName = "posts_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "posts_id_seq")
    private Long id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "users_id_fk"), nullable = false)
    private User user;

    @Column(length = 500)
    private String content;

    @CreationTimestamp
    @Column(name = "creation_date_time")
    private LocalDateTime creationDateTime;

    @Column(name = "img_path")
    private String imgPath;

    @Column(name = "vote_count", columnDefinition = "bigint default 0")
    private int voteCount;

    @OneToMany(targetEntity = Comment.class, mappedBy = "post")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(targetEntity = Vote.class, mappedBy = "post")
    @Builder.Default
    private List<Vote> votes = new ArrayList<>();

    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(targetEntity = Instrument.class)
    @JoinTable(name = "posts_instruments",
            joinColumns = {@JoinColumn(name = "post_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "posts_id_fk"))},
            inverseJoinColumns = {@JoinColumn(name = "instrument_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "instruments_id_fk"))})
    @Builder.Default
    private List<Instrument> instruments = new ArrayList<>();

}
