package com.perso;

import com.google.gson.internal.Primitives;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.PrimitiveValue;
import com.sun.jdi.Value;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;

public class JdiPrimitiveWrapperResolver extends AbstractJdiPrimitiveTypeResolver<Object, ObjectReference> {

    @Override
    public Object readValue(Class<?> clazz, ObjectReference jdiValue) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        PrimitiveValue primitiveUnboxedValue = (PrimitiveValue) jdiValue.getValue(jdiValue.referenceType().fieldByName("value"));
        Class primitiveClazz = Primitives.unwrap(clazz);
        JdiSimpleTypeResolver primitiveResolver = (JdiSimpleTypeResolver) ResolverFactory.getResolverByClass(primitiveClazz, null);
        return primitiveResolver.readValue(primitiveClazz, primitiveUnboxedValue);
    }

    @Override
    public Expression writeExpression(Object object, AST ast) {
        Class primitiveClazz = Primitives.unwrap(object.getClass());
        JdiSimpleTypeResolver primitiveResolver = (JdiSimpleTypeResolver) ResolverFactory.getResolverByClass(primitiveClazz, null);
        return primitiveResolver.writeExpression(object, ast);
    }

}
