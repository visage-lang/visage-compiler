/*
 * jfxc2088.fx
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
 * jfxc2088 Script
 * Correctly Failed to Add Exit3
 * Exiting Script
 * Running Exit Action6
 * Running Exit Action5
 * Running Exit Action3
 * Running Exit Action4
 *
 * @test
 * @run
 *
 * @author sgw
 */

// Show that we are starting the Script
println("jfxc2088 Script");

/*
 * A void Function that takes no arguments will be placed on the
 * exit action stack by the addShutdownAction to be executed at the
 * time the script exits either implicitly or explicitly.
 */
var runAtExit1 = function() : Void {
    println("Running Exit Action1");
}
var runAtExit2 = function() : Void {
    FX.exit();
    println("Running Exit Action2");
}
var runAtExit3 = function() : Void {
    println("Running Exit Action3");
}
var runAtExit4 = function() : Void {
    println("Running Exit Action4");
}
var runAtExit5 = function() : Void {
    println("Running Exit Action5");
}

var runTest = function() : Void {
    println("Running Exit Action6");
}

/*
 * Add the runAtExit function to the stack for the Shutdown Actions
 * this stack will be executed when the JavaFX Script exits.
 */
var ra1 = FX.addShutdownAction(runAtExit1);
FX.removeShutdownAction(ra1);

var ra2 = FX.addShutdownAction(runAtExit2);
var ra3 = FX.addShutdownAction(runAtExit3);
var ra4 = FX.addShutdownAction(runAtExit4);
var ra5 = FX.addShutdownAction(runAtExit5);

FX.removeShutdownAction(ra3);
ra3 = FX.addShutdownAction(runAtExit3);

FX.removeShutdownAction(ra5);
ra5 = FX.addShutdownAction(runAtExit5);

var rt = FX.addShutdownAction(runTest);
FX.removeShutdownAction(rt);
rt = FX.addShutdownAction(runTest);
ra3 = FX.addShutdownAction(runAtExit3);
if(FX.addShutdownAction(runAtExit3) == ra3) {
    println("Correctly Failed to Add Exit3");
}


println("Exiting Script");
/*
 * Script will now call FX.exit();
 */
FX.exit();
/*
 * FX.exit() should not return here
 */
println("Should not reach here");
