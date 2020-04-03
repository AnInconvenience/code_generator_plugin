package com.perso;

import com.sun.jdi.PrimitiveValue;
import com.sun.jdi.Value;

public abstract class AbstractJdiPrimitiveTypeResolver <T, V extends Value> implements JdiSimpleTypeResolver<T, V> {

    @Override
    public final Class getClassFromName(String name) throws ClassNotFoundException {
        Class primitiveClass = ClassEnum.getPrimitiveClass(name);
        if (primitiveClass != null) {
            return primitiveClass;
        }
        return Class.forName(name);
    }
}
