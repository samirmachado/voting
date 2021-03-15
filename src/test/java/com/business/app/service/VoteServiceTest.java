package com.business.app.service;

import com.business.app.exception.CustomException;
import com.business.app.integration.CpfValidator;
import com.business.app.integration.dto.CpfValidateDto;
import com.business.app.integration.dto.constant.CpfValidadeStatusDto;
import com.business.app.repository.SessionRepository;
import com.business.app.repository.VoteRepository;
import com.business.app.repository.model.Session;
import com.business.app.repository.model.User;
import com.business.app.repository.model.Vote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private CpfValidator cpfValidator;

    @InjectMocks
    private VoteService voteService;

    @Test
    void whenVotingShouldAssociateTheVoteWithTheSession() {

        String cpf = "00000000000";
        CpfValidateDto cpfValidateDto = new CpfValidateDto(CpfValidadeStatusDto.ABLE_TO_VOTE);
        Session session = Session.builder().id(20L).expirationDate(LocalDateTime.now().plusMinutes(30)).build();
        User user = User.builder().id(2L).cpf(cpf).build();
        Vote vote = Vote.builder().id(10L).session(session).user(user).build();

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.ofNullable(session));
        when(cpfValidator.validateCpfToVote(cpf)).thenReturn(cpfValidateDto);
        when(voteRepository.save(any(Vote.class))).then(returnsFirstArg());

        Vote returned = voteService.vote(vote);

        assertEquals(session.getId(), returned.getSession().getId());
    }

    @Test
    void whenVotingWithoutAbleToVoteShouldThrowCustomException() {

        String cpf = "00000000000";
        CpfValidateDto cpfValidateDto = new CpfValidateDto(CpfValidadeStatusDto.UNABLE_TO_VOTE);
        Session session = Session.builder().id(20L).expirationDate(LocalDateTime.now().plusMinutes(30)).build();
        User user = User.builder().id(2L).cpf(cpf).build();
        Vote vote = Vote.builder().id(10L).session(session).user(user).build();

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.ofNullable(session));
        when(cpfValidator.validateCpfToVote(cpf)).thenReturn(cpfValidateDto);

        Exception exception = assertThrows(CustomException.class, () -> {
            voteService.vote(vote);
        });

        assertEquals(VoteService.THIS_USER_CANNOT_VOTE, exception.getMessage());
    }

    @Test
    void whenVotingAndSessionNotExistsShouldThrowCustomException() {

        String cpf = "00000000000";
        Session session = Session.builder().id(20L).expirationDate(LocalDateTime.now().plusMinutes(30)).build();
        User user = User.builder().id(2L).cpf(cpf).build();
        Vote vote = Vote.builder().id(10L).session(session).user(user).build();

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(CustomException.class, () -> {
            voteService.vote(vote);
        });

        assertEquals(VoteService.THE_SESSION_DOESN_T_EXIST, exception.getMessage());
    }

    @Test
    void whenVotingAndSessionIsExpiredShouldThrowCustomException() {

        String cpf = "00000000000";
        Session session = Session.builder().id(20L).expirationDate(LocalDateTime.now().minusMinutes(30)).build();
        User user = User.builder().id(2L).cpf(cpf).build();
        Vote vote = Vote.builder().id(10L).session(session).user(user).build();

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.ofNullable(session));

        Exception exception = assertThrows(CustomException.class, () -> {
            voteService.vote(vote);
        });

        assertEquals(VoteService.THE_SESSION_HAS_ALREADY_BEEN_CLOSED, exception.getMessage());
    }

    @Test
    void whenVotingAndValidateCpfReturn404ShouldThrowCustomException() {

        String cpf = "00000000000";
        CpfValidateDto cpfValidateDto = new CpfValidateDto(CpfValidadeStatusDto.UNABLE_TO_VOTE);
        Session session = Session.builder().id(20L).expirationDate(LocalDateTime.now().plusMinutes(30)).build();
        User user = User.builder().id(2L).cpf(cpf).build();
        Vote vote = Vote.builder().id(10L).session(session).user(user).build();

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.ofNullable(session));
        when(cpfValidator.validateCpfToVote(cpf)).thenThrow(new CustomException(VoteService.THIS_CPF_IS_INVALID, HttpStatus.UNPROCESSABLE_ENTITY));

        Exception exception = assertThrows(CustomException.class, () -> {
            voteService.vote(vote);
        });

        assertEquals(VoteService.THIS_CPF_IS_INVALID, exception.getMessage());
    }

    @Test
    void whenListingAllVotesShouldReturnAListOfVotes() {
        List<Vote> votes = new ArrayList(Arrays.asList(Vote.builder().id(1L).build(), Vote.builder().id(2L).build()));

        when(voteRepository.findAll()).thenReturn(votes);

        List<Vote> returned = voteService.listAll();

        assertEquals(votes.size(), returned.size());
    }
}
