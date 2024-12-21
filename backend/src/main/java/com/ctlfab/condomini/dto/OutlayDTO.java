package com.ctlfab.condomini.dto;

import com.ctlfab.condomini.enumeration.OperationType;
import com.ctlfab.condomini.enumeration.OutlayType;
import com.ctlfab.condomini.enumeration.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
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
public class OutlayDTO {

    private Long id;
    @NotNull(message = "La spesa dovrebbe essere in Entrata  o Uscita")
    private OperationType operationType;

    @CreationTimestamp
    private Timestamp createdAt;

    @DecimalMin(value = "0.00", message = "La spesa dovrebbe avere un importo")
    private float amount;

    @NotEmpty(message = "La spesa dovrebbe avere una descrizione")
    private String description;

    @NotNull(message = "La spesa dovrebbe un metodo di pagamento")
    private PaymentMethod paymentMethod;

    @NotNull(message = "La spesa dovrebbe essere in Ordinaria o Straordinaria")
    private OutlayType outlayType;

    @NotNull(message = "Una spesa dovrebbe riferirsi ad una tabella")
    private TableAppendixDTO table;

    @Override
    public String toString() {
        return "OutlayDTO{" +
                "id=" + id +
                ", operationType=" + operationType +
                ", created_at=" + createdAt +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", paymentMethod=" + paymentMethod +
                ", outlayType=" + outlayType +
                '}';
    }
}
