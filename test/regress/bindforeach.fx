/**
 * regression test:  bind foreach
 * @test
 * @run
 */

import java.lang.System;

class Cell {attribute x: Number; attribute y: Number;}

var size = 10;
var grid = bind foreach (row in [1..size], col in [1..size]) Cell {x: col, y: row};
size = 4;
foreach (cell in grid) {System.out.println("x={cell.x} y={cell.y}");};
