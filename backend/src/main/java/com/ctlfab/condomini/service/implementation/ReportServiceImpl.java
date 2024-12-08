package com.ctlfab.condomini.service.implementation;

import com.ctlfab.condomini.DTO.ReportDTO;
import com.ctlfab.condomini.model.Condominium;
import com.ctlfab.condomini.model.Report;
import com.ctlfab.condomini.repository.CondominiumRepository;
import com.ctlfab.condomini.repository.ReportRepository;
import com.ctlfab.condomini.service.ReportService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
    private final CondominiumRepository condominiumRepository;

    @Override
    public ReportDTO saveReport(ReportDTO reportDTO) {
        logger.info("Saving report {}", reportDTO);

        Report report = mapDTOToEntity(reportDTO);
        return mapEntityToDTO(reportRepository.save(report));
    }

    @Override
    public ReportDTO findReportById(Long reportId) {
        logger.info("Fetching report by ID: {}", reportId);

        return mapEntityToDTO(reportRepository.findById(reportId).get());
    }

    @Override
    public Collection<ReportDTO> findAllReportByCondominiumId(Long condominiumId) {
        logger.info("Fetching all report by condominium ID: {}", condominiumId);

        Collection<ReportDTO> reports = new LinkedList<>();

        for(Report report : reportRepository.findAllReportByCondominiumId(condominiumId)) {
            reports.add(mapEntityToDTO(report));
        }

        return reports;
    }

    private Report mapDTOToEntity(ReportDTO reportDTO) {
        Condominium condominium = condominiumRepository.findCondominiumByReportId(reportDTO.getId());

        return Report.builder()
                .id(reportDTO.getId())
                .path_file(reportDTO.getPathFile())
                .created_at(reportDTO.getCreated_at())
                .condominium(condominium)
                .build();
    }

    private ReportDTO mapEntityToDTO(Report report) {
        return ReportDTO.builder()
                .id(report.getId())
                .pathFile(report.getPath_file())
                .created_at(report.getCreated_at())
                .build();
    }
}
