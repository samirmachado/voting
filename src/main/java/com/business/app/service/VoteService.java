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

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CpfValidator cpfValidator;

    public Vote vote(Vote vote) {
        Optional<Session> session = sessionRepository.findById(vote.getSession().getId());
        if (!session.isPresent()) {
            throw new CustomException("The session doesn't exist", HttpStatus.NOT_FOUND);
        }
        if (session.get().isExpired()) {
            throw new CustomException("The session has already been closed", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!cpfCanVote(vote.getUser().getCpf())) {
            throw new CustomException("this user cannot vote", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        vote.setSession(session.get());
        return voteRepository.save(vote);
    }

    private Boolean cpfCanVote(String cpf) {
        try {
            return cpfValidator.validateCpfToVote(cpf).isValid();
        } catch (CustomException e) {
            throw new CustomException("this CPF is invalid", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public List<Vote> listAll() {
        return voteRepository.findAll();
    }
}
