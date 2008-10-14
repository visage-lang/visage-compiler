import javafx.lang.FX;
/*
 * FX.addShutdownMessage - I suppose we should be able to return a java.lang.Void.
 *
 * @test
 * @run
 */

import java.lang.*;

function myshutdown() { var v:java.lang.Void; return v; }

FX.addShutdownAction( myshutdown )
