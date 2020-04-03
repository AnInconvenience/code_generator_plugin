package com.perso;

import com.sun.jdi.StringReference;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.StringLiteral;

import java.util.List;

public class JdiStringResolver implements JdiSimpleTypeResolver<String, StringReference>{

    @Override
    public Class getClassFromName(String name) throws ClassNotFoundException {
        return String.class;
    }

    @Override
    public String readValue(Class<? extends String> clazz, StringReference jdiValue) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        return jdiValue.value();
    }

    @Override
    public Expression writeExpression(String object, AST ast, List accumulatedStatements) {
        StringLiteral newStringLiteral = ast.newStringLiteral();
        newStringLiteral.setLiteralValue(object);
        return newStringLiteral;
    }
}
