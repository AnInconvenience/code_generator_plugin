package com.perso;

import java.util.*;

public enum ClassEnum {
    LONG(Long.class),
    LONG_PRIMITIVE(long.class),
    CHAR(Character.class),
    CHAR_PRIMITIVE(char.class),
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

    private static final Map<Class, ClassEnum> reverseLookupMap;
    private static final Map<String, ClassEnum> primitiveTypes;

    static {
        Map m = new HashMap();
        for (ClassEnum classEnum : ClassEnum.values()) {
            m.put(classEnum.clazz, classEnum);
        }
        reverseLookupMap = Collections.unmodifiableMap(m);


        Map map = new HashMap();
        map.put(Boolean.TYPE.getSimpleName(), BOOLEAN_PRIMITIVE);
        map.put(Byte.TYPE.getSimpleName(), BYTE_PRIMITIVE);
        map.put(Character.TYPE.getSimpleName(), CHAR_PRIMITIVE);
        map.put(Double.TYPE.getSimpleName(), DOUBLE_PRIMITIVE);
        map.put(Float.TYPE.getSimpleName(), FLOAT_PRIMITIVE);
        map.put(Integer.TYPE.getSimpleName(), INTEGER_PRIMITIVE);
        map.put(Long.TYPE.getSimpleName(), LONG_PRIMITIVE);
        map.put(Short.TYPE.getSimpleName(), SHORT_PRIMITIVE);
        primitiveTypes = Collections.unmodifiableMap(map);
    }

    public static ClassEnum getByClass(Class aClass) {
        return reverseLookupMap.get(aClass);
    }

    public static Class getPrimitiveClass(String primitiveName) {
        ClassEnum classEnum = primitiveTypes.get(primitiveName);
        if (classEnum == null) {
            return null;
        }
        return classEnum.clazz;
    }

    public Class getClassFromEnum() {
        return clazz;
    }
}
