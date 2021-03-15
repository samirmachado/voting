package com.business.app.service;

import com.business.app.repository.SessionRepository;
import com.business.app.repository.VoteRepository;
import com.business.app.repository.model.Session;
import com.business.app.repository.model.Vote;
import com.business.app.repository.model.constant.KafkaSessionStatus;
import com.business.app.service.pojo.SessionResultPojo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private SessionService sessionService;

    @Test
    void whenCreatingTheSessionShouldReturnTheCreatedSession() {
        Session session = Session.builder().build();
        Session createdSession = Session.builder().id(1L).expirationDate(LocalDateTime.now()).build();

        when(sessionRepository.save(session)).thenReturn(createdSession);

        Session returned = sessionService.create(session);

        assertEquals(createdSession.getId(), returned.getId());
    }

    @Test
    void whenCreatingTheSessionWithoutExpirationDateShouldCreateAutomatically() {
        Session session = Session.builder().id(1L).build();

        when(sessionRepository.save(session)).then(returnsFirstArg());

        Session returned = sessionService.create(session);

        assertNotNull(returned.getExpirationDate());
    }

    @Test
    void whenCreatingTheSessionMustCreateWithStatusEqualToWAITING_FOR_CONCLUSION() {
        Session session = Session.builder().id(1L).build();

        when(sessionRepository.save(session)).then(returnsFirstArg());

        Session returned = sessionService.create(session);

        assertEquals(KafkaSessionStatus.WAITING_FOR_CONCLUSION, returned.getKafkaSessionStatus());
    }

    @Test
    void whenListingAllSessionsShouldReturnAListOfSessions() {
        List<Session> sessions = new ArrayList(Arrays.asList(Session.builder().id(1L).build(), Session.builder().id(2L).build()));

        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> returned = sessionService.listAll();

        assertEquals(sessions.size(), returned.size());
    }

    @Test
    void whenFetchingSessionResultItShouldReturnSessionResultWithCalculatedVotes() {
        Long sessionId = 1L;
        Session session = Session.builder().id(sessionId).expirationDate(LocalDateTime.now().minusMinutes(30)).build();
        List<Vote> votes = Arrays.asList(Vote.builder().id(1L).vote(true).build(), Vote.builder().id(2L).vote(false).build());

        when(sessionRepository.findById(sessionId)).thenReturn(java.util.Optional.ofNullable(session));
        when(voteRepository.findBySessionId(sessionId)).thenReturn(java.util.Optional.ofNullable(votes));

        SessionResultPojo returned = sessionService.getSessionResult(sessionId);

        assertTrue(returned.getClosed());
        assertEquals(1, returned.getNo());
        assertEquals(1, returned.getYes());
    }

    @Test
    void whenFetchingSessionResultItAndDateHasNotExpiredShouldReturnSessionResultWithClosedFalse() {
        Long sessionId = 1L;
        Session session = Session.builder().id(sessionId).expirationDate(LocalDateTime.now().plusMinutes(30)).build();
        List<Vote> votes = Arrays.asList(Vote.builder().id(1L).vote(true).build(), Vote.builder().id(2L).vote(false).build());

        when(sessionRepository.findById(sessionId)).thenReturn(java.util.Optional.ofNullable(session));
        when(voteRepository.findBySessionId(sessionId)).thenReturn(java.util.Optional.ofNullable(votes));

        SessionResultPojo returned = sessionService.getSessionResult(sessionId);

        assertFalse(returned.getClosed());
    }
}
