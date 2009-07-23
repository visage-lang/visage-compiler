/**
 * JFXC-3337 : memory leak in bound for -- found in pursuit of RT-5120.
 *
 * Set maximum value expected by running the test on fixed javafx
 * runtime on Max OS X. The test fails with javafx runtime without the fix
 * for jfxc3337. FIXME: We may have to tune MAX_MEM for other platforms.
 *
 * @test
 * @run
 */

import java.lang.management.*; 

def MAX_MEM : Long = 900000; // 855464 on Mac OS X 10.5.7 with JDK 1.6.0_07

var initialMem : Long; 

var limi : Integer; 

class FakeLabel { 
} 

class FakeScene{ 
  public var content : FakeLabel[] 
} 

var t = FakeScene { 
   content: bind 
                for (i in [0..limi]) 
                    FakeLabel { 
                    } 
}; 

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
  for (n in [0..10000]) { 
    limi = n; 
  } 
  limi = -1; 
  checkMemory(MAX_MEM, "Possible leak via bound..for"); 
} 
