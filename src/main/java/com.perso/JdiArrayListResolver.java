package com.perso;

import com.intellij.util.lang.UrlClassLoader;
import com.sun.jdi.ArrayReference;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.Value;
import org.eclipse.jdt.core.dom.*;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class JdiArrayListResolver extends AbstractJdiObjectResolver<List, ObjectReference>{

    @Override
    public List readValue(Class<? extends List> clazz, ObjectReference objectRef) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        ArrayReference arrayVal = (ArrayReference) objectRef.getValue(objectRef.referenceType().fieldByName("elementData"));
        List list = new ArrayList<>();
        for (Value v : arrayVal.getValues()) {
            if (v == null) continue;
            Class arrayItemClass = getClassFromName(v.type().name());
            JdiValueResolver jvr = ResolverFactory.getResolverByClass(arrayItemClass, loader);
            list.add(jvr.readValue(arrayItemClass, v));
        }
        return list;
    }

    @Override
    public Expression writeExpression(List objects, AST ast, List statements) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        SimpleName arrayVariableName = appendObjectInstanciationStatement(objects, ast, statements);
        for (Object o : objects) {
            if (o == null) continue;
            MethodInvocation addInvocation = ast.newMethodInvocation();
            SimpleName addSimpleName = ast.newSimpleName("add");
            addInvocation.setName(addSimpleName);

            JdiValueResolver jdiValueResolver = ResolverFactory.getResolverByClass(o.getClass(), loader);
            addInvocation.arguments().add(jdiValueResolver.writeExpression(o, ast, statements));

            SimpleName arraySimpleName = ast.newSimpleName(arrayVariableName.getIdentifier());
            addInvocation.setExpression(arraySimpleName);
            ExpressionStatement addStatement = ast.newExpressionStatement(addInvocation);
            statements.add(addStatement);
        }
        return ast.newSimpleName(arrayVariableName.getIdentifier());
    }
}
