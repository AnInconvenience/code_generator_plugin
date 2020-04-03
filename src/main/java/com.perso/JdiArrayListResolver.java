package com.perso;

import com.intellij.util.lang.UrlClassLoader;
import com.sun.jdi.ArrayReference;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.Value;

import java.util.ArrayList;
import java.util.List;

public class JdiArrayListResolver extends AbstractJdiObjectResolver<List, ObjectReference>{

    public JdiArrayListResolver(UrlClassLoader loader) {
        super(loader);
    }

    @Override
    public List readValue(Class<? extends List> clazz, ObjectReference objectRef) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        ArrayReference arrayVal = (ArrayReference) objectRef.getValue(objectRef.referenceType().fieldByName("elementData"));
        List list = new ArrayList<>();
        for (Value v : arrayVal.getValues()) {
            if (v == null) continue;
            Class arrayItemClass = getClassFromName(v.type().name());
            JdiValueResolver jvr = ResolverFactory.getResolverByClass(arrayItemClass, loader);
            list.add(jvr.readValue(arrayItemClass, v));
        }
        return list;
    }
}
