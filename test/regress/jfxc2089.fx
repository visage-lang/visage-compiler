/*
 * jfxc2089.fx
 *
 * Created on Aug 26, 2008, 10:28:47 AM
 */


import javafx.lang.FX;

/**
 * Example JavaFX Script showing the usage of FX.addShutdownAction() with an
 * explicit FX.exit() call.
 * The exit occurs when the script when FX.exit is called.
 *
 * Output from this script will be as follows:
 * jfxc2089 Script
 * Exiting Script
 * Running Exit Action1
 * Running Exit Action2
 *
 * @test
 * @run
 * @author sgw
 */

// Show that we are starting the Script
println("jfxc2089 Script");

/*
 * A void Function that takes no arguments will be placed on the
 * exit action stack by the addShutdownAction to be executed at the
 * time the script exits either implicitly or explicitly.
 */
var runAtExit1 = function() : Void {
    println("Running Exit Action1");
}
var runAtExit2 = function() : Void {
    println("Running Exit Action2");
    FX.exit();
    println("Should not reach this - Exit Action2");
}

/*
 * Add the runAtExit function to the stack for the Shutdown Actions
 * this stack will be executed when the JavaFX Script exits.
 */
var ra1 = FX.addShutdownAction(runAtExit1);
var ra2 = FX.addShutdownAction(runAtExit2);

println("Exiting Script");
/*
 * Script will now call FX.exit();
 */
FX.exit();
/*
 * FX.exit() should not return here
 */
println("Should not reach here");
