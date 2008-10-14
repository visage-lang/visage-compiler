import javafx.lang.FX;
/*
 * FX.addShutdownMessage
 *
 * @test
 * @run
 */

function shutdown() {
 println("Test FX.addShutdownAction");
}

FX.addShutdownAction( shutdown )

