package com.business.app.service;

import com.business.app.repository.GuidelineRepository;
import com.business.app.repository.model.Guideline;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GuidelineServiceTest {

    @Mock
    private GuidelineRepository guidelineRepository;

    @InjectMocks
    private GuidelineService guidelineService;

    @Test
    void whenCreatingTheGuidelineShouldReturnTheCreatedGuideline() {
        Guideline guideline = Guideline.builder().build();
        Guideline createdGuideline = Guideline.builder().id(1L).build();

        when(guidelineRepository.save(guideline)).thenReturn(createdGuideline);

        Guideline returned = guidelineService.create(guideline);

        assertEquals(createdGuideline.getId(), returned.getId());
    }

    @Test
    void whenListingAllGuidelinesShouldReturnAListOfGuidelines() {
        List<Guideline> guidelines = new ArrayList(Arrays.asList(Guideline.builder().id(1L).build(), Guideline.builder().id(2L).build()));

        when(guidelineRepository.findAll()).thenReturn(guidelines);

        List<Guideline> returned = guidelineService.listAll();

        assertEquals(guidelines.size(), returned.size());
    }
}
