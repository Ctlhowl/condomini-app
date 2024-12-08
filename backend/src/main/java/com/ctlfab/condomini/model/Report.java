package com.ctlfab.condomini.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Il file report dovrebbe avere un path")
    @Column(name = "path_file", nullable = false)
    private String path_file;

    @ManyToOne
    @JoinColumn(name = "condominium_id")
    @NotNull(message = "Il report dovrebbe avere un condominio associato")
    private Condominium condominium;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp created_at;

    @Override
    public String toString() {
        return "Report{" +
                "created_at=" + created_at +
                ", path_file='" + path_file + '\'' +
                ", id=" + id +
                '}';
    }
}
