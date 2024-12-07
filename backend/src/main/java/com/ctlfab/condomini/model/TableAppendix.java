package com.ctlfab.condomini.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "table_appendix")
public class TableAppendix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "La tabella dovrebbe avere classificare una serire di spese")
    private String category;

    @NotEmpty(message = "La tabella dovrebbe avere una descrizione")
    private String description;

    @OneToOne(mappedBy = "table", cascade = CascadeType.ALL)
    private Quote quote;

    @OneToMany(mappedBy = "table")
    private List<Outlay> outlays;

    @Override
    public String toString() {
        return "TableAppendix{" +
                "description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", id=" + id +
                '}';
    }
}
