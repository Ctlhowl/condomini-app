package com.ctlfab.condomini.controller;

import com.ctlfab.condomini.DTO.ApartmentDTO;
import com.ctlfab.condomini.DTO.QuoteDTO;
import com.ctlfab.condomini.model.Response;
import com.ctlfab.condomini.service.ApartmentService;
import com.ctlfab.condomini.service.CondominiumService;
import com.ctlfab.condomini.service.QuoteService;
import com.ctlfab.condomini.service.TableAppendixService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/condominium/{condominiumId}/quote")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class QuoteController {
    private final QuoteService quoteService;
    private final TableAppendixService tableAppendixService;
    private final ApartmentService apartmentService;


    @GetMapping("/list")
    public ResponseEntity<Response> getQuotes(@PathVariable(value = "condominiumId") long condominiumId) {
        return  ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("quotes", quoteService.findAllQuotesByCondominium(condominiumId)))
                        .message("Quotes retrieved")
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/details")
    public ResponseEntity<Response> getQuoteByTableAndCondominium(@PathVariable(value = "condominiumId") long condominiumId,
                                                    @RequestParam(value = "tableId") long tableId) {

        QuoteDTO quoteDTO = quoteService.findQuoteByCondominiumAndTableId(condominiumId, tableId);

        if(quoteDTO == null) {
            return  ResponseEntity.ok(
                    Response.builder()
                            .timestamp(now())
                            .data(Map.of("quote", new QuoteDTO()))
                            .message("Quotes retrieved")
                            .httpStatus(NO_CONTENT)
                            .statusCode(NO_CONTENT.value())
                            .build()
            );
        }

        return  ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("quote", quoteDTO))
                        .message("Quotes retrieved")
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/save")
    public ResponseEntity<Response> saveQuote(@PathVariable(value = "condominiumId") long condominiumId,
                                              @Valid @RequestBody QuoteDTO quoteDTO) {

        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("quote", quoteService.saveQuote(quoteDTO, condominiumId, quoteDTO.getTable().getId())))
                        .message("Quote created")
                        .httpStatus(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @PutMapping("/edit")
    private ResponseEntity<Response> editQuote(@RequestParam(value = "id") long id,
                                                   @PathVariable(value = "condominiumId") long condominiumId,
                                                   @RequestBody QuoteDTO quoteDTO) {
        quoteDTO.setId(id);

        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("apartment", quoteService.updateQuote(quoteDTO, condominiumId, quoteDTO.getTable().getId())))
                        .message("Quote updated")
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteApartment(@PathVariable(value = "condominiumId") long condominiumId,
                                                    @RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("deleted", quoteService.deleteQuote(id)))
                        .message("Quote deleted")
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
