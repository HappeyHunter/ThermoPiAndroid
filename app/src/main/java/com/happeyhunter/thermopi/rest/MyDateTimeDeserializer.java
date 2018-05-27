package com.happeyhunter.thermopi.rest;

import com.fasterxml.jackson.datatype.joda.cfg.FormatConfig;
import com.fasterxml.jackson.datatype.joda.cfg.JacksonJodaDateFormat;
import com.fasterxml.jackson.datatype.joda.deser.DateTimeDeserializer;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Created by david on 27/05/18.
 */

public class MyDateTimeDeserializer extends DateTimeDeserializer {
    public MyDateTimeDeserializer() {
        super(DateTime.class, FormatConfig.DEFAULT_DATETIME_PARSER);
    }

    public MyDateTimeDeserializer(Class<?> cls, JacksonJodaDateFormat format) {
        super(cls, format);
    }
}
