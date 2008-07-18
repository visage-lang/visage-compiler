/**
 * regression test: JFXC-1224 : Compiler error when converting indexof value to int inside function attribute definitions
 *
 * @test
 * @run
 */

import java.lang.System;

var series = [ "One", "two", "three" ];

class CBox {
   attribute onChange : function(newValue:Boolean) : Integer;
}

var ff =  bind for (s in series)
    CBox {
         onChange: function(newValue:Boolean) : Integer {
             var t:Integer = indexof s; // didn't compile
             t
         }
    }

System.out.println(ff[1].onChange(false))
