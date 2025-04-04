package ru.sauvest.social.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instruments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Instrument {

    @Id
    @SequenceGenerator(name = "instruments_id_seq", sequenceName = "instruments_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instruments_id_seq")
    private Long id;

    private String figi;

    private String name;

    private String ticker;

    private String isin;

    @Column(name = "class_code")
    private String classCode;

    @Column(name = "instrument_type")
    private String instrumentType;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(targetEntity = Post.class, mappedBy = "instruments")
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

}
