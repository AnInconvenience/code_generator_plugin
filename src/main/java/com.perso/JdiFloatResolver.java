package com.perso;

import com.sun.jdi.FloatValue;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;

import java.util.List;

public class JdiFloatResolver extends AbstractJdiPrimitiveTypeResolver<Float, FloatValue> {

    @Override
    public Float readValue(Class<? extends Float> clazz, FloatValue jdiValue) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        return jdiValue.value();
    }

    @Override
    public Expression writeExpression(Float object, AST ast, List accumulatedStatements) {
        return ast.newNumberLiteral(String.valueOf(object));
    }
}
