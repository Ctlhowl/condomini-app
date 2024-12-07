package com.ctlfab.condomini.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
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
@Table(name = "quote")
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DecimalMin(value = "0.00", message = "Il preventivo dovrebbe avere un importo")
    private float totalAmount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @OneToOne
    @NotNull(message = "Il preventivo dovrebbe essere associato ad una tabella di riferimento")
    private TableAppendix table;

    @ManyToOne
    @JoinColumn(name = "condominium_id", nullable = false)
    @NotNull(message = "Il preventivo dovrebbe essere associato ad un condominio")
    private Condominium condominium;

    @Override
    public String toString() {
        return "Quote{" +
                "createdAt=" + createdAt +
                ", id=" + id +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
