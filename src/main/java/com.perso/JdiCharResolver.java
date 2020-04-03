package com.perso;

import com.sun.jdi.CharValue;
import com.sun.jdi.ShortValue;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.Expression;

public class JdiCharResolver extends AbstractJdiPrimitiveTypeResolver<Character, CharValue> {

    @Override
    public Character readValue(Class<? extends Character> clazz, CharValue jdiValue) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        return jdiValue.value();
    }

    @Override
    public Expression writeExpression(Character object, AST ast) {
        CharacterLiteral cl = ast.newCharacterLiteral();
        cl.setCharValue(object);
        return cl;
    }
}
