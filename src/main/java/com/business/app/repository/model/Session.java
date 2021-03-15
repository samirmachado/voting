package com.business.app.repository.model;

import com.business.app.repository.model.constant.KafkaSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Enumerated(EnumType.STRING)
    private KafkaSessionStatus kafkaSessionStatus;

    public Boolean isExpired() {
        return Objects.isNull(expirationDate) || expirationDate.isBefore(LocalDateTime.now());
    }
}
