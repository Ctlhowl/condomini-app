package com.ctlfab.condomini.service.implementation;

import com.ctlfab.condomini.service.MyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class MyUtilsImp implements MyUtils {
    public Timestamp startDateTime(){
        LocalDate startDate = LocalDate.of(Year.now().getValue(), 1, 1);
        long startDateTime = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        return new Timestamp(startDateTime);
    }

    public Timestamp endDateTime(){
        LocalDate endDate = LocalDate.of(Year.now().getValue(), 12, 31);
        long endDateTimestamp = endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        return new Timestamp(endDateTimestamp);
    }

    public Timestamp startDateTime(int year){
        LocalDate startDate = LocalDate.of(year, 1, 1);
        long startDateTime = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        return new Timestamp(startDateTime);
    }

    public Timestamp endDateTime(int year){
        LocalDate endDate = LocalDate.of(year, 12, 31);
        long endDateTimestamp = endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        return new Timestamp(endDateTimestamp);
    }
}
