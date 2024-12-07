package com.ctlfab.condomini.model;

import com.ctlfab.condomini.enumeration.OperationType;
import com.ctlfab.condomini.enumeration.OutlayType;
import com.ctlfab.condomini.enumeration.PaymentMethod;
import jakarta.persistence.*;
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
@Entity
@Table(name = "outlay")
public class Outlay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "La spesa dovrebbe essere in Entrata  o Uscita")
    private OperationType operationType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp created_at;

    @DecimalMin(value = "0.00", message = "La spesa dovrebbe avere un importo")
    private float amount;

    @NotEmpty(message = "La spesa dovrebbe avere una descrizione")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "La spesa dovrebbe un metodo di pagamento")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "La spesa dovrebbe essere in Ordinaria o Straordinaria")
    private OutlayType outlayType;

    @ManyToOne
    @JoinColumn(name = "table_id")
    @NotNull(message = "Una spesa dovrebbe riferirsi ad una tabella")
    private TableAppendix table;

    @ManyToOne
    @JoinColumn(name = "appartament_id")
    private Apartment apartment;

    @ManyToOne
    @JoinColumn(name = "condominium_id", nullable = false)
    private Condominium condominium;

    @Override
    public String toString() {
        return "Outlay{" +
                "id=" + id +
                ", operationType=" + operationType +
                ", created_at=" + created_at +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", paymentMethod=" + paymentMethod +
                ", outlayType=" + outlayType +
                '}';
    }
}
