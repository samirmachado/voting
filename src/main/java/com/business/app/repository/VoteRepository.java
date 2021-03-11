package com.business.app.repository;

import com.business.app.repository.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("select v from Vote v where v.session.id = :sessionId")
    Optional<List<Vote>> findBySessionId(@Param("sessionId") Long sessionId);
}
