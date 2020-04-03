package com.perso;

import com.intellij.util.lang.UrlClassLoader;
import com.sun.jdi.LongValue;
import com.sun.jdi.ObjectReference;

import java.util.Date;

public class JdiDateResolver extends AbstractJdiObjectResolver<Date, ObjectReference> {

    public JdiDateResolver(UrlClassLoader loader) {
        super(loader);
    }

    @Override
    public Date readValue(Class<? extends Date> clazz, ObjectReference objectRef) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        LongValue fastTimeVal = (LongValue) objectRef.getValue(objectRef.referenceType().fieldByName("fastTime"));
        return new Date(fastTimeVal.value());
    }

}
