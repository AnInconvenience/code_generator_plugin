package com.perso;

import com.sun.jdi.BooleanValue;

import java.util.*;

public enum ClassEnum {
    LONG(Long.class),
    LONG_PRIMITIVE(long.class),
    CHAR(char.class),
    CHAR_PRIMITIVE(Character.class),
    BYTE(Byte.class),
    BYTE_PRIMITIVE(byte.class),
    SHORT(Short.class),
    SHORT_PRIMITIVE(short.class),
    INTEGER(Integer.class),
    INTEGER_PRIMITIVE(int.class),
    FLOAT(Float.class),
    FLOAT_PRIMITIVE(float.class),
    DOUBLE(Double.class),
    DOUBLE_PRIMITIVE(double.class),
    BOOLEAN(Boolean.class),
    BOOLEAN_PRIMITIVE(boolean.class),
    LIST(List.class),
    ARRAY_LIST(ArrayList.class),
    MAP(Map.class),
    HASH_MAP(HashMap.class),
    STRING(String.class),
    DATE(Date.class);

    Class clazz;

    ClassEnum(Class clazz) {
        this.clazz = clazz;
    }

    private static final Map<Class, ClassEnum> classMap;
    private static final Map<String, ClassEnum> simpleTypes;

    static {
        Map m = new HashMap();
        for (ClassEnum classEnum : ClassEnum.values()) {
            m.put(classEnum.clazz, classEnum);
        }
        classMap = Collections.unmodifiableMap(m);


        Map map = new HashMap();
        map.put(Boolean.TYPE.getSimpleName(), BOOLEAN_PRIMITIVE);
        map.put(Byte.TYPE.getSimpleName(), BYTE_PRIMITIVE);
        map.put(Character.TYPE.getSimpleName(), CHAR_PRIMITIVE);
        map.put(Double.TYPE.getSimpleName(), DOUBLE_PRIMITIVE);
        map.put(Float.TYPE.getSimpleName(), FLOAT_PRIMITIVE);
        map.put(Integer.TYPE.getSimpleName(), INTEGER_PRIMITIVE);
        map.put(Long.TYPE.getSimpleName(), LONG_PRIMITIVE);
        map.put(Short.TYPE.getSimpleName(), SHORT_PRIMITIVE);
        simpleTypes = Collections.unmodifiableMap(map);
    }

    public static ClassEnum getByClass(Class aClass) {
        return classMap.get(aClass);
    }

    public static Class getPrimitiveClass(String primitiveName) {
        ClassEnum classEnum = simpleTypes.get(primitiveName);
        if (classEnum == null) {
            return null;
        }
        return classEnum.clazz;
    }

    public Class getClassFromEnum() {
        return clazz;
    }
}
