package com.ctlfab.condomini.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "apartment")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "L'appartamento dovrebbe avere un proprietario")
    private String owner;

    private String tenant;

    @DecimalMin(value = "0.00", message = "L'appartamento dovrebbe avere i millesimi relativi alla Tabella A")
    private float millTabA;

    @DecimalMin(value = "0.00", message = "L'appartamento dovrebbe avere i millesimi relativi alla Tabella B")
    private float millTabB;

    @DecimalMin(value = "0.00", message = "L'appartamento dovrebbe avere i millesimi relativi alla Tabella C")
    private float millTabC;

    @DecimalMin(value = "0.00", message = "L'appartamento dovrebbe avere i millesimi relativi alla Tabella D")
    private float millTabD;

    @NotEmpty(message = "L'appartamento dovrebbe essere collocato in una scala")
    private String scala;

    private float lastYearBalance;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.REMOVE)
    private List<Outlay> outlays;

    @ManyToOne
    @JoinColumn(name = "condominium_id", nullable = false)
    private Condominium condominium;

    @Override
    public String toString() {
        return "Apartment{" +
                "id=" + id +
                ", owner='" + owner + '\'' +
                ", tenant='" + tenant + '\'' +
                ", millTabA=" + millTabA +
                ", millTabB=" + millTabB +
                ", millTabC=" + millTabC +
                ", millTabD=" + millTabD +
                ", scala='" + scala + '\'' +
                ", lastYearBalance=" + lastYearBalance +
                '}';
    }
}
