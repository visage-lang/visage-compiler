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

    public JavafxTaskEvent(Kind kind) {
        this.javacEvent = new TaskEvent(kind);
        this.unit = null;
    }

    public JavafxTaskEvent(Kind kind, JavaFileObject sourceFile) {
        this.javacEvent = new TaskEvent(kind, sourceFile);
        this.unit = null;
    }

    public JavafxTaskEvent(Kind kind, CompilationUnitTree javaUnit) {
        this.javacEvent = new TaskEvent(kind, javaUnit);
        this.unit = null;
    }

    public JavafxTaskEvent(Kind kind, CompilationUnitTree javaUnit, TypeElement clazz) {
        this.javacEvent = new TaskEvent(kind, javaUnit, clazz);
        this.unit = null;
    }

    public JavafxTaskEvent(Kind kind, UnitTree unit) {
        this.javacEvent = new TaskEvent(kind);
        this.unit = unit;
    }

    public JavafxTaskEvent(Kind kind, UnitTree unit, TypeElement clazz) {
        this.javacEvent = new TaskEvent(kind, null, clazz);
        this.unit = unit;
    }

    public Kind getKind() {
        return javacEvent.getKind();
    }

    public JavaFileObject getSourceFile() {
        return javacEvent.getSourceFile();
    }

    public UnitTree getUnit() {
        return unit;
    }

    public CompilationUnitTree getCompilationUnit() {
        return javacEvent.getCompilationUnit();
    }

    public TypeElement getTypeElement() {
        return javacEvent.getTypeElement();
    }

    public String toString() {
        return "JavafxTaskEvent["
            + getKind() + ","
            + getSourceFile() + ","
            // the compilation unit is identified by the file
            + "]";
    }
}
