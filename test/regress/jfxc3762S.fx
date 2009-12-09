/**
 * Regression test JFXC-3762 : Compiled bind:runtime IllegalArgumentException: no such variable: 0 on Button{}
 *
 * @subtest
 */

public def CSS: String[] = ["hi", jfxc3762M.CSS]; 
public class jfxc3762S extends jfxc3762M { 
}
