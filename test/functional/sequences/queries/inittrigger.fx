/*
 * extended version of testcase for jfxc635
 *
 * trigger 1,2,5,10 do not fire
 *
 * when fixed, will need to change expected output :)
 *
 * @test
 * @run
 */

import java.lang.System;
public class InitTest {
    public attribute foo:Boolean = true on replace (old) {
        System.out.println("1. Replace Boolean old = {old} new = {foo}");
    }
    public attribute ifoo:Integer = 10 on replace (old) {
        System.out.println("2. Replace Integer old = {old} new = {ifoo}");
    }
    public attribute dfoo:Number = 20 on replace (old) {
        System.out.println("3. Replace Number old = {old} new = {dfoo}");
    }
    public attribute sfoo:String = "Hello, World!" on replace (old) {
        System.out.println("4. Replace Number old = {old} new = {sfoo}");
    }

    public attribute seqbfoo:Boolean[] = [false,false,false] on replace (old) {
        System.out.println("6. Replace Number old = {old} new = {seqbfoo}");
    }

    public attribute seqifoo:Integer[] = [1,2,3,4,5] on replace (old) {
        System.out.println("7. Replace Number old = {old} new = {seqifoo}");
    }

    public attribute seqnfoo:Number[] = [20,30,40] on replace (old) {
        System.out.println("8. Replace Number old = {old} new = {seqnfoo}");
    }

    public attribute seqsfoo:String[] = ["Hello",","," World!"] on replace (old) {
        System.out.println("9. Replace Number old = {old} new = {seqsfoo}");
    }

}

var i = InitTest {
    dfoo: 3.14
    foo:false
    ifoo: 0
    sfoo: "Hello, Dollie!"
    seqbfoo: [true,true,true]
    seqifoo: [ 6,7,8,9,10]
    seqnfoo: [ 100,200,300]
    seqsfoo: ["Mary", " had", " a", " little"," lamb."]
};
System.out.println("After Create");
System.out.println("i.foo = {i.foo}");
System.out.println("i.ifoo = {i.ifoo}");
System.out.println("i.dfoo = {i.dfoo}");
System.out.println("i.sfoo = {i.sfoo}");
System.out.println("i.seqbfoo = {i.seqbfoo}");
System.out.println("i.seqifoo = {i.seqifoo}");
System.out.println("i.seqnfoo = {i.seqnfoo}");
System.out.println("i.seqsfoo = {i.seqsfoo}");
