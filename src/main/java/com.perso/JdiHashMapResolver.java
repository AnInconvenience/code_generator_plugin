package com.perso;

import com.sun.jdi.ArrayReference;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.Value;
import org.eclipse.jdt.core.dom.*;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdiHashMapResolver extends AbstractJdiObjectResolver<Map, ObjectReference> {

    @Override
    public Map readValue(Class<? extends Map> clazz, ObjectReference objectRef) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        ArrayReference arr = (ArrayReference) objectRef.getValue(objectRef.referenceType().fieldByName("table"));
        Map map = new HashMap();
        for (Value node : arr.getValues()) {
            if (node == null) continue;
            ObjectReference nodeObj = (ObjectReference)node;
            Value mapKey = nodeObj.getValue(nodeObj.referenceType().fieldByName("key"));
            Class keyClass = getClassFromName(mapKey.type().name());
            JdiValueResolver keyResolver = ResolverFactory.getResolverByClass(keyClass, classLoader);

            Value mapValue = nodeObj.getValue(nodeObj.referenceType().fieldByName("value"));
            Class valueClass = getClassFromName(mapValue.type().name());
            JdiValueResolver valueResolver = ResolverFactory.getResolverByClass(valueClass, classLoader);

            map.put(keyResolver.readValue(keyClass, mapKey), valueResolver.readValue(valueClass, mapValue));
        }
        return map;
    }

    @Override
    public Expression writeExpression(Map object, AST ast, List statements) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        SimpleName mapVarName = appendObjectInstanciationStatement(object, ast, statements);
        for(Object entry : object.entrySet()) {
            Map.Entry mapEntry = (Map.Entry)entry;
            Object key = mapEntry.getKey();
            JdiValueResolver keyJdiValueResolver = ResolverFactory.getResolverByClass(key.getClass(), classLoader);
            Object value = mapEntry.getValue();
            JdiValueResolver valueJdiValueResolver = ResolverFactory.getResolverByClass(value.getClass(), classLoader);

            MethodInvocation putInvocation = ast.newMethodInvocation();
            SimpleName putSimpleName = ast.newSimpleName("put");
            putInvocation.setName(putSimpleName);
            putInvocation.arguments().add(keyJdiValueResolver.writeExpression(key, ast, statements));
            putInvocation.arguments().add(valueJdiValueResolver.writeExpression(value, ast, statements));

            putInvocation.setExpression(ast.newSimpleName(mapVarName.getIdentifier()));
            ExpressionStatement putStatement = ast.newExpressionStatement(putInvocation);
            statements.add(putStatement);
        }
        return ast.newSimpleName(mapVarName.getIdentifier());
    }
}
