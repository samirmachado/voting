package com.business.app.repository.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Data
public class Session extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @OneToMany(mappedBy = "session")
    private List<Vote> votes;

    @OneToOne(optional = false)
    private Guideline guideline;

    public Boolean isNotExpired() {
        return Objects.isNull(expirationDate) || LocalDateTime.now().isBefore(expirationDate);
    }
}
