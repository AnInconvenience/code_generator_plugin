package com.perso;

import com.sun.jdi.Value;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface JdiValueResolver<T,V extends Value> {
    T readValue(Class<? extends T> clazz, V jdiValue) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException;

    Expression writeExpression(T object, AST ast, List accumulatedStatements) throws IntrospectionException, InvocationTargetException, IllegalAccessException;
}
