/**
 * JFXC-3180 : incorrect inInstanceContext for function value in on-replace block
 *
 * test other aspects of context
 *
 * @test
 * @run
 */

class jfxc3180edge {
  function toS(cxt : String, x : Integer) { "{cxt} : {x}" }

  var ts : function(: Integer) : String;

  var boop = "rodents" on replace {
    ts = function(z : Integer) {
      toS(boop, z)
    }
  }
}

def ed = jfxc3180edge {}
println(ed.ts(6));
ed.boop = "Luna";
println(ed.ts(44));
