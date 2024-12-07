package com.ctlfab.condomini.controller;

import com.ctlfab.condomini.DTO.ApartmentDTO;
import com.ctlfab.condomini.model.Response;
import com.ctlfab.condomini.service.ApartmentService;
import com.ctlfab.condomini.service.CondominiumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/condominium/{condominiumId}/apartment")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ApartmentController {
    private final ApartmentService apartmentService;

    @GetMapping("/list")
    public ResponseEntity<Response> getApartments(@PathVariable(value = "condominiumId") long condominiumId) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("apartments", apartmentService.findApartmentsByCondominiumId(condominiumId)))
                        .message("Apartments retrieved")
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/save")
    public ResponseEntity<Response> saveApartment(@PathVariable(value = "condominiumId") long condominiumId,
                                                  @RequestBody @Valid ApartmentDTO apartmentDTO) {

        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("apartment", apartmentService.saveApartment(apartmentDTO, condominiumId)))
                        .message("Apartment created")
                        .httpStatus(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @PutMapping("/edit")
    private ResponseEntity<Response> editApartment(@PathVariable(value = "condominiumId", required = false) Long condominiumId,
                                                   @RequestParam(value = "id") long id,
                                                   @RequestBody @Valid ApartmentDTO apartmentDTO) {
        apartmentDTO.setId(id);

        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("apartment", apartmentService.updateApartment(apartmentDTO, condominiumId)))
                        .message("Apartment updated")
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteApartment(@PathVariable(value = "condominiumId", required = false) Long condominiumId,
                                                    @RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("deleted", apartmentService.deleteApartment(id)))
                        .message("Apartment deleted")
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
