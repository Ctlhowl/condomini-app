package com.ctlfab.condomini.job;

import com.ctlfab.condomini.dto.ApartmentDTO;
import com.ctlfab.condomini.dto.CondominiumDTO;
import com.ctlfab.condomini.repository.ApartmentRepository;
import com.ctlfab.condomini.repository.CondominiumRepository;
import com.ctlfab.condomini.service.ApartmentService;
import com.ctlfab.condomini.service.CondominiumService;
import com.ctlfab.condomini.service.OutlayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Year;


@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class NewYear {
    private final CondominiumService condominiumService;
    private final ApartmentRepository apartmentRepository;
    private final OutlayService outlayService;
    private final CondominiumRepository condominiumRepository;
    private final ApartmentService apartmentService;

    @Scheduled(cron = "0 50 23 31 12 *")
    public void setNewLastYearBalance(){
        for(CondominiumDTO condominiumDTO : condominiumService.findAllCondominiums()){
            Float newCondominiumLastYearBalance = condominiumDTO.getLastYearBalance();

            for(ApartmentDTO apartmentDTO : condominiumDTO.getApartments()){
                Float totalOutlay = outlayService.totalAmountByApartmentId(apartmentDTO.getId(), Year.now().getValue());
                if(totalOutlay == null){
                    totalOutlay = 0f;
                }

                float newApartmentLastYearBalance = apartmentDTO.getLastYearBalance() + totalOutlay;
                newCondominiumLastYearBalance += newApartmentLastYearBalance;
                apartmentDTO.setLastYearBalance(newApartmentLastYearBalance);

                apartmentService.updateApartment(apartmentDTO, condominiumDTO.getId());
            }

            condominiumDTO.setLastYearBalance(newCondominiumLastYearBalance);
            condominiumService.updateCondominium(condominiumDTO);
        }
    }
}
