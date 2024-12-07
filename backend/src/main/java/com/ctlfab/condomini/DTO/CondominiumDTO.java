package com.ctlfab.condomini.DTO;

import com.ctlfab.condomini.model.Quote;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CondominiumDTO {
    private Long id;
    @NotEmpty(message = "Il condominio dovrebbe avere un nome")
    private String name;

    @NotEmpty(message = "Il condominio dovrebbe avere un indirizzo")
    private String address;
    private float lastYearBalance;

    private List<OutlayDTO> outlays;

    private List<ApartmentDTO> apartments;

    private List<ReportDTO> reports;

    private List<QuoteDTO> quotes;

    @Override
    public String toString() {
        return "CondominiumDTO{" +
                "lastYearBalance=" + lastYearBalance +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
