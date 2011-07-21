import java.lang.System;
/*
 * JFXC-1938 ConcurrentModificationException is thrown if a variable is assgined in two different places simultaneously
 *
 * @subtest
 * 
 *
*/



package class jfxc1938a {package var x: Number; package var y: Number;}

package var size = 4;
protected var grid = bind for (row in [1..size], col in [1..size]) jfxc1938a {x: col, y: row};

function run(){	        
	for (cell in grid) {System.out.println("x={cell.x}  y={cell.y}");};
} 