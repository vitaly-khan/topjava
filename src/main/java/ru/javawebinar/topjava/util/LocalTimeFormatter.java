package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalTimeFormatter implements Formatter<LocalTime> {
    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        return LocalTime.parse(text, DateTimeFormatter.ofPattern("HH-mm"));
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        return object.toString();
    }
}
