package com.ctlfab.condomini.service;

import java.sql.Timestamp;


public interface MyUtils {
    Timestamp startDateTime();
    Timestamp endDateTime();

    Timestamp startDateTime(int year);
    Timestamp endDateTime(int year);
}
