/**
 * Regress test for JFXC-3416.
 *
 *  Binding behavior difference in 1.2.1 and Soma vs Marina
 *
 * @test
 * @run
 */

class RefObj {
    var running:Boolean;
}

var ref:RefObj = RefObj{};

var count = -1 on replace {
    println("<'count' on replace: now is {count}>");
    if (count >= 0) {
        println("creating new RefObj - count is {count}");
        ref = RefObj {
            running: false;
        };
    }
    println("set running to true  - count is {count}");
    ref.running = true;
}

var running:Boolean = bind ref.running on replace {
    println("<'running' on replace: now is {running}>");
    if (count <= 3) {
       println("recursion!");
       count++;
    }
}
count++;
println("START");
ref.running = false;
println("Running set to false...");
