/**
 * @subtest jfxc710
 */

import java.lang.System;

class jfxc710Out extends jfxc710WayOut {
   override attribute wayOuti = 'ovOut-WayOuti'
       on replace { System.out.println('trig: override Out wayOuti {wayOuti}') }
   attribute outi = 'Outi'
       on replace { System.out.println('trig: outi {outi}') }
   static attribute outs = 'Outs';
   function outfi() : String { 'Outfi' }
   static function outfs() : String { 'Outfs' }
   function show() : Void {
	super.show();
	System.out.println( outi );
	System.out.println( outs );
	System.out.println( outfi() );
	System.out.println( outfs() );
   }
}

