package com.perso;

import com.google.common.base.CaseFormat;
import com.intellij.util.lang.UrlClassLoader;
import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.Value;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;

import java.util.List;
import java.util.Map;

public abstract class AbstractJdiObjectResolver<T,V extends ObjectReference> implements JdiObjectResolver<T, V> {
    protected UrlClassLoader loader;

    protected static int counter = 0;

    protected String createVariableName(String clazzName) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, clazzName + counter++);
    }

    public AbstractJdiObjectResolver(UrlClassLoader loader) {
        this.loader = loader;
    }

    @Override
    public T readValue(Class<? extends T> clazz, V objectRef) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        T result = newInstance(clazz);
        Map<Field, Value> valueMap = objectRef.getValues(objectRef.referenceType().fields());
        for (Map.Entry<Field, Value> entry : valueMap.entrySet()) {
            Field field = entry.getKey();
            Value value = entry.getValue();
            Class fieldClass = getClassFromName(field.typeName());
            JdiValueResolver jdiValueResolver =  ResolverFactory.getResolverByClass(fieldClass, loader);
            FieldUtils.writeField(clazz.getDeclaredField(field.name()), result, jdiValueResolver.readValue(fieldClass, value), true);
        }
        return result;
    }

    @Override
    public Expression writeExpression(T object, AST ast, List accumulatedStatements) {

        return null;
    }

    protected T newInstance(Class<? extends T> tClass) throws IllegalAccessException, InstantiationException {
        return tClass.newInstance();
    }

    @Override
    public final Class getClassFromName(String name) throws ClassNotFoundException {
        Class fieldClass = null;
        try {
            fieldClass = ClassEnum.getPrimitiveClass(name);
            if (fieldClass == null) {
                fieldClass = Class.forName(name);
            }
        } catch (ClassNotFoundException e) {
            fieldClass = Class.forName(name, true, loader);
        }
        return fieldClass;
    }
}
