package com.business.app.service;

import com.business.app.exception.CustomException;
import com.business.app.integration.CpfValidator;
import com.business.app.repository.SessionRepository;
import com.business.app.repository.VoteRepository;
import com.business.app.repository.model.Session;
import com.business.app.repository.model.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoteService {

    public static final String THE_SESSION_DOESN_T_EXIST = "The session doesn't exist";
    public static final String THE_SESSION_HAS_ALREADY_BEEN_CLOSED = "The session has already been closed";
    public static final String THIS_USER_CANNOT_VOTE = "this user cannot vote";
    public static final String THIS_CPF_IS_INVALID = "this CPF is invalid";

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CpfValidator cpfValidator;

    public Vote vote(Vote vote) {
        Optional<Session> session = sessionRepository.findById(vote.getSession().getId());
        if (!session.isPresent()) {
            throw new CustomException(THE_SESSION_DOESN_T_EXIST, HttpStatus.NOT_FOUND);
        }
        if (session.get().isExpired()) {
            throw new CustomException(THE_SESSION_HAS_ALREADY_BEEN_CLOSED, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!cpfCanVote(vote.getUser().getCpf())) {
            throw new CustomException(THIS_USER_CANNOT_VOTE, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        vote.setSession(session.get());
        return voteRepository.save(vote);
    }

    private Boolean cpfCanVote(String cpf) {
        try {
            return cpfValidator.validateCpfToVote(cpf).isValid();
        } catch (CustomException e) {
            throw new CustomException(THIS_CPF_IS_INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public List<Vote> listAll() {
        return voteRepository.findAll();
    }
}
