/**
 * Fixed JFXC-3856 :  TextBox shows no cursor.
 *
 * @test
 * @run
 */

class path {
    var visible: Boolean;
}
var bindee1: Boolean;
def bindee2 = bind bindee1 
    on replace { 
        println("trigger: bindee2 = {bindee2}");
    }

var caret = path {
    // binding to bindee1 works ok
    override var visible = bind bindee2 
        on replace {println("trigger: visible = {visible}");}
}       

println("bindee2 = {bindee2}, caret.visible = {caret.visible}");
println("--1");
bindee1 = true;
println("bindee2 = {bindee2}, caret.visible = {caret.visible}");
