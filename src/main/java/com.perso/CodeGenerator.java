package com.perso;

import com.google.common.base.CaseFormat;
import com.google.common.primitives.Primitives;
import com.intellij.util.lang.UrlClassLoader;
import com.sun.jdi.*;
import com.sun.tools.jdi.ObjectReferenceImpl;
import com.sun.tools.jdi.StringReferenceImpl;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.eclipse.jdt.core.dom.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CodeGenerator {

    int counter = 0;

    private String createVariableName(String clazzName) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, clazzName + getCurrentCounter());
    }

    public String generateCode(AST ast, List statements, Object object) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String clazzName = object.getClass().getSimpleName();
        String variableName = createVariableName(clazzName);

        VariableDeclarationFragment newVariable = ast.newVariableDeclarationFragment();
        newVariable.setName(ast.newSimpleName(variableName)); // Or clazzName.toCamelCase()

        ClassInstanceCreation newInstance = ast.newClassInstanceCreation();
        newInstance.setType(ast.newSimpleType(ast.newSimpleName(clazzName)));
        newVariable.setInitializer(newInstance);

        VariableDeclarationStatement newObjectStatement = ast.newVariableDeclarationStatement(newVariable);
        newObjectStatement.setType(ast.newSimpleType(ast.newSimpleName(clazzName)));

        statements.add(newObjectStatement);

        BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
        for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
            String propertyName = propertyDesc.getName();


            if (!shouldIgnore(propertyName) && propertyDesc.getWriteMethod() != null && propertyDesc.getReadMethod() != null) {

                if (object.getClass() == Date.class && !propertyDesc.getWriteMethod().getName().equals("setTime")) {
                    continue;
                }

                MethodInvocation setterInvocation = ast.newMethodInvocation();

                SimpleName setterName = ast.newSimpleName(propertyDesc.getWriteMethod().getName());
                setterInvocation.setName(setterName);

                Object invoked = propertyDesc.getReadMethod().invoke(object);

                if (invoked == null) {
                    continue;
                }

                if (Primitives.isWrapperType(invoked.getClass())) {
                    String variableValue = invoked.toString();
                    if (Number.class.isAssignableFrom(invoked.getClass())) {
                        if (Long.class.isAssignableFrom(invoked.getClass())) {
                            variableValue += "L";
                        }
                        else if (Double.class.isAssignableFrom(invoked.getClass())){
                            variableValue += "D";
                        }
                        setterInvocation.arguments().add(ast.newNumberLiteral(variableValue));
                    } else if (Boolean.class.isAssignableFrom(invoked.getClass())) {
                        setterInvocation.arguments().add(ast.newBooleanLiteral(Boolean.parseBoolean(invoked.toString())));
                    }

                }  else{

                    if (invoked instanceof String) {
                        StringLiteral newStringLiteral = ast.newStringLiteral();
                        newStringLiteral.setLiteralValue(invoked.toString());
                        setterInvocation.arguments().add(newStringLiteral);
                    }
                    else if (invoked instanceof List) {
                        String arrayVariableName = generateArrayCode(ast, statements, (List) invoked);
                        //apparently we cant have the same SimpleName
                        SimpleName arraySimpleName = ast.newSimpleName(arrayVariableName);
                        setterInvocation.arguments().add(arraySimpleName);
                    }
                    else {
                        String newObjectVariable = generateCode(ast, statements, invoked);
                        SimpleName newSimpleName = ast.newSimpleName(newObjectVariable);
                        setterInvocation.arguments().add(newSimpleName);
                    }

                }

                SimpleName newSimpleName = ast.newSimpleName(variableName);
                setterInvocation.setExpression(newSimpleName);

                ExpressionStatement setterStatement = ast.newExpressionStatement(setterInvocation);

                statements.add(setterStatement);

            }

        }

        return variableName;
    }

    public String generateArrayCode(AST ast, List statements, List invoked) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        String arrayVariableName = generateCode(ast, statements, invoked);
        for (Object o : (List)invoked) {
            String arrayElementVariableName = generateCode(ast, statements, o);
            SimpleName arrayElementSimpleName = ast.newSimpleName(arrayElementVariableName);
            MethodInvocation addInvocation = ast.newMethodInvocation();
            SimpleName addSimpleName = ast.newSimpleName("add");
            addInvocation.setName(addSimpleName);
            addInvocation.arguments().add(arrayElementSimpleName);
            SimpleName arraySimpleName = ast.newSimpleName(arrayVariableName);
            addInvocation.setExpression(arraySimpleName);
            ExpressionStatement addStatement = ast.newExpressionStatement(addInvocation);
            statements.add(addStatement);
        }
        return arrayVariableName;
    }

    public String generateCode(Object object) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        resetCounter();

        AST ast = AST.newAST(AST.JLS3);
        Block block = ast.newBlock();

        if (object instanceof List) {
            generateArrayCode(ast, block.statements(), (List) object);
        } else {
            generateCode(ast, block.statements(), object);
        }

        return block.toString();

    }


    private int getCurrentCounter() {
        return counter++;
    }

    private void resetCounter() {
        counter = 0;
    }

    private boolean shouldIgnore(String propertyName) {
        return "class".equals(propertyName);
    }

    <T> T createObjectFromReference(Class<T> clazz, ObjectReference objectRef, UrlClassLoader loader) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
       JdiValueResolver jdiVr = ResolverFactory.getResolverByClass(clazz, loader);
       return (T) jdiVr.readValue(clazz, objectRef);
    }

    <T> void setField(T target, java.lang.reflect.Field field, Value valueRef) throws IllegalAccessException {
        if (valueRef instanceof BooleanValue) {
            FieldUtils.writeField(field, target, Boolean.parseBoolean(valueRef.toString()), true);
        } else if (valueRef instanceof StringReferenceImpl) {
            FieldUtils.writeField(field, target, ((StringReferenceImpl) valueRef).value(), true);
        }
        else if (valueRef instanceof PrimitiveValue) {
            Object valueToSet = null;
            if (valueRef instanceof DoubleValue) {
                valueToSet = ((DoubleValue) valueRef).value();
            } else if (valueRef instanceof LongValue) {
                valueToSet = ((LongValue) valueRef).value();
            } else if (valueRef instanceof FloatValue) {
                valueToSet = ((FloatValue) valueRef).value();
            }
            else if (valueRef instanceof IntegerValue) {
                valueToSet = ((IntegerValue) valueRef).value();
            }else if (valueRef instanceof ShortValue) {
                valueToSet = ((ShortValue) valueRef).value();
            }
            //all number classes
            FieldUtils.writeField(field, target, valueToSet, true);
        }
    }

    Object createObjectFromReference(PrimitiveValue primitiveValue) {
        Object returnObject = null;
        return returnObject;
    }

    String createObjectFromReference(StringReference stringReference) {
        return null;
    }

    private Class getModuleClass(String className, UrlClassLoader loader) throws ClassNotFoundException {
        Class fieldClass = null;
        try {
            fieldClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            fieldClass = Class.forName(className, true, loader);
        }
        return fieldClass;
    }
}
