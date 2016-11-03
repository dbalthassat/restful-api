package com.dbalthassat.restapi.mapper;

import java.util.function.Function;

public class Converter<IN, OUT> {
    private final Class<IN> inClass;
    private final Class<OUT> outClass;
    private final Function<IN, OUT> converter;

    public Converter(Class<IN> inClass, Class<OUT> outClass, Function<IN, OUT> converter) {
        this.inClass = inClass;
        this.outClass = outClass;
        this.converter = converter;
    }

    public Class<IN> getInClass() {
        return inClass;
    }

    public Class<OUT> getOutClass() {
        return outClass;
    }

    public OUT convert(IN obj) {
        return converter.apply(obj);
    }
}
