/**
 * JFXC-3106 - Sequence variable with public-init modifier can be updated 
 * outside of script file.
 *
 * @compilefirst jfxc3106Test.fx
 * @test/compile-error
 */

var obj = jfxc3106Test {
    seq : ["a","b"]
};

// compiler should issue an error for this
// because jfxc3106Test.seq is a "public-init"
// member of jfxc3106Test class.

obj.seq[0..1] = ["b", "c"]; 
