package com.perso;

import com.sun.jdi.ByteValue;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;

import java.util.List;

public class JdiByteResolver extends AbstractJdiPrimitiveTypeResolver<Byte, ByteValue> {

    @Override
    public Byte readValue(Class<? extends Byte> clazz, ByteValue jdiValue) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        return jdiValue.value();
    }

    @Override
    public Expression writeExpression(Byte object, AST ast, List accumulatedStatements) {
        return ast.newNumberLiteral(object.toString());
    }
}
