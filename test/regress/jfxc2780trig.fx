/**
 * JFXC-2780 : Can't compile when "bind rect.boundsInLocal.minX" in a function
 *
 * derivative test case
 *
 * @test
 */

function test(yo : Boolean)  {
    var x = 4 on replace { if (yo) println("hi") }
}
