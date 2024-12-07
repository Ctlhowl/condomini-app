package com.ctlfab.condomini.DTO;

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
public class QuoteDTO {
    private Long id;

    @DecimalMin(value = "0.00", message = "Il preventivo dovrebbe avere un importo")
    private float totalAmount;

    @CreationTimestamp
    private Timestamp createdAt;

    @NotNull(message = "Il preventivo dovrebbe essere associato ad una tabella di riferimento")
    private TableAppendixDTO table;

    @Override
    public String toString() {
        return "QuoteDTO{" +
                "createdAt=" + createdAt +
                ", id=" + id +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
