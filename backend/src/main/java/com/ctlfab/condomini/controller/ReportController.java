package com.ctlfab.condomini.controller;

import com.ctlfab.condomini.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/condominium/{id}/report")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ReportController {
    private final ReportService reportService;

}
