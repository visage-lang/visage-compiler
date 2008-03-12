/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.tools.javafx.ui;

import com.sun.javafx.runtime.RuntimeProvider;
import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * Entry point for the Swing-based JavaFX UI runtime library.  This class
 * ensures that applications using this library are run on the AWT 
 * EventDispatchThread.
 * 
 * @author Tom Ball
 */
public class UIRuntimeProvider implements RuntimeProvider {

    public boolean usesRuntimeLibrary(Class mainClass) {
        try {
            String resName = mainClass.getName().replace('.', '/') + ".class";
            InputStream res = mainClass.getClassLoader().getResourceAsStream(resName);
            DataInputStream classFile = new DataInputStream(res);

            // skip classfile header
            int magic = classFile.readInt();
            if (magic != 0xCAFEBABE) {
                throw new ClassFormatError();
            }
            classFile.readShort(); // minor version
            classFile.readShort(); // major version
            int constantPoolCount = classFile.readUnsignedShort();

            // scan constant pool for javafx.ui class and package references
            for (int i = 1; i < constantPoolCount; i++) { // constantPool[0] reserved
                byte type = classFile.readByte();
                if (hasJavaFXUIReference(type, classFile))
                    return true;
                if (type == CONSTANT_Double || type == CONSTANT_Long)
                    i++;  // doubles and longs take two constant pool slots
            }
            return false;
        } catch (IOException ex) {
            return false;
        }
    }

    public Object run(final Method entry, final String... args) throws Throwable {
        Throwable error = null;
        try {
            final Object[] result = new Object[1];
            EventQueue.invokeAndWait(new Runnable() {

                public void run() {
                    try {
                        result[0] = entry.invoke(null);
                    } catch (Exception e) {
                        result[0] = e.getCause();
                    }
                }
            });
            if (result[0] instanceof Throwable)
                error = (Throwable)result[0];
            else
                return result[0];
        } catch (Exception e) {
            error = e;
        }
        assert error != null;
        StackTraceElement[] stack = error.getStackTrace();
        int n = 0;
        while (n < stack.length) {
            if (stack[n++].getMethodName().equals(entry.getName()))
                break;
        }
        StackTraceElement[] shortStack = new StackTraceElement[n];
        System.arraycopy(stack, 0, shortStack, 0, n);
        error.setStackTrace(shortStack);
        throw error;
    }
    
    // Constant Type enums (JVM spec table 4.3)
    static final int CONSTANT_Utf8 = 1;
    static final int CONSTANT_Integer = 3;
    static final int CONSTANT_Float = 4;
    static final int CONSTANT_Long = 5;
    static final int CONSTANT_Double = 6;
    static final int CONSTANT_Class = 7;
    static final int CONSTANT_String = 8;
    static final int CONSTANT_FieldRef = 9;
    static final int CONSTANT_MethodRef = 10;
    static final int CONSTANT_InterfaceMethodRef = 11;
    static final int CONSTANT_NameAndType = 12;

    private boolean hasJavaFXUIReference(byte type, DataInputStream dis)
            throws IOException {
        switch (type) {
          case CONSTANT_Utf8: {
              String s = dis.readUTF();
              if (s.startsWith("javafx/ui/")
                      || s.startsWith("Ljavafx/ui/")
                      || s.startsWith("javafx/gui")
                      || s.startsWith("Ljavafx/gui"))
                  return true;
              break;
          }
          case CONSTANT_Integer:
              dis.readInt();
              break;
          case CONSTANT_Float:
              dis.readFloat();
              break;
          case CONSTANT_Long:
              dis.readLong();
              break;
          case CONSTANT_Double:
              dis.readDouble();
              break;
          case CONSTANT_Class: {
              dis.readUnsignedShort();
              break;
          }
          case CONSTANT_String: {
              dis.readUnsignedShort();
              break;
          }
          case CONSTANT_FieldRef: {
              int classIndex = dis.readUnsignedShort();
              int natIndex = dis.readUnsignedShort();
              break;
          }
          case CONSTANT_MethodRef: {
              int classIndex = dis.readUnsignedShort();
              int natIndex = dis.readUnsignedShort();
              break;
          }
          case CONSTANT_InterfaceMethodRef: {
              int classIndex = dis.readUnsignedShort();
              int natIndex = dis.readUnsignedShort();
              break;
          }
          case CONSTANT_NameAndType: {
              int nameIndex = dis.readUnsignedShort();
              int descIndex = dis.readUnsignedShort();
              break;
          }
          default:
              throw new ClassFormatError();
        }
        return false;
    }
}
