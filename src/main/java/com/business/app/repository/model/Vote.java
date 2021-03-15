package com.business.app.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"user_id", "session_id"})
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vote extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean vote;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id")
    private Session session;
}
