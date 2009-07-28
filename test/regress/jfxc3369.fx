/**
 * JFXC-3369 : Fix leaks caused by bound..if implementation.
 *
 * @test
 * @run
 */

import java.lang.management.*; 

def MAX_MEM : Long = 0; 

var initialMem : Long; 

function memUsed() : Long { 
  java.lang.System.gc(); 
  ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed(); 
} 

function checkMemory(maximum : Long, msg : String) : Void { 
  def used = memUsed() - initialMem; 
  if (used > maximum) { 
    println("{msg}: {used}"); 
  } 
} 

function run() { 
  initialMem = memUsed(); 
  var stuff = "hi"; 
  var cnt = 0; 
  while (++cnt < 100) { 
    var cnt2 = 0; 
    while (++cnt2 < 10000) { 
      var bo = bind if (true) stuff else stuff; 
    } 

    checkMemory(MAX_MEM, "possible leak in bound..if"); 
  } 
} 
