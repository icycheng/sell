package com.imooc.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * @author: icych
 * @description: Date -> Long
 * @date: Created on 21:24 2018/7/16
 */
public class Date2LongSerializer extends JsonSerializer<Date> {

    private Date2LongSerializer() {
    }

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, //
                          SerializerProvider serializerProvider) //
            throws IOException {

        jsonGenerator.writeNumber(date.getTime() / 1000);
    }

}
