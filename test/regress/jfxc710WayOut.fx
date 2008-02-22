/**
 * @subtest jfxc710
 */

import java.lang.System;

class jfxc710WayOut {
   attribute wayOuti = 'WayOuti'
       on replace { System.out.println('trig: wayOut1 {wayOuti}') }
   static attribute wayOuts = 'WayOuts';
   function wayOutfi() : String { 'WayOutfi' }
   static function wayOutfs() : String { 'WayOutfs' }
   function show() : Void {
	System.out.println( wayOuti );
	System.out.println( wayOuts );
	System.out.println( wayOutfi() );
	System.out.println( wayOutfs() );
   }
}

