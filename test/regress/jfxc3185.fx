/**
 * JFXC-3185 : "Cannot find symbol getRoot$$bound$()" error
 *
 * @test
 * @run
 */

class jfxc3185 {
  bound function getRoot() { "zork" }
  bound function zii() : String[] {
    if (getRoot() != null) getRoot() else "";
  }
}

def it = jfxc3185{}
println(it.zii())
