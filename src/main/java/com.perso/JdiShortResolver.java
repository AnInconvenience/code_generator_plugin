package com.perso;

import com.sun.jdi.IntegerValue;
import com.sun.jdi.ShortValue;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;

public class JdiShortResolver extends AbstractJdiPrimitiveTypeResolver<Short, ShortValue> {

    @Override
    public Short readValue(Class<? extends Short> clazz, ShortValue jdiValue) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        return jdiValue.value();
    }

    @Override
    public Expression writeExpression(Short object, AST ast) {
        return ast.newNumberLiteral(String.valueOf(object));
    }
}
