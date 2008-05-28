/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.javafx.runtime;

import com.sun.javafx.runtime.sequence.Sequence;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test Entry default implementation.
 * @author tball
 */
public class EntryTest {
    
    public static class TestApp {
        // JavaFX Script main method
        public static Object javafx$run$(Sequence<String> __ARGS__) {
            assertEquals("Test was not started on main thread", 
                         "main", Thread.currentThread().getName());
            return null;
        }
    }

    @Test
    public void testEntry() throws Throwable {
        Entry.start(TestApp.class, null);
    }
}
