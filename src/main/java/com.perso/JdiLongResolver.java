package com.perso;

import com.sun.jdi.LongValue;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;

public class JdiLongResolver extends AbstractJdiPrimitiveTypeResolver<Long, LongValue> {

    @Override
    public Long readValue(Class<? extends Long> clazz, LongValue jdiValue) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        return jdiValue.longValue();
    }

    @Override
    public Expression writeExpression(Long object, AST ast) {
        return ast.newNumberLiteral(String.valueOf(object));
    }


}
