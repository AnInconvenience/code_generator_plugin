package com.perso;

import com.intellij.util.lang.UrlClassLoader;
import com.sun.jdi.LongValue;
import com.sun.jdi.ObjectReference;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class JdiDateResolver extends AbstractJdiObjectResolver<Date, ObjectReference> {

    @Override
    public Date readValue(Class<? extends Date> clazz, ObjectReference objectRef) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        LongValue fastTimeVal = (LongValue) objectRef.getValue(objectRef.referenceType().fieldByName("fastTime"));
        return new Date(fastTimeVal.value());
    }

    @Override
    public Expression writeExpression(Date object, AST ast, List statements) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        SimpleName dateInstanciation = appendObjectInstanciationStatement(object, ast, statements);
        BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
        PropertyDescriptor setTimePropDesc = Arrays.stream(beanInfo.getPropertyDescriptors())
                .filter(propertyDescriptor -> propertyDescriptor.getWriteMethod() != null && propertyDescriptor.getWriteMethod().getName().equals("setTime"))
                .findFirst()
                .orElse(null);
        appendSetterStatement(dateInstanciation.getIdentifier(), setTimePropDesc , object, ast, statements);
        return ast.newSimpleName(dateInstanciation.getIdentifier());
    }
}
