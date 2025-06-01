package ru.ifmo.lab5.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Адаптер для преобразования ZonedDateTime в строку и обратно для JAXB.
 */
public class ZonedDateTimeAdapter extends XmlAdapter<String, ZonedDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    @Override
    public ZonedDateTime unmarshal(String v) throws Exception {
        if (v == null) {
            return null;
        }
        return ZonedDateTime.parse(v, formatter);
    }

    @Override
    public String marshal(ZonedDateTime v) throws Exception {
        if (v == null) {
            return null;
        }
        return v.format(formatter);
    }
}