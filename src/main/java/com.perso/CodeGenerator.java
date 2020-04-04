package com.perso;

import com.intellij.util.lang.UrlClassLoader;
import com.sun.jdi.*;
import org.eclipse.jdt.core.dom.*;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

public class CodeGenerator {
    public String generateCode(Object object, UrlClassLoader loader) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        AST ast = AST.newAST(AST.JLS3);
        Block block = ast.newBlock();

        for (AbstractJdiObjectResolver jdiObjectResolver : ResolverFactory.getResolversWithCounters()) {
            jdiObjectResolver.resetCounter();
        }

        JdiValueResolver jdiValueResolver = ResolverFactory.getResolverByClass(object.getClass(), loader);
        jdiValueResolver.writeExpression(object, ast, block.statements());
        return block.toString();

    }

    <T> T createObjectFromReference(Class<T> clazz, ObjectReference objectRef, UrlClassLoader loader) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
       JdiValueResolver jdiVr = ResolverFactory.getResolverByClass(clazz, loader);
       return (T) jdiVr.readValue(clazz, objectRef);
    }

}
