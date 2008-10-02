/*
 * implicitExitNoAction.fx
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
java.lang.System.out.println("implicitExitNoAction Script");
java.lang.System.out.println("Starting Script");


java.lang.System.out.println("Exiting Script");
/*
 * Script will now have FX.exit() called implicitly
 */
