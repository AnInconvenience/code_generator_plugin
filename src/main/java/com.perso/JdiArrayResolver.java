package com.perso;

import com.sun.jdi.Value;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class JdiArrayResolver<T, V extends Value> implements JdiValueResolver<T, V> {

    @Override
    public T readValue(Class<? extends T> clazz, V jdiValue) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        return null;
    }

    @Override
    public Expression writeExpression(T object, AST ast, List accumulatedStatements) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        return null;
    }
}
