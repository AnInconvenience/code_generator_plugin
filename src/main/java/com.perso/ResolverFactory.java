package com.perso;

import com.intellij.util.lang.UrlClassLoader;

public class ResolverFactory {

    //final UrlClassLoader urlClassLoader;
    static JdiLongResolver _jdiLongResolver = null;
    static JdiCharResolver _jdiCharResolver = null;
    static JdiShortResolver _jdiShortResolver = null;
    static JdiIntegerResolver _jdiIntegerResolver = null;
    static JdiFloatResolver _jdiFloatResolver = null;
    static JdiByteResolver _jdiByteResolver = null;
    static JdiDoubleResolver _jdiDoubleResolver = null;
    static JdiStringResolver _jdiStringResolver = null;
    static JdiDateResolver _jdiDateResolver = null;
    static JdiBooleanResolver _jdiBooleanResolver = null;
    static JdiArrayListResolver _jdiListResolver = null;
    static JdiHashMapResolver _jdiMapResolver = null;

    public static JdiValueResolver getResolverByClass(Class clazz, UrlClassLoader loader) {
        ClassEnum javaBasicClass = ClassEnum.getByClass(clazz);
        if (javaBasicClass != null) {
            switch (javaBasicClass) {
                case LONG:
                case LONG_PRIMITIVE:
                    return getJdiLongResolver();
                case CHAR:
                case CHAR_PRIMITIVE:
                    return getJdiCharResolver();
                case BYTE:
                case BYTE_PRIMITIVE:
                    return getJdiByteResolver();
                case SHORT:
                case SHORT_PRIMITIVE:
                    return getJdiShortResolver();
                case INTEGER:
                case INTEGER_PRIMITIVE:
                    return getJdiIntegerResolver();
                case FLOAT:
                case FLOAT_PRIMITIVE:
                    return getJdiFloatResolver();
                case DOUBLE:
                case DOUBLE_PRIMITIVE:
                    return getJdiDoubleResolver();
                case BOOLEAN:
                case BOOLEAN_PRIMITIVE:
                    return getJdiBooleanResolver();
                case LIST:
                case ARRAY_LIST:
                    return getJdiListResolver(loader);
                case MAP:
                case HASH_MAP:
                    return getJdiMapResolver(loader);
                case STRING:
                    return getJdiStringResolver();
                case DATE:
                    return getJdiDateResolver(loader);
            }
        }
        return new JdiObjectResolverImpl(loader);
    }

    public static JdiLongResolver getJdiLongResolver() {
        if (_jdiLongResolver == null) {
            _jdiLongResolver = new JdiLongResolver();
        }
        return _jdiLongResolver;
    }

    public static JdiCharResolver getJdiCharResolver() {
        if (_jdiCharResolver == null) {
            _jdiCharResolver = new JdiCharResolver();
        }
        return _jdiCharResolver;
    }

    public static JdiShortResolver getJdiShortResolver() {
        if (_jdiShortResolver == null) {
            _jdiShortResolver = new JdiShortResolver();
        }
        return _jdiShortResolver;
    }

    public static JdiIntegerResolver getJdiIntegerResolver() {
        if (_jdiIntegerResolver == null) {
            _jdiIntegerResolver = new JdiIntegerResolver();
        }
        return _jdiIntegerResolver;
    }

    public static JdiFloatResolver getJdiFloatResolver() {
        if (_jdiFloatResolver == null) {
            _jdiFloatResolver = new JdiFloatResolver();
        }
        return _jdiFloatResolver;
    }

    public static JdiDoubleResolver getJdiDoubleResolver() {
        if (_jdiDoubleResolver == null) {
            _jdiDoubleResolver = new JdiDoubleResolver();
        }
        return _jdiDoubleResolver;
    }

    public static JdiBooleanResolver getJdiBooleanResolver() {
        if (_jdiBooleanResolver == null) {
            _jdiBooleanResolver = new JdiBooleanResolver();
        }
        return _jdiBooleanResolver;
    }

    public static JdiArrayListResolver getJdiListResolver(UrlClassLoader urlClassLoader) {
        if (_jdiListResolver == null) {
            _jdiListResolver = new JdiArrayListResolver(urlClassLoader);
        }
        return _jdiListResolver;
    }

    public static JdiHashMapResolver getJdiMapResolver(UrlClassLoader urlClassLoader) {
        if (_jdiMapResolver == null) {
            _jdiMapResolver = new JdiHashMapResolver(urlClassLoader);
        }
        return _jdiMapResolver;
    }

    public static JdiByteResolver getJdiByteResolver() {
        if (_jdiByteResolver == null) {
            _jdiByteResolver = new JdiByteResolver();
        }
        return _jdiByteResolver;
    }

    public static JdiStringResolver getJdiStringResolver() {
        if (_jdiStringResolver == null) {
            _jdiStringResolver = new JdiStringResolver();
        }
        return _jdiStringResolver;
    }

    public static JdiDateResolver getJdiDateResolver(UrlClassLoader urlClassLoader) {
        if (_jdiDateResolver == null) {
            _jdiDateResolver = new JdiDateResolver(urlClassLoader);
        }
        return _jdiDateResolver;
    }
}
