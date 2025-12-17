package com.renato.pruebatecnica.seek.prueba_tecnica_seek.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.Instant;

@Entity
@Table(name = "user")
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
