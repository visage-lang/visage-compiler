import java.lang.System;

/*
 *
 * JFXC-1938 ConcurrentModificationException is thrown if a variable is assgined in two different places simultaneously
 * @compilefirst jfxc1938a.fx
 * @test/fail
 *
 */

function run(args :String[]){    
  jfxc1938a.size=9;
  for (cell in jfxc1938a.grid) {System.out.println("x={cell.x} y={cell.y}");};
} 