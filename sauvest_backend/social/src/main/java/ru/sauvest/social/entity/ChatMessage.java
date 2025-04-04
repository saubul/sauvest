package ru.sauvest.social.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @SequenceGenerator(name = "chat_messages_id_seq", sequenceName = "chat_messages_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_messages_id_seq")
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "users_id_fk"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "chats_id_fk"))
    private Chat chat;

    @CreationTimestamp
    @Column(name = "creation_date_time")
    private LocalDateTime creationDateTime;

}
