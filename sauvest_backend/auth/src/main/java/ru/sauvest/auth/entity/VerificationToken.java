package ru.sauvest.auth.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationToken {

	@Id
	@SequenceGenerator(name = "verification_tokens_id_seq", sequenceName = "verification_tokens_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verification_tokens_id_seq")
	private Long id;

	private String token;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "user_id_fk"))
	private User user;
	
	@CreationTimestamp
	@Column(name = "creation_date_time")
	private LocalDateTime creationDateTime;
	
}
