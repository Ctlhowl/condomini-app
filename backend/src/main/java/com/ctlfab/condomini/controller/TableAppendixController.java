package com.ctlfab.condomini.controller;

import com.ctlfab.condomini.model.Response;
import com.ctlfab.condomini.service.TableAppendixService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/table")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TableAppendixController {
    private final TableAppendixService tableAppendixService;

    @GetMapping("/list")
    public ResponseEntity<Response> getTableAppendix() {
        return ResponseEntity.ok(
          Response.builder()
                  .timestamp(now())
                  .data(Map.of("tables", tableAppendixService.findAll()))
                  .message("Tables retrieved")
                  .httpStatus(OK)
                  .statusCode(OK.value())
                  .build()
        );
    }

    @GetMapping("/details")
    public ResponseEntity<Response> getTableAppendixById(@RequestParam(value = "id") long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(now())
                        .data(Map.of("tables", tableAppendixService.findTableById(id)))
                        .message("Table: retrieved")
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

}
