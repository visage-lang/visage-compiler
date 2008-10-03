/*
 * deferAction.fx
 *
 * Created on Wed Oct  1 15:39:44 PDT 2008
 *
 * @test
 * @run
 */


import javafx.lang.FX;
import java.lang.Exception;

/**
 * Example JavaFX Script showing the usage of FX.deferAction()
 *
 * Output from this script will be as follows:
 * {@code Starting Script}
 * {@code Running Deferred Action}
 * {@code Running Deferred Action2}
 * @author sgw
 */

// Show that we are starting the Script
java.lang.System.out.println("deferAction Script");
java.lang.System.out.println("Starting Script");

/*
 * Should throw an NPE here
 */
try {
    deferAction(null);
} catch (e: Exception) {
    java.lang.System.out.println("{e}");
}

/**
 * A void Function that takes no arguments will be run by the
 * JavaFX Script Runtime at a later time determined by the
 * implementation
 */
deferAction(function() : Void {
    java.lang.System.out.println("Running Deferred Action");
});

function action2(): Void {
    java.lang.System.out.println("Running Deferred Action2");
}

deferAction(action2);

/*
 * Nested DeferActions
 */
deferAction(function() : Void {
    java.lang.System.out.println("Running Deferred Action with Nesting");
    deferAction(function() : Void {
        java.lang.System.out.println("Running Nested Deferred Action");
    });
});

