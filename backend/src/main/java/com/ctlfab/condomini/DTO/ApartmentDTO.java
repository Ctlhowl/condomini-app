package com.ctlfab.condomini.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApartmentDTO {
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

    private List<OutlayDTO> outlays;

    @Override
    public String toString() {
        return "ApartmentDTO{" +
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
