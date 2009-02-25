/**
 * JFXC-2788 : Collapse bound conditional accesses into the single per-scipt BindingExpression
 *
 * cut-off and / or
 *
 * @test
 * @run
 */

var sw = false;
var tail = true;
function here(str : String, val : Boolean) : Boolean { println("  {str}"); val}

def ba = bind sw and here("[and]", tail);
def bo = bind sw or here("[or]", tail);
println("  and: {ba},  or: {bo}");
println("sw = true");
sw = true;
println("  and: {ba},  or: {bo}");
println("tail = false");
tail = false;
println("  and: {ba},  or: {bo}");
println("tail = true");
tail = true;
println("  and: {ba},  or: {bo}");
println("sw = false");
sw = false;
println("  and: {ba},  or: {bo}");
println("tail = false");
tail = false;
println("  and: {ba},  or: {bo}");
println("tail = true");
tail = true;
println("  and: {ba},  or: {bo}");
println("sw = true");
sw = true;
println("  and: {ba},  or: {bo}");
println("tail = false");
tail = false;
println("  and: {ba},  or: {bo}");
println("tail = true");
tail = true;
println("  and: {ba},  or: {bo}");

