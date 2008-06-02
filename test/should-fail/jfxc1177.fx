/* Test for a cycle involing the module class.
 * @test/warning
 */

class jfxc1177 extends jfxc1177.A{}
class A extends jfxc1177{}
