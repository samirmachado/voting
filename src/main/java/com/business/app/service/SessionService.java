package com.business.app.service;

import com.business.app.repository.model.Session;
import com.business.app.repository.model.constant.SessionStatus;
import com.business.app.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public Session create(Session session) {
        session.setSessionStatus(SessionStatus.OPENED);
        if (Objects.isNull(session.getExpirationDate())) {
            session.setExpirationDate(LocalDateTime.now().plusSeconds(60));
        }
        return sessionRepository.save(session);
    }

    public List<Session> listAll() {
        return sessionRepository.findAll();
    }
}
