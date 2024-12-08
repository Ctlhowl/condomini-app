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
    @Column(name = "owner", nullable = false)
    private String owner;

    @Column(name = "tenant")
    private String tenant;

    @DecimalMin(value = "0.00", message = "L'appartamento dovrebbe avere i millesimi relativi alla Tabella A")
    @Column(name = "mill_tab_a", nullable = false)
    private float millTabA;

    @DecimalMin(value = "0.00", message = "L'appartamento dovrebbe avere i millesimi relativi alla Tabella B")
    @Column(name = "mill_tab_b", nullable = false)
    private float millTabB;

    @DecimalMin(value = "0.00", message = "L'appartamento dovrebbe avere i millesimi relativi alla Tabella C")
    @Column(name = "mill_tab_c", nullable = false)
    private float millTabC;

    @DecimalMin(value = "0.00", message = "L'appartamento dovrebbe avere i millesimi relativi alla Tabella D")
    @Column(name = "mill_tab_d", nullable = false)
    private float millTabD;

    @NotEmpty(message = "L'appartamento dovrebbe essere collocato in una scala")
    @Column(name = "scala", nullable = false)
    private String scala;

    @Column(name = "last_year_balance", nullable = false)
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
