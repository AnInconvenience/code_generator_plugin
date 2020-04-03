package com.perso;

import com.sun.jdi.CharValue;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.Expression;

import java.util.List;

public class JdiCharResolver extends AbstractJdiPrimitiveTypeResolver<Character, CharValue> {

    @Override
    public Character readValue(Class<? extends Character> clazz, CharValue jdiValue) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        return jdiValue.value();
    }

    @Override
    public Expression writeExpression(Character object, AST ast, List accumulatedStatements) {
        CharacterLiteral cl = ast.newCharacterLiteral();
        cl.setCharValue(object);
        return cl;
    }
}
