package com.ctlfab.condomini.controller;

import com.ctlfab.condomini.DTO.OutlayDTO;
import com.ctlfab.condomini.model.Response;
import com.ctlfab.condomini.service.OutlayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/condominium/{condominiumId}/outlay")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class OutlayController {
    private final OutlayService outlayService;


    @GetMapping("/list")
    public ResponseEntity<Response> getCondominiumOutlays(@PathVariable(value = "condominiumId") long condominiumId) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("outlays", outlayService.findAllOutlaysByCondominiumId(condominiumId)))
                        .message("Outlays retrieved")
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getApartmentOutlays(@PathVariable(value = "condominiumId") long condominiumId,
                                                        @PathVariable(value = "id") long id) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("outlays", outlayService.findApartmentOutlaysByApartmentId(id)))
                        .message("Outlays retrieved")
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/save")
    public ResponseEntity<Response> saveOutlay(@PathVariable(value = "condominiumId") long condominiumId,
                                              @RequestParam(value = "apartmentId", required = false) Long apartmentId,
                                              @Valid @RequestBody OutlayDTO outlayDTO) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("outlay", outlayService.saveOutlay(outlayDTO, condominiumId, apartmentId, outlayDTO.getTable().getId())))
                        .message("Outlay created")
                        .httpStatus(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );

    }

    @PutMapping("/edit")
    private ResponseEntity<Response> editOutlay(@PathVariable(value = "condominiumId") long condominiumId,
                                               @RequestParam(value = "outlayId") long outlayId,
                                               @RequestParam(value = "apartmentId", required = false) Long apartmentId,
                                               @Valid @RequestBody OutlayDTO outlayDTO) {
        outlayDTO.setId(Long.valueOf(outlayId));
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("outlay", outlayService.updateOutlay(outlayDTO, condominiumId, apartmentId,outlayDTO.getTable().getId())))
                        .message("Outlay created")
                        .httpStatus(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );

    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteOutlay(@PathVariable(value = "condominiumId") long condominiumId,
                                                    @RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("deleted", outlayService.deleteOutlay(id)))
                        .message("Outlay deleted")
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
