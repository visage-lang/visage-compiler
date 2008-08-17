/**
 * @subtest jfxc710
 */

import java.lang.System;

public class jfxc710Out extends jfxc710WayOut {
   override var wayOuti = 'ovOut-WayOuti'
       on replace { System.out.println('trig: override Out wayOuti {wayOuti}') }
   package var outi = 'Outi'
       on replace { System.out.println('trig: outi {outi}') }
   package function outfi() : String { 'Outfi' }
   package function show() : Void {
	super.show();
	System.out.println( outi );
	System.out.println( outfi() );
   }
}

