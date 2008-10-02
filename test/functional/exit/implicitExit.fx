/*
 * implicitExit.fx
 *
 * Created on Aug 26, 2008, 10:28:47 AM
 * @test
 * @run
 */


import javafx.lang.FX;

/**
 * Example JavaFX Script showing the usage of FX.addShutdownAction() with an implicit exit.
 * The exit occurs when the script (which does not contain any event loop) runs off the
 * end of the script.
 *
 * Output from this script will be as follows:
 * {@code Starting Script}
 * {@code Exiting Script}
 * {@code Running Exit Action}
 * @author sgw
 */

// Show that we are starting the Script
java.lang.System.out.println("implicitExit Script");
java.lang.System.out.println("Starting Script");

/*
 * A void Function that takes no arguments will be placed on the
 * exit stack list by the addShutdownAction to be executed at the
 * time the script exits either implicitly or explicitly.
 */
var runAtExit = function(): Void {
    java.lang.System.out.println("Running Exit Action");
}

/*
 * Add the runAtExit function to the stack for the Shutdown Action
 * this action stack will be executed when the JavaFX Script exits.
 */
FX.addShutdownAction(runAtExit);


java.lang.System.out.println("Exiting Script");
/*
 * Script will now have FX.exit() called implicitly
 */
