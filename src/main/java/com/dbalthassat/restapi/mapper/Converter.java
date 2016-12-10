package com.dbalthassat.restapi.mapper;

import java.util.function.Function;

public class Converter<LEFT, RIGHT> {
    private final Class<LEFT> inClass;
    private final Class<RIGHT> outClass;
    private final Function<LEFT, RIGHT> aToB;
    private final Function<RIGHT, LEFT> bToA;

    public Converter(Class<LEFT> inClass, Class<RIGHT> outClass, Function<LEFT, RIGHT> aToB, Function<RIGHT, LEFT> bToA) {
        this.inClass = inClass;
        this.outClass = outClass;
        this.aToB = aToB;
        this.bToA = bToA;
    }

    public Class<LEFT> getInClass() {
        return inClass;
    }

    public Class<RIGHT> getOutClass() {
        return outClass;
    }

    public RIGHT convertRight(LEFT obj) {
        return aToB.apply(obj);
    }

    public LEFT convertLeft(RIGHT obj) {
        return bToA.apply(obj);
    }
}
