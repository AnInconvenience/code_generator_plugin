package com.perso;

import com.sun.jdi.Value;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;

public interface JdiValueResolver<T,V extends Value> {
    Class getClassFromName(String name) throws ClassNotFoundException;

    T readValue(Class<? extends T> clazz, V jdiValue) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException;

    Expression writeExpression(T object, AST ast);
}
