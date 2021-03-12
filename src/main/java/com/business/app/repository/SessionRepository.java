package com.business.app.repository;

import com.business.app.repository.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query("select s from Session s where s.expirationDate < current_timestamp and s.kafkaSessionStatus = 'WAITING_FOR_CONCLUSION'")
    Optional<List<Session>> findAllSessionsExpiredAndNotSubmitted();
}
