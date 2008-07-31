/**
 * @subtest jfxc710
 */

import java.lang.System;

class jfxc710WayOut {
   var wayOuti = 'WayOuti'
       on replace { System.out.println('trig: wayOut1 {wayOuti}') }
   function wayOutfi() : String { 'WayOutfi' }
   function show() : Void {
	System.out.println( wayOuti );
	System.out.println( wayOutfi() );
   }
}

