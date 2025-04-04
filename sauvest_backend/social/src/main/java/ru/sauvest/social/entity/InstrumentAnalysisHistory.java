package ru.sauvest.social.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "instrument_analysis_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstrumentAnalysisHistory {

    @Id
    @SequenceGenerator(name = "instrument_analysis_history_id_seq", sequenceName = "instrument_analysis_history_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instrument_analysis_history_id_seq")
    private Long id;

    @CreationTimestamp
    @Column(name = "creation_date_time")
    private LocalDateTime creationDateTime;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "users_id_fk"))
    private User user;

    @ManyToOne(targetEntity = Instrument.class)
    @JoinColumn(name = "instrument_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "instruments_id_fk"))
    private Instrument instrument;

    private Float recommendation;

    private Float ema;

    private Float rsi;

    private Float macd;

    private Float stohastic;

    private Float parabolic;

    @Column(name = "analysis_date")
    private Date analysisDate;

}
