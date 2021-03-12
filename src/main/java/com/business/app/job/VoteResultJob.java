package com.business.app.job;

import com.business.app.exception.CustomException;
import com.business.app.repository.SessionRepository;
import com.business.app.repository.model.Session;
import com.business.app.repository.model.constant.KafkaSessionStatus;
import com.business.app.service.SessionService;
import com.business.app.service.kafka.SessionResultService;
import com.business.app.service.pojo.SessionResultPojo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class VoteResultJob {

    @Autowired
    private SessionResultService sessionResultService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionService sessionService;

    @Scheduled(fixedRateString = "${vote.result.check.fixedRate.in.milliseconds}")
    public void sendResultJob() {
        List<Session> sessionsExpired = sessionRepository.findAllSessionsExpiredAndNotSubmitted().orElse(new ArrayList<>());
        sessionsExpired.stream().forEach(s -> {
            log.info("Processing session:  ID={} to send to Kafka", s.getId());
            submitSessionResult(s);
        });
    }

    private void submitSessionResult(Session session) {
        try {
            sendSessionResultToKafka(session);
            updateStatusToSubmitted(session);
        } catch (CustomException e) {
            log.debug(e.getMessage());
        }
    }

    private void sendSessionResultToKafka(Session session) {
        SessionResultPojo sessionResultPojo = sessionService.getSessionResult(session.getId());
        sessionResultService.sendDataToKafkaTopic(sessionResultPojo);
    }

    private void updateStatusToSubmitted(Session session) {
        session.setKafkaSessionStatus(KafkaSessionStatus.SUBMITTED);
        sessionRepository.save(session);
    }
}
