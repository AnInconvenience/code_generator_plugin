package com.perso;

import com.intellij.util.lang.UrlClassLoader;
import com.sun.jdi.ArrayReference;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.Value;

import java.util.HashMap;
import java.util.Map;

public class JdiHashMapResolver extends AbstractJdiObjectResolver<Map, ObjectReference> {

    public JdiHashMapResolver(UrlClassLoader loader) {
        super(loader);
    }

    @Override
    public Map readValue(Class<? extends Map> clazz, ObjectReference objectRef) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        ArrayReference arr = (ArrayReference) objectRef.getValue(objectRef.referenceType().fieldByName("table"));
        Map map = new HashMap();
        for (Value node : arr.getValues()) {
            if (node == null) continue;
            ObjectReference nodeObj = (ObjectReference)node;
            Value mapKey = nodeObj.getValue(nodeObj.referenceType().fieldByName("key"));
            Class keyClass = getClassFromName(mapKey.type().name());
            JdiValueResolver keyResolver = ResolverFactory.getResolverByClass(keyClass, loader);

            Value mapValue = nodeObj.getValue(nodeObj.referenceType().fieldByName("value"));
            Class valueClass = getClassFromName(mapValue.type().name());
            JdiValueResolver valueResolver = ResolverFactory.getResolverByClass(valueClass, loader);

            map.put(keyResolver.readValue(keyClass, mapKey), valueResolver.readValue(valueClass, mapValue));
        }
        return map;
    }
}
