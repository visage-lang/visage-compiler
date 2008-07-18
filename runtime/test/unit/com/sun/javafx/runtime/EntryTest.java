/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.javafx.runtime;

import com.sun.javafx.runtime.sequence.Sequence;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This originally tested that the default Entry implementation started on main.
 * Now this tests that the runtime provider in the workspace starts up on the AWT EDT.
 * @author tball
 */
public class EntryTest {
    
    public static class TestApp {
        // JavaFX Script main method
        public static Object javafx$run$(Sequence<String> __ARGS__) {
            assertEquals("Test was not started on main thread", 
                         "AWT-EventQueue-0", Thread.currentThread().getName());
            return null;
        }
    }

    @Test
    public void testEntry() throws Throwable {
        Entry.start(TestApp.class, null);
    }
}
