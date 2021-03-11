package com.business.app.repository.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Guideline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @OneToOne(mappedBy = "guideline")
    private Session session;
}
