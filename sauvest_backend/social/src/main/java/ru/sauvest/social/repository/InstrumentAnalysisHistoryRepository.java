package ru.sauvest.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sauvest.social.entity.InstrumentAnalysisHistory;

import java.util.List;

@Repository
public interface InstrumentAnalysisHistoryRepository extends JpaRepository<InstrumentAnalysisHistory, Long>{

    @Query("SELECT e FROM InstrumentAnalysisHistory e JOIN User u ON e.user = u WHERE u.username = :username")
    List<InstrumentAnalysisHistory> findAllByUsername(@Param("username") String username);

}
