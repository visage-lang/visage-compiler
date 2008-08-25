/**
 * Regression test JFXC-1862 : 'var' declaration as last statement of function generates bad code
 *
 * @test
 * @run
 */

function run() {
   var x = 4
}
