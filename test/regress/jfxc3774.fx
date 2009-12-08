/*
 * Regression :  JFXC-3774 - names of public generated vars/funcs change in multiple file compile vs. single file compile.
 *
 * @compilefirst jfxc3774A.fx
 * @test
 */
public class jfxc3774 extends jfxc3774A { 
      protected var nodes:Integer[] on replace oldNodes[a..b] = newNodes { 
          println("newNodes = {newNodes}"); 
      } 
      var labeled = bind nodes; 
} 

var fred : Integer[]; 
var jjobj = jfxc3774{ labeled: bind fred }; 
