/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.javafx.api;

import com.sun.javafx.api.tree.UnitTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskEvent.Kind;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

/**
 * Provides details about work that has been done by the JavaFX Compiler.
 */
public final class JavafxTaskEvent
{
    private TaskEvent javacEvent;
    private UnitTree unit;
    private TypeElement clazz;

    public JavafxTaskEvent(Kind kind, JavaFileObject sourceFile) {
        this.javacEvent = new TaskEvent(kind, sourceFile);
        this.unit = null;
    }

    public JavafxTaskEvent(Kind kind, CompilationUnitTree javaUnit, TypeElement clazz) {
        this.javacEvent = new TaskEvent(kind, javaUnit, clazz);
        this.unit = null;
    }

    public JavafxTaskEvent(Kind kind, UnitTree unit) {
        this(kind, unit, null);
    }

    public JavafxTaskEvent(Kind kind, UnitTree unit, TypeElement clazz) {
        this.javacEvent = new TaskEvent(kind);
        this.unit = unit;
        this.clazz = clazz;
    }

    public Kind getKind() {
        return javacEvent.getKind();
    }

    public JavaFileObject getSourceFile() {
        return unit != null ? unit.getSourceFile() : javacEvent.getSourceFile();
    }

    public UnitTree getUnit() {
        return unit;
    }

    public CompilationUnitTree getCompilationUnit() {
        return javacEvent.getCompilationUnit();
    }

    public TypeElement getTypeElement() {
        return clazz != null ? clazz : javacEvent.getTypeElement();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("JavafxTaskEvent[");
        sb.append(getKind());
        sb.append(',');
        sb.append(getSourceFile());
        TypeElement type = getTypeElement();
        if (type != null) {
            sb.append(',');
            sb.append(type);
        }
        sb.append(']');
        return sb.toString();
    }
}
