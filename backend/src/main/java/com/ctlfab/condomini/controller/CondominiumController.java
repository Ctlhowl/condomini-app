package com.ctlfab.condomini.controller;

import com.ctlfab.condomini.dto.CondominiumDTO;
import com.ctlfab.condomini.model.Response;
import com.ctlfab.condomini.service.CondominiumService;
import com.ctlfab.condomini.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/condominium")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CondominiumController {
    private final CondominiumService condominiumService;
    private final ReportService reportService;

    @GetMapping("/list")
    public ResponseEntity<Response> getCondominiums() {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("condominiums", condominiumService.findAllCondominiums()))
                        .message("Condominiums retrieved")
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/details")
    public ResponseEntity<Response> getCondominium(@RequestParam(value = "id") long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("condominium", condominiumService.findCondominiumById(id)))
                        .message("Condominium retrieved")
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PostMapping("/save")
    public ResponseEntity<Response> saveCondominium(@RequestBody @Valid CondominiumDTO condominium) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("newCondominium", condominiumService.saveCondominium(condominium)))
                        .message("Condominium created")
                        .httpStatus(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @PutMapping("/edit")
    public ResponseEntity<Response> editCondominium(@RequestParam(value = "id") long id, @RequestBody @Valid CondominiumDTO condominium) {
        condominium.setId(id);

        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("condominium", condominiumService.updateCondominium(condominium)))
                        .message("Condominium updated")
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteCondominium(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("deleted", condominiumService.deleteCondominium(id)))
                        .message("Condominium deleted")
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/export/{condominiumId}")
    public ResponseEntity<InputStreamResource> exportCondominium(@PathVariable(value = "condominiumId") long condominiumId,
                                                                 @RequestParam(value = "year") int year) {

        CondominiumDTO condominiumDTO = condominiumService.findCondominiumByIdAndYear(condominiumId, year);
        ByteArrayInputStream bais = reportService.exportToPDF(condominiumDTO, year);

        HttpHeaders headers = new HttpHeaders();
        String filename = year + "_report.pdf";
        headers.add("Content-Disposition", "inline; filename=" + filename);

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bais));
    }
}
