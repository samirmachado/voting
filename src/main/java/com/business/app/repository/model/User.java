package com.business.app.repository.model;

import com.business.app.repository.model.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends Auditable {

    @ElementCollection(fetch = FetchType.EAGER)
    List<Role> roles;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String cpf;
    @Column(nullable = false)
    private String password;
    @OneToMany(mappedBy = "user")
    private List<Vote> votes;
}
