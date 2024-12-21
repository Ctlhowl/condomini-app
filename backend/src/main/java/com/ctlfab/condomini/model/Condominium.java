package com.ctlfab.condomini.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "condominium")
public class Condominium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Il condominio dovrebbe avere un nome")
    @Column(name = "name", nullable = false)
    private String name;

    @NotEmpty(message = "Il condominio dovrebbe avere un indirizzo")
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "last_year_balance", nullable = false)
    private float lastYearBalance;

    @OneToMany(mappedBy = "condominium", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Outlay> outlays;

    @OneToMany(mappedBy = "condominium", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Apartment> apartments;

    @OneToMany(mappedBy = "condominium", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Quote> quotes;

    @Override
    public String toString() {
        return "Condominium{" +
                "lastYearBalance=" + lastYearBalance +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
