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

    @DecimalMin(value = "0.00", message = "La spesa dovrebbe avere un importo")
    @Column(name = "amount", nullable = false)
    private float amount;

    @NotEmpty(message = "La spesa dovrebbe avere una descrizione")
    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "La spesa dovrebbe essere in Ordinaria o Straordinaria")
    @Column(name = "outlay_type", nullable = false)
    private OutlayType outlayType;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "La spesa dovrebbe essere in Entrata  o Uscita")
    @Column(name = "operation_type", nullable = false)
    private OperationType operationType;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "La spesa dovrebbe un metodo di pagamento")
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
    

    @ManyToOne
    @JoinColumn(name = "table_id")
    @NotNull(message = "Una spesa dovrebbe riferirsi ad una tabella")
    private TableAppendix table;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @ManyToOne
    @JoinColumn(name = "condominium_id", nullable = false)
    private Condominium condominium;

    @Override
    public String toString() {
        return "Outlay{" +
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
