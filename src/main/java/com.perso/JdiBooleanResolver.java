package com.perso;

import com.sun.jdi.BooleanValue;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;

import java.util.List;

public class JdiBooleanResolver extends AbstractJdiPrimitiveTypeResolver<Boolean, BooleanValue> {

    @Override
    public final Boolean readValue(Class<? extends Boolean> clazz, BooleanValue jdiValue) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        return jdiValue.value();
    }

    @Override
    public Expression writeExpression(Boolean object, AST ast, List accumulatedStatements) {
        return ast.newBooleanLiteral(object);
    }

}
