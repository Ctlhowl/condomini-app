package com.ctlfab.condomini.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {
    private Long id;

    @NotEmpty(message = "Il file report dovrebbe avere un nome")
    private String name;

    @NotEmpty(message = "Il file report dovrebbe avere un path")
    private String pathFile;

    @CreationTimestamp
    private Timestamp created_at;

    @Override
    public String toString() {
        return "ReportDTO{" +
                "created_at=" + created_at +
                ", path_file='" + pathFile + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
