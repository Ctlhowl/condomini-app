package com.ctlfab.condomini.service;

import com.ctlfab.condomini.dto.CondominiumDTO;

import java.io.ByteArrayInputStream;

public interface ReportService {
    ByteArrayInputStream exportToPDF(CondominiumDTO condominiumDTO, int year);
}
