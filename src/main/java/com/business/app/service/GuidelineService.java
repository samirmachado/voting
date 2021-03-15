package com.business.app.service;

import com.business.app.repository.model.Guideline;
import com.business.app.repository.GuidelineRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class GuidelineService {

    @Autowired
    private GuidelineRepository guidelineRepository;

    public Guideline create(Guideline guideline) {
        log.info("Creating Guideline: {}", guideline);
        Guideline created = guidelineRepository.save(guideline);
        log.info("Guideline created: {}", created);
        return created;
    }

    public List<Guideline> listAll() {
        log.info("Listing Guidelines");
        return guidelineRepository.findAll();
    }
}
