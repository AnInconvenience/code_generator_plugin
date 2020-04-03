package com.perso;

import com.sun.jdi.IntegerValue;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;

import java.util.List;

public class JdiIntegerResolver extends AbstractJdiPrimitiveTypeResolver<Integer, IntegerValue> {

    @Override
    public Integer readValue(Class<? extends Integer> clazz, IntegerValue jdiValue) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        return jdiValue.value();
    }

    @Override
    public Expression writeExpression(Integer object, AST ast, List accumulatedStatements) {
        Expression s = ast.newNumberLiteral(String.valueOf(object));
        return s;
    }
}
