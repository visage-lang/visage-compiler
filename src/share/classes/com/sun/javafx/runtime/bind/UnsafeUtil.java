package com.sun.javafx.runtime.bind;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * UnsafeUtil
 *
 * @author Brian Goetz
 */
public class UnsafeUtil {
    public static Unsafe getUnsafe() {
        Unsafe unsafe = null;

        Class uc = Unsafe.class;
        Field[] fields = uc.getDeclaredFields();
        try {
            for (Field field : fields) {
                if (field.getName().equals("theUnsafe")) {
                    field.setAccessible(true);
                    unsafe = (Unsafe) field.get(uc);
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            return null;
        }
        return unsafe;
    }
}
