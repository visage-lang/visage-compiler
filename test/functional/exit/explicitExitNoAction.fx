/*
 * explicitExitNoAction.fx
 *
 * Created on Aug 26, 2008, 10:28:47 AM
 * @test
 * @run
 */


import javafx.lang.FX;

/**
 * Example JavaFX Script showing the usage of FX.addShutdownAction() with an
 * explicit FX.exit() call.
 * The exit occurs when the script when FX.exit is called.
 *
 * Output from this script will be as follows:
 * {@code Starting Script}
 * {@code Exiting Script}
 * {@code Running Exit Action}
 * @author sgw
 */

// Show that we are starting the Script
java.lang.System.out.println("explicitExitNoAction Script");
java.lang.System.out.println("Starting Script");

java.lang.System.out.println("Exiting Script");
/*
 * Script will now call FX.exit();
 */
FX.exit();
/*
 * FX.exit() should not return here
 */
java.lang.System.out.println("Should not reach here");
