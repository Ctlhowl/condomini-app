package com.ctlfab.condomini.controller;

import com.ctlfab.condomini.DTO.CondominiumDTO;
import com.ctlfab.condomini.model.Response;
import com.ctlfab.condomini.service.CondominiumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/condominium")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CondominiumController {
    private final CondominiumService condominiumService;

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
                        .data(Map.of("condominium", condominiumService.saveCondominium(condominium)))
                        .message("Condominium created")
                        .httpStatus(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @PutMapping("/edit")
    private ResponseEntity<Response> editCondominium(@RequestParam(value = "id") long id, @RequestBody @Valid CondominiumDTO condominium) {
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
}
