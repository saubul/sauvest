package ru.sauvest.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sauvest.social.entity.Instrument;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long> {

    Optional<Instrument> findByFigi(String figi);

    @Query("SELECT p.instruments FROM Post p WHERE p.id = :postId")
    List<Instrument> findByPost(@Param("postId") Long postId);

}
