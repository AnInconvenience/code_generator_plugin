package com.perso;

import com.google.common.base.CaseFormat;
import com.intellij.util.lang.UrlClassLoader;
import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.Value;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.eclipse.jdt.core.dom.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public abstract class AbstractJdiObjectResolver<T,V extends ObjectReference> implements JdiObjectResolver<T, V> {
    protected UrlClassLoader classLoader;

    public final void setClassLoader(UrlClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public final void resetCounter() {
        this.counter = 0;
    }

    protected int counter = 0;

    protected String createVariableName(String clazzName) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, clazzName + ++counter);
    }

    @Override
    public T readValue(Class<? extends T> clazz, V objectRef) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        T result = newInstance(clazz);
        Map<Field, Value> valueMap = objectRef.getValues(objectRef.referenceType().fields());
        for (Map.Entry<Field, Value> entry : valueMap.entrySet()) {
            Field field = entry.getKey();
            Value value = entry.getValue();
            if (value == null) continue;
            Class fieldClass = getClassFromName(field.typeName());
            JdiValueResolver jdiValueResolver =  ResolverFactory.getResolverByClass(fieldClass, classLoader);
            FieldUtils.writeField(clazz.getDeclaredField(field.name()), result, jdiValueResolver.readValue(fieldClass, value), true);
        }
        return result;
    }

    @Override
    public Expression writeExpression(T object, AST ast, List statements) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        SimpleName objectVarName = appendObjectInstanciationStatement(object, ast, statements);
        BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
        for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
            String propertyName = propertyDesc.getName();
            if (!shouldIgnore(propertyName) && propertyDesc.getWriteMethod() != null && propertyDesc.getReadMethod() != null) {
                appendSetterStatement(objectVarName.getIdentifier(), propertyDesc, object, ast, statements);

            }

        }

        return ast.newSimpleName(objectVarName.getIdentifier());
    }

    protected SimpleName appendObjectInstanciationStatement(T object, AST ast, List statements) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        String clazzNameString = object.getClass().getSimpleName();
        String variableNameString = createVariableName(clazzNameString);
        SimpleName varName = ast.newSimpleName(variableNameString);

        VariableDeclarationFragment newVariable = ast.newVariableDeclarationFragment();
        newVariable.setName(varName); // Or clazzName.toCamelCase()

        ClassInstanceCreation newInstance = ast.newClassInstanceCreation();
        newInstance.setType(ast.newSimpleType(ast.newSimpleName(clazzNameString)));
        newVariable.setInitializer(newInstance);

        VariableDeclarationStatement newObjectStatement = ast.newVariableDeclarationStatement(newVariable);
        newObjectStatement.setType(ast.newSimpleType(ast.newSimpleName(clazzNameString)));

        statements.add(newObjectStatement);
        return varName;
    }

    protected final void appendSetterStatement(String callerVariableName, PropertyDescriptor propertyDesc, T caller, AST ast, List statements) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        MethodInvocation setterInvocation = ast.newMethodInvocation();

        SimpleName setterName = ast.newSimpleName(propertyDesc.getWriteMethod().getName());
        setterInvocation.setName(setterName);

        Object invoked = propertyDesc.getReadMethod().invoke(caller);

        if (invoked == null) {
            return;
        }

        JdiValueResolver jdiValueResolver = ResolverFactory.getResolverByClass(invoked.getClass(), classLoader);
        setterInvocation.arguments().add(jdiValueResolver.writeExpression(invoked, ast, statements));

        SimpleName newSimpleName = ast.newSimpleName(callerVariableName);
        setterInvocation.setExpression(newSimpleName);

        ExpressionStatement setterStatement = ast.newExpressionStatement(setterInvocation);

        statements.add(setterStatement);
    }

    private boolean shouldIgnore(String propertyName) {
        return "class".equals(propertyName);
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
            fieldClass = Class.forName(name, true, classLoader);
        }
        return fieldClass;
    }
}
