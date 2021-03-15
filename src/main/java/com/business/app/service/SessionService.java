package com.business.app.service;

import com.business.app.exception.CustomException;
import com.business.app.repository.SessionRepository;
import com.business.app.repository.VoteRepository;
import com.business.app.repository.model.Session;
import com.business.app.repository.model.Vote;
import com.business.app.repository.model.constant.KafkaSessionStatus;
import com.business.app.service.pojo.SessionResultPojo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Log4j2
public class SessionService {

    public static final String SESSION_NOT_FOUND = "Session not found";

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private VoteRepository voteRepository;

    public Session create(Session session) {
        log.info("Creating Session: {}", session);
        if (Objects.isNull(session.getExpirationDate())) {
            session.setExpirationDate(LocalDateTime.now().plusSeconds(60));
        }
        session.setKafkaSessionStatus(KafkaSessionStatus.WAITING_FOR_CONCLUSION);
        Session createdSession = sessionRepository.save(session);
        log.info("Created Session: {}", createdSession);
        return createdSession;
    }

    public SessionResultPojo getSessionResult(Long sessionId) {
        log.info("Find Session Result by session id: {}", sessionId);
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new CustomException(SESSION_NOT_FOUND, HttpStatus.NOT_FOUND));
        List<Vote> votes = voteRepository.findBySessionId(sessionId).orElse(new ArrayList<>());
        SessionResultPojo sessionResultPojo = new SessionResultPojo();
        votes.stream().forEach(v -> {
            if(v.getVote()){
                sessionResultPojo.setYes(sessionResultPojo.getYes()+1);
            }else{
                sessionResultPojo.setNo(sessionResultPojo.getNo()+1);
            }
        });
        sessionResultPojo.setClosed(session.isExpired());
        log.info("Session result is: {}", sessionResultPojo);
        return sessionResultPojo;
    }

    public List<Session> listAll() {
        log.info("Listing Sessions");
        return sessionRepository.findAll();
    }
}
