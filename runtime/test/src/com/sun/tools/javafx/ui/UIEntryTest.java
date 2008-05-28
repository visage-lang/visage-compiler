/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.tools.javafx.ui;

import com.sun.javafx.runtime.Entry;
import com.sun.javafx.runtime.sequence.Sequence;
import java.awt.EventQueue;
import org.junit.Test;
import javafx.ui.Button;
import static org.junit.Assert.*;

/**
 * Test Entry default implementation.
 * @author tball
 */
public class UIEntryTest {
    public UIEntryTest() {
        System.out.println("test");
    }
    
    public static class TestApp {
        Button widget = new Button();
        
        // JavaFX Script main method
        public static Object javafx$run$(Sequence<String> __ARGS__) {
            assertTrue("Test was not started on event dispatch thread", 
                       EventQueue.isDispatchThread());
            return null;
        }
    }

    @Test
    public void testEntry() throws Throwable {
        Entry.start(TestApp.class, null);
    }
}
