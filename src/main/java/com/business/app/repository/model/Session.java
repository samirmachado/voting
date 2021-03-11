package com.business.app.repository.model;

import com.business.app.repository.model.constant.SessionStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private SessionStatus sessionStatus;

    @OneToMany(mappedBy = "session")
    private List<Vote> votes;

    @OneToOne(optional = false)
    private Guideline guideline;
}
