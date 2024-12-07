package com.ctlfab.condomini.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableAppendixDTO {
    private Long id;

    @NotEmpty(message = "La tabella dovrebbe avere classificare una serire di spese")
    private String category;

    @NotEmpty(message = "La tabella dovrebbe avere una descrizione")
    private String description;

    @Override
    public String toString() {
        return "TableAppendixDTO{" +
                "description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", id=" + id +
                '}';
    }
}
