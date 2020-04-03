package com.perso;

import com.sun.jdi.DoubleValue;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;

import java.util.List;

public class JdiDoubleResolver extends AbstractJdiPrimitiveTypeResolver<Double, DoubleValue> {

    @Override
    public Double readValue(Class<? extends Double> clazz, DoubleValue jdiValue) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        return jdiValue.value();
    }

    @Override
    public Expression writeExpression(Double object, AST ast, List accumulatedStatements) {
        String objectAsString = String.valueOf(object);
        objectAsString += "D";
        return ast.newNumberLiteral(objectAsString);
    }
}
