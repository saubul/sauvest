package ru.sauvest.social.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {

    @Id
    @SequenceGenerator(name = "chats_id_seq", sequenceName = "chats_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chats_id_seq")
    private Long id;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany
    @JoinTable(name = "users_chats",
            joinColumns = {@JoinColumn(name = "chat_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "chats_id"))},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "users_id"))})
    @Builder.Default
    private List<User> users = new ArrayList<>();

    @Column(nullable = false)
    private String name;
}
