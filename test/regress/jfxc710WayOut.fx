/**
 * @subtest jfxc710
 */

import java.lang.System;

package class jfxc710WayOut {
   package var wayOuti = 'WayOuti'
       on replace { System.out.println('trig: wayOut1 {wayOuti}') }
   package function wayOutfi() : String { 'WayOutfi' }
   package function show() : Void {
	System.out.println( wayOuti );
	System.out.println( wayOutfi() );
   }
}

