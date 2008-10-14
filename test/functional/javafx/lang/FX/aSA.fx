import javafx.lang.FX;
/*
 * FX.addShutdownMessage cannot be null
 *
 * @test
 * @run
 */

var exceptionmessage:String = "java.lang.NullPointerException: Action function can not be null";

try {
    FX.addShutdownAction( null )
    } catch ( npe:java.lang.NullPointerException ) {
      if(exceptionmessage.compareTo( npe.toString() ) != 0 )
             println(npe.toString());
    }

