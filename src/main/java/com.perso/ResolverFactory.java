package com.perso;

import com.intellij.util.lang.UrlClassLoader;

import java.util.Arrays;
import java.util.List;

public class ResolverFactory {

    //final UrlClassLoader urlClassLoader;
    private static JdiLongResolver _jdiLongResolver = null;
    private static JdiCharResolver _jdiCharResolver = null;
    private static JdiShortResolver _jdiShortResolver = null;
    private static JdiIntegerResolver _jdiIntegerResolver = null;
    private static JdiFloatResolver _jdiFloatResolver = null;
    private static JdiByteResolver _jdiByteResolver = null;
    private static JdiDoubleResolver _jdiDoubleResolver = null;
    private static JdiStringResolver _jdiStringResolver = null;
    private static JdiDateResolver _jdiDateResolver = null;
    private static JdiBooleanResolver _jdiBooleanResolver = null;
    private static JdiArrayListResolver _jdiListResolver = null;
    private static JdiHashMapResolver _jdiMapResolver = null;
    private static JdiObjectResolverImpl _jdiObjectResolverImpl = null;
    private static JdiPrimitiveWrapperResolver _jdiPrimitiveWrapperResolver = null;

    public static List<AbstractJdiObjectResolver> getResolversWithCounters() {
        return Arrays.asList(
                getJdiDateResolver(null),
                getJdiListResolver(null),
                getJdiMapResolver(null),
                getJdiObjectResolverImpl(null)
        );
    }
    public static JdiValueResolver getResolverByClass(Class clazz, UrlClassLoader loader) {
        ClassEnum javaBasicClass = ClassEnum.getByClass(clazz);
        if (javaBasicClass != null) {
            switch (javaBasicClass) {
                case BOOLEAN:
                case BYTE:
                case CHAR:
                case DOUBLE:
                case FLOAT:
                case INTEGER:
                case LONG:
                case SHORT:
                    return getJdiPrimitiveWrapperResolver();
                case BOOLEAN_PRIMITIVE:
                    return getJdiBooleanResolver();
                case BYTE_PRIMITIVE:
                    return getJdiByteResolver();
                case CHAR_PRIMITIVE:
                    return getJdiCharResolver();
                case DOUBLE_PRIMITIVE:
                    return getJdiDoubleResolver();
                case FLOAT_PRIMITIVE:
                    return getJdiFloatResolver();
                case INTEGER_PRIMITIVE:
                    return getJdiIntegerResolver();
                case LONG_PRIMITIVE:
                    return getJdiLongResolver();
                case SHORT_PRIMITIVE:
                    return getJdiShortResolver();
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
        return getJdiObjectResolverImpl(loader);
    }

    private final static JdiLongResolver getJdiLongResolver() {
        if (_jdiLongResolver == null) {
            _jdiLongResolver = new JdiLongResolver();
        }
        return _jdiLongResolver;
    }

    private final static JdiCharResolver getJdiCharResolver() {
        if (_jdiCharResolver == null) {
            _jdiCharResolver = new JdiCharResolver();
        }
        return _jdiCharResolver;
    }

    private final static JdiShortResolver getJdiShortResolver() {
        if (_jdiShortResolver == null) {
            _jdiShortResolver = new JdiShortResolver();
        }
        return _jdiShortResolver;
    }

    private final static JdiIntegerResolver getJdiIntegerResolver() {
        if (_jdiIntegerResolver == null) {
            _jdiIntegerResolver = new JdiIntegerResolver();
        }
        return _jdiIntegerResolver;
    }

    private final static JdiFloatResolver getJdiFloatResolver() {
        if (_jdiFloatResolver == null) {
            _jdiFloatResolver = new JdiFloatResolver();
        }
        return _jdiFloatResolver;
    }

    private final static JdiDoubleResolver getJdiDoubleResolver() {
        if (_jdiDoubleResolver == null) {
            _jdiDoubleResolver = new JdiDoubleResolver();
        }
        return _jdiDoubleResolver;
    }

    private final static JdiBooleanResolver getJdiBooleanResolver() {
        if (_jdiBooleanResolver == null) {
            _jdiBooleanResolver = new JdiBooleanResolver();
        }
        return _jdiBooleanResolver;
    }

    private final static JdiArrayListResolver getJdiListResolver(UrlClassLoader urlClassLoader) {
        if (_jdiListResolver == null) {
            _jdiListResolver = new JdiArrayListResolver();
        }
        if (urlClassLoader != null)
        _jdiListResolver.setClassLoader(urlClassLoader);
        return _jdiListResolver;
    }

    private final static JdiHashMapResolver getJdiMapResolver(UrlClassLoader urlClassLoader) {
        if (_jdiMapResolver == null) {
            _jdiMapResolver = new JdiHashMapResolver();
        }
        _jdiMapResolver.setClassLoader(urlClassLoader);
        return _jdiMapResolver;
    }

    private final static JdiByteResolver getJdiByteResolver() {
        if (_jdiByteResolver == null) {
            _jdiByteResolver = new JdiByteResolver();
        }
        return _jdiByteResolver;
    }

    private final static JdiStringResolver getJdiStringResolver() {
        if (_jdiStringResolver == null) {
            _jdiStringResolver = new JdiStringResolver();
        }
        return _jdiStringResolver;
    }

    private final static JdiDateResolver getJdiDateResolver(UrlClassLoader urlClassLoader) {
        if (_jdiDateResolver == null) {
            _jdiDateResolver = new JdiDateResolver();
        }
        _jdiDateResolver.setClassLoader(urlClassLoader);
        return _jdiDateResolver;
    }

    private final static JdiPrimitiveWrapperResolver getJdiPrimitiveWrapperResolver() {
        if (_jdiPrimitiveWrapperResolver == null) {
            _jdiPrimitiveWrapperResolver = new JdiPrimitiveWrapperResolver();
        }
        return _jdiPrimitiveWrapperResolver;
    }

    public static JdiObjectResolverImpl getJdiObjectResolverImpl(UrlClassLoader urlClassLoader) {
        if (_jdiObjectResolverImpl == null) {
            _jdiObjectResolverImpl = new JdiObjectResolverImpl();
        }
        _jdiObjectResolverImpl.setClassLoader(urlClassLoader);
        return _jdiObjectResolverImpl;
    }
}
