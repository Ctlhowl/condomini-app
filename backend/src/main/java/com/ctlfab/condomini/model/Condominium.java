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
    private String name;

    @NotEmpty(message = "Il condominio dovrebbe avere un indirizzo")
    private String address;
    private float lastYearBalance;

    @OneToMany(mappedBy = "condominium", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Outlay> outlays;

    @OneToMany(mappedBy = "condominium", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Apartment> apartments;

    @OneToMany(mappedBy = "condominium", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Report> reports;

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
