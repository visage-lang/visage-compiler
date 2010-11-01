/* Feature test #27 -- recursive null data structure
 * Demonstrates use of the null check dereference operator (!.)
 *
 * @test
 * @run
 */
class RecursiveNull {
    public var nullVar:RecursiveNull;
}

def rn:RecursiveNull = RecursiveNull {};

println(rn.nullVar.nullVar);
println(rn!.nullVar.nullVar);
println(rn!.nullVar!.nullVar);
