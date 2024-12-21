package com.ctlfab.condomini.job;

import com.ctlfab.condomini.dto.ApartmentDTO;
import com.ctlfab.condomini.dto.CondominiumDTO;
import com.ctlfab.condomini.repository.ApartmentRepository;
import com.ctlfab.condomini.repository.CondominiumRepository;
import com.ctlfab.condomini.service.CondominiumService;
import com.ctlfab.condomini.service.OutlayService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Year;


@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Transactional
public class NewYear {
    private final CondominiumService condominiumService;
    private final ApartmentRepository apartmentRepository;
    private final OutlayService outlayService;
    private final CondominiumRepository condominiumRepository;

    @Scheduled(cron = "0 50 23 31 12 *")
    public void setNewLastYearBalance(){
        for(CondominiumDTO condominiumDTO : condominiumService.findAllCondominiums()){
            Float newCondominiumLastYearBalance = condominiumDTO.getLastYearBalance();

            for(ApartmentDTO apartmentDTO : condominiumDTO.getApartments()){
                Float totalOutlay = outlayService.totalAmountByApartmentId(apartmentDTO.getId(), Year.now().getValue());
                float newApartmentLastYearBalance = condominiumDTO.getLastYearBalance() + totalOutlay;
                newCondominiumLastYearBalance += newApartmentLastYearBalance;

                apartmentRepository.setNewLastYearBalance(apartmentDTO.getId(), newApartmentLastYearBalance);
            }

            condominiumRepository.setNewLastYearBalance(condominiumDTO.getId(), newCondominiumLastYearBalance);
        }
    }
}
