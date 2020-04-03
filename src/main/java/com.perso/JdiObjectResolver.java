package com.perso;

import com.sun.jdi.ObjectReference;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Statement;

public interface JdiObjectResolver<T, V extends ObjectReference> extends JdiValueResolver<T,V> {

}
