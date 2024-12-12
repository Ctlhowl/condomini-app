package com.ctlfab.condomini.service.implementation;

import com.ctlfab.condomini.DTO.TableAppendixDTO;
import com.ctlfab.condomini.model.Outlay;
import com.ctlfab.condomini.model.Quote;
import com.ctlfab.condomini.model.TableAppendix;
import com.ctlfab.condomini.repository.OutlayRepository;
import com.ctlfab.condomini.repository.QuoteRepository;
import com.ctlfab.condomini.repository.ReportRepository;
import com.ctlfab.condomini.repository.TableAppendixRepository;
import com.ctlfab.condomini.service.TableAppendixService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Transactional
public class TableAppendixServiceImpl implements TableAppendixService {
    private final TableAppendixRepository tableAppendixRepository;
    private static final Logger logger = LoggerFactory.getLogger(TableAppendixServiceImpl.class);
    private final QuoteRepository quoteRepository;
    private final OutlayRepository outlayRepository;
    private final ReportRepository reportRepository;

    @Override
    public TableAppendixDTO findTableById(Long tableId) {
        logger.info("Fetching table by id {}", tableId);
        return mapEntityToDTO(tableAppendixRepository.findById(tableId).get());
    }

    @Override
    public Collection<TableAppendixDTO> findAll() {
        logger.info("Fetching all tables");
        Collection<TableAppendixDTO> tables = new LinkedList<>();

        for (TableAppendix table : tableAppendixRepository.findAll()) {
            tables.add(mapEntityToDTO(table));
        }
        return tables;
    }

    @Override
    public Float findTotalQuoteByCategory(String category) {
        return reportRepository.findTotalQuoteByCategory(category);
    }

    private TableAppendixDTO mapEntityToDTO(TableAppendix table) {
        return TableAppendixDTO.builder()
                .id(table.getId())
                .category(table.getCategory())
                .description(table.getDescription())
                .build();
    }

    private TableAppendix mapDTOToEntity(TableAppendixDTO tableDTO) {
        Quote quote = quoteRepository.findQuoteByTableId(tableDTO.getId());
        List<Outlay> outlays = outlayRepository.findAllOutlaysByTableId(tableDTO.getId()).stream().toList();

        return TableAppendix.builder()
                .id(tableDTO.getId())
                .category(tableDTO.getCategory())
                .description(tableDTO.getDescription())
                .quote(quote)
                .outlays(outlays)
                .build();
    }
}
