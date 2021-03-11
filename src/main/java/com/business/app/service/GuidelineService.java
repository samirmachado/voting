package com.business.app.service;

import com.business.app.repository.model.Guideline;
import com.business.app.repository.GuidelineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuidelineService {

    @Autowired
    private GuidelineRepository guidelineRepository;

    public Guideline create(Guideline guideline) {
        return guidelineRepository.save(guideline);
    }

    public List<Guideline> listAll() {
        return guidelineRepository.findAll();
    }
}
