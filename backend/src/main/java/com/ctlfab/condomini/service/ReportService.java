package com.ctlfab.condomini.service;

import com.ctlfab.condomini.DTO.ReportDTO;

import java.util.Collection;

public interface ReportService {
    ReportDTO saveReport(ReportDTO reportDTO);
    ReportDTO findReportById(Long reportId);
    Collection<ReportDTO> findAllReportByCondominiumId(Long condominiumId);
}
