package com.business.app.service;

import com.business.app.exception.CustomException;
import com.business.app.repository.SessionRepository;
import com.business.app.repository.VoteRepository;
import com.business.app.repository.model.Session;
import com.business.app.repository.model.Vote;
import com.business.app.service.pojo.SessionResultPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private VoteRepository voteRepository;

    public Session create(Session session) {
        if (Objects.isNull(session.getExpirationDate())) {
            session.setExpirationDate(LocalDateTime.now().plusSeconds(60));
        }
        return sessionRepository.save(session);
    }

    public SessionResultPojo getSessionResult(Long sessionId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new CustomException("Session not found", HttpStatus.NOT_FOUND));
        List<Vote> votes = voteRepository.findBySessionId(sessionId).orElseThrow(() -> new CustomException("Votes not found", HttpStatus.NOT_FOUND));
        SessionResultPojo sessionResultPojo = SessionResultPojo.builder().build();
        votes.stream().forEach(v -> {
            if(v.getVote()){
                sessionResultPojo.setYes(sessionResultPojo.getYes()+1);
            }else{
                sessionResultPojo.setNo(sessionResultPojo.getNo()+1);
            }
        });
        sessionResultPojo.setClosed(!session.isNotExpired());
        return sessionResultPojo;
    }

    public List<Session> listAll() {
        return sessionRepository.findAll();
    }
}
