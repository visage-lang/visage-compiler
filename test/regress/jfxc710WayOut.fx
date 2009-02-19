/**
 * @subtest jfxc710
 */

import java.lang.System;
import java.util.BitSet;

package class jfxc710WayOut extends BitSet {
   package var wayOuti = 'WayOuti'
       on replace { System.out.println('trig: wayOut1 {wayOuti}') }
   package function wayOutfi() : String { 'WayOutfi' }
   package function show() : Void {
	System.out.println( wayOuti );
	System.out.println( wayOutfi() );
   }
}

